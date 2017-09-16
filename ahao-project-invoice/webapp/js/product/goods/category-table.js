$(document).ready(function () {
    'use strict';
    //1.初始化Table
    $.bootstrapTable({
        selector: '#table-category',
        url: '/product/categorys/page',
        columns: [{
            checkbox: true
        },
            {
                field: 'id',
                title: '类别Id'
            },
            {
                field: 'name',
                title: '类别名'
            },
            {
                field: 'description',
                title: '类别描述'
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
                        'href="/product/category/' + value + '">' +
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
            url: '/product/categorys',
            key: 'categoryIds',
            table: '#table-category'
        })
    });

    $('body').on('click', '.btn-delete', function () {
        $.deleteTable('one', {
            url: '/product/categorys',
            key: 'categoryIds',
            id: $(this).attr('data-id')
        });
    });
});