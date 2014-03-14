
package org.mule.modules.salesforce.processors;

import java.util.regex.Pattern;
import javax.annotation.Generated;
import org.mule.modules.salesforce.SalesforceOAuthDisplay;
import org.mule.modules.salesforce.SalesforceOAuthImmediate;
import org.mule.modules.salesforce.SalesforceOAuthPrompt;
import org.mule.modules.salesforce.oauth.SalesforceOAuthConnectorOAuthManager;
import org.mule.security.oauth.processor.BaseOAuth2AuthorizeMessageProcessor;

@Generated(value = "Mule DevKit Version 3.5.0-M4", date = "2014-03-14T09:57:57-05:00", comments = "Build M4.1875.17b58a3")
public class AuthorizeMessageProcessor
    extends BaseOAuth2AuthorizeMessageProcessor<SalesforceOAuthConnectorOAuthManager>
{

    private final static Pattern AUTH_CODE_PATTERN = Pattern.compile("code=([^&]+)");
    private Object display;
    private SalesforceOAuthDisplay _displayType;
    private Object immediate;
    private SalesforceOAuthImmediate _immediateType;
    private Object prompt;
    private SalesforceOAuthPrompt _promptType;

    /**
     * Sets display
     * 
     * @param value Value to set
     */
    public void setDisplay(Object value) {
        this.display = value;
    }

    /**
     * Sets immediate
     * 
     * @param value Value to set
     */
    public void setImmediate(Object value) {
        this.immediate = value;
    }

    /**
     * Sets prompt
     * 
     * @param value Value to set
     */
    public void setPrompt(Object value) {
        this.prompt = value;
    }

    @Override
    protected String getAuthCodeRegex() {
        return AUTH_CODE_PATTERN.pattern();
    }

    @Override
    protected Class<SalesforceOAuthConnectorOAuthManager> getOAuthManagerClass() {
        return SalesforceOAuthConnectorOAuthManager.class;
    }

}
