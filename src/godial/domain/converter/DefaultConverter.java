package godial.domain.converter;

import godial.act.ActType;
import godial.act.ActUnit;
import godial.act.UserAct;
import godial.dialstructure.DialElement;
import godial.dialstructure.DialStructure;
import godial.domain.Domain;
import godial.kernel.Kernel;
import godial.utils.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Convert an utterance to a user act using regexplus.
 * @See godial.utils.RegexUtil
 *
 *
 * Created by zhouyi on 16-10-23.
 */
public class DefaultConverter extends AbstractConverter {
    public UserAct convert(String utterance){
        ArrayList<DialElement> dialElements= getDomain().getDialStructure().getDialElements();
        UserAct userAct=new UserAct();
        for(DialElement dialElement:dialElements){
            HashMap<String,String> map=RegexUtil.extract(utterance,dialElement.pattern);
            if(map!=null){
                ActUnit actUnit=new ActUnit();
                actUnit.actType= ActType.INFORM;
                actUnit.slot=dialElement.slot;
                actUnit.value=map.toString();
                userAct.addActUnit(actUnit);
            }
        }
        userAct.setContext(getDomain().getContext());
        if(userAct.getActUnits().isEmpty())
            return UserAct.NONE;
        else
            return userAct;
    }


}