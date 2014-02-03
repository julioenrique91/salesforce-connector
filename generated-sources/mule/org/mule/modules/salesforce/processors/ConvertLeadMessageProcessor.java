
package org.mule.modules.salesforce.processors;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Generated;
import com.sforce.soap.partner.LeadConvertResult;
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
import org.mule.modules.salesforce.exception.SalesforceSessionExpiredException;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * ConvertLeadMessageProcessor invokes the {@link org.mule.modules.salesforce.BaseSalesforceConnector#convertLead(java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.String, java.lang.Boolean)} method in {@link BaseSalesforceConnector }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-02-03T12:06:26-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class ConvertLeadMessageProcessor
    extends AbstractConnectedProcessor
    implements MessageProcessor, OperationMetaDataEnabled
{

    protected Object leadId;
    protected String _leadIdType;
    protected Object contactId;
    protected String _contactIdType;
    protected Object accountId;
    protected String _accountIdType;
    protected Object overWriteLeadSource;
    protected Boolean _overWriteLeadSourceType;
    protected Object doNotCreateOpportunity;
    protected Boolean _doNotCreateOpportunityType;
    protected Object opportunityName;
    protected String _opportunityNameType;
    protected Object convertedStatus;
    protected String _convertedStatusType;
    protected Object sendEmailToOwner;
    protected Boolean _sendEmailToOwnerType;

    public ConvertLeadMessageProcessor(String operationName) {
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
     * Sets sendEmailToOwner
     * 
     * @param value Value to set
     */
    public void setSendEmailToOwner(Object value) {
        this.sendEmailToOwner = value;
    }

    /**
     * Sets accountId
     * 
     * @param value Value to set
     */
    public void setAccountId(Object value) {
        this.accountId = value;
    }

    /**
     * Sets contactId
     * 
     * @param value Value to set
     */
    public void setContactId(Object value) {
        this.contactId = value;
    }

    /**
     * Sets doNotCreateOpportunity
     * 
     * @param value Value to set
     */
    public void setDoNotCreateOpportunity(Object value) {
        this.doNotCreateOpportunity = value;
    }

    /**
     * Sets opportunityName
     * 
     * @param value Value to set
     */
    public void setOpportunityName(Object value) {
        this.opportunityName = value;
    }

    /**
     * Sets leadId
     * 
     * @param value Value to set
     */
    public void setLeadId(Object value) {
        this.leadId = value;
    }

    /**
     * Sets overWriteLeadSource
     * 
     * @param value Value to set
     */
    public void setOverWriteLeadSource(Object value) {
        this.overWriteLeadSource = value;
    }

    /**
     * Sets convertedStatus
     * 
     * @param value Value to set
     */
    public void setConvertedStatus(Object value) {
        this.convertedStatus = value;
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
            final String _transformedLeadId = ((String) evaluateAndTransform(getMuleContext(), event, ConvertLeadMessageProcessor.class.getDeclaredField("_leadIdType").getGenericType(), null, leadId));
            final String _transformedContactId = ((String) evaluateAndTransform(getMuleContext(), event, ConvertLeadMessageProcessor.class.getDeclaredField("_contactIdType").getGenericType(), null, contactId));
            final String _transformedAccountId = ((String) evaluateAndTransform(getMuleContext(), event, ConvertLeadMessageProcessor.class.getDeclaredField("_accountIdType").getGenericType(), null, accountId));
            final Boolean _transformedOverWriteLeadSource = ((Boolean) evaluateAndTransform(getMuleContext(), event, ConvertLeadMessageProcessor.class.getDeclaredField("_overWriteLeadSourceType").getGenericType(), null, overWriteLeadSource));
            final Boolean _transformedDoNotCreateOpportunity = ((Boolean) evaluateAndTransform(getMuleContext(), event, ConvertLeadMessageProcessor.class.getDeclaredField("_doNotCreateOpportunityType").getGenericType(), null, doNotCreateOpportunity));
            final String _transformedOpportunityName = ((String) evaluateAndTransform(getMuleContext(), event, ConvertLeadMessageProcessor.class.getDeclaredField("_opportunityNameType").getGenericType(), null, opportunityName));
            final String _transformedConvertedStatus = ((String) evaluateAndTransform(getMuleContext(), event, ConvertLeadMessageProcessor.class.getDeclaredField("_convertedStatusType").getGenericType(), null, convertedStatus));
            final Boolean _transformedSendEmailToOwner = ((Boolean) evaluateAndTransform(getMuleContext(), event, ConvertLeadMessageProcessor.class.getDeclaredField("_sendEmailToOwnerType").getGenericType(), null, sendEmailToOwner));
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
                    return ((BaseSalesforceConnector) object).convertLead(_transformedLeadId, _transformedContactId, _transformedAccountId, _transformedOverWriteLeadSource, _transformedDoNotCreateOpportunity, _transformedOpportunityName, _transformedConvertedStatus, _transformedSendEmailToOwner);
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
        return new DefaultResult<MetaData>(new DefaultMetaData(getPojoOrSimpleModel(LeadConvertResult.class)));
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
