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
</head>
<body>
  <%
    oldOrder list= (oldOrder) session.getAttribute("OldOrdini");
    if(list!=null){
      for(ordine x :list.getList()){
        out.print("<fieldset>");
        out.print("<h3>"+x.getCodice()+" "+x.getPrezzo()+" "+x.getDataEvasione()+" "+x.getNumItem()+"</h3>");
        out.print("<table>");
        for(prodotto p: x.getCarrello())
          out.print("<tr><td>"+p.getQuantita()+"</td><td>"+p.getPrezzo()+"</td><td>"+p.getArticolo().getTitolo()+"</td></tr>");
        out.print("</table>");
        out.print("</fieldset>");
       }
    }
  %>
</body>
</html>
