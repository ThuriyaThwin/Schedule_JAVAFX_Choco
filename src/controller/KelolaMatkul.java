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
import javafx.scene.text.Text;
import model.MataKuliah;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KelolaMatkul implements Initializable {

    @FXML
    private AnchorPane kelolaMataKuliahPane;
    @FXML
    private ComboBox<String> kategoriCombo;
    @FXML
    private TextField namaField;
    @FXML
    private TextField jumlahField;
    @FXML
    private TextField sksField;
    @FXML
    private Text totalData;
    @FXML
    private TableView<MataKuliah> tblDataMataKuliah;
    @FXML
    private TableColumn<MataKuliah, String> tblKolomNama;
    @FXML
    private TableColumn<MataKuliah, String> tblKolomJumlah;
    @FXML
    private TableColumn<MataKuliah, String> tblKolomSKS;
    @FXML
    private TableColumn<MataKuliah, String> tblKolomKategori;

    public AnchorPane pane;
    public Button btnTambah;
    public Button btnUpdate;
    public Button btnHapus;
    public Button btnDashboard;

    private ObservableList<MataKuliah> ol;
    private ObservableList<String> kategori;
    private Connection connec;
    private PreparedStatement prs;
    private String id_mata_kuliah = null;
    private int next_id = 0;
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
        fromTableToTextField();
        fillComboBox();
        setCellValue();
    }

    @FXML
    private void tambahMataKuliahAction() {
        String nama = namaField.getText();
        String jumlah = jumlahField.getText();
        String sks = sksField.getText();

        try {
            Statement stmt = connec.createStatement();

            String sql = "INSERT INTO matkul (no, nama, sks, jumlah, kategori)"
                    + "VALUES('" + next_id + "', '" + nama + "', '" + sks + "', '" + jumlah + "', '" + id_kategori + "')";
            stmt.executeUpdate(sql);
            stmt.close();

            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/kelola_matkul.fxml"));
            kelolaMataKuliahPane.getChildren().setAll(pane);
            clearText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateMataKuliahAction() {
        String sql = "UPDATE matkul SET nama=?, sks=?, jumlah=?, kategori=? WHERE no=?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, namaField.getText());
            prs.setString(2, sksField.getText());
            prs.setString(3, jumlahField.getText());
            prs.setInt(4, id_kategori);
            prs.setString(5, id_mata_kuliah);
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
            Logger.getLogger(KelolaMatkul.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void hapusMataKuliahAction() {
        String sql = "DELETE FROM matkul WHERE no = ?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, id_mata_kuliah);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil dihapus", ButtonType.OK);
                alert.setTitle("Dihapus");
                alert.showAndWait();
                loadDataFromDatabase();
                clearText();
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaMatkul.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void toDashboard() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            kelolaMataKuliahPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void fillComboBox(){
        try {
            String sql = "SELECT * FROM kategori";
            prs = connec.prepareStatement(sql);
            ResultSet rs_kategori = prs.executeQuery();

            while (rs_kategori.next()){
                if (rs_kategori.getInt("no") == 1 || rs_kategori.getInt("no") == 2)
                    kategori.add(rs_kategori.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        kategoriCombo.setItems(kategori);
    }

    private void setCellValue() {
        tblKolomNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        tblKolomJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        tblKolomSKS.setCellValueFactory(new PropertyValueFactory<>("sks"));
        tblKolomKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql = "SELECT matkul.no AS matkul_id,\n" +
                    "matkul.nama AS matkul,\n" +
                    "matkul.sks AS sks,\n" +
                    "matkul.jumlah AS jumlah,\n" +
                    "kategori.nama AS kategori\n" +
                    "FROM matkul\n" +
                    "INNER JOIN kategori ON matkul.kategori = kategori.no\n" +
                    "ORDER BY matkul_id";
            ResultSet rs = connec.createStatement().executeQuery(sql);
            int i =0;

            while (rs.next()) {
                ol.add(new MataKuliah(rs.getString("matkul_id"), rs.getString("matkul"), rs.getString("sks"), rs.getString("jumlah"), rs.getString("kategori")));
                i++;
            }

            next_id = i + 1;
            totalData.setText("Total Data : " + ol.size());
        } catch (SQLException ex) {
            Logger.getLogger(KelolaMatkul.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataMataKuliah.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataMataKuliah.setOnMouseClicked((MouseEvent event) -> {
            MataKuliah mataKuliah = tblDataMataKuliah.getItems().get(tblDataMataKuliah.getSelectionModel().getSelectedIndex());
            if (mataKuliah != null){
                id_mata_kuliah = mataKuliah.getNo();
                namaField.setText(mataKuliah.getNama());
                jumlahField.setText(mataKuliah.getJumlah());
                sksField.setText(mataKuliah.getSks());
            }
        });
    }

    private void clearText(){
        namaField.clear();
        jumlahField.clear();
        kategoriCombo.setValue("");
        sksField.clear();
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
