let TableInit = function () {
    let oTableInit = {};
    //初始化Table
    oTableInit.Init = function () {
        $('#table-auth').bootstrapTable({
            url: '/admin/auths/page',                 //请求后台的URL（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            rowStyle: function (row) {
                if (!isTrue(row.enabled)) {
                    return {classes: 'danger'};
                }
                return '';
            },
            columns: [{
                checkbox: true
            }, {
                field: 'id',
                title: '权限Id'
            }, {
                field: 'name',
                title: '权限名'
            }, {
                field: 'description',
                title: '权限描述'
            }, {
                field: 'enabled',
                title: '是否可用',
                formatter: function (value) {
                    return isTrue(value) ? '可用' : '禁用';
                }
            }, {
                field: 'createTime',
                title: '创建时间',
                formatter: function (value) {
                    return $.format.date(value, 'yyyy-MM-dd HH:mm:ss');
                }
            }, {
                field: 'modifyTime',
                title: '修改时间',
                formatter: function (value) {
                    return $.format.date(value, 'yyyy-MM-dd HH:mm:ss');
                }
            }, {
                field: 'id',
                title: '操作',
                formatter: function (value) {
                    return '<a type="button" class="btn btn-primary btn-circle btn-sm" ' +
                        'href="/admin/auth/' + value + '">' +
                        '<i class="fa fa-pencil-square-o"></i>' +
                        '</a> &nbsp;' +
                        '<a type="button" class="btn btn-warning btn-circle btn-sm" ' +
                        'onclick="deleteOne(' + value + ')">' +
                        '<i class="fa fa-times"></i>' +
                        '</a>';
                }
            }]
        });
    };
    return oTableInit;
};


$(document).ready(function () {
    $(function () {
        //1.初始化Table
        let oTable = new TableInit();
        oTable.Init();

        //2.初始化Button的点击事件
        $('#btn_delete').click(deleteList);

    });
});

function deleteOne(authId) {
    let authIds = [authId];
    deleteAjax(authIds);
}

function deleteList() {
    let selected = $('#table-auth').bootstrapTable('getSelections');
    if (selected.length === 0) {
        sweetAlert('错误', '请选择要删除的权限', 'error');
        return false;
    }

    let authIds = [];
    $.each(selected, function (index, value) {
        authIds.push(value.id);
    });

    deleteAjax(authIds);
}

function deleteAjax(authIds) {
    swal({
            title: '确认删除记录?',
            type: 'warning',
            showCancelButton: true,
            closeOnConfirm: false,
            showLoaderOnConfirm: true
        },
        function () {
            $.ajax({
                type: 'DELETE',
                async: true,
                timeout: 100000,
                url: '/admin/auths',
                data: {
                    authIds: authIds
                },
                success: function (data) {
                    if (data.result === 0) {
                        swal('删除失败', data.msg, 'error');
                        return false;
                    }

                    swal('成功', data.msg, 'success');
                    goUrl("/admin/auths");
                },
                error: function (xhr) {
                    swal('错误', '服务器代码:' + xhr.status, 'error');
                }
            });
        });
}