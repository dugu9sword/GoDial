package godial.dialstructure;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-23.
 */
public class DialStructure {

    private String task;
    private String trigger;
    private ArrayList<DialElement> dialElements;

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }


    public ArrayList<DialElement> getDialElements() {
        return dialElements;
    }

    public void setDialElements(ArrayList<DialElement> dialElements) {
        this.dialElements = dialElements;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }


}
