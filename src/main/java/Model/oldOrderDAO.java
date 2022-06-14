package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Model.ordineDAO.listaTupleDB;

public class oldOrderDAO {

    public static oldOrder doRetriveById(utente u, listaVinili service){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT codice, prezzo, evaso, dataev,via,cap,civico FROM ordine WHERE id_user=? and evaso=true order by dataev desc");
            ps.setInt(1,u.getID());
            ResultSet rs = ps.executeQuery();
            oldOrder ret=new oldOrder();
            while(rs.next()) {
                ordine tmp = new ordine();
                tmp.setCodice(rs.getInt(1));
                tmp.setPrezzo(rs.getDouble(2));
                tmp.setEvaso(rs.getBoolean(3));
                tmp.setDataEvasione(rs.getDate(4));
                tmp.setVia(rs.getString(5));
                tmp.setCap(rs.getInt(6));
                tmp.setCivico(rs.getInt(7));
                System.out.println("2 code " + tmp.getCodice());
                tmp.setList(listaTupleDB(tmp, service));
                ret.getList().add(tmp);
            }
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
