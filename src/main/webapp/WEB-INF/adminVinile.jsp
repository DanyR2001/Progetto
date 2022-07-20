<%@ page import="Model.ListaVinili" %>
<%@ page import="Model.Vinile" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 24/05/22
  Time: 10:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="css/iframe.css" type="text/css"/>
</head>
<body>
<div class="allign">
    <a href="RedirectTo?src=Vinile" ><img src="img/add.png" class="image"><p>Aggiungi un nuovo vinile</p></a>
</div>
<table class="vin">
    <tr>
        <td class="al"><h2>Titolo:</h2></td>
        <td class="al"><h2>Quanità:</h2></td>
        <td class="al"><h2>Prezzo:</h2></td>
        <td class="al"><h2>Artista</h2></td>
        <td></td>
    </tr>
<%
ListaVinili lib= (ListaVinili) session.getAttribute("libreria");
if(lib!=null)
    for(int i = 0; i<lib.getAllVinili().size(); i++){
        Vinile v=lib.get(i);
        %>
        <form action="UploadItem">
        <input type="hidden" name="index" value="<%=i%>">
            <tr>
                <td><h2><%=v.getTitolo()%></h2></td>
                <td><input type="number" name="quantita"  min="0" value="<%=lib.getQuantitaVin(v)%>" ></td>
                <td><input type="number" step=".01" name="prezzo" value="<%=v.getPrezzo()%>"></td>
                <td><input type="text" name="nameArtist" value="<%=v.getArtista()%>"></td>
                <td><input type="submit" value="Applica"></td>
            </tr>
        </form>
   <% }
%>
</table>
</body>
</html>
