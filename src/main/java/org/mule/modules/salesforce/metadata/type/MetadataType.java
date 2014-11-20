/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.metadata.type;

import com.sforce.soap.metadata.Metadata;


/**
 * @author Mulesoft, Inc
 */
public enum MetadataType {
	ExternalDataSource("com.sforce.soap.metadata", com.sforce.soap.metadata.ExternalDataSource.class, "ExternalDataSource"),
	RemoteSiteSetting("com.sforce.soap.metadata", com.sforce.soap.metadata.RemoteSiteSetting.class,"RemoteSiteSetting"),
    CustomObject("com.sforce.soap.metadata", com.sforce.soap.metadata.CustomObject.class, "CustomObject"),
    BusinessProcess("com.sforce.soap.metadata", com.sforce.soap.metadata.BusinessProcess.class, "BusinessProcess"),
    PicklistValue("com.sforce.soap.metadata", com.sforce.soap.metadata.PicklistValue.class, "PicklistValue"),
    CompactLayout("com.sforce.soap.metadata", com.sforce.soap.metadata.CompactLayout.class, "CompactLayout"),
    CustomField("com.sforce.soap.metadata", com.sforce.soap.metadata.CustomField.class, "CustomField"),
    FieldSet("com.sforce.soap.metadata", com.sforce.soap.metadata.FieldSet.class, "FieldSet"),
    ListView("com.sforce.soap.metadata", com.sforce.soap.metadata.ListView.class, "ListView"),
    RecordType("com.sforce.soap.metadata", com.sforce.soap.metadata.RecordType.class, "RecordType"),
    SharingReason("com.sforce.soap.metadata", com.sforce.soap.metadata.SharingReason.class, "SharingReason"),
    ValidationRule("com.sforce.soap.metadata", com.sforce.soap.metadata.ValidationRule.class, "ValidationRule"),
    WebLink("com.sforce.soap.metadata", com.sforce.soap.metadata.WebLink.class, "WebLink");

    String entityPackage;
    Class<? extends com.sforce.soap.metadata.Metadata> metadataEntityClass;
    String displayName;

    MetadataType(String entityPackage, Class<? extends com.sforce.soap.metadata.Metadata> metadataEntityClass, String displayName) {
        this.entityPackage = entityPackage;
        this.metadataEntityClass = metadataEntityClass;
        this.displayName = displayName;

    }

    public String getEntityPackage() {
        return this.entityPackage;
    }

    public Class<? extends com.sforce.soap.metadata.Metadata> getMetadataEntityClass() {
        return this.metadataEntityClass;
    }

    public String getDisplayName() {
        return  displayName;
    }
    
    public Metadata getMetadataObject() throws InstantiationException, IllegalAccessException {
    	return this.metadataEntityClass.newInstance();
    }
    
    public static MetadataType getByClass(Class<? extends com.sforce.soap.metadata.Metadata> clazz){
    	for (MetadataType type : values()) {
    	    if (type.metadataEntityClass.equals(clazz)) {
    	      return type;
    	    }
    	  }
    	return null;
    }
}
