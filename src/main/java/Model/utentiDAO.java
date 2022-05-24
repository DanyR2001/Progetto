package Model;

import java.sql.*;

public class utentiDAO {

    public static void doSave(utente u) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Users (nome, cognome,mail,dat,passwordhash,admin_bool)VALUES (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getNome());
            ps.setString(2, u.getCognome());
            ps.setString(3, u.getMail());
            ps.setDate(4, u.getDataNascita());
            ps.setString(5, u.getPasswordhash());
            ps.setBoolean(6, u.isAdmin_bool());

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            u.setID(id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static utente doRetrieveByUsernamePassword(String mail,String passw){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id, nome, cognome,mail,dat,passwordhash,admin_bool FROM Users WHERE mail=? AND passwordhash=SHA1(?)");
            ps.setString(1,mail);
            ps.setString(2,passw);
            ResultSet rs = ps.executeQuery();
            if(!rs.next())
                return null;
            return new utente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getDate(5),rs.getString(6),rs.getBoolean(7));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
