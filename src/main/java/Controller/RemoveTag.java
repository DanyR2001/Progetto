package Controller;

import Model.Tag;
import Model.tagsDAO;
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
        Utente u = (Utente) snn.getAttribute("utente");
        ArrayList<Tag> tags= (ArrayList<Tag>) snn.getAttribute("tags");
        if(snn != null && u != null && tags != null)   {
            if(u.isAdmin_bool()){
                Integer id=Integer.parseInt(request.getParameter("ID_tag"));
                if(id!=null){
                    Tag fin=null;
                    tagsDAO.removeTagByID(id);
                    for(Tag t: tags)
                        if(t.getId_tag()==id)
                            fin=t;
                    if(fin!=null)
                        tags.remove(fin);
                    response.sendRedirect("./Admin?src=adminTag");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
