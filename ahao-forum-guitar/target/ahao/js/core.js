$(function () {
    (function ($) {
        $('.glyphicon-triangle-bottom').on('click', function () {
           $(this).removeClass('glyphicon-triangle-bottom')
               .addClass('glyphicon-triangle-top');
        });
        $('.glyphicon-triangle-top').on('click', function () {
            $(this).removeClass('glyphicon-triangle-top')
                .addClass('glyphicon-triangle-bottom');
        });
    })(jQuery)
});