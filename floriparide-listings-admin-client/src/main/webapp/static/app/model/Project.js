Ext.define('App.model.Project', {
    extend: 'App.model.BaseModel',
    alias: 'model.project',
    title: 'Project',
    url : '/project',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'name',
        type: 'string'
    }]
});