/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.metadata.category;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.mule.api.annotations.MetaDataCategory;
import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataOutputRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.common.metadata.DefaultDefinedMapMetaDataModel;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.PojoMetaDataBuilder;
import org.mule.modules.salesforce.SalesforceConnector;
import org.mule.modules.salesforce.metadata.type.MetadataType;

/**
 * @author Mulesoft, Inc
 */
@MetaDataCategory
public class CreateMetadataCategory {

    @Inject
    private SalesforceConnector connector;

    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetadataKeys() {
        List<MetaDataKey> metaDataKeyList = new ArrayList<MetaDataKey>();

        for (MetadataType metadataEntity : MetadataType.values()) {
            metaDataKeyList.add(new DefaultMetaDataKey(metadataEntity.name(), metadataEntity.getDisplayName()));
        }

        return metaDataKeyList;
    }

    @MetaDataOutputRetriever
    public MetaData getOutputMetaData(MetaDataKey key) throws Exception {
        PojoMetaDataBuilder<?> dynamicObject = new DefaultMetaDataBuilder().createPojo(MetadataType.valueOf(key.getId()).getMetadataEntityClass());
        return new DefaultMetaData(new DefaultDefinedMapMetaDataModel(dynamicObject.build().getFields()));
    }

    @MetaDataRetriever
    public MetaData getMetaData(MetaDataKey key) throws Exception {
        PojoMetaDataBuilder<?> dynamicObject = new DefaultMetaDataBuilder().createPojo(MetadataType.valueOf(key.getId()).getMetadataEntityClass());
        return new DefaultMetaData(new DefaultDefinedMapMetaDataModel(dynamicObject.build().getFields()));
    }

    public SalesforceConnector getConnector() {
        return connector;
    }

    public void setConnector(SalesforceConnector connector) {
        this.connector = connector;
    }
}
