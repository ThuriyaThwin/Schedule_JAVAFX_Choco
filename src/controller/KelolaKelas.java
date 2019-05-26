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
import javafx.scene.text.Text;
import model.Kelas;
import model.MataKuliah;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KelolaKelas implements Initializable {

    @FXML
    private AnchorPane kelolaKelasPane;
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

    public Button btnTambah;
    public Button btnUpdate;
    public Button btnHapus;
    public Button btnDashboard;

    private ObservableList<Kelas> ol;
    private ObservableList<MataKuliah> ol_matkul;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs_kelas;
    private Statement stmt;
    private int id_kelas;
    private int next_id=0;
    private int matkul_size=0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = SQLHelper.getConnection();
        ol = FXCollections.observableArrayList();
        ol_matkul = FXCollections.observableArrayList();
        loadDataFromDatabase();
        fromTableToTextField();
        setCellValue();
    }

    @FXML
    private void tambahKelasAction() {
        String nama = namaField.getText();
        String jumlah = jumlahField.getText();

        getMatkulSize();

        if (checkInput(nama, jumlah)){
            try {
                stmt = connec.createStatement();

                String sql = "INSERT INTO kelas (no, nama, jumlah)"
                        + "VALUES('" + next_id + "', '" + nama + "', '" + jumlah + "')";
                stmt.executeUpdate(sql);
                stmt.close();

                insertMatkulKelas();

                AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/kelola_kelas.fxml"));
                kelolaKelasPane.getChildren().setAll(pane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kelas sudah ada / Data kosong", ButtonType.OK);
            alert.setTitle("Gagal menambah");
            alert.showAndWait();
        }
    }

    @FXML
    private void updateKelasAction() {
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
    private void hapusKelasAction() {
        getMatkulSize();
        deleteMatkulKelas();

        try {
            String sql = "DELETE FROM kelas WHERE no = ?";
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
    private void toDashboard() {
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
                System.out.println(kelas.getNo());
                System.out.println(kelas.getNama());
                System.out.println(kelas.getJumlah());
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

    private void insertMatkulKelas(){
        try {
            stmt = connec.createStatement();

            for (int i=1;i<matkul_size+1;i++){
                String sql = "INSERT INTO matkul_kelas (no_matkul, no_kelas, nilai)"
                        + "VALUES('" + i + "', '" + next_id + "', '" + 0 + "')";

                stmt.executeUpdate(sql);
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMatkulKelas(){
        try {
            for (int i=1;i<matkul_size+1;i++){
                String sql = "DELETE FROM matkul_kelas WHERE no_matkul=? AND no_kelas=?";

                prs = connec.prepareStatement(sql);
                prs.setInt(1, i);
                prs.setInt(2, id_kelas);

                prs.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getMatkulSize(){
        try {
            String sql = "SELECT * FROM matkul";
            rs_kelas = connec.createStatement().executeQuery(sql);

            while (rs_kelas.next()) {
                ol_matkul.add(new MataKuliah(rs_kelas.getString("no"), rs_kelas.getString("nama"), rs_kelas.getString("sks"), rs_kelas.getString("jumlah"), rs_kelas.getString("kategori")));
            }

            matkul_size = ol_matkul.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkInput(String nama, String jumlah){
        String sql = "SELECT * FROM kelas WHERE nama='" + nama + "'";
        boolean result = true;

        try{
            prs = connec.prepareStatement(sql);
            ResultSet rs = prs.executeQuery();

            if (rs.next() || nama.equalsIgnoreCase("") || jumlah.equalsIgnoreCase("")){
                result = false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return result;
    }
}
