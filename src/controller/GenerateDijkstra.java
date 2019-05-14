package controller;

import helper.AutoCompleteBoxHelper;
import helper.CSPHelper;
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
import model.Jadwal;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerateDijkstra implements Initializable {

    @FXML
    private AnchorPane dijkstraPane;
    @FXML
    private TextField kelasField;
    @FXML
    private ComboBox<String> ruanganCombo;
    @FXML
    private ComboBox<String> hariCombo;
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
    @FXML
    private TableColumn<Jadwal, String> tblKolomSesi;
    @FXML
    private TableColumn<Jadwal, String> tblKolomRuangan;

    public AnchorPane pane;
    public Button btnSetRuangan;
    public Button btnDashboard;
    public Button btnGenerateDijkstra;

    private ObservableList<Jadwal> ol;
    private ObservableList<String> ruangan;
    private ObservableList<String> hari;
    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs_jadwal;
    private String sql_jadwal;
    private String id_ruangan = null;
    private String id_jadwal = null;
    private int hari_dipilih = 5;

    private int kelasSize;
    private int sesiSize;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = SQLHelper.getConnection();
        ol = FXCollections.observableArrayList();
        ruangan = FXCollections.observableArrayList();
        hari = FXCollections.observableArrayList();

        fillComboBox();
        fillSesi();
        fillKelas();
        loadDataFromDatabase(hari_dipilih);
        fromTableToTextField();
        setCellValue();
    }

    @FXML
    private void setRuanganAction() {
        try {
            String sql = "SELECT * FROM ruangan WHERE no=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, ruanganCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_ruangan = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        String sql = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, id_ruangan);
            prs.setString(2, id_jadwal);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil di-set", ButtonType.OK);
                alert.setTitle("Set Ruangan");
                alert.showAndWait();
                loadDataFromDatabase(hari_dipilih);
                clearText();
            }

            prs.close();
        } catch (SQLException ex) {
            Logger.getLogger(KelolaDosen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void toDashboard() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            dijkstraPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setCellValue() {
        tblKolomDosen.setCellValueFactory(new PropertyValueFactory<>("dosen"));
        tblKolomMatkul.setCellValueFactory(new PropertyValueFactory<>("mataKuliah"));
        tblKolomKelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        tblKolomSesi.setCellValueFactory(new PropertyValueFactory<>("sesi"));
        tblKolomRuangan.setCellValueFactory(new PropertyValueFactory<>("ruangan"));
    }

    private void loadDataFromDatabase(int hari) {
        ol.clear();
        try {
            for (int k=kelasSize;k>0;k--){
                boolean filled = false;

                for (int s=sesiSize;s>0;s--){
                    if (!filled){
                        if (check(hari, k, s)){
                            rs_jadwal = connec.createStatement().executeQuery(sql_jadwal);

                            while (rs_jadwal.next()){
                                Jadwal jadwal = new Jadwal();

                                jadwal.setId(rs_jadwal.getString("id"));
                                jadwal.setDosen(rs_jadwal.getString("dosen"));
                                jadwal.setDosenId(rs_jadwal.getString("dosen_id"));
                                jadwal.setMataKuliah(rs_jadwal.getString("matkul"));
                                jadwal.setMataKuliahId(rs_jadwal.getString("matkul_id"));
                                jadwal.setKelas(rs_jadwal.getString("kelas"));
                                jadwal.setKelasId(rs_jadwal.getString("kelas_id"));
                                jadwal.setHari(rs_jadwal.getString("hari"));
                                jadwal.setHariId(rs_jadwal.getString("hari_id"));
                                jadwal.setSesi(rs_jadwal.getString("sesi"));
                                jadwal.setSesiId(rs_jadwal.getString("sesi_id"));
                                jadwal.setRuangan(rs_jadwal.getString("ruangan"));
                                jadwal.setRuanganId(rs_jadwal.getString("ruangan_id"));

                                ol.add(jadwal);
                                filled = true;
                            }
                        }
                    }
                }
            }

            totalData.setText("Total Data : " + ol.size());
        } catch (SQLException ex) {
            Logger.getLogger(GenerateDijkstra.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataJadwal.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataJadwal.setOnMouseClicked((MouseEvent event) -> {
            Jadwal jadwal = tblDataJadwal.getItems().get(tblDataJadwal.getSelectionModel().getSelectedIndex());
            if (jadwal != null){
                System.out.println(jadwal.getDosenId());
                id_jadwal = jadwal.getId();
                kelasField.setText(jadwal.getKelas());
            }
        });
    }

    private void clearText(){
        kelasField.setText("");
        ruanganCombo.setValue("");
    }

    public void onClickRuanganCombo() {
        String sql = "SELECT * FROM ruangan WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, ruanganCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_ruangan = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillComboBox(){
        ruanganCombo.setEditable(true);

        try {
            String sql = "SELECT * FROM ruangan";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                ruangan.add(rs_jadwal.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM hari ORDER BY no DESC";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                hari.add(rs_jadwal.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ruanganCombo.setItems(ruangan);
        hariCombo.setItems(hari);
        ruanganCombo.getSelectionModel().select(5);
        new AutoCompleteBoxHelper(ruanganCombo);
    }

    public void generateDijkstra() {
//        CSPHelper.main();
        toDijkstra();
    }

    private void toDijkstra() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/output_dijkstra.fxml"));
            pane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onClickHariCombo() {
        String sql = "SELECT * FROM hari WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, hariCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                hari_dipilih = Integer.parseInt(rs_jadwal.getString("no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadDataFromDatabase(hari_dipilih);
    }

    private void fillKelas() {
        try {
            String sql = "SELECT * FROM kelas ORDER BY no DESC";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                kelasSize++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillSesi() {
        try {
            String sql = "SELECT * FROM sesi ORDER BY no DESC";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                sesiSize++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean check(int hari, int kelas, int sesi) throws SQLException {
        sql_jadwal = "SELECT jadwal.id_jadwal AS id, " +
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
                "ruangan.nama AS ruangan " +
                "FROM jadwal " +
                "INNER JOIN matkul ON jadwal.no_matkul = matkul.no " +
                "INNER JOIN dosen ON jadwal.no_dosen = dosen.no " +
                "INNER JOIN kelas ON jadwal.no_kelas = kelas.no " +
                "INNER JOIN hari ON jadwal.no_hari = hari.no " +
                "INNER JOIN sesi ON jadwal.no_sesi = sesi.no " +
                "INNER JOIN ruangan ON jadwal.no_ruangan = ruangan.no " +
                "WHERE jadwal.no_hari='" + hari + "' AND jadwal.no_kelas='" + kelas + "' AND jadwal.no_sesi='" + sesi + "'" +
                "ORDER BY jadwal.no_hari DESC, jadwal.no_sesi DESC, jadwal.no_kelas DESC";
        rs_jadwal = connec.createStatement().executeQuery(sql_jadwal);

        return rs_jadwal.next();
    }
}
