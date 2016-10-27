package test;

import godial.act.ActType;
import godial.act.ActUnit;
import godial.act.UserAct;
import godial.domain.Domain;
import godial.kernel.Kernel;
import godial.utils.RegexUtil;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * Created by zhouyi on 16-10-24.
 */
public class Main {
    static {
        PropertyConfigurator.configure("log4j.properties");
    }

    public static void main(String[] args) {

        Kernel kernel=new Kernel();

        Domain domain1=new Domain("MOCK-FLIGHT-BOOKING");
        domain1.setExecutor(new FlightBookingExecutor());
        domain1.setGenerator(new FlightBookingGenerator());

        Domain domain2=new Domain("MOCK-WEATHER-ASKING");
        Domain domain3=new Domain("MOCK-REMIND-SERVICE");

        kernel.registerDomain(domain1);
        kernel.registerDomain(domain2);
        kernel.registerDomain(domain3);

        Scanner scanner=new Scanner(System.in);
        String input;
        while (!(input=scanner.nextLine()).equals("bye")){
            System.out.println("[System]:"+kernel.work(input)+"\n");
            System.out.print("[User]:");
        }


//        String p="remind me that {todo};{todo}=(goto)";
//        Map<String,String> map= RegexUtil.extract("remind me that i will fly to beijing",p);
//        for (String x:map.keySet()){
//            System.out.println(x+" = "+map.get(x));
//        }
    }
}
