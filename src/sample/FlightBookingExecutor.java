package sample;

import godial.act.SystemAct;
import godial.context.Context;
import godial.domain.executor.AbstractExecutor;

import javax.swing.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by zhouyi on 16-10-27.
 */
public class FlightBookingExecutor extends AbstractExecutor {
    @Override
    public HashMap execute(SystemAct systemAct) {
        Context context = systemAct.getDomain().correspondingContext();
        HashMap map = context.getValues();
        int price = new Random().nextInt(100);
        JOptionPane.showMessageDialog(null, "You are booking a flight ticket, the price is " + price + " dollars.\n\n" + map);
        HashMap ret = new HashMap();
        ret.put("price", price);
        return ret;
    }
}
