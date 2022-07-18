package Controller;

import Model.Utente;
import Model.UtentiDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "RegistrazioneServlet", value = "/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utente utente = new Utente();
        String nome=request.getParameter("nome");
        utente.setNome(nome);
        String cognome=request.getParameter("cognome");
        utente.setCognome(cognome);
        String password=request.getParameter("pass");
        utente.setPassword(password);
        Date x= Date.valueOf(request.getParameter("date"));
        utente.setDataNascita(x);
        String email=request.getParameter("email");
        utente.setMail(email);
        utente.setAdmin_bool(false);
        String via=request.getParameter("via");
        utente.setVia(via);
        Integer cap=Integer.parseInt(request.getParameter("cap"));
        utente.setCap(cap);
        Integer civico=Integer.parseInt(request.getParameter("civico"));
        utente.setCivico(civico);
        UtentiDAO.doSave(utente);
        request.getSession().setAttribute("utente", utente);
        response.sendRedirect(".");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
