<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gán lớp cho sinh viên</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>

<h1>Gán lớp cho sinh viên: <c:out value="${student.name}"/></h1>

<form action="/student/setclass" method="post">
    <!-- Truyền ID sinh viên -->
    <input type="hidden" name="id" value="<c:out value='${student.id}'/>"/>

    <label for="class_id">Chọn lớp:</label>
    <select name="class_id" id="class_id">
        <c:forEach var="group" items="${listGroup}">
            <option value="${group.id}">${group.name}</option>
        </c:forEach>
    </select>

    <button type="submit">Gán lớp</button>
</form>

</body>
</html>
