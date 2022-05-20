<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="Model.ordine" %>
<%@ page import="Model.vinile" %>
<%@ page import="Model.listaVinili" %>
<%@ page import="java.net.http.HttpRequest" %><%--
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
        listaVinili service= (listaVinili) snn.getAttribute("lista");
        vinile v=service.findVinilieFromId(Id);
        //out.print("ciao "+v.getTitolo());
    %>

</head>
<body>
<form action="AddItem">
    <table>
        <tr><td><img src="<% out.print(v.getUrl());%>"></td><td><% out.print(v.getTitolo());%></td></tr>
        <tr><td>Quantità:   <input type="number" name="quantita" min="1" value="1" max="<% out.print(v.getQuantita());%>" >
            </td><td>Prezzo: <% out.print(v.getPrezzo());%></td></tr>
        <tr><td>Artista: <% out.print(v.getArtista());%></td><td><input type="submit" value="aggiungi al carrello"></td></tr>
        <input type="hidden" name="id" value="<% out.print(v.getPK());%>">
    </table>
</form>


</body>
</html>
