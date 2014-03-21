Ext.define('App.widget.ModelForm', {
    extend: 'Ext.form.Panel',
    autoScroll: true,
    labelWidth: 250,
    defaults: {
        anchor: '100%',
        msgTarget:'under'
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
                if(f.editor) {
                    var editor = Ext.apply({
                        xtype:'textfield',
                        fieldLabel: Ext.util.Format.stripTags(f.alias.replace(/<br>/g,' ')),
                        name:f.name
                    }, f.editor);
                    if(editor.allowBlank==false){
                        editor.fieldLabel += '*';
                    }
                } else {
                    switch (f.type.type) {
                        case 'int':
                            editor = {
                                xtype: 'numberfield',
                                allowDecimals: false
                            };
                            break;
                        case 'float':
                            editor = {
                                xtype: 'numberfield'
                            };
                            break;
                        case 'date':
                            editor = {
                                xtype: 'datefield'
                            };
                            break;
                        default:
                            editor = {
                                xtype: 'textfield'
                            };
                    }

                    Ext.applyIf(editor,{
                        id: f.name,
                        name: f.name,
                        fieldLabel: f.title || f.name
                    });
                }
                this.items.push(editor);
            }
        }
        this.callParent();
    }
});