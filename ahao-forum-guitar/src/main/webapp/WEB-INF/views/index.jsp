<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>电吉他设备选购及知识普及软件平台</title>
</head>
<body>
<%-- 导航条 --%>
<%@include file="/WEB-INF/views/static/nav.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>电吉他设备选购及知识普及软件平台</h1>
        <p class="lead">Basic grid layouts to get you familiar with building within the Bootstrap grid system.</p>
    </div>
    <div class="panel-group">
        <c:forEach items="${category}" var="group" varStatus="loop">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        ${group.getString("name")}
                        <span class="glyphicon glyphicon-triangle-bottom" data-toggle="collapse" href="#collapse${loop.index}"></span>
                    </h4>
                </div>
                <div id="collapse${loop.index}" class="panel-collapse collapse in">
                    <div class="panel-body media-list">
                        <div class="col-md-4 panel-item media" style="display: none">
                            <div class="media-left">
                                <a href="#"><img src="https://v3.bootcss.com/assets/img/coding.jpeg"/></a>
                            </div>
                            <div class="media-body">
                                <a href="#">板块标题</a><br/>
                                数量:0000, 回复:0000<br/>
                                最后发表时间: N天前
                            </div>
                        </div>
                        <c:forEach items="${group.get('sub')}" var="item">
                            <div class="col-md-4 panel-item media">
                                <div class="media-left">
                                    <a href="${contextPath}/category-${item.getInt("id")}"><img src="${item.getString("icon_url")}"/></a>
                                </div>
                                <div class="media-body">
                                    <a href="${contextPath}/category-${item.getInt("id")}">${item.getString("name")}</a><br/>
                                    数量:1000, 回复:1000<br/>
                                    最后发表时间: 4天前
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
</html>
