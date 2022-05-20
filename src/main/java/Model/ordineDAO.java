package Model;

import jakarta.servlet.http.HttpSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ordineDAO {

    private static ArrayList<prodotto> listElementCarrello(ordine o,HttpSession snn){
        System.out.println(" inizio lista da db");
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id_vinile,quantita, prezzo FROM comporre WHERE code_ordine=?");
            ps.setInt(1,o.getCodice());
            System.out.println(" ha2 "+o.getCodice());
            ResultSet rs = ps.executeQuery();
            listaVinili service= (listaVinili) snn.getAttribute("lista");
            ordine carrello = (ordine) snn.getAttribute("carrello");
            boolean flag=false;
            ArrayList<prodotto> listaDB=new ArrayList<>();
            System.out.println(" ha3 ");
            while(rs.next()){
                flag=true;
                System.out.println(" ha1ha ");
                prodotto pr=new prodotto();
                pr.setArticolo(service.findVinilieFromId(rs.getInt(1)));
                pr.setPrezzo(rs.getDouble(3));
                pr.setQuantita(rs.getInt(2));
                listaDB.add(pr);
            }
            if(flag==false) {
                System.out.println("haha 1212");
                return null;
            }

            ArrayList<prodotto> ret=new ArrayList<>();
            for(prodotto p: listaDB) {
                System.out.println(" ha5 "+p.getArticolo().getTitolo()+" quantita p "+p.getQuantita()+" service quantita "+service.numDispVinil(p.getArticolo()));
                if (service.isAvable(p.getArticolo()) && service.numDispVinil(p.getArticolo()) >= p.getQuantita()) {
                    System.out.println(" ha51 ");
                    ret.add(p);
                }
                else if(service.isAvable(p.getArticolo()) && service.numDispVinil(p.getArticolo()) < p.getQuantita()){
                    System.out.println(" ha52 disp "+service.numDispVinil(p.getArticolo())+" p "+p.getQuantita());
                    p.setQuantita(service.numDispVinil(p.getArticolo()));
                    if(carrello!=null){
                        System.out.println("hello");
                        if(carrello.getItem(p)!=null)
                            p.setQuantita(service.numDispVinil(p.getArticolo())+carrello.getItem(p).getQuantita());
                    }
                    ret.add(p);
                }
                else if(!service.isAvable(p.getArticolo())&&ordine.isPresent(carrello.getCarrello(),p)){
                    System.out.println("ha23haha");
                    ret.add(p);
                    service.aggiorna(p.getArticolo(),p.getQuantita());
                }
                if(service.isAvable(p.getArticolo())){
                    System.out.println(" ha53 "+p.getQuantita());
                    service.aggiorna(p.getArticolo(),p.getQuantita());
                    System.out.println("ha "+service.numDispVinil(p.getArticolo()));
                }
            }
            if(ret.size()!=0) {
                System.out.println("che ridere");
                return ret;
            }
            else {
                System.out.println(" ha6 "+ret.size());
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ordine doRetrieveByUser(utente u,HttpSession snn){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT codice, prezzo, evaso FROM ordine WHERE id_user=? and evaso=false");
            ps.setInt(1,u.getID());
            ResultSet rs = ps.executeQuery();
            if(!rs.next())
                return null;
            ordine tmp = new ordine();
            tmp.setCodice(rs.getInt(1));
            tmp.setPrezzo(rs.getDouble(2));
            tmp.setEvaso(rs.getBoolean(3));
            System.out.println(" code "+tmp.getCodice());
            tmp.setList(listElementCarrello(tmp,snn));
            return tmp;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    public static void insertProductByOrder(prodotto p, int codice_ordine){
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

    public static ArrayList<vinile> uploadOrdine(utente u, ordine temp,HttpSession snn) {
        ordine old=doRetrieveByUser(u,snn);
        if(old.getCarrello()!=null)
        for(prodotto p:old.getCarrello())
            System.out.println(" titolo "+p.getArticolo().getTitolo()+" quantita "+p.getQuantita());
        if(old!=null) {
            System.out.println("size "+temp.getNumItem());
            if(old.getCarrello()!=null) {
                System.out.println("ciao pero ");
                if (old.getCarrello().size() > 0) {
                    System.out.println("x cod ordine temp" + temp.getCodice() + " cod ordine old " + old.getCodice());
                    if(temp.getCodice()==null) {
                        System.out.println("ciaoooo5678");
                        deleteAllPorductByOrder(old.getCodice());
                    }
                    else if(temp.getCodice()==old.getCodice()) {
                        System.out.println("ciaoooo1234");
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
                throw new RuntimeException("Update error.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList<vinile> ret=new ArrayList<>();
        for(prodotto p: temp.getCarrello()) {
            insertProductByOrder(p, temp.getCodice());
        }
        if(old!=null&&old.getCarrello()!=null)
            if(old.getCarrello().size()>0)
                for(int i=0;i<old.getCarrello().size();i++){
                    boolean flag= true;
                    vinile v=old.getCarrello().get(i).getArticolo();
                    for(int j=0;j<temp.getCarrello().size();j++){
                        if(v.equals(temp.getCarrello().get(j).getArticolo()))
                            flag=false;
                    }
                    if(flag)
                        ret.add(v);
                }
        if(ret.size()>0)
            return ret;
        else
            return null;
    }

    public static void uploadOrdineStop(utente u, ordine temp,HttpSession snn) {
        ordine old=getCarrelloFromDb(u,snn);
        if(old.getCarrello()!=null)
            for(prodotto p:old.getCarrello())
                System.out.println("2 titolo "+p.getArticolo().getTitolo()+" quantita "+p.getQuantita());
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

    }

    public static void insertOrdine(utente u, ordine temp) {
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
        for(prodotto p: temp.getCarrello()) {
            insertProductByOrder(p, temp.getCodice());
        }
    }


    public static ArrayList<prodotto> listaTupleDB(ordine o,HttpSession snn){
        System.out.println("2 inizio lista da db");
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id_vinile,quantita, prezzo FROM comporre WHERE code_ordine=?");
            ps.setInt(1,o.getCodice());
            System.out.println("2 ha2 "+o.getCodice());
            ResultSet rs = ps.executeQuery();
            listaVinili service= (listaVinili) snn.getAttribute("lista");
            boolean flag=false;
            ArrayList<prodotto> listaDB=new ArrayList<>();
            System.out.println("2 ha3 ");
            while(rs.next()){
                flag=true;
                System.out.println("2 ha1ha ");
                prodotto pr=new prodotto();
                pr.setArticolo(service.findVinilieFromId(rs.getInt(1)));
                pr.setPrezzo(rs.getDouble(3));
                pr.setQuantita(rs.getInt(2));
                listaDB.add(pr);
            }
            if(flag==false) {
                System.out.println("2 haha 1212");
                return null;
            }
            return listaDB;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static ordine getCarrelloFromDb(utente u,HttpSession snn){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT codice, prezzo, evaso FROM ordine WHERE id_user=? and evaso=false");
            ps.setInt(1,u.getID());
            ResultSet rs = ps.executeQuery();
            if(!rs.next())
                return null;
            ordine tmp = new ordine();
            tmp.setCodice(rs.getInt(1));
            tmp.setPrezzo(rs.getDouble(2));
            tmp.setEvaso(rs.getBoolean(3));
            System.out.println("2 code "+tmp.getCodice());
            tmp.setList(listaTupleDB(tmp,snn));
            return tmp;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
