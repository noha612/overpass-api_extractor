package sample.pre_processing;

import static sample.constant.FileConstant.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import sample.util.StringUtils;

public class InvertedIndex {

  public static void start() {
    preProcessing();
  }

  private static void preProcessing() {
    Map<String, String> map = new HashMap<>();

    System.out.println("start read file " + NAME);
    try {
      File myObj = new File(NAME);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine().trim();
        if (StringUtils.isNotEmpty(line)) {
          String[] temp = line.split("::");
          for (String i : temp[1].split("~")[0].trim().split("\\s+")) {
            i = StringUtils.removeAccents(i);
            i = i.toLowerCase();
            String arrayValue = map.get(i) == null ? temp[0] : map.get(i) + "," + temp[0];
            map.put(i, arrayValue);
          }
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred " + e);
    }
    System.out.println("done read file " + NAME);

    System.out.println("start write file " + INVERTED);
    try {
      File f = new File(INVERTED);
      if (f.createNewFile()) {
        System.out.println("File created " + f.getName());
      } else {
        System.out.println("File already exists " + f.getName());
      }
    } catch (IOException e) {
      System.out.println("An error occurred " + e);
    }
    try (
        FileWriter fw = new FileWriter(INVERTED);
    ) {
      System.out.println("begin insert inverted index");
      for (Map.Entry<String, String> entry : map.entrySet()) {
        fw.write(entry.getKey() + " = {" + entry.getValue() + "}\n");
      }
      System.out.println("done");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    System.out.println("done write file");
  }
}
