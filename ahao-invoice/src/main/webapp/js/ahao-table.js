(function ($) {

    var deleteAjax = function (url, queryParams) {
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
                    timeout: 100000,
                    url: url,
                    data: queryParams,
                    success: function (data) {
                        if (data&&data.result === 0) {
                            swal('删除失败', data&&data.msg, 'error');
                            return;
                        }

                        swal('成功', data&&data.msg, 'success');
                        $.goUrl(url);
                    },
                    error: function (xhr) {
                        swal('错误', '服务器代码:' + xhr.status, 'error');
                    }
                });
            });
    };

    var methods = {
        one: function (option) {
            console.log(option);
            var url = option.url, key = option.key,
                id = option.id;

            var queryParams = {};
            queryParams[key] = [id];
            deleteAjax(url, queryParams);
        },
        list: function (option) {
            var url = option.url, key = option.key, name = option.name,
                ids = $.getSelectIds($(option.table));

            if (ids.length === 0) {
                sweetAlert('错误', '请选择要删除的'+name, 'error');
                return;
            }
            var queryParams = {};
            queryParams[key] = ids;
            deleteAjax(url, queryParams);
        }
    };

    $.extend({
        getSelectIds : function ($table) {
            var selected = $table.bootstrapTable('getSelections');
            var ids = [];
            $.each(selected, function (index, value) {
                ids.push(value.id);
            });
            return ids;
        },
        deleteTable: function (method, option) {
            var options = $.extend({
                url: undefined,
                key: 'ids',
                ids: [],
                table: '#table',
                name: '记录'
            }, typeof option === 'object' && option);

            if(methods[method]){
                return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
            } else if ( typeof method === 'object' || ! method ) {
                return methods.init.apply( this, arguments );
            } else {
                $.error( 'Method ' +  method + ' does not exist on jQuery.tooltip' );
            }
        },
        bootstrapTable: function (option) {
            var options = $.extend({
                selector: '',
                url: '',
                rowStyle: undefined,
                columns: undefined,
                queryParams: function (params) {    //传递参数（*）
                    return {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                        pageSize: params.limit,   //页面大小
                        page: params.offset / params.limit,  //页码
                        order: params.order,
                        sort: params.sort
                    };
                },
                toolbar: '#toolbar',
                pagination: true,
                sidePagination: 'server', // client or server
                showColumns: true,
                contentType: 'application/x-www-form-urlencoded',
                uniqueId: 'id',
                sortName: 'id',
                striped: true,
                cache: false
            }, option);


            $(options.selector).bootstrapTable({
                selector: options.selector,
                url: options.url,
                rowStyle: options.rowStyle,
                columns: options.columns,
                queryParams: options.queryParams,
                toolbar: options.toolbar,
                pagination: options.pagination,
                sidePagination: options.sidePagination,
                showColumns: options.showColumns,
                contentType: options.contentType,
                uniqueId: options.uniqueId,
                sortName: options.sortName,
                striped: options.striped,
                cache: options.cache
            });
        }
    });

})(jQuery);