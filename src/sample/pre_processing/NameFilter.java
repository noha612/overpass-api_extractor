package sample.pre_processing;


import static sample.constant.FileConstant.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import sample.util.CommonUtil;
import sample.util.StringUtils;

public class NameFilter {

    private static Map<String, Double[]> listV;
    private static Map<String, Double[]> listR;

    public static void start() {

        loadVertex();
        loadRawNode();
        vertexToName();
        nameToVertex();

    }

    private static void vertexToName() {
        Set<String> set = new LinkedHashSet<>();

        Map<String, String> map = new LinkedHashMap<>();
        String node = "";
        String name = "";
        String number = "";
        String street = "";
        String district = "";
        String subDistrict = "";
        String subName = "";

        try {
            System.out.println("start read file " + MAP_FILE);
            File myObj = new File(MAP_FILE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String way = myReader.nextLine().trim();

                if (way.startsWith("<node")) {
                    String[] temp = way.split(" ");

                    node = temp[1].substring(temp[1].indexOf("\"") + 1, temp[1].length() - 1);

                    String line = myReader.nextLine().trim();
                    while (!line.startsWith("</node") && !line.startsWith("<node")) {
                        if (line.startsWith("<tag k=\"name\" ")) {
                            line = line.replace("<tag k=\"name\" v=\"", "");
                            line = line.replace("\"/>", "");
                            name = line;
                        } else if (line.startsWith("<tag k=\"addr:housenumber\"")) {
                            line = line.replace("<tag k=\"addr:housenumber\" v=\"", "");
                            line = line.replace("\"/>", "");
                            number = line;
                        } else if (line.startsWith("<tag k=\"addr:street\" ")) {
                            line = line.replace("<tag k=\"addr:street\" v=\"", "");
                            line = line.replace("\"/>", "");
                            street = line;
                        } else if (line.startsWith("<tag k=\"addr:subdistrict\" ")) {
                            line = line.replace("<tag k=\"addr:subdistrict\" v=\"", "");
                            line = line.replace("\"/>", "");
                            subDistrict = line;
                        } else if (line.startsWith("<tag k=\"addr:district\" ")) {
                            line = line.replace("<tag k=\"addr:district\" v=\"", "");
                            line = line.replace("\"/>", "");
                            district = line;
                        }
                        line = myReader.nextLine().trim();
                    }
//          if (StringUtils.isNotEmpty(subDistrict) && StringUtils.isNotEmpty(district)) {
//            subName = subDistrict + ", " + district;
//          } else {
//            subName = subDistrict + district;
//          }
                    if (StringUtils.isNotBlank(name)) {
                        map.put(name, node);
                    }
                    if (StringUtils.isNotBlank(street)) {
                        if (StringUtils.isNotEmpty(number)) {
                            if (number.toLowerCase().startsWith("so") || number.toLowerCase()
                                    .startsWith("số")) {
                                map.put(number + " " + street, node);
                            } else {
                                map.put("Số " + number + " " + street, node);
                            }
                        } else {
                            map.put(street, node);
                        }
                    }
                }

                if (way.startsWith("<way")) {
                    ArrayList<String> nodeInWay = new ArrayList<>();
                    String line = myReader.nextLine().trim();
                    while (!line.startsWith("</way")) {
                        if (line.startsWith("<nd")) {
                            line = line.replace("<nd ref=\"", "");
                            line = line.replace("\"/>", "");
                            nodeInWay.add(line);
                        }
                        if (line.startsWith("<tag k=\"name\" v=\"")) {
                            line = line.replace("<tag k=\"name\" v=\"", "");
                            line = line.replace("\"/>", "");
                            name = line;
                        } else if (line.startsWith("<tag k=\"addr:subdistrict\" ")) {
                            line = line.replace("<tag k=\"addr:subdistrict\" v=\"", "");
                            line = line.replace("\"/>", "");
                            subDistrict = line;
                        } else if (line.startsWith("<tag k=\"addr:district\" ")) {
                            line = line.replace("<tag k=\"addr:district\" v=\"", "");
                            line = line.replace("\"/>", "");
                            district = line;
                        }
                        line = myReader.nextLine().trim();
                    }
//          if (StringUtils.isNotEmpty(subDistrict) && StringUtils.isNotEmpty(district)) {
//            subName = subDistrict + ", " + district;
//          } else {
//            subName = subDistrict + district;
//          }
                    if (!map.containsKey(name) && StringUtils.isNotBlank(name)) {
                        map.put(name, nodeInWay.get(nodeInWay.size() / 2));
                    }
                }
                node = "";
                name = "";
                number = "";
                street = "";
                subDistrict = "";
                district = "";
                subName = "";
            }
            myReader.close();

        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred " + e);
        }
        System.out.println("done read file " + MAP_FILE);

        int c = 0;
        for (Map.Entry<String, Double[]> entry : listV.entrySet()) {
            double d = Double.MAX_VALUE;
            String nn = "blabla";
            for (Map.Entry<String, String> entry2 : map.entrySet()) {
                double temp = CommonUtil.haversineFormula(entry.getValue()[0], entry.getValue()[1],
                        listR.get(entry2.getValue())[0], listR.get(entry2.getValue())[1]);
                if (temp < d) {
                    d = temp;
                    nn = entry2.getKey();
                }
            }
            set.add(entry.getKey() + "::" + nn);
            c++;
            System.out.println(c + "");
        }

        System.out.println("start write file " + VERTEX_NAME);
        try {
            File f = new File(VERTEX_NAME);
            if (f.createNewFile()) {
                System.out.println("File created " + f.getName());
            } else {
                System.out.println("File already exists " + f.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred " + e);
        }
        try (
                FileWriter fw = new FileWriter(VERTEX_NAME)
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

    private static void nameToVertex() {
        Map<String, String> map = new LinkedHashMap<>();
        String node = "";
        String name = "";
        String number = "";
        String street = "";
        String district = "";
        String subDistrict = "";
        String subName = "";
        int cC = 0;

        try {
            System.out.println("start read file " + MAP_FILE);
            File myObj = new File(MAP_FILE);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String way = myReader.nextLine().trim();

                if (way.startsWith("<node")) {
                    String[] temp = way.split(" ");

                    node = temp[1].substring(temp[1].indexOf("\"") + 1, temp[1].length() - 1);

                    String line = myReader.nextLine().trim();
                    while (!line.startsWith("</node") && !line.startsWith("<node")) {
                        if (line.startsWith("<tag k=\"name\" ")) {
                            line = line.replace("<tag k=\"name\" v=\"", "");
                            line = line.replace("\"/>", "");
                            name = line;
                        } else if (line.startsWith("<tag k=\"addr:housenumber\"")) {
                            line = line.replace("<tag k=\"addr:housenumber\" v=\"", "");
                            line = line.replace("\"/>", "");
                            number = line;
                        } else if (line.startsWith("<tag k=\"addr:street\" ")) {
                            line = line.replace("<tag k=\"addr:street\" v=\"", "");
                            line = line.replace("\"/>", "");
                            street = line;
                        } else if (line.startsWith("<tag k=\"addr:subdistrict\" ")) {
                            line = line.replace("<tag k=\"addr:subdistrict\" v=\"", "");
                            line = line.replace("\"/>", "");
                            subDistrict = line;
                        } else if (line.startsWith("<tag k=\"addr:district\" ")) {
                            line = line.replace("<tag k=\"addr:district\" v=\"", "");
                            line = line.replace("\"/>", "");
                            district = line;
                        }
                        line = myReader.nextLine().trim();
                    }

                    if (StringUtils.isNotEmpty(subDistrict) && StringUtils.isNotEmpty(district)) {
                        subName = subDistrict + ", " + district;
                    } else if (StringUtils.isNotEmpty(district)) {
                        subName = district;
                    } else {
//                        subName = CommonUtil
//                                .getDistrict(
//                                        new GeoPoint(listR.get(node)[0], listR.get(node)[1]));
                    }

                    if (StringUtils.isNotBlank(name)) {
                        if (StringUtils.isBlank(subName)) {
                            cC++;
                        }
                        map.put(name + " ~ " + subName, node);
                    }
                    if (StringUtils.isNotBlank(street)) {
                        if (StringUtils.isNotEmpty(number)) {
                            if (number.toLowerCase().startsWith("so") || number.toLowerCase()
                                    .startsWith("số")) {
                                if (StringUtils.isBlank(subName)) {
                                    cC++;
                                }
                                map.put(number + " " + street + " ~ " + subName, node);
                            } else {
                                if (StringUtils.isBlank(subName)) {
                                    cC++;
                                }
                                map.put("Số " + number + " " + street + " ~ " + subName, node);
                            }
                        } else {
                            if (StringUtils.isBlank(subName)) {
                                cC++;
                            }
                            map.put(street + " ~ " + subName, node);
                        }
                    }
                }

                if (way.startsWith("<way")) {
                    ArrayList<String> nodeInWay = new ArrayList<>();
                    String line = myReader.nextLine().trim();
                    while (!line.startsWith("</way")) {
                        if (line.startsWith("<nd")) {
                            line = line.replace("<nd ref=\"", "");
                            line = line.replace("\"/>", "");
                            nodeInWay.add(line);
                        }
                        if (line.startsWith("<tag k=\"name\" v=\"")) {
                            line = line.replace("<tag k=\"name\" v=\"", "");
                            line = line.replace("\"/>", "");
                            name = line;
                        } else if (line.startsWith("<tag k=\"addr:subdistrict\" ")) {
                            line = line.replace("<tag k=\"addr:subdistrict\" v=\"", "");
                            line = line.replace("\"/>", "");
                            subDistrict = line;
                        } else if (line.startsWith("<tag k=\"addr:district\" ")) {
                            line = line.replace("<tag k=\"addr:district\" v=\"", "");
                            line = line.replace("\"/>", "");
                            district = line;
                        }
                        line = myReader.nextLine().trim();
                    }

                    if (StringUtils.isNotEmpty(subDistrict) && StringUtils.isNotEmpty(district)) {
                        subName = subDistrict + ", " + district;
                    } else if (StringUtils.isNotEmpty(district)) {
                        subName = district;
                    } else {
//                        subName = CommonUtil
//                                .getDistrict(
//                                        new GeoPoint(listR.get(nodeInWay.get(nodeInWay.size() / 2))[0],
//                                                listR.get(nodeInWay.get(nodeInWay.size() / 2))[1]));
                    }

                    if (StringUtils.isNotBlank(name) && !map.containsKey(name + " ~ " + subName)) {
                        if (StringUtils.isBlank(subName)) {
                            cC++;
                        }
                        map.put(name + " ~ " + subName, nodeInWay.get(nodeInWay.size() / 2));
                    }
                }

                node = "";
                name = "";
                number = "";
                street = "";
                subDistrict = "";
                district = "";
                subName = "";
            }
            myReader.close();

        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred " + e);
        }
        System.out.println("done read file " + MAP_FILE);

        int c = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Double[] d = listR.get(entry.getValue());
            map.put(entry.getKey(), findLocationByPoint(d[0], d[1]));
            System.out.println(c++);
        }

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
                if (StringUtils.isNotBlank(entry.getKey())) {
                    fw.write(count + "::" + entry.getKey() + "::" + entry.getValue() + "\n");
                    count++;
                }
            }
            System.out.println("done");
        } catch (IOException e) {
            System.out.println("An error occurred " + e);
            e.printStackTrace();
        }
        System.out.println("done write file " + NAME);

    }

