/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.mule.api.annotations.MetaDataCategory;
import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.builder.EnumMetaDataBuilder;
import org.mule.common.metadata.datatype.DataType;

import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;
import com.sforce.soap.partner.PicklistEntry;

/**
 * @author Mulesoft, Inc
 */
@MetaDataCategory
public class SalesforceMetadataManager {

    @Inject
    private SalesforceConnector connector;

    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<MetaDataKey>();
        DescribeGlobalResult describeGlobal = connector.describeGlobal();

        if (describeGlobal != null) {
            DescribeGlobalSObjectResult[] sobjects = describeGlobal.getSobjects();
            for (DescribeGlobalSObjectResult sobject : sobjects) {
                keys.add(new DefaultMetaDataKey(sobject.getName(), sobject.getLabel(), sobject.isQueryable()));
            }
        }

        return keys;
    }

    @MetaDataRetriever
    public MetaData getMetaData(MetaDataKey key) throws Exception {
        DescribeSObjectResult describeSObject = connector.describeSObject(key.getId());

        MetaData metaData = null;
        if (describeSObject != null) {
            Field[] fields = describeSObject.getFields();
            DynamicObjectBuilder dynamicObject = new DefaultMetaDataBuilder().createDynamicObject(key.getId());
            for (Field f : fields) {
               addField(f, dynamicObject);
            }
            MetaDataModel model = dynamicObject.build();
            metaData = new DefaultMetaData(model);
        }
        return metaData;
    }
    
    private void addField(Field f, DynamicObjectBuilder dynamicObject) {
        DataType dataType = getDataType(f.getType());
        switch (dataType){
            case POJO:
                dynamicObject.addPojoField(f.getName(), Object.class);
                break;
            case ENUM:
                EnumMetaDataBuilder enumMetaDataBuilder = dynamicObject.addEnumField(f.getName());
                if (f.getPicklistValues().length != 0){
                    String[] values = new String[f.getPicklistValues().length];
                    int i =0;
                    for (PicklistEntry picklistEntry : f.getPicklistValues()){
                        values[i] = (picklistEntry.getValue());
                        i++;
                    }
                    enumMetaDataBuilder.setValues(values)
                            .isWhereCapable(f.isFilterable())
                            .isOrderByCapable(f.isSortable());
                }
                break;
            default:
                dynamicObject.addSimpleField(f.getName(), dataType)
                        .isWhereCapable(f.isFilterable())
                        .isOrderByCapable(f.isSortable());
        }
    }

    private DataType getDataType(FieldType fieldType) {
        DataType dt;
        switch (fieldType) {
            case _boolean:
                dt = DataType.BOOLEAN;
                break;
            case _double:
                dt = DataType.DOUBLE;
                break;
            case _int:
                dt = DataType.INTEGER;
                break;
            case anyType:
                dt = DataType.POJO;
                break;
            case base64:
                dt = DataType.STRING;
                break;
            case combobox:
                dt = DataType.ENUM;
                break;
            case currency:
                dt = DataType.STRING;
                break;
            case datacategorygroupreference:
                dt = DataType.STRING;
                break;
            case date:
                dt = DataType.DATE_TIME;
                break;
            case datetime:
                dt = DataType.DATE_TIME;
                break;
            case email:
                dt = DataType.STRING;
                break;
            case encryptedstring:
                dt = DataType.STRING;
                break;
            case id:
                dt = DataType.STRING;
                break;
            case multipicklist:
                dt = DataType.ENUM;
                break;
            case percent:
                dt = DataType.STRING;
                break;
            case phone:
                dt = DataType.STRING;
                break;
            case picklist:
                dt = DataType.ENUM;
                break;
            case reference:
                dt = DataType.STRING;
                break;
            case string:
                dt = DataType.STRING;
                break;
            case textarea:
                dt = DataType.STRING;
                break;
            case time:
                dt = DataType.DATE_TIME;
                break;
            case url:
                dt = DataType.STRING;
                break;
            default:
                dt = DataType.STRING;
        }
        return dt;
    }

    public SalesforceConnector getConnector() {
        return connector;
    }

    public void setConnector(SalesforceConnector connector) {
        this.connector = connector;
    }
}
