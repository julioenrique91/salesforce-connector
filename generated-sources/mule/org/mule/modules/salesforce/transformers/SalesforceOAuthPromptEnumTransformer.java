
package org.mule.modules.salesforce.transformers;

import javax.annotation.Generated;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.modules.salesforce.SalesforceOAuthPrompt;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-02-14T12:48:49-06:00", comments = "Build UNKNOWN_BUILDNUMBER")
public class SalesforceOAuthPromptEnumTransformer
    extends AbstractTransformer
    implements DiscoverableTransformer
{

    private int weighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;

    public SalesforceOAuthPromptEnumTransformer() {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnClass(SalesforceOAuthPrompt.class);
        setName("SalesforceOAuthPromptEnumTransformer");
    }

    protected Object doTransform(Object src, String encoding)
        throws TransformerException
    {
        SalesforceOAuthPrompt result = null;
        result = Enum.valueOf(SalesforceOAuthPrompt.class, ((String) src));
        return result;
    }

    public int getPriorityWeighting() {
        return weighting;
    }

    public void setPriorityWeighting(int weighting) {
        this.weighting = weighting;
    }

}
