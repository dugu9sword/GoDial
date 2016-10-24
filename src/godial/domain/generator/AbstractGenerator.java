package godial.domain.generator;

import godial.domain.Domain;

/**
 * Created by zhouyi on 16-10-24.
 */
public abstract class AbstractGenerator implements IGenerator{
    private Domain domain;

    public void setDomain(Domain domain){
        this.domain=domain;
    }

    public Domain getDomain(){
        return domain;
    }
}
