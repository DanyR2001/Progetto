package Controller;

import Model.listaDisponibiliDAO;
import Model.listaVinili;
import Model.utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "Admin", value = "/Admin")
public class Admin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession();
        utente u= (utente) snn.getAttribute("utente");
        if(u!=null){
            if(u.isAdmin_bool()){
                listaDisponibiliDAO service=new listaDisponibiliDAO();
                listaVinili libreria= service.getAll();
                snn.setAttribute("libreria",libreria);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/admin.jsp");
                dispatcher.forward(request, response);
            }
            else{
                snn.invalidate();
                RequestDispatcher dispatcher = request.getRequestDispatcher("/InitServlet");
                dispatcher.forward(request, response);
            }
        }
        else{
            snn.invalidate();
            RequestDispatcher dispatcher = request.getRequestDispatcher("/InitServlet");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
