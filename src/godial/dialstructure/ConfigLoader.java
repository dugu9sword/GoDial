package godial.dialstructure;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-23.
 */
public class ConfigLoader {
    public static DialStructure loadConfig(String filePath) {
        // TODO
        if (filePath.equals("MOCK-FLIGHT-BOOKING")) {
            DialElement departure = new DialElement("departure", "from {departure};{departure}=(Beijing|Shanghai)", true);
            DialElement destination = new DialElement("destination", "to {destination};{destination}=(Beijing|Shanghai)", true);
            DialElement date = new DialElement("date", "(on the {day}th of {month})|(on {month} {day}th);{day}=\\d+,{month}=(May|June|July)", true);
            ArrayList<DialElement> dialElements = new ArrayList<>();
            dialElements.add(departure);
            dialElements.add(destination);
            dialElements.add(date);
            DialStructure dialStructure = new DialStructure(dialElements);
            return dialStructure;
        }else if(filePath.equals("MOCK-WEATHER-ASKING")){
            DialElement location = new DialElement("location", "{location}'s weather;{location}=(Beijing|Shanghai|Nanjing)", true);
            DialElement day = new DialElement("day", "{day};{day}=(today|tomorrow)", true);
            ArrayList<DialElement> dialElements = new ArrayList<>();
            dialElements.add(location);
            dialElements.add(day);
            DialStructure dialStructure = new DialStructure(dialElements);
            return dialStructure;
        }
        return null;
    }
}
