package ch.epfl.tchu.thomas.computer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrintToTxt {
    private static DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static LocalDateTime now = LocalDateTime.now();
    private static String fileId;

    public static void createFile(String fileName){
        try {
            File myObj = new File(formatDate.format(now)  + " " +fileName+ ".txt");
            fileId = fileName;
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                throw new IllegalArgumentException("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeToFile(String textToWrite){
        try {
            File f = new File(formatDate.format(now)  + " " +fileId+ " Log.txt");
            PrintWriter pw = new PrintWriter(new FileOutputStream(f,true));
            pw.append(textToWrite);
            pw.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
