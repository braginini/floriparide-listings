Ext.define('App.widget.ModelForm', {
    extend: 'Ext.form.Panel',
    autoScroll: true,
    labelWidth: 250,
    defaults: {
        anchor: '100%',
        msgTarget:'under',
        labelWidth: 200
    },
    cls: 'x-panel-mc',
    border:false,
    bodyPadding: 10,

    initComponent: function() {
        if (!this.items) {
            this.items = [];
            var fields = this.model.getFields();
            for(var i=0;i<fields.length;i++){
                var f = fields[i];
                this.items.push(f.getEditor());
            }
        }
        this.callParent();
    }
});