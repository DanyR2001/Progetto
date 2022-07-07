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
        Utente u= (Utente) snn.getAttribute("utente");
        System.out.println("1");
        if(u!=null){
            if(u.isAdmin_bool()){
                System.out.println("11");
                listaDisponibiliDAO service=new listaDisponibiliDAO();
                ListaVinili libreria= service.getAll();
                snn.setAttribute("libreria",libreria);
                ArrayList<Tag> lista= tagsDAO.getAll();
                snn.setAttribute("tags",lista);
                RequestDispatcher dispatcher = null;
                String src=request.getParameter("src");
                if(src==null)
                    dispatcher= request.getRequestDispatcher("/WEB-INF/gestione.jsp");
                else{
                    System.out.println("111");
                    if(src.equals("adminVinile")||src.equals("adminTag")){
                        System.out.println("11111");
                        dispatcher=request.getRequestDispatcher("/WEB-INF/"+src+".jsp");
                    }
                    else{
                        dispatcher=request.getRequestDispatcher("/Logout");
                    }
                }
                System.out.println("111111");
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
