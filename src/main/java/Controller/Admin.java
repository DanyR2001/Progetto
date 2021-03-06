package Controller;

import Model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "Admin", value = "/Admin")
public class Admin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession();
        Utente u= (Utente) snn.getAttribute("utente"); //prendo l'utente dalla sessione
        ListaDisponibiliDAO service = new ListaDisponibiliDAO();
        ArrayList<Tag> lista = TagsDAO.getAll();
        if(snn!=null&&lista!=null&&service!=null) {
            if(!snn.isNew()) {
                if (u != null) {
                    if (u.isAdmin_bool()) {
                        ListaVinili libreria = service.getAll();
                        snn.setAttribute("libreria", libreria);
                        snn.setAttribute("tags", lista);
                        RequestDispatcher dispatcher = null;
                        String src = request.getParameter("src");
                        if (src == null)
                            dispatcher = request.getRequestDispatcher("/WEB-INF/gestione.jsp");
                        else {
                            if (src.equals("adminVinile") || src.equals("adminTag")) {
                                dispatcher = request.getRequestDispatcher("/WEB-INF/" + src + ".jsp");
                            } else {
                                dispatcher = request.getRequestDispatcher("/Logout");
                            }
                        }
                        dispatcher.forward(request, response);
                    } else {
                        snn.invalidate();
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/InitServlet");
                        dispatcher.forward(request, response);
                    }
                } else {
                    response.sendError(500);
                }
            }else{
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
