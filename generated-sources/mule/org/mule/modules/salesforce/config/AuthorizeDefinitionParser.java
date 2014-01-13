
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import org.mule.modules.salesforce.processors.AuthorizeMessageProcessor;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T03:42:10-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class AuthorizeDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{


    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AuthorizeMessageProcessor.class.getName());
        parseConfigRef(element, builder);
        parseProperty(builder, element, "state");
        parseProperty(builder, element, "display");
        parseProperty(builder, element, "immediate");
        parseProperty(builder, element, "prompt");
        parseProperty(builder, element, "authorizationUrl");
        parseProperty(builder, element, "accessTokenUrl");
        parseProperty(builder, element, "accessTokenId");
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        attachProcessorDefinition(parserContent, definition);
        return definition;
    }

}
