<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<td><a href="/group/add">Add</a></td>
<table border="1">
    <tr>
        <th>STT</th>
        <th>Name</th>
        <th>Delete</th>
        <th>Edit</th>
    </tr>
    <c:forEach items="${requestScope.listGroup}" var="group" varStatus="status">
        <tr>
            <td>${status.index + 1}</td>
            <td>
                <c:out value="${group.name}"/>
            </td>
            <td><a href="/group/delete?id=${group.id}">Delete</a></td>
            <td><a href="/group/edit?id=${group.id}">Edit</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
