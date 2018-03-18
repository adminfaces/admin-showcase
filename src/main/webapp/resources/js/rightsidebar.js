$(function () {

    'use strict'

    /**
     * Get access to plugins
     */

    $('[data-toggle="control-sidebar"]').controlSidebar()
    $('[data-toggle="push-menu"]').pushMenu()

    var $pushMenu = $('[data-toggle="push-menu"]').data('lte.pushmenu')
    var $controlSidebar = $('[data-toggle="control-sidebar"]').data('lte.controlsidebar')



    function changeLayout(cls) {
        $('body').toggleClass(cls)
        if ($('body').hasClass('fixed') && cls == 'fixed') {
            $pushMenu.expandOnHover()
        }
        $controlSidebar.fix()
    }

    /**
     * Retrieve default settings and apply them to the template
     *
     * @returns void
     */
    function setup() {

        // Add the layout manager
        $('[data-layout]').on('click', function () {
            changeLayout($(this).data('layout'))
        });

        $('[data-controlsidebar]').on('click', function () {
            changeLayout($(this).data('controlsidebar'))
            var slide = !$controlSidebar.options.slide

            $controlSidebar.options.slide = slide
            if (!slide) {
                $('.control-sidebar').removeClass('control-sidebar-open')
            }
        });

        $('[data-sidebarskin="toggle"]').on('click', function () {
            var $sidebar = $('.control-sidebar')
            if ($sidebar.hasClass('control-sidebar-dark')) {
                $sidebar.removeClass('control-sidebar-dark')
                $sidebar.addClass('control-sidebar-light')
            } else {
                $sidebar.removeClass('control-sidebar-light')
                $sidebar.addClass('control-sidebar-dark')
            }
        });

        $('[data-enable="expandOnHover"]').on('click', function () {
            $(this).attr('disabled', true)
            $pushMenu.expandOnHover()
            if (!$('body').hasClass('sidebar-collapse'))
                $('[data-layout="sidebar-collapse"]').click()
        });

        //  Reset options
        if ($('body').hasClass('fixed')) {
            $('[data-layout="fixed"]').attr('checked', 'checked')
        }
        if ($('body').hasClass('layout-boxed')) {
            $('[data-layout="layout-boxed"]').attr('checked', 'checked')
        }
        if ($('body').hasClass('sidebar-collapse')) {
            $('[data-layout="sidebar-collapse"]').attr('checked', 'checked')
        }

        $('#content').click(function () {
            $('.control-sidebar').removeClass('control-sidebar-open');
        })

        if($('body').hasClass('layout-top-nav')) {
           $('#horizontal-layout').prop('checked', false);
        } else {
            $('#horizontal-layout').prop('checked', true);
        }

    }

    setup();

    $(document).on("pfAjaxComplete", function () {
        setup();
    });
});

