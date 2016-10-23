package godial.kernel;

import godial.act.SystemAct;
import godial.act.UserAct;

/**
 * Created by zhouyi on 16-10-23.
 */
public interface Reactor {
    SystemAct react(UserAct userAct);
}
