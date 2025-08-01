<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add class</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<form action="${pageContext.request.contextPath}/group/add" method="post">
    <table>
        <tr>
            <td>Name</td>
            <td><input type="text" name="name"></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <button type="submit">Save</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
