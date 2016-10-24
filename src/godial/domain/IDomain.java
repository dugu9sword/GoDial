package godial.domain;

import godial.act.SystemAct;
import godial.act.UserAct;
import godial.domain.converter.IConverter;
import godial.domain.generator.IGenerator;

/**
 * Created by zhouyi on 16-10-23.
 */
public interface IDomain extends IConverter,IGenerator{
    UserAct convert(String utterance);
    String generate(SystemAct systemAct);
}
