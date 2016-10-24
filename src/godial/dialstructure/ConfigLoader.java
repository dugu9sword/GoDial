package godial.dialstructure;

import godial.dialstructure.DialStructure;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-23.
 */
public class ConfigLoader {
    public static DialStructure loadConfig(String filePath){
        // TODO
        if(filePath.equals("MOCK-FLIGHTBOOKING")){
            DialElement departure=new DialElement("departure","from {departure};{departure}=(Beijing|Shanghai)",true,DialEleType.LOCATION);
            DialElement destination=new DialElement("destination","to {destination};{destination}=(Beijing|Shanghai)",true,DialEleType.LOCATION);
            DialElement date=new DialElement("date","(on the {day}th of {month})|(on {month} {day}th);{day}=\\d+,{month}=(May|June|July)",true,DialEleType.DATE);
            ArrayList<DialElement> dialElements=new ArrayList<>();
            dialElements.add(departure);
            dialElements.add(destination);
            dialElements.add(date);
            DialStructure dialStructure=new DialStructure(dialElements);
            return dialStructure;
        }
        return null;
    }
}
