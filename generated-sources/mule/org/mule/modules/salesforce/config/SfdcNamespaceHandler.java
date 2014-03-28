
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import org.mule.config.MuleManifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/sfdc</code>.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-M4", date = "2014-03-28T02:39:20-05:00", comments = "Build M4.1875.17b58a3")
public class SfdcNamespaceHandler
    extends NamespaceHandlerSupport
{

    private static Logger logger = LoggerFactory.getLogger(SfdcNamespaceHandler.class);

    private void handleException(String beanName, String beanScope, NoClassDefFoundError noClassDefFoundError) {
        String muleVersion = "";
        try {
            muleVersion = MuleManifest.getProductVersion();
        } catch (Exception _x) {
            logger.error("Problem while reading mule version");
        }
        logger.error(((((("Cannot launch the mule app, the  "+ beanScope)+" [")+ beanName)+"] within the connector [sfdc] is not supported in mule ")+ muleVersion));
        throw new FatalBeanException(((((("Cannot launch the mule app, the  "+ beanScope)+" [")+ beanName)+"] within the connector [sfdc] is not supported in mule ")+ muleVersion), noClassDefFoundError);
    }

    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        try {
            this.registerBeanDefinitionParser("config-with-oauth", new SalesforceOAuthConnectorConfigDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("config", "@Config", ex);
        }
        try {
            this.registerBeanDefinitionParser("authorize", new AuthorizeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("authorize", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("authorize", new AuthorizeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("unauthorize", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create", new CreateDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-job", new CreateJobDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-job", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("close-job", new CloseJobDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("close-job", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("abort-job", new AbortJobDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("abort-job", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("job-info", new JobInfoDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("job-info", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-batch", new CreateBatchDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-batch", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-batch-stream", new CreateBatchStreamDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-batch-stream", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-batch-for-query", new CreateBatchForQueryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-batch-for-query", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-bulk", new CreateBulkDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-bulk", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-single", new CreateSingleDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-single", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("update", new UpdateDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("update", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("update-single", new UpdateSingleDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("update-single", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("update-bulk", new UpdateBulkDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("update-bulk", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("upsert", new UpsertDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("upsert", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("upsert-bulk", new UpsertBulkDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("upsert-bulk", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("batch-info", new BatchInfoDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("batch-info", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("batch-result", new BatchResultDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("batch-result", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("batch-result-stream", new BatchResultStreamDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("batch-result-stream", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("query-result-stream", new QueryResultStreamDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("query-result-stream", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("describe-global", new DescribeGlobalDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("describe-global", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("retrieve", new RetrieveDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("retrieve", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("paginated-query", new PaginatedQueryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("paginated-query", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("query", new QueryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("query", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("non-paginated-query", new NonPaginatedQueryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("non-paginated-query", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("query-all", new QueryAllDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("query-all", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("search", new SearchDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("search", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("query-single", new QuerySingleDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("query-single", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("convert-lead", new ConvertLeadDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("convert-lead", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("empty-recycle-bin", new EmptyRecycleBinDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("empty-recycle-bin", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-server-timestamp", new GetServerTimestampDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-server-timestamp", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("delete", new DeleteDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("delete", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("hard-delete-bulk", new HardDeleteBulkDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("hard-delete-bulk", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-updated-range", new GetUpdatedRangeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-updated-range", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-deleted-range", new GetDeletedRangeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-deleted-range", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("describe-sobject", new DescribeSObjectDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("describe-sobject", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-deleted", new GetDeletedDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-deleted", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-updated", new GetUpdatedDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-updated", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-updated-objects", new GetUpdatedObjectsDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-updated-objects", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("reset-updated-objects-timestamp", new ResetUpdatedObjectsTimestampDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("reset-updated-objects-timestamp", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("set-password", new SetPasswordDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("set-password", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("publish-topic", new PublishTopicDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("publish-topic", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-user-info", new GetUserInfoDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-user-info", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("subscribe-topic", new SubscribeTopicDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("subscribe-topic", "@Source", ex);
        }
        try {
            this.registerBeanDefinitionParser("config", new SalesforceConnectorConfigDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("config", "@Config", ex);
        }
        try {
            this.registerBeanDefinitionParser("create", new CreateDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-job", new CreateJobDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-job", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("close-job", new CloseJobDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("close-job", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("abort-job", new AbortJobDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("abort-job", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("job-info", new JobInfoDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("job-info", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-batch", new CreateBatchDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-batch", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-batch-stream", new CreateBatchStreamDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-batch-stream", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-batch-for-query", new CreateBatchForQueryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-batch-for-query", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-bulk", new CreateBulkDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-bulk", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("create-single", new CreateSingleDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("create-single", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("update", new UpdateDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("update", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("update-single", new UpdateSingleDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("update-single", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("update-bulk", new UpdateBulkDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("update-bulk", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("upsert", new UpsertDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("upsert", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("upsert-bulk", new UpsertBulkDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("upsert-bulk", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("batch-info", new BatchInfoDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("batch-info", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("batch-result", new BatchResultDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("batch-result", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("batch-result-stream", new BatchResultStreamDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("batch-result-stream", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("query-result-stream", new QueryResultStreamDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("query-result-stream", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("describe-global", new DescribeGlobalDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("describe-global", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("retrieve", new RetrieveDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("retrieve", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("paginated-query", new PaginatedQueryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("paginated-query", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("query", new QueryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("query", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("non-paginated-query", new NonPaginatedQueryDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("non-paginated-query", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("query-all", new QueryAllDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("query-all", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("search", new SearchDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("search", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("query-single", new QuerySingleDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("query-single", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("convert-lead", new ConvertLeadDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("convert-lead", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("empty-recycle-bin", new EmptyRecycleBinDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("empty-recycle-bin", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-server-timestamp", new GetServerTimestampDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-server-timestamp", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("delete", new DeleteDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("delete", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("hard-delete-bulk", new HardDeleteBulkDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("hard-delete-bulk", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-updated-range", new GetUpdatedRangeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-updated-range", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-deleted-range", new GetDeletedRangeDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-deleted-range", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("describe-sobject", new DescribeSObjectDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("describe-sobject", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-deleted", new GetDeletedDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-deleted", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-updated", new GetUpdatedDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-updated", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-updated-objects", new GetUpdatedObjectsDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-updated-objects", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("reset-updated-objects-timestamp", new ResetUpdatedObjectsTimestampDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("reset-updated-objects-timestamp", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("set-password", new SetPasswordDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("set-password", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("publish-topic", new PublishTopicDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("publish-topic", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-user-info", new GetUserInfoDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-user-info", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("subscribe-topic", new SubscribeTopicDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("subscribe-topic", "@Source", ex);
        }
    }

}
