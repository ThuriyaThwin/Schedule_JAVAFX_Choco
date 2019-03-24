package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    public Button btnKelolaKelas;
    public Button btnKelolaDosen;
    public Button btnKelolaMatkul;
    public Button btnKelolaRuangan;
    public Button btnKelolaJadwal;

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
    private void kelolaDosenAction() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/kelola_dosen.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void kelolaKelasAction() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/kelola_kelas.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){ e.printStackTrace(); }
    }

    @FXML
    private void kelolaMatkulAction() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/kelola_matkul.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){ e.printStackTrace(); }
    }

    @FXML
    private void kelolaRuanganAction() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/kelola_ruangan.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){ e.printStackTrace(); }
    }

    @FXML
    private void kelolaJadwalAction() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/generate_csp.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){ e.printStackTrace(); }
    }

}
