package controller;

import helper.AutoCompleteBoxHelper;
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
    private ComboBox<String> dosenCombo;
    @FXML
    private ComboBox<String> matkulCombo;
    @FXML
    private ComboBox<String> kelasCombo;
    @FXML
    private TableView<Jadwal> tblDataJadwal;
    @FXML
    private TableColumn<Jadwal, String> tblKolomDosen;
    @FXML
    private TableColumn<Jadwal, String> tblKolomMatkul;
    @FXML
    private TableColumn<Jadwal, String> tblKolomKelas;

    private ObservableList<Jadwal> ol;
    private ObservableList<String> dosen;
    private ObservableList<String> mata_kuliah;
    private ObservableList<String> kelas;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs_jadwal;
    private Statement stmt;
    private SQLHelper sqlHelper = new SQLHelper();
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
        dosen = FXCollections.observableArrayList();
        mata_kuliah = FXCollections.observableArrayList();
        kelas = FXCollections.observableArrayList();
        loadDataFromDatabase();
        fromTableToTextField();
        setCellValue();
        fillComboBox();
    }

    @FXML
    private void tambahJadwalAction(ActionEvent event) {

        try {
            String sql = "SELECT * FROM dosen WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)dosenCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_dosen = rs_jadwal.getString("nip");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        try {
            String sql = "SELECT * FROM mata_kuliah WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)dosenCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_mata_kuliah = rs_jadwal.getString("id_mata_kuliah");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        try {
            String sql = "SELECT * FROM kelas WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)dosenCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_kelas = rs_jadwal.getString("id_kelas");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        try {
            stmt = (Statement) connec.createStatement();

            String sql = "INSERT INTO jadwal (dosen, mata_kuliah, kelas)"
                    + "VALUES('" + id_dosen + "', '" + id_mata_kuliah + "', '" + id_kelas + "')";
            stmt.executeUpdate(sql);
            stmt.close();

            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/generate_jadwal.fxml"));
            kelolaJadwalPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateJadwalAction(ActionEvent event) {
        try {
            String sql = "SELECT * FROM dosen WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)dosenCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_dosen = rs_jadwal.getString("nip");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        try {
            String sql = "SELECT * FROM mata_kuliah WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)dosenCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_mata_kuliah = rs_jadwal.getString("id_mata_kuliah");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        try {
            String sql = "SELECT * FROM kelas WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)dosenCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_kelas = rs_jadwal.getString("id_kelas");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        try {
            String sql = "UPDATE jadwal SET dosen=?, mata_kuliah=?, kelas=? WHERE id_jadwal=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, id_dosen);
            prs.setString(2, id_mata_kuliah);
            prs.setString(3, id_kelas);
//            prs.setString(4, id_jadwal);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Update berhasil", ButtonType.OK);
                alert.setTitle("Update");
                alert.showAndWait();
                loadDataFromDatabase();
//                clearText();
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
//            prs.setString(1, id_jadwal);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil dihapus", ButtonType.OK);
                alert.setTitle("Dihapus");
                alert.showAndWait();
                loadDataFromDatabase();
//                clearText();
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaKelas.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException ex) {
            Logger.getLogger(GenerateJadwal.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataJadwal.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataJadwal.setOnMouseClicked((MouseEvent event) -> {
            Jadwal jadwal = tblDataJadwal.getItems().get(tblDataJadwal.getSelectionModel().getSelectedIndex());
            if (jadwal != null){
                id_dosen = jadwal.getDosenId();
                id_mata_kuliah = jadwal.getMataKuliahId();
                id_kelas = jadwal.getKelasId();
                dosenCombo.setValue(jadwal.getDosen());
                matkulCombo.setValue(jadwal.getMataKuliah());
                kelasCombo.setValue(jadwal.getKelas());
            }
        });
    }

//    private void clearText(){
//        dosenField.clear();
//        matkulField.clear();
//        kelasField.clear();
//    }

    public void onClickKelasCombo(ActionEvent actionEvent) {
        String sql = "SELECT * FROM kelas WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_kelas = rs_jadwal.getString("no_kelas");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClickMatkulCombo(ActionEvent actionEvent) {
        String sql = "SELECT * FROM matkul WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_mata_kuliah = rs_jadwal.getString("no_matkul");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClickDosenCombo(ActionEvent actionEvent) {
        String sql = "SELECT * FROM dosen WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_dosen = rs_jadwal.getString("no_dosen");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fillComboBox(){
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
        new AutoCompleteBoxHelper<>(dosenCombo);
        new AutoCompleteBoxHelper<>(matkulCombo);
        new AutoCompleteBoxHelper<>(kelasCombo);
    }
}
