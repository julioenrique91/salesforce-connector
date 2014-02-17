
package com.sforce.async.transformers;

import javax.annotation.Generated;
import com.sforce.async.OperationEnum;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-02-17T03:16:10-06:00", comments = "Build UNKNOWN_BUILDNUMBER")
public class OperationEnumEnumTransformer
    extends AbstractTransformer
    implements DiscoverableTransformer
{

    private int weighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;

    public OperationEnumEnumTransformer() {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnClass(OperationEnum.class);
        setName("OperationEnumEnumTransformer");
    }

    protected Object doTransform(Object src, String encoding)
        throws TransformerException
    {
        OperationEnum result = null;
        result = Enum.valueOf(OperationEnum.class, ((String) src));
        return result;
    }

    public int getPriorityWeighting() {
        return weighting;
    }

    public void setPriorityWeighting(int weighting) {
        this.weighting = weighting;
    }

}
