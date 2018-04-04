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
                <li><a href="${contextPath}/manager/password">修改密码</a></li>
                <li><a href="${contextPath}/manager/categories">分区管理</a></li>
                <li><a href="${contextPath}/manager/forums">板块管理</a></li>
                <li><a href="${contextPath}/manager/users">用户管理</a></li>
                <li><a href="${contextPath}/manager/auths">权限管理</a></li>
                <li><a href="${contextPath}/manager/roles">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="tab-content tab-pane">
                <div class="panel panel-default">
                    <%--@elvariable id="isExist" type="java.lang.Boolean"--%>
                    <%--@elvariable id="profile" type="com.ahao.core.entity.IDataSet"--%>
                    <div class="panel-heading">个人资料</div>
                    <div class="panel-body">
                        <form class="form-horizontal" id="form-profile">
                            <input class="form-control" type="hidden" name="user-id"
                                   value="${profile.getInt("id")}"/>
                            <div class="form-group">
                                <label for="input-username" class="col-md-2 control-label">用户名</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="用户名"
                                           id="input-username" name="user-username"
                                           value="${username}" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-email" class="col-md-2 control-label">电子邮箱</label>
                                <div class="col-md-10">
                                    <input type="email" class="form-control" placeholder="电子邮箱" value="${profile.getString('email')}"
                                           id="input-email" name="profile-email"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">性别</label>
                                <div class="col-md-10">
                                    <c:set var="sex" value="${profile.getInt('sex')}"/>
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
                                <label for="input-qq" class="col-md-2 control-label">QQ号</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="QQ号" value="${profile.getString('qq')}"
                                           id="input-qq" name="profile-qq"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-city" class="col-md-2 control-label">城市</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="城市" value="${profile.getString('city')}"
                                           id="input-city" name="profile-city"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-enabled" class="col-md-2 control-label">帐号状态</label>
                                <div class="col-md-10">
                                    <c:set var="enabled" value="${user.getInt('enabled') == 0 ? '封禁' : '正常'}"/>
                                    <input class="form-control" placeholder="帐号状态" disabled
                                           id="input-enabled" value="${enabled}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-last-login-time" class="col-md-2 control-label">上次登录时间</label>
                                <div class="col-md-4">
                                    <input class="form-control" placeholder="上次登录时间" disabled
                                           id="input-last-login-time"
                                           value="${user.getString('last_login_time')}" />
                                </div>
                                <label for="input-last-login-ip" class="col-md-2 control-label">上次登录ip</label>
                                <div class="col-md-4">
                                    <input class="form-control" placeholder="上次登录ip" disabled
                                           id="input-last-login-ip"
                                           value="${user.getString('last_login_ip')}" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">角色权限</label>
                                <div class="col-md-4">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                角色
                                                <span class="glyphicon glyphicon-triangle-bottom" data-toggle="collapse"
                                                      href="#pane-sub-role"></span>
                                            </h4>
                                        </div>
                                        <div id="pane-sub-role" class="panel-collapse collapse in">
                                            <div class="panel-body">
                                                <c:forEach items="${roles}" var="item">
                                                    <div class="checkbox disabled">
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
                                                <span class="glyphicon glyphicon-triangle-bottom" data-toggle="collapse"
                                                      href="#pane-sub-auth"></span>
                                            </h4>
                                        </div>
                                        <div id="pane-sub-auth" class="panel-collapse collapse in">
                                            <div class="panel-body">
                                                <c:forEach items="${auths}" var="item">
                                                    <div class="checkbox disabled">
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
<script>
    $(function () {
        (function () {
            $('#form-profile').submit(function (e) {
                e.preventDefault();

                var userId = $('input[name="user-id"]').val();
                if (!userId) {
                    swal({type: 'warning', title: '警告', text: '保存失败, 当前用户非法!'});
                    return;
                }
                var email = $('input[name="profile-email"]').val() || '';
                var sex = $('input[name="profile-sex"]:checked').val() || '0';
                var qq = $('input[name="profile-qq"]').val() || '';
                var city = $('input[name="profile-city"]').val() || '';

                $.ajax({
                    type: 'post',
                    url: ctx + '/manager/api/profile/save',
                    data: {userId: userId, email: email, sex: sex, qq: qq, city: city},
                    success: function (json) {
                        if (!json.result) {
                            swal({type: 'warning', title: '警告', text: '保存错误!'});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: '保存成功'});

                        setTimeout(function () {
                            location.reload();
                        }, 3000)
                    }
                });
            });
        })(jQuery);
    });
</script>
</html>