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
    EXIT,

    /**
     * System act
     */
    GROUND,
    CLARIFY_YES_NO,
    CLARIFY_OPTION,
    REQUEST,
    INFO,

    /**
     * Both
     */

    UPDATE,
    EXECUTE,
}
