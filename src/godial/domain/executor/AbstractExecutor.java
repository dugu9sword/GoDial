package godial.domain.executor;

import godial.act.SystemAct;
import godial.domain.Domain;

import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-27.
 */
public abstract class AbstractExecutor implements IExecutor {
    private Domain domain;

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Domain getDomain() {
        return domain;
    }
}
