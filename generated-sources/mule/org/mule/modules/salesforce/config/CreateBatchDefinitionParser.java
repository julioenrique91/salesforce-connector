
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import com.sforce.async.holders.JobInfoExpressionHolder;
import org.mule.config.MuleManifest;
import org.mule.modules.salesforce.processors.CreateBatchMessageProcessor;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser.ParseDelegate;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser.ParseDelegate;
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

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-10T12:22:40-05:00", comments = "Build UNKNOWN_BUILDNUMBER")
public class CreateBatchDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{

    private static Logger logger = LoggerFactory.getLogger(CreateBatchDefinitionParser.class);

    private BeanDefinitionBuilder getBeanDefinitionBuilder(ParserContext parserContext) {
        try {
            return BeanDefinitionBuilder.rootBeanDefinition(CreateBatchMessageProcessor.class.getName());
        } catch (NoClassDefFoundError noClassDefFoundError) {
            String muleVersion = "";
            try {
                muleVersion = MuleManifest.getProductVersion();
            } catch (Exception _x) {
                logger.error("Problem while reading mule version");
            }
            logger.error(("Cannot launch the mule app, the @Processor [create-batch] within the connector [sfdc] is not supported in mule "+ muleVersion));
            throw new BeanDefinitionParsingException(new Problem(("Cannot launch the mule app, the @Processor [create-batch] within the connector [sfdc] is not supported in mule "+ muleVersion), new Location(parserContext.getReaderContext().getResource()), null, noClassDefFoundError));
        }
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = getBeanDefinitionBuilder(parserContext);
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
