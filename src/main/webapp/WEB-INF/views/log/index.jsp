<%--
  Created by IntelliJ IDEA.
  User: liuyandong
  Date: 2020/3/30
  Time: 10:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript">
        var start = 0;
        $(function () {
            window.setInterval(function () {
                $.get("../log/obtainLog", {start: start}, function (result) {
                    var data = result["data"];
                    if (data != null) {
                        start += data["length"];
                        $("#log").text($("#log").text() + data["log"]);
                    }
                }, "json");
            }, 500);
        });
    </script>
</head>
<body>
<pre style="font-family: Consolas" id="log"></pre>
</body>
</html>
