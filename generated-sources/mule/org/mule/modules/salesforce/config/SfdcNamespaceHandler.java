
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/sfdc</code>.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T09:22:38-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class SfdcNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config-with-oauth", new SalesforceOAuthConnectorConfigDefinitionParser());
        registerBeanDefinitionParser("authorize", new AuthorizeDefinitionParser());
        registerBeanDefinitionParser("unauthorize", new UnauthorizeDefinitionParser());
        registerBeanDefinitionParser("create", new CreateDefinitionParser());
        registerBeanDefinitionParser("create-job", new CreateJobDefinitionParser());
        registerBeanDefinitionParser("close-job", new CloseJobDefinitionParser());
        registerBeanDefinitionParser("abort-job", new AbortJobDefinitionParser());
        registerBeanDefinitionParser("create-batch", new CreateBatchDefinitionParser());
        registerBeanDefinitionParser("create-batch-stream", new CreateBatchStreamDefinitionParser());
        registerBeanDefinitionParser("create-batch-for-query", new CreateBatchForQueryDefinitionParser());
        registerBeanDefinitionParser("create-bulk", new CreateBulkDefinitionParser());
        registerBeanDefinitionParser("create-single", new CreateSingleDefinitionParser());
        registerBeanDefinitionParser("update", new UpdateDefinitionParser());
        registerBeanDefinitionParser("update-single", new UpdateSingleDefinitionParser());
        registerBeanDefinitionParser("update-bulk", new UpdateBulkDefinitionParser());
        registerBeanDefinitionParser("upsert", new UpsertDefinitionParser());
        registerBeanDefinitionParser("upsert-bulk", new UpsertBulkDefinitionParser());
        registerBeanDefinitionParser("batch-info", new BatchInfoDefinitionParser());
        registerBeanDefinitionParser("batch-result", new BatchResultDefinitionParser());
        registerBeanDefinitionParser("batch-result-stream", new BatchResultStreamDefinitionParser());
        registerBeanDefinitionParser("query-result-stream", new QueryResultStreamDefinitionParser());
        registerBeanDefinitionParser("describe-global", new DescribeGlobalDefinitionParser());
        registerBeanDefinitionParser("retrieve", new RetrieveDefinitionParser());
        registerBeanDefinitionParser("paginated-query", new PaginatedQueryDefinitionParser());
        registerBeanDefinitionParser("query", new QueryDefinitionParser());
        registerBeanDefinitionParser("query-all", new QueryAllDefinitionParser());
        registerBeanDefinitionParser("search", new SearchDefinitionParser());
        registerBeanDefinitionParser("query-single", new QuerySingleDefinitionParser());
        registerBeanDefinitionParser("convert-lead", new ConvertLeadDefinitionParser());
        registerBeanDefinitionParser("empty-recycle-bin", new EmptyRecycleBinDefinitionParser());
        registerBeanDefinitionParser("get-server-timestamp", new GetServerTimestampDefinitionParser());
        registerBeanDefinitionParser("delete", new DeleteDefinitionParser());
        registerBeanDefinitionParser("hard-delete-bulk", new HardDeleteBulkDefinitionParser());
        registerBeanDefinitionParser("get-updated-range", new GetUpdatedRangeDefinitionParser());
        registerBeanDefinitionParser("get-deleted-range", new GetDeletedRangeDefinitionParser());
        registerBeanDefinitionParser("describe-sobject", new DescribeSObjectDefinitionParser());
        registerBeanDefinitionParser("get-deleted", new GetDeletedDefinitionParser());
        registerBeanDefinitionParser("get-updated", new GetUpdatedDefinitionParser());
        registerBeanDefinitionParser("get-updated-objects", new GetUpdatedObjectsDefinitionParser());
        registerBeanDefinitionParser("reset-updated-objects-timestamp", new ResetUpdatedObjectsTimestampDefinitionParser());
        registerBeanDefinitionParser("set-password", new SetPasswordDefinitionParser());
        registerBeanDefinitionParser("publish-topic", new PublishTopicDefinitionParser());
        registerBeanDefinitionParser("get-user-info", new GetUserInfoDefinitionParser());
        registerBeanDefinitionParser("subscribe-topic", new SubscribeTopicDefinitionParser());
        registerBeanDefinitionParser("config", new SalesforceConnectorConfigDefinitionParser());
        registerBeanDefinitionParser("create", new CreateDefinitionParser());
        registerBeanDefinitionParser("create-job", new CreateJobDefinitionParser());
        registerBeanDefinitionParser("close-job", new CloseJobDefinitionParser());
        registerBeanDefinitionParser("abort-job", new AbortJobDefinitionParser());
        registerBeanDefinitionParser("create-batch", new CreateBatchDefinitionParser());
        registerBeanDefinitionParser("create-batch-stream", new CreateBatchStreamDefinitionParser());
        registerBeanDefinitionParser("create-batch-for-query", new CreateBatchForQueryDefinitionParser());
        registerBeanDefinitionParser("create-bulk", new CreateBulkDefinitionParser());
        registerBeanDefinitionParser("create-single", new CreateSingleDefinitionParser());
        registerBeanDefinitionParser("update", new UpdateDefinitionParser());
        registerBeanDefinitionParser("update-single", new UpdateSingleDefinitionParser());
        registerBeanDefinitionParser("update-bulk", new UpdateBulkDefinitionParser());
        registerBeanDefinitionParser("upsert", new UpsertDefinitionParser());
        registerBeanDefinitionParser("upsert-bulk", new UpsertBulkDefinitionParser());
        registerBeanDefinitionParser("batch-info", new BatchInfoDefinitionParser());
        registerBeanDefinitionParser("batch-result", new BatchResultDefinitionParser());
        registerBeanDefinitionParser("batch-result-stream", new BatchResultStreamDefinitionParser());
        registerBeanDefinitionParser("query-result-stream", new QueryResultStreamDefinitionParser());
        registerBeanDefinitionParser("describe-global", new DescribeGlobalDefinitionParser());
        registerBeanDefinitionParser("retrieve", new RetrieveDefinitionParser());
        registerBeanDefinitionParser("paginated-query", new PaginatedQueryDefinitionParser());
        registerBeanDefinitionParser("query", new QueryDefinitionParser());
        registerBeanDefinitionParser("query-all", new QueryAllDefinitionParser());
        registerBeanDefinitionParser("search", new SearchDefinitionParser());
        registerBeanDefinitionParser("query-single", new QuerySingleDefinitionParser());
        registerBeanDefinitionParser("convert-lead", new ConvertLeadDefinitionParser());
        registerBeanDefinitionParser("empty-recycle-bin", new EmptyRecycleBinDefinitionParser());
        registerBeanDefinitionParser("get-server-timestamp", new GetServerTimestampDefinitionParser());
        registerBeanDefinitionParser("delete", new DeleteDefinitionParser());
        registerBeanDefinitionParser("hard-delete-bulk", new HardDeleteBulkDefinitionParser());
        registerBeanDefinitionParser("get-updated-range", new GetUpdatedRangeDefinitionParser());
        registerBeanDefinitionParser("get-deleted-range", new GetDeletedRangeDefinitionParser());
        registerBeanDefinitionParser("describe-sobject", new DescribeSObjectDefinitionParser());
        registerBeanDefinitionParser("get-deleted", new GetDeletedDefinitionParser());
        registerBeanDefinitionParser("get-updated", new GetUpdatedDefinitionParser());
        registerBeanDefinitionParser("get-updated-objects", new GetUpdatedObjectsDefinitionParser());
        registerBeanDefinitionParser("reset-updated-objects-timestamp", new ResetUpdatedObjectsTimestampDefinitionParser());
        registerBeanDefinitionParser("set-password", new SetPasswordDefinitionParser());
        registerBeanDefinitionParser("publish-topic", new PublishTopicDefinitionParser());
        registerBeanDefinitionParser("get-user-info", new GetUserInfoDefinitionParser());
        registerBeanDefinitionParser("subscribe-topic", new SubscribeTopicDefinitionParser());
    }

}
