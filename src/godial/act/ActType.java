package godial.act;

/**
 * Created by zhouyi on 16-10-23.
 */
public enum ActType {
    /**
     * User act
     */
    INFORM,
    CONFIRM,
    CANCEL,
    SELECT,
    TRIGGER,
    EXIT,

    /**
     * System act
     */
    GROUND,
    CLARIFY_YES_NO,
    NEW_DOMAIN,
    INFO,

    /**
     * Both
     */

    UPDATE,
    EXECUTE,
}
