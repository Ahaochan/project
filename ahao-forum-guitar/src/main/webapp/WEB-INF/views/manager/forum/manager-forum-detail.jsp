<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>板块管理</title>
    <link href="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/css/fileinput.min.css" rel="stylesheet"/>
    <style>
        .kv-avatar .krajee-default.file-preview-frame, .kv-avatar .krajee-default.file-preview-frame:hover {
            margin: 0;
            padding: 0;
            border: none;
            box-shadow: none;
            text-align: center;
        }

        .kv-avatar {
            display: inline-block;
        }

        .kv-avatar .file-input {
            display: table-cell;
            width: 213px;
        }

        .kv-reqd {
            color: red;
            font-family: monospace;
            font-weight: normal;
        }
    </style>
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
                <li class="active"><a href="${contextPath}/manager/forums">板块管理</a></li>
                <li><a href="${contextPath}/manager/users">用户管理</a></li>
                <li><a href="${contextPath}/manager/auths">权限管理</a></li>
                <li><a href="${contextPath}/manager/roles">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="tab-content tab-pane">
                <div class="panel panel-default">
                    <%--@elvariable id="isExist" type="java.lang.Boolean"--%>
                    <%--@elvariable id="forum" type="com.ahao.core.entity.IDataSet"--%>
                    <div class="panel-heading">${isExist?'编辑板块':'增加板块'}</div>
                    <div class="panel-body">
                        <form class="form-horizontal" id="form-forum">
                            <div class="form-group">
                                <c:if test="${isExist}">
                                    <label for="input-id" class="col-md-2 control-label">板块id</label>
                                    <div class="col-md-3">
                                        <input class="form-control" placeholder="板块id" readonly
                                               id="input-id" name="forum-id"
                                               value="${forum.getInt('id')}"/>
                                    </div>
                                </c:if>
                                <label for="input-name" class="col-md-2 control-label">板块名称</label>
                                <div class="col-md-3">
                                    <input class="form-control" placeholder="板块名称"
                                           id="input-name" name="forum-name"
                                           value="${forum.getString('name')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">板块状态</label>
                                <c:set var="status" value="${forum.getInt('status') == 0}"/>
                                <div class="col-md-3 radio">
                                    <label>
                                        <input type="radio" id="input-status-1" name="forum-status"
                                               value="1" ${status?'':'checked'}>启用
                                    </label>
                                </div>
                                <div class="col-md-3 radio">
                                    <label>
                                        <input type="radio" id="input-status-0" name="forum-status"
                                               value="0" ${status?'checked':''}>禁用
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <input type="hidden" id="input_icon_url" name="forum-icon-url"/>
                                <label for="input_icon" class="col-md-2 control-label">版图</label>
                                <div class="col-md-10 ">
                                    <div class="kv-avatar">
                                        <div class="file-loading">
                                            <input type="file" id="input_icon" name="file"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-description" class="col-md-2 control-label">板块描述</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="板块描述"
                                           id="input-description" name="forum-description"
                                           value="${forum.getString('description')}">
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
<script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/js/plugins/purify.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/js/plugins/piexif.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/js/fileinput.min.js"></script>
<script src="${contextPath}/js/core.js"></script>
<script>
    $(function () {
        (function ($) {
            var fileInput = new FileInput();
            fileInput.initImg({
                selector: '#input_icon',
                uploadUrl: ctx + '/upload/img',
                filePath: 'forum-icon'
            }).on('change', function(event) {
                // TODO 提醒上传, 并禁用提交按钮
                console.log("change");
            }).on('filebeforedelete', function() {
                console.log("filebeforedelete");
            }).on('filedeleted', function() {
                console.log("filedeleted");
            }).on('fileuploaded', function (event, data, previewId, index) {
                var json = data.response;
                if (!json.result) {
                    swal({type: 'warning', title: '警告', text: '上传失败'});
                    return;
                }
                $('input[name="forum-icon-url"]').val(json.obj.url || '');
            });
        })(jQuery);


        (function ($) {
            $('#form-forum').submit(function (e) {
                e.preventDefault();

                var forumId = $('input[name="forum-id"]').val();
                var name = $('input[name="forum-name"]').val();
                var description = $('input[name="forum-description"]').val();
                var status = $('input[name="forum-status"]:checked').val();
                var iconUrl = $('input[name="forum-icon-url"]').val();

                $.ajax({
                    type: 'post',
                    url: ctx + '/manager/api/forum/save',
                    data: {
                        forumId: forumId,
                        name: name,
                        description: description,
                        status: status,
                        iconUrl: iconUrl
                    },
                    success: function (json) {
                        if (!json.result) {
                            swal({type: 'warning', title: '警告', text: '保存失败'});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: '保存成功'});

                        // setTimeout(function () {
                        //     window.location.href = ctx + '/manager/forums';
                        // })
                    }
                });
            });
        })(jQuery);
    });
</script>
</html>