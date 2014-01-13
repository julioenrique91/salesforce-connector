
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import com.sforce.async.holders.JobInfoExpressionHolder;
import org.mule.modules.salesforce.processors.CreateBatchMessageProcessor;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser.ParseDelegate;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser.ParseDelegate;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T03:42:10-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class CreateBatchDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{


    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(CreateBatchMessageProcessor.class.getName());
        builder.addConstructorArgValue("createBatch");
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        parseConfigRef(element, builder);
        if (!parseObjectRef(element, builder, "job-info", "jobInfo")) {
            BeanDefinitionBuilder jobInfoBuilder = BeanDefinitionBuilder.rootBeanDefinition(JobInfoExpressionHolder.class.getName());
            Element jobInfoChildElement = DomUtils.getChildElementByTagName(element, "job-info");
            if (jobInfoChildElement!= null) {
                parseProperty(jobInfoBuilder, jobInfoChildElement, "id", "id");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "operation", "operation");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "object", "object");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "createdById", "createdById");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "createdDate", "createdDate");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "systemModstamp", "systemModstamp");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "state", "state");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "externalIdFieldName", "externalIdFieldName");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "concurrencyMode", "concurrencyMode");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "fastPathEnabled", "fastPathEnabled");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "numberBatchesQueued", "numberBatchesQueued");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "numberBatchesInProgress", "numberBatchesInProgress");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "numberBatchesCompleted", "numberBatchesCompleted");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "numberBatchesFailed", "numberBatchesFailed");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "numberBatchesTotal", "numberBatchesTotal");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "numberRecordsProcessed", "numberRecordsProcessed");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "numberRetries", "numberRetries");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "contentType", "contentType");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "apiVersion", "apiVersion");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "assignmentRuleId", "assignmentRuleId");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "numberRecordsFailed", "numberRecordsFailed");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "totalProcessingTime", "totalProcessingTime");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "apiActiveProcessingTime", "apiActiveProcessingTime");
                parseProperty(jobInfoBuilder, jobInfoChildElement, "apexProcessingTime", "apexProcessingTime");
                builder.addPropertyValue("jobInfo", jobInfoBuilder.getBeanDefinition());
            }
        }
        parseListWithDefaultAndSetProperty(element, builder, "objects", "objects", "object", "#[payload]", new ParseDelegate<Object>() {


            public Object parse(Element element) {
                if (element.hasAttribute("ref")) {
                    if (!isMuleExpression(element.getAttribute("ref"))) {
                        return new RuntimeBeanReference(element.getAttribute("ref"));
                    } else {
                        return element.getAttribute("ref");
                    }
                }
                return parseMap(element, "inner-object", new ParseDelegate<String>() {


                    public String parse(Element element) {
                        return element.getTextContent();
                    }

                }
                );
            }

        }
        );
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
