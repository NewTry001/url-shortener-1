/**
 * Created by Haoji on 2016-07-27.
 */
define([
    "jquery",
    "backbone",
    "knockout",
    "text!templates/urlShortener.ko.html"
], function(
    $,
    Backbone,
    ko,
    urlShortenerTemplate
) {
    var TodoView = Backbone.View.extend({
        _viewModel: null,

        initialize: function() {
            this._template = urlShortenerTemplate;
            this._viewModel = this._initViewModel();
        },

        render: function() {
            this._applyKnockoutBinding(this, this._viewModel, this._template);
        },

        _initViewModel: function() {
            var that = this;

            function UrlShortenerViewModel() {
                this.inputPlaceHolder = "Enter URL here";
                this.urlInput = ko.observable("");
                this.canShowResult = ko.observable(false);
                this.fullUrl = ko.observable("");
                this.shortUrl = ko.observable("");
                this.shortenClick = function() {
                  return that.submitUrl();
                };
            }

            return new UrlShortenerViewModel();
        },

        submitUrl: function() {
            var that = this;
            var url = this._viewModel.urlInput();
            var data = {
                url: url
            };

            // post request to server
            $.ajax({
                type: "POST",
                url: "/shorten",
                contentType: "application/json",
                async: true,
                data: JSON.stringify(data)
            }).done(function(response) {
                var currentUrlPrefix = window.location.href + "url/";
                var hash = response.hash;
                that._viewModel.urlInput("");
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
        new TodoView({el: $('body')}).render();
    };
});
