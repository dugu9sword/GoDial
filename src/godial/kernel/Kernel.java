package godial.kernel;

import godial.act.ActUnit;
import godial.act.SystemAct;
import godial.act.UserAct;
import godial.context.Context;
import godial.domain.Domain;
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
    private HashMap<Domain,Context> domainContextHashMap;

    public Kernel() {
        domainContextHashMap=new HashMap<>();
        domains = new ArrayList<>();
    }

    public void registerDomain(Domain domain) {
        domain.setKernel(this);
        domains.add(domain);
    }

    public Context getContextByDomain(Domain domain){
        return domainContextHashMap.get(domain);
    }

    public void execute(SystemAct systemAct) {
        log.info(systemAct);
        if(systemAct==SystemAct.NONE){

        }

        ArrayList<ActUnit> actUnits = systemAct.getActUnits();
        for (ActUnit actUnit : actUnits) {
            switch (actUnit.actType) {
                case INFORM:
                    systemAct.getContext().setSlot(actUnit.slot, actUnit.value);
                    log.info("System act will fill the slot " + actUnit.slot + " with " + actUnit.value);
                    break;
            }
        }

    }

    public UserAct select(ArrayList<UserAct> userActs) {
        log.info(userActs.size());
        for(UserAct userAct:userActs)
            if(userAct!=UserAct.NONE)
                return userAct;
        return UserAct.NONE;
    }

    public SystemAct react(UserAct userAct) {
        ArrayList<ActUnit> actUnits = userAct.getActUnits();
        SystemAct systemAct = new SystemAct();
        systemAct.setContext(userAct.getContext());
        for (ActUnit actUnit : actUnits) {
            switch (actUnit.actType) {
                case INFORM:
                    systemAct.addActUnit(actUnit);
                    break;
            }
        }
        return systemAct;
    }

    @Override
    public String work(String utterance) {

        ArrayList<UserAct> userActs = new ArrayList<>();
        for (Domain domain : domains){
            UserAct userAct=domain.convert(utterance);
            if(userAct!=UserAct.NONE) {
                if (domain.hasCorrespondingContext())
                    userAct.setContext(domain.correspondingContext());
                else {
                    Context context = new Context(domain.getDialStructure());
                    domainContextHashMap.put(domain,context);
                    userAct.setContext(context);
                    log.info("A new context is born, and now there are "+domains.size()+" domains and "+domainContextHashMap.size()+" contexts");
                }
            }
            userActs.add(userAct);
        }

        UserAct bestUserAct = select(userActs);

        if(bestUserAct==UserAct.NONE){
            return "Sorry, but what do you mean?";
        }

        SystemAct systemAct = react(bestUserAct);

        execute(systemAct);
        String systemUtterance = domains.get(userActs.indexOf(bestUserAct)).generate(systemAct);

        return systemUtterance;
    }
}
