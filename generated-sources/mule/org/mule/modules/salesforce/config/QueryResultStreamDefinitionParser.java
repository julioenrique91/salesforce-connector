
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import com.sforce.async.holders.BatchInfoExpressionHolder;
import org.mule.config.MuleManifest;
import org.mule.modules.salesforce.processors.QueryResultStreamMessageProcessor;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-02-19T08:16:01-06:00", comments = "Build UNKNOWN_BUILDNUMBER")
public class QueryResultStreamDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{

    private static Logger logger = LoggerFactory.getLogger(QueryResultStreamDefinitionParser.class);

    private BeanDefinitionBuilder getBeanDefinitionBuilder(ParserContext parserContext) {
        try {
            return BeanDefinitionBuilder.rootBeanDefinition(QueryResultStreamMessageProcessor.class.getName());
        } catch (NoClassDefFoundError noClassDefFoundError) {
            String muleVersion = "";
            try {
                muleVersion = MuleManifest.getProductVersion();
            } catch (Exception _x) {
                logger.error("Problem while reading mule version");
            }
            logger.error(("Cannot launch the mule app, the @Processor [query-result-stream] within the connector [sfdc] is not supported in mule "+ muleVersion));
            throw new BeanDefinitionParsingException(new Problem(("Cannot launch the mule app, the @Processor [query-result-stream] within the connector [sfdc] is not supported in mule "+ muleVersion), new Location(parserContext.getReaderContext().getResource()), null, noClassDefFoundError));
        }
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = getBeanDefinitionBuilder(parserContext);
        builder.addConstructorArgValue("queryResultStream");
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        parseConfigRef(element, builder);
        if (!parseObjectRefWithDefault(element, builder, "batch-info", "batchInfo", "#[payload:]")) {
            BeanDefinitionBuilder batchInfoBuilder = BeanDefinitionBuilder.rootBeanDefinition(BatchInfoExpressionHolder.class.getName());
            Element batchInfoChildElement = DomUtils.getChildElementByTagName(element, "batch-info");
            if (batchInfoChildElement!= null) {
                parseProperty(batchInfoBuilder, batchInfoChildElement, "id", "id");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "jobId", "jobId");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "state", "state");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "stateMessage", "stateMessage");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "createdDate", "createdDate");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "systemModstamp", "systemModstamp");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "numberRecordsProcessed", "numberRecordsProcessed");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "numberRecordsFailed", "numberRecordsFailed");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "totalProcessingTime", "totalProcessingTime");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "apiActiveProcessingTime", "apiActiveProcessingTime");
                parseProperty(batchInfoBuilder, batchInfoChildElement, "apexProcessingTime", "apexProcessingTime");
                builder.addPropertyValue("batchInfo", batchInfoBuilder.getBeanDefinition());
            }
        }
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
