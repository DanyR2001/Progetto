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
        function myFunction() {
            var x = document.getElementById("snackbar");
            if(x!=null) {
                x.className = "show";
                setTimeout(function () {
                    x.className = x.className.replace("show", "");
                }, 3000);
            }
        }

        $(document).ready(function(){
            myFunction();
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

        function showRegistration() {
            $(".login-wrap").hide();
            $(".register-option").hide();
            $(".registration-wrap").show();
        }

        function  showLogin() {
            $(".registration-wrap").hide();
            $(".login-wrap").show();
            $(".register-option").show();

        }

    </script>

</head>

<body>

<%
Boolean flag= (Boolean) session.getAttribute("noLogOrder");
if(flag!=null)
    if(!flag) {

%>
      <div id="snackbar">Accedi per completare l'ordine.</div>
<%
            session.setAttribute("noLogOrder",null);
    }
    Boolean flag1= (Boolean) session.getAttribute("noLogArea");
    if(flag1!=null)
        if(!flag1) {
%>
            <div id="snackbar">Accedi per visualizzare la tua area personale.</div>
<%
            session.setAttribute("noLogArea",null);
        }
    Boolean flag3= (Boolean) session.getAttribute("invalidMail");
    if(flag3!=null)
        if(flag3) {
%>
        <div id="snackbar">Mail non valida, riprova con altre credenziali.</div>
<%
            session.setAttribute("invalidMail",null);
        }
%>


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
                                <input id="login-mail" name="mail" type="email" class="input-field" required placeholder="Email"/>
                            </div>

                            <div class="input-wrap">
                                <input id="login-pass" name="pass" type="password" class="input-field" required placeholder="Password"/>
                            </div>

                            <input type="submit" value="Accedi" class="sign-in-btn"  />

                            <%
                                Boolean flag2 = (Boolean) session.getAttribute("failLogin");
                                if(flag2 != null)
                                    if(flag2){

                            %>
                            <div class ="err">
                                <span> &#9888; Email o password errate.</span>
                            </div>
                            <%
                                        session.setAttribute("failLogin",null);
                                    }
                            %>
                        </div>
                    </form>
                </div>

                <div class="register-option">

                    <section>
                        <div class="heading2">
                            <h2>Prima volta qui?</h2>
                        </div>

                        <button class="reg-btn" type="button" onclick="showRegistration()">Registrati</button>
                    </section>

                </div>

                <div class="registration-wrap">
                    <form action="RegistrazioneServlet" method="post"  autocomplete="off" class="sign-up-form">

                        <div class="heading">
                            <h2>Nuovo Account</h2>
                        </div>

                        <div class="actual-form">

                            <div class="part1">
                                <div class="input-wrap">
                                    <label for="reg-nome">Nome</label>
                                    <input type="text" pattern="[A-Za-z]{1,}" title="Il nome puo contere solo lettere" id="reg-nome" name="nome" class="input-field" required>
                                </div>
                                <div class="input-wrap">
                                    <label for="reg-cognome">Cognome</label>
                                    <input type="text" pattern="[A-Za-z]{1,}" title="Il cognome puo contere solo lettere" id="reg-cognome" name="cognome" class="input-field" required>
                                </div>
                                <div class="input-wrap">
                                    <label for="reg-date">Data di nascita</label>
                                    <input type="date" id="reg-date" name="date" class="input-field" placeholder="  " onfocus="(this.type='date')" required>
                                </div>
                                <div class="input-wrap">
                                    <label for="reg-email">Email</label>
                                    <input type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" title="Mail non valida" id="reg-email" name="email" class="input-field" required>
                                </div>
                            </div>

                            <div class="part2">
                                <div class="input-wrap">
                                    <label for="reg-pass">Password</label>
                                    <input type="password" pattern=".{8,}" id="reg-pass" title="La password deve essere di almeno 8 caratteri" name="pass" class="input-field" required>
                                </div>
                                <div class="input-wrap">
                                    <label for="reg-via">Via</label>
                                    <input type="text" pattern="[A-Za-z]{1,}" title="La via puo contere solo lettere" name="via" id="reg-via" class="input-field" required>
                                </div>
                                <div class="input-wrap">
                                    <label for="reg-civico">Civico</label>
                                    <input type="number" pattern="[1-9]{1}/d"name="civico" min="1" max="999" title="Il civico puo contere solo numeri" id="reg-civico" class="input-field" required>
                                </div>
                                <div class="input-wrap">
                                    <label for="reg-cap">CAP</label>
                                    <input type="number" pattern="[1-9]{5,5}" min="9999" max="99999" title="Il cap puo contere solo 5 numeri" name="cap" id="reg-cap" class="input-field" required>
                                </div>
                            </div>
                        </div>

                        <div class="submit-wrap">
                            <input type="submit" value="Registrati" class="sign-up-btn" />
                        </div>
                        <div class="sign-in">
                            <span>Hai già un account? <a onclick="showLogin()"> Accedi </a></span>
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