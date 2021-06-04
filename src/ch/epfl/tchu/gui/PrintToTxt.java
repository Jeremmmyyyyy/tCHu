package ch.epfl.tchu.gui;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrintToTxt {


    public static void createFile(String path){
        try {
            File myObj = new File(path);
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

    public static void writeToFile(String path, String textToWrite){
        try {
//            File f = new File( path);
            FileWriter pw = new FileWriter(path);
            pw.append(textToWrite);
            pw.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void deleteFile(String path){
        File f = new File(path);
        if(f.delete()){
            System.out.println("File deleted " + f.getName());
        }else{
            System.out.println("Failed to delete the File");
        }
    }
}

