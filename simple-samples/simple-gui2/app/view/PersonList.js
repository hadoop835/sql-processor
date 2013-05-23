/*
 * File: app/view/PersonList.js
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

Ext.define('Simplereg.view.PersonList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.personlist',

    height: 250,
    width: 400,
    title: 'Person List',
    store: 'Person',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            columns: [
                {
                    xtype: 'numbercolumn',
                    hidden: true,
                    dataIndex: 'id',
                    text: 'Id',
                    format: '0'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'firstName',
                    text: 'First Name'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'lastName',
                    text: 'Last Name'
                },
                {
                    xtype: 'datecolumn',
                    dataIndex: 'dateOfBirth',
                    text: 'Date of Birth'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'ssn',
                    text: 'SSN'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'genger',
                    text: 'Genger'
                }
            ],
            dockedItems: [
                {
                    xtype: 'pagingtoolbar',
                    dock: 'bottom',
                    width: 360,
                    displayInfo: true,
                    store: 'Person'
                }
            ]
        });

        me.callParent(arguments);
    }

});