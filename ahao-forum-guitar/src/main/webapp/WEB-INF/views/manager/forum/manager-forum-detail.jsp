<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>板块管理</title>
    <meta name="icon-url" content="${forum.getString('icon_url')}"/>
        <meta name="forumId" content="${forum.getInt('id')}"/>
    <link href="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/css/fileinput.min.css" rel="stylesheet"/>
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
                                    <div class="ahao-avatar">
                                        <div class="file-loading">
                                            <input type="file" id="input_icon" name="file"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">板块描述</label>
                                <div class="col-md-10">
                                    <div id="editor">
                                        ${forum.getString('description')}
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
            <c:if test="${isExist}">
            <div class="tab-content tab-pane">
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
                                <form id="form-thread-search">
                                    <div class="input-group">
                                        <input type="text" class="form-control" placeholder="搜索主题名"
                                               name="thread-name"/>
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
                            <th class="col-md-2">主题标题</th>
                            <th class="col-md-2">发表用户</th>
                            <th class="col-md-2">回复数量</th>
                            <th class="col-md-2">最后回复时间</th>
                            <th class="col-md-4">操作</th>
                        </tr>
                        </thead>
                        <tbody id="tbody-thread">
                        <%-- Ajax加载数据 --%>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="text-right" id="pagination">
                <%-- Ajax加载数据 获取分页器 --%>
                    哈哈哈
            </div>
            </c:if>
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
<%-- Boostrap 文件上传 --%>
<script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/js/plugins/purify.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/js/plugins/piexif.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/js/fileinput.min.js"></script>
<%-- wangEditor 富文本编辑器 --%>
<script src="https://unpkg.com/wangeditor@3.1.0/release/wangEditor.min.js"></script>

<script src="${contextPath}/js/core.js"></script>
<script>
    var jump = function (page) {
        var search = $('input[name="thread-name"]').val();
        getListFun({page: page, search: search});
    };
    var getListFun = function (option) {
        var options = $.extend({
            page: 1,
            search: ''
        }, option);

        var page = options.page, search = options.search;
        var forumId = $('meta[name="forumId"]').attr('content');
        $.ajax({
            type: 'get',
            url: ctx + '/api/forum-' + forumId + '/thread/list-' + page,
            data: {search: search},
            success: function (json) {
                var $tbody = $('#tbody-thread');
                $tbody.empty();

                if (!json.result) {
                    $tbody.append('<tr><td colspan="6" class="text-center">暂无数据</tr>');
                    return;
                }

                var list = json.obj.list;
                for (var i = 0, len = list.length; i < len; i++) {
                    var item = list[i];
                    $tbody.append('<tr>' +
                        '   <td><input type="checkbox" name="thread-id" value="' + item.id + '"/></td>' +
                        '   <td>' + item.id + '</td>' +
                        '   <td><a href="' + ctx + '/thread-' + item.id + '">' + item.title + '</a></td>' +
                        '   <td>' + item.username + '</td>' +
                        '   <td>' + (item.reply_num || '0') + '</td>' +
                        '   <td>' + ($.format.date(item.last_reply_time, 'yyyy-MM-dd hh:mm:ss')) + '</td>' +
                        '   <td>' +
                        '       <a type="button" class="btn btn-primary btn-circle btn-sm" href="' + ctx + '/thread-' + item.id + '/modify">' +
                        '           <i class="glyphicon glyphicon-pencil"></i>' +
                        '       </a> &nbsp;' +
                        '       <a class="btn btn-warning btn-circle btn-sm btn-delete" ahao-thread-id="' + item.id + '">' +
                        '           <i class="glyphicon glyphicon-remove"></i>' +
                        '       </a>' +
                        '   </td>' +
                        '</tr>');
                }
                $('#pagination').empty().append(json.obj.pageIndicator);
            }
        });
    };

    $(function () {
        // 1. 文件上传插件
        (function ($) {
            var url = $('meta[name="icon-url"]').attr('content');
            url = (!!url) ? ctx + url : undefined;

            var fileInput = new FileInput();
            fileInput.initImg({
                selector: '#input_icon',
                uploadUrl: ctx + '/upload/img',
                placeholderImg: url,
                filePath: 'forum-icon'
            }).on('change', function (event) {
                // TODO 提醒上传, 并禁用提交按钮
                console.log("change");
            }).on('filebeforedelete', function () {
                console.log("filebeforedelete");
            }).on('filedeleted', function () {
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

        // 2. 富文本编辑插件
        var editor;
        (function ($) {
            editor = new RichEditor();
            editor.init({
                uploadImgUrl: ctx + '/upload/img',
                selector: '#editor',
                filePath: 'editor',
            });
        })(jQuery);

        // 3. ajax提交表单
        (function ($) {
            $('#form-forum').submit(function (e) {
                e.preventDefault();

                var forumId = $('input[name="forum-id"]').val();
                var name = $('input[name="forum-name"]').val();
                var description = filterXSS(editor.html());
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

                        setTimeout(function () {
                            window.location.href = ctx + '/manager/forums';
                        }, 3000)
                    }
                });
            });
        })(jQuery);

        // 4. 加载主题列表
        (function ($) {

            // 4.1. 加载主题列表
            getListFun();

            // 4.2. 加载搜索功能
            $('#form-thread-search').submit(function (event) {
                event.preventDefault();

                var search = $('input[name="thread-name"]').val();
                getListFun({search: search});
            });
            $('input[name="thread-name"]').on('keyup', function () {
                var $this = $(this);
                clearTimeout(parseInt($this.data('timer')));
                var search = $this.val();
                $this.data('timer', setTimeout(function () {
                    getListFun({search: search});
                }, 500));
            });

            // 4.3. 加载删除功能
            var deleteFun = function (threadIds) {
                $.ajax({
                    type: 'post',
                    url: ctx + '/manager/api/thread/delete',
                    data: {threadIds: [].concat(threadIds)},
                    dataType: 'json',
                    success: function (json) {
                        if (!json.result) {
                            swal({type: 'warning', title: '警告', text: json.msg});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: json.msg});
                        $('#form-thread-search').submit();
                    }
                });
            };
            // 4.3.1 批量删除
            $('#btn_delete_list').on('click', function () {
                var forumIds = $('input[name="thread-id"]:checked').map(function () {
                    return this.value;
                }).get();
                deleteFun(forumIds);
            });
            // 4.3.2. 单个删除
            $('#tbody-thread').on('click', 'a.btn-delete', function () {
                console.log("测试2");
                var $this = $(this);
                var threadId = $this.attr('ahao-thread-id');
                if (!!threadId) {
                    deleteFun(threadId);
                }
            });
        })(jQuery);
    });
</script>
</html>