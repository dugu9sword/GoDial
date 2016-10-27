package godial.domain.executor;

import godial.act.SystemAct;

import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-27.
 */
public interface IExecutor {
    public HashMap execute(SystemAct systemAct);
}
