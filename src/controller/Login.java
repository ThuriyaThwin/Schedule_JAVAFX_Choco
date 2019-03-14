package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Login implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void loginAction(ActionEvent event) {
        String Username = usernameField.getText();
        String password = passwordField.getText();

        if ((Username.equals("root")) && (password.equals("root"))){
            openDashboard();
        }else{
            Alert alert = new Alert(Alert.AlertType.NONE, "Username atau password salah.", ButtonType.OK);
            alert.showAndWait();
            alert.setTitle("Gagal login.");
        }
    }

    private void openDashboard() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
