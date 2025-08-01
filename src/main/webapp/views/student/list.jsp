<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Student List</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<h1>FPT Academy</h1>
<h2>List Student</h2>
<form action="/student/search" method="get">
    <input type="text" name="keyword" placeholder="Search by name or email">
    <button type="submit">Search</button>
</form>

<a href="/student/add" class="add-button">+ Add Student</a>
<a href="/subject/add" class="add-button">+ Add Subject</a>
<table border="1">
    <tr>
        <th>STT</th>
        <th>Name</th>
        <th>Gender</th>
        <th>Email</th>
        <th>Phone</th>
        <th>Group</th>
        <th>Subjects</th>
        <th>Class Chosse</th>
        <th>Assign Subject</th>
        <th>DELETE</th>
        <th>EDIT</th>
    </tr>
    <c:forEach items="${listStudent}" var="student" varStatus="status">
        <tr>
            <td>${status.index + 1}</td>
            <td>${student.name}</td>
            <c:if test="${student.gender == 1}">
                <td><c:out value="Male"/></td>
            </c:if>
            <c:if test="${student.gender == 0}">
                <td><c:out value="Famale"/></td>
            </c:if>
            <td>${student.email}</td>
            <td>${student.phone}</td>
            <td>${student.getGroups().getName()}</td>
            <td>${student.subjectNames}</td>
            <td>
                <c:choose>
                    <c:when test="${student.groups.name == 'Chưa có lớp'}">
                        <a href="${pageContext.request.contextPath}/student/setclass?id=${student.id}">Gán lớp</a>

                    </c:when>
                    <c:otherwise>
                        Đã có lớp
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/student/assign-subject?id=${student.id}">Assign</a>
            </td>

            <td><a href="/student/delete?id=${student.id}">Delete</a></td>
            <td><a href="/student/edit?id=${student.id}">Edit</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
