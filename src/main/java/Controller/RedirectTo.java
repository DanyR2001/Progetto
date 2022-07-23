package Controller;

import Model.ListaVinili;
import Model.Tag;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "RedirectTo", value = "/RedirectTo")
public class RedirectTo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession(false);
        if(session!=null) {
            if(!session.isNew()) {
                String src = request.getParameter("src");
                if (src != null) {
                    if (src.equals("Tag") || src.equals("Vinile")) {
                        RequestDispatcher dispatcher = null;
                        Utente u = (Utente) session.getAttribute("untete");
                        if (u != null) {
                            if(u.isAdmin_bool()) {
                                String id = request.getParameter("id");
                                if (id != null) {
                                    int id_t = Integer.parseInt(id);
                                    ArrayList<Tag> lib = (ArrayList<Tag>) session.getAttribute("tags");
                                    Tag find = null;
                                    for (Tag t : lib)
                                        if (t.getId_tag() == id_t)
                                            find = t;
                                    ListaVinili libreria = (ListaVinili) session.getAttribute("libreria");
                                    ListaVinili lib_tag = libreria.getFromTag(find);
                                    session.setAttribute("Tag_selected", find);
                                    session.setAttribute("List_tag_selected", lib_tag);
                                    dispatcher = request.getRequestDispatcher("WEB-INF/modify" + src + ".jsp");
                                } else
                                    dispatcher = request.getRequestDispatcher("WEB-INF/add" + src + ".jsp");
                                dispatcher.forward(request, response);
                            }
                            else{
                                response.sendError(500);
                            }
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
        }else{
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
