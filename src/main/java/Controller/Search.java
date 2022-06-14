package Controller;

import Model.listaVinili;
import Model.tag;
import Model.tagsDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "Search", value = "/Search")
public class Search extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cont=request.getParameter("String");
        HttpSession snn= request.getSession();
        listaVinili libreria= (listaVinili) snn.getAttribute("libreria");
        if(cont!=null){
            if(libreria!=null) {
                List vin;
                if(cont.length()>=1)
                    vin = libreria.getTitleContein(cont);
                else
                    vin = libreria.getAvableVinil().getAllVinil();
                snn.setAttribute("listaResult",vin);
                snn.setAttribute("String",cont);
                List<tag> lista= tagsDAO.getAll();
                snn.setAttribute("tags",lista);
                String val=request.getParameter("String");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/search.jsp");
                dispatcher.forward(request, response);
            }
        }
        else{
            String radio=request.getParameter("choose");
            List resp = null;
            if(radio.equals("Testo")){
                String testo=request.getParameter("search");
                if(testo!=null) {
                    if (testo.length() >= 1)
                        resp = libreria.getTitleContein(testo);
                    else
                        resp = libreria.getAvableVinil().getAllVinil();
                    snn.setAttribute("String", testo);
                }
            }
            else if(radio.equals("Tag")){
                String[] selected = request.getParameterValues("cheackbox");
                resp=libreria.getListFromTag(selected);
                snn.setAttribute("String","");
            }
            snn.setAttribute("listaResult",resp);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/search.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
