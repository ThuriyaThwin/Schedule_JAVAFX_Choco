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
    private TextField namaField;
    @FXML
    private TextField kapasitasField;
    @FXML
    private ComboBox<String> kategoriCombo;
    @FXML
    private TableView<Ruangan> tblDataRuangan;
    @FXML
    private TableColumn<Ruangan, String> tblKolomNama;
    @FXML
    private TableColumn<Ruangan, String> tblKolomKapasitas;
    @FXML
    private TableColumn<Ruangan, String> tblKolomKategori;

    public AnchorPane pane;
    public Button btnTambah;
    public Button btnUpdate;
    public Button btnHapus;
    public Button btnDashboard;

    private ObservableList<Ruangan> ol;
    private ObservableList<String> kategori;
    private Connection connec;
    private PreparedStatement prs;
    private String id_ruangan = null;

    private int next_id=0;
    private int id_kategori;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = SQLHelper.getConnection();
        ol = FXCollections.observableArrayList();
        kategori = FXCollections.observableArrayList();

        loadDataFromDatabase();
        fillComboBox();
        fromTableToTextField();
        setCellValue();
    }

    @FXML
    private void tambahRuanganAction() {
        String nama = namaField.getText();
        String kapasitas = kapasitasField.getText();

        try {
            Statement stmt = connec.createStatement();

            String sql = "INSERT INTO ruangan (no, nama, kapasitas, kategori, status)"
                    + "VALUES('" + next_id + "', '" + nama + "', '" + kapasitas + "', '" + id_kategori + "', '" + 1 + "')";
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
            String sql = "UPDATE ruangan SET nama=?, kapasitas=?, kategori=? WHERE no=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, namaField.getText());
            prs.setString(2, kapasitasField.getText());
            prs.setInt(3, id_kategori);
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

    private void fillComboBox(){
        try {
            String sql = "SELECT * FROM kategori";
            prs = connec.prepareStatement(sql);
            ResultSet rs_kategori = prs.executeQuery();

            while (rs_kategori.next()){
                kategori.add(rs_kategori.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        kategoriCombo.setItems(kategori);
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
        tblKolomKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql_ruangan = "SELECT ruangan.no AS ruangan_id,\n" +
                    "ruangan.nama AS ruangan,\n" +
                    "ruangan.kapasitas AS kapasitas,\n" +
                    "kategori.nama AS kategori\n" +
                    "FROM ruangan\n" +
                    "INNER JOIN kategori ON ruangan.kategori = kategori.no\n" +
                    "ORDER BY ruangan_id";
            ResultSet rs_ruangan = connec.createStatement().executeQuery(sql_ruangan);

            int i = 1;
            while (rs_ruangan.next()) {
                if (!rs_ruangan.getString("ruangan").equalsIgnoreCase("Belum di-set")) {
                    ol.add(new Ruangan(rs_ruangan.getString("ruangan_id"), rs_ruangan.getString("ruangan"), rs_ruangan.getString("kapasitas"), rs_ruangan.getString("kategori")));
                }
                i++;
            }

            System.out.println(i);
            next_id = i+1;
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
        kategoriCombo.setValue("Kategori");
        kapasitasField.clear();
    }

    public void onClickKategoriCombo() {
        String sql = "SELECT * FROM kategori WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, kategoriCombo.getSelectionModel().getSelectedItem());
            ResultSet rs = prs.executeQuery();

            while (rs.next()){
                id_kategori = rs.getInt("no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
