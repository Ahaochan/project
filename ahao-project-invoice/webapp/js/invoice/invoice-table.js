$(document).ready(function () {
    'use strict';
    //1.初始化Table
    $.bootstrapTable({
        selector: '#table-invoice',
        url: '/invoice/page',
        columns: [{
            checkbox: true
        },
            {
                field: 'id',
                title: '发票Id'
            },
            {
                field: 'name',
                title: '货物名称'
            },
            {
                field: 'price',
                title: '价钱'
            },
            {
                field: 'type',
                title: '发票类型',
                formatter: function (value) {
                    return !!value ? '销项' : '进项';
                }
            },
            {
                field: 'date',
                title: '开票日期',
                formatter: function (value) {
                    return $.format.date(value, 'yyyy-MM-dd HH:mm:ss');
                }
            },
            {
                field: 'id',
                title: '操作',
                formatter: function (value) {
                    return '<a class="btn btn-primary btn-circle btn-sm" ' +
                        'href="/invoice/' + value + '">' +
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
            url: '/invoices',
            key: 'invoiceIds',
            table: '#table-invoice'
        })
    });

    $('body').on('click', '.btn-delete', function () {
        $.deleteTable('one', {
            url: '/invoices',
            key: 'invoiceIds',
            id: $(this).attr('data-id')
        });
    });
});