package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    @FXML
    private AnchorPane pane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void kelolaDosenAction(ActionEvent event) {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/kelola_dosen.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void kelolaKelasAction(ActionEvent event) {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/kelola_kelas.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){ e.printStackTrace(); }
    }

    @FXML
    private void kelolaMatkulAction(ActionEvent event) {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/kelola_matkul.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){ e.printStackTrace(); }
    }

    @FXML
    private void kelolaRuanganAction(ActionEvent event) {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/kelola_ruangan.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){ e.printStackTrace(); }
    }

    @FXML
    private void kelolaJadwalAction(ActionEvent event) {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/generate_jadwal.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){ e.printStackTrace(); }
    }

}
