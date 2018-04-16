<%@ page contentType="text/html;charset=UTF-8" %>
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
    <form class="form-signin" id="form-register">
        <h2 class="form-signin-heading">请注册</h2>
        <div class="alert alert-danger" style="display: none" id="ajax-error-msg">用户已注册</div>
        <div class="input-group form-group">
            <label class="input-group-addon" for="form-username">
                <i class="glyphicon glyphicon-user"></i>
            </label>
            <%--@elvariable id="username" type="java.lang.String"--%>
            <input id="form-username" name="username" class="form-control" value=""/>
        </div>
        <div class="input-group form-group">
            <label class="input-group-addon" for="form-password">
                <i class="glyphicon glyphicon-lock"></i>
            </label>
            <%--@elvariable id="password" type="java.lang.String"--%>
            <input id="form-password" name="password" class="form-control" type="password" value=""/>
        </div>
        <button class="btn btn-lg btn-primary btn-block" id="btn-submit">注册</button>
    </form>
</div>

<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
<script src="${contextPath}/js/core.js"></script>
<script>
    $(function () {
        // 1. 检查用户名是否存在
        (function ($) {
            $('input[name="username"]').delayKeyUp(function (val) {
                $.ajax({
                    type: "POST",
                    url: ctx + "/register/checkUserName",
                    data: {username: val},
                    success: function (json) {
                        var $msg = $('#ajax-error-msg');
                        $msg.empty();
                        if (!json.result) {
                            $msg.show().text(json.msg);
                            return;
                        }
                        $msg.hide();
                    }
                });
            })
        })(jQuery);

        // 2. 注册用户
        (function ($) {
            $('#form-register').submit(function (e) {
                e.preventDefault();
                var username = $('input[name="username"]').val();
                var password = $('input[name="password"]').val();
                if (!username.trim() || !password.trim()) {
                    swal({type: 'warning', title: '警告', text: '用户名或密码不能为空!'});
                    return;
                }
                // if ($('#ajax-error-msg').css('display') !== 'none') {
                //     swal({type: 'warning', title: '警告', text: '注册失败! 请检查错误信息!'});
                //     return;
                // }

                $('#btn-submit').disable(true);
                $.ajax({
                    type: 'post',
                    url: ctx + '/register',
                    data: { username: username, password: password},
                    success: function (json) {
                        if (!json.result) {
                            swal({type: 'warning', title: '警告', text: json.msg});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: json.msg});

                        setTimeout(function () {
                            location.href = ctx + '/login';
                        }, 3000)
                    }
                }).always(function () {
                    $('#btn-submit').disable(false);
                });
            });
        })(jQuery);
    });
</script>
</html>
