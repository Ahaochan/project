'use strict';
$(document).ready(function () {
    //1.初始化Table
    $.bootstrapTable({
        selector: '#table-role',
        url: contextPath+'admin/roles/page',
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
                title: '角色Id'
            },
            {
                field: 'name',
                title: '角色名'
            },
            {
                field: 'description',
                title: '角色描述'
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
                    return '<a type="button" class="btn btn-primary btn-circle btn-sm" ' +
                        'href="'+contextPath+'admin/role/' + value + '">' +
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
            url: contextPath+'admin/roles',
            key: 'roleIds',
            table: '#table-role'
        })
    });

    $('body').on('click', '.btn-delete', function () {
        $.deleteTable('one', {
            url: contextPath+'admin/roles',
            key: 'roleIds',
            id: $(this).attr('data-id')
        });
    })
});