<%@ page import="Model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<%
    int Id = Integer.parseInt(request.getParameter("id"));
    HttpSession snn = request.getSession();
    ListaVinili service = (ListaVinili) snn.getAttribute("libreria");
    Vinile v = service.findViniliFromId(Id);
    Ordine carrello = (Ordine) snn.getAttribute("carrello");
    Prodotto eq = null;
    Utente u = (Utente) snn.getAttribute("utente");
    if(u != null)
        if(u.isAdmin_bool()) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
            dispatcher.forward(request, response);
        }
    if(carrello != null)
        if(carrello.getCarrello() != null)
            eq = carrello.getItem(v);
%>

<head>
    <title><%=v.getArtista()%> - <%=v.getTitolo()%> </title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/header.css" type="text/css"/>

    <script src="./lib/jquery-3.6.0.js"> </script>
    <script>

        $(document).ready(function(){
            $("#search-box").keyup(function(){
                $.ajax({
                    type: "POST", //tipo di richiesta
                    url: "SuggestName", //url a cui inviare la richiesta
                    data:'keyword='+ $(this).val(), //dati passati al server: passa cosa c'è scritto nella barra di ricerca
                    success: function(data){
                        $("#suggestion-box").show(); //rende visibile il div
                        $("#suggestion-box").html(data);
                    }
                });
            });
        });

        $(document).click(function (e) {
            var target=$(e.target);
            if(!target.hasClass("c"))
                $("#suggestion-box").hide();
            else
                $("#suggestion-box").show();
        })

        //To select country name
        function selectSuggest(val) {
            $("#search-box").val(val);
            $("#suggestion-box").hide();
            var id=document.getElementById(val).getAttribute("value");
            window.location.href = "./item.jsp?id="+id;
        }
        function search(){
            var val= document.getElementById("search-box").value;
            if (event.keyCode == 13)
                window.location.href = "./Search?String="+val;
        }

    </script>
</head>
<body>
<header class="header">
    <img src="img/vynil.png" class="vinyl" alt="vinyl">
    <a href="index.jsp" class="logo">LostInTheLoop</a>

    <input type="checkbox" id="checkbox_toggle" />
    <label for="checkbox_toggle" class="hamburger">&#9776;</label>

    <a href="carrello.jsp" id="cart2"><img src="img/shopping-cart.png" alt="cart"><span id="cart-counter2"><%=carrello.getNumItem()%></span></a>
    <a href="Search?String=" class="ricerca"><img src="img/loupe.png"></a>

    <nav class="header-right" id="myLinks">
        <div class = "Search">
            <input type="text" class="c" id="search-box" placeholder="Search.." onkeypress="search()" >
            <div id="suggestion-box" class="c"></div>
        </div>
        <a href="index.jsp" ><p>Home</p><img src="img/home.png" alt="homepage" tooltip="Home"></a>
        <a href="AreaPersonale"><p>Profilo</p><img src="img/user%20(2).png" alt="profile">
            <% if(u!=null) { %>
            <span id="user"><%=u.getNome()%></span>
            <%}%>
        </a>
        <a href="carrello.jsp" id="cart1"><img src="img/shopping-cart.png" alt="cart"><span id="cart-counter1"><%=carrello.getNumItem()%></span></a>
        <%if(u == null){%>
        <a href="access.jsp"><p>Login</p><img src="img/enter.png" alt="login"></a> <!--se non c'è l'utente appare il login -->
        <%} else {%>
        <a href="Logout"><p>Logout</p><img src="img/logout.png" alt="logout"></a> <!-- se c'è, logout -->
        <%}%>
    </nav>

</header>

<main>
    <form action="AddItem">
        <table>
            <tr><td><img src="<% out.print(application.getContextPath()+ v.getUrl() );%>"></td><td><% out.print(v.getTitolo());%></td></tr>
            <tr><td>Quantità:<%
                if (eq!=null){
                    if(service.getMaxDispId(v.getPK())-eq.getQuantita()==0)
                        out.print(" ESAURITO");
                    else
                        out.print("<input type=\"number\" name=\"quantita\" min=\"1\" value=\"1\" max=\""+(service.getMaxDispId(v.getPK())-eq.getQuantita())+"\">");
                }
                else{
                    if(service.getMaxDispId(v.getPK())!=0)
                        out.print("<input type=\"number\" name=\"quantita\" min=\"1\" value=\"1\" max=\""+service.getMaxDispId(v.getPK())+"\">");
                    else
                        out.print(" ESAURITO");
                }
            %>
            </td><td>Prezzo: <% out.print(v.getPrezzo());%></td></tr>
            <tr><td>Artista: <% out.print(v.getArtista());%></td><td><input type="submit" value="aggiungi al carrello"></td></tr>
            <%
                if(v.getTags()!=null)
                    for(int i=0;i<v.getTags().size();i++)
                        out.print("<fieldset><h3>"+v.getTags().get(i).getNome()+" </h3></fieldset>");
            %>
            <input type="hidden" name="id" value="<% out.print(v.getPK());%>">
        </table>
    </form>
</main>
<footer class="footer">
    <div class="footer-info">
        <a href="index.jsp" class="footer-link" >Home</a>
        <%if(u==null){%>
        <a href="access.jsp" class="footer-link" >Login</a>
        <%} else {%>
        <a href="Logout" class="footer-link">Logout </a>
        <%}%>
        <a href="carrello.jsp" class="footer-link">Carrello</a>
        <a href="Search?String=" class="footer-link">Ricerca</a>
    </div>
</footer>



</body>
</html>
