package godial.kernel;

import godial.act.ActUnit;
import godial.act.SystemAct;
import godial.act.UserAct;
import godial.context.Context;
import godial.domain.Domain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-24.
 */
public class Kernel implements IKernel {

    static Log log= LogFactory.getLog(Kernel.class);

//    private ArrayList<Context> contexts;
    private ArrayList<Domain> domains;

    public Kernel() {
//        contexts = new ArrayList<>();
        domains = new ArrayList<>();
    }

    public void registerDomain(Domain domain) {
        domain.setKernel(this);
        domain.setContext(new Context());
        domains.add(domain);
    }

//    public ArrayList<Context> getContexts() {
//        return contexts;
//    }

    public void execute(SystemAct systemAct) {
        ArrayList<ActUnit> actUnits=systemAct.getActUnits();
        for(ActUnit actUnit:actUnits){
            switch (actUnit.actType){
                case INFORM:
                    log.info("System act will fill in "+actUnit.slot+" with "+actUnit.value);
                    systemAct.getContext().setSlot(actUnit.slot,actUnit.value);
                    break;
            }
        }

    }

    public UserAct select(ArrayList<UserAct> userActs) {
        return userActs.get(0);
    }

    public SystemAct react(UserAct userAct) {
        ArrayList<ActUnit> actUnits=userAct.getActUnits();
        SystemAct systemAct=new SystemAct();
        systemAct.setContext(userAct.getContext());
        for(ActUnit actUnit:actUnits){
            switch (actUnit.actType){
                case INFORM:
                    systemAct.addActUnit(actUnit);
                    break;
            }
        }
        return systemAct;
    }

    @Override
    public String work(String utterance) {
        ArrayList<UserAct> userActs=new ArrayList<>();
        for(Domain domain:domains)
            userActs.add(domain.convert(utterance));
        UserAct bestUserAct=select(userActs);
        int index=userActs.indexOf(bestUserAct);
        SystemAct systemAct=react(bestUserAct);
        execute(systemAct);
        String systemUtterance=domains.get(index).generate(systemAct);
        return systemUtterance;
    }
}
