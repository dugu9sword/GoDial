package godial.act;

import godial.context.Context;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-24.
 */
public class AbstractAct {
    private ArrayList<ActUnit> actUnits;

    private Context context;

    public AbstractAct() {
        actUnits = new ArrayList<>();
    }

    public void addActUnit(ActUnit actUnit) {
        actUnits.add(actUnit);
    }

    public ArrayList<ActUnit> getActUnits() {
        return actUnits;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String toString(){
        String ret=getClass().getName()+" ";
        for(ActUnit actUnit:actUnits)
            ret+=actUnit.toString()+" ";
        return ret;
    }
}
