<%@ page import="Model.oldOrder" %>
<%@ page import="Model.ordine" %>
<%@ page import="Model.prodotto" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 27/05/22
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
  <style>
    table, th, td {
      border: 1px solid black;
      border-collapse: collapse;
    }
  </style>
</head>
<body>
  <%
    oldOrder list= (oldOrder) session.getAttribute("OldOrdini");
    if(list!=null){
      for(ordine x :list.getList()){
        out.print("<fieldset>");
        out.print("<h3>Codice ordine:"+x.getCodice()+" <br>Prezzo:"+x.getPrezzo()+" <br>Data evasione:"+x.getDataEvasione()+" <br>Num Elementi Carrello"+x.getNumItem()+"<br> Indirizzo di spedizione:"+x.getVia()+" "+x.getCivico()+" "+x.getCap()+"</h3>");
        out.print("<caption>\n" +
                "<p>Elementi ordine:</p>\n" +
                "</caption>");
        out.print("<table >");
        out.print("<tr><th>Quantita</th><th>Prezzo</th><th>Titolo</th><tr>");
        for(prodotto p: x.getCarrello())
          out.print("<tr><td>"+p.getQuantita()+"</td><td>"+p.getPrezzo()+"</td><td>"+p.getArticolo().getTitolo()+"</td>"+"</tr>");
        out.print("</table>");
        out.print("</fieldset>");
       }
    }
  %>
</body>
</html>
