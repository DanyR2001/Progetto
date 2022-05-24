<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.net.http.HttpRequest" %>
<%@ page import="Model.*" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 25/03/22
  Time: 19:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%
        int Id =Integer.parseInt(request.getParameter("id"));
        HttpSession snn=request.getSession();
        listaVinili service= (listaVinili) snn.getAttribute("libreria");
        vinile v=service.findVinilieFromId(Id);
        ordine carrello= (ordine) snn.getAttribute("carrello");
        prodotto eq = null;
        utente u = (utente) snn.getAttribute("utente");
        if(u!=null)
            if(u.isAdmin_bool()){
                RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
                dispatcher.forward(request, response);
            }
        if(carrello!=null)
            if(carrello.getCarrello()!=null)
                 eq=carrello.getItem(v);
        //out.print("ciao "+v.getTitolo());
    %>

</head>
<body>
<form action="AddItem">
    <table>
        <tr><td><img src="<% out.print(application.getContextPath()+ v.getUrl() );%>"></td><td><% out.print(v.getTitolo());%></td></tr>
        <tr><td>Quantità:   <input type="number" name="quantita" min="1" value="1" max="<% 
        if (eq!=null)
            out.print(service.getMaxDispId(v.getPK())-eq.getQuantita());
        else{
            out.print(service.getMaxDispId(v.getPK()));
        }
        %>" >
            </td><td>Prezzo: <% out.print(v.getPrezzo());%></td></tr>
        <tr><td>Artista: <% out.print(v.getArtista());%></td><td><input type="submit" value="aggiungi al carrello"></td></tr>
        <%
        if(v.getTags()!=null)
            for(int i=0;i<v.getTags().size();i++)
                out.print("<fieldset><h3>"+v.getTags().get(i).getNome()+" </h3></fieldset>");
        %>
        <input type="hidden" name="id" value="<% out.print(v.getPK());%>">
    </table>
</form>


</body>
</html>
