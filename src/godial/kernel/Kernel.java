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

    private static final String LAST_USER_ACTS = "LAST_USER_ACTS";
    private static final String REQUESTING_DIAL_ELEMENT = "REQUESTING_DIAL_ELEMENT";
    private static final String CLARIFYING_YES_OR_NO = "CLARIFYING_YES_OR_NO";
    private static final String CLARIFYING_OPTION = "CLARIFYING_OPTION";
    private static final String IS_RESPONDING_TO_SYSTEM = "IS_RESPONDING_TO_SYSTEM";
    private static final String DOMAIN_FINISHED = "DOMAIN_FINISHED";
    private static final String DOMAIN_CREATED = "DOMAIN_CREATED";

    static Log log = LogFactory.getLog(Kernel.class);
    private ArrayList<Domain> domains;
    private HashMap<Domain, Context> domainContextHashMap;
    private Domain lastDomain;
    private HashMap globalState;


    public Kernel() {
        domainContextHashMap = new HashMap<>();
        domains = new ArrayList<>();
        globalState = new HashMap();
    }

    public void registerDomain(Domain domain) {
        domain.setKernel(this);
        domains.add(domain);
    }

    public Context getContextByDomain(Domain domain) {
        return domainContextHashMap.get(domain);
    }

    private void clearStateIfSet(String state) {
        if (globalState.containsKey(state))
            globalState.remove(state);
    }

    public void init() {
        log.info("[init]\tBefore input, globalState are filled with " + globalState.keySet());
        log.info("[init]\tBefore input, contexts are filled with " + domainContextHashMap.keySet());

        if (globalState.containsKey(DOMAIN_FINISHED)) {
            lastDomain = null;
            globalState.clear();
        }
        clearStateIfSet(DOMAIN_CREATED);
        clearStateIfSet(IS_RESPONDING_TO_SYSTEM);
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
        else
            log.info("[Convert]\tLast domain is " + lastDomain);

        DialEleType utterType = NameEntityRecognizer.convert(utterance);
        log.info("[Convert]\tName entity is " + utterType);

        boolean isRespondingToSystem = false;

        if (globalState.containsKey(REQUESTING_DIAL_ELEMENT)) {
            DialElement dialElement = (DialElement) globalState.get(REQUESTING_DIAL_ELEMENT);
            if (utterType == dialElement.type) {
                log.info("[Convert]\tCurrently requesting " + utterType);
                UserAct userAct = new UserAct();
                userAct.setDomain(lastDomain);
                userAct.addActUnit(new ActUnit(ActType.INFORM, dialElement.slot, utterance));
                userActs.add(userAct);
                isRespondingToSystem = true;
            } else
                globalState.remove(REQUESTING_DIAL_ELEMENT);
        } else if (globalState.containsKey(CLARIFYING_YES_OR_NO)) {
            if (utterType == DialEleType.CONFIRMATION || utterType == DialEleType.CANCELLATION) {
                log.info("[Convert]\tCurrently requesting " + utterType);
                UserAct userAct = new UserAct();
                userAct.setDomain(Domain.SYSTEM);
                userAct.addActUnit(new ActUnit(utterType == DialEleType.CONFIRMATION ? ActType.INFORM : ActType.CANCEL,
                        null,
                        null));
                userActs.add(userAct);
                isRespondingToSystem = true;
            } else
                globalState.remove(CLARIFYING_YES_OR_NO);
        } else if (globalState.containsKey(CLARIFYING_OPTION)) {
            if (utterType == DialEleType.NUMBER) {
                log.info("[Convert]\tCurrently requesting " + utterType);
                UserAct userAct = new UserAct();
                userAct.setDomain(Domain.SYSTEM);
                userAct.addActUnit(new ActUnit(ActType.SELECT,
                        null,
                        utterance));
                userActs.add(userAct);
                isRespondingToSystem = true;
            } else
                globalState.remove(CLARIFYING_OPTION);
        }

        if (!isRespondingToSystem) {
            for (Domain domain : domains) {
                UserAct userAct = domain.convert(utterance);
                userAct.setDomain(domain);
                userActs.add(userAct);
            }
        } else
            globalState.put(IS_RESPONDING_TO_SYSTEM, null);

        return userActs;
    }

    /**
     * In two cases will the user act be handled:
     * The first case is that the domain is triggered;
     * The second case is that the domain has corresponding context.
     * If both of the above are not satisfied, the user act will be ignored.
     *
     * @param userActs
     * @return
     */
    public SystemAct react(ArrayList<UserAct> userActs) {
        if (lastDomain == null)
            log.info("[React]\tLast domain NOT exists");

        if (globalState.containsKey(IS_RESPONDING_TO_SYSTEM)) {
            return handle(userActs.get(0));
        }

        ArrayList<UserAct> validUserActs = new ArrayList<>();
        for (UserAct userAct : userActs) {
            boolean isTriggered = false;
            for (ActUnit actUnit : userAct.getActUnits())
                if (actUnit.actType == ActType.TRIGGER)
                    isTriggered = true;
            if (isTriggered || userAct.getDomain().hasCorrespondingContext() && userAct != UserAct.NONE)
                validUserActs.add(userAct);
        }


        log.info("[React]\tTotally " + validUserActs.size() + " valid user acts, they are " + validUserActs);

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
                systemAct.setDomain(Domain.SYSTEM);
                // the first userAct in the list will be the most preferred
                validUserActs.sort((UserAct o2, UserAct o1) -> {
                    if (o1.getDomain() == lastDomain)
                        return 1;
                    else if (o2.getDomain() == lastDomain)
                        return -1;
                    else
                        return o1.getActUnits().size() - o2.getActUnits().size();
                });
                StringBuffer sb = new StringBuffer();
                sb.append("Sorry, but what do you mean? You may select one of the following options:\n");
                for (int i = 0; i < validUserActs.size(); i++)
                    sb.append("\t" + (i + 1) + ".\t" + validUserActs.get(i).getDomain() + "\t" + validUserActs.get(i).toString() + "\n");  //  TODO: How to represent user act?
                globalState.put(CLARIFYING_OPTION, sb.toString());
                globalState.put(LAST_USER_ACTS, validUserActs);
                lastDomain = Domain.SYSTEM;
//                systemAct.addActUnit(new ActUnit(ActType.CLARIFY_OPTION, null, sb.toString()));
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
                    ArrayList<UserAct> lastUserActs = (ArrayList<UserAct>) globalState.get(LAST_USER_ACTS);
                    if (chosenAct <= lastUserActs.size())
                        systemAct = handle(lastUserActs.get(chosenAct - 1));
                    globalState.remove(LAST_USER_ACTS);
                    globalState.remove(CLARIFYING_OPTION);
                    break;
                case CONFIRM:

                    break;
                case CANCEL:

                    break;
                case TRIGGER:
                    if (!userAct.getDomain().hasCorrespondingContext())
                        systemAct.addActUnit(new ActUnit(ActType.NEW_DOMAIN, null, systemAct.getDomain().getDialStructure().getTask()));
                    break;
            }
        }
        lastDomain = systemAct.getDomain();
        return systemAct;
    }

    public HashMap run(SystemAct systemAct) {
        HashMap tempState = new HashMap();
        ArrayList<ActUnit> actUnits = systemAct.getActUnits();
        for (ActUnit actUnit : actUnits) {
            switch (actUnit.actType) {
                case GROUND:
                    systemAct.getDomain().correspondingContext().setSlot(actUnit.slot, actUnit.value);

                    log.info("[Run]\tSystem act will fill the slot " + actUnit.slot + " with " + actUnit.value);

                    if (systemAct.getDomain().correspondingContext().hasUnfilledDialElement()) {
                        log.info("[Run]\tPut a new REQ_DIA_ELEMENT " + systemAct.getDomain().correspondingContext().nextUnfilledDialElement());
                        globalState.put(REQUESTING_DIAL_ELEMENT, systemAct.getDomain().correspondingContext().nextUnfilledDialElement());
                    } else {
                        globalState.put(DOMAIN_FINISHED, lastDomain);
                    }
                    if (!systemAct.getDomain().correspondingContext().hasUnfilledDialElement())
                        tempState = systemAct.getDomain().execute(systemAct);
                    break;
                case CLARIFY_OPTION:
//                    globalState.put(CLARIFYING_OPTION,actUnit.value);
//                    globalState.put(LAST_USER_ACTS,)
                    break;
                case NEW_DOMAIN:
                    Context context = new Context(systemAct.getDomain().getDialStructure());
                    domainContextHashMap.put(systemAct.getDomain(), context);
                    log.info("[Run]\tA new context is created, and now there are " + domains.size() +
                            " domains and " + domainContextHashMap.size() + " contexts");

                    globalState.put(DOMAIN_CREATED, actUnit.value);
                    globalState.put(REQUESTING_DIAL_ELEMENT, systemAct.getDomain().correspondingContext().nextUnfilledDialElement());
                    break;
            }
        }
        return tempState;

    }


    public String work(String utterance) {

        init();

        ArrayList<UserAct> userActs = convert(utterance);

        SystemAct systemAct = react(userActs);

        StringBuffer systemUtterance = new StringBuffer();

        if (systemAct != SystemAct.NONE) {

            HashMap tempState = run(systemAct);

            if (lastDomain == Domain.SYSTEM) {
                systemUtterance.append((String) globalState.get(CLARIFYING_OPTION));
            } else {
                if (globalState.containsKey(DOMAIN_CREATED))
                    systemUtterance.append("I will help you with " + globalState.get(Kernel.DOMAIN_CREATED) + "!\n");

                log.info("[Work]\t" + lastDomain.getDialStructure().getTask());
                systemUtterance.append(lastDomain.generate(tempState));
                // If a context has been closed, reset the current globalState.
                if (!lastDomain.correspondingContext().hasUnfilledDialElement()) {
                    domainContextHashMap.remove(lastDomain);
                    globalState.clear();
                }

            }
        } else
            systemUtterance.append("Hello, what can i do for you?");


        return systemUtterance.toString();
    }
}
