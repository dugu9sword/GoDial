package godial.domain.generator;

import godial.act.SystemAct;

/**
 * Generator a response by simply asking "What is the {slot}?".
 *
 *
 * Created by zhouyi on 16-10-23.
 */
public class DefaultGenerator extends AbstractGenerator{
    public String generate(SystemAct systemAct){
        if(getDomain().getContext().allSlotFilled()){
            return "OK! I will do that for you";
        }else{
            return "Ok, then, What is the "+getDomain().getContext().nextUnfilledSlot()+"?";
        }
    }
}
