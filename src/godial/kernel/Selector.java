package godial.kernel;

import godial.act.UserAct;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-23.
 */
public interface Selector {
    UserAct select(ArrayList<UserAct> userActs);
}
