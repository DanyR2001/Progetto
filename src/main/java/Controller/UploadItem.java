package Controller;

import Model.ListaDisponibiliDAO;
import Model.ListaVinili;
import Model.Utente;
import Model.Vinile;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UploadItem", value = "/UploadItem")
public class UploadItem extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession();
        Utente u= (Utente) snn.getAttribute("utente");
        if(u!=null){
            ListaVinili lib= (ListaVinili) snn.getAttribute("libreria");
            if(lib!=null){
                int Index=Integer.parseInt(request.getParameter("index"));
                Vinile v=lib.get(Index);
                int Quantita= Integer.parseInt(request.getParameter("quantita"));
                double Prezzo=Double.parseDouble(request.getParameter("prezzo"));
                String Artista= request.getParameter("nameArtist");
                ListaDisponibiliDAO service=new ListaDisponibiliDAO();
                v.setPrezzo(Prezzo);
                v.setArtista(Artista);
                lib.setQuantitaVin(v,Quantita);
                service.changeById(v,Quantita);
                System.out.println("aiuto");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin?src=adminVinile");
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
