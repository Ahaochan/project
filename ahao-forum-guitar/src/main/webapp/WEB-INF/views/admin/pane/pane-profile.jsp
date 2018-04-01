<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>个人资料</title>
</head>
<body>
<%-- 导航条 --%>
<%@include file="/WEB-INF/views/static/nav.jsp" %>

<%-- 内容 --%>
<div class="container">
    <div class="row">
        <div class="col-md-2">
            <ul class="nav nav-pills nav-stacked">
                <li class="active"><a href="javascript:void(0)">个人资料</a></li>
                <li><a href="${contextPath}/password">修改密码</a></li>
                <li><a href="${contextPath}/manager/categories">分区管理</a></li>
                <li><a href="#">板块管理</a></li>
                <li><a href="#">用户管理</a></li>
                <li><a href="#">权限管理</a></li>
                <li><a href="#manager-user">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="tab-content tab-pane">
                <div class="panel panel-default">
                    <div class="panel-heading">个人资料</div>
                    <div class="panel-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label for="input-username" class="col-md-2 control-label">用户名</label>
                                <div class="col-md-10">
                                    <input class="form-control" id="input-username" placeholder="用户名" readonly disabled
                                           value="${username}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-email" class="col-md-2 control-label">电子邮箱</label>
                                <div class="col-md-10">
                                    <input type="email" class="form-control" id="input-email" placeholder="电子邮箱"
                                           value="${profile.getString('email')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-sex" class="col-md-2 control-label">性别</label>
                                <div class="col-md-10">
                                    <input class="form-control" id="input-sex" placeholder="性别"
                                           value="${profile.getString('sex')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-qq" class="col-md-2 control-label">QQ号</label>
                                <div class="col-md-10">
                                    <input class="form-control" id="input-qq" placeholder="QQ号"
                                           value="${profile.getString('qq')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-city" class="col-md-2 control-label">城市</label>
                                <div class="col-md-10">
                                    <input type="email" class="form-control" id="input-city" placeholder="城市"
                                           value="${profile.getString('city')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-enabled" class="col-md-2 control-label">帐号状态</label>
                                <div class="col-md-10">
                                    <c:choose>
                                        <c:when test="${user.getInt('enabled') == 0}">
                                            <input class="form-control" id="input-enabled" placeholder="帐号状态" readonly
                                                   value="封禁">
                                        </c:when>
                                        <c:otherwise>
                                            <input class="form-control" id="input-enabled" placeholder="帐号状态" readonly
                                                   value="正常">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-last-login-time" class="col-md-2 control-label">上次登录时间</label>
                                <div class="col-md-4">
                                    <input class="form-control" id="input-last-login-time" placeholder="上次登录时间"
                                           value="${user.getString('last_login_time')}" disabled>
                                </div>
                                <label for="input-last-login-ip" class="col-md-2 control-label">上次登录ip</label>
                                <div class="col-md-4">
                                    <input class="form-control" id="input-last-login-ip" placeholder="上次登录ip"
                                           value="${user.getString('last_login_ip')}" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">角色权限</label>
                                <div class="col-md-4">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                角色
                                                <span class="glyphicon glyphicon-triangle-bottom" data-toggle="collapse" href="#pane-sub-role"></span>
                                            </h4>
                                        </div>
                                        <div id="pane-sub-role" class="panel-collapse collapse in">
                                            <div class="panel-body">
                                                <c:forEach items="${roles}" var="item">
                                                    <div class="checkbox">
                                                        <label>
                                                            <input type="checkbox" name="roles" disabled
                                                                   value="${item.getInt("id")}"
                                                                ${"".equals(item.getString("selected")) ? "":"checked"}/>
                                                                ${item.getString("description")}</label>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                权限
                                                <span class="glyphicon glyphicon-triangle-bottom" data-toggle="collapse" href="#pane-sub-auth"></span>
                                            </h4>
                                        </div>
                                        <div id="pane-sub-auth" class="panel-collapse collapse in">
                                            <div class="panel-body">
                                                <c:forEach items="${auths}" var="item">
                                                    <div class="checkbox">
                                                        <label>
                                                            <input type="checkbox" name="auths" disabled
                                                                   value="${item.getInt("id")}"
                                                                ${"".equals(item.getString("selected")) ? "":"checked"}/>
                                                                ${item.getString("description")}</label>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-2 col-md-10">
                                    <button type="submit" class="btn btn-default">保存</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
</html>