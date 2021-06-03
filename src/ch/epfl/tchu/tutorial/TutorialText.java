package ch.epfl.tchu.tutorial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TutorialText {

    public final static List<String> entireText = new ArrayList<>();
    public static int index = -1;

    public TutorialText() {
        try { //TODO changer le path
            BufferedReader reader = new BufferedReader(new FileReader("./resources/tutorial.txt"));
            String readLine;
            while ((readLine = reader.readLine()) != null) {
                entireText.add(readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readText() {
        for (String s : entireText) {
            System.out.println(s);
        }
    }

    public String nextLine() {
        ++index;
        return index != entireText.size() ? entireText.get(index) : null;
    }


}