<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  公共的head标签内容
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
<link rel="stylesheet" href="${contextPath}/css/app.css"/>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<%--
    注意!! 以上代码兼容IE9, 必须在if范围内.
    添加公共js需要添加到script.jsp中!!!!
    添加公共js需要添加到script.jsp中!!!!
    添加公共js需要添加到script.jsp中!!!!
--%>
