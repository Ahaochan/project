
$(document).ready(function () {
    'use strict';
    var oldCode = $('#input-code').val();
    var oldNumber = $('#input-number').val();

    $('#input-code').on('blur', function () {
        var code = $(this).val();
        var province = $.province({invoiceCode: code});
        // 4400163320
        $('#title-invoice').html(province+'增值税发票');
    }).blur();

    // 购销单位的模态框js
    (function ($) {
        $('#modal-unit-select').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);

            var title = button.data('title');
            $('#modal-unit-select-title').text('请选择' + title);

            $('#input-tax').val('')
                .off('onDataRequestSuccess')
                .off('onSetSelectValue')
                .off('onUnsetSelectValue')
                .on('onSetSelectValue', function (e, keyword, data) {
                    $('#modal-unit-select').modal('hide');
                    $('#input-unit-id').val(data.id);
                    $('#input-unit-name').val(data.name);
                    $('#input-unit-tax').val(data.taxId);
                    $('#input-unit-address').val($.province({taxId: data.taxId}) + data.address);
                    $('#input-unit-tel').val(data.tel);
                    $('#input-unit-bank-account').val(data.account);
                })
        });

        $('#input-tax').bsSuggest({
            url: contextPath+'invoice/unit/tax/',
            getDataMethod: 'url',
            effectiveFields: ['id', 'taxId', 'name', 'account'],
            effectiveFieldsAlias: {id: 'id', taxId: '社会统一信用代码', name: '单位名称', account: '银行帐号'},
            showHeader: true,
            autoSelect: true,
            idField: "id",
            keyField: "taxId",
            fnAdjustAjaxParam: function (keyword, options) {
                return {
                    type: 'POST',
                    data: {
                        taxId: $('#input-tax').val()
                    }
                }
            }
        });
    })(jQuery);

    // 货物选择的模态框js
    (function ($, Nzh) {
        $('#modal-goods-select').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);

            var title = button.data('title');
            $('#modal-goods-select-title').text('请选择' + title);

            var name = button.data('name');
            // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
            // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.

            $('#input-goods').val('')
                .off('onDataRequestSuccess')
                .off('onSetSelectValue')
                .off('onUnsetSelectValue')
                .on('onSetSelectValue', function (e, keyword, data) {
                    $('#modal-goods-select').modal('hide');
                    var id = data.id;
                    $('#input-goods-id').val(id);

                    // 添加表格内容
                    $('#div-goods').empty()
                        .append('<tr>' +
                            '<td class="col-md-2" colspan="2">'+data.name+'</td>' +
                            '<td class="col-md-1">'+data.specification+'</td>' +
                            '<td class="col-md-1">'+data.unit+'</td>' +
                            '<td class="col-md-1"><input type="number" id="input-goods'+id+'-number" data-id="'+id+'"></td>' +
                            '<td class="col-md-1" id="td-goods'+id+'-unite-price" data-id="'+id+'">'+data.unitePrice+'</td>' +
                            '<td class="col-md-1" id="td-goods'+id+'-price" data-id="'+id+'"></td>' +
                            '<td class="col-md-1" id="td-goods'+id+'-tax-rate" data-id="'+id+'">'+data.taxRate+'</td>' +
                            '<td class="col-md-1" id="td-goods'+id+'-tax-money" data-id="'+id+'"></td>' +
                            '</tr>');

                    $('#input-goods'+id+'-number').off('change').on('change', function () {
                        var $this = $(this);
                        var id =  $this.attr('data-id');
                        var num = $(this).val();
                        // 总价
                        var unitPrice = $('#td-goods'+id+'-unite-price').text();
                        var price = parseFloat((num*unitPrice).toFixed(2));
                        $('#td-goods'+id+'-price').text(price);
                        // 税额
                        var taxRate = $('#td-goods'+id+'-tax-rate').text();
                        var taxMoney = parseFloat((price*taxRate).toFixed(2));
                        $('#td-goods'+id+'-tax-money').text(taxMoney);
                        // 价税合计
                        var total = (price+taxMoney).toFixed(2);
                        $('#td-goods-total-zh').text(Nzh.encodeB(total));
                        $('#td-goods-total-num').text(total.toLocaleString());
                    });

                });
        });

        $('#input-goods').bsSuggest({
            url: contextPath+'product/good/searchByName',
            getDataMethod: 'url',
            effectiveFields: ['id', 'name', 'unitePrice', 'taxRate', 'createTime'],
            effectiveFieldsAlias: {name: '货物名称', unitePrice: '单价', taxRate: '税率', createTime: '创建时间'},
            showHeader: true,
            autoSelect: true,
            idField: "id",
            keyField: "name",
            fnAdjustAjaxParam: function (keyword, options) {
                return {
                    type: 'POST',
                    data: {
                        name: $('#input-goods').val()
                    }
                }
            },
            fnProcessData: function (result) {
                var value = result.value;
                for(var i in value){
                    if(value.hasOwnProperty(i)) {
                        var createTime = new Date(value[i].createTime);
                        value[i].createTime = $.format.date(createTime, 'yyyy-MM-dd HH:mm:ss');
                    }
                }
                return result;
            }
        });
    })(jQuery, Nzh);


    $('form').validate({
        rules: {
            unit: {
                required: true
            },
            code: {
                required: true,
                rangelength: [10, 10],
                normalizer: function (value) {
                    return $.trim(value);
                }
            },
            number: {
                required: true,
                rangelength: [8, 8],
                remote: {
                    param:{
                        url : contextPath+'invoice/check',
                        method : 'post',
                        delay: 2000,
                        data:{
                            code: function () {
                                return $('#input-code').val();
                            },
                            number: function () {
                                return $('#input-number').val();
                            }
                        }
                    },
                    depends: function (element) {
                        return !!$('#input-code') &&
                            oldCode !== $('#input-code').val() && oldNumber!==$('#input-number').val();
                    }
                },
                normalizer: function (value) {
                    return $.trim(value);
                }
            },
            type: {
                required: true
            },
            date: {
                required: true
            },
            goods:{
                required: true
            }
        },
        messages: {
            unit: {
                required: "请选择购销单位"
            },
            // TODO 验证发票代码和发票号码后, 会不显示其他错误
            code: {
                required: "发票代码不能为空",
                rangelength: "发票代码长度必须为{0}",
                remote: "发票代码或发票号码验证错误, 请检查格式或者是否有重复发票"
            },
            number: {
                required: "发票号码不能为空",
                rangelength: "发票号码长度必须为{0}",
                remote: "发票代码或发票号码验证错误, 请检查格式或者是否有重复发票"
            },
            type: {
                required: "必须选择发票类型"
            },
            date: {
                required: "必须填写开票日期"
            },
            goods: {
                required: "必须选择货物"
            }
        },
        errorClass: 'has-error',
        validClass: 'has-success',
        onfocusout : false,
        onkeyup: false,
        onclick: false,
        highlight: function (element, errorClass, validClass) {
            var $group = $(element).parent().parent();
            if($group.hasClass('form-group')){
                $group.attr('class', 'form-group '+errorClass);
            }
        },
        unhighlight: function (element, errorClass, validClass) {
            var $group = $(element).parent().parent();
            if($group.hasClass('form-group')){
                $group.attr('class', 'form-group '+validClass);
            }
        },
        errorPlacement: function(error, element) {
        },
        showErrors: function(errorMap, errorList) {
            var errors = [];
            for(var i in errorMap){
                if(errorMap.hasOwnProperty(i)) {
                    errors.push(errorMap[i]);
                }
            }
            swal({
                title: '你有 '+ this.numberOfInvalids() + ' 个错误信息',
                text: errors.join("\n"),
                type: 'warning'
            });
            this.defaultShowErrors();
        },
        submitHandler:function(form){
            swal({
                    title: '确认保存记录?',
                    type: 'info',
                    showCancelButton: true,
                    closeOnConfirm: false,
                    showLoaderOnConfirm: true
                },
                function () {
                    $('.form-group').attr('class', 'form-group');

                    var goodsId =  $('#input-goods-id').val();
                    var invoiceId = $('#input-invoice-id').val();
                    $.ajax({
                        type: 'POST',
                        timeout: 100000,
                        url: contextPath+'invoice/'+(invoiceId || ''),
                        dataType: 'json',
                        data: {
                            id: invoiceId,
                            code: $('#input-code').val(),
                            number: $('#input-number').val(),
                            date: $('#input-date').val(),
                            sell: parseInt($('#select-type').val()) !== 0,
                            unitId: $('#input-unit-id').val(),
                            goods: goodsId,
                            goodsNumber: $('#input-goods'+goodsId+'-number').val()
                        },
                        success: function (data) {
                            var json = data;
                            if (json.result === 0) {
                                swal('失败', json.msg, 'error');
                                var fields = json.obj;
                                var msg = [];
                                for (var property in fields) {
                                    if (fields.hasOwnProperty(property)) {
                                        var errorMsg = fields[property].join("\n");
                                        msg.push(errorMsg);
                                    }
                                }
                                swal({
                                    title: '你有 '+ msg.length + ' 个错误信息',
                                    text: msg.join("\n"),
                                    type: 'warning'
                                });
                                return;
                            }

                            swal('成功', json.msg, 'success');
                            $.goUrl(contextPath+'invoices');
                        }
                    });
                });
        }
    });
});