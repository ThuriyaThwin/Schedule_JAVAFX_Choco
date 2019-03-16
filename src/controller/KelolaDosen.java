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
import model.Dosen;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KelolaDosen implements Initializable {

    @FXML
    private AnchorPane kelolaDosenPane;
    @FXML
    private TextField namaField;
    @FXML
    private TextField inisialField;
    @FXML
    private TextField nipField;
    @FXML
    private TableView<Dosen> tblDataDosen;
    @FXML
    private TableColumn<Dosen, String> tblKolomNama;
    @FXML
    private TableColumn<Dosen, String> tblKolomInisial;
    @FXML
    private TableColumn<Dosen, String> tblKolomNIP;

    private ObservableList<Dosen> ol;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs;
    private Statement stmt;
    private SQLHelper sqlHelper = new SQLHelper();
    String nip_temp = null;

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
    private void tambahDosenAction(ActionEvent event) {
        String nama = namaField.getText();
        String nip = nipField.getText();
        String inisial = inisialField.getText();

        try {
            stmt = (Statement) connec.createStatement();

            String sql = "INSERT INTO dosen (nip, inisial, nama)" + "VALUES('" + nip + "', '" +  inisial + "', '" +  nama + "')";
            int exec = stmt.executeUpdate(sql);
            stmt.close();

            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/kelola_dosen.fxml"));
            kelolaDosenPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateDosenAction(ActionEvent event) {
        String sql = "UPDATE dosen SET nip=?, inisial=?, nama=? WHERE nip=?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, nipField.getText());
            prs.setString(2, inisialField.getText());
            prs.setString(3, namaField.getText());
            prs.setString(4, nip_temp);
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
            Logger.getLogger(KelolaDosen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void hapusDosenAction(ActionEvent event) {
        String sql = "DELETE FROM dosen WHERE nip = ?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, nipField.getText());
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil dihapus", ButtonType.OK);
                alert.setTitle("Dihapus");
                alert.showAndWait();
                loadDataFromDatabase();
                clearText();
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaDosen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void toDashboard(ActionEvent event) {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            kelolaDosenPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setCellValue() {
        tblKolomNIP.setCellValueFactory(new PropertyValueFactory<>("nip"));
        tblKolomInisial.setCellValueFactory(new PropertyValueFactory<>("inisial"));
        tblKolomNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql = "SELECT * FROM dosen";
            rs = connec.createStatement().executeQuery(sql);

            while (rs.next()) {
                ol.add(new Dosen(rs.getString("nip"), rs.getString("inisial"), rs.getString("nama")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaDosen.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataDosen.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataDosen.setOnMouseClicked((MouseEvent event) -> {
            Dosen dosen = tblDataDosen.getItems().get(tblDataDosen.getSelectionModel().getSelectedIndex());
            if (dosen != null){
                nipField.setText(dosen.getNip());
                nip_temp = dosen.getNip();
                namaField.setText(dosen.getNama());
                inisialField.setText(dosen.getInisial());
            }
        });
    }

    private void clearText(){
        nipField.clear();
        inisialField.clear();
        namaField.clear();
    }

}
