package model;

public class Jadwal {

    private String id;
    private String dosen;
    private String dosenId;
    private String mataKuliah;
    private String mataKuliahId;
    private String kelas;
    private String kelasId;
    private String hari;
    private String hariId;
    private String sesi;
    private String sesiId;
    private String ruangan;
    private String ruanganId;
    private String kategori;

    public Jadwal() {
    }

    public Jadwal(String dosen, String dosenId, String mataKuliah, String mataKuliahId, String kelas, String kelasId) {
        this.dosen = dosen;
        this.dosenId = dosenId;
        this.mataKuliah = mataKuliah;
        this.mataKuliahId = mataKuliahId;
        this.kelas = kelas;
        this.kelasId = kelasId;
    }

    public Jadwal(String id, String dosen, String dosenId, String mataKuliah, String mataKuliahId, String kelas, String kelasId) {
        this.id = id;
        this.dosen = dosen;
        this.dosenId = dosenId;
        this.mataKuliah = mataKuliah;
        this.mataKuliahId = mataKuliahId;
        this.kelas = kelas;
        this.kelasId = kelasId;
    }

    public Jadwal(String id, String dosen, String dosenId, String mataKuliah, String mataKuliahId, String kelas, String kelasId, String hari, String hariId, String sesi, String sesiId, String ruangan, String ruanganId) {
        this.id = id;
        this.dosen = dosen;
        this.dosenId = dosenId;
        this.mataKuliah = mataKuliah;
        this.mataKuliahId = mataKuliahId;
        this.kelas = kelas;
        this.kelasId = kelasId;
        this.hari = hari;
        this.hariId = hariId;
        this.sesi = sesi;
        this.sesiId = sesiId;
        this.ruangan = ruangan;
        this.ruanganId = ruanganId;
    }

    public String getId() {
        return id;
    }

    public String getDosen() {
        return dosen;
    }

    public String getDosenId() {
        return dosenId;
    }

    public String getMataKuliah() {
        return mataKuliah;
    }

    public String getMataKuliahId() {
        return mataKuliahId;
    }

    public String getKelas() {
        return kelas;
    }

    public String getKelasId() {
        return kelasId;
    }

    public String getHari() {
        return hari;
    }

    public String getHariId() {
        return hariId;
    }

    public String getSesi() {
        return sesi;
    }

    public String getSesiId() {
        return sesiId;
    }

    public String getRuangan() {
        return ruangan;
    }

    public String getRuanganId() {
        return ruanganId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public void setDosenId(String dosenId) {
        this.dosenId = dosenId;
    }

    public void setMataKuliah(String mataKuliah) {
        this.mataKuliah = mataKuliah;
    }

    public void setMataKuliahId(String mataKuliahId) {
        this.mataKuliahId = mataKuliahId;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public void setKelasId(String kelasId) {
        this.kelasId = kelasId;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public void setHariId(String hariId) {
        this.hariId = hariId;
    }

    public void setSesi(String sesi) {
        this.sesi = sesi;
    }

    public void setSesiId(String sesiId) {
        this.sesiId = sesiId;
    }

    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
    }

    public void setRuanganId(String ruanganId) {
        this.ruanganId = ruanganId;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    @Override
    public String toString() {
        return "Jadwal{" +
                "id='" + id + '\'' +
                ", dosen='" + dosen + '\'' +
                ", dosenId='" + dosenId + '\'' +
                ", mataKuliah='" + mataKuliah + '\'' +
                ", mataKuliahId='" + mataKuliahId + '\'' +
                ", kelas='" + kelas + '\'' +
                ", kelasId='" + kelasId + '\'' +
                ", hari='" + hari + '\'' +
                ", hariId='" + hariId + '\'' +
                ", sesi='" + sesi + '\'' +
                ", sesiId='" + sesiId + '\'' +
                ", ruangan='" + ruangan + '\'' +
                ", ruanganId='" + ruanganId + '\'' +
                ", kategori='" + kategori + '\'' +
                '}';
    }
}
