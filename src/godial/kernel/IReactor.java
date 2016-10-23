package godial.kernel;

import godial.act.SystemAct;
import godial.act.UserAct;

/**
 * Created by zhouyi on 16-10-23.
 */
public interface IReactor {
    SystemAct react(UserAct userAct);
}
