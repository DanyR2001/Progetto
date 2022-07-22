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
        String cognome=request.getParameter("cognome");
        String password=request.getParameter("pass");
        String data_s=request.getParameter("date");
        String email=request.getParameter("email");
        String via=request.getParameter("via");
        String cap_s=request.getParameter("cap");
        String citta=request.getParameter("citta");
        if(nome!=null && cognome!=null && password!=null && data_s!=null && email!=null && via!=null && cap_s!=null && citta!=null){
            Integer cap=Integer.parseInt(cap_s);
            Date x= Date.valueOf(data_s);
            String sub[]=via.split(" ");
            Integer civico= Integer.valueOf(sub[sub.length-1]);
            via=via.replace(" "+sub[sub.length-1],"");
            utente.setMail(email);
            utente.setAdmin_bool(false);
            utente.setDataNascita(x);
            utente.setPassword(password);
            utente.setCognome(cognome);
            utente.setNome(nome);
            utente.setVia(via);
            utente.setCivico(civico);
            utente.setCap(cap);
            utente.setCitta(citta);
            boolean result=UtentiDAO.doSave(utente);
            if(result==true) {
                request.getSession().setAttribute("utente", utente);
                response.sendRedirect(".");
            }
            else{
                request.getSession().setAttribute("invalidMail",true);
                response.sendRedirect("./access.jsp");
            }
        }
        else{
            response.sendRedirect("./error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
