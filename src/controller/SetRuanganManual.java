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
import model.Jadwal;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetRuanganManual implements Initializable {

    @FXML
    public AnchorPane manualSetPane;
    @FXML
    private TextField kelasField;
    @FXML
    private ComboBox<String> ruanganCombo;
    @FXML
    private ComboBox<String> hariCombo;
    @FXML
    private ComboBox<String> sesiCombo;
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
    @FXML
    private TableColumn<Jadwal, String> tblKolomKategori;

    public Button btnDashboard;
    public Button btnToOutput;
    public Button btnSetRuanganPerHari;
    public Button btnSetRuanganPerSesi;

    private ObservableList<Jadwal> ol;
    private ObservableList<String> ruangan;
    private ObservableList<String> hari;
    private ObservableList<String> sesi;

    private Connection connec;
    private PreparedStatement prs;
    private ResultSet rs_jadwal;
    private String sql_jadwal;

    private String id_ruangan = null;
    private String ruangan_unset = null;
    private String id_matkul = null;
    private String id_jadwal = null;
    private String id_kategori = null;

    private int hari_dipilih;
    private int sesi_dipilih;
    private int ruanganSize;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connec = SQLHelper.getConnection();
        ol = FXCollections.observableArrayList();
        ruangan = FXCollections.observableArrayList();
        hari = FXCollections.observableArrayList();
        sesi = FXCollections.observableArrayList();

        fillComboBox();
        fillRuangan();
        fromTableToTextField();
        setCellValue();
    }

    @FXML
    private void toDashboard() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            manualSetPane.getChildren().setAll(ap);
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
        tblKolomKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
    }

    private void loadDataFromDatabase(int hari, int sesi) {
        ol.clear();
        try {
            if (check(hari, sesi)){
                rs_jadwal = connec.createStatement().executeQuery(sql_jadwal);

                while (rs_jadwal.next()){
                    if (rs_jadwal.getString("matkul").equalsIgnoreCase("PRAK KIMDAS") ||
                            rs_jadwal.getString("matkul").equalsIgnoreCase("PRAK KIMFIS") ||
                            rs_jadwal.getString("matkul").equalsIgnoreCase("PRAK FISDAS 1") ||
                            rs_jadwal.getString("matkul").equalsIgnoreCase("PRAK MIKRUM") ||
                            rs_jadwal.getString("matkul").equalsIgnoreCase("PRAK KIMOR") ||
                            rs_jadwal.getString("matkul").equalsIgnoreCase("PRAK PIP") ||
                            rs_jadwal.getString("matkul").equalsIgnoreCase("MATLAB")){
                        setPreferenceMatkul(rs_jadwal.getInt("id"), rs_jadwal.getString("matkul"));
                    }
                }

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
                    jadwal.setKategori(rs_jadwal.getString("kategori"));

                    ol.add(jadwal);
                }
            }

            totalData.setText("Total Data : " + ol.size());
        } catch (SQLException ex) {
            Logger.getLogger(SetRuanganManual.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblDataJadwal.setItems(ol);
    }

    private void fromTableToTextField() {
        tblDataJadwal.setOnMouseClicked((MouseEvent event) -> {
            Jadwal jadwal = tblDataJadwal.getItems().get(tblDataJadwal.getSelectionModel().getSelectedIndex());
            if (jadwal != null){
                id_jadwal = jadwal.getId();
                id_matkul = jadwal.getMataKuliahId();
                id_ruangan = jadwal.getRuanganId();
                if (jadwal.getKategori().equalsIgnoreCase("Teori")) id_kategori = "1";
                else if (jadwal.getKategori().equalsIgnoreCase("Praktikum")) id_kategori = "2";
                kelasField.setText(jadwal.getKelas());

                refreshRuanganComboBox();
            }
        });
    }

    private void setPreferenceMatkul(int id, String preference){
        if (preference.equalsIgnoreCase("PRAK KIMDAS")){
            String sql = "SELECT * FROM ruangan WHERE nama='GD812'";

            try {
                prs = connec.prepareStatement(sql);
                rs_jadwal = prs.executeQuery();

                while (rs_jadwal.next()){
                    String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                    prs = connec.prepareStatement(sql_ruangan);
                    prs.setInt(1, rs_jadwal.getInt("no"));
                    prs.setInt(2, id);
                    prs.executeUpdate();
                    prs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (preference.equalsIgnoreCase("PRAK FISDAS 1")){
            String sql = "SELECT * FROM ruangan WHERE nama='Lab Fisika'";

            try {
                prs = connec.prepareStatement(sql);
                rs_jadwal = prs.executeQuery();

                while (rs_jadwal.next()){
                    String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                    prs = connec.prepareStatement(sql_ruangan);
                    prs.setInt(1, rs_jadwal.getInt("no"));
                    prs.setInt(2, id);
                    prs.executeUpdate();
                    prs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (preference.equalsIgnoreCase("PRAK PIP")){
            String sql = "SELECT * FROM ruangan WHERE nama='GD726'";

            try {
                prs = connec.prepareStatement(sql);
                rs_jadwal = prs.executeQuery();

                while (rs_jadwal.next()){
                    String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                    prs = connec.prepareStatement(sql_ruangan);
                    prs.setInt(1, rs_jadwal.getInt("no"));
                    prs.setInt(2, id);
                    prs.executeUpdate();
                    prs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (preference.equalsIgnoreCase("MATLAB")){
            String sql = "SELECT * FROM ruangan WHERE nama='GD726'";

            try {
                prs = connec.prepareStatement(sql);
                rs_jadwal = prs.executeQuery();

                while (rs_jadwal.next()){
                    String sql_ruangan = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

                    prs = connec.prepareStatement(sql_ruangan);
                    prs.setInt(1, rs_jadwal.getInt("no"));
                    prs.setInt(2, id);
                    prs.executeUpdate();
                    prs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshRuanganComboBox() {
        ruanganCombo.setEditable(true);
        ruanganCombo.getItems().clear();

        ruanganCombo.setItems(checkKapasitas());
        new AutoCompleteBoxHelper(ruanganCombo);
    }

    private void refreshHariComboBox() {
        hariCombo.getItems().clear();
        hariCombo.setItems(checkStatusHari());
    }

    private void refreshSesiComboBox() {
        sesiCombo.getItems().clear();
        sesiCombo.setItems(checkStatusSesi());
    }

    private void clearText(){
        kelasField.setText("");
        ruanganCombo.setValue("");
    }

    public void onClickRuanganCombo() {
        String[] id = (ruanganCombo.getSelectionModel().getSelectedItem()).split(" ");

        try {
            String sql = "SELECT * FROM ruangan WHERE nama=?";
            prs = connec.prepareStatement(sql);
            prs.setString(1, id[0]);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                id_ruangan = rs_jadwal.getString("no");
            }
        } catch (SQLException e) {
            System.out.println("Erorr");
        }

        String sql_ruangan = "UPDATE ruangan SET status=? WHERE nama=?";

        try {
            prs = connec.prepareStatement(sql_ruangan);
            prs.setInt(1, 2);
            prs.setString(2, id[0]);
            prs.executeUpdate();
            prs.close();
        } catch (SQLException ex) {
            System.out.println("Erorr");
        }

        String sql = "UPDATE jadwal SET no_ruangan=? WHERE id_jadwal=?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, id_ruangan);
            prs.setString(2, id_jadwal);
            int exec = prs.executeUpdate();

            if(exec == 1){
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil di-set", ButtonType.OK);
//                alert.setTitle("Set Ruangan");
//                alert.showAndWait();
                loadDataFromDatabase(onClickHariCombo(), onClickSesiCombo());
                fillComboBox();
                clearText();
            }

            prs.close();
        } catch (SQLException ex) {
            System.out.println("Erorr");
        }
    }

    private void fillComboBox(){
        ruanganCombo.setEditable(true);
        ruanganCombo.getItems().clear();
        hariCombo.getItems().clear();
        sesiCombo.getItems().clear();

        try {
            String sql = "SELECT * FROM ruangan WHERE status='1' OR status='3'";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                ruangan.add(rs_jadwal.getString("nama") + " | " + rs_jadwal.getString("kapasitas"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM hari WHERE status='1' ORDER BY no DESC";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                hari.add(rs_jadwal.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM sesi WHERE status='1' ORDER BY no DESC";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                sesi.add(rs_jadwal.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ruanganCombo.setItems(ruangan);
        hariCombo.setItems(hari);
        sesiCombo.setItems(sesi);
        new AutoCompleteBoxHelper(ruanganCombo);
    }

    public int onClickSesiCombo() {
        String sql = "SELECT * FROM sesi WHERE nama=?";
        try {
            prs = connec.prepareStatement(sql);
            prs.setString(1, sesiCombo.getSelectionModel().getSelectedItem());
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                sesi_dipilih = Integer.parseInt(rs_jadwal.getString("no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadDataFromDatabase(hari_dipilih, sesi_dipilih);
        return sesi_dipilih;
    }

    public int onClickHariCombo() {
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

        loadDataFromDatabase(hari_dipilih, sesi_dipilih);

        return hari_dipilih;
    }

    private void fillRuangan() {
        try {
            String sql = "SELECT * FROM ruangan ORDER BY no DESC";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                if (rs_jadwal.getString("nama").equalsIgnoreCase("Belum di-set")){
                    ruangan_unset = rs_jadwal.getString("no");
                }
                ruanganSize++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean check(int hari, int sesi) throws SQLException {
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
                "WHERE jadwal.no_hari='" + hari + "' AND jadwal.no_sesi='" + sesi + "'" +
                "ORDER BY jadwal.no_hari DESC, jadwal.no_sesi DESC, jadwal.no_kelas DESC";
        rs_jadwal = connec.createStatement().executeQuery(sql_jadwal);

        return rs_jadwal.next();
    }

    private ObservableList<String> checkKapasitas(){
        int jumlah = 0;
        try {
            String sql = "SELECT * FROM matkul WHERE no='" + id_matkul + "'";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                jumlah = rs_jadwal.getInt("jumlah");
            }

            for (int i=1; i<=ruanganSize; i++){
                String sql_ruangan = "SELECT * FROM ruangan WHERE no='" + i + "' AND status='1'";
                prs = connec.prepareStatement(sql_ruangan);
                ResultSet rs = prs.executeQuery();

                int kapasitas;
                int delta;

                while (rs.next()){
                    if (!rs.getString("nama").equalsIgnoreCase("Belum di-set")){
                        kapasitas = rs.getInt("kapasitas");
                        delta = kapasitas - jumlah;

                        if (delta >= 0){
                            ruangan.add(rs.getString("nama") + "  | " + rs.getString("kapasitas"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!ruangan.isEmpty()) return ruangan;
        else ruangan.add("Ruangan tidak tersedia"); return ruangan;
    }

    private ObservableList<String> checkStatusHari(){
        try {
            String sql = "SELECT * FROM hari WHERE status='1' ORDER BY no DESC";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                hari.add(rs_jadwal.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!hari.isEmpty()) return hari;
        else hari.add("Tidak tersedia"); return hari;
    }

    private ObservableList<String> checkStatusSesi(){
        try {
            String sql = "SELECT * FROM sesi WHERE status='1' ORDER BY no DESC";
            prs = connec.prepareStatement(sql);
            rs_jadwal = prs.executeQuery();

            while (rs_jadwal.next()){
                sesi.add(rs_jadwal.getString("nama"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!sesi.isEmpty()) return sesi;
        else sesi.add("Ruangan tidak tersedia"); return sesi;
    }

    public void setRuanganPerHari() {
        String sql = "UPDATE hari SET status='2' WHERE no=?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setInt(1, hari_dipilih);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil di-set", ButtonType.OK);
                alert.setTitle("Berhasil");
                alert.showAndWait();
                loadDataFromDatabase(onClickHariCombo(), onClickSesiCombo());
                fillComboBox();
                clearText();
            }

            prs.close();
        } catch (SQLException ex) {
            System.out.println("Erorr");
        }

        refreshHariComboBox();
        resetSesi();
        resetRuangan();
        tblDataJadwal.getItems().clear();
    }

    public void setRuanganPerSesi() {
        String sql = "UPDATE sesi SET status='2' WHERE no=?";

        try {
            prs = connec.prepareStatement(sql);
            prs.setInt(1, sesi_dipilih);
            int exec = prs.executeUpdate();

            if(exec == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Berhasil di-set", ButtonType.OK);
                alert.setTitle("Berhasil");
                alert.showAndWait();
                loadDataFromDatabase(onClickHariCombo(), onClickSesiCombo());
                fillComboBox();
                clearText();
            }

            prs.close();
        } catch (SQLException ex) {
            System.out.println("Erorr");
        }

        refreshSesiComboBox();
        resetRuangan();
        tblDataJadwal.getItems().clear();
    }

    private void resetRuangan(){
        for (int i=1; i<ruanganSize; i++){
            if (i!=Integer.parseInt(ruangan_unset)){
                String sql_ruangan = "UPDATE ruangan SET status='1' WHERE no=?";

                try {
                    prs = connec.prepareStatement(sql_ruangan);
                    prs.setInt(1, i);
                    prs.executeUpdate();
                    prs.close();
                } catch (SQLException ex) {
                    System.out.println("Erorr");
                }
            }
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

    public void toOutput() {
        try{
            AnchorPane ap = FXMLLoader.load(getClass().getResource("../view/output.fxml"));
            manualSetPane.getChildren().setAll(ap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
