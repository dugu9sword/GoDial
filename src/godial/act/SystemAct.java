package godial.act;

import godial.dialstructure.DialElement;

/**
 * Created by zhouyi on 16-10-23.
 */
public class SystemAct extends AbstractAct {
    public static final SystemAct NONE = new SystemAct();

    public boolean isRequesting() {
        return requestingDialElement() != null ? true : false;
    }

    public DialElement requestingDialElement() {
        String slot = null;
        for (ActUnit actUnit : getActUnits())
            if (actUnit.actType == ActType.REQUEST)
                slot = actUnit.slot;

        if (slot == null)
            return null;
        for (DialElement dialElement : getContext().getDialStructure().getDialElements())
            if (dialElement.slot.equals(slot))
                return dialElement;
        return null;
    }

    public boolean isClarifyingYesOrNo() {
        for (ActUnit actUnit : getActUnits())
            if (actUnit.actType == ActType.CLARIFY_YES_NO)
                return true;
        return false;
    }

    public boolean isClarifyingOptionally() {
        for (ActUnit actUnit : getActUnits())
            if (actUnit.actType == ActType.CLARIFY_OPTION)
                return true;
        return false;
    }
}
