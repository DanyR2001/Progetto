package Controller;

import Model.Tag;
import Model.TagsDAO;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateTagList", value = "/UpdateTagList")
public class UpdateTagList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        if(!session.isNew()) {
            String[] cheackbox=request.getParameterValues("vin");
            Tag t= (Tag) session.getAttribute("Tag_selected");
            Utente u = (Utente) session.getAttribute("utente");
            if (cheackbox != null && t != null&&u!=null) {
                if(u.isAdmin_bool()) {
                    System.out.println("-----(Admin e tutti parametri correti: modifica vinili relativi a un tag)-----");
                    TagsDAO.remeveTagVinilByID(t.getId_tag());
                    for (int i = 0; i < cheackbox.length; i++) {
                        System.out.println("----(vinile con id " + cheackbox[i]+" aggiunto a tag "+t.getNome()+")----");
                        TagsDAO.insertTagForVinil(Integer.parseInt(cheackbox[i]), t.getId_tag());
                    }
                    session.setAttribute("Tag_selected", null);
                    response.sendRedirect("./Admin?src=adminTag");
                } else{
                    response.sendError(500);
                }
            }else{
                response.sendError(500);
            }
        } else{
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
