package sample;

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

        Kernel kernel = new Kernel();

        String path = "domain_confs/";

        Domain domain1 = Domain.newInstance(path + "MOCK-FLIGHT-BOOKING.json");
        if (domain1 != null) {
            domain1.setExecutor(new FlightBookingExecutor());
            domain1.setGenerator(new FlightBookingGenerator());
            kernel.registerDomain(domain1);
        }

        Domain domain2 = Domain.newInstance(path + "MOCK-WEATHER-ASKING.json");
        if (domain2 != null) {
            domain2.setExecutor(new WeatherAskingExecutor());
            domain2.setGenerator(new WeatherAskingGenerator());
            kernel.registerDomain(domain2);
        }

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("[System]:Hello, what can i do for you?\n");
        System.out.print("[User]:");
        while (!(input = scanner.nextLine()).equals("bye")) {
            System.out.println("\n[System]:" + kernel.work(input)+"\n");
            System.out.print("[User]:");
        }
    }
}