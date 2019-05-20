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
import model.Jadwal;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Output implements Initializable {

    @FXML
    private AnchorPane outputPane;
    @FXML
    private Text totalData;
    @FXML
    private TableView<Jadwal> tblDataJadwal;
    @FXML
    private TableColumn<Jadwal, String> tblKolomDosen;
    @FXML
    private TableColumn<Jadwal, String> tblKolomMatkul;
    @FXML
    private TableColumn<Jadwal, String> tblKolomKelas;
    @FXML
    private TableColumn<Jadwal, String> tblKolomSesi;
    @FXML
    private TableColumn<Jadwal, String> tblKolomRuangan;
    @FXML
    private TableColumn<Jadwal, String> tblKolomKategori;
    @FXML
    private TableColumn<Jadwal, String> tblKolomHari;

    public Button btnDashboard;

    private ObservableList<Jadwal> ol;
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
            outputPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setCellValue() {
        tblKolomDosen.setCellValueFactory(new PropertyValueFactory<>("dosen"));
        tblKolomMatkul.setCellValueFactory(new PropertyValueFactory<>("mataKuliah"));
        tblKolomKelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        tblKolomSesi.setCellValueFactory(new PropertyValueFactory<>("sesi"));
        tblKolomRuangan.setCellValueFactory(new PropertyValueFactory<>("ruangan"));
        tblKolomKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        tblKolomHari.setCellValueFactory(new PropertyValueFactory<>("hari"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql_jadwal = "SELECT jadwal.id_jadwal AS id, " +
                    "jadwal.no_dosen AS dosen_id, " +
                    "jadwal.no_matkul AS matkul_id, " +
                    "jadwal.no_hari AS hari_id, " +
                    "jadwal.no_sesi AS sesi_id, " +
                    "jadwal.no_kelas AS kelas_id, " +
                    "jadwal.no_ruangan AS ruangan_id, " +
                    "dosen.nama AS dosen, " +
                    "matkul.nama AS matkul, " +
                    "kelas.nama AS kelas, " +
                    "hari.nama AS hari, " +
                    "sesi.nama AS sesi, " +
                    "ruangan.nama AS ruangan, " +
                    "kategori.nama AS kategori " +
                    "FROM jadwal " +
                    "INNER JOIN matkul ON jadwal.no_matkul = matkul.no " +
                    "INNER JOIN dosen ON jadwal.no_dosen = dosen.no " +
                    "INNER JOIN kelas ON jadwal.no_kelas = kelas.no " +
                    "INNER JOIN hari ON jadwal.no_hari = hari.no " +
                    "INNER JOIN sesi ON jadwal.no_sesi = sesi.no " +
                    "INNER JOIN ruangan ON jadwal.no_ruangan = ruangan.no " +
                    "INNER JOIN kategori ON jadwal.kategori = kategori.no " +
                    "ORDER BY jadwal.no_hari DESC, jadwal.no_sesi DESC, jadwal.no_kelas DESC";
            ResultSet rs_jadwal = connec.createStatement().executeQuery(sql_jadwal);

            while (rs_jadwal.next()){
                Jadwal jadwal = new Jadwal();

                jadwal.setId(rs_jadwal.getString("id"));
                jadwal.setDosen(rs_jadwal.getString("dosen"));
                jadwal.setDosenId(rs_jadwal.getString("dosen_id"));
                jadwal.setMataKuliah(rs_jadwal.getString("matkul"));
                jadwal.setMataKuliahId(rs_jadwal.getString("matkul_id"));
                jadwal.setKelas(rs_jadwal.getString("kelas"));
                jadwal.setKelasId(rs_jadwal.getString("kelas_id"));
                jadwal.setHari(rs_jadwal.getString("hari"));
                jadwal.setHariId(rs_jadwal.getString("hari_id"));
                jadwal.setSesi(rs_jadwal.getString("sesi"));
                jadwal.setSesiId(rs_jadwal.getString("sesi_id"));
                jadwal.setRuangan(rs_jadwal.getString("ruangan"));
                jadwal.setRuanganId(rs_jadwal.getString("ruangan_id"));
                jadwal.setKategori(rs_jadwal.getString("kategori"));

                ol.add(jadwal);
            }

            totalData.setText("Total Data : " + ol.size());
        } catch (SQLException ex) {
            System.out.println("Erorr");
        }
        tblDataJadwal.setItems(ol);
    }
}
