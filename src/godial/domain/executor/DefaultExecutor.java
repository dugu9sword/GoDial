package godial.domain.executor;

import godial.act.SystemAct;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-27.
 */
public class DefaultExecutor extends AbstractExecutor{
    public HashMap execute(SystemAct systemAct) {
        JOptionPane.showMessageDialog(null,"External Module");
        return null;
    }
}
