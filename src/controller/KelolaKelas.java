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
import javafx.scene.text.Text;
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
    private TextField jumlahField;
    @FXML
    private Text totalData;
    @FXML
    private TableView<Kelas> tblDataKelas;
    @FXML
    private TableColumn<Kelas, String> tblKolomNama;
    @FXML
    private TableColumn<Kelas, String> tblKolomJumlah;

    private ObservableList<Kelas> ol;
    private ObservableList<String> prodi;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs_kelas;
    private Statement stmt;
    private SQLHelper sqlHelper = new SQLHelper();
    private int id_kelas;
    private int next_id = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = sqlHelper.getConnection();
        ol = FXCollections.observableArrayList();
        prodi = FXCollections.observableArrayList();
        loadDataFromDatabase();
        fromTableToTextField();
        setCellValue();
//        fillComboBox();
    }

    @FXML
    private void tambahKelasAction(ActionEvent event) {
        String nama = namaField.getText();
        String jumlah = jumlahField.getText();

        try {
            stmt = (Statement) connec.createStatement();

            String sql = "INSERT INTO kelas (no, nama, jumlah)"
                    + "VALUES('" + next_id + "', '" + nama + "', '" + jumlah + "')";
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
        try {
            String sql = "UPDATE kelas SET nama=?, jumlah=? WHERE no=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, namaField.getText());
            prs.setInt(2, Integer.valueOf(jumlahField.getText()));
            prs.setInt(3, id_kelas);
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
        String sql = "DELETE FROM kelas WHERE no = ?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setInt(1, id_kelas);
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
        tblKolomJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql_kelas = "SELECT * FROM kelas";
            rs_kelas = connec.createStatement().executeQuery(sql_kelas);
            int i=0;

            while (rs_kelas.next()) {
                ol.add(new Kelas(rs_kelas.getInt("no"), rs_kelas.getString("nama"), rs_kelas.getString("jumlah")));
                i++;
            }

            next_id = i+1;
            totalData.setText("Total Data : " + ol.size());
        } catch (SQLException ex) {
            Logger.getLogger(KelolaKelas.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataKelas.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataKelas.setOnMouseClicked((MouseEvent event) -> {
            Kelas kelas = tblDataKelas.getItems().get(tblDataKelas.getSelectionModel().getSelectedIndex());
            if (kelas != null){
                id_kelas = kelas.getNo();
                namaField.setText(kelas.getNama());
                jumlahField.setText(kelas.getJumlah());
            }
        });
    }

    private void clearText(){
        namaField.clear();
        jumlahField.clear();
    }
}