    private static void loadVertex() {
        listV = new LinkedHashMap<>();

        System.out.println("start read file " + VERTEX);
        try {
            File myObj = new File(VERTEX);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");
                    String key = temp[0];
                    Double[] array = new Double[2];
                    for (int i = 0; i < array.length; i++) {
                        array[i] = Double.parseDouble(temp[i + 1]);
                    }
                    listV.put(key, array);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred " + e);
        }
        System.out.println("done read file " + VERTEX + ", total V: " + listV.size());
    }

    private static void loadRawNode() {
        listR = new LinkedHashMap<>();

        System.out.println("start read file " + RAW);
        try {
            File myObj = new File(RAW);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (StringUtils.isNotEmpty(line)) {
                    String[] temp = line.split(" ");
                    String key = temp[0];
                    Double[] array = new Double[2];
                    for (int i = 0; i < array.length; i++) {
                        array[i] = Double.parseDouble(temp[i + 1]);
                    }
                    listR.put(key, array);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred " + e);
        }
        System.out.println("done read file " + RAW + ", total V: " + listR.size());
    }

    private static String findLocationByPoint(double lat, double lng) {
        double d = Double.MAX_VALUE;
        String id = "blabla";

        for (Map.Entry<String, Double[]> entry : listV.entrySet()) {
            double temp = CommonUtil
                    .haversineFormula(lat, lng, entry.getValue()[0], entry.getValue()[1]);
            if (temp < d) {
                d = temp;
                id = entry.getKey();
            }
        }

        return id;
    }
}
