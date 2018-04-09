<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>用户管理</title>
    <meta name="superModeratorId" content="${superModeratorId}"/>
    <meta name="moderatorId" content="${moderatorId}"/>
</head>
<body>
<%-- 导航条 --%>
<%@include file="/WEB-INF/views/static/nav.jsp" %>

<%-- 内容 --%>
<div class="container">
    <div class="row">
        <div class="col-md-2">
            <ul class="nav nav-pills nav-stacked">
                <li><a href="${contextPath}/manager/profile">个人资料</a></li>
                <li><a href="${contextPath}/manager/password">修改密码</a></li>
                <li><a href="${contextPath}/manager/categories">分区管理</a></li>
                <li><a href="${contextPath}/manager/forums">板块管理</a></li>
                <li class="active"><a href="${contextPath}/manager/users">用户管理</a></li>
                <li><a href="${contextPath}/manager/auths">权限管理</a></li>
                <li><a href="${contextPath}/manager/roles">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="tab-content tab-pane">
                <div class="panel panel-default">
                    <%--@elvariable id="isExist" type="java.lang.Boolean"--%>
                    <%--@elvariable id="user" type="com.ahao.core.entity.IDataSet"--%>
                    <div class="panel-heading">${isExist?'编辑用户':'增加用户'}</div>
                    <div class="panel-body">
                        <form class="form-horizontal" id="form-user">
                            <div class="form-group">
                                <c:if test="${isExist}">
                                <label for="input-id" class="col-md-2 control-label">用户id</label>
                                <div class="col-md-3">
                                    <input class="form-control" placeholder="用户id" disabled
                                           id="input-id" name="user-id"
                                           value="${user.getInt('id')}"/>
                                </div>
                                </c:if>
                                <label for="input-username" class="col-md-2 control-label">用户名</label>
                                <div class="col-md-3">
                                    <input class="form-control" placeholder="用户名"
                                           id="input-username" name="user-username"
                                           value="${user.getString('username')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-password" class="col-md-2 control-label">密码</label>
                                <div class="col-md-3">
                                    <input type="password" class="form-control" placeholder="密码"
                                           id="input-password" name="user-password"
                                           value="${user.getString('password')}">
                                </div>
                                <label class="col-md-2 control-label">所属角色</label>
                                <div class="col-md-3">
                                    <select class="selectpicker form-control" name="select-role">
                                        <c:forEach items="${roles}" var="item">
                                            <option ${item.getBoolean("enabled") ? '' : 'disabled'}
                                                ${"".equals(item.getString("selected")) ? "":"selected"}
                                                    value="${item.getInt("id")}">${item.getString("description")}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-email" class="col-md-2 control-label">电子邮箱</label>
                                <div class="col-md-3">
                                    <input type="email" class="form-control" placeholder="电子邮箱"
                                           value="${user.getString('email')}"
                                           id="input-email" name="profile-email"/>
                                </div>
                                <label for="input-qq" class="col-md-2 control-label">QQ号</label>
                                <div class="col-md-3">
                                    <input class="form-control" placeholder="QQ号" value="${user.getString('qq')}"
                                           id="input-qq" name="profile-qq"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">性别</label>
                                <div class="col-md-10">
                                    <c:set var="sex" value="${user.getInt('sex')}"/>
                                    <div class="col-md-3 radio">
                                        <label>
                                            <input type="radio" id="input-sex-1" name="profile-sex"
                                                   value="1" ${sex == 1 ?'checked':''}>男
                                        </label>
                                    </div>
                                    <div class="col-md-3 radio">
                                        <label>
                                            <input type="radio" id="input-sex-2" name="profile-sex"
                                                   value="2" ${sex == 2 ?'checked':''}>女
                                        </label>
                                    </div>
                                    <div class="col-md-3 radio">
                                        <label>
                                            <input type="radio" id="input-sex-0" name="profile-sex"
                                                   value="0" ${sex == 0 ?'checked':''}>保密
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-city" class="col-md-2 control-label">城市</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="城市" value="${user.getString('city')}"
                                           id="input-city" name="profile-city"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">帐号状态</label>
                                <div class="col-md-10">
                                    <c:set var="enabled" value="${user.getInt('enabled')}"/>
                                    <div class="col-md-3 radio">
                                        <label>
                                            <input type="radio" id="input-enabled-1" name="user-enabled"
                                                   value="1" ${enabled == 1 ?'checked':''}>正常
                                        </label>
                                    </div>
                                    <div class="col-md-3 radio">
                                        <label>
                                            <input type="radio" id="input-enabled-0" name="user-enabled"
                                                   value="0" ${enabled == 0 ?'checked':''}>封禁
                                        </label>
                                    </div>

                                </div>
                            </div>
                            <c:if test="${isExist}">
                                <div class="form-group">
                                    <label for="input-last-login-time" class="col-md-2 control-label">上次登录时间</label>
                                    <div class="col-md-3">
                                        <input class="form-control" placeholder="上次登录时间" disabled
                                               id="input-last-login-time"
                                               value="${user.getString('last_login_time')}"/>
                                    </div>
                                    <label for="input-last-login-ip" class="col-md-2 control-label">上次登录ip</label>
                                    <div class="col-md-3">
                                        <input class="form-control" placeholder="上次登录ip" disabled
                                               id="input-last-login-ip"
                                               value="${user.getString('last_login_ip')}"/>
                                    </div>
                                </div>
                            </c:if>
                            <div class="form-group" id="panel-category" style="display: ${showCategory ? 'block' : 'none'}">
                                <label class="col-md-2 control-label">管理分区</label>
                                <div class="col-md-3">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                管理分区
                                                <span class="glyphicon glyphicon-triangle-bottom" data-toggle="collapse"
                                                      href="#pane-category"></span>
                                            </h4>
                                        </div>
                                        <div id="pane-category" class="panel-collapse collapse in">
                                            <div class="panel-body">
                                                <c:forEach items="${categories}" var="item">
                                                    <div class="checkbox ${item.getBoolean("status") ? '' : 'disabled'}">
                                                        <label>
                                                            <input type="checkbox" name="category-id"
                                                                   value="${item.getInt("id")}"
                                                                ${item.getBoolean("status") ? '' : 'disabled'}
                                                                ${"".equals(item.getString("selected")) ? "":"checked"}/>
                                                                ${item.getString("name")}</label>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                </div>
                            <div class="form-group" id="panel-forum" style="display: ${showForum ? 'block' : 'none'}">
                                <label class="col-md-2 control-label">管理板块</label>
                                <div class="col-md-3">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                管理板块
                                                <span class="glyphicon glyphicon-triangle-bottom" data-toggle="collapse"
                                                      href="#pane-forum"></span>
                                            </h4>
                                        </div>
                                        <div id="pane-forum" class="panel-collapse collapse in">
                                            <div class="panel-body">
                                                <c:forEach items="${forums}" var="item">
                                                    <div class="checkbox ${item.getBoolean("status") ? '' : 'disabled'}">
                                                        <label>
                                                            <input type="checkbox" name="forum-id"
                                                                   value="${item.getInt("id")}"
                                                                ${item.getBoolean("status") ? '' : 'disabled'}
                                                                ${"".equals(item.getString("selected")) ? "":"checked"}/>
                                                                ${item.getString("name")}</label>
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
                <c:if test="${isExist}">
                    <div class="panel panel-default">
                        <div class="panel-heading">主题管理</div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="btn-group">
                                        <a id="btn_delete_thread-list" class="btn btn-warning">
                                            <span class="glyphicon glyphicon-remove"></span>删除
                                        </a>
                                    </div>
                                </div>
                                <div class="col-md-3 col-md-offset-6">
                                    <form id="form-thread-search">
                                        <div class="input-group">
                                            <input type="text" class="form-control" placeholder="搜索主题名"
                                                   name="thread-title"/>
                                            <span class="input-group-btn">
                                            <button class="btn btn-default" type="submit">搜索</button>
                                        </span>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <table class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th class="col-md-1"></th>
                                <th class="col-md-1">主题id</th>
                                <th class="col-md-1">主题名称</th>
                                <th class="col-md-2">主题链接</th>
                                <th class="col-md-2">最后一次登录Ip</th>
                                <th class="col-md-1">状态</th>
                                <th class="col-md-1">主题数</th>
                                <th class="col-md-1">回复数</th>
                                <th class="col-md-2">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                                <%-- Ajax加载数据 --%>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading">主题管理</div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="btn-group">
                                        <a id="btn_delete_list" class="btn btn-warning">
                                            <span class="glyphicon glyphicon-remove"></span>删除
                                        </a>
                                    </div>
                                </div>
                                <div class="col-md-3 col-md-offset-6">
                                    <form id="form-search">
                                        <div class="input-group">
                                            <input type="text" class="form-control" placeholder="搜索主题名"
                                                   name="user-name"/>
                                            <span class="input-group-btn">
                                                    <button class="btn btn-default" type="submit">搜索</button>
                                                </span>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <table class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th class="col-md-1"></th>
                                <th class="col-md-1">用户id</th>
                                <th class="col-md-1">用户名</th>
                                <th class="col-md-2">最后一次登录时间</th>
                                <th class="col-md-2">最后一次登录Ip</th>
                                <th class="col-md-1">状态</th>
                                <th class="col-md-1">主题数</th>
                                <th class="col-md-1">回复数</th>
                                <th class="col-md-2">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                                <%-- Ajax加载数据 --%>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
<script src="${contextPath}/js/core.js"></script>
<script src="${contextPath}/js/manager/user/manager-user-detail.js"></script>
</html>