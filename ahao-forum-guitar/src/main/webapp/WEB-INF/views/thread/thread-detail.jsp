<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <%--@elvariable id="isExist" type="java.lang.Boolean"--%>
    <%--@elvariable id="thread" type="com.ahao.core.entity.IDataSet"--%>
    <title>${isExist ? '编辑主题' : '发表主题'}-电吉他设备选购及知识普及软件平台</title>
    <meta name="forumId" content="${thread.getInt("forum_id")}"/>
</head>
<body>
<%-- 导航条 --%>
<%@include file="/WEB-INF/views/static/nav.jsp" %>

<%-- 内容 --%>
<div class="container">
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-body">
                <div class="page-header">
                    <h1>${isExist ? '编辑主题' : '发表主题'}</h1>
                </div>
                <a href="${contextPath}">首页</a> >
                <a href="${contextPath}/forum-${thread.getInt("forum_id")}">${thread.getString("forum_name")}</a> >
                <span>${isExist ? '编辑主题' : '发表主题'}</span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-body">
                <form class="form" id="form-thread">
                    <c:if test="${isExist}">
                        <input type="hidden" name="thread-id" value="${thread.getInt("thread_id")}"/>
                    </c:if>
                    <div class="form-group">
                        <div class="col-md-12">
                            <label for="input-title1"
                                   class="control-label">主题标题</label>
                            <input class="form-control" placeholder="主题标题"
                                   id="input-title1" name="thread-title"
                                   value="${thread.getString('thread_title')}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <div id="editor">
                                ${thread.getString('thread_content')}
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <button type="submit" class="btn btn-primary btn-lg btn-block">保存</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
<%-- 预防 XSS 攻击 --%>
<script src="https://cdn.bootcss.com/js-xss/0.3.3/xss.min.js"></script>
<%-- wangEditor 富文本编辑器 --%>
<script src="https://unpkg.com/wangeditor@3.1.0/release/wangEditor.min.js"></script>
<script src="${contextPath}/js/core.js"></script>
<script>
    $(function () {
        // 1. 富文本编辑插件
        var editor;
        (function ($) {
            editor = new RichEditor();
            editor.init({
                uploadImgUrl: ctx + '/upload/img',
                selector: '#editor',
                filePath: 'thread-img',
            });
        })(jQuery);

        // 2. ajax提交表单
        (function ($) {
            $('#form-thread').submit(function (e) {
                e.preventDefault();

                var forumId = $('meta[name="forumId"]').attr('content');
                var threadId = $('input[name="thread-id"]').val() || '0';
                var title = filterXSS($('input[name="thread-title"]').val());
                var content = filterXSS(editor.html());

                if(!title.trim() || !content.trim()){
                    swal({type: 'warning', title: '警告', text: '标题或内容不能为空!'});
                    return;
                }

                $.ajax({
                    type: 'post',
                    url: ctx + '/api/thread-' + threadId + '/save',
                    data: {
                        forumId: forumId,
                        title: title,
                        content: content
                    },
                    success: function (json) {
                        if (!json.result || !json.obj) {
                            swal({type: 'warning', title: '警告', text: '保存失败'});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: '保存成功'});

                        setTimeout(function () {
                            window.location.href = ctx + '/thread-'+json.obj;
                        }, 3000)
                    }
                });
            });
        })(jQuery);
    });
</script>
</html>
