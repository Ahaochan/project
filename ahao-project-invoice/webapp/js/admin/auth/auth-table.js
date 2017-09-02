$(document).ready(function () {
    //1.初始化Table
    $('#table-auth').bootstrapTable({
        url: '/admin/auths/page',                 //请求后台的URL（*）
        toolbar: '#toolbar',                //工具按钮用哪个容器
        rowStyle: function (row) {
            if (!row.enabled) {
                return {classes: 'danger'};
            }
            return '';
        },
        columns: [{
            checkbox: true
        },
            {
                field: 'id',
                title: '权限Id'
            },
            {
                field: 'name',
                title: '权限名'
            },
            {
                field: 'description',
                title: '权限描述'
            },
            {
                field: 'enabled',
                title: '是否可用',
                formatter: function (value) {
                    return !!value ? '可用' : '禁用';
                }
            },
            {
                field: 'createTime',
                title: '创建时间',
                formatter: function (value) {
                    return $.format.date(value, 'yyyy-MM-dd HH:mm:ss');
                }
            },
            {
                field: 'modifyTime',
                title: '修改时间',
                formatter: function (value) {
                    return $.format.date(value, 'yyyy-MM-dd HH:mm:ss');
                }
            },
            {
                field: 'id',
                title: '操作',
                formatter: function (value) {
                    return '<a class="btn btn-primary btn-circle btn-sm" ' +
                        'href="/admin/auth/' + value + '">' +
                        '<i class="fa fa-pencil-square-o"></i>' +
                        '</a> &nbsp;' +
                        '<a class="btn btn-warning btn-circle btn-sm btn-delete" ' +
                        'data-id="' + value + '">' +
                        '<i class="fa fa-times"></i>' +
                        '</a>';
                }
            }]
    });

    //2.初始化Button的点击事件
    $('#btn_delete_list').click(function () {
        $.deleteTable('list', {
            url: '/admin/auths',
            key: 'authIds',
            table: '#table-auth'
        })
    });

    $('body').on('click', '.btn-delete', function () {
        $.deleteTable('one', {
            url: '/admin/auths',
            key: 'authIds',
            id: $(this).attr('data-id')
        });
    });
});