package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OldOrderDAO {

    /**
     * Questo metodo restituisce i prodotti di un vecchio ordine, passato come parametro
     * @param o ordine di riferimento
     * @param service è la lista dei vinili disponibili
     * @return null, se l'ordine era vuoto, la lista dei prodotti altrimenti
     */
    public static List<Prodotto> listaTupleDbOldOrder(Ordine o, ListaVinili service){
        System.out.println("------(Leggo comporre per il vecchio ordine)------");
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id_vinile,quantita, prezzo FROM comporre WHERE code_ordine=?");
            ps.setInt(1,o.getCodice());
            ResultSet rs = ps.executeQuery();
            boolean flag=false;
            List<Prodotto> listaDB=new ArrayList<>();
            while(rs.next()){
                flag=true;
                System.out.println("------(Prodotto aggiunto)------ ");
                Prodotto pr=new Prodotto();
                pr.setArticolo(service.findViniliFromId(rs.getInt(1)));
                pr.setPrezzo(rs.getDouble(3));
                pr.setQuantitaOldOrder(rs.getInt(2));
                listaDB.add(pr);
            }
            if(!flag) { //se il risultato della query è vuoto, quindi non ci sono vecchi ordini
                System.out.println("------(Ordine vuoto)-----");
                return null;
            }
            return listaDB;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * questo metodo mostra tutti gli ordini vecchi di un utente, passato come parametro
     * @param u è l'utente di cui dobbiamo listare i vecchi ordini
     * @param service è la lista dei vinili disponibili
     * @return null se l'utente non aveva vecchi ordini, altrimenti tutti i vecchi ordini dell'utente
     */
    public static OldOrder doRetriveById(Utente u, ListaVinili service){ //il service serve per la chiamata al metodo per ottenere òa lista dei prodotti
        System.out.println("------(Inizio lettura dei vecchi ordini da DB)------");
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT codice, prezzo, evaso, dataev,via,cap,civico,citta FROM ordine WHERE id_user=? and evaso=true order by dataev desc");
            ps.setInt(1,u.getID());
            ResultSet rs = ps.executeQuery();
            OldOrder ret=new OldOrder();
            while(rs.next()) {
                Ordine tmp = new Ordine();
                tmp.setCodice(rs.getInt(1));
                tmp.setPrezzo(rs.getDouble(2));
                tmp.setEvaso(rs.getBoolean(3));
                tmp.setDataEvasione(rs.getDate(4));
                tmp.setVia(rs.getString(5));
                tmp.setCap(rs.getInt(6));
                tmp.setCivico(rs.getInt(7));
                tmp.setCitta(rs.getString(8));
                System.out.println("------(Ordine num "+tmp.getCodice()+" aggiunto)------");
                tmp.setList((ArrayList<Prodotto>) listaTupleDbOldOrder(tmp, service)); //setto la lista dei prodotti dell'ordine
                ret.add(tmp);
            }
            if(ret.size()>0)
                return ret;
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
