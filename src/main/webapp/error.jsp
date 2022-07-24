<%@ page import="Model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<html>

<%
    HttpSession snn = request.getSession();
    Ordine carrello = (Ordine) snn.getAttribute("carrello");
    if(carrello==null){
        carrello=new Ordine();
    }
    Utente u = (Utente) snn.getAttribute("utente");
    if(u != null)
        if(u.isAdmin_bool()) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
            dispatcher.forward(request, response);
        }
%>

<head>
    <title>Error Page - LP</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/header.css" type="text/css"/>
    <link rel="stylesheet" href="css/error.css" type="text/css"/>


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

        //To suggest vinil name
        function selectSuggest(val) {
            var id="#"+val;
            var value=$(id).attr("value");
            $("#search-box").val(value);
            $("#suggestion-box").hide();
            window.location.href = "./item.jsp?id="+val;
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
            <input type="text" class="c" id="search-box" placeholder="Cerca.." onkeypress="search()" >
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

<main class="cart-page">
    <div class="box">
        <div class="inner-box">
            <div class="err-desc">
                <div class="err-wrap">
                    <div class="err-text">
                        <h1>Ops... <br> Qualcosa è andato storto</h1>
                        <h5>Codice errore <%=response.getStatus()%>, vai <a href="./InitServlet">QUI</a></h5>
                    </div>
                    <img src="img/error.jpeg" class="image" alt="error">
                </div>

            </div>
        </div>
    </div>
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
