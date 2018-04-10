var ctx = $('meta[name="ctx"]').attr('content');

//==================封装Bootstrap file input的函数================================
var FileInput = function () {
    var File = {};
    File.initImg = function (option) {
        var options = $.extend({
            selector: undefined,
            placeholderImg: ctx + '/img/default_avatar_male.jpg',
            filePath: '',
            fileInputParam: {},
        }, option);
        var selector = options.selector;
        if (typeof selector !== 'string') {
            console.error('请输入string参数!');
            return;
        }

        return $(selector).fileinput($.extend({
            overwriteInitial: true,
            maxFileCount: 1,
            maxFileSize: 5000,
            showClose: false,
            showRemove: false,
            showCaption: false,
            showBrowse: false,
            browseOnZoneClick: true,
            uploadUrl: ctx + '/upload/img',
            uploadExtraData: {filePath: options.filePath},
            language: 'zh',
            allowedPreviewTypes: ['image'],
            allowedFileExtensions: ['jpg', 'jpeg', 'png', 'gif'],
            autoReplace: true,

            removeLabel: '',
            removeIcon: '<i class="glyphicon glyphicon-remove"></i>',
            removeTitle: 'Cancel or reset changes',

            // elErrorContainer: '#kv-avatar-errors-2',
            msgErrorClass: 'alert alert-block alert-danger',
            defaultPreviewContent: '<img src="' + options.placeholderImg + '" alt="Your Avatar"><h6 class="text-muted">Click to select</h6>',
            layoutTemplates: {main2: '{preview} {remove} {browse}'},
        }, options.fileInputParam));
    };
    return File;
};
//==================封装Bootstrap file input的函数================================

//==================封装wangEditor富文本编辑器的函数===============================
var RichEditor = function () {
    var Editor = {};
    var editor;
    Editor.init = function (option) {
        var options = $.extend({
            uploadImgUrl: ctx + '/upload/img',
            selector: undefined,
            filePath: 'editor',
        }, option);
        var selector = options.selector;
        if (typeof selector !== 'string') {
            console.error('请输入string参数!');
            return;
        }

        editor = new window.wangEditor(selector);
        editor.customConfig.uploadImgServer = ctx + '/upload/img';
        editor.customConfig.uploadImgParams = {filePath: options.filePath};
        editor.customConfig.uploadFileName = 'file';
        editor.customConfig.customAlert = function (info) {
            swal(info);
        };
        editor.customConfig.uploadImgHooks = {
            // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
            // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）
            customInsert: function (insertImg, json, editor) {
                // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
                // insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果

                // 举例：假如上传图片成功后，服务器端返回的是 {url:'....'} 这种格式，即可这样插入图片：
                if (!json.result) {
                    swal({type: 'warning', title: '警告', text: '上传图片失败'});
                    return;
                }
                var url = json.obj.url;
                if (!/^http.*/.test(url)) {
                    url = ctx + '/' + url;
                }
                insertImg(url)
                // result 必须是一个 JSON 格式字符串！！！否则报错
            }
        }
        editor.create();
    };
    Editor.html = function () {
        return editor.txt.html();
    };
    Editor.text = function () {
        return editor.txt.text();
    };
    return Editor;
};
//==================封装wangEditor富文本编辑器的函数===============================

$(function () {

    // 1. panel 伸缩按钮样式变化
    (function ($) {
        $('span[data-toggle="collapse"]').on('click', function () {
            $(this).toggleClass('glyphicon-triangle-top glyphicon-triangle-bottom');
        });
    })(jQuery);


});


Array.prototype.contains = function (obj) {
    var i = this.length;
    while (i--) {
        if (this[i] == obj) {
            return true;
        }
    }
    return false;
};