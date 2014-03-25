Ext.application({
    name: 'App',
    modules: ['admin'],
    appFolder: '/app',

    menu: null,

    onBeforeLaunch: function() {
        this.menu = {
            main: [],
            side: []
        };

        this.stores = {};

        this.callParent();
    },

    launch: function () {
        "use strict";
        var me = this;
        var i, links;

        Ext.create('Ext.container.Viewport', {
            layout: 'border',
            items: [
                this.createSideMenu({
                    region: 'west',
                    width: 250
                }), this.getContent()
            ]
        });

        Deft.Injector.configure({
            '$store.*': function(scope, name) {
                name = name.substring(6);
                return me.getStore(name);
            }
        });

        Ext.History.html5Mode = true;
        Ext.History.hashPrefix = '!';
        Ext.Router.init();

        var linkRedirect = function(e, dom) {
            e.stopEvent();
            Ext.Router.redirect(dom.getAttribute('href'));
        }

        links = Ext.DomQuery.select('a');
        for(i=0; i<links.length;i++) {
            links[i].wrapped = true;
            Ext.fly(links[i]).on('click', linkRedirect);
        }

        Ext.getDoc().on('animationstart', function(e, link) {
            if (!link.wrapped && e.browserEvent.animationName == 'nodeInserted') {
                link.wrapped = true;
                if (link.getAttribute('href')) {
                    Ext.fly(link).on('click', linkRedirect);
                }
            }
        });


        Ext.fly('loading-mask').remove();
        Ext.fly('loading').remove();
    },

    initModule: function(module) {
        if (module.mainMenu) {
            this.menu.main = this.menu.main.concat(module.mainMenu);
        }

        if (module.sideMenu) {
            this.menu.side = this.menu.side.concat(module.sideMenu);
        }
    },

    createSideMenu: function(cfg) {
        var store = new Ext.data.TreeStore({
            root: {
                expanded: true,
                children: this.menu.side
            }
        });

        var menu = Ext.create('Ext.tree.Panel', Ext.apply(cfg, {
            bodyCls: 'main-menu',
            rootVisible: false,
            useArrows: true,
            border: false,
            store: store,

            listeners: {
                select: function(tree, r) {
                    if (r.data.href) {
                        Ext.Router.redirect(r.data.href);
                    }
                }
            }
        }));

        var find_record = function(r, href) {
            if (r.data.href != '' && Ext.String.startsWith(href, r.data.href)) {
                return r;
            }

            if (r.childNodes.length) {
                for (var i=0; i<r.childNodes.length; i++) {
                    var res = find_record(r.childNodes[i], href);
                    if (res) {
                        return res;
                    }
                }
            }
            return null;
        }

        var sync_menu = function(path) {
            if (!path) {
                path = Ext.History.getToken();
            }
            var r = menu.getSelectionModel().getLastSelected();
            if (!r || (r.data.href !== path)) {
                r = find_record(menu.getRootNode(), path);
                if (r) {
                    menu.getSelectionModel().select(r, false, true);
                }
            }
        };

        Ext.History.on('change', sync_menu, this, {buffer: 500});
//        Ext.TaskManager.start({
//            run: sync_menu,
//            args: [],
//            interval: 500
//        });
        menu.on('selectionchange', function(tree, rs) {
            if (!rs.length) {
                alert('ahtung');
            }
        }, this);

        return menu;
    },

    getModel: function(name) {
        return Ext.ClassManager.getByAlias('model.' + name);
    },

    getStore: function(name) {
        if (!this.stores[name]) {
            this.stores[name] = new Ext.data.Store({
                paramsAsHash: true,
                model: this.getModel(name),
                remoteSort: true,
                pageSize: 100
            });
        }
        return this.stores[name];
    },

    getContent: function() {
        if (!this.content) {
            this.content = Ext.create('Ext.panel.Panel', {
                region: 'center',
                layout: 'card',
                border: false
            });
        }
        return this.content;
    },

    show: function(component) {
        if (!this.content.child(component)) {
            this.content.add(component);
        }
        this.content.getLayout().setActiveItem(component);
    }
});