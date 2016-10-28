package godial.context;

import godial.dialstructure.DialElement;
import godial.dialstructure.DialStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-23.
 */
public class Context {

    static Log log = LogFactory.getLog(Context.class);

    private DialStructure dialStructure;

    private HashMap<String, String> values;

    public Context(DialStructure dialStructure) {
        this.dialStructure = dialStructure;
        values = new HashMap<>();
    }

    public DialStructure getDialStructure() {
        return dialStructure;
    }

    public void setSlot(String slot, String value) {
        values.put(slot, value);
    }

    public DialElement nextUnfilledDialElement() {
        for (DialElement dialElement : dialStructure.getDialElements())
            if (dialElement.required && !values.containsKey(dialElement.slot))
                return dialElement;
        return null;
    }

    public boolean hasUnfilledDialElement() {
        return nextUnfilledDialElement() != null;
    }

    public HashMap<String, String> getValues() {
        return values;
    }


}
