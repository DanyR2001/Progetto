<%@ page import="Model.Ordine" %>
<%@ page import="Model.Utente" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<%
    HttpSession snn = request.getSession();
    Ordine carrello = (Ordine) session.getAttribute("carrello");
    Utente u = (Utente) snn.getAttribute("utente");
    if(u!=null)
        if(u.isAdmin_bool()){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
            dispatcher.forward(request, response);
        }
%>
<head>
    <title>Login - LP</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/header.css" type="text/css"/>
    <link rel="stylesheet" href="css/homepage.css" type="text/css"/>
    <link rel="stylesheet" href="css/login.css" type="text/css"/>

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

<%
Boolean flag= (Boolean) session.getAttribute("noLogOrder");
if(flag!=null)
    if(flag==false) {
        out.print("<h1>Devi aver fatto l'acceso per copletare l'ordine, accedi o registrati.</h1>");
        session.setAttribute("noLogOrder",null);
    }
    Boolean flag1= (Boolean) session.getAttribute("noLogArea");
    if(flag1!=null)
        if(flag1==false) {
            out.print("<h1>Devi aver fatto l'acceso per poter accedere all'area personale, accedi o registrati.</h1>");
            session.setAttribute("noLogArea",null);
        }
Boolean flag2= (Boolean) session.getAttribute("failLogin");
    if(flag2!=null)
        if(flag2){
            out.print("<h1>Credenziali errate, riprovare</h1>");
            session.setAttribute("failLogin",null);
        }
%>

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
        <%if(u==null){%>
        <a href="access.jsp"><p>Login</p><img src="img/enter.png" alt="login"></a> <!--se non c'è l'utente appare il login -->
        <%} else {%>
        <a href="Logout"><p>Logout</p><img src="img/logout.png" alt="logout"></a> <!-- se c'è, logout -->
        <%}%>
    </nav>

</header>

    <main class="login">
        <div class="box">
            <div class="inner-box">

                <div class="login-wrap" >
                    <form action="LoginServlet" method="post" class="sign-in-form" autocomplete="off">

                        <div class="heading1">
                            <h2>Login</h2>
                        </div>

                        <div class="login-form">
                            <div class="input-wrap">
                                <input id="login-mail" name="mail" type="text" class="input-field" required placeholder="Email"/>
                            </div>

                            <div class="input-wrap">
                                <input id="login-pass" name="pass" type="password" class="input-field" required placeholder="Password"/>
                            </div>

                            <input type="submit" value="Sign In" class="sign-btn"  />
                        </div>
                    </form>
                </div>

                <div class="register-wrap">

                    <section>
                        <div class="heading2">
                            <h2>Not registered yet?</h2>
                        </div>

                        <button class="reg-btn" type="button">Sign Up</button>
                    </section>


                    <form action="RegistrazioneServlet" method="post"  autocomplete="off" class="sign-up-form">

                        <div class="heading">
                            <h2>Sign Up</h2>
                        </div>

                        <div class="actual-form">

                            <div class="input-wrap">
                                <input type="text" id="reg-nome" name="nome" class="input-field" required>
                                <label for="reg-nome">Nome</label>
                            </div>
                            <div class="input-wrap">
                                <input type="text" id="reg-cognome" name="cognome" class="input-field" required>
                                <label for="reg-cognome">Cognome</label>
                            </div>
                            <div class="input-wrap">
                                <input type="date" id="reg-date"  name="date" class="input-field" required>
                                <label for="reg-date">Data di nascita</label>
                            </div>
                            <div class="input-wrap">
                                <input type="email" id="reg-email" name="email" class="input-field" required>
                                <label for="reg-email">Email</label>
                            </div>
                            <div class="input-wrap">
                                <input type="password" id="reg-pass" name="pass" class="input-field" required>
                                <label for="reg-pass">Password</label>
                            </div>
                            <div class="input-wrap">
                                <input type="text" name="via" id="reg-via" class="input-field" required>
                                <label for="reg-via">Via</label>
                            </div>
                            <div class="input-wrap">
                                <input type="number" name="civico" id="reg-civico" class="input-field" required>
                                <label for="reg-civico">Civico</label>
                            </div>
                            <div class="input-wrap">
                                <input type="number" name="cap" id="reg-cap" class="input-field" required>
                                <label for="reg-cap">CAP</label>
                            </div>

                            <input type="submit" value="Registrati" class="sign-btn"  />
                        </div>
                    </form>
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