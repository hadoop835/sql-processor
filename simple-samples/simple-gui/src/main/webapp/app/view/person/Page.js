/*
 * File: app/view/Page.js
 *
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('SimpleWeb.view.person.Page', {
    extend : 'Ext.container.Viewport',

    layout : {
        type : 'fit'
    },

    initComponent : function() {
        var me = this;

        Ext.applyIf(me, {
            items : [ {
                xtype : 'tabpanel',
                id : 'PersonRegistry',
                header : false,
                manageHeight : false,
                title : 'Simple Person Registry',
                activeTab : 0,
                items : [ {
                    xtype : 'panel',
                    id : 'PersonSelection',
                    layout : {
                        type : 'fit'
                    },
                    title : 'Person Selection',
                    dockedItems : [ {
                        xtype : 'toolbar',
                        dock : 'top',
                        cls : 'line',
                        items : [ {
                            xtype : 'button',
                            itemId : 'search_person',
                            iconCls : 'icon-search',
                            text : 'Search'
                        }, {
                            xtype : 'button',
                            itemId : 'all_person',
                            iconCls : 'icon-search',
                            text : 'All'
                        }, {
                            xtype : 'button',
                            itemId : 'refresh_person',
                            iconCls : 'icon-refresh',
                            text : 'Refresh'
                        }, {
                            xtype : 'tbseparator'
                        }, {
                            xtype : 'button',
                            itemId : 'add_person',
                            iconCls : 'icon-add',
                            text : 'Add'
                        } ]
                    } ],
                    items : [ {
                        xtype : 'gridpanel',
                        itemId : 'person_list',
                        autoScroll : true,
                        header : false,
                        title : 'Person List',
                        store : 'People',
                        columns : [ {
                            xtype : 'numbercolumn',
                            hidden : true,
                            dataIndex : 'id',
                            text : 'Id',
                            flex : 1
                        }, {
                            xtype : 'gridcolumn',
                            dataIndex : 'firstName',
                            text : 'First Name',
                            sortable : false,
                            flex : 2
                        }, {
                            xtype : 'gridcolumn',
                            dataIndex : 'lastName',
                            text : 'Last Name',
                            flex : 2
                        }, {
                            xtype : 'datecolumn',
                            dataIndex : 'dateOfBirth',
                            text : 'Date of Birth',
                            sortable : false,
                            flex : 1,
                            format : 'd.m.Y'
                        }, {
                            xtype : 'gridcolumn',
                            dataIndex : 'ssn',
                            text : 'SSN',
                            sortable : false,
                            flex : 1
                        }, {
                            xtype : 'gridcolumn',
                            dataIndex : 'gender',
                            sortable : false,
                            text : 'Gender',
                            flex : 1
                        } ],
                        dockedItems : [ {
                            xtype : 'pagingtoolbar',
                            dock : 'bottom',
                            width : 360,
                            displayInfo : true,
                            store : 'People',
                            listeners : {
                                beforerender : function() {
                                    this.down('#refresh').hide();
                                }
                            }
                        } ]
                    } ]
                } ]
            } ]
        });

        me.callParent(arguments);
    }

});