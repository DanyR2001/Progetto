package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class tagsDAO {

    public static ArrayList<tag> getTagByIdVinil(int id){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT tags.id_tag,tags.nome FROM vinil_tag,tags WHERE id_vinile=? and tags.id_tag=vinil_tag.id_tag");
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            boolean flag=false;
            ArrayList<tag> ret=new ArrayList<>();
            while(rs.next()){
                flag=true;
                tag tag_retrun=new tag();
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

    public static ArrayList<tag> getAll(){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM tags");
            ResultSet rs = ps.executeQuery();
            boolean flag=false;
            ArrayList<tag> ret=new ArrayList<>();
            while(rs.next()){
                flag=true;
                tag tag_retrun=new tag();
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
}
