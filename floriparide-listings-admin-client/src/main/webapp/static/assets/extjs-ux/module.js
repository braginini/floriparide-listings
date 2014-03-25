(function() {
    Ext.define('Ext.ux.Module',{
        extend: 'Ext.app.Application',
        alternateClassName: ['Ext.app.Module', 'Ext.Module']
    });

    Ext.override(Ext.app.Application, {
        modulesFolder: null,

        onBeforeLaunch: function() {
            var i, ln, modules, requires, paths, name;

            modules = Ext.Array.from(this.modules);
            this.modules = new Ext.util.MixedCollection();
            this.modulesFolder = this.modulesFolder ? this.appFolder + '/' + this.modulesFolder : this.appFolder;

            if (modules.length) {

                requires = [];
                paths = {};
                for (i = 0, ln = modules.length; i < ln; i++) {
                    name = modules[i];
                    var moduleNameSpace = this.$namespace + '.' + name;
                    var className = moduleNameSpace + '.Bootstrap';

                    this.modules.add(name, className);
                    paths[moduleNameSpace] = this.modulesFolder + '/' + name;

                    if (!Ext.ClassManager.isCreated(className)) {
                        requires.push(className);
                    }
                }

                Ext.Loader.setPath(paths);

                if (requires.length) {
                    Ext.require(requires);
                }

//                this.callOverridden();

                for (i = 0, ln = modules.length; i < ln; i++) {
                    name = modules[i];
                    this.modules.add(name, this.createModule(name, this.modules.get(name)));
                }
                this.callOverridden();
            } else {
                this.callOverridden();
            }
        },

        createModule: function(name, className) {
            var moduleFolder, module;
            moduleFolder = this.appFolder ? this.appFolder + '/' : '';
            moduleFolder += (this.modulesFolder ? this.modulesFolder + '/' : '') + name;
            module = Ext.create(className, {
                application: this.application,
                id: name,
                name: this.name + '.' + name,
                appFolder: moduleFolder
            });
            this.initModule(module, name);
            return module;
        },

        initModule: Ext.emptyFn,

        getModule: function(name, create) {
            var module;
            module = this.modules.get(name);
            if(!module && create) {
                module = this.createModule(name);
                this.modules.add(name, module);
            }
            return module;
        }
    });
}());