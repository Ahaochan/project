$(document).ready(function () {
    //1.初始化Table
    $('#table-goods').bootstrapTable({
        url: '/product/goods/page',                 //请求后台的URL（*）
        toolbar: '#toolbar',                //工具按钮用哪个容器
        columns: [{
            checkbox: true
        },
            {
                field: 'id',
                title: '货物Id'
            },
            {
                field: 'name',
                title: '货物名'
            },
            {
                field: 'category',
                title: '类别名称'
            },
            {
                field: 'specification',
                title: '规格型号'
            },
            {
                field: 'unit',
                title: '单位'
            },
            {
                field: 'unitePrice',
                title: '单价'
            },
            {
                field: 'taxRate',
                title: '税率',
                formatter: function (value) {
                    return $.percentage(value);
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
                        'href="/product/good/' + value + '">' +
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
            url: '/product/goods',
            key: 'goodIds',
            table: '#table-goods'
        })
    });

    $('body').on('click', '.btn-delete', function () {
        $.deleteTable('one', {
            url: '/product/goods',
            key: 'goodIds',
            id: $(this).attr('data-id')
        });
    });
});