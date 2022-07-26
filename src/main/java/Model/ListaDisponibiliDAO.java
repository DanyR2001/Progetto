package Model;

import java.sql.*;

public class ListaDisponibiliDAO {

    /**
     * Questo metodo restiruisce tutti i vinili presenti nel DB
     * @return lista dei vinili disponibili
     */
     public ListaVinili getAll(){
         System.out.println("---(inizio lista vinili da db)---");
         boolean flag=false;
         ListaVinili retun= new ListaVinili();
         try (Connection con = ConPool.getConnection()) {
             PreparedStatement ps =
                     con.prepareStatement("SELECT * FROM vinilidisp");
             ResultSet rs = ps.executeQuery();
             while(rs.next()){
                 System.out.println("---(Vinil added)--- ");
                 Vinile v=new Vinile();
                 v.setPK(rs.getInt(1));
                 v.setTitolo(rs.getString(2));
                 v.setPrezzo(rs.getDouble(3));
                 Integer quantita=rs.getInt(4);
                 v.setUrl(rs.getString(5));
                 v.setArtista(rs.getString(6));
                 v.setTags(TagsDAO.getTagByIdVinil(v.getPK()));
                 retun.add(v,quantita);
                 flag=true;
             }
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
         if(flag==false) {
             System.out.println("Nessun vinilie nel DB");
             return null;
         }
         else{
             return retun;
         }

     }

    /**
     * Questo metodo cambia nel DB la quantità di un vinile, andando a sottrarre la quantità passata come parametro
     * a quella totale del vinile presente nel DB
     * @param quantita quantità da rimuovere
     * @param id_vinile id del vinile in questione
     */
     public void changeQuantiti(int quantita,int id_vinile){
         //update vinilidisp set Quantita = Quantita - 2  where ID = 1;
         try (Connection con = ConPool.getConnection()) {
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE vinilidisp set Quantita  =Quantita - ? where ID=?");
             ps.setInt(1, quantita);
             ps.setInt(2, id_vinile);
             if (ps.executeUpdate() < 1) {
                 throw new RuntimeException("2 Update error.");
             }
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }

     }

    /**
     * questo metodo inserisce un nuovo vinile nel DB
     * @param v il vinile da aggiungere
     * @param quantita quanti vinili aggiungere nel DB
     */
    public void insertVinil(Vinile v, Integer quantita) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "insert into vinilidisp (Titolo,Prezzo,Quantita,Url,Artista) value (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, v.getTitolo());
            ps.setDouble(2, v.getPrezzo());
            ps.setInt(3,quantita);
            ps.setString(4,v.getUrl());
            ps.setString(5,v.getArtista());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            v.setPK(id);
            if(v.getTags()!=null)
                for(int i=0;i<v.getTags().size();i++)
                    TagsDAO.insertTagForVinil(v.getPK(),v.getTags().get(i).getId_tag()); //per ogni tag associato a quel vinile, lo aggiungo al DB
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * cambia i dati del vinile. il vinile passato come parametro è già modificato, lo si va a salvare nel database
     * @param v modificato che va salvato nel DB
     * @param quantita quantità del vinile
     */
    public void changeById(Vinile v, int quantita){
        //update vinilidisp set Quantita = Quantita - 2  where ID = 1;
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE vinilidisp set Quantita = ? , Prezzo = ?, Artista = ? where ID=?");
            ps.setInt(1, quantita);
            ps.setDouble(2,v.getPrezzo());
            ps.setString(3,v.getArtista());
            ps.setInt(4, v.getPK());
            if (ps.executeUpdate() < 1) {
                throw new RuntimeException("2 Update error.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}


