import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<Person> suppliers = new ArrayList<>();
    static List<Person> recipients = new ArrayList<>();
    static double[][] costs;
    static double[][] income;
    static double[][] supplies;

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
        supplies = new double[suppliers.size()][recipients.size()];

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
            BufferedReader reader = new BufferedReader(new FileReader("example.txt"));
            String line;
            System.out.println("Reading data...");
            while ((line = reader.readLine()) != null) {
                if (line.equals(";"))
                    break;
                String[] parts = line.split(" ");
                int quanity = Integer.parseInt(parts[0]);
                int value = Integer.parseInt(parts[1]);
                supply += value;
                suppliers.add(new Person(quanity, value));
            }

            while ((line = reader.readLine()) != null) {
                if (line.equals(";"))
                    break;
                String[] parts = line.split(" ");
                int quanity = Integer.parseInt(parts[0]);
                int value = Integer.parseInt(parts[1]);
                demand += value;
                recipients.add(new Person(quanity, value));
            }

            costs = new double[suppliers.size()][recipients.size()];
            income = new double[suppliers.size()][recipients.size()];
            supplies = new double[suppliers.size()][recipients.size()];

            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                for(int j = 0; j < costs[0].length; j++) {
                    costs[i][j] = Integer.parseInt(parts[j]);
                }
                i++;
            }
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void setIncome() {
        for(int i = 0; i < costs.length; i++) {
            for(int j = 0; j < costs[0].length; j++) {
                income[i][j] = recipients.get(j).value - suppliers.get(i).value - costs[i][j];
                //System.out.println("Income: " + i + " -> " + j + " is: " + income[i][j]);
            }
        }
    }

    public static void setSupplies() {
        double left = 0;
        int tmp, j = 0;
        for(int i = 0; i < costs.length; i++) {
            tmp = suppliers.get(i).quanity;
            for( ; j < costs[0].length ; ) {
                supplies[i][j] = Math.min(tmp, left == 0 ? recipients.get(j).quanity : left);
                tmp -= supplies[i][j];
                if(tmp == 0) {
                    left = (left == 0 ? recipients.get(j).quanity : left) - supplies[i][j];
                    break;
                }
                left = 0;
                j++;
            }
        }
    }

    public static void main(String[] args) {
        //readPeopleData();
        readDataFromFile();

        setIncome();

        setSupplies();

        for(int i = 0; i < costs.length; i++) {
            for(int j = 0; j < costs[0].length; j++) {
                System.out.println("Supplies: " + i + " -> " + j + " is: " + supplies[i][j]);
            }
        }
    }
}
