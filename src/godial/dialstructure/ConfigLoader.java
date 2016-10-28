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
    }
}
