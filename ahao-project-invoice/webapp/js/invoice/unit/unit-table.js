$(document).ready(function () {
    'use strict';
    //1.初始化Table
    $.bootstrapTable({
        selector: '#table-unit',
        url: contextPath+'invoice/units/page',
        columns: [{
            checkbox: true
        },
            {
                'class': 'col-md-1',
                field: 'id',
                title: '单位id'
            },
            {
                'class': 'col-md-1',
                field: 'name',
                title: '单位名称'
            },
            {
                'class': 'col-md-2',
                field: 'taxId',
                title: '统一社会信用代码'
            },
            {
                'class': 'col-md-2',
                field: 'address',
                title: '地址',
                formatter: function (value, row) {
                    var taxId = row['taxId'];
                    return $.province({taxId: taxId})+value;
                }
            },
            {
                'class': 'col-md-1',
                field: 'tel',
                title: '联系电话'
            },
            {
                'class': 'col-md-2',
                field: 'account',
                title: '银行帐号'
            },
            {
                'class': 'col-md-1',
                field: 'createTime',
                title: '创建时间',
                formatter: function (value) {
                    return $.format.date(value, 'yyyy-MM-dd');
                }
            },
            {
                'class': 'col-md-1',
                field: 'modifyTime',
                title: '修改时间',
                formatter: function (value) {
                    return $.format.date(value, 'yyyy-MM-dd');
                }
            },
            {
                'class': 'col-md-1',
                field: 'id',
                title: '操作',
                formatter: function (value) {
                    return '<a type="button" class="btn btn-primary btn-circle btn-sm" ' +
                        'href="'+contextPath+'invoice/unit/' + value + '">' +
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
            url: contextPath+'invoice/units',
            key: 'unitIds',
            table: '#table-unit'
        })
    });

    $('body').on('click', '.btn-delete', function () {
        $.deleteTable('one', {
            url: contextPath+'invoice/units',
            key: 'unitIds',
            id: $(this).attr('data-id')
        });
    })
});