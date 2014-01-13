
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import org.mule.modules.salesforce.processors.PaginatedQueryMessageProcessor;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T03:30:10-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class PaginatedQueryDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{


    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(PaginatedQueryMessageProcessor.class.getName());
        builder.addConstructorArgValue("paginatedQuery");
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        parseConfigRef(element, builder);
        parseProperty(builder, element, "query", "query");
        if (hasAttribute(element, "queryResultObject-ref")) {
            if (element.getAttribute("queryResultObject-ref").startsWith("#")) {
                builder.addPropertyValue("queryResultObject", element.getAttribute("queryResultObject-ref"));
            } else {
                builder.addPropertyValue("queryResultObject", (("#[registry:"+ element.getAttribute("queryResultObject-ref"))+"]"));
            }
        }
        parseProperty(builder, element, "withDeletedRecords", "withDeletedRecords");
        parseProperty(builder, element, "username", "username");
        parseProperty(builder, element, "password", "password");
        parseProperty(builder, element, "securityToken", "securityToken");
        parseProperty(builder, element, "url", "url");
        parseProperty(builder, element, "proxyHost", "proxyHost");
        parseProperty(builder, element, "proxyPort", "proxyPort");
        parseProperty(builder, element, "proxyUsername", "proxyUsername");
        parseProperty(builder, element, "proxyPassword", "proxyPassword");
        parseProperty(builder, element, "sessionId", "sessionId");
        parseProperty(builder, element, "serviceEndpoint", "serviceEndpoint");
        parseProperty(builder, element, "accessTokenId");
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        attachProcessorDefinition(parserContext, definition);
        return definition;
    }

}