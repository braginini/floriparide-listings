Ext.define('App.model.BaseModel', {
    extend: 'Ext.data.Model',
    alias: 'model.branch',
    idProperty: 'id',

    inheritableStatics: {
        load: function(id, config) {
            config = Ext.apply({}, config);
            config = Ext.applyIf(config, {
                action: 'read',
                id    : id
            });

            var operation  = new Ext.data.Operation(config),
                scope      = config.scope || this,
                callback;

            callback = function(operation) {
                var record = null,
                    success = operation.wasSuccessful();

                if (success) {
                    record = operation.getResultSet();
                    record = this.create(record);
                    record.internalId = record.data.id;

                    if (!record.hasId()) {
                        record.setId(id);
                    }
                    Ext.callback(config.success, scope, [record, operation]);
                } else {
                    Ext.callback(config.failure, scope, [record, operation]);
                }
                Ext.callback(config.callback, scope, [record, operation, success]);
            };

            this.getProxy().read(operation, callback, this);
        }
    },

    onClassExtended: function(cls, data, hooks) {
        var me = this;
        var onBeforeClassCreated = hooks.onBeforeCreated;

        hooks.onBeforeCreated = function(cls, data) {
            if (!data.proxy && data.url) {
                data.proxy = {
                    type: 'rest',
                    url: '/api/admin/v1' + data.url,
                    reader: {
                        type: 'json',
                        idProperty: 'id',
                        root: 'list',
                        totalProperty: 'totalCount'
                    }
                };
                delete data.url;
            }
            onBeforeClassCreated.call(me, cls, data, hooks);
        };
    }
});