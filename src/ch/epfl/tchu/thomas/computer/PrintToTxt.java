package ch.epfl.tchu.thomas.computer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrintToTxt {
//    private static DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    private static LocalDateTime now = LocalDateTime.now();
//    private static String fileId;

    public static void createFile(String fileName){
        try {
            File myObj = new File(fileName+ ".txt");
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

    public static void writeToFile(String fileId, String textToWrite){
        try {
            File f = new File( fileId+ ".txt");
            PrintWriter pw = new PrintWriter(new FileOutputStream(f,true));
            pw.append(textToWrite);
            pw.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void deleteFile(String fileId){
        File f = new File(fileId+ ".txt");
        if(f.delete()){
            System.out.println("File deleted " + f.getName());
        }else{
            System.out.println("Failed to delete the File");
        }
    }
}
