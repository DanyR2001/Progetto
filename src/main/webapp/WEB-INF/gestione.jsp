<%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 14/06/22
  Time: 10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="css/admin.css" type="text/css"/>
    <script src="./lib/jquery-3.6.0.js"> </script>

    <title>Admin page - LP</title>
    <script>
        function change(i){
            if(i==0){
                $("#1").removeClass("tablink");
                $("#1").addClass("tablink-selected");
                $("#2").addClass("tablink");
                $("#2").removeClass("tablink-selected");
                document.getElementById("iframe").src="Admin?src=adminVinile";
            }
            else{
                $("#2").removeClass("tablink");
                $("#2").addClass("tablink-selected");
                $("#1").addClass("tablink");
                $("#1").removeClass("tablink-selected");
                document.getElementById("iframe").src="Admin?src=adminTag";
            }
        }
    </script>
</head>

<body onload="change(0)">
    <div class="main">
        <div class="fix">
            <button class="tablink" id="1" onclick="change(0)">Gestisci Vinili</button><button id="2" class="tablink" onclick="change(1)">Gestisci TAG</button><button class="tablink" onclick="location.href = './Logout';">Logout</button>
        </div>
        <div class="corpo">
            <iframe src="" id="iframe"></iframe>
        </div>
    </div>
</body>
</html>
