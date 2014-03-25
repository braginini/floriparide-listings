Ext.define('App.widget.ModelGrid', {
    extend:'Ext.grid.Panel',
    winCfg:null,
    readOnly: false,
    storeAutoLoad: true,

    initComponent:function () {
        this.addEvents('editaction');
        if(!this.columns) {
            this.columns = [];
            if(this.model) {
                var fields = this.model.getFields();
                for(var i=0;i<fields.length;i++) {
                    var f = fields[i];
                    if(!f.hidden) {
                        this.columns.push(Ext.apply({
                            dataIndex: f.name,
                            header: f.title || f.name,
                            flex: 1
                        }, f.columnCfg || {}));
                    }
                }
            }
        }

        if(!this.readOnly) {
            this.columns.push({header:'Actions', xtype:'fa_actioncolumn', width:80,
                items:[
                    {
                        iconCls:'fa fa-pencil-square-o',
                        tooltip:'Edit',
                        scope:this,
                        handler:function (grid, rowIndex) {
                            var r = grid.store.getAt(rowIndex);
                            this.fireEvent('editaction', r);
                        }
                    },{
                        iconCls:'fa fa-trash-o',
                        tooltip:'Remove',
                        scope: this,
                        handler:function (grid, rowIndex, colIndex, itm, e, rec) {
                            this.removeRecords([rec]);
                        }
                    }
                ]
            });
        }

        if(!this.store && this.model) {
            this.store = new Ext.data.Store({
                paramsAsHash: true,
                model: this.model,
                remoteSort: true,
                pageSize: 100
            });
        }

        if(!this.readOnly) {
            this.tbar = [{
                text: 'Добавить',
                iconCls: 'fa fa-plus-square-o',
                handler: this.addRecord,
                scope: this
            },{
                text: 'Удалить',
                iconCls: 'fa fa-trash-o',
                handler: function() {
                    var rs = this.getSelectionModel().getSelection();
                    this.removeRecords(rs);
                },
                scope: this
            }];
        }

        this.bbar = {
            xtype: 'pagingtoolbar',
            store: this.store,
            dock: 'bottom',
            displayInfo: true
        };

        this.callParent();

        if(!this.readOnly) {
            this.mon(this,'celldblclick',function(view, td, cellIndex, record){
                this.fireEvent('editaction', record);
            },this);
        }

        if(this.storeAutoLoad)
            this.store.reload();
    },

    addRecord: function(){
        var win = this.getFormWindow(null,{
            title: 'Новая запись'
        });
        win.show();
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
    }

});

Ext.define('App.grid.column.Action', {
    extend:  'Ext.grid.column.Action' ,
    alias: ['widget.fa_actioncolumn'],
    defaultRenderer: function(v, meta, record, rowIdx, colIdx, store, view){
        var me = this,
            prefix = Ext.baseCSSPrefix,
            scope = me.origScope || me,
            items = me.items,
            len = items.length,
            i = 0,
            item, ret, disabled, tooltip;




        ret = Ext.isFunction(me.origRenderer) ? me.origRenderer.apply(scope, arguments) || '' : '';

        meta.tdCls += ' ' + Ext.baseCSSPrefix + 'action-col-cell';
        for (; i < len; i++) {
            item = items[i];

            disabled = item.disabled || (item.isDisabled ? item.isDisabled.call(item.scope || scope, view, rowIdx, colIdx, item, record) : false);
            tooltip = disabled ? null : (item.tooltip || (item.getTip ? item.getTip.apply(item.scope || scope, arguments) : null));


            if (!item.hasActionConfiguration) {


                item.stopSelection = me.stopSelection;
                item.disable = Ext.Function.bind(me.disableAction, me, [i], 0);
                item.enable = Ext.Function.bind(me.enableAction, me, [i], 0);
                item.hasActionConfiguration = true;
            }

            ret += '<i role="button" style="font-size: 16px;margin-left: 5px;"' +
                '" class="' + prefix + 'action-col-icon ' + prefix + 'action-col-' + String(i) + ' ' + (disabled ? prefix + 'item-disabled' : ' ') +
                ' ' + (Ext.isFunction(item.getClass) ? item.getClass.apply(item.scope || scope, arguments) : (item.iconCls || me.iconCls || '')) + '"' +
                (tooltip ? ' data-qtip="' + tooltip + '"' : '') + '></i>';
        }
        return ret;
    }
});