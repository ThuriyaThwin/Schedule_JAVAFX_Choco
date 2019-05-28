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
import model.Dosen;
import model.MataKuliah;

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
    private Text totalData;
    @FXML
    private TableView<Dosen> tblDataDosen;
    @FXML
    private TableColumn<Dosen, String> tblKolomNama;

    public Button btnTambah;
    public Button btnUpdate;
    public Button btnHapus;
    public Button btnDashboard;

    private ObservableList<Dosen> ol;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs;
    private Statement stmt;
    private ObservableList<MataKuliah> ol_matkul;
    private int next_id=0;
    private int matkul_size=0;
    private int id_dosen;

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
    private void tambahDosenAction() {
        String nama = namaField.getText();

        getMatkulSize();

        if (checkInput(nama)){
            try {
                stmt = connec.createStatement();

                String sql = "INSERT INTO dosen (no, nama)" + "VALUES('" + next_id + "', '" + nama + "')";
                stmt.executeUpdate(sql);
                stmt.close();

                insertDosenMatkul();

                AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/kelola_dosen.fxml"));
                kelolaDosenPane.getChildren().setAll(pane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dosen sudah ada / Data kosong", ButtonType.OK);
            alert.setTitle("Gagal menambah");
            alert.showAndWait();
        }
    }

    @FXML
    private void updateDosenAction() {
        String sql = "UPDATE dosen SET nama=? WHERE no=?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, namaField.getText());
            prs.setInt(2, id_dosen);
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
    private void hapusDosenAction() {
        getMatkulSize();
        deleteDosenMatkul();

        try {
            String sql = "DELETE FROM dosen WHERE no = ?";
            prs = connec.prepareStatement(sql);
            prs.setInt(1, id_dosen);
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
    private void toDashboard() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            kelolaDosenPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setCellValue() {
        tblKolomNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
    }

    private void loadDataFromDatabase() {
        ol.clear();
        try {
            String sql = "SELECT * FROM dosen";
            rs = connec.createStatement().executeQuery(sql);
            int i = 0;

            while (rs.next()) {
                ol.add(new Dosen(rs.getInt("no"), rs.getString("nama")));
                i++;
            }

            next_id = i+1;
            totalData.setText("Total Data : " + ol.size());
        } catch (SQLException ex) {
            Logger.getLogger(KelolaDosen.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataDosen.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataDosen.setOnMouseClicked((MouseEvent event) -> {
            Dosen dosen = tblDataDosen.getItems().get(tblDataDosen.getSelectionModel().getSelectedIndex());
            if (dosen != null){
                id_dosen = dosen.getNo();
                namaField.setText(dosen.getNama());
            }
        });
    }

    private void clearText(){
        namaField.clear();
    }

    private void insertDosenMatkul(){
        try {
            stmt = connec.createStatement();

            for (int i=1;i<matkul_size+1;i++){
                String sql = "INSERT INTO dosen_matkul (no_matkul, no_dosen, nilai)"
                        + "VALUES('" + i + "', '" + next_id + "', '" + 0 + "')";

                stmt.executeUpdate(sql);
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteDosenMatkul(){
        try {
            for (int i=1;i<matkul_size+1;i++){
                String sql = "DELETE FROM dosen_matkul WHERE no_matkul=? AND no_dosen=?";

                prs = connec.prepareStatement(sql);
                prs.setInt(1, i);
                prs.setInt(2, id_dosen);

                prs.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getMatkulSize(){
        try {
            String sql = "SELECT * FROM matkul";
            rs = connec.createStatement().executeQuery(sql);

            while (rs.next()) {
                ol_matkul.add(new MataKuliah(rs.getString("no"), rs.getString("nama"), rs.getString("sks"), rs.getString("jumlah"), rs.getString("kategori")));
            }

            matkul_size = ol_matkul.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkInput(String nama){
        String sql = "SELECT * FROM dosen WHERE nama='" + nama + "'";
        boolean result = true;

        try{
            prs = connec.prepareStatement(sql);
            ResultSet rs = prs.executeQuery();

            if (rs.next() || nama.equalsIgnoreCase("")){
                result = false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return result;
    }
}
