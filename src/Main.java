import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<Person> suppliers = new ArrayList<>();
    static List<Person> recipients = new ArrayList<>();
    static double[][] costs, income, supplies, deltas;
    static double[] alpha, betha;
    static double totalIncome;
    static int iteration, supply = 0, demand = 0;

    public static void CreateArrays() {
        costs = new double[suppliers.size()][recipients.size()];
        income = new double[suppliers.size()][recipients.size()];
        supplies = new double[suppliers.size()][recipients.size()];
        deltas = new double[suppliers.size()][recipients.size()];
        alpha = new double[suppliers.size()];
        betha = new double[recipients.size()];
        Arrays.fill(alpha, Math.PI);
        Arrays.fill(betha, Math.PI);
        iteration = 1;
    }

    public static void ReadPeopleData() {
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
            supply += quanity;
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
            demand += quanity;
            recipients.add(new Person(quanity, value));
        }
        CreateArrays();

        System.out.println("Read costs:");
        for(int i = 0; i < costs.length; i++) {
            for(int j = 0; j < costs[0].length; j++) {
                System.out.println("Path: " + i + " -> " + j);
                costs[i][j] = scanner.nextDouble();
            }
        }
    }

    public static void ReadDataFromFile() {
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
                supply += quanity;
                suppliers.add(new Person(quanity, value));
            }

            while ((line = reader.readLine()) != null) {
                if (line.equals(";"))
                    break;
                String[] parts = line.split(" ");
                int quanity = Integer.parseInt(parts[0]);
                int value = Integer.parseInt(parts[1]);
                demand += quanity;
                recipients.add(new Person(quanity, value));
            }
            CreateArrays();

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

    public static void SetIncome() {
        for(int i = 0; i < costs.length; i++) {
            for(int j = 0; j < costs[0].length; j++) {
                income[i][j] = recipients.get(j).value - suppliers.get(i).value - costs[i][j];
                System.out.println("Income: " + i + " -> " + j + " is: " + income[i][j]);
            }
        }
    }

    public static void SetSupplies() {
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

    public static void CountTotalIncome() {
        totalIncome = 0;
        for(int i = 0; i < costs.length; i++)
            for(int j = 0; j < costs[0].length ; j++)
                totalIncome += supplies[i][j] * income[i][j];
        System.out.println("Total income on iteration nr. " + iteration + " is equal: " + totalIncome);
    }

    public static void SetVariables() {
        boolean first = true;
        for(int i = 0; i < costs.length; i++) {
            for(int j = 0; j < costs[0].length; j++) {
                if(supplies[i][j] != 0) {
                    if(first) {
                        alpha[i] = 0;
                        betha[j] = income[i][j];
                        first = false;
                    }
                    else {
                        if(alpha[i] != Math.PI)
                            betha[j] = income[i][j] - alpha[i];
                        else if(betha[j] != Math.PI)
                            alpha[i] = income[i][j] - betha[j];
                        else {
                            alpha[i] = 0;
                            betha[j] = income[i][j];
                        }
                    }
                    deltas[i][j] = 0;
                }
            }
        }

        for(int i = 0; i < costs.length; i++) {
            for (int j = 0; j < costs[0].length; j++) {
                if(supplies[i][j] != 0)
                    deltas[i][j] = 0;
                else
                    deltas[i][j] = income[i][j] - alpha[i] - betha[j];
            }
        }
    }

    public static void Shuffle(int[] change, int row_r, int column_r, int row_c, int column_c) {
        double diff = Math.min(supplies[row_r][column_r], supplies[row_c][column_c]);
        supplies[change[0]][change[1]] += diff;
        supplies[row_c][column_r] += diff;
        supplies[row_r][column_r] -= diff;
        supplies[row_c][column_c] -= diff;
    }

    public static void FindLowest(int[] change) {
        double min_r = Double.MAX_VALUE, min_c = Double.MAX_VALUE;
        int row_r = -1, column_r = -1, row_c = -1, column_c = -1;

        for(int i = 0; i < costs.length; i++) {
            if(supplies[i][change[1]] > 0 && supplies[i][change[1]] < min_c) {
                min_c = supplies[i][change[1]];
                row_c = i;
                column_c = change[1];
            }
        }

        for(int j = 0; j < costs[0].length; j++) {
            if(supplies[change[0]][j] > 0 && supplies[change[0]][j] < min_r) {
                min_r = supplies[change[0]][j];
                column_r = change[0];
                row_r = j;
            }
        }

/*        System.out.println(row_r);
        System.out.println(column_r);
        System.out.println(row_c);
        System.out.println(column_c);*/

        Shuffle(change, row_r, column_r, row_c, column_c);
    }

    public static void PrintSupplies() {
        for(int i = 0; i < costs.length; i++) {
            for(int j = 0; j < costs[0].length; j++) {
                System.out.println("Supplies: " + i + " -> " + j + " is: " + supplies[i][j]);
            }
        }
    }

    public static void main(String[] args) {
        //ReadPeopleData();
        ReadDataFromFile();
        SetIncome();
        SetSupplies();

        boolean toContinue;
        int[] toChange = new int[2];
        while(true) {
            PrintSupplies();
            toContinue = false;

            CountTotalIncome();
            SetVariables();

            for(int i = 0; i < costs.length; i++) {
                for(int j = 0; j < costs[0].length; j++) {
                    if(deltas[i][j] > 0) {
                        toChange[0] = i;
                        toChange[1] = j;
                        toContinue = true;
                        break;
                    }
                }
            }

            if(!toContinue)
                break;

            iteration++;
            FindLowest(toChange);

        }


        for(int i = 0; i < costs.length; i++) {
            System.out.println("Alpha " + i + " = " + alpha[i]);
            for(int j = 0; j < costs[0].length; j++) {
                System.out.println("Betha " + j + " = " + betha[j]);
                System.out.println("Delta: " + deltas[i][j]);
            }
        }
    }
}