<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>电吉他设备选购及知识普及软件平台</title>
</head>
<body>
<%@include file="/WEB-INF/views/static/nav.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>电吉他设备选购及知识普及软件平台</h1>
        <p class="lead">Basic grid layouts to get you familiar with building within the Bootstrap grid system.</p>
    </div>
    <div class="panel-group">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <span>电吉他专区</span>
                    <span class="glyphicon glyphicon-triangle-bottom" data-toggle="collapse" href="#collapseOne"></span>
                </h4>
            </div>
            <div id="collapseOne" class="panel-collapse collapse in">
                <div class="panel-body">
                    <div class="col-md-4 panel-item">
                        <img src="https://v3.bootcss.com/assets/img/coding.jpeg"/>
                        <div>
                            <a href="#">电吉他A</a><br/>
                            数量:1000, 回复:1000<br/>
                            最后发表时间: 4天前
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<%@include file="/WEB-INF/views/static/script.jsp" %>
</html>
