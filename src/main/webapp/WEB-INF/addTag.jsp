<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.tag" %>
<%@ page import="Model.vinile" %>
<%@ page import="Model.listaVinili" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 14/06/22
  Time: 13:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="AddTag">
    <table>
        <tr><td>Inserisci il nome del tag</td><td><input type="text" name="Nome" required></td>
        <tr><td colspan="2" >seleziona il vinile al quale lo vuoi collegare:</td></tr>
        <%
           listaVinili vin= (listaVinili) session.getAttribute("libreria");
           if(vin != null) {
               ArrayList<vinile> list = vin.getAllVinil();
               if (list != null) {
                   for (int i=0;i<list.size();i++){
                       out.print("<tr><td colspan='2'>");
                       out.print("<input type='checkbox' id='"+list.get(i).getPK()+"' name='tag' value='" + list.get(i).getPK() + "'>");
                       out.print("<lable for ='"+list.get(i).getPK()+"'>"+list.get(i).getTitolo()+"</lable>");
                       out.print("</tr></td>");
                   }
               }
           }
        %>
        <tr><td colspan="2"><input type="submit" value="invia"></td></tr>
    </table>
</form>
</body>
</html>
