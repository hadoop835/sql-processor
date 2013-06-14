/*
 * File: app/view/ContactCreate.js
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

Ext.define('Simplereg.view.ContactCreate', {
    extend: 'Ext.window.Window',

    requires: [
        'Simplereg.view.override.ContactCreate'
    ],

    id: 'contact-create',
    width: 400,
    closeAction: 'hide',
    iconCls: 'icon-add',
    title: 'Add Contact',
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    bodyPadding: 10,
                    header: false,
                    title: 'Data',
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'bottom',
                            items: [
                                {
                                    xtype: 'button',
                                    itemId: 'reset',
                                    iconCls: 'icon-reset',
                                    text: 'Reset'
                                },
                                {
                                    xtype: 'tbfill'
                                },
                                {
                                    xtype: 'button',
                                    itemId: 'cancel',
                                    iconCls: 'icon-cancel',
                                    text: 'Cancel'
                                },
                                {
                                    xtype: 'button',
                                    itemId: 'submit',
                                    iconCls: 'icon-add',
                                    text: 'Add Contact'
                                }
                            ]
                        }
                    ],
                    items: [
                        {
                            xtype: 'numberfield',
                            anchor: '100%',
                            hidden: true,
                            fieldLabel: 'Person Id',
                            name: 'personId',
                            readOnly: true
                        },
                        {
                            xtype: 'combobox',
                            anchor: '100%',
                            fieldLabel: 'Type',
                            name: 'ctype',
                            allowBlank: false,
                            editable: false,
                            displayField: 'name',
                            store: 'ContactTypes',
                            valueField: 'value'
                        },
                        {
                            xtype: 'combobox',
                            anchor: '100%',
                            tpl: '<tpl for="."><div class="x-boundlist-item">{name} ({code})</div></tpl>',
                            displayTpl: '<tpl for=".">{name} ({code})</tpl>',
                            itemId: 'country',
                            fieldLabel: 'Country',
                            name: 'countryCode',
                            displayField: 'name',
                            forceSelection: true,
                            queryMode: 'local',
                            queryParam: 'name',
                            store: 'Countries',
                            valueField: 'code'
                        },
                        {
                            xtype: 'textfield',
                            anchor: '100%',
                            fieldLabel: 'Address',
                            name: 'address'
                        },
                        {
                            xtype: 'textfield',
                            anchor: '100%',
                            fieldLabel: 'Phone',
                            name: 'phoneNumber'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});