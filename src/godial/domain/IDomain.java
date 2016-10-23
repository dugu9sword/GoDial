package godial.domain;

import godial.act.SystemAct;
import godial.act.UserAct;

/**
 * Created by zhouyi on 16-10-23.
 */
public interface IDomain {
    UserAct convert(String utterance);
    String generate(SystemAct systemAct);
}
