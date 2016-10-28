package godial.domain.generator;

import godial.dialstructure.DialElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generator a response by simply asking "What is the {slot} of {Domain}?".
 * <p>
 * <p>
 * Created by zhouyi on 16-10-23.
 */
public class DefaultGenerator extends AbstractGenerator {
    static Log log = LogFactory.getLog(DefaultGenerator.class);

    public String generate(HashMap map) {
        log.info("[Generator]\t" + getDomain().getDialStructure().getTask());
        if (getDomain().correspondingContext().nextUnfilledDialElement() == null) {
            StringBuffer generated = new StringBuffer();
            generated.append("Task completed!\n");
            ArrayList<DialElement> dialElements = getDomain().getDialStructure().getDialElements();
            HashMap<String, String> values = getDomain().correspondingContext().getValues();
            for (DialElement dialElement : dialElements)
                generated.append("- \t\t" + dialElement.slot + "\t" + values.get(dialElement.slot) + "\n");
            generated.append("Can I help you with anything else?");
            return generated.toString();
        } else {
            DialElement unfilled = getDomain().correspondingContext().nextUnfilledDialElement();
            return "Ok, what is the " + unfilled.slot + " of (" + getDomain() + ")?";
        }
    }
}
