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
    <title>Title</title>

</head>
<body>
<a href="RedirectTo?src=Vinile" >Aggiungi un nuovo vinile</a>
<%
ListaVinili lib= (ListaVinili) session.getAttribute("libreria");
if(lib!=null)
    for(int i = 0; i<lib.getAllVinili().size(); i++){
        Vinile v=lib.get(i);
        %>
        <form action="UploadItem">
        <input type="hidden" name="index" value="<%out.print(i);%>">
        <tr><td><%out.print("<h2>"+v.getTitolo()+"</h2>");%></td><td><%out.print("Quantita: <input type='number' name='quantita'  min='0' value='"+(lib.getQuantitaVin(v))+"' >");%></td><td><%out.print("Prezzo:<input type='number' step='.01' name='prezzo' value='"+v.getPrezzo()+"'>");%></td><td><input type="submit" value="applica modifiche"></td></tr>
</form>
   <% }
%>

</body>
</html>
