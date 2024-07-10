package main.java.ru.clevertec.check.utils;

import main.java.ru.clevertec.check.dataSource.DataProvider;
import main.java.ru.clevertec.check.dataSource.DataProviderImpl;
import main.java.ru.clevertec.check.model.Errors;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

public class EditCSV {

    public static void createErrorCSV(Errors code) {
        try {
            FileWriter csvWriter = new FileWriter("error.csv");

            String time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();

            csvWriter.append("Date").append(Utils.delimiter).append("Time");
            csvWriter.append("\n");
            csvWriter.append(formatter.format(date)).append(Utils.delimiter).append(time);
            csvWriter.append("\n");


            csvWriter.append("ERROR").append("\n").append(Utils.errorText(code));
            csvWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteCSVrow(int lineToRemove) {

        String fileName = DataProviderImpl.pathDiscounts;

        try {
            File tempFile = new File("temp.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {

                if (!currentLine.contains("" + lineToRemove)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }

            reader.close();
            writer.close();

            File oldFile = new File(fileName);
            oldFile.delete();
            tempFile.renameTo(oldFile);

        } catch (IOException ex) {
            System.out.println(Utils.errorText(Errors.BAD_REQ));
        }
    }

    public static void updateCSVproductCount(String toCompare, int newProductQty) {

        String fileName = DataProviderImpl.pathProducts;

        try {
            File tempFile = new File("temp.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {

                if (currentLine.contains(toCompare)) {
                    String[] s = currentLine.split(",");
                    s[3] = String.valueOf(Integer.parseInt(s[3]) - newProductQty);
                    String input = Arrays.toString(s).replace(", ", ",");
                    input = input.substring(1, input.length() - 1);
                    writer.write(input + System.getProperty("line.separator"));

                } else {
                    writer.write(currentLine + System.getProperty("line.separator"));

                }
            }

            reader.close();
            writer.close();

            File oldFile = new File(fileName);
            oldFile.delete();

            tempFile.renameTo(oldFile);

        } catch (IOException ex) {
            System.out.println(Utils.errorText(Errors.BAD_REQ));
        }
    }
}