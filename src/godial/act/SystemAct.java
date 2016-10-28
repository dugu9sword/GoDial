package godial.act;

/**
 * Created by zhouyi on 16-10-23.
 */
public class SystemAct extends AbstractAct {
    public static final SystemAct NONE = new SystemAct();

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
