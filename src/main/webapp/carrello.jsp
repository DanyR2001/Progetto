<%@ page import="Model.ordine" %>
<%@ page import="Model.prodotto" %>
<%@ page import="Model.listaVinili" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.vinile" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 06/05/22
  Time: 00:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<table>
<%
    HttpSession snn=request.getSession();
    ordine carrello= (ordine) snn.getAttribute("carrello");
    listaVinili service=(listaVinili) snn.getAttribute("lista");
    ArrayList<vinile> listaRimossi= (ArrayList<vinile>) snn.getAttribute("removedVinil");
   if(listaRimossi!=null){
       out.print("<fieldset>");
       for(vinile v :listaRimossi)
           out.print("<h1>"+v.getTitolo()+" di "+v.getArtista());
       out.print("</fieldset>");
       snn.setAttribute("removedVinil",null);
   }
    for(int i=0;i<carrello.getNumItem();i++){
        prodotto p=carrello.getCarrello().get(i);
%>
    <form action="UpdateCarrello">
        <input type="hidden" name="index" value="<%out.print(i);%>">
        <%System.out.println("ciao "+p.getQuantita()+" cazzo "+service.numDispVinil(p.getArticolo()));%>
    <tr><td><%out.print("<h2>"+p.getArticolo().getTitolo()+"</h2>");%></td><td><%out.print("Quantita: <input type='number' name='quantita' min='0' max='"+(p.getQuantita()+service.numDispVinil(p.getArticolo()))+"' value='"+p.getQuantita()+"'>");%></td><td><%out.print(p.getPrezzo());%></td><td><input type="submit" value="applica modifiche"></td></tr>
    </form>
    <%}%>
</table>
</body>
</html>
