package Controller;

import Model.Utente;
import Model.UtentiDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "UpdateUser", value = "/UpdateUser")
public class UpdateUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        if(!session.isNew()){
            Utente u= (Utente) session.getAttribute("utente");
            if(u!=null){
                Integer id= null;
                String idS=request.getParameter("id");
                if(idS!=null)
                    id=Integer.valueOf(idS);
                String nome=request.getParameter("nome");
                String cognome=request.getParameter("cognome");
                Date data= Date.valueOf(request.getParameter("date"));
                String via=request.getParameter("via");
                String capS=request.getParameter("cap");
                Integer cap= null;
                if(capS!=null)
                    cap= Integer.valueOf(capS);
                String civicoS=request.getParameter("civico");
                Integer civico= null;
                if (civicoS!=null)
                        civico=Integer.valueOf(civicoS);
                String pass=request.getParameter("pass");
                if(id!=null&&nome!=null&&cognome!=null&&data!=null&&via!=null&&cap!=null&&civico!=null&&pass!=null){
                    Utente newUser = new Utente();
                    newUser.setPassword(pass);
                    if(newUser.getPasswordhash().equals(u.getPasswordhash())) {
                        newUser.setID(id);
                        newUser.setNome(nome);
                        newUser.setCognome(cognome);
                        newUser.setDataNascita(data);
                        newUser.setMail(u.getMail());
                        newUser.setVia(via);
                        newUser.setCivico(civico);
                        newUser.setCap(cap);
                        System.out.println("odl " + u.toString());
                        System.out.println("new " + newUser.toString());
                        if (!newUser.equals(u)) {
                            session.setAttribute("modify",true);
                            UtentiDAO.doUpdate(newUser);//da implementare
                            session.setAttribute("utente",newUser);
                            System.out.println("bene");
                        } else {
                            session.setAttribute("noModify",true);
                            //facciamo stampare messaggio di nessuna modifica
                        }
                    }
                    else{
                        session.setAttribute("noPassCorrect",true);
                        //password errata riprova
                    }
                    dispatcher = request.getRequestDispatcher("/WEB-INF/AreaPersonale.jsp");
                }
                else{
                    //errore parametri
                }
            }
            else{
                dispatcher = request.getRequestDispatcher("/access.jsp");
            }
        }
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}