(function ($) {

    let deleteAjax = function (url, queryParams) {
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
    }

    let methods = {
        one: function (option) {
            console.log(option);
            let url = option.url, key = option.key,
                id = option.id;

            let queryParams = {};
            queryParams[key] = [id];
            deleteAjax(url, queryParams);
        },
        list: function (option) {
            let url = option.url, key = option.key, name = option.name,
                ids = $.getSelectIds($(option.table));

            if (ids.length === 0) {
                sweetAlert('错误', '请选择要删除的'+name, 'error');
                return;
            }
            let queryParams = {};
            queryParams[key] = ids;
            deleteAjax(url, queryParams);
        }
    };

    $.extend({
        getSelectIds : function ($table) {
            let selected = $table.bootstrapTable('getSelections');
            let ids = [];
            $.each(selected, function (index, value) {
                ids.push(value.id);
            });
            return ids;
        },
        deleteTable: function (method, option) {
            let options = $.extend({
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
        }
    });

})(jQuery);