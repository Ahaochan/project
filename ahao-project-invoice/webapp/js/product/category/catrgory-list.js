$(document).ready(function () {

    // 初始化表格
    (function () {
        $('#table-auth').bootstrapTable({
            url: '/product/category/page',                 //请求后台的URL（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            columns: [
                {checkbox: true},
                {field: 'id', title: '类别Id'},
                {field: 'name', title: '类别名'},
                {field: 'description', title: '类别描述'},
                {field: 'createTime', title: '创建时间', formatter: function (value) {
                    return $.format.date(value, 'yyyy-MM-dd HH:mm:ss');
                }},
                {field: 'modifyTime', title: '修改时间', formatter: function (value) {
                    return $.format.date(value, 'yyyy-MM-dd HH:mm:ss');
                }},
                {field: 'id', title: '操作', formatter: function (value) {
                    return '<a type="button" class="btn btn-primary btn-circle btn-sm" ' +
                        'href="/product/category/' + value + '">' +
                        '<i class="fa fa-pencil-square-o"></i>' +
                        '</a> &nbsp;' +
                        '<a type="button" class="btn btn-warning btn-circle btn-sm" ' +
                        'onclick="deleteOne(' + value + ')">' +
                        '<i class="fa fa-times"></i>' +
                        '</a>';
                }}]
        });
    })();

    // 初始化删除事件
    (function () {
        $('#btn_delete').click(function () {
            $.deleteTable({
                method: 'list',
                url: '/product/category',
                key : 'categoryIds',
                table: '#table-category'
            });
        });
    })();
});




function deleteOne(id) {
    $.deleteTable({
        url: '/admin/auths',
        queryParams: {
            authIds: [id]
        }
    });
}

function deleteList($table, name) {
    let selected = $table.bootstrapTable('getSelections');
    if (selected.length === 0) {
        sweetAlert('错误', name&&'请选择要删除的'+name, 'error');
        return;
    }

    let ids = [];
    $.each(selected, function (index, value) {
        ids.push(value.id);
    });

    $.deleteTable({
        url: '/admin/auths',
        queryParams: {
            authIds: [ids]
        }
    });
}