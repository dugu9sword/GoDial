package godial.kernel;

import godial.act.ActType;
import godial.act.ActUnit;
import godial.act.SystemAct;
import godial.act.UserAct;
import godial.context.Context;
import godial.dialstructure.DialEleType;
import godial.dialstructure.DialElement;
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
    private Domain lastDomain;
    private HashMap state;

    public static final String LAST_SYSTEM_ACT = "LAST_SYSTEM_ACT";
    public static final String LAST_USER_ACTS = "LAST_USER_ACTS";
    //    public static final String LAST_DOMAIN = "LAST_DOMAIN";
    public static final String REQUESTING_DIAL_ELEMENT = "REQUESTING_DIAL_ELEMENT";
    public static final String CLARIFYING_YES_OR_NO = "CLARIFYING_YES_OR_NO";
    public static final String CLARIFYING_OPTION = "CLARIFYING_OPTION";
    public static final String DOMAIN_FINISHED = "DOMAIN_FINISHED";


    public Kernel() {
        domainContextHashMap = new HashMap<>();
        domains = new ArrayList<>();
        state = new HashMap();
    }

    public void registerDomain(Domain domain) {
        domain.setKernel(this);
        domains.add(domain);
    }

    public Context getContextByDomain(Domain domain) {
        return domainContextHashMap.get(domain);
    }

    public void init() {
        log.info("[init]\tBefore input, state are filled with " + state.keySet());
        log.info("[init]\tBefore input, contexts are filled with " + domainContextHashMap.keySet());
        if (state.containsKey(DOMAIN_FINISHED)) {
            lastDomain = null;
            state.clear();
        }
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
        if (lastDomain == null)
            log.info("[Convert]\tLast domain NOT exists");

        DialEleType utterType = NameEntityRecognizer.convert(utterance);
        log.info("[Convert]\tName entity is " + utterType);

        if (state.containsKey(REQUESTING_DIAL_ELEMENT)) {
            DialElement dialElement = (DialElement) state.get(REQUESTING_DIAL_ELEMENT);
            if (utterType == dialElement.type) {
                log.info("[Convert]\tCurrently requesting" + utterType);
                UserAct userAct = new UserAct();
                userAct.setDomain(lastDomain);
                userAct.addActUnit(new ActUnit(ActType.INFORM, dialElement.slot, utterance));
                userActs.add(userAct);
            }
        }

        if (state.containsKey(CLARIFYING_YES_OR_NO)) {
            if (utterType == DialEleType.CONFIRMATION || utterType == DialEleType.CANCELLATION) {
                log.info("[Convert]\tCurrently requesting" + utterType);
                UserAct userAct = new UserAct();
                userAct.setDomain(Domain.SYSTEM);
                userAct.addActUnit(new ActUnit(utterType == DialEleType.CONFIRMATION ? ActType.INFORM : ActType.CANCEL,
                        null,
                        null));
                userActs.add(userAct);
            }
        }

        if (state.containsKey(CLARIFYING_OPTION)) {
            if (utterType == DialEleType.NUMBER) {
                log.info("[Convert]\tCurrently requesting" + utterType);
                UserAct userAct = new UserAct();
                userAct.setDomain(Domain.SYSTEM);
                userAct.addActUnit(new ActUnit(ActType.SELECT,
                        null,
                        utterance));
                userActs.add(userAct);
            }
        }

        for (Domain domain : domains) {
            UserAct userAct = domain.convert(utterance);
            userAct.setDomain(domain);
            userActs.add(userAct);
        }

        return userActs;
    }

    public SystemAct react(ArrayList<UserAct> userActs) {
        if (lastDomain == null)
            log.info("[React]\tLast domain NOT exists");

        ArrayList<UserAct> validUserActs = new ArrayList<>();
        for (UserAct userAct : userActs)
            if (userAct != UserAct.NONE)
                validUserActs.add(userAct);

        SystemAct systemAct;
        switch (validUserActs.size()) {
            case 0:
                log.info("[React]\tFaced with NONE");
                systemAct = SystemAct.NONE;
                break;
            case 1:
                log.info("[React]\tFaced with a single user act");
                UserAct userAct = validUserActs.get(0);
                systemAct = handle(userAct);
                break;
            default:
                log.info("[React]\tFaced with " + validUserActs.size() + " valid user acts");
                systemAct = new SystemAct();
                // the first userAct in the list will be the most preferred
                userActs.sort((UserAct o2, UserAct o1) -> {
                    if (o1.getDomain() == lastDomain)
                        return 1;
                    else if (o2.getDomain() == lastDomain)
                        return -1;
                    else
                        return o1.getActUnits().size() - o2.getActUnits().size();
                });
                state.put(LAST_USER_ACTS, userActs);
                StringBuffer sb = new StringBuffer();
                sb.append("Sorry, but what do you mean? You may select one of the following options:\n");
                for (int i = 0; i < userActs.size(); i++)
                    sb.append(i + ".\t" + userActs.get(i).toString());  //  TODO: How to represent user act?
                systemAct.addActUnit(new ActUnit(ActType.CLARIFY_OPTION, null, sb.toString()));
                break;
        }
        return systemAct;
    }

    public SystemAct handle(UserAct userAct) {
        SystemAct systemAct = new SystemAct();
        ArrayList<ActUnit> actUnits = userAct.getActUnits();
        systemAct.setDomain(userAct.getDomain());
        for (ActUnit actUnit : actUnits) {
            switch (actUnit.actType) {
                case INFORM:
                    systemAct.addActUnit(new ActUnit(ActType.GROUND, actUnit.slot, actUnit.value));
                    if (!userAct.getDomain().hasCorrespondingContext()) {
                        Context context = new Context(userAct.getDomain().getDialStructure());
                        domainContextHashMap.put(userAct.getDomain(), context);
                        log.info("[Handle]\tA new context is created, and now there are " + domains.size() +
                                " domains and " + domainContextHashMap.size() + " contexts");
                    }
                    break;
                case SELECT:
                    int chosenAct = Integer.parseInt(actUnit.value);
                    ArrayList<UserAct> lastUserActs = (ArrayList<UserAct>) state.get(LAST_USER_ACTS);
                    if (chosenAct <= lastUserActs.size())
                        systemAct = handle(lastUserActs.get(chosenAct - 1));
                    break;
                case CONFIRM:

                    break;
                case CANCEL:

                    break;
            }
        }
        lastDomain = systemAct.getDomain();
        return systemAct;
    }

    public void execute(SystemAct systemAct) {
        ArrayList<ActUnit> actUnits = systemAct.getActUnits();
        for (ActUnit actUnit : actUnits) {
            switch (actUnit.actType) {
                case GROUND:
                    systemAct.getDomain().correspondingContext().setSlot(actUnit.slot, actUnit.value);

                    log.info("[Execute]\tSystem act will fill the slot " + actUnit.slot + " with " + actUnit.value);
                    break;
            }
        }
        if (systemAct.getDomain().correspondingContext().hasUnfilledDialElement()) {
            log.info("[Execute]\tPut a new REQ_DIA_ELEMENT " + systemAct.getDomain().correspondingContext().nextUnfilledDialElement());
            state.put(REQUESTING_DIAL_ELEMENT, systemAct.getDomain().correspondingContext().nextUnfilledDialElement());
        } else {
            state.put(DOMAIN_FINISHED, lastDomain);
        }
    }


    public String work(String utterance) {

        init();

        ArrayList<UserAct> userActs = convert(utterance);

        SystemAct systemAct = react(userActs);

        String systemUtterance;

        if (systemAct != SystemAct.NONE) {
            execute(systemAct);

            systemUtterance = lastDomain.generate(systemAct);   // TODO:Maybe something wrong

            // If a context has been closed, reset the current state.
            if (!lastDomain.correspondingContext().hasUnfilledDialElement()) {
                domainContextHashMap.remove(lastDomain);
                state.clear();
            }
        } else
            systemUtterance = "Hello, what can i do for you?";


        return systemUtterance;
    }
}
