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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleException;
import org.mule.streaming.PagingDelegate;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;

public abstract class SalesforcePagingDelegate extends PagingDelegate<Map<String, Object>>
{
    private String query;
    private String queryLocator = null;
    private QueryResult cachedQueryResult = null;
    private PartnerConnection connection;
    private boolean lastPageFound = false;
    
    public SalesforcePagingDelegate(PartnerConnection connection, String query) {
        this.connection = connection;
        this.query = query;
    }
    
    @Override
    public List<Map<String, Object>> getPage() {
        if (this.cachedQueryResult != null) {
            List<Map<String, Object>> items = this.consume(this.cachedQueryResult);
            this.cachedQueryResult = null;
            
            return items;
        }

        if (this.lastPageFound) {
            return Collections.emptyList();
        }
        
        QueryResult queryResult = getQueryResult();

        setQueryLocatorStatus(queryResult);

        return this.consume(queryResult);
    }

    private void setQueryLocatorStatus(QueryResult queryResult) {
        if (queryResult.isDone()) {
            this.queryLocator = null;
            this.lastPageFound = true;
        } else {
            this.queryLocator = queryResult.getQueryLocator();
        }
    }

    private QueryResult getQueryResult() {
        try {
            return this.queryLocator != null ? this.connection.queryMore(this.queryLocator) : this.doQuery(this.query); 
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected abstract QueryResult doQuery(String query) throws ConnectionException;
    
    private List<Map<String, Object>> consume(QueryResult queryResult) {
        List<Map<String, Object>> result = null;
        SObject[] records = queryResult.getRecords();

        if (records != null && records.length > 0) {
            result = new ArrayList<Map<String, Object>>();
            for (SObject object : records) {
                result.add(object.toMap());
            }
        }
        
        return result;
    }

    @Override
    public void close() throws MuleException {
        this.cachedQueryResult = null;
    }
    
    @Override
    public int getTotalResults() {
        if (this.cachedQueryResult == null) {
            this.cachedQueryResult = this.getQueryResult();
            setQueryLocatorStatus(this.cachedQueryResult);
        }
        
        return this.cachedQueryResult.getSize();
    }

}


