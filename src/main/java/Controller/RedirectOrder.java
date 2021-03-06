package Controller;

import Model.Ordine;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "RedirectOrder", value = "/RedirectOrder")
public class RedirectOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        Utente user= (Utente) session.getAttribute("utente");
        Ordine carrello=(Ordine) session.getAttribute("carrello");
        if(session!=null&&carrello!=null) {
            if(!session.isNew()) {
                if (user == null) {
                    System.out.println("--(Utente non loggato)--");
                    session.setAttribute("noLogOrder", false);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/access.jsp");
                    dispatcher.forward(request, response);
                } else {
                    if(user.isAdmin_bool()){
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/gestione.jsp");
                        dispatcher.forward(request, response);
                    }else {
                        System.out.println("--(Utente loggato)--");
                        if (carrello.getCarrello() == null) {
                            System.out.println("--(lista prodotti vuota)--");
                            //non si fa nulla
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                            dispatcher.forward(request, response);
                        } else if (carrello.getCarrello().size() > 0) {
                            System.out.println("--(lista prodotti presente, redirect)--");
                            //allora si conclude l'ordine
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/ConfirmOrder.jsp");
                            dispatcher.forward(request, response);
                        }
                    }
                }
            } else{
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
