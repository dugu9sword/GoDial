package godial.domain.executor;

import godial.domain.Domain;

/**
 * Created by zhouyi on 16-10-27.
 */
public abstract class AbstractExecutor implements IExecutor {
    private Domain domain;

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
