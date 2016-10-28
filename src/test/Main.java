package test;

import godial.domain.Domain;
import godial.kernel.Kernel;
import org.apache.log4j.PropertyConfigurator;

import java.util.Scanner;


/**
 * Created by zhouyi on 16-10-24.
 */
public class Main {
    static {
        PropertyConfigurator.configure("log4j.properties");
    }

    public static void main(String[] args) throws Exception {

//        ObjectMapper objectMapper=new ObjectMapper();
//        String name="MOCK-REMIND-SERVICE";
//        String x=objectMapper.writeValueAsString(ConfigLoader.loadConfig(name));
//        PrintWriter printWriter=new PrintWriter("domain_confs/"+name+".json");
//        printWriter.write(x);
//        printWriter.close();

//        ObjectMapper objectMapper=new ObjectMapper();
//        Scanner scanner=new Scanner(new File("domain_confs/MOCK-FLIGHT-BOOKING.json"));
//        StringBuffer sb=new StringBuffer();
//        while (scanner.hasNext())
//            sb.append(scanner.nextLine());
//
//        DialStructure dialStructure=objectMapper.readValue(sb.toString(),DialStructure.class);
//        System.out.print(dialStructure);

        Kernel kernel = new Kernel();

        String path = "domain_confs/";

        Domain domain1 = Domain.newInstance(path + "MOCK-FLIGHT-BOOKING.json");
        if (domain1 != null) {
            domain1.setExecutor(new FlightBookingExecutor());
            domain1.setGenerator(new FlightBookingGenerator());
            kernel.registerDomain(domain1);
        }

        Domain domain2 = Domain.newInstance(path + "MOCK-WEATHER-ASKING.json");
        if (domain2 != null)
            kernel.registerDomain(domain2);

        Domain domain3 = Domain.newInstance(path + "MOCK-REMIND-SERVICE.json");
        if (domain3 != null)
            kernel.registerDomain(domain3);

        Scanner scanner = new Scanner(System.in);
        String input;
        while (!(input = scanner.nextLine()).equals("bye")) {
            System.out.println("[System]:" + kernel.work(input) + "\n");
            System.out.print("[User]:");
        }


//        String p="remind me that {todo};{todo}=(goto)";
//        Map<String,String> map= RegexUtil.extract("remind me that i will fly to beijing",p);
//        for (String x:map.keySet()){
//            System.out.println(x+" = "+map.get(x));
//        }
    }
}