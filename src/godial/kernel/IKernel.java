package godial.kernel;

import godial.act.SystemAct;
import godial.act.UserAct;
import godial.domain.Domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-23.
 */
public interface IKernel {

    void init();

    void registerDomain(Domain domain);

    HashMap run(SystemAct systemAct);

    ArrayList<UserAct> convert(String utterance);

    SystemAct react(ArrayList<UserAct> userActs);

    String work(String utterance);

}
