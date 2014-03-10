
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import org.mule.config.MuleManifest;
import org.mule.modules.salesforce.oauth.SalesforceOAuthConnectorOAuthManager;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

@Generated(value = "Mule DevKit Version 3.5.0-M4", date = "2014-03-10T01:04:06-05:00", comments = "Build M4.1875.17b58a3")
public class SalesforceOAuthConnectorConfigDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{

    private static Logger logger = LoggerFactory.getLogger(SalesforceOAuthConnectorConfigDefinitionParser.class);

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        parseConfigName(element);
        BeanDefinitionBuilder builder = getBeanDefinitionBuilder(parserContext);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        setInitMethodIfNeeded(builder, SalesforceOAuthConnectorOAuthManager.class);
        setDestroyMethodIfNeeded(builder, SalesforceOAuthConnectorOAuthManager.class);
        parseProperty(builder, element, "consumerKey", "consumerKey");
        parseProperty(builder, element, "consumerSecret", "consumerSecret");
        if (hasAttribute(element, "timeObjectStore-ref")) {
            if (element.getAttribute("timeObjectStore-ref").startsWith("#")) {
                builder.addPropertyValue("timeObjectStore", element.getAttribute("timeObjectStore-ref"));
            } else {
                builder.addPropertyValue("timeObjectStore", new RuntimeBeanReference(element.getAttribute("timeObjectStore-ref")));
            }
        }
        parseProperty(builder, element, "clientId", "clientId");
        parseProperty(builder, element, "assignmentRuleId", "assignmentRuleId");
        parseProperty(builder, element, "useDefaultRule", "useDefaultRule");
        parseProperty(builder, element, "batchSobjectMaxDepth", "batchSobjectMaxDepth");
        parseProperty(builder, element, "allowFieldTruncationSupport", "allowFieldTruncationSupport");
        parseProperty(builder, element, "name", "name");
        parseProperty(builder, element, "authorizationUrl");
        parseProperty(builder, element, "accessTokenUrl");
        parseProperty(builder, element, "onNoToken");
        Element httpCallbackConfigElement = DomUtils.getChildElementByTagName(element, "oauth-callback-config");
        if (httpCallbackConfigElement!= null) {
            parseProperty(builder, httpCallbackConfigElement, "domain");
            parseProperty(builder, httpCallbackConfigElement, "localPort");
            parseProperty(builder, httpCallbackConfigElement, "remotePort");
            parseProperty(builder, httpCallbackConfigElement, "async");
            parseProperty(builder, httpCallbackConfigElement, "path");
            parseProperty(builder, httpCallbackConfigElement, "defaultAccessTokenId");
            if (hasAttribute(httpCallbackConfigElement, "connector-ref")) {
                builder.addPropertyValue("connector", new RuntimeBeanReference(httpCallbackConfigElement.getAttribute("connector-ref")));
            }
        }
        Element oauthStoreConfigElement = DomUtils.getChildElementByTagName(element, "oauth-store-config");
        if (oauthStoreConfigElement!= null) {
            parsePropertyRef(builder, oauthStoreConfigElement, "objectStore-ref", "accessTokenObjectStore");
        }
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        return definition;
    }

    private BeanDefinitionBuilder getBeanDefinitionBuilder(ParserContext parserContext) {
        try {
            return BeanDefinitionBuilder.rootBeanDefinition(SalesforceOAuthConnectorOAuthManager.class.getName());
        } catch (NoClassDefFoundError noClassDefFoundError) {
            String muleVersion = "";
            try {
                muleVersion = MuleManifest.getProductVersion();
            } catch (Exception _x) {
                logger.error("Problem while reading mule version");
            }
            logger.error(("Cannot launch the mule app, the configuration [config-with-oauth] within the connector [sfdc] is not supported in mule "+ muleVersion));
            throw new BeanDefinitionParsingException(new Problem(("Cannot launch the mule app, the configuration [config-with-oauth] within the connector [sfdc] is not supported in mule "+ muleVersion), new Location(parserContext.getReaderContext().getResource()), null, noClassDefFoundError));
        }
    }

}
