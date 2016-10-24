package test;

import godial.domain.Domain;
import godial.kernel.Kernel;
import godial.utils.RegexUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by zhouyi on 16-10-24.
 */
public class Main {
    public static void main(String[] args) {

//        ArrayList<String> a=new ArrayList<>(3);
//        System.out.print(a.size());
//        for(String x:a)
//            System.out.println(x==null?"null":"yes");
//        System.exit(0);

        Kernel kernel=new Kernel();
        Domain domain=new Domain("MOCK-FLIGHTBOOKING");
        kernel.registerDomain(domain);
        Scanner scanner=new Scanner(System.in);
        while (true){
            System.out.println(kernel.work(scanner.nextLine()));
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
