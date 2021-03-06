package Model;

import java.io.IOException;
import java.sql.*;

public class UtentiDAO {

    /**
     * questo metodo aggiorna i dati di un utente nel DB
     * @param u è l'utente da inserire nel DB
     */
    public static void doUpdate(Utente u){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "Update Users set nome=?, cognome = ?, dat = ? , passwordhash = ? , via = ? , cap = ?, civico = ?, citta = ? where id = ?");
            ps.setString(1, u.getNome());
            ps.setString(2, u.getCognome());
            ps.setDate(3, u.getDataNascita());
            ps.setString(4, u.getPasswordhash());
            ps.setString(5,u.getVia());
            ps.setInt(6,u.getCap());
            ps.setInt(7,u.getCivico());
            ps.setString(8,u.getCitta());
            ps.setInt(9,u.getID());;
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * questo metodo inserisce un nuovo utente nel DB
     * @param u è l'utente da inserire
     * @return true se l'utente è stato inserito con successo, false altrimenti
     * @throws IOException
     */
    public static boolean doSave(Utente u) throws IOException {
        boolean status=true;
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Users (nome, cognome, mail, dat, passwordhash, admin_bool, via, cap, civico, citta) VALUES (?,?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getNome());
            ps.setString(2, u.getCognome());
            ps.setString(3, u.getMail());
            ps.setDate(4, u.getDataNascita());
            ps.setString(5, u.getPasswordhash());
            ps.setBoolean(6, u.isAdmin_bool());
            ps.setString(7,u.getVia());
            ps.setInt(8,u.getCap());
            ps.setInt(9,u.getCivico());
            ps.setString(10,u.getCitta());

            if (ps.executeUpdate() != 1) {
                status=false;
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            u.setID(id);

        } catch (SQLException e) {
            status=false;
        }
        return status;
    }

    /**
     * prendere un utente dal DB data la mail e la password
     * @param mail è la mail dell'utente
     * @param passw è la password dell'utente
     * @return l'utente se esiste, null altrimenti
     */
    public static Utente doRetrieveByUsernamePassword(String mail, String passw){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id, nome, cognome,mail,dat,passwordhash,admin_bool,via,cap,civico, citta FROM Users WHERE mail=? AND passwordhash=SHA1(?)");
            ps.setString(1,mail);
            ps.setString(2,passw);
            ResultSet rs = ps.executeQuery();
            if(!rs.next())
                return null;
            Utente u=new Utente();
            u.setID(rs.getInt(1));
            u.setNome(rs.getString(2));
            u.setCognome(rs.getString(3));
            u.setMail(rs.getString(4));
            u.setDataNascita(rs.getDate(5));
            u.setPasswordFromDB(rs.getString(6));
            u.setAdmin_bool(rs.getBoolean(7));
            u.setVia(rs.getString(8));
            u.setCap(rs.getInt(9));
            u.setCivico(rs.getInt(10));
            u.setCitta(rs.getString(11));
            return u;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
