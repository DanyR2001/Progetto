package Controller;

import Model.Tag;
import Model.TagsDAO;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "RemoveTag", value = "/RemoveTag")
public class RemoveTag extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession(false);
        if(snn != null)   {
            ArrayList<Tag> tags= (ArrayList<Tag>) snn.getAttribute("tags");
            Utente u = (Utente) snn.getAttribute("utente");
            if(!snn.isNew()&&u!=null&&tags!= null) {
                if (u.isAdmin_bool()) {
                    String id_s=request.getParameter("ID_tag");
                    Integer id = null;
                    if(id_s!=null)
                        id=Integer.parseInt(id_s);
                    if (id != null) {
                        Tag fin = null;
                        TagsDAO.removeTagByID(id);
                        for (Tag t : tags)
                            if (t.getId_tag() == id)
                                fin = t;
                        if (fin != null)
                            tags.remove(fin);
                        response.sendRedirect("./Admin?src=adminTag");
                    }
                    else{
                        response.sendError(500);
                    }
                }
                else{
                    response.sendError(500);
                }
            }else{
                response.sendError(500);
            }
        }
        else{
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
