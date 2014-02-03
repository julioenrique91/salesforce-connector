
package org.mule.modules.salesforce.processors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.sforce.async.BatchInfo;
import com.sforce.async.JobInfo;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.processor.MessageProcessor;
import org.mule.common.DefaultResult;
import org.mule.common.Result;
import org.mule.common.metadata.DefaultListMetaDataModel;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultParameterizedMapMetaDataModel;
import org.mule.common.metadata.DefaultPojoMetaDataModel;
import org.mule.common.metadata.DefaultSimpleMetaDataModel;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.OperationMetaDataEnabled;
import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.datatype.DataTypeFactory;
import org.mule.modules.salesforce.BaseSalesforceConnector;
import org.mule.modules.salesforce.exception.SalesforceSessionExpiredException;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * CreateBatchMessageProcessor invokes the {@link org.mule.modules.salesforce.BaseSalesforceConnector#createBatch(com.sforce.async.JobInfo, java.util.List)} method in {@link BaseSalesforceConnector }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-02-03T12:06:26-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class CreateBatchMessageProcessor
    extends AbstractConnectedProcessor
    implements MessageProcessor, OperationMetaDataEnabled
{

    protected Object jobInfo;
    protected JobInfo _jobInfoType;
    protected Object objects;
    protected List<Map<String, Object>> _objectsType;

    public CreateBatchMessageProcessor(String operationName) {
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
     * Sets jobInfo
     * 
     * @param value Value to set
     */
    public void setJobInfo(Object value) {
        this.jobInfo = value;
    }

    /**
     * Sets objects
     * 
     * @param value Value to set
     */
    public void setObjects(Object value) {
        this.objects = value;
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
            final JobInfo _transformedJobInfo = ((JobInfo) evaluateAndTransform(getMuleContext(), event, CreateBatchMessageProcessor.class.getDeclaredField("_jobInfoType").getGenericType(), null, jobInfo));
            final List<Map<String, Object>> _transformedObjects = ((List<Map<String, Object>> ) evaluateAndTransform(getMuleContext(), event, CreateBatchMessageProcessor.class.getDeclaredField("_objectsType").getGenericType(), null, objects));
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
                    return ((BaseSalesforceConnector) object).createBatch(_transformedJobInfo, _transformedObjects);
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
        return new DefaultResult<MetaData>(new DefaultMetaData(new DefaultListMetaDataModel(new DefaultParameterizedMapMetaDataModel(getPojoOrSimpleModel(String.class), getPojoOrSimpleModel(Object.class)))));
    }

    @Override
    public Result<MetaData> getOutputMetaData(MetaData inputMetadata) {
        return new DefaultResult<MetaData>(new DefaultMetaData(getPojoOrSimpleModel(BatchInfo.class)));
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
