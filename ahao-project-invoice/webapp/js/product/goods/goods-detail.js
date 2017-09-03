$(document).ready(function () {

    var id = $('#input-id').val();
    $.submitDetail({
        url: '/product/good/' + ((id === undefined) ? "" : id),
        go: '/product/goods',
        data: {
            id: '#input-id',
            name: '#input-name',
            categoryId: '#input-category-id',
            specification: '#input-specification',
            unit: '#input-unit',
            unitePrice: '#input-unitePrice',
            taxRate: '#input-taxRate'

        },
        rules: {
            name: {
                required: true,
                minlength: 2,
                normalizer: function (value) {
                    return $.trim(value);
                }
            },
            unitePrice: {
                required: true,
                number: true
            },
            taxRate:{
                required: true,
                number: true,
                max: 1
            }
        },
        messages: {
            name: {
                required: "货物名不能为空",
                minlength: "货物名长度过短",
            },
            unitePrice: {
                required: "单价不能为空",
                number: "单价必须为纯数字"
            },
            taxRate: {
                required: "税率不能为空",
                number: "税率必须为纯数字",
                max: "最大值不能超过{0}"
            }
        }
    });

    $('#modal-category-select').on('show.bs.modal', function (event) {
        $('#input-category-select').val('')
            .off('onDataRequestSuccess')
            .off('onSetSelectValue')
            .off('onUnsetSelectValue')
            .on('onSetSelectValue', function (e, keyword, data) {
                $('#modal-category-select').modal('hide');
                $('#input-category-id').val(data.id);
                $('#input-category-name').val(data.name);
            })
    });

    $('#input-category-select').bsSuggest({
        url: '/product/category/searchByName',
        getDataMethod: 'url',
        effectiveFields: ['id', 'name', 'description'],
        effectiveFieldsAlias:{name: '类别名称', description: '类别描述'},
        showHeader: true,
        autoSelect: true,
//        showBtn: true,
        idField: "id",
        keyField: "name",
        fnAdjustAjaxParam: function (keyword, options) {
            return {
                type: 'POST',
                data: {
                    name: $('#input-category-select').val()
                }
            }
        },
        fnProcessData: function () {

        }
    });
});


