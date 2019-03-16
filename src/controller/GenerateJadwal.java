package controller;

import helper.SQLHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Jadwal;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerateJadwal implements Initializable {

    @FXML
    private AnchorPane kelolaJadwalPane;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField dosenField;
    @FXML
    private TextField matkulField;
    @FXML
    private TextField kelasField;
    @FXML
    private TableView<Jadwal> tblDataJadwal;
    @FXML
    private TableColumn<Jadwal, String> tblKolomDosen;
    @FXML
    private TableColumn<Jadwal, String> tblKolomMatkul;
    @FXML
    private TableColumn<Jadwal, String> tblKolomKelas;

    private ObservableList<Jadwal> ol;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs_jadwal;
    private Statement stmt;
    private SQLHelper sqlHelper = new SQLHelper();
    private String id_jadwal = null;
    private String id_dosen = null;
    private String id_mata_kuliah = null;
    private String id_kelas = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = sqlHelper.getConnection();
        ol = FXCollections.observableArrayList();
        loadDataFromDatabase();
        fromTableToTextField();
        setCellValue();
    }

    @FXML
    private void tambahJadwalAction(ActionEvent event) {
        String dosen = dosenField.getText();
        String matkul = matkulField.getText();
        String kelas = kelasField.getText();

        try {
            stmt = (Statement) connec.createStatement();

            String sql = "INSERT INTO jadwal (dosen, mata_kuliah, kelas)"
                    + "VALUES('" + dosen + "', '" + matkul + "', '" + kelas + "')";
            int exec = stmt.executeUpdate(sql);
            stmt.close();

            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/generate_jadwal.fxml"));
            kelolaJadwalPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateJadwalAction(ActionEvent event) {
//        String sql = "UPDATE jadwal SET dosen=?, mata_kuliah=?, kelas=? WHERE id_jadwal=?";
        String sql = "UPDATE jadwal SET dosen=?, mata_kuliah=?, kelas=? WHERE id_jadwal=?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, id_dosen);
            prs.setString(2, id_mata_kuliah);
            prs.setString(3, id_kelas);
            prs.setString(4, id_jadwal);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Update berhasil", ButtonType.OK);
                alert.setTitle("Update");
                alert.showAndWait();
                loadDataFromDatabase();
                clearText();
            }

            prs.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateJadwal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void hapusJadwalAction(ActionEvent event) {
        String sql = "DELETE FROM jadwal WHERE id_jadwal = ?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, id_jadwal);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil dihapus", ButtonType.OK);
                alert.setTitle("Dihapus");
                alert.showAndWait();
                loadDataFromDatabase();
                clearText();
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateJadwal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void toDashboard(ActionEvent event) {
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
            String sql_jadwal = "SELECT jadwal.id_jadwal, jadwal.dosen AS dosen_id, jadwal.kelas AS kelas_id, jadwal.mata_kuliah AS mata_kuliah_id, dosen.nama AS dosen, kelas.nama AS kelas, mata_kuliah.nama AS mata_kuliah FROM jadwal INNER JOIN dosen ON jadwal.dosen = dosen.nip INNER JOIN kelas ON jadwal.kelas = kelas.id_kelas INNER JOIN mata_kuliah ON jadwal.mata_kuliah = mata_kuliah.id_mata_kuliah";
//            String sql = "SELECT * dosen INNER JOIN kelas ON ";
            rs_jadwal = connec.createStatement().executeQuery(sql_jadwal);

            while (rs_jadwal.next()) {
                ol.add(new Jadwal(rs_jadwal.getString("id_jadwal"), rs_jadwal.getString("dosen"), rs_jadwal.getString("dosen_id"), rs_jadwal.getString("mata_kuliah"), rs_jadwal.getString("mata_kuliah_id"), rs_jadwal.getString("kelas"), rs_jadwal.getString("kelas_id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateJadwal.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataJadwal.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataJadwal.setOnMouseClicked((MouseEvent event) -> {
            Jadwal jadwal = tblDataJadwal.getItems().get(tblDataJadwal.getSelectionModel().getSelectedIndex());
            if (jadwal != null){
                id_jadwal = jadwal.getId_jadwal();
                id_dosen = jadwal.getDosenId();
                id_mata_kuliah = jadwal.getMataKuliahId();
                id_kelas = jadwal.getKelasId();
                dosenField.setText(jadwal.getDosen());
                matkulField.setText(jadwal.getMataKuliah());
                kelasField.setText(jadwal.getKelas());
            }
        });
    }

    private void clearText(){
        dosenField.clear();
        matkulField.clear();
        kelasField.clear();
    }

}
