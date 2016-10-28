package godial.domain.executor;

import godial.act.SystemAct;
import godial.domain.generator.DefaultGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-27.
 */
public class DefaultExecutor extends AbstractExecutor {

    static Log log = LogFactory.getLog(DefaultGenerator.class);

    public HashMap execute(SystemAct systemAct) {
//        JOptionPane.showMessageDialog(null,"External Module");
        log.info("[Executor]\tDefault executor is running");
        return null;
    }
}
