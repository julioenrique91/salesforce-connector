
package org.mule.modules.salesforce.sources;

import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MessagingException;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.callback.SourceCallback;
import org.mule.api.callback.StopSourceCallback;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.api.source.ClusterizableMessageSource;
import org.mule.config.i18n.CoreMessages;
import org.mule.modules.salesforce.BaseSalesforceConnector;
import org.mule.security.oauth.callback.ProcessCallback;
import org.mule.security.oauth.processor.AbstractListeningMessageProcessor;


/**
 * SubscribeTopicMessageSource wraps {@link org.mule.modules.salesforce.BaseSalesforceConnector#subscribeTopic(java.lang.String, org.mule.api.callback.SourceCallback)} method in {@link BaseSalesforceConnector } as a message source capable of generating Mule events.  The POJO's method is invoked in its own thread.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T03:30:10-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class SubscribeTopicMessageSource
    extends AbstractListeningMessageProcessor
    implements FlowConstructAware, MuleContextAware, Startable, Stoppable, ClusterizableMessageSource
{

    protected Object topic;
    protected String _topicType;
    private StopSourceCallback stopSourceCallback;

    public SubscribeTopicMessageSource(String operationName) {
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

    /**
     * Sets topic
     * 
     * @param value Value to set
     */
    public void setTopic(Object value) {
        this.topic = value;
    }

    /**
     * Method to be called when Mule instance gets started.
     * 
     */
    public void start()
        throws MuleException
    {
        Object moduleObject = null;
        try {
            moduleObject = findOrCreate(ProcessAdapter.class, false, null);
            ProcessTemplate<Object, Object> processTemplate = ((ProcessAdapter<Object> ) moduleObject).getProcessTemplate();
            final SourceCallback sourceCallback = this;
            final String transformedTopic = ((String) transform(getMuleContext(), ((MuleEvent) null), getClass().getDeclaredField("_topicType").getGenericType(), null, topic));
            processTemplate.execute(new ProcessCallback<Object,Object>() {


                public List<Class<? extends Exception>> getManagedExceptions() {
                    return null;
                }

                public boolean isProtected() {
                    return false;
                }

                public Object process(Object object)
                    throws Exception
                {
                    stopSourceCallback = ((BaseSalesforceConnector) object).subscribeTopic(transformedTopic, sourceCallback);
                    return null;
                }

            }
            , null, ((MuleEvent) null));
        } catch (Exception e) {
            getMuleContext().getExceptionListener().handleException(e);
        }
    }

    /**
     * Method to be called when Mule instance gets stopped.
     * 
     */
    public void stop()
        throws MuleException
    {
        if (stopSourceCallback!= null) {
            try {
                stopSourceCallback.stop();
            } catch (Exception e) {
                throw new MessagingException(CoreMessages.failedToStop("subscribeTopic"), ((MuleEvent) null), e);
            }
        }
    }

}
