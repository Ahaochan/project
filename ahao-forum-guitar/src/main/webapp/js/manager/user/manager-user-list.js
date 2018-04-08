var jump = function (page) {
    var search = $('input[name="user-name"]').val();
    getListFun({page: page, search: search});
};
var getListFun = function (option) {
    var options = $.extend({
        page: 1,
        search: ''
    }, option);

    var page = options.page, search = options.search;

    $.ajax({
        type: 'get',
        url: ctx + '/manager/api/users/list-' + page,
        data: {search: search},
        success: function (json) {
            var $tbody = $('tbody');
            $tbody.empty();

            if (!json.result) {
                $tbody.append('<tr><td colspan="9" class="text-center">暂无数据</tr>');
                return;
            }


            var list = json.obj.list;
            for (var i = 0, len = list.length; i < len; i++) {
                var item = list[i];
                $tbody.append('<tr ' + (item.enabled ? '' : 'class="danger"') + '>' +
                    '   <td><input type="checkbox" name="user-id" value="' + item.id + '"/></td>' +
                    '   <td>' + item.id + '</td>' +
                    '   <td>' + item.username + '</td>' +
                    '   <td>' + ($.format.date(item.last_login_time || '无', 'yyyy-MM-dd')) + '</td>' +
                    '   <td>' + (item.last_login_ip || '无') + '</td>' +
                    '   <td>' + (!!item.enabled ? '正常' : '禁用') + '</td>' +
                    '   <td>' + (item.thread_num || '0') + '</td>' +
                    '   <td>' + (item.post_num || '0') + '</td>' +
                    '   <td>' +
                    '       <a type="button" class="btn btn-primary btn-circle btn-sm" href="' + ctx + '/manager/user-' + item.id + '">' +
                    '           <i class="glyphicon glyphicon-pencil"></i>' +
                    '       </a> &nbsp;' +
                    '       <a class="btn btn-warning btn-circle btn-sm btn-delete" ahao-user-id="' + item.id + '">' +
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
    // 1. 初始化表格, 获取第1页数据
    (function ($) {
        getListFun();
    })(jQuery);

    // 2. 搜索功能
    (function ($) {
        $('#form-search').submit(function (event) {
            var search = $('input[name="user-name"]').val();
            getListFun({search: search});
            event.preventDefault();
        });
        $('input[name="user-name"]').on('keyup', function () {
            var $this = $(this);
            clearTimeout(parseInt($this.data('timer')));
            var search = $this.val();
            $this.data('timer', setTimeout(function () {
                getListFun({search: search});
            }, 500));
        });
    })(jQuery);

    // 3. 删除功能
    (function ($) {
        var deleteFun = function (userIds) {
            var ids = [].concat(userIds);
            // TODO 为所有list添加删除判断
            if(ids.length <= 0){
                swal({type: 'warning', title: '警告', text: '最少选中一条数据进行删除'});
                return;
            }

            $.ajax({
                type: 'post',
                url: ctx + '/manager/api/users/delete',
                data: {userIds: ids},
                dataType: 'json',
                success: function (json) {
                    if (!json.result) {
                        swal({type: 'warning', title: '警告', text: json.msg});
                        return;
                    }
                    swal({type: 'success', title: '成功', text: json.msg});
                    $('#form-search').submit();
                }
            });
        };

        // 3.1 批量删除
        $('#btn_delete_list').on('click', function () {
            var userIds = $('input[name="user-id"]:checked').map(function () {
                return this.value;
            }).get();
            deleteFun(userIds);
        });

        // 3.2. 单个删除
        $('tbody').on('click', 'a.btn-delete', function () {
            var $this = $(this);
            var userId = $this.attr('ahao-user-id');
            if (!!userId) {
                deleteFun(userId);
            }
        });
    })(jQuery);
});