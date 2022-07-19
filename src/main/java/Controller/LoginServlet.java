package Controller;

import Model.Utente;
import Model.UtentiDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=request.getParameter("mail");
        String password=request.getParameter("pass");
        Utente x= new UtentiDAO().doRetrieveByUsernamePassword(username,password);
        HttpSession snn=request.getSession();
        String path="";
        if (x == null) {
            //se l'utente non esiste
            snn.setAttribute("failLogin",true);
            path+="/access.jsp";
        }
        else {
            snn.setAttribute("utente", x);
        }
        response.sendRedirect("."+path);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
