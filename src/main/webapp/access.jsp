<%@ page import="javax.swing.text.StyledEditorKit" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 06/05/22
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet"
          href="./css/styles.css"
          type="text/css"/>
</head>

<%
Boolean flag= (Boolean) session.getAttribute("login");
if(flag!=null)
    if(flag==false) {
        out.print("<h1>Devi aver fatto l'acceso per copletare l'ordine, accedi o registrati.</h1>");
        session.setAttribute("login",null);
    }
%>
<!--  ciao -->
<body>
<form action="LoginServlet" method="post">
    <h2>Login:</h2>
    <table class="tab">
        <tr><th>Compila i campi:</th><th></th></tr>
        <tr><td>Mail:</td><td><input type="text" name="mail"></td></tr>
        <tr><td>Password:</td><td><input type="password" name="pass"></td></tr>
        <tr><td colspan="2" ><input type="submit" class="button"></td></tr>
    </table>
</form>
<form action="RegistrazioneServlet" method="post">
    <H2>Registrazione:</H2>
    <!--
    nome varchar(30) not null,
	cognome varchar(30) not null,
	mail varchar(30) not null,
    passwrd varchar(30)not null,
    dat
    -->
    <table class="tab">
        <tr><th>Compila i campi:</th><th></th></tr>
        <tr><td>Nome:</td><td><input type="text" name="nome"></td></tr>
        <tr><td>Cognome:</td><td><input type="text" name="cognome"></td></tr>
        <tr><td>Data di nasciata:</td><td><input type="date" name="date"></td></tr>
        <tr><td>Email:</td><td><input type="email" name="email"></td></tr>
        <tr><td>Password:</td><td><input type="password" name="pass"></td></tr>
        <tr><td colspan="2" ><input type="submit"  class="button"></td></tr>
    </table>
</form>
</body>
</html>