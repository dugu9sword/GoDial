package godial.dialstructure;

/**
 * Created by zhouyi on 16-10-24.
 */
public class DialElement {
    public String slot;
    public String pattern;
    public boolean required;
    public DialEleType type;

    public DialElement(String slot, String pattern, boolean required,DialEleType type) {
        this.slot = slot;
        this.pattern = pattern;
        this.required = required;
        this.type=type;
    }

    public DialElement() {

    }

    public String toString(){
        return "slot: "+slot+"\ttype: "+type+"\trequired: "+required;
    }
}
