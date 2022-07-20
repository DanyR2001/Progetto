<%@ page import="Model.Ordine" %>
<%@ page import="Model.Prodotto" %>
<%@ page import="Model.Utente" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Ordine carrello= (Ordine) session.getAttribute("carrello");
    Utente u= (Utente) session.getAttribute("utente");
%>
<head>
    <title>Conferma Ordine - LP</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/header.css" type="text/css">
    <link rel="stylesheet" href="css/confirmOrder.css" type="text/css">

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
    <a href="Search?String=" class="ricerca"><img src="img/loupe.png" alt="loupe"></a>

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
        <%if(u==null){%>
        <a href="access.jsp"><p>Login</p><img src="img/enter.png" alt="login"></a> <!--se non c'è l'utente appare il login -->
        <%} else {%>
        <a href="Logout"><p>Logout</p><img src="img/logout.png" alt="logout"></a> <!-- se c'è, logout -->
        <%}%>
    </nav>

    </header>
    <main class="confirm-order">
        <div class="box">
            <div class="inner-box">
                <div class="orders">
                    <div class="title-order">
                        <p>Riepilogo Ordine</p>
                    </div>
                    <div class="order-info">

                        <div class="margin">

                            <div class="order-frame">

                                <table>
                                    <%
                                        if(carrello!=null && u!=null){
                                            if(carrello.getCarrello()!=null)
                                                if(carrello.getCarrello().size()>0){
                                    %>
                                    <tr>
                                        <td><h4>Prodotto</h4></td>
                                        <td class="al"><h4>Quantità</h4></td>
                                        <td class="al"><h4>Prezzo</h4></td>
                                        <td class="al"><h4>Subtotale</h4></td>
                                    <tr>
                                    <%
                                                    DecimalFormat dr=new DecimalFormat("0.00");
                                                    for(Prodotto p :carrello.getCarrello()){
                                    %>
                                    <tr>
                                    <td><%=p.getArticolo().getTitolo()%></td>
                                    <td class="quant"><%=p.getQuantita()%></td>
                                    <td class="prezzo"><%=dr.format(p.getPrezzo()/p.getQuantita())%></td>
                                    <td class="subtotale"><%=p.getPrezzo()%></td>
                                    </tr>
                                    <%
                                                    }
                                    %>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td class="al"><h4>Totale</h4></td>
                                        <td class="al"><h4><%=carrello.getPrezzo()%></h4></td>
                                    </tr>
                                </table>

                            </div>

                        </div>

                    </div>

                </div>

                <div class="register-option">
                    <form action="CompleteOrder" >
                        <p>Conferma l'indirizzo di spedizione</p>
                        <div class="input-wrap">
                            <label for="reg-via">Via</label>
                            <input type="text" name="via" id="reg-via" class="input-field" value="<%=u.getVia()%>" required>
                        </div>
                        <div class="input-wrap">
                            <label for="reg-civico">Civico</label>
                            <input type="number" name="civico" id="reg-civico" class="input-field" value="<%=u.getCivico()%>" required>
                        </div>
                        <div class="input-wrap">
                            <label for="reg-cap">CAP</label>
                            <input type="number" name="cap" id="reg-cap" class="input-field" value="<%=u.getCap()%>" required>
                        </div>
                        <div class="buttons">
                            <input type="submit" value="Conferma acquisto" class="button">
                        </div>

                    </form>

                    <%
                                }
                        }
                    %>
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
