package sample;

import godial.act.SystemAct;
import godial.context.Context;
import godial.domain.executor.AbstractExecutor;

import javax.swing.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by zhouyi on 16-10-28.
 */
public class WeatherAskingExecutor extends AbstractExecutor {
    @Override
    public HashMap execute(SystemAct systemAct) {
        HashMap ret=new HashMap();
        ret.put("weather", "sunny");
        return ret;
    }
}
