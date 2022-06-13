package Controller;

import Model.ordine;
import Model.ordineDAO;
import Model.utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "RedirectOrder", value = "/RedirectOrder")
public class RedirectOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        utente user= (utente) session.getAttribute("utente");
        ordine carrello=(ordine) session.getAttribute("carrello");
        if(user==null){
            System.out.println("complete 1");
            session.setAttribute("login",false);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/access.jsp");
            dispatcher.forward(request, response);
        }
        else{
            System.out.println("complete 2");
            if(carrello.getCarrello()==null){
                System.out.println("complete 2.1");
                //non si fa nulla
                RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
            }
            else if(carrello.getCarrello().size()>0) {
                System.out.println("complete 2.2");
                //allora si conclude l'ordine
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/ConfirmOrder.jsp");
                dispatcher.forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
