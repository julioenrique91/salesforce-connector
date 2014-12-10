/**
 2 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce;

import com.sforce.async.*;
import com.sforce.soap.metadata.*;
import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.annotations.*;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.Optional;
import org.mule.api.callback.SourceCallback;
import org.mule.api.callback.StopSourceCallback;
import org.mule.api.config.MuleProperties;
import org.mule.api.context.MuleContextAware;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.store.ObjectStore;
import org.mule.api.store.ObjectStoreException;
import org.mule.api.store.ObjectStoreManager;
import org.mule.common.query.DsqlQuery;
import org.mule.modules.salesforce.api.*;
import org.mule.modules.salesforce.bulk.SaveResultToBulkOperationTransformer;
import org.mule.modules.salesforce.bulk.UpsertResultToBulkOperationTransformer;
import org.mule.modules.salesforce.connection.CustomMetadataConnection;
import org.mule.modules.salesforce.connection.CustomPartnerConnection;
import org.mule.modules.salesforce.connection.strategy.SalesforceStrategy;
import org.mule.modules.salesforce.connection.strategy.Subscription;
import org.mule.modules.salesforce.exception.SalesforceSessionExpiredException;
import org.mule.modules.salesforce.lazystream.impl.LazyQueryResultInputStream;
import org.mule.modules.salesforce.metadata.MetadataService;
import org.mule.modules.salesforce.metadata.category.MetadataCategory;
import org.mule.modules.salesforce.metadata.type.MetadataOperationType;
import org.mule.modules.salesforce.metadata.type.MetadataType;
import org.mule.streaming.PagingConfiguration;
import org.mule.streaming.ProviderAwarePagingDelegate;
import org.mule.util.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.util.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
@org.mule.api.annotations.Connector(name = "sfdc", schemaVersion = "5.0", friendlyName = "Salesforce", minMuleVersion = "3.5")
@MetaDataScope(SalesforceMetadataManager.class)
@ReconnectOn(exceptions={SalesforceSessionExpiredException.class})
public class SalesforceConnector implements MuleContextAware {

    private static final Logger LOGGER = Logger.getLogger(SalesforceConnector.class);

    @Start
    public void init() {
        this.registerTransformers();
    }

    @ConnectionStrategy
    private SalesforceStrategy salesforceStrategy;

    public SalesforceStrategy getSalesforceStrategy() {
        return salesforceStrategy;
    }

    public void setSalesforceStrategy(SalesforceStrategy salesforceStrategy) {
        this.salesforceStrategy = salesforceStrategy;
    }

    /**
     * Object store manager to obtain a store to support {@link this#getUpdatedObjects}
     */
    private ObjectStoreManager objectStoreManager;

    /**
     * A ObjectStore instance to use in {@link this#getUpdatedObjects}
     */
    @Configurable
    @Optional
    private ObjectStore<? extends Serializable> timeObjectStore;

    /**
     * Creating a batch will create SObjects using this value for the MAX_DEPTH check.
     */
    @Configurable
    @Default("5")
    private Integer batchSobjectMaxDepth;

    private ObjectStoreHelper objectStoreHelper;

    private MuleRegistry registry;

    protected synchronized void setObjectStoreHelper(ObjectStoreHelper objectStoreHelper) {
        this.objectStoreHelper = objectStoreHelper;
    }

    /**
     * Adds one or more new records to your organization's data.
     * <p/>
     * <p class="caution">
     * IMPORTANT: When you map your objects to the input of this message processor keep in mind that they need
     * to match the expected type of the object at Salesforce.
     * <p/>
     * Take the CloseDate of an Opportunity as an example, if you set that field to a string of value "2011-12-13"
     * it will be sent to Salesforce as a string and operation will be rejected on the basis that CloseDate is not
     * of the expected type.
     * <p/>
     * The proper way to actually map it is to generate a Java Date object, you can do so using Groovy expression
     * evaluator as <i>#[groovy:Date.parse("yyyy-MM-dd", "2011-12-13")]</i>.
     * </p>
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create}
     *
     * @param objects An array of one or more sObjects objects.
     * @param type    Type of object to create
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return An array of {@link com.sforce.soap.partner.SaveResult} if async is false
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_create.htm">create()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public List<SaveResult> create(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                   @Placement(group = "sObject Field Mappings") @FriendlyName("sObjects") @Default("#[payload]") List<Map<String, Object>> objects,
                                   @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws Exception {
        SObject[] sObjects = SalesforceUtils.toSObjectList(type, objects);
        return SalesforceUtils.enrichWithPayload(sObjects, getSalesforceSoapAdapter(headers).create(sObjects));
    }
    
    /**
     * Creates a Job in order to perform one or more batches through Bulk API Operations.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create-job:example-1}
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create-job:example-2}
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create-job:example-3}
     *
     * @param operation           The {@link com.sforce.async.OperationEnum} that will be executed by the job.
     * @param type                The type of Salesforce object that the job will process.
     * @param externalIdFieldName Contains the name of the field on this object with the external ID field attribute
     *                            for custom objects or the idLookup field property for standard objects
     *                            (only required for Upsert Operations).
     * @param contentType         The Content Type for this Job results. When specifying a content type different from
     *                            XML for a query type use {@link #queryResultStream(com.sforce.async.BatchInfo)}
     *                            batchResultStream} method to retrieve results.
     * @param concurrencyMode     The concurrency mode of the job, either Parallel or Serial.
     * @return A {@link com.sforce.async.JobInfo} that identifies the created Job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_jobinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_jobs_create.htm">createJob()</a>
     * @since 4.3
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public JobInfo createJob(OperationEnum operation, @MetaDataKeyParam String type, @Optional String externalIdFieldName, @Optional ContentType contentType, @Optional ConcurrencyMode concurrencyMode) throws Exception {
        return createJobInfo(operation, type, externalIdFieldName, contentType, concurrencyMode);
    }

    /**
     * Closes an open Job given its ID.
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:close-job}
     *
     * @param jobId The Job ID identifying the Job to be closed.
     * @return A {@link JobInfo} that identifies the closed Job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_jobinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_jobs_close.htm">closeJob()</a>
     * @since 4.3
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public JobInfo closeJob(String jobId) throws Exception {
        return getSalesforceRestAdapter().closeJob(jobId);
    }

    /**
     * Aborts an open Job given its ID.
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:abort-job}
     *
     * @param jobId The Job ID identifying the Job to be aborted.
     * @return A {@link JobInfo} that identifies the aborted Job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_jobinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_jobs_abort.htm">abortJob()</a>
     * @since 5.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public JobInfo abortJob(String jobId) throws Exception {
        return getSalesforceRestAdapter().abortJob(jobId);
    }

    /**
     * Access latest {@link JobInfo} of a submitted {@link JobInfo}. Allows to track execution status.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:job-info}
     *
     * @param jobId the {@link JobInfo} being monitored
     * @return Latest {@link JobInfo} representing status of the job result.
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_jobs_get_details.htm">getJobInfo()</a>
     * @since 5.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public JobInfo jobInfo(String jobId) throws Exception {
        return getSalesforceRestAdapter().getJobStatus(jobId);
    }

    /**
     * Creates a Batch using the given objects within the specified Job.
     * <p/>
     * This call uses the Bulk API. The operation will be done in asynchronous fashion.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create-batch}
     *
     * @param jobInfo The {@link JobInfo} in which the batch will be created.
     * @param objects A list of one or more sObjects objects. This parameter defaults to payload content.
     * @return A {@link com.sforce.async.BatchInfo} that identifies the batch job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_batchinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_create.htm">createBatch()</a>
     * @since 4.3
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public BatchInfo createBatch(JobInfo jobInfo, @Default("#[payload]") List<Map<String, Object>> objects) throws Exception {
        return createBatchAndCompleteRequest(jobInfo, objects);
    }

    /**
     * Creates a Batch using the given stream within the specified Job.
     * <p/>
     * This call uses the Bulk API. The operation will be done in asynchronous fashion.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create-batch-stream}
     *
     * @param jobInfo The {@link JobInfo} in which the batch will be created.
     * @param stream  A stream containing the data. This parameter defaults to payload content.
     * @return A {@link com.sforce.async.BatchInfo} that identifies the batch job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_batchinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_create.htm">createBatch()</a>
     * @since 5.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public BatchInfo createBatchStream(JobInfo jobInfo, @Default("#[payload]") InputStream stream) throws Exception {
    	if(ContentType.ZIP_XML.equals(jobInfo.getContentType()) || ContentType.ZIP_CSV.equals(jobInfo.getContentType())) {
    		return getSalesforceRestAdapter().createBatchFromZipStream(jobInfo, stream);
    	} else {
    		return getSalesforceRestAdapter().createBatchFromStream(jobInfo, stream);
    	}
    }

    /**
     * Creates a Batch using the given query.
     * <p/>
     * This call uses the Bulk API. The operation will be done in asynchronous fashion.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create-batch-for-query}
     *
     * @param jobInfo The {@link JobInfo} in which the batch will be created.
     * @param query   The query to be executed.
     * @return A {@link BatchInfo} that identifies the batch job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_batchinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_create.htm">createBatch()</a>
     * @since 4.5
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public BatchInfo createBatchForQuery(JobInfo jobInfo, @Default("#[payload]") String query) throws Exception {
        InputStream queryStream = new ByteArrayInputStream(query.getBytes());
        return createBatchForQuery(jobInfo, queryStream);
    }

    /**
     * Adds one or more new records to your organization's data.
     * <p/>
     * This call uses the Bulk API. The creation will be done in asynchronous fashion.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create-bulk}
     *
     * @param objects An array of one or more sObjects objects.
     * @param type    Type of object to create
     * @return A {@link BatchInfo} that identifies the batch job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_batchinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_create.htm">createBatch()</a>
     * @since 4.1
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public BatchInfo createBulk(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                @Placement(group = "sObject Field Mappings") @FriendlyName("sObjects") @Default("#[payload]") List<Map<String, Object>> objects) throws Exception {

        return createBatchAndCompleteRequest(createJobInfo(OperationEnum.insert, type), objects);
    }

    /**
     * Adds one new records to your organization's data.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create-single}
     * {@sample.java ../../../doc/mule-module-sfdc.java.sample sfdc:create-single}
     *
     * @param object  SObject to create
     * @param type    Type of object to create
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return An array of {@link SaveResult}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_create.htm">create()</a>
     * @since 4.1
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public SaveResult createSingle(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                   @Placement(group = "sObject Field Mappings") @FriendlyName("sObject") @Default("#[payload]") Map<String, Object> object,
                                   @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws Exception {
        SaveResult[] saveResults = getSalesforceSoapAdapter(headers).create(new SObject[]{SalesforceUtils.toSObject(type, object)});
        if (saveResults.length > 0) {
            return saveResults[0];
        }

        return null;
    }

    /**
     * Updates one or more existing records in your organization's data.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:update}
     *
     * @param objects An array of one or more sObjects objects.
     * @param type    Type of object to update
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return An array of {@link SaveResult}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_update.htm">update()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public List<SaveResult> update(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                   @Placement(group = "Salesforce sObjects list") @FriendlyName("sObjects") @Default("#[payload]") List<Map<String, Object>> objects,
                                   @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws Exception {
        SObject[] sObjects = SalesforceUtils.toSObjectList(type, objects);
        return SalesforceUtils.enrichWithPayload(sObjects, getSalesforceSoapAdapter(headers).update(sObjects));
    }

    /**
     * Updates one or more existing records in your organization's data.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:update-single}
     *
     * @param object  The object to be updated.
     * @param type    Type of object to update
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return A {@link SaveResult}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_update.htm">update()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public SaveResult updateSingle(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                   @Placement(group = "Salesforce Object") @FriendlyName("sObject") @Default("#[payload]") Map<String, Object> object,
                                   @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws Exception {
        return getSalesforceSoapAdapter(headers).update(new SObject[]{SalesforceUtils.toSObject(type, object)})[0];
    }

    /**
     * Updates one or more existing records in your organization's data.
     * <p/>
     * This call uses the Bulk API. The creation will be done in asynchronous fashion.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:update-bulk}
     *
     * @param objects An array of one or more sObjects objects.
     * @param type    Type of object to update
     * @return A {@link BatchInfo} that identifies the batch job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_batchinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_create.htm">createBatch()</a>
     * @since 4.1
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public BatchInfo updateBulk(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                @Placement(group = "Salesforce sObjects list") @FriendlyName("sObjects") @Default("#[payload]") List<Map<String, Object>> objects) throws Exception {
        return createBatchAndCompleteRequest(createJobInfo(OperationEnum.update, type), objects);
    }

    /**
     * <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_upsert.htm">Upserts</a>
     * an homogeneous list of objects: creates new records and updates existing records, using a custom field to determine the presence of existing records.
     * In most cases, prefer {@link #upsert(String, String, List, Map)} over {@link #create(String, List, Map)},
     * to avoid creating unwanted duplicate records.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:upsert}
     *
     * @param externalIdFieldName Contains the name of the field on this object with the external ID field attribute
     *                            for custom objects or the idLookup field property for standard objects.
     * @param type                the type of the given objects. The list of objects to upsert must be homogeneous
     * @param objects             the objects to upsert
     * @param headers             Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return a list of {@link com.sforce.soap.partner.UpsertResult}, one for each passed object
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error if a connection error occurs
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_upsert.htm">upsert()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public List<UpsertResult> upsert(@Placement(group = "Information") String externalIdFieldName,
                                     @MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                     @Placement(group = "Salesforce sObjects list") @FriendlyName("sObjects") @Default("#[payload]") List<Map<String, Object>> objects,
                                     @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws Exception {
        SObject[] sObjects = SalesforceUtils.toSObjectList(type, objects);
        return SalesforceUtils.enrichWithPayload(sObjects, getSalesforceSoapAdapter(headers).upsert(externalIdFieldName, sObjects));
    }

    /**
     * <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_upsert.htm">Upserts</a>
     * an homogeneous list of objects: creates new records and updates existing records, using a custom field to determine the presence of existing records.
     * In most cases, prefer {@link #upsert(String, String, List, Map)} over {@link #create(String, List, Map)},
     * to avoid creating unwanted duplicate records.
     * <p/>
     * This call uses the Bulk API. The creation will be done in asynchronous fashion.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:upsert-bulk}
     *
     * @param externalIdFieldName Contains the name of the field on this object with the external ID field attribute
     *                            for custom objects or the idLookup field property for standard objects.
     * @param type                the type of the given objects. The list of objects to upsert must be homogeneous
     * @param objects             the objects to upsert
     * @return A {@link BatchInfo} that identifies the batch job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_batchinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_create.htm">createBatch()</a>
     * @since 4.1
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public BatchInfo upsertBulk(@MetaDataKeyParam @Placement(group = "Information", order = 1) @FriendlyName("sObject Type") String type,
                                @Placement(group = "Information", order = 2) String externalIdFieldName,
                                @Placement(group = "Salesforce sObjects list") @FriendlyName("sObjects") @Default("#[payload]") List<Map<String, Object>> objects) throws Exception {
        return createBatchAndCompleteRequest(createJobInfo(OperationEnum.upsert, type, externalIdFieldName, null, null), objects);
    }

    /**
     * Access latest {@link BatchInfo} of a submitted {@link BatchInfo}. Allows to track execution status.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:batch-info}
     *
     * @param batchInfo the {@link BatchInfo} being monitored
     * @return Latest {@link BatchInfo} representing status of the batch job result.
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_get_info.htm">getBatchInfo()</a>
     * @since 4.1
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public BatchInfo batchInfo(@Default("#[payload:]") BatchInfo batchInfo) throws Exception {
        return getSalesforceRestAdapter().getBatchInfo(batchInfo.getJobId(), batchInfo.getId());
    }

    /**
     * Access {@link com.sforce.async.BatchResult} of a submitted {@link BatchInfo}.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:batch-result}
     *
     * @param batchInfo the {@link BatchInfo} being monitored
     * @return {@link com.sforce.async.BatchResult} representing result of the batch job result.
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_get_results.htm">getBatchResult()</a>
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_interpret_status.htm">BatchInfo status</a>
     * @since 4.1
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public BatchResult batchResult(@Default("#[payload:]") BatchInfo batchInfo) throws Exception {
        return getSalesforceRestAdapter().getBatchResult(batchInfo.getJobId(), batchInfo.getId());
    }

    /**
     * Access {@link com.sforce.async.BatchResult} of a submitted {@link BatchInfo}.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:batch-result-stream}
     *
     * @param batchInfo the {@link BatchInfo} being monitored
     * @return {@link java.io.InputStream} representing result of the batch job result.
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_get_results.htm">getBatchResult()</a>
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_interpret_status.htm">BatchInfo status</a>
     * @since 5.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public InputStream batchResultStream(@Default("#[payload:]") BatchInfo batchInfo) throws Exception {
        return getSalesforceRestAdapter().getBatchResultStream(batchInfo.getJobId(), batchInfo.getId());
    }

    /**
     * Returns an {@link InputStream} with the query results of a submitted {@link BatchInfo}
     * <p/>
     * Internally the InputStreams contained in the sequence will be requested on-demand (lazy-loading)
     * Shoud be used only with query jobs!
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:query-result-stream}
     *
     * @param batchInfo the {@link BatchInfo} being monitored
     * @return {@link InputStream} with the results of the Batch.
     * @throws AsyncApiException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_get_results.htm">getBatchResult()</a>
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_interpret_status.htm">BatchInfo status</a>
     * @since 4.5
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public InputStream queryResultStream(@Default("#[payload:]") BatchInfo batchInfo) throws AsyncApiException {

        QueryResultList queryResultList = getSalesforceRestAdapter().getQueryResultList(batchInfo.getJobId(), batchInfo.getId());
        String[] jobResultIds = queryResultList.getResult();
        LOGGER.debug(String.format("SF queryResultStream for JobId[%s] BatchId[%s] - Pages[%s]", batchInfo.getJobId(), batchInfo.getId(), jobResultIds.length));
        if (jobResultIds.length > 0) {
            List<InputStream> inputStreams = new LinkedList<InputStream>();
            for (String jobResultId : jobResultIds) {
                inputStreams.add(new LazyQueryResultInputStream(getSalesforceRestAdapter(), batchInfo.getJobId(), batchInfo.getId(), jobResultId));
            }

            return new SequenceInputStream(Collections.enumeration(inputStreams));
        }


        return null;
    }

    /**
     * Retrieves a list of available objects for your organization's data.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:describe-global}
     *
     * @return A {@link com.sforce.soap.partner.DescribeGlobalResult}
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_describeglobal.htm">describeGlobal()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Describe Calls", description = "A set of calls to describe record structure in Salesforce.")
    public DescribeGlobalResult describeGlobal() throws ConnectionException {
        return getSalesforceSoapAdapter().describeGlobal();
    }

    /**
     * Retrieves one or more records based on the specified IDs.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:retrieve}
     *
     * @param type    Object type. The sp ecified value must be a valid object for your organization.
     * @param ids     The ids of the objects to retrieve
     * @param fields  The fields to return for the matching objects
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return An array of {@link SObject}s
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public List<Map<String, Object>> retrieve(@MetaDataKeyParam @Placement(group = "Information", order = 1) @FriendlyName("sObject Type") String type,
                                              @Placement(group = "Ids to Retrieve") List<String> ids,
                                              @Placement(group = "Fields to Retrieve") List<String> fields,
                                              @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        String fiedsCommaDelimited = StringUtils.collectionToCommaDelimitedString(fields);
        SObject[] sObjects = getSalesforceSoapAdapter(headers).retrieve(fiedsCommaDelimited, type, ids.toArray(new String[ids.size()]));
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (sObjects != null) {
            for (SObject sObject : sObjects) {
                result.add(SalesforceUtils.toMap(sObject));
            }
        }
        return result;
    }

    /**
     * Executes a query against the specified object and returns data that matches the specified criteria.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:query}
     *
     * @param query               Query string that specifies the object to query, the fields to return, and any conditions for
     *                            including a specific object in the query. For more information, see Salesforce Object Query
     *                            Language (SOQL).
     * @param pagingConfiguration the paging configuration
     * @param headers             Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return An array of {@link SObject}s
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_query.htm">query()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    @Paged
    public ProviderAwarePagingDelegate<Map<String, Object>, SalesforceConnector> query(@Query @Placement(group = "Query") final String query, final PagingConfiguration pagingConfiguration,
                                                                                           @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional final Map<SalesforceHeader, Object> headers) {
        return new SalesforcePagingDelegate(query, headers) {

            @Override
            protected QueryResult doQuery(CustomPartnerConnection connection, String query) throws ConnectionException {
                return connection.query(query);
            }
        };
    }

    @QueryTranslator
    public String toNativeQuery(DsqlQuery query) {
        SfdcQueryVisitor visitor = new SfdcQueryVisitor();
        query.accept(visitor);
        return visitor.dsqlQuery();
    }

    /**
     * Executes a query against the specified object and returns data that matches the specified criteria.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:non-paginated-query}
     *
     * @param query   Query string that specifies the object to query, the fields to return, and any conditions for
     *                including a specific object in the query. For more information, see Salesforce Object Query
     *                Language (SOQL).
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return An array of {@link SObject}s
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_query.htm">query()</a>
     * @since 4.0
     * This method should be deprecated at some point since we only want paginated queries
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public List<Map<String, Object>> nonPaginatedQuery(@org.mule.api.annotations.Query @Placement(group = "Query") String query,
                                                       @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        QueryResult queryResult = getSalesforceSoapAdapter(headers).query(query);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        while (queryResult != null) {
            for (SObject object : queryResult.getRecords()) {
                result.add(SalesforceUtils.toMap(object));
            }
            if (queryResult.isDone()) {
                break;
            }
            queryResult = getSalesforceSoapAdapter(headers).queryMore(queryResult.getQueryLocator());
        }

        return result;
    }

    /**
     * Retrieves data from specified objects, whether or not they have been deleted.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:query}
     *
     * @param query               Query string that specifies the object to query, the fields to return, and any conditions for including a specific object in the query. For more information, see Salesforce Object Query Language (SOQL).
     * @param pagingConfiguration the paging configuration
     * @param headers             Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return An array of {@link SObject}s
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_query.htm">query()</a>
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    @Paged
    public ProviderAwarePagingDelegate<Map<String, Object>, SalesforceConnector> queryAll(@Placement(group = "Query") String query, PagingConfiguration pagingConfiguration,
                                                                                              @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) {
        return new SalesforcePagingDelegate(query, headers) {

            @Override
            protected QueryResult doQuery(CustomPartnerConnection connection, String query) throws ConnectionException {
                return connection.queryAll(query);
            }
        };
    }

    /**
     * Search for objects using Salesforce Object Search Language
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:search}
     *
     * @param query   Query string that specifies the object to query, the fields to return, and any conditions for including a specific object in the query. For more information, see Salesforce Object Search Language (SOSL).
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return An array of {@link SObject}s
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_search.htm">search()</a>
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public List<Map<String, Object>> search(@Placement(group = "Query") String query,
                                            @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        SearchResult searchResult = getSalesforceSoapAdapter(headers).search(query);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        for (SearchRecord object : searchResult.getSearchRecords()) {
            result.add(SalesforceUtils.toMap(object.getRecord()));
        }

        return result;
    }

    /**
     * Executes a query against the specified object and returns the first record that matches the specified criteria.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:query-single}
     *
     * @param query   Query string that specifies the object to query, the fields to return, and any conditions for
     *                including a specific object in the query. For more information, see Salesforce Object Query
     *                Language (SOQL).
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return A single {@link SObject}
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_query.htm">query()</a>
     * @since 4.1
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public Map<String, Object> querySingle(@Placement(group = "Query") String query,
                                           @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        SObject[] result = getSalesforceSoapAdapter(headers).query(query).getRecords();
        if (result.length > 0) {
            return SalesforceUtils.toMap(result[0]);
        }

        return null;
    }

    /**
     * Converts a Lead into an Account, Contact, or (optionally) an Opportunity.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:convert-lead}
     *
     * @param leadId                 ID of the Lead to convert. Required. For information on IDs, see ID Field Type.
     * @param contactId              ID of the Contact into which the lead will be merged (this contact must be
     *                               associated with the specified accountId, and an accountId must be specified).
     *                               Required only when updating an existing contact.IMPORTANT if you are converting
     *                               a lead into a person account, do not specify the contactId or an error will result.
     *                               Specify only the accountId of the person account. If no contactID is specified,
     *                               then the API creates a new contact that is implicitly associated with the Account.
     *                               To create a new contact, the client application must be logged in with sufficient
     *                               access rights. To merge a lead into an existing contact, the client application
     *                               must be logged in with read/write access to the specified contact. The contact
     *                               name and other existing data are not overwritten (unless overwriteLeadSource is
     *                               set to true, in which case only the LeadSource field is overwritten).
     *                               For information on IDs, see ID Field Type.
     * @param accountId              ID of the Account into which the lead will be merged. Required
     *                               only when updating an existing account, including person accounts.
     *                               If no accountID is specified, then the API creates a new account. To
     *                               create a new account, the client application must be logged in with
     *                               sufficient access rights. To merge a lead into an existing account,
     *                               the client application must be logged in with read/write access to the
     *                               specified account. The account name and other existing data are not overwritten.
     *                               For information on IDs, see ID Field Type.
     * @param overWriteLeadSource    Specifies whether to overwrite the LeadSource field on the target Contact object
     *                               with the contents of the LeadSource field in the source Lead object (true), or
     *                               not (false, the default). To set this field to true, the client application
     *                               must specify a contactId for the target contact.
     * @param doNotCreateOpportunity Specifies whether to create an Opportunity during lead conversion (false, the
     *                               default) or not (true). Set this flag to true only if you do not want to create
     *                               an opportunity from the lead. An opportunity is created by default.
     * @param opportunityName        Name of the opportunity to create. If no name is specified, then this value
     *                               defaults to the company name of the lead. The maximum length of this field is
     *                               80 characters. If doNotCreateOpportunity argument is true, then no Opportunity
     *                               is created and this field must be left blank; otherwise, an error is returned.
     * @param convertedStatus        Valid LeadStatus value for a converted lead. Required.
     *                               To obtain the list of possible values, the client application queries the
     *                               LeadStatus object, as in:
     *                               Select Id, MasterLabel from LeadStatus where IsConverted=true
     * @param sendEmailToOwner       Specifies whether to send a notification email to the owner specified in the
     *                               ownerId (true) or not (false, the default).
     * @param headers                Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return A {@link com.sforce.soap.partner.LeadConvertResult} object
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_convertlead.htm">convertLead()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public LeadConvertResult convertLead(String leadId, @Optional String contactId,
                                         @Optional String accountId,
                                         @Default("false") Boolean overWriteLeadSource,
                                         @Default("false") Boolean doNotCreateOpportunity,
                                         @Optional String opportunityName,
                                         String convertedStatus,
                                         @Default("false") Boolean sendEmailToOwner,
                                         @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {

        LeadConvert leadConvert = new LeadConvert();
        leadConvert.setLeadId(leadId);
        leadConvert.setContactId(contactId);
        leadConvert.setAccountId(accountId);
        leadConvert.setOverwriteLeadSource(overWriteLeadSource);
        leadConvert.setDoNotCreateOpportunity(doNotCreateOpportunity);
        if (opportunityName != null) {
            leadConvert.setOpportunityName(opportunityName);
        }
        leadConvert.setConvertedStatus(convertedStatus);
        leadConvert.setSendNotificationEmail(sendEmailToOwner);
        LeadConvert[] list = new LeadConvert[1];
        list[0] = leadConvert;

        return getSalesforceSoapAdapter(headers).convertLead(list)[0];
    }

    /**
     * The recycle bin lets you view and restore recently deleted records for 30 days before they are
     * permanently deleted. Your organization can have up to 5000 records per license in the Recycle Bin at any
     * one time. For example, if your organization has five user licenses, 25,000 records can be stored in the
     * Recycle Bin. If your organization reaches its Recycle Bin limit, Salesforce.com automatically removes
     * the oldest records, as long as they have been in the recycle bin for at least two hours.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:empty-recycle-bin}
     *
     * @param ids     Array of one or more IDs associated with the records to delete from the recycle bin.
     *                Maximum number of records is 200.
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return A list of {@link com.sforce.soap.partner.EmptyRecycleBinResult}
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_emptyrecyclebin.htm">emptyRecycleBin()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public List<EmptyRecycleBinResult> emptyRecycleBin(@Placement(group = "Ids to Delete") List<String> ids,
                                                       @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        return Arrays.asList(getSalesforceSoapAdapter(headers).emptyRecycleBin(ids.toArray(new String[ids.size()])));
    }

    /**
     * Retrieves the current system timestamp (Coordinated Universal Time (UTC) time zone) from the API.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:get-server-timestamp}
     *
     * @return Calendar with the current timestamp
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_getservertimestamp.htm">getServerTimestamp()</a>
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public Calendar getServerTimestamp() throws ConnectionException {
        return getSalesforceSoapAdapter().getServerTimestamp().getTimestamp();
    }

    /**
     * Deletes one or more records from your organization's data.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:delete}
     *
     * @param ids     Array of one or more IDs associated with the objects to delete.
     * @param headers Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return An array of {@link com.sforce.soap.partner.DeleteResult}
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_delete.htm">delete()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public List<DeleteResult> delete(@Default("#[payload]") @Placement(group = "Ids to Delete") List<String> ids,
                                     @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        return Arrays.asList(getSalesforceSoapAdapter(headers).delete(ids.toArray(new String[ids.size()])));
    }

    /**
     * Deletes one or more records from your organization's data.
     * The deleted records are not stored in the Recycle Bin.
     * Instead, they become immediately eligible for deletion.
     * <p/>
     * This call uses the Bulk API. The creation will be done in asynchronous fashion.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:hard-delete-bulk}
     *
     * @param objects An array of one or more sObjects objects.
     * @param type    Type of object to update
     * @return A {@link BatchInfo} that identifies the batch job. {@see http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_reference_batchinfo.htm}
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_batches_create.htm">createBatch()</a>
     * @since 4.3
     */
    @Processor
    @OAuthProtected
    @Category(name = "Bulk API", description = "The Bulk API provides programmatic access to allow you to quickly load your organization's data into Salesforce.")
    public BatchInfo hardDeleteBulk(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                    @Placement(group = "Salesforce sObjects list") @FriendlyName("sObjects") @Default("#[payload]") List<Map<String, Object>> objects) throws Exception {
        return createBatchAndCompleteRequest(createJobInfo(OperationEnum.hardDelete, type), objects);
    }

    /**
     * Retrieves the list of individual records that have been created/updated within the given timespan for the specified object.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:get-updated-range}
     *
     * @param type      Object type. The specified value must be a valid object for your organization.
     * @param startTime Starting date/time (Coordinated Universal Time (UTC)not local timezone) of the timespan for
     *                  which to retrieve the data. The API ignores the seconds portion of the specified dateTime value '
     *                  (for example, 12:30:15 is interpreted as 12:30:00 UTC).
     * @param endTime   Ending date/time (Coordinated Universal Time (UTC)not local timezone) of the timespan for
     *                  which to retrieve the data. The API ignores the seconds portion of the specified dateTime value
     *                  (for example, 12:35:15 is interpreted as 12:35:00 UTC). If it is not provided, the current
     *                  server time will be used.
     * @param headers   Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return {@link com.sforce.soap.partner.GetUpdatedResult}
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_getupdatedrange.htm">getUpdatedRange()</a>
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public GetUpdatedResult getUpdatedRange(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                            @Placement(group = "Information") @FriendlyName("Start Time Reference") Calendar startTime,
                                            @Placement(group = "Information") @FriendlyName("End Time Reference") @Optional Calendar endTime,
                                            @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        if (endTime == null) {
            Calendar serverTime = getSalesforceSoapAdapter().getServerTimestamp().getTimestamp();
            endTime = (Calendar) serverTime.clone();
        }
        if (endTime.getTimeInMillis() - startTime.getTimeInMillis() < 60000) {
            endTime.add(Calendar.MINUTE, 1);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Getting updated " + type + " objects between " + startTime.getTime() + " and " + endTime.getTime());
        }
        return getSalesforceSoapAdapter(headers).getUpdated(type, startTime, endTime);
    }

    /**
     * Retrieves the list of individual records that have been deleted within the given timespan for the specified object.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:get-deleted-range}
     * {@sample.java ../../../doc/mule-module-sfdc.java.sample sfdc:get-deleted-range}
     *
     * @param type      Object type. The specified value must be a valid object for your organization.
     * @param startTime Starting date/time (Coordinated Universal Time (UTC)not local timezone) of the timespan for
     *                  which to retrieve the data. The API ignores the seconds portion of the specified dateTime value '
     *                  (for example, 12:30:15 is interpreted as 12:30:00 UTC).
     * @param endTime   Ending date/time (Coordinated Universal Time (UTC)not local timezone) of the timespan for
     *                  which to retrieve the data. The API ignores the seconds portion of the specified dateTime value
     *                  (for example, 12:35:15 is interpreted as 12:35:00 UTC). If not specific, the current server
     *                  time will be used.
     * @param headers   Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return {@link com.sforce.soap.partner.GetDeletedResult}
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_getdeletedrange.htm">getDeletedRange()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public GetDeletedResult getDeletedRange(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                            @Placement(group = "Information") @FriendlyName("Start Time Reference") Calendar startTime,
                                            @Placement(group = "Information") @FriendlyName("End Time Reference") @Optional Calendar endTime,
                                            @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        if (endTime == null) {
            Calendar serverTime = getSalesforceSoapAdapter().getServerTimestamp().getTimestamp();
            endTime = (Calendar) serverTime.clone();
            if (endTime.getTimeInMillis() - startTime.getTimeInMillis() < 60000) {
                endTime.add(Calendar.MINUTE, 1);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Getting deleted " + type + " objects between " + startTime.getTime() + " and " + endTime.getTime());
        }
        return getSalesforceSoapAdapter(headers).getDeleted(type, startTime, endTime);
    }

    /**
     * Describes metadata (field list and object properties) for the specified object.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:describe-sobject}
     *
     * @param type Object. The specified value must be a valid object for your organization. For a complete list
     *             of objects, see Standard Objects
     * @return {@link com.sforce.soap.partner.DescribeSObjectResult}
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_describesobject.htm">describeSObject()</a>
     * @since 4.0
     */
    @Processor(name = "describe-sobject", friendlyName = "Describe sObject")
    @OAuthProtected
    @Category(name = "Describe Calls", description = "A set of calls to describe record structure in Salesforce.")
    public DescribeSObjectResult describeSObject(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type) throws ConnectionException {
        return getSalesforceSoapAdapter().describeSObject(type);
    }

    /**
     * Retrieves the list of individual records that have been deleted between the range of now to the duration before now.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:get-deleted}
     *
     * @param type     Object type. The specified value must be a valid object for your organization.
     * @param duration The amount of time in minutes before now for which to return records from.
     * @param headers  Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return {@link GetDeletedResult}
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_getdeleted.htm">getDeleted()</a>
     * @since 4.2
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public GetDeletedResult getDeleted(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                       @Placement(group = "Information") int duration,
                                       @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        Calendar serverTime = getSalesforceSoapAdapter().getServerTimestamp().getTimestamp();
        Calendar startTime = (Calendar) serverTime.clone();
        Calendar endTime = (Calendar) serverTime.clone();

        startTime.add(Calendar.MINUTE, -(duration));
        return getDeletedRange(type, startTime, endTime, headers);
    }

    /**
     * Retrieves the list of individual records that have been updated between the range of now to the duration before now.
     * <p/>
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:get-updated}
     *
     * @param type     Object type. The specified value must be a valid object for your organization.
     * @param duration The amount of time in minutes before now for which to return records from.
     * @param headers  Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return {@link GetUpdatedResult} object containing an array of GetUpdatedResult objects containing the ID of each
     * created or updated object and the date/time (Coordinated Universal Time (UTC) time zone) on which it was created
     * or updated, respectively
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_getupdated.htm">getUpdated()</a>
     */
    @Processor
    @OAuthProtected
    @Category(name = "Core Calls", description = "A set of calls that compromise the core of the API.")
    public GetUpdatedResult getUpdated(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                       @Placement(group = "Information") int duration,
                                       @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException {
        Calendar serverTime = getSalesforceSoapAdapter().getServerTimestamp().getTimestamp();
        Calendar startTime = (Calendar) serverTime.clone();
        Calendar endTime = (Calendar) serverTime.clone();

        startTime.add(Calendar.MINUTE, -(duration));
        return getUpdatedRange(type, startTime, endTime, headers);
    }

    /**
     * Retrieves the list of records that have been updated between the last time this method was called and now. This
     * method will save the timestamp of the latest date covered by Salesforce represented by {@link GetUpdatedResult#latestDateCovered}.
     * IMPORTANT: In order to use this method in a reliable way user must ensure that right after this method returns the result is
     * stored in a persistent way since the timestamp of the latest . In order to reset the latest update time
     * use {@link #resetUpdatedObjectsTimestamp(String) resetUpdatedObjectsTimestamp}
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:get-updated-objects}
     *
     * @param type              Object type. The specified value must be a valid object for your organization.
     * @param initialTimeWindow Time window (in minutes) used to calculate the start time (in time range) the first time
     *                          this operation is called. E.g: if initialTimeWindow equals 2, the start time will be
     *                          the current time (now) minus 2 minutes, then the range to retrieve the updated object will
     *                          be (now - 2 minutes; now). After first call the start time will be calculated from the
     *                          object store getting the last time this operation was exec
     * @param fields            The fields to retrieve for the updated objects
     * @param headers           Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @return List with the updated objects in the calculated time range
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @throws ObjectStoreException {@link org.mule.api.store.ObjectStoreException} when there is an error
     * @api.doc This operation extends <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_getupdated.htm">getUpdated()</a>
     */
    @Processor
    @OAuthProtected
    @Category(name = "Utility Calls", description = "API calls that your client applications can invoke to obtain the system timestamp, user information, and change user passwords.")
    public List<Map<String, Object>> getUpdatedObjects(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type,
                                                       @Placement(group = "Information") int initialTimeWindow,
                                                       @Default("#[payload]") @Placement(group = "Fields") List<String> fields,
                                                       @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws ConnectionException, ObjectStoreException {

        Calendar now = (Calendar) getSalesforceSoapAdapter().getServerTimestamp().getTimestamp().clone();
        boolean initialTimeWindowUsed = false;
        ObjectStoreHelper objectStoreHelper = getObjectStoreHelper(getSalesforceSoapAdapter().getConfig().getUsername());
        Calendar startTime = objectStoreHelper.getTimestamp(type);
        if (startTime == null) {
            startTime = (Calendar) now.clone();
            startTime.add(Calendar.MINUTE, -1 * initialTimeWindow);
            initialTimeWindowUsed = true;
        }

        GetUpdatedResult getUpdatedResult = getUpdatedRange(type, startTime, now, headers);

        if (getUpdatedResult.getLatestDateCovered().equals(startTime)
            && !initialTimeWindowUsed && getUpdatedResult.getIds().length > 0) {
                LOGGER.debug("Ignoring duplicated results from getUpdated() call");
                return Collections.emptyList();
        }

        List<Map<String, Object>> updatedObjects = retrieve(type, Arrays.asList(getUpdatedResult.getIds()), fields, headers);
        objectStoreHelper.updateTimestamp(getUpdatedResult, type);
        return updatedObjects;
    }

    /**
     * Resets the timestamp of the last updated object. After resetting this, a call to {@link this#getUpdatedObjects} will
     * use the initialTimeWindow to get the updated objects. If no timeObjectStore has been explicitly specified and {@link this#getUpdatedObjects}
     * has not been called then calling this method has no effect.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:reset-updated-objects-timestamp}
     *
     * @param type The object type for which the timestamp should be reset.
     * @throws org.mule.api.store.ObjectStoreException {@link com.sforce.ws.ConnectionException} when there is an error
     */
    @Processor
    @Category(name = "Utility Calls", description = "API calls that your client applications can invoke to obtain the system timestamp, user information, and change user passwords.")
    public void resetUpdatedObjectsTimestamp(@MetaDataKeyParam @Placement(group = "Information") @FriendlyName("sObject Type") String type) throws ObjectStoreException {
        if (getTimeObjectStore() == null) {
            LOGGER.warn("Trying to reset updated objects timestamp but no object store has been set, was getUpdatedObjects ever executed?");
            return;
        }
        ObjectStoreHelper objectStoreHelper = getObjectStoreHelper(getSalesforceSoapAdapter().getConfig().getUsername());
        objectStoreHelper.resetTimestamps(type);
    }

    /**
     * Change the password of a User or SelfServiceUser to a value that you specify.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:set-password}
     *
     * @param userId      The user to set the password for.
     * @param newPassword The new password for the user.
     * @param headers     Salesforce Headers <a href="http://www.salesforce.com/us/developer/docs/api/Content/soap_headers.htm">More Info</a>
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     */
    @Processor
    @Category(name = "Utility Calls", description = "API calls that your client applications can invoke to obtain the system timestamp, user information, and change user passwords.")
    public void setPassword(@Placement(group = "Information") @FriendlyName("User ID") String userId,
                            @Placement(group = "Information") @FriendlyName("Password") @Password String newPassword,
                            @Placement(group = "Salesforce SOAP Headers") @FriendlyName("Headers") @Optional Map<SalesforceHeader, Object> headers) throws Exception {
        getSalesforceSoapAdapter(headers).setPassword(userId, newPassword);
    }


    /**
     * Creates a topic which represents a query that is the basis for notifying
     * listeners of changes to records in an organization.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:publish-topic}
     *
     * @param topicName   Descriptive name of the push topic, such as MyNewCases or TeamUpdatedContacts. The
     *                    maximum length is 25 characters. This value identifies the channel.
     * @param description Description of what kinds of records are returned by the query. Limit: 400 characters
     * @param query       The SOQL query statement that determines which records' changes trigger events to be sent to
     *                    the channel. Maximum length: 1200 characters
     * @throws Exception {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_streaming/Content/pushtopic.htm">Push Topic</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Streaming API", description = "Create topics, to which applications can subscribe, receiving asynchronous notifications of changes to data in Salesforce, via the Bayeux protocol.")
    public void publishTopic(@Placement(group = "Information") String topicName,
                             @Placement(group = "Information") String query,
                             @Placement(group = "Information") @Optional String description) throws Exception {
        QueryResult result = getSalesforceSoapAdapter().query("SELECT Id FROM PushTopic WHERE Name = '" + topicName + "'");
        if (result.getSize() == 0) {
            SObject pushTopic = new SObject();
            pushTopic.setType("PushTopic");
            pushTopic.setField("ApiVersion", getSalesforceStrategy().getApiVersion());
            if (description != null) {
                pushTopic.setField("Description", description);
            }

            pushTopic.setField("Name", topicName);
            pushTopic.setField("Query", query);

            SaveResult[] saveResults = getSalesforceSoapAdapter().create(new SObject[]{pushTopic});
            if (!saveResults[0].isSuccess()) {
                throw new SalesforceException(saveResults[0].getErrors()[0].getStatusCode(), saveResults[0].getErrors()[0].getMessage());
            }
        } else {
            SObject pushTopic = result.getRecords()[0];
            if (description != null) {
                pushTopic.setField("Description", description);
            }

            pushTopic.setField("Query", query);

            SaveResult[] saveResults = getSalesforceSoapAdapter().update(new SObject[]{pushTopic});
            if (!saveResults[0].isSuccess()) {
                throw new SalesforceException(saveResults[0].getErrors()[0].getStatusCode(), saveResults[0].getErrors()[0].getMessage());
            }
        }
    }

    /**
     * Retrieves personal information for the user associated with the current session.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:get-user-info}
     *
     * @return {@link com.sforce.soap.partner.GetUserInfoResult}
     * @throws ConnectionException {@link com.sforce.ws.ConnectionException} when there is an error
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_getuserinfo.htm">getUserInfo()</a>
     * @since 4.0
     */
    @Processor
    @OAuthProtected
    @Category(name = "Utility Calls", description = "API calls that your client applications can invoke to obtain the system timestamp, user information, and change user passwords.")
    public GetUserInfoResult getUserInfo() throws ConnectionException {
        return getSalesforceSoapAdapter().getUserInfo();
    }
    
    /**
     * Create metadata
     * <p/>
	 * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:create-metadata}
	 * 
     * @param type metadata type
     * @param objects Objects
     * @return An array of {@link com.sforce.soap.metadata.SaveResult}
     * @throws Exception when there is an error
     */
    @Processor
	@OAuthProtected
	@Category(name = "Metadata Calls", description = "A set of calls that compromise the metadata of the API.")
	@MetaDataScope(MetadataCategory.class)
	public List<com.sforce.soap.metadata.SaveResult> createMetadata(@MetaDataKeyParam String type,
									@FriendlyName("Metadata Objects") @Default("#[payload]") List<Map<String, Object>> objects)
			throws Exception {
		return MetadataService.callCreateUpdateService(getSalesforceMetadaAdapter(), type, objects, MetadataOperationType.CREATE);
	}
    
    /**
     * Update metadata
     * <p/>
	 * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:update-metadata}
	 * 
     * @param type metadata type
     * @param objects Objects
     * @return An array of {@link com.sforce.soap.metadata.SaveResult}
     * @throws Exception when there is an error
     */
    @Processor
	@OAuthProtected
	@Category(name = "Metadata Calls", description = "A set of calls that compromise the metadata of the API.")
	@MetaDataScope(MetadataCategory.class)
	public List<com.sforce.soap.metadata.SaveResult> updateMetadata(@MetaDataKeyParam String type,
									@FriendlyName("Metadata Objects") @Default("#[payload]") List<Map<String, Object>> objects)
			throws Exception {
		return MetadataService.callCreateUpdateService(getSalesforceMetadaAdapter(), type, objects, MetadataOperationType.UPDATE);
	}
    
    /**
     * Upsert metadata
     * <p/>
	 * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:upsert-metadata}
	 * 
     * @param type metadata type
     * @param objects Objects
     * @return An array of {@link com.sforce.soap.metadata.UpsertResult}
     * @throws Exception when there is an error
     */
    @Processor
	@OAuthProtected
	@Category(name = "Metadata Calls", description = "A set of calls that compromise the metadata of the API.")
	@MetaDataScope(MetadataCategory.class)
	public List<com.sforce.soap.metadata.UpsertResult> upsertMetadata(@MetaDataKeyParam String type,
									@FriendlyName("Metadata Objects") @Default("#[payload]") List<Map<String, Object>> objects)
			throws Exception {
		return MetadataService.callUpsertService(getSalesforceMetadaAdapter(), type, objects);
	}
	
	/**
	 * Delete metadata
	 * <p/>
	 * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:delete-metadata}
	 * 
	 * @param type metadata type
	 * @param fullNames full names
	 * @return An array of {@link com.sforce.soap.metadata.DeleteResult}
	 * @throws Exception when there is an error
	 */
	@Processor
	@OAuthProtected
	@Category(name = "Metadata Calls", description = "A set of calls that compromise the metadata of the API.")
	@MetaDataScope(MetadataCategory.class)
	public List<com.sforce.soap.metadata.DeleteResult> deleteMetadata(@MetaDataKeyParam String type,
									@FriendlyName("Full Names") @Default("#[payload]") List<String> fullNames)
			throws Exception {

		return MetadataService.callDeleteService(getSalesforceMetadaAdapter(), type, fullNames);
	}
	
	/**
	 * Rename metadata
	 * <p/>
	 * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:rename-metadata}
	 * 
	 * @param type metadata type
	 * @param oldFullName old full names
	 * @param newFullName new full names
	 * @return {@link com.sforce.soap.metadata.SaveResult}
	 * @throws Exception when there is an error
	 */
	@Processor
	@OAuthProtected
	@Category(name = "Metadata Calls", description = "A set of calls that compromise the metadata of the API.")
	@MetaDataScope(MetadataCategory.class)
	public com.sforce.soap.metadata.SaveResult renameMetadata(@MetaDataKeyParam String type,
									@FriendlyName("Old Full Name") @Default("#[payload]") String oldFullName,
                                    @FriendlyName("New Full Name") /*@Default("#[payload]")*/ String newFullName) //TODO: Cristian, please choose which one should be the #[payload]
    throws Exception {

		return MetadataService.callRenameService(getSalesforceMetadaAdapter(), type, oldFullName, newFullName);
	}
	
	/**
	 * Read metadata
	 * <p/>
	 * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:read-metadata}
	 * 
	 * @param type metadata type
	 * @param fullNames full names
	 * @return {@link com.sforce.soap.metadata.ReadResult}
	 * @throws Exception when there is an error
	 */
	@Processor
	@OAuthProtected
	@Category(name = "Metadata Calls", description = "A set of calls that compromise the metadata of the API.")
	@MetaDataScope(MetadataCategory.class)
	public List<Map<String, Object>> readMetadata(@MetaDataKeyParam String type,
									@FriendlyName("Full Names") @Default("#[payload]") List<String> fullNames)
			throws Exception {

		ReadResult readResult =  MetadataService.callReadService(getSalesforceMetadaAdapter(), type, fullNames);
		Metadata[] metadataObjects =  readResult.getRecords();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (metadataObjects != null && metadataObjects.length > 0) {
			Class metadataObjClass = MetadataType.valueOf(type).getMetadataEntityClass();
			if (metadataObjClass != null) {
				for (Metadata metadataObj : metadataObjects) {
					Map<String, Object> beanMap = BeanUtils.describe(metadataObjClass.cast(metadataObj));
					result.add(beanMap);
				}
			}
		}
		return result;
	}
	
	/**
	 * List metadata
	 * <p/>
	 * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:list-metadata}
	 * 
	 * @param type metadata type
	 * @return An array of {@link com.sforce.soap.metadata.FileProperties}
	 * @throws Exception when there is an error
	 */
	@Processor
	@OAuthProtected
	@Category(name = "Metadata Calls", description = "A set of calls that compromise the metadata of the API.")
	@MetaDataScope(MetadataCategory.class)
	public List<FileProperties> listMetadata(@MetaDataKeyParam String type)
			throws Exception {

		MetadataType metadataType = MetadataType.valueOf(type);
		ListMetadataQuery query = new ListMetadataQuery();
		query.setType(metadataType.getDisplayName());
		FileProperties[] fileProperties = getSalesforceMetadaAdapter().listMetadata(new ListMetadataQuery[] {query}, getSalesforceStrategy().getApiVersion());
		return Arrays.asList(fileProperties);
	}
	
	/**
	 * Describe metadata
	 * <p/>
	 * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:describe-metadata}
	 * 
	 * @return {@link com.sforce.soap.metadata.DescribeMetadataResult}
	 * @throws Exception when there is an error
	 */
	@Processor
	@OAuthProtected
	@Category(name = "Metadata Calls", description = "A set of calls that compromise the metadata of the API.")
	@MetaDataScope(MetadataCategory.class)
	public DescribeMetadataResult describeMetadata()
			throws Exception {

		return getSalesforceMetadaAdapter().describeMetadata(getSalesforceStrategy().getApiVersion());
	}

    /**
     * Subscribe to a topic.
     * <p/>
     * {@sample.xml ../../../doc/mule-module-sfdc.xml.sample sfdc:subscribe-topic}
     *
     * @param topic    The name of the topic to subscribe to
     * @param callback The callback to be called when a message is received
     * @return {@link org.mule.api.callback.StopSourceCallback}
     * @api.doc <a href="http://www.salesforce.com/us/developer/docs/api_streaming/index_Left.htm">Streaming API</a>
     * @since 4.0
     */
    @Source(primaryNodeOnly = true, threadingModel = SourceThreadingModel.NONE)
    @OAuthProtected
    @Category(name = "Streaming API", description = "Create topics, to which applications can subscribe, receiving asynchronous notifications of changes to data in Salesforce, via the Bayeux protocol.")
    public StopSourceCallback subscribeTopic(final String topic, final SourceCallback callback) {
        final String topicName = "/topic" + topic;
        boolean subscribed = false;

        if (getSalesforceStrategy().isReadyToSubscribe()) {
            getSalesforceStrategy().subscribe(topicName, callback);
            subscribed = true;
        }

        getSalesforceStrategy().getSubscriptions().add(new Subscription(topicName, callback, subscribed));

        return new StopSourceCallback() {
            @Override
            public void stop() throws Exception {
                getSalesforceStrategy().getBayeuxClient().unsubscribe(topicName);
            }
        };
    }

    public synchronized void setObjectStoreManager(ObjectStoreManager objectStoreManager) {
        this.objectStoreManager = objectStoreManager;
    }

    public synchronized void setTimeObjectStore(ObjectStore<? extends Serializable> timeObjectStore) {
        this.timeObjectStore = timeObjectStore;
    }

    public void setRegistry(MuleRegistry registry) {
        this.registry = registry;
    }

    private BatchInfo createBatchAndCompleteRequest(JobInfo jobInfo, List<Map<String, Object>> objects) throws Exception {
        BatchRequest batchRequest = getSalesforceRestAdapter().createBatch(jobInfo);
        try {
            batchRequest.addSObjects(SalesforceUtils.toAsyncSObjectList(objects, getBatchSobjectMaxDepth()));
            return batchRequest.completeRequest();
        } catch (AsyncApiException e) {
            /**
             * Added custom handler if {@link com.sforce.async.BatchRequest#completeRequest()} fails while processing.
             * If so, throws the correct exception to reconnect.
             * TODO check if this is really necessary, as the Salesforce API has already make a successful response.
             */
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Problem when completing the request from the batch " + jobInfo.getId() + ", threw " + e.getClass());
            }

            throw SalesforceExceptionHandlerAdapter.analyzeRestException(e);
        }
    }

    private BatchInfo createBatchForQuery(JobInfo jobInfo, InputStream query) throws AsyncApiException {
        return getSalesforceRestAdapter().createBatchFromStream(jobInfo, query);
    }

    private JobInfo createJobInfo(OperationEnum op, String type) throws AsyncApiException {
        return createJobInfo(op, type, null, null, null);
    }

    private JobInfo createJobInfo(OperationEnum op, String type, String externalIdFieldName, ContentType contentType, ConcurrencyMode concurrencyMode) throws AsyncApiException {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setOperation(op);
        jobInfo.setObject(type);
        if (externalIdFieldName != null) {
            jobInfo.setExternalIdFieldName(externalIdFieldName);
        }
        if (contentType != null) {
            jobInfo.setContentType(contentType);
        }
        if (concurrencyMode != null) {
            jobInfo.setConcurrencyMode(concurrencyMode);
        }
        return getSalesforceRestAdapter().createJob(jobInfo);
    }

	protected SObject toSObject(String type, Map<String, Object> map) {
        SObject sObject = new SObject();
        sObject.setType(type);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if ("fieldsToNull".equals(key)) {
                sObject.setFieldsToNull((String[]) entry.getValue());
            } else if (entry.getValue() instanceof Map) {
                sObject.setField(key, toSObject(key, SalesforceUtils.toSObjectMap((Map) entry.getValue())));
            } else {
                sObject.setField(key, entry.getValue());
            }
        }
        return sObject;
    }

    private synchronized ObjectStoreHelper getObjectStoreHelper(String username) {
        if (objectStoreHelper == null) {
            if (timeObjectStore == null) {
                timeObjectStore = registry.lookupObject(MuleProperties.DEFAULT_USER_OBJECT_STORE_NAME);
                if (timeObjectStore == null) {
                    timeObjectStore = objectStoreManager.getObjectStore(username, true);
                }
                if (timeObjectStore == null) {
                    throw new IllegalArgumentException("Unable to acquire an object store.");
                }
            }
            objectStoreHelper = new ObjectStoreHelper(username, timeObjectStore);
        }
        return objectStoreHelper;
    }

    public ObjectStore<? extends Serializable> getTimeObjectStore() {
        return timeObjectStore;
    }

    public Integer getBatchSobjectMaxDepth() {
        return batchSobjectMaxDepth;
    }

    public void setBatchSobjectMaxDepth(Integer batchSobjectMaxDepth) {
        this.batchSobjectMaxDepth = batchSobjectMaxDepth;
    }

    @Override
    public void setMuleContext(MuleContext context) {
        setObjectStoreManager((ObjectStoreManager) context.getRegistry().get(MuleProperties.OBJECT_STORE_MANAGER));
        setRegistry((MuleRegistry) context.getRegistry());
    }

    protected boolean isDateField(Object object) {
        return object instanceof Date || object instanceof GregorianCalendar
                || object instanceof Calendar;
    }

    protected String convertDateToString(Object object) {
        return new DateTime(object).toString();
    }

    protected void registerTransformers() {
        synchronized (this.registry) {
            try {
                if (registry.lookupObject(SaveResultToBulkOperationTransformer.class) == null) {
                    this.registry.registerTransformer(new SaveResultToBulkOperationTransformer());
                }

                if (registry.lookupObject(UpsertResultToBulkOperationTransformer.class) == null) {
                    this.registry.registerTransformer(new UpsertResultToBulkOperationTransformer());
                }
            } catch (MuleException e) {
                throw new RuntimeException("Exception found trying to register bulk transformers", e);
            }
        }
    }

    public CustomPartnerConnection getSalesforceSoapAdapter() {
        return getSalesforceSoapAdapter(new HashMap<SalesforceHeader, Object>());
    }

    public CustomPartnerConnection getSalesforceSoapAdapter(Map<SalesforceHeader, Object> headers) {
        return SalesforceSoapAdapter.adapt(getSalesforceStrategy().getCustomPartnerConnection(), headers);
    }

    public BulkConnection getSalesforceRestAdapter() {
        return SalesforceRestAdapter.adapt(getSalesforceStrategy().getBulkConnection());
    }
    
    public CustomMetadataConnection getSalesforceMetadaAdapter() {
    	return SalesforceMetadataAdapter.adapt(getSalesforceStrategy().getCustomMetadataConnection());
    }
}
