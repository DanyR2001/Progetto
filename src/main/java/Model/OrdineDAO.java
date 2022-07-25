package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAO {

    /**
     * Questo metodo cancella un ordine dal DataBase dato il codice dell'ordine
     * @param codice_ordine è il codice dell'ordine da eliminare
     */
    public static void deleteAllPorductByOrder(int codice_ordine){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "delete from comporre  where code_ordine=?");
            ps.setInt(1,codice_ordine);
            int num = ps.executeUpdate();
            System.out.println("----(numero di tuple eliminate da comporre "+num+")----");
            if ( num < 1) {
                throw new RuntimeException("delete error.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * questo metodo inserisce un prodotto nell'ordine (in comporre)
     * @param p è il prodotto da inserire
     * @param codice_ordine è il codice dell'ordine
     */
    public static void insertProductByOrder(Prodotto p, int codice_ordine){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO comporre (id_vinile,code_ordine,quantita,prezzo )VALUES (?,?,?,?)");
            ps.setInt(1, p.getArticolo().getPK());
            ps.setInt(2, codice_ordine);
            ps.setInt(3,p.getQuantita());
            ps.setDouble(4, p.getPrezzo());
            if (ps.executeUpdate() < 1) {
                throw new RuntimeException("INSERT error.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Questo metodo aggiorna il carrello nel database e restituisce i vinili tolti (nel caso in cui ci sia l'utente)
     * @param u è l'utente
     * @param temp è il carrello della sessione
     * @param service è la lista dei vinili disponibili
     * @return la lista dei vinili tolti dal carrello
     */
    public static ArrayList<Vinile> uploadOrdine(Utente u, Ordine temp, ListaVinili service) {
        Ordine old = getCarrelloFromDb(u, service); //prendo carrello dal DB per tenere traccia di quello che ho tolto
        System.out.println("-----(aggiorno il carrello " + temp.getCodice() + " nel DB)----"); //prendo il carrello dell'utente dal DB
        if(old!=null) {
            if (old.getCarrello() != null) {
                if (old.getCarrello().size() > 0) { //e non è vuoto
                    System.out.println("----(elimino le vecchie occorrenze in comporre)----");
                    if (temp.getCodice() == null) {
                        deleteAllPorductByOrder(old.getCodice());
                    } else if (temp.getCodice() == old.getCodice()) {
                        deleteAllPorductByOrder(temp.getCodice());
                    } else
                        System.out.println("other problem");
                }
            }
        }

        System.out.println("----(aggiorno il prezzo nel DB)----");
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE ordine set prezzo = ? where codice=?");
            ps.setDouble(1, temp.getPrezzo());
            ps.setInt(2, temp.getCodice());
            if (ps.executeUpdate() < 1) {
                throw new RuntimeException("2 Update error.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(Prodotto p: temp.getCarrello()) {
            System.out.println("----(Inserisco un nuovo prodotto nel DB in comporre)----");
            insertProductByOrder(p, temp.getCodice());
        }
        ArrayList<Vinile> ret = new ArrayList<>(); //vinili tolti
        int i=0,j=0;
        if(old.getCarrello()!=null)
            for(i=0;i<old.getCarrello().size();i++) {
                boolean flag=true;
                for (j = 0; j < temp.getCarrello().size(); j++)
                    if(old.getCarrello().get(i).getArticolo().equals(temp.getCarrello().get(j).getArticolo()))
                        flag=false;
                if(flag)
                    ret.add(old.getCarrello().get(i).getArticolo());
            }
        if(ret.size()>0)
            return ret;
        return null;

    }

    public static void insertOrdine(Utente u, Ordine temp) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "Insert ordine (prezzo,id_user) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, temp.getPrezzo());
            ps.setInt(2, u.getID());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("Update error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int codice = rs.getInt(1);
            temp.setCodice(codice);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(Prodotto p: temp.getCarrello()) {
            insertProductByOrder(p, temp.getCodice());
        }
    }

//ok
    public static List<Prodotto> listaTupleDB(Ordine o, ListaVinili service){
        System.out.println("---(inizio lettura dei componenti del carrello da db)---");
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id_vinile,quantita, prezzo FROM comporre WHERE code_ordine=?");
            ps.setInt(1,o.getCodice());
            System.out.println("---(Lettura oldine "+o.getCodice()+" )---");
            ResultSet rs = ps.executeQuery();
            boolean flag=false;
            List<Prodotto> listaDB=new ArrayList<>();
            while(rs.next()){
                flag=true;
                System.out.println("---(Prodotto aggiunto)---");
                Prodotto pr=new Prodotto();
                pr.setArticolo(service.findViniliFromId(rs.getInt(1)));
                pr.setPrezzo(rs.getDouble(3));
                pr.setQuantita(rs.getInt(2));
                listaDB.add(pr);
            }
            if(!flag) {
                System.out.println("---(Lista comporre vuota)---");
                return null;
            }
            return listaDB;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Questo metodo prende il carrello di un utente dal DB
     * @param u è l'id dell'utente
     * @param service è la lista dei vinili disponibili
     * @return il carrello dell'utente se esiste nel Db, altrimenti null
     */
    public static Ordine getCarrelloFromDb(Utente u, ListaVinili service){

        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT codice, prezzo, evaso FROM ordine WHERE id_user=? and evaso=false");
            ps.setInt(1,u.getID());
            ResultSet rs = ps.executeQuery();

            if(!rs.next()) //se la query non restituisce nulla significa che non c'era un carrello del DB relativo all'utente
                return null;

            Ordine tmp = new Ordine(); //altrimenti restituisco il carrello
            tmp.setCodice(rs.getInt(1));
            tmp.setPrezzo(rs.getDouble(2));
            tmp.setEvaso(rs.getBoolean(3));
            System.out.println("---(Inzio lettuta carrella dal DB codice ordine "+tmp.getCodice()+")---");
            tmp.setList((ArrayList<Prodotto>) listaTupleDB(tmp,service));
            return tmp;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void completeOrdine(Ordine carrello){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE ordine set evaso = ?, dataev = ? , via=?,cap=?,civico=?,citta=? where codice=?");
            carrello.setEvaso(true);
            ps.setBoolean(1, carrello.isEvaso());
            long millis=System.currentTimeMillis();
            carrello.setDataEvasione(new Date(millis));
            ps.setDate(2, carrello.getDataEvasione());
            ps.setString(3,carrello.getVia());
            ps.setInt(4,carrello.getCap());
            ps.setInt(5,carrello.getCivico());
            ps.setString(6,carrello.getCitta());
            ps.setInt(7,carrello.getCodice());
            ListaDisponibiliDAO service=new ListaDisponibiliDAO();
            if(carrello.getCarrello()!=null)
                for(Prodotto p: carrello.getCarrello())
                    service.changeQuantiti(p.getQuantita(),p.getArticolo().getPK());
            if (ps.executeUpdate() < 1) {
                throw new RuntimeException("2 Update error.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
