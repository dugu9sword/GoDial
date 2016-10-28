package godial.act;

import godial.domain.Domain;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-24.
 */
public class AbstractAct {
    private ArrayList<ActUnit> actUnits;

    private Domain domain;

    public AbstractAct() {
        actUnits = new ArrayList<>();
    }

    public void addActUnit(ActUnit actUnit) {
        actUnits.add(actUnit);
    }

    public ArrayList<ActUnit> getActUnits() {
        return actUnits;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String toString() {
        String ret = getClass().getName() + " ";
        for (ActUnit actUnit : actUnits)
            ret += actUnit.toString() + " ";
        return ret;
    }
}
