<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <%--@elvariable id="isExist" type="java.lang.Boolean"--%>
    <%--@elvariable id="thread" type="com.ahao.core.entity.IDataSet"--%>
    <title>${isExist ? '编辑回复' : '发表回复'}-电吉他设备选购及知识普及软件平台</title>
    <meta name="threadId" content="${thread.getInt("thread_id")}"/>
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
                    <h1>${isExist ? '编辑回复' : '发表回复'}</h1>
                </div>
                <a href="${contextPath}">首页</a> >
                <a href="${contextPath}/forum-${thread.getInt("forum_id")}">${thread.getString("forum_name")}</a> >
                <a href="${contextPath}/thread-${thread.getInt("thread_id")}">${thread.getString("thread_title")}</a> >
                <span>${isExist ? '编辑回复' : '发表回复'}</span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-heading">${thread.getString('thread_title')}</div>
            <div class="panel-body">
                <div class="col-md-12">
                    ${thread.getString('thread_content')}
                </div>
            </div>
        </div>
    </div>
    <%--@elvariable id="prePost" type="com.ahao.core.entity.IDataSet"--%>
    <c:if test="${prePost != null}">
    <div class="row">
        <input type="hidden" name="pre-post-id" value="${prePost.getInt("id")}"/>
        <div class="panel-heading">回复该内容</div>
        <div class="panel panel-default">
            <div class="panel-body">
                <div class="col-md-12" id="pre-post-content">
                        ${prePost.getString('content')}
                </div>
            </div>
        </div>
    </div>
    </c:if>
    <%--@elvariable id="post" type="com.ahao.core.entity.IDataSet"--%>
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-body">
                <form class="form" id="form-post">
                    <c:if test="${isExist}">
                        <input type="hidden" name="post-id" value="${post.getInt("id")}"/>
                    </c:if>
                    <div class="form-group">
                        <div class="col-md-12">
                            <div id="editor">
                                <c:if test="${prePost != null}"><blockquote>${prePost.getString('content')}</blockquote></c:if>
                                ${post.getString('content')}
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <button type="submit" class="btn btn-primary btn-lg btn-block">${isExist ? '保存' : '发表回复'}</button>
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
                filePath: 'post-img',
            });
        })(jQuery);

        // 2. ajax提交表单
        (function ($) {
            $('#form-post').submit(function (e) {
                e.preventDefault();

                var postId = $('input[name="post-id"]').val() || '0';
                var prePostId = $('input[name="pre-post-id"]').val();
                var prePostContent = $('input[name="pre-post-content"]').val();
                var threadId = $('meta[name="threadId"]').attr('content');
                var content = filterXSS(editor.html());

                // 1. 添加回复信息
                if(!!prePostId && content.indexOf('<blockquote>'+prePostContent+'</blockquote>') !== -1){
                    content = '<blockquote>'+prePostContent+'</blockquote>' + content;
                }

                // 2. 判空
                if(!content.trim()){
                    swal({type: 'warning', title: '警告', text: '内容不能为空!'});
                    return;
                }

                // 3. 保存
                $.ajax({
                    type: 'post',
                    url: ctx + '/api/post-' + postId + '/save',
                    data: {
                        postId: postId,
                        prePostId: prePostId,
                        threadId: threadId,
                        content: content
                    },
                    success: function (json) {
                        if (!json.result || !json.obj) {
                            swal({type: 'warning', title: '警告', text: '保存失败'});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: '保存成功'});

                        setTimeout(function () {
                            window.location.href = ctx + '/thread-'+threadId;
                        }, 3000)
                    }
                });
            });
        })(jQuery);
    });
</script>
</html>
