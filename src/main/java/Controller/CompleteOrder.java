package Controller;

import Model.Ordine;
import Model.OrdineDAO;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CompleteOrder", value = "/CompleteOrder")
public class CompleteOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        Utente user= (Utente) session.getAttribute("utente");
        Ordine carrello=(Ordine) session.getAttribute("carrello");
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
                String via=request.getParameter("via");
                Integer cap=Integer.parseInt(request.getParameter("cap"));
                Integer civico=Integer.parseInt(request.getParameter("civico"));
                carrello.setCivico(civico);
                carrello.setVia(via);
                carrello.setCap(cap);
                OrdineDAO.completeOrdine(carrello);
                //devo scalare la quantita dei presi dai vinili
                carrello=null;
                session.removeAttribute("carrello");
                //session.setAttribute("carrello",carrello);
                RequestDispatcher dispatcher = request.getRequestDispatcher("InitServlet");
                dispatcher.forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
