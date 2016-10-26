package godial.domain.generator;

import godial.act.SystemAct;
import godial.dialstructure.DialElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generator a response by simply asking "What is the {slot}?".
 * <p>
 * <p>
 * Created by zhouyi on 16-10-23.
 */
public class DefaultGenerator extends AbstractGenerator {
    static Log log= LogFactory.getLog(DefaultGenerator.class);

    public String generate(SystemAct systemAct) {

        if (getDomain().correspondingContext().nextUnfilledDialElement() == null) {
            String s = "OK! I will do that for you.\n";
            ArrayList<DialElement> dialElements = getDomain().getDialStructure().getDialElements();
            HashMap<String,String> values=getDomain().correspondingContext().getValues();
            for (DialElement dialElement:dialElements)
                s += "- \t\t"+dialElement.slot + "\t" + values.get(dialElement.slot) + "\n";
            s += "Anything else?";
            return s;
        } else {
            return "Ok, what is the " + getDomain().correspondingContext().nextUnfilledDialElement() + "?";
        }
    }
}
