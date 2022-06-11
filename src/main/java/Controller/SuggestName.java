package Controller;

import Model.listaVinili;
import Model.vinile;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SuggestName", value = "/SuggestName")
public class SuggestName extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword=request.getParameter("keyword");
        if(keyword!=null){
            PrintWriter out = response.getWriter();
            HttpSession snn=request.getSession();
            out.println("<ul id='country-list'>");
            if(snn!=null){
                listaVinili libreria= (listaVinili) snn.getAttribute("libreria");
                if(libreria!=null){
                    List<vinile> lib=libreria.getTitleContein(keyword);
                    if(lib!=null)
                        for(vinile v: lib){
                            out.println("<li value=\""+v.getPK()+"\" id=\""+v.getTitolo()+"\" onClick='selectSuggest(\""+v.getTitolo()+"\");'>"+v.getTitolo()+"</li>");
                        }
                    /*else
                        out.println("<li >Nessuna corrispondenza</li>");*/
                }
            }
            out.println("</ul>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
