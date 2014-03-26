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
        Ext.apply(this, config);
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
        this.route('/new', this.create, this);
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

    create: function() {
        var new_obj = {};
        new_obj[this.model.prototype.idProperty] = null;
        var r = this.model.create(new_obj);
        var form = this.getForm();
        form.setTitle('New '+this.model.prototype.title);
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

            this.grid.mon(this.grid, 'createaction', function(r) {
                this.redirect('/new');
            }, this);

            this.grid.mon(this.grid, 'deleteaction', this.removeRecords, this);
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

    removeRecords: function(rs) {
        if(rs.length) {
            Ext.Msg.confirm('Внимание!','Вы точно хотите удалить выбранные записи?',function(button) {
                if (button === 'yes') {
                    this.store.remove(rs);
                    this.store.sync();
                } else {
                    // do something when No was clicked.
                }
            },this);
        }
    },

    onSubmit: function(){
        var form = this.form.getForm();
        var r = form._record;
        var phantom = r.phantom;

        var errors = {isValid:true};
        form.getFields().each(function(f){
            var fe = f.getErrors();
            if(fe.length){
                errors.isValid = false;
                errors[f.name] = fe[0];
            }
        });

        if (errors.isValid){
            form.updateRecord(r);
            r.save({
                scope: this,
                callback: function(r, op, success) {
                    if (success) {
                        r.commit();
                        form.loadRecord(Ext.isArray(r) ? r[0] : r);
                        if (phantom && !this.store.getById(r.getId())){
                            this.store.add(r);
                        }
                        this.redirect('/');
                    }
                }
            });

            return true;
        } else {
            form.markInvalid(errors);
        }
        return false;
    }
});