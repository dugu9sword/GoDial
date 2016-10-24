package godial.kernel;

import godial.act.SystemAct;
import godial.act.UserAct;
import godial.domain.Domain;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-23.
 */
public interface IKernel {

//    void newContext();

//    ArrayList<Context> getContexts();

    void registerDomain(Domain domain);

    void execute(SystemAct systemAct);

    UserAct select(ArrayList<UserAct> userActs);

    SystemAct react(UserAct userAct);

    String work(String utterance);

}
