package controller;

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
    private TextField namaField;
    @FXML
    private TextField kapasitasField;
    @FXML
    private TableView<Ruangan> tblDataRuangan;
    @FXML
    private TableColumn<Ruangan, String> tblKolomNama;
    @FXML
    private TableColumn<Ruangan, String> tblKolomKapasitas;

    public AnchorPane pane;
    public Button btnTambah;
    public Button btnUpdate;
    public Button btnHapus;
    public Button btnDashboard;

    private ObservableList<Ruangan> ol;
    private Connection connec;
    private PreparedStatement prs;
    private String id_ruangan = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = SQLHelper.getConnection();
        ol = FXCollections.observableArrayList();

        loadDataFromDatabase();
        fromTableToTextField();
        setCellValue();
    }

    @FXML
    private void tambahRuanganAction() {
        String nama = namaField.getText();
        String kapasitas = kapasitasField.getText();

        try {
            Statement stmt = connec.createStatement();

            String sql = "INSERT INTO ruangan (nama, kapasitas)"
                    + "VALUES('" + nama + "', '" + kapasitas + "')";
            stmt.executeUpdate(sql);
            stmt.close();

            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/kelola_ruangan.fxml"));
            kelolaRuanganPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateRuanganAction() {
        try {
            String sql = "UPDATE ruangan SET nama=?, kapasitas=? WHERE no=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, namaField.getText());
            prs.setString(2, kapasitasField.getText());
            prs.setString(3, id_ruangan);
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
    private void hapusRuanganAction() {
        String sql = "DELETE FROM ruangan WHERE no = ?";

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
    private void toDashboard() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            kelolaRuanganPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setCellValue() {
        tblKolomNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        tblKolomKapasitas.setCellValueFactory(new PropertyValueFactory<>("kapasitas"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql_ruangan = "SELECT * FROM ruangan";
            ResultSet rs_ruangan = connec.createStatement().executeQuery(sql_ruangan);

            while (rs_ruangan.next()) {
                if (!rs_ruangan.getString("nama").equalsIgnoreCase("Belum di-set"))
                    ol.add(new Ruangan(rs_ruangan.getString("no"), rs_ruangan.getString("nama"), rs_ruangan.getString("kapasitas")));
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
                kapasitasField.setText(ruangan.getKapasitas());
            }
        });
    }

    private void clearText(){
        namaField.clear();
//        jenisField.clear();
        kapasitasField.clear();
    }
}
