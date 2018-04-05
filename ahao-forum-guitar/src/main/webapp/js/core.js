var ctx = $('meta[name="ctx"]').attr('content');

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