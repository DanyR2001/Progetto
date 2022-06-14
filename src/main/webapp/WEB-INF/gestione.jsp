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
    <title>Title</title>
    <style>
        iframe{
            width: 100%;
            height: 80%;
        }
    </style>
    <script>
        function change(i){
            if(i==0){
                document.getElementById("iframe").src="Admin?src=adminVinile";
            }
            else{
                document.getElementById("iframe").src="Admin?src=adminTag";
            }
        }
    </script>
</head>

<body onload="change(0)">
<table>
    <tr><td><button onclick="change(0)">Gestisci Vinili</button> </td><td><button onclick="change(1)">Gestisci TAG</button> </td><td><button onclick="location.href = './Logout';">Logout</button> </td></tr>
</table>
<iframe src="" id="iframe"></iframe>
</body>
</html>
