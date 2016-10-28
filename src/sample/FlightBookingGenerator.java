package sample;

import godial.dialstructure.DialElement;
import godial.domain.generator.AbstractGenerator;

import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-27.
 */
public class FlightBookingGenerator extends AbstractGenerator {
    @Override
    public String generate(HashMap map) {
        if (map.containsKey("price"))
            return "I have helped you book a ticket, the price is " + map.get("price");
        else {
            DialElement unfilled = getDomain().correspondingContext().nextUnfilledDialElement();
            return "Ok, what is the " + unfilled.slot + " of " + getDomain() + "?";
        }
    }
}
