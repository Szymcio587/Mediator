import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<Person> suppliers = new ArrayList<>();
    static List<Person> recipients = new ArrayList<>();
    static double[][] costs;
    static double[][] income;

    static double supply = 0, demand = 0;

    public static void readPeopleData() {
        int quanity;
        double value;
        System.out.println("Read suppliers:");
        while(true) {
            System.out.println("Next quanity:");
            quanity = scanner.nextInt();
            if(quanity == 0)
                break;
            System.out.println("Next value:");
            value = scanner.nextDouble();
            supply += value;
            suppliers.add(new Person(quanity, value));
        }

        System.out.println("Read recipients:");
        while(true) {
            System.out.println("Next quanity:");
            quanity = scanner.nextInt();
            if(quanity == 0)
                break;
            System.out.println("Next value:");
            value = scanner.nextDouble();
            demand += value;
            recipients.add(new Person(quanity, value));
        }

        costs = new double[suppliers.size()][recipients.size()];
        income = new double[suppliers.size()][recipients.size()];

        System.out.println("Read costs");
        for(int i = 0; i < costs.length; i++) {
            for(int j = 0; j < costs[0].length; j++) {
                System.out.println("Path: " + i + " -> " + j);
                costs[i][j] = scanner.nextDouble();
            }
        }
    }

    public static void readDataFromFile() {
        try {
            File data = new File("example.txt");
            Scanner fileReader = new Scanner(data);

            while (fileReader.hasNextLine()) {
                //wczytywanie
            }
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void setIncome() {
        for(int i = 0; i < costs.length; i++) {
            for(int j = 0; j < costs[0].length; j++) {
                income[i][j] = recipients.get(j).value - suppliers.get(i).value - costs[i][j];
                System.out.println("Income: " + i + " -> " + j + " is: " + income[i][j]);
            }
        }
    }

    public static void main(String[] args) {
        readPeopleData();
        //readDataFromFile();

        setIncome();
    }
}
