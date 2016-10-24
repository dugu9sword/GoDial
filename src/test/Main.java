package test;

import godial.domain.Domain;
import godial.kernel.Kernel;
import org.apache.log4j.PropertyConfigurator;

import java.util.Scanner;

/**
 * Created by zhouyi on 16-10-24.
 */
public class Main {
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");

//        String x=null;
//        if(x!=null)System.out.print("yes");
//        System.exit(0);

        Kernel kernel=new Kernel();
        Domain domain1=new Domain("MOCK-FLIGHT-BOOKING");
        Domain domain2=new Domain("MOCK-WEATHER-ASKING");
        kernel.registerDomain(domain1);
        kernel.registerDomain(domain2);
        Scanner scanner=new Scanner(System.in);
        String input;
        while (!(input=scanner.nextLine()).equals("bye")){
            System.out.println("[System]:"+kernel.work(input)+"\n");
            System.out.print("[User]:");
        }

//        System.out.println(Pattern.compile("7th of May").matcher("7th of May").find().group(0));


//        String[] x=RegexUtil.split("\\,\\,\\,\\,",',');
//        for(String s:x)
//            System.out.println(s);

//        String p="(on the {day}th of {month})|(on {month} {day}th);{month}=(May|June|July),{day}=\\d+";
//        Map<String,String> map= RegexUtil.extract("I will fly to Beijing on Juned 67th, what about you?",p);
//        for (String x:map.keySet()){
//            System.out.println(x+" = "+map.get(x));
//        }
    }
}
