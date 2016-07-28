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
    urlShortener
) {
    var TodoView = Backbone.View.extend({
        _viewModel: null,

        initialize: function() {
            this._template = urlShortener;
            this._viewModel = this._initViewModel();
        },

        render: function() {
            this._applyKnockoutBinding(this, this._viewModel, this._template);
        },

        _initViewModel: function() {

            function ToDoViewModel() {

            }

            return new ToDoViewModel();
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
