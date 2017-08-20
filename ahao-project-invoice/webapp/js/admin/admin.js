(function ($) {
    $.extend({
        initPanel: function (option) {
            let options = $.extend({
                url: undefined,
                data: {},
                panel: '#panel',
                inputName: ''
            }, option);

            let url = options.url, data = options.data;

            $.ajax({
                type: 'GET',
                contentType: 'application/json',
                timeout: 100000,
                data: data,
                url: url,
                dataType: 'json',
                success: function (data) {
                    let panel = options.panel, name = options.inputName;

                    let $panel = $(panel);
                    $.each(data.obj, function (index, entry) {
                        let $div = $('<div class="checkbox"></div>');

                        if (!$.isTrue(entry.enabled)) {
                            $div.addClass('has-error');
                        }

                        let $checkbox = $('<input type="checkbox" name="' + name + '" value="' + entry.id + '"/>');
                        if (!$.isTrue(entry.enabled)) {
                            $checkbox.attr('disabled', 'disabled');
                        }
                        if ($.isTrue(entry.selected)) {
                            $checkbox.attr('checked', 'checked');
                        }

                        $div.append($('<label></label>').append($checkbox).append(entry.name));
                        $panel.append($div);
                    })
                }
            });
        }
    });

})(jQuery);