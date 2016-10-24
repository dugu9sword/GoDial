package godial.dialstructure;

/**
 * Created by zhouyi on 16-10-24.
 */
public class DialElement {
    public String slot;
    public String pattern;
    public boolean required;

    public DialElement(String slot, String pattern, boolean required) {
        this.slot = slot;
        this.pattern = pattern;
        this.required = required;
    }
}
