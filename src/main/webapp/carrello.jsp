<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.*" %><%--
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
    <style>
        table, th, tr {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>
<table>
<%
    HttpSession snn=request.getSession();
    Ordine carrello= (Ordine) snn.getAttribute("carrello");
    Utente user= (Utente) snn.getAttribute("utente");
    ListaVinili service=(ListaVinili) snn.getAttribute("libreria");
    ArrayList<Vinile> listaRimossi= (ArrayList<Vinile>) snn.getAttribute("removedVinil");
    if(user!=null)
        if(user.isAdmin_bool()){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
            dispatcher.forward(request, response);
        }
   if(listaRimossi!=null){
       out.print("<fieldset>");
       for(Vinile v :listaRimossi)
           out.print("<h1>"+v.getTitolo()+" di "+v.getArtista());
       out.print("</fieldset>");
       snn.setAttribute("removedVinil",null);
   }
   if(carrello.getNumItem()>=1){
    for(int i=0;i<carrello.getNumItem();i++){
        Prodotto p=carrello.getCarrello().get(i);
%>
    <form action="UpdateCarrello">
        <input type="hidden" name="index" value="<%out.print(i);%>">
        <%System.out.println("ciao "+p.getQuantita()+" cazzo ");%>
    <tr><td><%out.print("<h2>"+p.getArticolo().getTitolo()+"</h2>");%></td><td><%out.print("Quantita: <input type='number' name='quantita' min='0' max='"+(service.getQuantitaVin(p.getArticolo()))+"' value='"+p.getQuantita()+"'>");%></td><td><%out.print(p.getPrezzo());%></td><td><input type="submit" value="applica modifiche"></td></tr>
    </form>
    <%}
    %>
    <tr><td colspan="2"><h1>Totale</h1></td><td colspan="2"><h1><%=carrello.getPrezzo()%></h1></td></tr>
    <%
    }
    %>
</table>
<%
if(carrello.getCarrello().size()>0){
    out.print("<form action='RedirectOrder'>");
    out.print("<input type='submit' value='Completa ordine'>");
    out.print("</form>");
    }
else{
    out.print("<h1>Carrello vuoto</h1>");
}

%>
</body>
</html>
