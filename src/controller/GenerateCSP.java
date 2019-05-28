package controller;

import helper.AutoCompleteBoxHelper;
import helper.CSPHelper;
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
import model.Jadwal;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerateCSP implements Initializable {

    @FXML
    private AnchorPane cspPane;
    @FXML
    private ComboBox<String> dosenCombo;
    @FXML
    private ComboBox<String> matkulCombo;
    @FXML
    private ComboBox<String> kelasCombo;
    @FXML
    private Text totalData;
    @FXML
    private TableView<Jadwal> tblDataJadwal;
    @FXML
    private TableColumn<Jadwal, String> tblKolomDosen;
    @FXML
    private TableColumn<Jadwal, String> tblKolomMatkul;
    @FXML
    private TableColumn<Jadwal, String> tblKolomKelas;

    public Button btnTambah;
    public Button btnDashboard;
    public Button btnHapusDosenMatkulKelas;
    public Button btnGenerateJadwal;
    public Text text;

    private ObservableList<Jadwal> ol;
    private ObservableList<String> dosen;
    private ObservableList<String> mata_kuliah;
    private ObservableList<String> kelas;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs_jadwal;
    private String id_dosen = null;
    private String id_mata_kuliah = null;
    private String id_kelas = null;
    private int ruanganSize=0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = SQLHelper.getConnection();
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
    private void tambahJadwalAction() {
        String dosen_in = dosenCombo.getSelectionModel().getSelectedItem();
        String matkul_in = matkulCombo.getSelectionModel().getSelectedItem();
        String kelas_in = kelasCombo.getSelectionModel().getSelectedItem();

        if (dosenCombo.getSelectionModel().getSelectedItem() == null){
            dosen_in = "";
        }

        if (matkulCombo.getSelectionModel().getSelectedItem() == null){
            matkul_in = "";
        }

        if (kelasCombo.getSelectionModel().getSelectedItem() == null){
            kelas_in = "";
        }

        if (checkInput(dosen_in, matkul_in, kelas_in)) {
            try {
                String sql = "SELECT * FROM dosen WHERE nama=?";
                prs = connec.prepareStatement(sql);
                prs.setString(1, dosen_in);
                rs_jadwal = prs.executeQuery();

                while (rs_jadwal.next()){
                    id_dosen = rs_jadwal.getString("no");
                }
            } catch (SQLException e) {
                System.out.println("Erorr");
            }

            try {
                String sql = "SELECT * FROM matkul WHERE nama=?";
                prs = connec.prepareStatement(sql);
                prs.setString(1, matkul_in);
                rs_jadwal = prs.executeQuery();

                while (rs_jadwal.next()){
                    id_mata_kuliah = rs_jadwal.getString("no");
                }
            } catch (SQLException e) {
                System.out.println("Erorr");
            }

            try {
                String sql = "SELECT * FROM kelas WHERE nama=?";
                prs = connec.prepareStatement(sql);
                prs.setString(1, kelas_in);
                rs_jadwal = prs.executeQuery();

                while (rs_jadwal.next()){
                    id_kelas = rs_jadwal.getString("no");
                }
            } catch (SQLException e) {
                System.out.println("Erorr");
            }

            updateDosenMatkul();
            updateMatkulKelas();
            clearText();
            loadDataFromDatabase();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Semua data harus diisi", ButtonType.OK);
            alert.setTitle("Gagal menambah");
            alert.showAndWait();
        }
    }

    @FXML
    private void toDashboard() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            cspPane.getChildren().setAll(ap);
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

            totalData.setText("Total Data : " + ol.size());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateCSP.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataJadwal.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataJadwal.setOnMouseClicked((MouseEvent event) -> {
            Jadwal jadwal = tblDataJadwal.getItems().get(tblDataJadwal.getSelectionModel().getSelectedIndex());
            if (jadwal != null){
                System.out.println(jadwal.getDosenId());
                id_dosen = jadwal.getDosenId();
                id_mata_kuliah = jadwal.getMataKuliahId();
                id_kelas = jadwal.getKelasId();
                dosenCombo.setValue(jadwal.getDosen());
                matkulCombo.setValue(jadwal.getMataKuliah());
                kelasCombo.setValue(jadwal.getKelas());
            }
        });
    }

    private void clearText(){
        dosenCombo.setValue("");
        matkulCombo.setValue("");
        kelasCombo.setValue("");
    }

    public void onClickKelasCombo() {
        String sql = "SELECT * FROM kelas WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_kelas = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClickMatkulCombo() {
        String sql = "SELECT * FROM matkul WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_mata_kuliah = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClickDosenCombo() {
        String sql = "SELECT * FROM dosen WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, kelasCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_dosen = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillComboBox(){
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
        new AutoCompleteBoxHelper(dosenCombo);
        new AutoCompleteBoxHelper(matkulCombo);
        new AutoCompleteBoxHelper(kelasCombo);
    }

    private void updateMatkulKelas(){
        try {
            String sql = "UPDATE matkul_kelas SET nilai=1 WHERE no_kelas=? AND no_matkul=?";
            prs = connec.prepareStatement(sql);

            prs.setString(1, id_kelas);
            prs.setString(2, id_mata_kuliah);

            prs.executeUpdate();
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDosenMatkul(){
        try {
            String sql = "UPDATE dosen_matkul SET nilai=1 WHERE no_dosen=? AND no_matkul=?";
            prs = connec.prepareStatement(sql);

            prs.setString(1, id_dosen);
            prs.setString(2, id_mata_kuliah);

            prs.executeUpdate();
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMatkulKelas(){
        try {
            String sql = "UPDATE matkul_kelas SET nilai=0 WHERE no_kelas=? AND no_matkul=?";
            prs = connec.prepareStatement(sql);

            prs.setString(1, id_kelas);
            prs.setString(2, id_mata_kuliah);

            prs.executeUpdate();
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteDosenMatkul(){
        try {
            String sql = "UPDATE dosen_matkul SET nilai=0 WHERE no_dosen=? AND no_matkul=?";
            prs = connec.prepareStatement(sql);

            prs.setString(1, id_dosen);
            prs.setString(2, id_mata_kuliah);

            prs.executeUpdate();
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hapusDosenMatkulKelasAction() {
        deleteDosenMatkul();
        deleteMatkulKelas();
        clearText();
        loadDataFromDatabase();
    }

    public void generateJadwal() throws SQLException {
        deleteJadwal();
        resetRuangan();
        resetHari();
        resetSesi();
        CSPHelper.main();
        setPreference();
        toDijkstra();
    }

    private void deleteJadwal(){
        try {
            String sql = "TRUNCATE TABLE jadwal";
            prs.executeUpdate(sql);
            prs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void toDijkstra() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/generate_dijkstra.fxml"));
            cspPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void resetRuangan(){
        String sql_ruangan = "UPDATE ruangan SET status='1' WHERE no=?";

        try {
            for (int i=1; i<=ruanganSize; i++){
                if (i != getDefaultRuangan()){
                    prs = connec.prepareStatement(sql_ruangan);
                    prs.setInt(1, i);
                    prs.executeUpdate();
                    prs.close();

                    System.out.println("ruangan " + i);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }
    }

    private void resetHari(){
        String sql_hari = "UPDATE hari SET status='1' WHERE no=?";

        try {
            for (int i=1; i<=5; i++){
                prs = connec.prepareStatement(sql_hari);
                prs.setInt(1, i);
                prs.executeUpdate();
                prs.close();

                System.out.println("hari " + i);
            }
        } catch (SQLException ex) {
            System.out.println("Erorr");
        }
    }

    private void resetSesi(){
        String sql_hari = "UPDATE sesi SET status='1' WHERE no=?";

        try {
            for (int i=1; i<=8; i++){
                prs = connec.prepareStatement(sql_hari);
                prs.setInt(1, i);
                prs.executeUpdate();
                prs.close();

                System.out.println("sesi " + i);
            }
        } catch (SQLException ex) {
            System.out.println("Erorr");
        }
    }

    private int getDefaultRuangan() throws SQLException {
        int id_ruangan = 0;
        String sql = "SELECT * FROM ruangan";
        prs = connec.prepareStatement(sql);
        rs_jadwal = prs.executeQuery();

        int i=0;
        while (rs_jadwal.next()){
            if (rs_jadwal.getString(2).equalsIgnoreCase("Belum di-set"))
                id_ruangan = rs_jadwal.getInt("no");

            i++;
        }

        ruanganSize = i;

        return id_ruangan;
    }

    private void setPreference(){
        try{
            String sql_jadwal = "SELECT jadwal.id_jadwal AS id, " +
                    "jadwal.no_dosen AS dosen_id, " +
                    "jadwal.no_matkul AS matkul_id, " +
                    "jadwal.no_hari AS hari_id, " +
                    "jadwal.no_sesi AS sesi_id, " +
                    "jadwal.no_kelas AS kelas_id, " +
                    "jadwal.no_ruangan AS ruangan_id, " +
                    "dosen.nama AS dosen, " +
                    "matkul.nama AS matkul, " +
                    "kelas.nama AS kelas, " +
                    "hari.nama AS hari, " +
                    "sesi.nama AS sesi, " +
                    "ruangan.nama AS ruangan, " +
                    "kategori.nama AS kategori " +
                    "FROM jadwal " +
                    "INNER JOIN matkul ON jadwal.no_matkul = matkul.no " +
                    "INNER JOIN dosen ON jadwal.no_dosen = dosen.no " +
                    "INNER JOIN kelas ON jadwal.no_kelas = kelas.no " +
                    "INNER JOIN hari ON jadwal.no_hari = hari.no " +
                    "INNER JOIN sesi ON jadwal.no_sesi = sesi.no " +
                    "INNER JOIN ruangan ON jadwal.no_ruangan = ruangan.no " +
                    "INNER JOIN kategori ON jadwal.kategori = kategori.no " +
                    "ORDER BY jadwal.no_hari DESC, jadwal.no_sesi DESC, jadwal.no_kelas DESC";

            rs_jadwal = connec.createStatement().executeQuery(sql_jadwal);

            while (rs_jadwal.next()){
                String matkul = rs_jadwal.getString("matkul");
                int id = rs_jadwal.getInt("id");

                if (matkul.equalsIgnoreCase("PRAK KIMDAS")){
                    String sql = "SELECT * FROM ruangan WHERE nama='GD812'";

                    prs = connec.prepareStatement(sql);
                    ResultSet rs = prs.executeQuery();

                    while (rs.next()) {
                        String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                        prs = connec.prepareStatement(sql_ruangan);
                        prs.setInt(1, rs.getInt("no"));
                        prs.setInt(2, id);
                        prs.executeUpdate();
                        prs.close();
                    }
                } else if (matkul.equalsIgnoreCase("PRAK FISDAS 1")){
                    String sql = "SELECT * FROM ruangan WHERE nama='LAB FISIKA'";

                    prs = connec.prepareStatement(sql);
                    ResultSet rs = prs.executeQuery();

                    while (rs.next()) {
                        String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                        prs = connec.prepareStatement(sql_ruangan);
                        prs.setInt(1, rs.getInt("no"));
                        prs.setInt(2, id);
                        prs.executeUpdate();
                        prs.close();
                    }
                } else if (matkul.equalsIgnoreCase("PRAK MIKRUM")){
                    String sql = "SELECT * FROM ruangan WHERE nama='GD825'";

                    prs = connec.prepareStatement(sql);
                    ResultSet rs = prs.executeQuery();

                    while (rs.next()) {
                        String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                        prs = connec.prepareStatement(sql_ruangan);
                        prs.setInt(1, rs.getInt("no"));
                        prs.setInt(2, id);
                        prs.executeUpdate();
                        prs.close();
                    }
                } else if (matkul.equalsIgnoreCase("PRAK KIMOR")){
                    String sql = "SELECT * FROM ruangan WHERE nama='GD826'";

                    prs = connec.prepareStatement(sql);
                    ResultSet rs = prs.executeQuery();

                    while (rs.next()) {
                        String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                        prs = connec.prepareStatement(sql_ruangan);
                        prs.setInt(1, rs.getInt("no"));
                        prs.setInt(2, id);
                        prs.executeUpdate();
                        prs.close();
                    }
                } else if (matkul.equalsIgnoreCase("PRAK PIP")){
                    String sql = "SELECT * FROM ruangan WHERE nama='GD726'";

                    prs = connec.prepareStatement(sql);
                    ResultSet rs = prs.executeQuery();

                    while (rs.next()) {
                        String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                        prs = connec.prepareStatement(sql_ruangan);
                        prs.setInt(1, rs.getInt("no"));
                        prs.setInt(2, id);
                        prs.executeUpdate();
                        prs.close();
                    }
                } else if (matkul.equalsIgnoreCase("MATLAB")){
                    String sql = "SELECT * FROM ruangan WHERE nama='GD726'";

                    prs = connec.prepareStatement(sql);
                    ResultSet rs = prs.executeQuery();

                    while (rs.next()) {
                        String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                        prs = connec.prepareStatement(sql_ruangan);
                        prs.setInt(1, rs.getInt("no"));
                        prs.setInt(2, id);
                        prs.executeUpdate();
                        prs.close();
                    }
                } else if (matkul.equalsIgnoreCase("PRAK KIMFIS")){
                    String sql = "SELECT * FROM ruangan WHERE nama='GD814'";

                    prs = connec.prepareStatement(sql);
                    ResultSet rs = prs.executeQuery();

                    while (rs.next()) {
                        String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                        prs = connec.prepareStatement(sql_ruangan);
                        prs.setInt(1, rs.getInt("no"));
                        prs.setInt(2, id);
                        prs.executeUpdate();
                        prs.close();
                    }
                }
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
    }

    private boolean checkInput(String dosen, String matkul, String kelas){
        boolean result = true;

        System.out.println(dosen + " " + matkul + " " + kelas);

        if (dosen.equalsIgnoreCase("") || matkul.equalsIgnoreCase("")|| kelas.equalsIgnoreCase("")){
            result = false;
        }

        return result;
    }
}
