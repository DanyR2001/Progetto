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
                String cap_s=request.getParameter("cap");
                String citta=request.getParameter("citta");
                if(via!=null&&cap_s!=null&&citta!=null){
                    Integer cap=Integer.parseInt(cap_s);
                    String sub[]=via.split(" ");
                    Integer civico=Integer.parseInt(sub[sub.length-1]);
                    via=via.replace(" "+sub[sub.length-1],"");
                    carrello.setCivico(civico);
                    carrello.setVia(via);
                    carrello.setCap(cap);
                    carrello.setCitta(citta);
                    OrdineDAO.completeOrdine(carrello);
                    session.removeAttribute("carrello");
                }
                //devo scalare la quantita dei presi dai vinili
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
