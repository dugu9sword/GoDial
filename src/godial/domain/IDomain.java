package godial.domain;

import godial.act.SystemAct;
import godial.act.UserAct;
import godial.domain.converter.IConverter;
import godial.domain.executor.IExecutor;
import godial.domain.generator.IGenerator;

import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-23.
 */
public interface IDomain extends IConverter, IGenerator,IExecutor {
    UserAct convert(String utterance);

    String generate(HashMap map);

    HashMap execute(SystemAct systemAct);

}
