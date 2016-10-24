package godial.act;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-24.
 */
public class AbstractAct {
    private ArrayList<ActUnit> actUnits;

    public AbstractAct(){
        actUnits=new ArrayList<>();
    }

    public void addActUnit(ActUnit actUnit){
        actUnits.add(actUnit);
    }

    public ArrayList<ActUnit> getActUnits(){
        return actUnits;
    }
}
