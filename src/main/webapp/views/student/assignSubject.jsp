<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Assign Subjects</title></head>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<body>
<h2>Assign Subjects for Student: ${student.name}</h2>
<form action="/student/assign-subject" method="post">
    <input type="hidden" name="studentId" value="${student.id}"/>

    <c:forEach var="subject" items="${subjectList}">
        <input type="checkbox" name="subjectIds" value="${subject.id}"/>
        <br> ${subject.name} <br/>
    </c:forEach>

    <input type="submit" value="Save Subjects"/>
</form>
</body>
</html>
