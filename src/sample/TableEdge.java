//package sample;
//
//import static sample.constant.FileConstant.MAP_FILE;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.net.URL;
//import java.util.LinkedHashSet;
//import java.util.ResourceBundle;
//import java.util.Scanner;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import sample.model.Node;
//
//public class TableEdge implements Initializable {
//
//  @FXML
//  private Button btn1;
//
//  @FXML
//  private TableColumn idCol;
//
//  @FXML
//  private TableColumn latCol;
//
//  @FXML
//  private TableColumn lngCol;
//
//  @FXML
//  private TableView table;
//
//  @Override
//  public void initialize(URL url, ResourceBundle resourceBundle) {
//  }
//
//  public void show() {
//    genV();
//  }
//
//  private void genV() {
//
//    LinkedHashSet<Node> set = new LinkedHashSet<>();
//    System.out.println("start read file " + MAP_FILE);
//    try {
//      File myObj = new File(MAP_FILE);
//      Scanner myReader = new Scanner(myObj);
//      int i = 1;
//      while (myReader.hasNextLine()) {
//        String line = myReader.nextLine().trim();
//        if (line.startsWith("<node")) {
//          String[] temp = line.split(" ");
//          set.add(
//              new Node(
//                  temp[1].substring(temp[1].indexOf("\"") + 1, temp[1].length() - 1),
//                  temp[2].substring(temp[2].indexOf("\"") + 1, temp[2].length() - 1),
//                  temp[3].substring(temp[3].indexOf("\"") + 1, temp[3].length() - 1))
//          );
//          i++;
//        }
//      }
//      myReader.close();
//    } catch (FileNotFoundException e) {
//      System.out.println("An error occurred " + e);
//    }
//    System.out.println("done read file " + MAP_FILE);
//
//    idCol.setCellValueFactory(new PropertyValueFactory<Node, String>("id"));
//    latCol.setCellValueFactory(new PropertyValueFactory<Node, String>("lat"));
//    lngCol.setCellValueFactory(new PropertyValueFactory<Node, String>("lng"));
//
//    ObservableList<Node> items = FXCollections.observableArrayList(set);
////    lv.setItems(items);
//    table.setItems(items);
//  }
//}
