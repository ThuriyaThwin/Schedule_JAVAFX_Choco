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
import model.Ruangan;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KelolaRuangan implements Initializable {

    @FXML
    private AnchorPane kelolaRuanganPane;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField namaField;
    @FXML
    private ComboBox<String> jenisCombo;
    @FXML
    private TextField kapasitasField;
    @FXML
    private TableView<Ruangan> tblDataRuangan;
    @FXML
    private TableColumn<Ruangan, String> tblKolomNama;
    @FXML
    private TableColumn<Ruangan, String> tblKolomJenis;
    @FXML
    private TableColumn<Ruangan, String> tblKolomKapasitas;

    private ObservableList<Ruangan> ol;
    private ObservableList<String> jenis;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs_ruangan;
    private Statement stmt;
    private SQLHelper sqlHelper = new SQLHelper();
    private String id_ruangan = null;
    private int id_jenis;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = sqlHelper.getConnection();
        ol = FXCollections.observableArrayList();
        jenis = FXCollections.observableArrayList();

        loadDataFromDatabase();
        fromTableToTextField();
        setCellValue();
        fillComboBox();
    }

    @FXML
    private void tambahRuanganAction(ActionEvent event) {
        String nama = namaField.getText();
        String kapasitas = kapasitasField.getText();

        try {
            String sql = "SELECT * FROM jenis_ruangan WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)jenisCombo.getSelectionModel().getSelectedItem());
            rs_ruangan = prs.executeQuery();

            while (rs_ruangan.next()){
                id_jenis = Integer.valueOf(rs_ruangan.getString("id_jenis_ruangan"));
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        try {
            stmt = (Statement) connec.createStatement();

            String sql = "INSERT INTO ruangan (nama, jenis, kapasitas)"
                    + "VALUES('" + nama + "', '" + id_jenis + "', '" + kapasitas + "')";
            int exec = stmt.executeUpdate(sql);
            stmt.close();

            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/kelola_ruangan.fxml"));
            kelolaRuanganPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateRuanganAction(ActionEvent event) {
        try {
            String sql = "SELECT * FROM jenis_ruangan WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)jenisCombo.getSelectionModel().getSelectedItem());
            rs_ruangan = prs.executeQuery();

            while (rs_ruangan.next()){
                id_jenis = Integer.valueOf(rs_ruangan.getString("id_jenis_ruangan"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "UPDATE ruangan SET nama=?, jenis=?, kapasitas=? WHERE id_ruangan=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, namaField.getText());
            prs.setInt(2, id_jenis);
            prs.setString(3, kapasitasField.getText());
            prs.setString(4, id_ruangan);
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
            Logger.getLogger(KelolaRuangan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void hapusRuanganAction(ActionEvent event) {
        String sql = "DELETE FROM ruangan WHERE id_ruangan = ?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, id_ruangan);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil dihapus", ButtonType.OK);
                alert.setTitle("Dihapus");
                alert.showAndWait();
                loadDataFromDatabase();
                clearText();
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaRuangan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void toDashboard(ActionEvent event) {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            kelolaRuanganPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setCellValue() {
        tblKolomNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        tblKolomJenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        tblKolomKapasitas.setCellValueFactory(new PropertyValueFactory<>("kapasitas"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql_ruangan = "SELECT ruangan.id_ruangan, ruangan.nama, ruangan.jenis AS jenis_id, ruangan.kapasitas, jenis_ruangan.nama AS jenis FROM ruangan INNER JOIN jenis_ruangan ON ruangan.jenis = jenis_ruangan.id_jenis_ruangan";
            rs_ruangan = connec.createStatement().executeQuery(sql_ruangan);

            while (rs_ruangan.next()) {
                ol.add(new Ruangan(rs_ruangan.getString("id_ruangan"), rs_ruangan.getString("nama"), rs_ruangan.getString("jenis"), rs_ruangan.getString("kapasitas")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaRuangan.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataRuangan.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataRuangan.setOnMouseClicked((MouseEvent event) -> {
            Ruangan ruangan = tblDataRuangan.getItems().get(tblDataRuangan.getSelectionModel().getSelectedIndex());
            if (ruangan != null){
                id_ruangan = ruangan.getId_ruangan();
                namaField.setText(ruangan.getNama());
                jenisCombo.setValue(ruangan.getJenis());
                kapasitasField.setText(ruangan.getKapasitas());
            }
        });
    }

    private void clearText(){
        namaField.clear();
//        jenisField.clear();
        kapasitasField.clear();
    }

    public void fillComboBox(){
        try {
            String sql = "SELECT * FROM jenis_ruangan";
            prs = connec.prepareStatement(sql);
            rs_ruangan = prs.executeQuery();

            while (rs_ruangan.next()){
                jenis.add(rs_ruangan.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        jenisCombo.setItems(jenis);
    }

    public void onClickCombo(ActionEvent actionEvent) {
        String sql = "SELECT * FROM dosen WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, (String)jenisCombo.getSelectionModel().getSelectedItem());
            rs_ruangan = prs.executeQuery();

            while (rs_ruangan.next()){
                id_jenis = Integer.valueOf(rs_ruangan.getString("id_jenis_ruangan"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
