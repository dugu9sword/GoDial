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

        Domain domain3 = Domain.newInstance(path + "MOCK-REMIND-SERVICE.json");
        if (domain3 != null)
            kernel.registerDomain(domain3);

        Scanner scanner = new Scanner(System.in);
        String input;
        while (!(input = scanner.nextLine()).equals("bye")) {
            System.out.println("[System]:" + kernel.work(input) + "\n");
            System.out.print("[User]:");
        }
    }
}