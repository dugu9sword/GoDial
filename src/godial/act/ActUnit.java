package godial.act;

/**
 * Created by zhouyi on 16-10-23.
 */
public class ActUnit {
    public ActType actType;
    public String slot;
    public String value;

    public ActUnit(ActType actType, String slot, String value) {
        this.actType = actType;
        this.slot = slot;
        this.value = value;
    }

    public String toString() {
        return actType + " " + slot + " " + value;
    }
}
