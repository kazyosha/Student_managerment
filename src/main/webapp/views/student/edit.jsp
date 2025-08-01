<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="student" value="${requestScope.studentEdit}"/>
<c:set var="listGroup" value="${requestScope.listGroup}"/>
<html>
<head>
    <title>Edit student</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<h2>Add new student</h2>
<form action="/student/edit?id=<c:out value="${student.id}"/>" method="post">
    <table>
        <tr>
            <td>
                Name:
            </td>
            <td>
                <input type="text" name="name" value="<c:out value="${student.name}"/>">
            </td>
        </tr>
        <tr>
            <td>
                Gender:
            </td>
            <td>
                <input type="radio" name="gender" <c:if test="${student.gender == 1}"> checked </c:if>value="1"> Male
                <input type="radio" name="gender"
                <c:if test="${student.gender == 0}"> checked </c:if> value="0"> Female
            </td>
        </tr>
        <tr>
            <td>Email</td>
            <td>
                <input type="text" name="email" value="<c:out value="${student.email}"/>">
            </td>
        </tr>
        <tr>
            <td>Phone</td>
            <td>
                <input type="text" name="phone" value="<c:out value="${student.phone}"/>">
            </td>
        </tr>
        <tr>
            <td>
                Group
            </td>
            <td>
                <select name="class_id">
                    <c:forEach var="group" items="${listGroup}">
                        <option
                                <c:if test="${student.getGroups().getId() == group.getId()}">selected</c:if>
                                value="<c:out value="${group.getId()}"/>"><c:out value="${group.getName()}"/></option>
                    </c:forEach>
                </select>
            </td>
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
</form>
</body>
</html>
