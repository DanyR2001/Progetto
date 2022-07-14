package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAO {

    public static void deleteAllPorductByOrder(int codice_ordine){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "delete from comporre  where code_ordine=?");
            ps.setInt(1,codice_ordine);
            int num=ps.executeUpdate();
            System.out.println("numero di eliminazioni "+num);
            if ( num < 1) {
                throw new RuntimeException("delete error.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    public static ArrayList<Vinile> uploadOrdine(Utente u, Ordine temp, ListaVinili service) {
        Ordine old=getCarrelloFromDb(u,service);
        if(old!=null) {
            System.out.println("2 size "+temp.getNumItem());
            if(old.getCarrello()!=null) {
                System.out.println("2 ciao pero ");
                if (old.getCarrello().size() > 0) {
                    System.out.println(" 2x cod ordine temp" + temp.getCodice() + " cod ordine old " + old.getCodice());
                    if(temp.getCodice()==null) {
                        System.out.println(" 2ciaoooo5678");
                        deleteAllPorductByOrder(old.getCodice());
                    }
                    else if(temp.getCodice()==old.getCodice()) {
                        System.out.println("2 ciaoooo1234");
                        deleteAllPorductByOrder(temp.getCodice());
                    }
                    else
                        System.out.println("other problem");

                }
            }
        }
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
            insertProductByOrder(p, temp.getCodice());
        }
        ArrayList<Vinile> ret=new ArrayList<>();
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
        System.out.println("2 inizio lista da db");
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id_vinile,quantita, prezzo FROM comporre WHERE code_ordine=?");
            ps.setInt(1,o.getCodice());
            System.out.println("2 ha2 "+o.getCodice());
            ResultSet rs = ps.executeQuery();
            boolean flag=false;
            List<Prodotto> listaDB=new ArrayList<>();
            System.out.println("2 ha3 ");
            while(rs.next()){
                flag=true;
                System.out.println("2 ha1ha ");
                Prodotto pr=new Prodotto();
                pr.setArticolo(service.findViniliFromId(rs.getInt(1)));
                pr.setPrezzo(rs.getDouble(3));
                pr.setQuantita(rs.getInt(2));
                listaDB.add(pr);
            }
            if(!flag) {
                System.out.println("2 haha 1212");
                return null;
            }
            return listaDB;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




//ok
    public static Ordine getCarrelloFromDb(Utente u, ListaVinili service){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT codice, prezzo, evaso FROM ordine WHERE id_user=? and evaso=false");
            ps.setInt(1,u.getID());
            ResultSet rs = ps.executeQuery();
            if(!rs.next())
                return null;
            Ordine tmp = new Ordine();
            tmp.setCodice(rs.getInt(1));
            tmp.setPrezzo(rs.getDouble(2));
            tmp.setEvaso(rs.getBoolean(3));
            System.out.println("2 code "+tmp.getCodice());
            tmp.setList((ArrayList<Prodotto>) listaTupleDB(tmp,service));
            return tmp;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void completeOrdine(Ordine carrello){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE ordine set evaso = ?, dataev = ? , via=?,cap=?,civico=? where codice=?");
            carrello.setEvaso(true);
            ps.setBoolean(1, carrello.isEvaso());
            long millis=System.currentTimeMillis();
            carrello.setDataEvasione(new Date(millis));
            ps.setDate(2, carrello.getDataEvasione());
            ps.setString(3,carrello.getVia());
            ps.setInt(4,carrello.getCap());
            ps.setInt(5,carrello.getCivico());
            ps.setInt(6,carrello.getCodice());
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
