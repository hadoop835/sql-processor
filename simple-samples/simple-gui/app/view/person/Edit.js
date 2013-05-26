/*
 * File: app/view/PersonModify.js
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

Ext.define('SimpleWeb.view.person.Edit', {
    extend : 'Ext.window.Window',

    id : 'PersonModify',
    width : 400,
    closeAction : 'hide',
    iconCls : 'icon-edit',
    title : 'Modify Personal Data',
    modal : true,
    y : 100,

    initComponent : function() {
        var me = this;

        Ext.applyIf(me, {
            items : [ {
                xtype : 'form',
                bodyPadding : 10,
                header : false,
                title : 'Personal Data',
                items : [ {
                    xtype : 'textfield',
                    anchor : '100%',
                    itemId : 'firstName',
                    name : 'firstName',
                    fieldLabel : 'First Name'
                }, {
                    xtype : 'textfield',
                    anchor : '100%',
                    itemId : 'lastName',
                    name : 'lastName',
                    fieldLabel : 'Last Name'
                }, {
                    xtype : 'datefield',
                    anchor : '100%',
                    itemId : 'dateOfBirth',
                    name : 'dateOfBirth',
                    fieldLabel : 'Date of Birth'
                }, {
                    xtype : 'textfield',
                    anchor : '100%',
                    itemId : 'ssn',
                    name : 'ssn',
                    fieldLabel : 'SSN'
                }, {
                    xtype : 'combobox',
                    anchor : '100%',
                    itemId : 'gender',
                    name : 'gender',
                    fieldLabel : 'Gender'
                } ]
            } ],
            dockedItems : [ {
                xtype : 'toolbar',
                dock : 'bottom',
                items : [ {
                    xtype : 'tbfill'
                }, {
                    xtype : 'button',
                    itemId : 'cancel_dialog',
                    text : 'Cancel'
                }, {
                    xtype : 'button',
                    itemId : 'accept_modify',
                    iconCls : 'icon-accept',
                    text : 'Save Changes'
                } ]
            } ]
        });

        me.callParent(arguments);
    }

});