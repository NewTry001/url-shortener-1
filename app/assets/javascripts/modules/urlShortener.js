/**
 * Created by Haoji on 2016-07-27.
 */
define([
    "jquery",
    "backbone",
    "knockout",
    "underscore",
    "text!templates/urlShortener.ko.html"
], function(
    $,
    Backbone,
    ko,
    _,
    urlShortenerTemplate
) {
    var UrlShortenerView = Backbone.View.extend({
        _viewModel: null,

        initialize: function() {
            this._template = urlShortenerTemplate;
            this._viewModel = this._initViewModel();
        },

        render: function() {
            this._applyKnockoutBinding(this, this._viewModel, this._template);
            this._bindKeyEvents();
        },

        _bindKeyEvents: function() {
            var that = this;
            $('.url-input').keypress(_.bind(that.inputKeypressHandler, that));
        },

        _initViewModel: function() {
            var that = this;

            function UrlShortenerViewModel() {
                var self = this;

                // All the knockout bindings are initialized here
                this.inputPlaceHolder = "Paste your URL here";
                this.urlInput = ko.observable("");
                this.canShowResult = ko.observable(false);
                this.fullUrl = ko.observable("");
                this.shortUrl = ko.observable("");
                this.isInputDisabled = ko.observable(false);

                // Prevent the empty URL
                this.isButtonDisabled = ko.computed(function(){
                    return self.urlInput().trim().length === 0;
                });
                this.shortenClick = function() {
                    if (!self.isButtonDisabled()) {
                        return that.submitUrl();
                    }
                };
            }

            return new UrlShortenerViewModel();
        },

        inputKeypressHandler: function(event) {
            var that = this;
            // if it is an enter key
            if(event.keyCode === 13 && !that._viewModel.isButtonDisabled()) {
                that.submitUrl();
            }
        },

        submitUrl: function() {
            var that = this;
            // TODO: Add some validation of the input URL
            var url = this._viewModel.urlInput();
            var data = {
                url: url
            };

            // empty the input box, and it will also disable the button
            that._viewModel.urlInput("");

            // disable the input when the request is processing
            this._viewModel.isInputDisabled(true);

            // post request to server
            $.ajax({
                type: "POST",
                url: "/api/shorten",
                contentType: "application/json",
                async: true,
                data: JSON.stringify(data)
            }).done(function(response) {
                var currentUrlPrefix = window.location.href;
                var hash = response.hash;

                // re-enable the input box
                that._viewModel.isInputDisabled(false);

                // show the shortened result
                that._viewModel.canShowResult(true);
                that._viewModel.fullUrl(url);
                that._viewModel.shortUrl(currentUrlPrefix + hash);
            });
        },

        _applyKnockoutBinding: function(thisView, thisViewModel, template) {
            thisView.$el.empty();
            thisView.$el.append(template);
            ko.cleanNode(thisView.$el[0]);
            ko.applyBindings(thisViewModel, thisView.$el[0]);
        }
    });

    return function() {
        $('body').prepend("<div class='url-shortener-container'></div>");
        new UrlShortenerView({el: $('.url-shortener-container')}).render();
    };
});
