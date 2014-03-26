Ext.override(Ext.data.Field, {
    constructor: function(config) {
        if (Ext.isString(config)) {
            config = {name: config};
        }

        Ext.applyIf(config, {
            label: config.name,
            hidden: false,
            readOnly: false,
            columnCfg: null
        });

        this.callOverridden([config]);
    },

    getEditor: function(def) {
        var editor;
        if(this.editor) {
            editor = Ext.apply({
                xtype:'textfield'
            }, this.editor);
        } else {
            switch (this.type.type) {
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

            if (this.hidden || this.name == this.model.prototype.idProperty) {
                editor.xtype = 'hidden';
            }
        }

        Ext.applyIf(editor,{
            itemId: this.name,
            name: this.name,
            fieldLabel: Ext.util.Format.stripTags(this.label.replace(/<br>/g,' ')),
            readOnly: this.readOnly
        });

        if(editor.allowBlank==false){
            editor.fieldLabel = '<b>'+editor.fieldLabel+'</b>';
        }

        return def ? Ext.apply(editor, def) : editor;
    }
});