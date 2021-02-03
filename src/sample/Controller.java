package sample;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

  @FXML
  private Button btn1;

  @FXML
  private TextField txt1;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }

  public void show(){
    txt1.setText(new Date().toString());
  }
}
