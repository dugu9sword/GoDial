package godial.context;

import godial.dialstructure.DialElement;
import godial.dialstructure.DialStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-23.
 */
public class Context {

    static Log log = LogFactory.getLog(Context.class);

    private static final String UNFILLED = "!@#$%^&*()";

    private ArrayList<String> values;

    public DialStructure getDialStructure() {
        return dialStructure;
    }

    public void setDialStructure(DialStructure dialStructure) {
        this.dialStructure = dialStructure;
        values = new ArrayList<>();
        for (int i = 0; i < dialStructure.getDialElements().size(); i++)
            values.add(UNFILLED);
    }

    private DialStructure dialStructure;

    public void setSlot(String slot, String value) {
        int index;
        for (index = 0; index < dialStructure.getDialElements().size(); index++)
            if (dialStructure.getDialElements().get(index).slot.equals(slot)) {
                values.set(index, value);
            }
    }

    public boolean allSlotFilled() {
        boolean filled = true;
        for (int index = 0; index < values.size(); index++)
            if (values.get(index).equals(UNFILLED))
                filled = false;
        return filled;
    }

    public String nextUnfilledSlot() {
        int index;
        for (index = 0; index < values.size(); index++)
            if (values.get(index).equals(UNFILLED))
                break;
        return dialStructure.getDialElements().get(index).slot;
    }

    public ArrayList<String> getValues(){
        return values;
    }


}
