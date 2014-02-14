
package com.sforce.async.transformers;

import javax.annotation.Generated;
import com.sforce.async.ConcurrencyMode;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-02-14T12:48:49-06:00", comments = "Build UNKNOWN_BUILDNUMBER")
public class ConcurrencyModeEnumTransformer
    extends AbstractTransformer
    implements DiscoverableTransformer
{

    private int weighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;

    public ConcurrencyModeEnumTransformer() {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnClass(ConcurrencyMode.class);
        setName("ConcurrencyModeEnumTransformer");
    }

    protected Object doTransform(Object src, String encoding)
        throws TransformerException
    {
        ConcurrencyMode result = null;
        result = Enum.valueOf(ConcurrencyMode.class, ((String) src));
        return result;
    }

    public int getPriorityWeighting() {
        return weighting;
    }

    public void setPriorityWeighting(int weighting) {
        this.weighting = weighting;
    }

}
