package sample;

import godial.context.Context;
import godial.dialstructure.DialElement;
import godial.domain.generator.AbstractGenerator;

import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-28.
 */
public class WeatherAskingGenerator extends AbstractGenerator {
    @Override
    public String generate(HashMap map) {
        Context context = getDomain().correspondingContext();
        if (map.containsKey("weather"))
            return "The weather in " + context.getValues().get("location") + " on " + context.getValues().get("date") + " is " + map.get("weather");
        else {
            DialElement unfilled = getDomain().correspondingContext().nextUnfilledDialElement();
            return "Ok, what is the " + unfilled.slot + " of " + getDomain() + "?";
        }
    }
}
