package Controller;

import Model.listaDisponibiliDAO;
import Model.listaVinili;
import Model.utente;
import Model.vinile;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UploadItem", value = "/UploadItem")
public class UploadItem extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession();
        utente u= (utente) snn.getAttribute("utente");
        if(u!=null){
            listaVinili lib= (listaVinili) snn.getAttribute("libreria");
            if(lib!=null){
                int Index=Integer.parseInt(request.getParameter("index"));
                vinile v=lib.get(Index);
                int Quantita= Integer.parseInt(request.getParameter("quantita"));
                double Prezzo=Double.parseDouble(request.getParameter("prezzo"));
                listaDisponibiliDAO service=new listaDisponibiliDAO();
                v.setPrezzo(Prezzo);
                lib.setQuantitaVin(v,Quantita);
                service.changeById(v,Quantita);
                System.out.println("aiuto");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
                dispatcher.forward(request, response);
            }
            else{
                System.out.println("ciao");
            }
        }else{
            System.out.println("ciao 1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
