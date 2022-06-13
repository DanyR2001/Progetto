<%@ page import="Model.ordine" %>
<%@ page import="Model.prodotto" %>
<%@ page import="Model.utente" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 13/06/22
  Time: 11:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<table style="border: 1px black">
    <%
    ordine carrello= (ordine) session.getAttribute("carrello");
    utente u= (utente) session.getAttribute("utente");
    if(carrello!=null&&u!=null){
        if(carrello.getCarrello()!=null)
            if(carrello.getCarrello().size()>0){
                for(prodotto p :carrello.getCarrello()){
                %>
                <tr><td><%=p.getArticolo().getTitolo()%></td><td><%=p.getQuantita()%></td><td><%=p.getPrezzo()%></td></tr>
           <% }
                %>
            <form action="CompleteOrder" >
                <table>
                    <tr><th colspan="2">Conferma l'indirizzo di spedizione:</th></tr>
                    <tr><td>Via:</td><td><input type="text" value="<%= u.getVia()%>" name="via" id="via" required /></td></tr>
                    <tr><td>Civico:</td><td><input type="number" value="<%= u.getCivico()%>" name="civico" required /> </td></tr>
                    <tr><td>Cap:</td><td><input type="number" value="<%= u.getCap()%>" name="cap" required /></td></tr>
                    <tr><td colspan="2"><input type="submit"></td> </tr>
                </table>
            </form>
    <%
           }
    }
    %>
</table>
</body>
</html>
