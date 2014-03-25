/*global App, module*/
(function () {
    "use strict";
    function getIncludeFiles() {
        return {
            js: [
                '/assets/extjs/ext-all-debug.js',
                '/assets/deft/deft-debug.js',
                '/assets/extjs-ux/module.js',
                '/assets/extjs-ux/history.js',
                '/assets/extjs-ux/routable.js',
                '/assets/extjs-ux/router.js',
                '/app/App.js',
                '/app/widget/ModelGrid.js',
                '/app/widget/ModelForm.js',
                '/app/model/BaseModel.js',
                '/app/model/Branch.js',
                '/app/model/Project.js',
                '/app/core/controller/ModelController.js',
                '/app/admin/Bootstrap.js'
            ],
            css: [
                '/assets/extjs/resources/css/ext-all-neptune.css',
                '/css/bootstrap.css',
                '/css/font-awesome.css',
                '/css/app.css'
            ]
        };
    }

    if (typeof App !== "undefined") {
        var files = getIncludeFiles(), i, file;

        for (i = 0; i < files.css.length; i++) {
            file = files.css[i];
            document.write('<link rel="stylesheet" type="text/css" href="' + file + '"/>');
        }

        if (App.Config.debug) {
            for (i = 0; i < files.js.length; i++) {
                file = files.js[i];
                document.write('<script src="' + file + '" type="text/javascript"></script>');
            }
        } else {
            var version = "@@version";
            document.write('<script src="build/build' + version + '.min.js" type="text/javascript"></script>');
        }
    }

    if (typeof module !== "undefined") {
        module.exports.getIncludeFiles = getIncludeFiles;
    }
}());