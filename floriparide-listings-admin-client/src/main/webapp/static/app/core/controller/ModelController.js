Ext.define('App.core.controller.ModelController', {

    mixins: {
        routable: 'Ext.ux.mvc.Routable'
    },

    config: {
        model: null,
        grid: null,
        form: null,
        store: null
    },

    gridCfg: null,

    textSave: 'Save',
    textCancel: 'Cancel',

    constructor: function(config) {
        this.mixins.routable.constructor.call(this, config);
        this.initConfig(config);

        if (Ext.isString(this.model)) {
            if (!this.store) {
                this.store = Deft.Injector.resolve('store.'+this.model);
            }
            this.model = Ext.ClassManager.getByAlias('model.' + this.model);
        }

        this.init();
    },

    init: function() {
        this.route('/', this.show, this);
        this.route('/edit/:id', function(params, state){
            var r =  this.store.getById(parseInt(params.id));
            if (!r) {
                if (state && state.record) {
                    r = this.model.create(state.record);
                } else {
                    this.model.load(params.id, {
                        scope: this,
                        success: function(r) {
                            this.edit(r);
                        }
                    });
                    return;
                }
            }
            this.edit(r);
        }, this);
    },

    show: function() {
        App.app.show(this.getGrid());
    },

    edit: function(r) {
        var form = this.getForm();
        form.setTitle('Edit '+this.model.prototype.title+': '+ r.internalId);
        form.getForm().loadRecord(r);
        App.app.show(form);
    },

    getGrid: function() {
        if (!this.grid) {
            this.grid = Ext.create('App.widget.ModelGrid', Ext.apply({
                model: this.model,
                store: this.store
            }, this.gridCfg || {}));

            this.grid.mon(this.grid, 'editaction', function(r) {
                this.redirect('/edit/'+ r.internalId, {record: r.data});
            }, this);
        }
        return this.grid;
    },

    getForm: function() {
        if (!this.form) {
            this.form = Ext.create('App.widget.ModelForm', {
                model: this.model,
                buttons:[{
                    text: this.textSave,
                    handler: function(){
                        this.onSubmit();
                    },
                    scope: this
                }, {
                    text: this.textCancel,
                    handler: function() {
                        var form = this.form.getForm();
                        if (form._record) {
                            form._record.reject();
                        }
                        this.redirect('/');
                    },
                    scope: this
                }]
            });
        }
        return this.form;
    },

    onSubmit: function(){
        var form = this.form.getForm();
        var r = form._record;

        var errors = {isValid:true};
        form.getFields().each(function(f){
            var fe = f.getErrors();
            if(fe.length){
                errors.isValid = false;
                errors[f.name] = fe[0];
            }
        });

        if (errors.isValid){
            if(!r){
                r = this.store.createModel({});
                this.store.insert(0,r);
            } else if (!this.store.getById(r.internalId)){
                this.store.add(r);
            }

            form.updateRecord(r);

            var count = this.store.getNewRecords().length + this.store.getUpdatedRecords().length;
            if(count) {
                this.store.sync({
                    success: function() {
                        //win.close();
                    }
                });
            } else {
                //win.close();
            }
            return true;
        } else {
            form.markInvalid(errors);
        }
        return false;
    }
});