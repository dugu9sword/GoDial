package godial.dialstructure;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by zhouyi on 16-10-23.
 */
public class ConfigLoader {
    public static DialStructure loadConfig(String filePath) throws IOException, FileNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        Scanner scanner = new Scanner(new File(filePath));
        StringBuffer sb = new StringBuffer();
        while (scanner.hasNext())
            sb.append(scanner.nextLine());

        DialStructure dialStructure = objectMapper.readValue(sb.toString(), DialStructure.class);
        return dialStructure;

//        if (filePath.equals("MOCK-FLIGHT-BOOKING")) {
//            DialElement departure = new DialElement("departure",
//                    "from {departure};{departure}=(Beijing|Shanghai)",
//                    true,
//                    DialEleType.LOCATION);
//            DialElement destination = new DialElement("destination",
//                    "to {destination};{destination}=(Beijing|Shanghai)",
//                    true,
//                    DialEleType.LOCATION);
//            DialElement date = new DialElement("date",
//                    "(on the {day}th of {month})|(on {month} {day}th);{day}=\\d+,{month}=(May|June|July)",
//                    true,
//                    DialEleType.DATE);
//            ArrayList<DialElement> dialElements = new ArrayList<>();
//            dialElements.add(departure);
//            dialElements.add(destination);
//            dialElements.add(date);
//            DialStructure dialStructure = new DialStructure();
//            dialStructure.setDialElements(dialElements);
//            dialStructure.setTask("MOCK-FLIGHT-BOOKING");
//            return dialStructure;
//        }else if(filePath.equals("MOCK-WEATHER-ASKING")){
//            DialElement location = new DialElement("location",
//                    "{location}'s weather;{location}=(Beijing|Shanghai|Nanjing)",
//                    true,
//                    DialEleType.LOCATION);
//            DialElement day = new DialElement("day",
//                    "{day};{day}=(today|tomorrow)",
//                    true,
//                    DialEleType.DATE);
//            ArrayList<DialElement> dialElements = new ArrayList<>();
//            dialElements.add(location);
//            dialElements.add(day);
//            DialStructure dialStructure = new DialStructure();
//            dialStructure.setDialElements(dialElements);
//            dialStructure.setTask("MOCK-WEATHER-ASKING");
//            return dialStructure;
//        }else if(filePath.equals("MOCK-REMIND-SERVICE")){
//            DialElement todo = new DialElement("todo",
//                    "remind me to {todo};{todo}=(do homework|play football|exercise)",
//                    true,
//                    DialEleType.OTHER);
//            DialElement date = new DialElement("date",
//                    "(on the {day}th of {month})|(on {month} {day}th);{day}=\\d+,{month}=(May|June|July)",
//                    true,
//                    DialEleType.DATE);
//            ArrayList<DialElement> dialElements = new ArrayList<>();
//            dialElements.add(todo);
//            dialElements.add(date);
//            DialStructure dialStructure = new DialStructure();
//            dialStructure.setDialElements(dialElements);
//            dialStructure.setTask("MOCK-REMIND-SERVICE");
//            return dialStructure;
//        }
    }
}
