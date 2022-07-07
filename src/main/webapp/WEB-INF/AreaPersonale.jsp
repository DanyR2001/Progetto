<%@ page import="Model.oldOrder" %>
<%@ page import="Model.Ordine" %>
<%@ page import="Model.Prodotto" %><%--
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
    table, th, tr {
      border: 1px solid black;
      border-collapse: collapse;
    }
    .riga{
      border: 1px solid black;
      border-collapse: collapse;
    }
  </style>
</head>
<body>
  <%
    oldOrder list= (oldOrder) session.getAttribute("OldOrdini");
    if(list!=null){
      for(Ordine x :list.getList()){
        out.print("<fieldset>");
        out.print("<h3>Codice ordine:"+x.getCodice()+" <br>Prezzo:"+x.getPrezzo()+" <br>Data evasione:"+x.getDataEvasione()+" <br>Num Elementi Carrello"+x.getNumItem()+"<br> Indirizzo di spedizione:"+x.getVia()+" "+x.getCivico()+" "+x.getCap()+"</h3>");
        out.print("<caption>\n" +
                "<p>Elementi ordine:</p>\n" +
                "</caption>");
        out.print("<table >");
        out.print("<tr><th>Quantita</th><th>Prezzo</th><th>Titolo</th><tr>");
        for(Prodotto p: x.getCarrello())
          out.print("<tr><td class='riga'>"+p.getArticolo().getTitolo()+"</td><td class='riga'>"+p.getQuantita()+"</td><td class='riga'>"+p.getPrezzo()+"</td>"+"</tr>");
        out.print("<tr><td colspan='2'><h2>Totale:</h2></td><td>"+x.getPrezzo()+"</td></tr>");
        out.print("</table>");
        out.print("</fieldset>");
       }
    }
  %>
</body>
</html>
