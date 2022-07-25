package Controller;

import Model.Tag;
import Model.TagsDAO;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "AddTag", value = "/AddTag")
public class AddTag extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome=request.getParameter("Nome");
        HttpSession snn=request.getSession();
        ArrayList<Tag> tags= (ArrayList<Tag>) snn.getAttribute("tags");
        Utente u= (Utente) snn.getAttribute("utente");
        System.out.println("p1");
        if(!snn.isNew()&&nome!=null&&tags!=null&&u!=null) {
            if(u.isAdmin_bool()) {
                String[] cheackbox = request.getParameterValues("tag");
                System.out.println("p2");
                System.out.println("p3");
                Tag t = TagsDAO.insertTag(nome);
                if (cheackbox != null)
                    if (cheackbox.length >= 1) {
                        System.out.println("p4");
                        for (String s : cheackbox) {
                            int x = Integer.parseInt(s);
                            TagsDAO.insertTagForVinil(x, t.getId_tag());
                        }
                    }
                tags.add(t);
                snn.setAttribute("tags", tags);
                response.sendRedirect("./Admin?src=adminTag");
            }else{
                snn.invalidate();
                RequestDispatcher dispatcher = request.getRequestDispatcher("/InitServlet");
                dispatcher.forward(request, response);
            }
        }else{
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
