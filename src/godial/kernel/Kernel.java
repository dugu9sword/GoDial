package godial.kernel;

import godial.act.ActType;
import godial.act.ActUnit;
import godial.act.SystemAct;
import godial.act.UserAct;
import godial.context.Context;
import godial.dialstructure.DialEleType;
import godial.domain.Domain;
import godial.utils.NameEntityRecognizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-24.
 */
public class Kernel implements IKernel {

    static Log log = LogFactory.getLog(Kernel.class);

    private ArrayList<Domain> domains;
    private HashMap<Domain, Context> domainContextHashMap;

    private ArrayList<Domain> domainHistory;
    private SystemAct lastSystemAct;
    private ArrayList<UserAct> lastUserActs;
    private int chosenAct;
    private HashMap state;

    private Domain getLastDomain() {
        return domainHistory.size() == 0 ? null : domainHistory.get(0);
    }


    public Kernel() {
        domainContextHashMap = new HashMap<>();
        domains = new ArrayList<>();
        domainHistory = new ArrayList<>();

    }

    public void registerDomain(Domain domain) {
        domain.setKernel(this);
        domains.add(domain);
        domainHistory.add(domain);
    }

    public Context getContextByDomain(Domain domain) {
        return domainContextHashMap.get(domain);
    }

    public void execute(SystemAct systemAct) {
        ArrayList<ActUnit> actUnits = systemAct.getActUnits();
        for (ActUnit actUnit : actUnits) {
            switch (actUnit.actType) {
                case GROUND:
                    systemAct.getContext().setSlot(actUnit.slot, actUnit.value);

                    log.info("System act will fill the slot " + actUnit.slot + " with " + actUnit.value);
                    break;

            }
        }
        if(systemAct.getContext().hasUnfilledSlot())
            systemAct.addActUnit(new ActUnit(ActType.REQUEST,systemAct.getContext().nextUnfilledSlot(),null));

    }

    /**
     * This method is used to employ converters of existing domains to convert the utterance to userActs.
     * We may employ the {@link godial.utils.NameEntityRecognizer} as a utility to recognize the name entity,
     * for abbreviate, we call it NER in the following text.
     * <p>
     * Several cases shall be taken into considerations:
     * 1. If the last systemAct is to REQUEST the value of a slot, the NER is employed to recognize the value
     * and set the type of userAct as INFORM.
     * 2. If the last systemAct is to CLARIFY_YES_NO the purpose of the user, the NER is employed to judge whether the
     * utterance is to CONFIRM or CANCEL some action.
     * 3. If the last systemAct is to CLARIFY_OPTION the purpose of the user, the NER is employed to recognize which
     * kind of action the user want to SELECT.
     *
     * @param utterance
     * @return
     */
    public ArrayList<UserAct> convert(String utterance) {
        ArrayList<UserAct> userActs = new ArrayList<>();

        DialEleType utterType = NameEntityRecognizer.convert(utterance);
        if(lastSystemAct!=null) {
            log.info("Name entity is "+utterType);
            UserAct userAct = new UserAct();
            userAct.setContext(getLastDomain().correspondingContext());
            if (lastSystemAct.isRequesting() && utterType == lastSystemAct.requestingDialElement().type) {
                log.info("Name entity detected");
                userAct.addActUnit(new ActUnit(
                        ActType.INFORM,
                        lastSystemAct.requestingDialElement().slot,
                        utterance));
            }
            if (lastSystemAct.isClarifyingYesOrNo() && utterType == DialEleType.CONFIRMATION || utterType == DialEleType.CANCELLATION) {
                userAct.addActUnit(new ActUnit(
                        utterType == DialEleType.CONFIRMATION ? ActType.CONFIRM : ActType.CANCEL,
                        null,
                        null));
            }
            if (lastSystemAct.isClarifyingOptionally() && utterType == DialEleType.NUMBER) {
                log.info("Option detected");
                userAct.addActUnit(new ActUnit(
                        ActType.SELECT,
                        null,
                        utterance));
            }
            if(userAct.getActUnits().size()!=0)
                userActs.add(userAct);
        }

        for (Domain domain : domains) {
            UserAct userAct = domain.convert(utterance);
            if (userAct != UserAct.NONE) {
                if (domain.hasCorrespondingContext())
                    userAct.setContext(domain.correspondingContext());
                else {
                    Context context = new Context(domain.getDialStructure());
                    domainContextHashMap.put(domain, context);
                    userAct.setContext(context);
                    log.info("A new context is created, and now there are " + domains.size() +
                            " domains and " + domainContextHashMap.size() + " contexts");
                }
            }
            userActs.add(userAct);
        }
        return userActs;
    }

