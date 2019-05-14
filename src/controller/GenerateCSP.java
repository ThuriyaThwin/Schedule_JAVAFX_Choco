package controller;

import helper.AutoCompleteBoxHelper;
import helper.CSPHelper;
import helper.SQLHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Jadwal;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerateCSP implements Initializable {

    @FXML
    private AnchorPane kelolaJadwalPane;
    @FXML
    private ComboBox<String> dosenCombo;
    @FXML
    private ComboBox<String> matkulCombo;
    @FXML
    private ComboBox<String> kelasCombo;
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

    public AnchorPane pane;
    public Button btnTambah;
    public Button btnUpdate;
    public Button btnDashboard;
    public Button btnHapusMatkulKelas;
    public Button btnHapusDosenMatkul;
    public Button btnHapusDosenMatkulKelas;
    public Button btnGenerateJadwal;
    public Text text;

    private ObservableList<Jadwal> ol;
    private ObservableList<String> dosen;
    private ObservableList<String> mata_kuliah;
    private ObservableList<String> kelas;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs_jadwal;
    private String id_dosen = null;
    private String id_mata_kuliah = null;
    private String id_kelas = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = SQLHelper.getConnection();
        ol = FXCollections.observableArrayList();
        dosen = FXCollections.observableArrayList();
        mata_kuliah = FXCollections.observableArrayList();
        kelas = FXCollections.observableArrayList();
        loadDataFromDatabase();
        fromTableToTextField();
        setCellValue();
        fillComboBox();
    }

    @FXML
    private void tambahJadwalAction() {

        try {
            String sql = "SELECT * FROM dosen WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, dosenCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_dosen = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        try {
            String sql = "SELECT * FROM matkul WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, matkulCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_mata_kuliah = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        try {
            String sql = "SELECT * FROM kelas WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_kelas = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        updateDosenMatkul();
        updateMatkulKelas();
        clearText();
        loadDataFromDatabase();
    }

    @FXML
    private void updateJadwalAction() {
//        try {
//            String sql = "SELECT * FROM dosen WHERE nama=?";
//            prs = connec.prepareStatement(sql);
//            prs.setString(1, dosenCombo.getSelectionModel().getSelectedItem());
//            rs_jadwal = prs.executeQuery();
//
//            while (rs_jadwal.next()){
//                id_dosen = rs_jadwal.getString("nip");
//            }
//        } catch (SQLException e) {
//            System.out.println("Erorr");
//        }
//
//        try {
//            String sql = "SELECT * FROM mata_kuliah WHERE nama=?";
//            prs = connec.prepareStatement(sql);
//            prs.setString(1, dosenCombo.getSelectionModel().getSelectedItem());
//            rs_jadwal = prs.executeQuery();
//
//            while (rs_jadwal.next()){
//                id_mata_kuliah = rs_jadwal.getString("id_mata_kuliah");
//            }
//        } catch (SQLException e) {
//            System.out.println("Erorr");
//        }
//
//        try {
//            String sql = "SELECT * FROM kelas WHERE nama=?";
//            prs = connec.prepareStatement(sql);
//            prs.setString(1, dosenCombo.getSelectionModel().getSelectedItem());
//            rs_jadwal = prs.executeQuery();
//
//            while (rs_jadwal.next()){
//                id_kelas = rs_jadwal.getString("id_kelas");
//            }
//        } catch (SQLException e) {
//            System.out.println("Erorr");
//        }
//
//        try {
//            String sql = "UPDATE jadwal SET dosen=?, mata_kuliah=?, kelas=? WHERE id_jadwal=?";
//            prs = connec.prepareStatement(sql);
//            prs.setString(1, id_dosen);
//            prs.setString(2, id_mata_kuliah);
//            prs.setString(3, id_kelas);
////            prs.setString(4, id_jadwal);
//            int exec = prs.executeUpdate();
//
//            if(exec == 1){
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Update berhasil", ButtonType.OK);
//                alert.setTitle("Update");
//                alert.showAndWait();
//                loadDataFromDatabase();
////                clearText();
//            }
//
//            prs.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(GenerateCSP.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @FXML
    private void toDashboard() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            kelolaJadwalPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setCellValue() {
        tblKolomDosen.setCellValueFactory(new PropertyValueFactory<>("dosen"));
        tblKolomMatkul.setCellValueFactory(new PropertyValueFactory<>("mataKuliah"));
        tblKolomKelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql_jadwal = "SELECT dosen_matkul.no_dosen AS dosen_id, dosen_matkul.no_matkul AS matkul_id, " +
                    "matkul_kelas.no_kelas AS kelas_id, dosen.nama AS dosen, " +
                    "matkul.nama AS matkul, kelas.nama AS kelas " +
                    "FROM dosen_matkul " +
                    "INNER JOIN matkul_kelas ON dosen_matkul.no_matkul = matkul_kelas.no_matkul " +
                    "INNER JOIN dosen ON dosen_matkul.no_dosen = dosen.no " +
                    "INNER JOIN matkul ON dosen_matkul.no_matkul = matkul.no " +
                    "INNER JOIN kelas ON matkul_kelas.no_kelas = kelas.no " +
                    "WHERE dosen_matkul.nilai=1 AND matkul_kelas.nilai=1";
            rs_jadwal = connec.createStatement().executeQuery(sql_jadwal);

            while (rs_jadwal.next()) {
                ol.add(new Jadwal(rs_jadwal.getString("dosen"), rs_jadwal.getString("dosen_id"), rs_jadwal.getString("matkul"), rs_jadwal.getString("matkul_id"), rs_jadwal.getString("kelas"), rs_jadwal.getString("kelas_id")));
            }

            totalData.setText("Total Data : " + ol.size());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateCSP.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataJadwal.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataJadwal.setOnMouseClicked((MouseEvent event) -> {
            Jadwal jadwal = tblDataJadwal.getItems().get(tblDataJadwal.getSelectionModel().getSelectedIndex());
            if (jadwal != null){
                System.out.println(jadwal.getDosenId());
                id_dosen = jadwal.getDosenId();
                id_mata_kuliah = jadwal.getMataKuliahId();
                id_kelas = jadwal.getKelasId();
                dosenCombo.setValue(jadwal.getDosen());
                matkulCombo.setValue(jadwal.getMataKuliah());
                kelasCombo.setValue(jadwal.getKelas());
            }
        });
    }

    private void clearText(){
        dosenCombo.setValue("");
        matkulCombo.setValue("");
        kelasCombo.setValue("");
    }

    public void onClickKelasCombo() {
        String sql = "SELECT * FROM kelas WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_kelas = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClickMatkulCombo() {
        String sql = "SELECT * FROM matkul WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_mata_kuliah = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClickDosenCombo() {
        String sql = "SELECT * FROM dosen WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_dosen = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillComboBox(){
        dosenCombo.setEditable(true);
        matkulCombo.setEditable(true);
        kelasCombo.setEditable(true);

        try {
            String sql = "SELECT * FROM dosen";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                dosen.add(rs_jadwal.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM matkul";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                mata_kuliah.add(rs_jadwal.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM kelas";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                kelas.add(rs_jadwal.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dosenCombo.setItems(dosen);
        matkulCombo.setItems(mata_kuliah);
        kelasCombo.setItems(kelas);
        new AutoCompleteBoxHelper(dosenCombo);
        new AutoCompleteBoxHelper(matkulCombo);
        new AutoCompleteBoxHelper(kelasCombo);
    }

    private void updateMatkulKelas(){
        try {
            String sql = "UPDATE matkul_kelas SET nilai=1 WHERE no_kelas=? AND no_matkul=?";
            prs = connec.prepareStatement(sql);

            prs.setString(1, id_kelas);
            prs.setString(2, id_mata_kuliah);

            prs.executeUpdate();
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDosenMatkul(){
        try {
            String sql = "UPDATE dosen_matkul SET nilai=1 WHERE no_dosen=? AND no_matkul=?";
            prs = connec.prepareStatement(sql);

            prs.setString(1, id_dosen);
            prs.setString(2, id_mata_kuliah);

            prs.executeUpdate();
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMatkulKelas(){
        try {
            String sql = "UPDATE matkul_kelas SET nilai=0 WHERE no_kelas=? AND no_matkul=?";
            prs = connec.prepareStatement(sql);

            prs.setString(1, id_kelas);
            prs.setString(2, id_mata_kuliah);

            prs.executeUpdate();
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteDosenMatkul(){
        try {
            String sql = "UPDATE dosen_matkul SET nilai=0 WHERE no_dosen=? AND no_matkul=?";
            prs = connec.prepareStatement(sql);

            prs.setString(1, id_dosen);
            prs.setString(2, id_mata_kuliah);

            prs.executeUpdate();
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hapusMatkulKelasAction() {
        deleteMatkulKelas();
        clearText();
        loadDataFromDatabase();
    }

    public void hapusDosenMatkulAction() {
        deleteDosenMatkul();
        clearText();
        loadDataFromDatabase();
    }

    public void hapusDosenMatkulKelasAction() {
        deleteDosenMatkul();
        deleteMatkulKelas();
        clearText();
        loadDataFromDatabase();
    }

    public void generateJadwal() {
        deleteJadwal();
        CSPHelper.main();
        toDijkstra();
    }

    private void deleteJadwal(){
        try {
            String sql = "TRUNCATE TABLE jadwal";
            prs.executeUpdate(sql);
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void toDijkstra() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/generate_dijkstra.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
