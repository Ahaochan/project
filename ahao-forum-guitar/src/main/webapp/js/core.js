var ctx = $('meta[name="ctx"]').attr('content');

//==================封装Bootstrap file input的函数================================
var FileInput = function () {
    var File = {};
    File.initImg = function (option) {
        var options = $.extend({
            selector: undefined,
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
            uploadExtraData: { filePath: options.filePath},
            language: 'zh',
            allowedPreviewTypes: ['image'],
            allowedFileExtensions: ['jpg', 'jpeg', 'png', 'gif'],
            autoReplace: true,

            removeLabel: '',
            removeIcon: '<i class="glyphicon glyphicon-remove"></i>',
            removeTitle: 'Cancel or reset changes',

            // elErrorContainer: '#kv-avatar-errors-2',
            msgErrorClass: 'alert alert-block alert-danger',
            defaultPreviewContent: '<img src="' + ctx + '/img/default_avatar_male.jpg" alt="Your Avatar"><h6 class="text-muted">Click to select</h6>',
            layoutTemplates: {main2: '{preview} {remove} {browse}'},
        }, options.fileInputParam));
    };
    return File;
};
//==================封装Bootstrap file input的函数================================

$(function () {

    // 1. panel 伸缩按钮样式变化
    (function ($) {
        $('span[data-toggle="collapse"]').on('click', function () {
            $(this).toggleClass('glyphicon-triangle-top glyphicon-triangle-bottom');
        });
    })(jQuery);


});



Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] == obj) {
            return true;
        }
    }
    return false;
};