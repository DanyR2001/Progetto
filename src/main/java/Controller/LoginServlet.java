package Controller;

import Model.utente;
import Model.utentiDAO;
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
        utente x=new utentiDAO().doRetrieveByUsernamePassword(username,password);
        if (x==null)
            System.out.println("prco");
        request.getSession().setAttribute("utente", x);
        response.sendRedirect(".");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
