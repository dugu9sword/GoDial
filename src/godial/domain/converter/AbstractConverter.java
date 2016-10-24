package godial.domain.converter;

import godial.dialstructure.DialStructure;
import godial.domain.Domain;

/**
 * Created by zhouyi on 16-10-24.
 */
public abstract class AbstractConverter implements IConverter{
    private Domain domain;

    public void setDomain(Domain domain){
        this.domain=domain;
    }

    public Domain getDomain(){
        return domain;
    }

}
