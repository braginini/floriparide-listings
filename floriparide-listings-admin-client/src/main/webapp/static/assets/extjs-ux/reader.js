Ext.override(Ext.data.reader.Reader, {
    readRecords: function(data) {
        var me = this,
            meta;

        //this has to be before the call to super because we use the meta data in the superclass readRecords
        if (me.getMeta) {
            meta = me.getMeta(data);
            if (meta) {
                me.onMetaChange(meta);
            }
        } else if (data.metaData) {
            me.onMetaChange(data.metaData);
        }

        if (!Ext.isArray(data) && !data[this.root]) {
            data = [data];
        }
        /**
         * @property {Object} jsonData
         * A copy of this.rawData.
         * @deprecated Will be removed in Ext JS 5.0. This is just a copy of this.rawData - use that instead.
         */
        me.jsonData = data;

        return me.callParent([data]);
    }
});