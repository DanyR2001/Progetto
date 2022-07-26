package Model;

import java.sql.*;
import java.util.ArrayList;

public class TagsDAO {

    /**
     * questo metodo restituisce tutti i tag associati a un vinile
     * @param id è l'id del vinile
     * @return la lista dei tag del vinile, altrimenti null se non esiste il vinile o il vinile non ha tag
     */
    public static ArrayList<Tag> getTagByIdVinil(int id){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT tags.id_tag,tags.nome FROM vinil_tag,tags WHERE id_vinile=? and tags.id_tag=vinil_tag.id_tag");
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            boolean flag=false;
            ArrayList<Tag> ret=new ArrayList<>();
            while(rs.next()){
                flag=true;
                Tag tag_retrun=new Tag();
                tag_retrun.setId_tag(rs.getInt(1));
                tag_retrun.setNome(rs.getString(2));
                ret.add(tag_retrun);
            }
            if(flag==false) {
                return null;
            }
            else if(ret.size()<1)
                return null;
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * questo metodo inserisce un tag a un vnile
     * @param id_vinile è l'id del vinile a cui inserire il tag
     * @param id_tag è l'id del tag da inserie
     */
    public static void insertTagForVinil(int id_vinile,int id_tag){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("insert into vinil_tag value (?,?)");
            ps.setInt(1,id_vinile);
            ps.setInt(2,id_tag);
            int rs = ps.executeUpdate();
            if(rs==0)
                throw new RuntimeException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * questo metodo prende tutti i tag presenti nel DB
     * @return la lista dei tag se ci sono, altrimenti null
     */
    public static ArrayList<Tag> getAll(){
        System.out.println("---(inizio lista Tag da db)---");
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM tags");
            ResultSet rs = ps.executeQuery();
            boolean flag=false;
            ArrayList<Tag> ret=new ArrayList<>();
            while(rs.next()){
                System.out.println("---(Tag added)---");
                flag=true;
                Tag tag_retrun=new Tag();
                tag_retrun.setId_tag(rs.getInt(1));
                tag_retrun.setNome(rs.getString(2));
                ret.add(tag_retrun);
            }
            if(flag==false) {
                return null;
            }
            else if(ret.size()<1)
                return null;
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * questo metodo rimuove a tutti i vinili che lo avevano, il tag passato come parametro
     *  (vengono eliminate le tuple di vinili associate al tag)
     * @param id è l'id del tag da rimuovere
     */
    public static void remeveTagVinilByID(Integer id){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("delete FROM vinil_tag where id_tag=?");
            ps.setInt(1,id);
            int rs = ps.executeUpdate();
            if(rs>=1)
                System.out.println("--(Rimozione Tag-Vinile andata a buon fine)--");
            else
                System.out.println("--(Rimozione Tag-Vinile non andata a buon fine)--");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**questo metodo rimuove un tag dal DB
     * @param id è l'id del tag da rimuovere
     */
    public static void removeTagByID(Integer id) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("delete FROM tags where id_tag=?");
            ps.setInt(1,id);
            int rs = ps.executeUpdate();
            if(rs>=1)
                System.out.println("--(Rimozione Tag andata a buon fine)--");
            else
                System.out.println("--(Rimozione Tag non andata a buon fine)--");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        remeveTagVinilByID(id); //tolgo dai vinili il tag
    }

    /**
     * questo metodo inserisce un nuovo tag nel DB
     * @param name è il nome del tag
     * @return il tag aggiunto
     */
    public static Tag insertTag(String name){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO tags (nome) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            Tag ret=new Tag();
            ret.setId_tag(id);
            ret.setNome(name);
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Questo metodo modifica il nome di un tag nel DB
     * @param id è l'id del tag da modificare
     * @param newName è il nuovo nome del tag
     */
    public static void uploadTagByID(Integer id, String newName) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE tags set nome=? where id_tag=?");
            ps.setString(1, newName);
            ps.setInt(2, id);
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
