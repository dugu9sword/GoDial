package godial.domain.generator;

import godial.act.SystemAct;
import godial.dialstructure.DialElement;

import java.util.ArrayList;

/**
 * Generator a response by simply asking "What is the {slot}?".
 *
 *
 * Created by zhouyi on 16-10-23.
 */
public class DefaultGenerator extends AbstractGenerator{
    public String generate(SystemAct systemAct){
        if(getDomain().getContext().allSlotFilled()){
            String s= "OK! I will do that for you.\n";
            ArrayList<DialElement> dialElements=getDomain().getContext().getDialStructure().getDialElements();
            for(int i=0;i<dialElements.size();i++)
                s+=dialElements.get(i).slot+"\t"+getDomain().getContext().getValues().get(i)+"\n";
            s+="Thank you.";
            return s;
        }else{
            return "Ok, then, what is the "+getDomain().getContext().nextUnfilledSlot()+"?";
        }
    }
}
