package Controller;

import Model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "UpdateCarrello", value = "/UpdateCarrello")
public class UpdateCarrello extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int index= Integer.parseInt( request.getParameter("index"));
        int quantita= Integer.parseInt( request.getParameter("quantita"));
        HttpSession snn=request.getSession();
        listaVinili service= (listaVinili) snn.getAttribute("lista");
        ordine carrello= (ordine) snn.getAttribute("carrello");
        vinile v=carrello.getCarrello().get(index).getArticolo();
        int actual=carrello.getCarrello().get(index).getQuantita();
        int remain=service.numDispVinil(v);
        int total=remain+actual;
        int newremain=total-quantita;
        System.out.println(" item "+v.getTitolo()+" act order "+actual+" remin list "+remain+" total "+total+" quanita form "+quantita+" nuovi riamente"+newremain );
        carrello.getCarrello().get(index).setQuantita(quantita);
        carrello.refreshCost();
        service.setDisponibili(v,newremain);
        carrello.check();
        utente u= (utente) snn.getAttribute("utente");
        if(u!=null){
            ArrayList<vinile> lista=ordineDAO.uploadOrdine(u,carrello,snn);
            System.out.println(" qunr carrelo "+carrello.getPrezzo());
            snn.setAttribute("removedVinil",lista);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
