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
import model.Kelas;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KelolaKelas implements Initializable {

    @FXML
    private AnchorPane kelolaKelasPane;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField namaField;
    @FXML
    private TextField prodiField;
    @FXML
    private TextField jumlahField;
    @FXML
    private TableView<Kelas> tblDataKelas;
    @FXML
    private TableColumn<Kelas, String> tblKolomNama;
    @FXML
    private TableColumn<Kelas, String> tblKolomProdi;
    @FXML
    private TableColumn<Kelas, String> tblKolomJumlah;

    private ObservableList<Kelas> ol;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs;
    private Statement stmt;
    private SQLHelper sqlHelper = new SQLHelper();
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
    private void tambahKelasAction(ActionEvent event) {
        String nama = namaField.getText();
        String prodi = prodiField.getText();
        String jumlah = jumlahField.getText();

        try {
            stmt = (Statement) connec.createStatement();

            String sql = "INSERT INTO kelas (nama, prodi, jumlah)"
                    + "VALUES('" + nama + "', '" + prodi + "', '" + jumlah + "')";
            int exec = stmt.executeUpdate(sql);
            stmt.close();

            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/kelola_kelas.fxml"));
            kelolaKelasPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateKelasAction(ActionEvent event) {
        String sql = "UPDATE kelas SET nama=?, prodi=?, jumlah=? WHERE id_kelas=?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, namaField.getText());
            prs.setString(2, prodiField.getText());
            prs.setString(3, jumlahField.getText());
            prs.setString(4, id_kelas);
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
            Logger.getLogger(KelolaKelas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void hapusKelasAction(ActionEvent event) {
        String sql = "DELETE FROM kelas WHERE id_kelas = ?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, id_kelas);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil dihapus", ButtonType.OK);
                alert.setTitle("Dihapus");
                alert.showAndWait();
                loadDataFromDatabase();
                clearText();
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaKelas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void toDashboard(ActionEvent event) {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            kelolaKelasPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setCellValue() {
        tblKolomNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        tblKolomProdi.setCellValueFactory(new PropertyValueFactory<>("prodi"));
        tblKolomJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql = "SELECT * FROM kelas";
            rs = connec.createStatement().executeQuery(sql);

            while (rs.next()) {
                ol.add(new Kelas(rs.getString("id_kelas"), rs.getString("nama"), rs.getString("prodi"), rs.getString("jumlah")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaKelas.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataKelas.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataKelas.setOnMouseClicked((MouseEvent event) -> {
            Kelas kelas = tblDataKelas.getItems().get(tblDataKelas.getSelectionModel().getSelectedIndex());
            if (kelas != null){
                id_kelas = kelas.getId_kelas();
                namaField.setText(kelas.getNama());
                prodiField.setText(kelas.getProdi());
                jumlahField.setText(kelas.getJumlah());
            }
        });
    }

    private void clearText(){
        namaField.clear();
        prodiField.clear();
        jumlahField.clear();
    }

}
