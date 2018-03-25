<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>标题</title>
</head>
<body>
<%-- 导航条 --%>
<%@include file="/WEB-INF/views/static/nav.jsp" %>

<%-- 内容 --%>
<div class="container">
    <div class="row">
        <div class="col-md-2">
            <ul class="nav nav-pills nav-stacked">
                <li class="active"><a href="#profile">个人资料</a></li>
                <li><a href="#password">修改密码</a></li>
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
                                    <input class="form-control" id="input-username" placeholder="用户名" readonly disabled value="${username}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-email" class="col-md-2 control-label">电子邮箱</label>
                                <div class="col-md-10">
                                    <input type="email" class="form-control" id="input-email" placeholder="电子邮箱">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-sex" class="col-md-2 control-label">性别</label>
                                <div class="col-md-10">
                                    <input class="form-control" id="input-sex" placeholder="性别">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-qq" class="col-md-2 control-label">QQ号</label>
                                <div class="col-md-10">
                                    <input class="form-control" id="input-qq" placeholder="QQ号">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-city" class="col-md-2 control-label">城市</label>
                                <div class="col-md-10">
                                    <input type="email" class="form-control" id="input-city" placeholder="城市">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-enabled" class="col-md-2 control-label">帐号状态</label>
                                <div class="col-md-10">
                                    <input class="form-control" id="input-enabled" placeholder="正常" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">角色权限</label>
                                <div class="col-md-3">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" href="#pane-sub-role">角色</a>
                                            </h4>
                                        </div>
                                        <div id="pane-sub-role" class="panel-collapse collapse in">
                                            <div class="panel-body">
                                                <div class="checkbox"><label></label><input type="checkbox" name="1234" value="123"/>测试</div>
                                                <div class="checkbox"><label></label><input type="checkbox" name="1234" value="123"/>测试</div>
                                                <div class="checkbox"><label></label><input type="checkbox" name="1234" value="123"/>测试</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" href="#pane-sub-auth">权限</a>
                                            </h4>
                                        </div>
                                        <div id="pane-sub-auth" class="panel-collapse collapse in">
                                            <div class="panel-body">
                                                <div class="checkbox"><label></label><input type="checkbox" name="1234" value="123"/>测试</div>
                                                <div class="checkbox"><label></label><input type="checkbox" name="1234" value="123"/>测试</div>
                                                <div class="checkbox"><label></label><input type="checkbox" name="1234" value="123"/>测试</div>
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