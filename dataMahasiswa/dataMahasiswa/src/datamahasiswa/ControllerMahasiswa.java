package datamahasiswa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class ControllerMahasiswa {
    ArrayList<ModelMahasiswa> ArrayData;
    DefaultTableModel tabelModel;

    public ControllerMahasiswa() {
        ArrayData = new ArrayList<ModelMahasiswa>();
    }

    public void InsertData(String npm, String nama, int tinggi, boolean pindahan) {
        String pindahanText = pindahan ? "Ya" : "Tidak";
        ModelMahasiswa mhs = new ModelMahasiswa(npm, nama, tinggi, pindahan);

        ArrayData.add(mhs);

        try (Connection conn = koneksi.getConnect()) {
            String checkSql = "SELECT COUNT(*) FROM tb_mahasiswa WHERE npm = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, npm);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("NPM sudah ada: " + npm);
                return;
            }
            
            String sql = "INSERT INTO tb_mahasiswa (npm, nama, tinggi, pindahan) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, npm);
            stmt.setString(2, nama);
            stmt.setInt(3, tinggi);
            stmt.setBoolean(4, pindahan);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DefaultTableModel showData() {
        String[] kolom = {"NPM", "Nama", "Tinggi", "Pindah"};
        ArrayList<ModelMahasiswa> mahasiswaList = new ArrayList<>();
        
        try (Connection conn = koneksi.getConnect()) {
            String sql = "SELECT * FROM tb_mahasiswa";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String npm = rs.getString("npm");
                String nama = rs.getString("nama");
                int tinggi = rs.getInt("tinggi");
                boolean pindahan = rs.getBoolean("pindahan");
                String pindahanText = pindahan ? "Ya" : "Tidak";

                ModelMahasiswa mhs = new ModelMahasiswa(npm, nama, tinggi, pindahan);
                mahasiswaList.add(mhs);
            }

            Object[][] objData = new Object[mahasiswaList.size()][4];
            for (int i = 0; i < mahasiswaList.size(); i++) {
                ModelMahasiswa mhs = mahasiswaList.get(i);
                objData[i] = new Object[]{mhs.getNPM(), mhs.getNama(), mhs.getTinggi(), mhs.isPindahan()};
            }

            tabelModel = new DefaultTableModel(objData, kolom) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tabelModel;
    }
}