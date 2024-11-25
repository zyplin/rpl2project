package datamahasiswa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class koneksi {
    private static Connection conn;

    public static Connection getConnect() {
        String host = "jdbc:mysql://localhost:3306/db_mahasiswa";
        String user = "root";
        String pass = "";

        try {
            conn = DriverManager.getConnection(host, user, pass);
            JOptionPane.showMessageDialog(null, "Delete Data Berhasil");
        } catch (SQLException err) {
            JOptionPane.showMessageDialog(null, "Koneksi Gagal: " + err.getMessage());
        }
        return conn;
    }
}