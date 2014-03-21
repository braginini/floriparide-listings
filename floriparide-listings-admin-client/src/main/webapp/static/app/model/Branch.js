Ext.define('App.model.Branch', {
    extend: 'Ext.data.Model',
    alias: 'model.branch',

    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'name',
        type: 'string'
    },{
        name: 'description',
        type: 'string'
    }]
});