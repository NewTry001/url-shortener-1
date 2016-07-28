/**
 * Created by Haoji on 2016-07-27.
 */
(function () {
    require.config({
        baseUrl: "/assets/javascripts",
        paths: {
            "backbone": "libs/backbone/backbone-min",
            "jquery": "libs/jquery/jquery-2.1.4.min",
            "knockout": "libs/knockout/knockout-3.3.0",
            "lodash": "libs/lodash/lodash",
            "text": "libs/requirejs-text/text"
        },
        map: {
            "*": {
                "underscore": "lodash"
            }
        },
        shim: {
            // -- for easier reading/searching, please keep this list in ALPHABETICAL order --
            "backbone": {
                deps: ["lodash", "jquery"],
                exports: "Backbone"
            }
        }
    });
}).call(this);
