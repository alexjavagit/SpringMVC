<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

    <title>Prog.kiev.ua</title>
</head>
<body>
<div align="center">
    <form method="POST" >
    <h1>List of photos</h1>

    <input type="submit" value="Delete Selected Photos" formaction="/delete_pics"/>
    <input type="submit" value="Achieve Selected Photos" formaction="/achieve_pics"/>
    <input type="button" value="Upload New" onclick="window.location='/';" />

    <br/><br/>

    <table class="table table-striped table-hover">
        <tr><th></th><th>Id</th><th>Photo</th></tr>
        <c:forEach var="pic" items="${pictures}">
            <tr>
                <td><input type="checkbox" name="del[]" value="${pic.key}"></td>
                <td>${pic.key}</td>
                <td><img src="/photo/${pic.key}" /></td>
            </tr>
        </c:forEach>
    </table>
    </form>
</div>
</body>
</html>