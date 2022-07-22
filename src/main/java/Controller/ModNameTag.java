package Controller;

import Model.Tag;
import Model.TagsDAO;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ModNameTag", value = "/ModNameTag")
public class ModNameTag extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession();
        Utente u = (Utente) snn.getAttribute("utente");
        String newName=request.getParameter("Nome");
        ArrayList<Tag> tags= (ArrayList<Tag>) snn.getAttribute("tags");
        String ID=request.getParameter("ID_tag");
        Integer id=null;
        if(ID!=null)
            id=Integer.parseInt(ID);
        if(!snn.isNew() && u != null && newName != null && id!=null && tags!= null) {
            if (u.isAdmin_bool()) {
                for (Tag t : tags)
                    if (t.getId_tag() == id)
                        t.setNome(newName);
                TagsDAO.uploadTagByID(id, newName);
                response.sendRedirect("./Admin?src=adminTag");
            } else {
                snn.invalidate();
                RequestDispatcher dispatcher = request.getRequestDispatcher("/InitServlet");
                dispatcher.forward(request, response);
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