    public SystemAct react(ArrayList<UserAct> userActs) {
        ArrayList<UserAct> validUserActs = new ArrayList<>();
        for (UserAct userAct : userActs)
            if (userAct != UserAct.NONE)
                validUserActs.add(userAct);

        SystemAct systemAct;
        switch (validUserActs.size()) {
            case 0:
                log.info("Faced with NONE");
                systemAct = SystemAct.NONE;
                break;
            case 1:
                log.info("Faced with a single user act");
                UserAct userAct = validUserActs.get(0);
                systemAct=handle(userAct);
                break;
            default:
                log.info("Faced with "+validUserActs.size()+" valid user acts");
                systemAct = new SystemAct();
                // the first userAct in the list will be the most preferred
                userActs.sort((UserAct o2,UserAct o1)->{
                    if(o1.getContext()==getLastDomain().correspondingContext())
                        return 1;
                    else if(o2.getContext()==getLastDomain().correspondingContext())
                        return -1;
                    else
                        return o1.getActUnits().size()-o2.getActUnits().size();
                });
                lastUserActs=userActs;
                StringBuffer sb=new StringBuffer();
                sb.append("Sorry, but what do you mean? You may select one of the following options:\n");
                for(int i=0;i<userActs.size();i++)
                    sb.append(i+".\t"+userActs.get(i).toString());  //  TODO: How to represent user act?
                systemAct.addActUnit(new ActUnit(ActType.CLARIFY_OPTION, null, sb.toString()));
                break;
        }
        lastSystemAct=systemAct;
        log.info("Last System Act Updated to "+systemAct);
        return systemAct;
    }

    public SystemAct handle(UserAct userAct){
        SystemAct systemAct=new SystemAct();
        ArrayList<ActUnit> actUnits = userAct.getActUnits();
        systemAct.setContext(userAct.getContext());
        for (ActUnit actUnit : actUnits) {
            switch (actUnit.actType) {
                case INFORM:
                    systemAct.addActUnit(new ActUnit(ActType.GROUND, actUnit.slot, actUnit.value));
                    break;
                case SELECT:
                    chosenAct=Integer.parseInt(actUnit.value);
                    if(chosenAct>lastUserActs.size())
                        systemAct=lastSystemAct;
                    else
                        systemAct=handle(lastUserActs.get(chosenAct));
                    break;
                case CONFIRM:

                    break;
                case CANCEL:

                    break;
            }
        }
        return systemAct;
    }

    public String work(String utterance) {

        ArrayList<UserAct> userActs = convert(utterance);

        SystemAct systemAct = react(userActs);

        execute(systemAct);

        String systemUtterance;
        if(systemAct==SystemAct.NONE){
            systemUtterance="Hello, what can i do for you?";
        }else {
            systemUtterance = getLastDomain().generate(systemAct);   // TODO:Maybe something wrong

            if (!getLastDomain().correspondingContext().hasUnfilledSlot()) {
                domainContextHashMap.remove(getLastDomain());

                lastSystemAct = null;
                lastUserActs = null;
                chosenAct = 0;
            }
        }

        return systemUtterance;
    }
}
