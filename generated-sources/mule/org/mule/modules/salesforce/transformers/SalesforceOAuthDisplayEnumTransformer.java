
package org.mule.modules.salesforce.transformers;

import javax.annotation.Generated;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.modules.salesforce.SalesforceOAuthDisplay;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;

@Generated(value = "Mule DevKit Version 3.5.0-M4", date = "2014-03-28T02:36:29-05:00", comments = "Build M4.1875.17b58a3")
public class SalesforceOAuthDisplayEnumTransformer
    extends AbstractTransformer
    implements DiscoverableTransformer
{

    private int weighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;

    public SalesforceOAuthDisplayEnumTransformer() {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnClass(SalesforceOAuthDisplay.class);
        setName("SalesforceOAuthDisplayEnumTransformer");
    }

    protected Object doTransform(Object src, String encoding)
        throws TransformerException
    {
        SalesforceOAuthDisplay result = null;
        result = Enum.valueOf(SalesforceOAuthDisplay.class, ((String) src));
        return result;
    }

    public int getPriorityWeighting() {
        return weighting;
    }

    public void setPriorityWeighting(int weighting) {
        this.weighting = weighting;
    }

}
