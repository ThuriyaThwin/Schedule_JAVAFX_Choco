package controller;

import helper.SQLHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.JadwalCSP;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputCSP implements Initializable {

    @FXML
    private AnchorPane outputCSPPane;
    @FXML
    private Text totalData;
    @FXML
    private TableView<JadwalCSP> tblDataJadwal;
    @FXML
    private TableColumn<JadwalCSP, String> tblKolomDosen;
    @FXML
    private TableColumn<JadwalCSP, String> tblKolomHari;
    @FXML
    private TableColumn<JadwalCSP, String> tblKolomSesi;
    @FXML
    private TableColumn<JadwalCSP, String> tblKolomMatkul;
    @FXML
    private TableColumn<JadwalCSP, String> tblKolomKelas;

    public AnchorPane pane;
    public Button btnDashboard;
    public Button toDijkstra;

    private ObservableList<JadwalCSP> ol;
    private Connection connec;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = SQLHelper.getConnection();
        ol = FXCollections.observableArrayList();
        loadDataFromDatabase();
        setCellValue();
    }

    @FXML
    private void toDashboard() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            outputCSPPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void toDijkstra() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            outputCSPPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setCellValue() {
        tblKolomDosen.setCellValueFactory(new PropertyValueFactory<>("dosen"));
        tblKolomHari.setCellValueFactory(new PropertyValueFactory<>("hari"));
        tblKolomKelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        tblKolomMatkul.setCellValueFactory(new PropertyValueFactory<>("matkul"));
        tblKolomSesi.setCellValueFactory(new PropertyValueFactory<>("sesi"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql = "SELECT jadwal.id_jadwal, " +
                    "jadwal.no_dosen AS dosen_id, " +
                    "jadwal.no_matkul AS matkul_id, " +
                    "jadwal.no_hari AS hari_id, " +
                    "jadwal.no_sesi AS sesi_id, " +
                    "jadwal.no_kelas AS kelas_id, " +
                    "dosen.nama AS dosen, " +
                    "matkul.nama AS matkul, " +
                    "kelas.nama AS kelas, " +
                    "hari.nama AS hari, " +
                    "sesi.nama AS sesi " +
                    "FROM jadwal " +
                    "INNER JOIN matkul ON jadwal.no_matkul = matkul.no " +
                    "INNER JOIN dosen ON jadwal.no_dosen = dosen.no " +
                    "INNER JOIN kelas ON jadwal.no_kelas = kelas.no " +
                    "INNER JOIN hari ON jadwal.no_hari = hari.no " +
                    "INNER JOIN sesi ON jadwal.no_sesi = sesi.no " +
                    "ORDER BY jadwal.no_hari DESC, jadwal.no_sesi";
            ResultSet rs = connec.createStatement().executeQuery(sql);

            while (rs.next()) {
                ol.add(new JadwalCSP(rs.getInt(1),                      //id_jadwal
                        rs.getInt(2), rs.getString(7),      //[dosen]
                        rs.getInt(4), rs.getString(10),     //[hari]
                        rs.getInt(5), rs.getString(11),     //[sesi]
                        rs.getInt(3), rs.getString(8),      //[matkul]
                        rs.getInt(6), rs.getString(9)));    //[kelas]
            }

            totalData.setText("Total Jadwal : " + ol.size());
        } catch (SQLException ex) {
            Logger.getLogger(OutputCSP.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataJadwal.setItems(ol);
    }
}
