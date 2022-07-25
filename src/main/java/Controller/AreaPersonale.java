package Controller;

import Model.OldOrder;
import Model.OldOrderDAO;
import Model.ListaVinili;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AreaPersonale", value = "/AreaPersonale")
public class AreaPersonale extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession();
        if(!snn.isNew()){
            Utente u= (Utente) snn.getAttribute("utente");
            ListaVinili lib= (ListaVinili) snn.getAttribute("libreria");
            if(lib!=null) {
                if (u != null) {
                    System.out.println("------(Area presonale - Log)------");
                    if(!u.isAdmin_bool()) {
                        OldOrder old = OldOrderDAO.doRetriveById(u, lib);
                        snn.setAttribute("OldOrdini", old);
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/AreaPersonale.jsp");
                        dispatcher.forward(request, response);
                    }else {
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/gestione.jsp");
                        dispatcher.forward(request, response);
                    }
                } else {
                    System.out.println("------(Area presonale - NoLog)------");
                    snn.setAttribute("noLogArea", false);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/access.jsp");
                    dispatcher.forward(request, response);
                }
            }
            else{
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
