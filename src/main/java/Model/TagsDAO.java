package Model;

import java.sql.*;
import java.util.ArrayList;

public class TagsDAO {

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
        remeveTagVinilByID(id);
    }

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
