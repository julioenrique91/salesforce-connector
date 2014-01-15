
package org.mule.modules.salesforce.processors;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.common.DefaultResult;
import org.mule.common.Result;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultPojoMetaDataModel;
import org.mule.common.metadata.DefaultSimpleMetaDataModel;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.OperationMetaDataEnabled;
import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.datatype.DataTypeFactory;
import org.mule.modules.salesforce.BaseSalesforceConnector;
import org.mule.modules.salesforce.QueryResultObject;
import org.mule.modules.salesforce.exception.SalesforceSessionExpiredException;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * PaginatedQueryMessageProcessor invokes the {@link org.mule.modules.salesforce.BaseSalesforceConnector#paginatedQuery(java.lang.String, org.mule.modules.salesforce.QueryResultObject, java.lang.Boolean)} method in {@link BaseSalesforceConnector }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-15T04:12:08-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class PaginatedQueryMessageProcessor
    extends AbstractConnectedProcessor
    implements MessageProcessor, OperationMetaDataEnabled
{

    protected Object query;
    protected String _queryType;
    protected Object queryResultObject;
    protected QueryResultObject _queryResultObjectType;
    protected Object withDeletedRecords;
    protected Boolean _withDeletedRecordsType;

    public PaginatedQueryMessageProcessor(String operationName) {
        super(operationName);
    }

    /**
     * Obtains the expression manager from the Mule context and initialises the connector. If a target object  has not been set already it will search the Mule registry for a default one.
     * 
     * @throws InitialisationException
     */
    public void initialise()
        throws InitialisationException
    {
    }

    @Override
    public void start()
        throws MuleException
    {
        super.start();
    }

    @Override
    public void stop()
        throws MuleException
    {
        super.stop();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Sets query
     * 
     * @param value Value to set
     */
    public void setQuery(Object value) {
        this.query = value;
    }

    /**
     * Sets withDeletedRecords
     * 
     * @param value Value to set
     */
    public void setWithDeletedRecords(Object value) {
        this.withDeletedRecords = value;
    }

    /**
     * Sets queryResultObject
     * 
     * @param value Value to set
     */
    public void setQueryResultObject(Object value) {
        this.queryResultObject = value;
    }

    /**
     * Invokes the MessageProcessor.
     * 
     * @param event MuleEvent to be processed
     * @throws Exception
     */
    public MuleEvent doProcess(final MuleEvent event)
        throws Exception
    {
        Object moduleObject = null;
        try {
            moduleObject = findOrCreate(ProcessAdapter.class, false, event);
            final String _transformedQuery = ((String) evaluateAndTransform(getMuleContext(), event, PaginatedQueryMessageProcessor.class.getDeclaredField("_queryType").getGenericType(), null, query));
            final QueryResultObject _transformedQueryResultObject = ((QueryResultObject) evaluateAndTransform(getMuleContext(), event, PaginatedQueryMessageProcessor.class.getDeclaredField("_queryResultObjectType").getGenericType(), null, queryResultObject));
            final Boolean _transformedWithDeletedRecords = ((Boolean) evaluateAndTransform(getMuleContext(), event, PaginatedQueryMessageProcessor.class.getDeclaredField("_withDeletedRecordsType").getGenericType(), null, withDeletedRecords));
            Object resultPayload;
            ProcessTemplate<Object, Object> processTemplate = ((ProcessAdapter<Object> ) moduleObject).getProcessTemplate();
            resultPayload = processTemplate.execute(new ProcessCallback<Object,Object>() {


                public List<Class<? extends Exception>> getManagedExceptions() {
                    return Arrays.asList(((Class<? extends Exception> []) new Class[] {SalesforceSessionExpiredException.class }));
                }

                public boolean isProtected() {
                    return true;
                }

                public Object process(Object object)
                    throws Exception
                {
                    return ((BaseSalesforceConnector) object).paginatedQuery(_transformedQuery, _transformedQueryResultObject, _transformedWithDeletedRecords);
                }

            }
            , this, event);
            event.getMessage().setPayload(resultPayload);
            return event;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Result<MetaData> getInputMetaData() {
        return new DefaultResult<MetaData>(null, (Result.Status.SUCCESS));
    }

    @Override
    public Result<MetaData> getOutputMetaData(MetaData inputMetadata) {
        return new DefaultResult<MetaData>(new DefaultMetaData(getPojoOrSimpleModel(QueryResultObject.class)));
    }

    private MetaDataModel getPojoOrSimpleModel(Class clazz) {
        DataType dataType = DataTypeFactory.getInstance().getDataType(clazz);
        if (DataType.POJO.equals(dataType)) {
            return new DefaultPojoMetaDataModel(clazz);
        } else {
            return new DefaultSimpleMetaDataModel(dataType);
        }
    }

}
