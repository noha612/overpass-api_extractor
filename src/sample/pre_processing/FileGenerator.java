package sample.pre_processing;


import static sample.constant.FileConstant.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import sample.util.StringUtils;

public class FileGenerator {

  static LinkedHashSet<String> set;

  public static void start() {
    genV();
    genE();
    filter();
//    genN();
  }

  private static void genV() {
    set = new LinkedHashSet<>();
    System.out.println("start read file " + MAP_FILE);
    try {
      File myObj = new File(MAP_FILE);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine().trim();
        if (line.startsWith("<node")) {
          String[] temp = line.split(" ");
          set.add(
              temp[1].substring(temp[1].indexOf("\"") + 1, temp[1].length() - 1) + " " +
                  temp[2].substring(temp[2].indexOf("\"") + 1, temp[2].length() - 1) + " " +
                  temp[3].substring(temp[3].indexOf("\"") + 1, temp[3].length() - 1)
          );
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred " + e);
    }
    System.out.println("done read file " + MAP_FILE);

    System.out.println("start write file " + RAW);
    try {
      File f = new File(RAW);
      if (f.createNewFile()) {
        System.out.println("File created " + f.getName());
      } else {
        System.out.println("File already exists " + f.getName());
      }
    } catch (IOException e) {
      System.out.println("An error occurred " + e);
    }
    try (
        FileWriter fw = new FileWriter(RAW)
    ) {
      System.out.println("begin insert raw node, size: " + set.size());
      for (String i : set) {
        fw.write(i + "\n");
      }
      System.out.println("done");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    System.out.println("done write file");
  }

  private static void genE() {
    set = new LinkedHashSet<>();
    System.out.println("start read file " + MAP_FILE);
    try {
      File myObj = new File(MAP_FILE);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String way = myReader.nextLine().trim();
        if (way.startsWith("<way")) {
          boolean oneWay = false;
          ArrayList<String> nodeInWay = new ArrayList<>();
          String line = myReader.nextLine().trim();
          while (!line.startsWith("</way")) {
            if (line.startsWith("<nd")) {
              line = line.replace("<nd ref=\"", "");
              line = line.replace("\"/>", "");
              nodeInWay.add(line);
            }
            if (line.contains("<tag k=\"oneway\" v=\"yes\"/>")) {
              oneWay = true;
            }
            line = myReader.nextLine().trim();
          }
          if (!nodeInWay.get(0).equalsIgnoreCase(nodeInWay.get(nodeInWay.size() - 1))) {
            for (int i = 0; i < nodeInWay.size(); i++) {
              if (i < nodeInWay.size() - 1) {
                set.add(nodeInWay.get(i) + " " + nodeInWay.get(i + 1));
              }
              if (i > 0 && !oneWay) {
                set.add(nodeInWay.get(i) + " " + nodeInWay.get(i - 1));
              }
            }
          }
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred " + e);
    }
    System.out.println("done read file " + MAP_FILE);

    System.out.println("start write file " + EDGE);
    try {
      File f = new File(EDGE);
      if (f.createNewFile()) {
        System.out.println("File created " + f.getName());
      } else {
        System.out.println("File already exists " + f.getName());
      }
    } catch (IOException e) {
      System.out.println("An error occurred " + e);
    }
    try (
        FileWriter fw = new FileWriter(EDGE)
    ) {
      System.out.println("begin insert E, size: " + set.size());
      for (String i : set) {
        fw.write(i + "\n");
      }
      System.out.println("done");
    } catch (IOException e) {
      System.out.println("An error occurred " + e);
      e.printStackTrace();
    }
    System.out.println("done write file " + EDGE);
  }

  private static void genN() {
    Map<String, String> map = new LinkedHashMap<>();
    System.out.println("start read file " + MAP_FILE);
    try {
      File myObj = new File(MAP_FILE);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String way = myReader.nextLine().trim();
        if (way.startsWith("<way")) {
          ArrayList<String> nodeInWay = new ArrayList<>();
          String line = myReader.nextLine().trim();
          while (!line.startsWith("</way")) {
            if (line.startsWith("<nd")) {
              line = line.replace("<nd ref=\"", "");
              line = line.replace("\"/>", "");
              nodeInWay.add(line);
            }
            if (!nodeInWay.get(0).equalsIgnoreCase(nodeInWay.get(nodeInWay.size() - 1))) {
              if (line.startsWith("<tag k=\"name\" v=\"")) {
                line = line.replace("<tag k=\"name\" v=\"", "");
                line = line.replace("\"/>", "");
                  if (!map.containsKey(line)) {
                      map.put(line, nodeInWay.get(nodeInWay.size() / 2));
                  }
              }
            }
            line = myReader.nextLine().trim();
          }
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred " + e);
    }
    System.out.println("done read file " + MAP_FILE);

    System.out.println("start write file " + NAME);
    try {
      File f = new File(NAME);
      if (f.createNewFile()) {
        System.out.println("File created " + f.getName());
      } else {
        System.out.println("File already exists " + f.getName());
      }
    } catch (IOException e) {
      System.out.println("An error occurred " + e);
    }
    try (
        FileWriter fw = new FileWriter(NAME);
    ) {
      System.out.println("begin insert E, size: " + map.size());
      int count = 0;
      for (Map.Entry<String, String> entry : map.entrySet()) {
        fw.write(count + "::" + entry.getKey() + "::" + entry.getValue() + "\n");
        count++;
      }
      System.out.println("done");
    } catch (IOException e) {
      System.out.println("An error occurred " + e);
      e.printStackTrace();
    }
    System.out.println("done write file " + NAME);
  }

  private static void filter() {
    Set<String> keys = new HashSet<>();
    Set<String> disconnect = new HashSet<>();
    List<String> filterV = new ArrayList<>();
    List<String> filterE = new ArrayList<>();

    System.out.println("start read file " + DISCONNECT);
    try {
      File myObj = new File(DISCONNECT);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine().trim();
        if (StringUtils.isNotEmpty(line)) {
          disconnect.add(line.trim());
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred " + e);
    }
    System.out.println("done read file " + DISCONNECT);

    System.out.println("start read file " + EDGE);
    try {
      File myObj = new File(EDGE);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine().trim();
        if (StringUtils.isNotEmpty(line)) {
          String[] temp = line.split(" ");
          if (!disconnect.contains(temp[0])) {
            keys.add(temp[0]);
            keys.add(temp[1]);
            filterE.add(line);
          }
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred " + e);
    }
    System.out.println("done read file " + EDGE);

    System.out.println("start read file " + RAW);
    try {
      File myObj = new File(RAW);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine().trim();
        if (StringUtils.isNotEmpty(line)) {
          String[] temp = line.split(" ");
          if (keys.contains(temp[0])) {
            filterV.add(temp[0] + " " + temp[1] + " " + temp[2]);
          }
        }
      }
      myReader.close();
    } catch (
        FileNotFoundException e) {
      System.out.println("An error occurred " + e);
    }

    System.out.println("start write file " + VERTEX);
    try {
      File f = new File(VERTEX);
      if (f.createNewFile()) {
        System.out.println("File created " + f.getName());
      } else {
        System.out.println("File already exists " + f.getName());
      }
    } catch (IOException e) {
      System.out.println("An error occurred " + e);
    }
    try (
        FileWriter fw = new FileWriter(VERTEX);
    ) {
      System.out.println("begin insert V, size: " + filterV.size());
      for (String i : filterV) {
        fw.write(i + "\n");
      }
      System.out.println("done");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    System.out.println("done write file");

    System.out.println("start write file " + EDGE);
    try {
      File f = new File(EDGE);
      if (f.createNewFile()) {
        System.out.println("File created " + f.getName());
      } else {
        System.out.println("File already exists " + f.getName());
      }
    } catch (IOException e) {
      System.out.println("An error occurred " + e);
    }
    try (
        FileWriter fw = new FileWriter(EDGE);
    ) {
      System.out.println("begin insert E, size: " + filterE.size());
      for (String i : filterE) {
        fw.write(i + "\n");
      }
      System.out.println("done");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    System.out.println("done write file");
  }
}

