package godial.domain.converter;

import godial.act.UserAct;

/**
 *
 * Created by Yi Zhou on 2016/10/22.
 */
public interface IConverter {
    UserAct convert(String utterance);
}
