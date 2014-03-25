Ext.define('App.admin.Bootstrap', {
    extend: 'Ext.Module',

    _controllers: null,

    sideMenu: [{
        text: 'Projects',
        href: '/project',
        iconCls: 'app-icon-city',
        expanded: true,
        children: [{
            href: '/branch',
            iconCls: 'app-icon-bank',
            text: 'Branches',
            leaf: true
        }]
    }],

    launch: function() {
        "use strict";
        this._controllers = {};

        this.route('/project(/*path)', function(params) {
            this.getController('project').parse(params.path || '/');
        });

        this.route('/branch(/*path)', function(params) {
            this.getController('branch').parse(params.path || '/');
        });
    },

    getController: function(name) {
        if (!this._controllers[name]) {
            var controller = new App.core.controller.ModelController({
                model: name,
                routeBasePath: '/' + name
            });
            this._controllers[name] = controller;
        }
        return this._controllers[name];
    }
});