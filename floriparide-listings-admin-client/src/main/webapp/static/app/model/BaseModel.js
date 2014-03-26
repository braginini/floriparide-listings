Ext.define('App.model.BaseModel', {
    extend: 'Ext.data.Model',
    alias: 'model.branch',
    idProperty: 'id',

    onClassExtended: function(cls, data, hooks) {
        var me = this;
        var onBeforeClassCreated = hooks.onBeforeCreated;

        hooks.onBeforeCreated = function(cls, data) {
            if (!data.proxy && data.url) {
                data.proxy = Ext.create('Ext.data.proxy.Rest',{
                    type: 'rest',
                    url: '/api/admin/v1' + data.url,
                    batchActions: true,
                    headers: {
                        'Accept': 'text/json,application/json',
                        'Content-type': 'application/json'
                    },
                    reader: {
                        type: 'json',
                        idProperty: 'id',
                        root: 'list',
                        totalProperty: 'totalCount'
                    },

                    buildUrl: function(request) {
                        var operation = request.operation;
                        var records   = operation.records || [];
                        if (operation.action === 'create') {
                            this.appendId = false;
                        } else if (request.operation.action === 'destroy' && records.length > 1) {
                            this.appendId = false;
                            request.params['id'] = Ext.Array.map(records, function(r) {return r.getId();});
                        }
                        var url = Ext.data.proxy.Rest.prototype.buildUrl.apply(this, arguments);
                        this.appendId = true;

                        return url;
                    }
                });
                delete data.url;
            }
            onBeforeClassCreated.call(me, cls, data, hooks);

            var fields = data.fields.items;
            for (var i=0; i< fields.length; i++) {
                fields[i].model = cls;
            }
        };
    }
});