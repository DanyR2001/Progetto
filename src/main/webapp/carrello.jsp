<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<%
    HttpSession snn=request.getSession();
    Ordine carrello= (Ordine) snn.getAttribute("carrello");
    Utente user= (Utente) snn.getAttribute("utente");
    ListaVinili service=(ListaVinili) snn.getAttribute("libreria");
    if(user!=null)
        if(user.isAdmin_bool()){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
            dispatcher.forward(request, response);
        }
%>
<head>
    <title>Carrello - LP</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/header.css" type="text/css"/>

    <style>
        table, th, tr {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
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
            <% if(user!=null) { %>
            <span id="user"><%=user.getNome()%></span>
            <%}%>
        </a>
        <a href="carrello.jsp" id="cart1"><img src="img/shopping-cart.png" alt="cart"><span id="cart-counter1"><%=carrello.getNumItem()%></span></a>
        <%if(user==null){%>
        <a href="access.jsp"><p>Login</p><img src="img/enter.png" alt="login"></a> <!--se non c'è l'utente appare il login -->
        <%} else {%>
        <a href="Logout"><p>Logout</p><img src="img/logout.png" alt="logout"></a> <!-- se c'è, logout -->
        <%}%>
    </nav>

</header>

    <main>
        <table>
            <%

                ArrayList<Vinile> listaRimossi= (ArrayList<Vinile>) snn.getAttribute("removedVinil");
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
    </main>

    <footer class="footer">
        <div class="footer-info">
            <a href="index.jsp" class="footer-link" >Home</a>
            <%if(user==null){%>
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
