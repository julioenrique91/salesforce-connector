
package org.mule.modules.salesforce.transformers;

import java.util.Calendar;
import javax.annotation.Generated;
import javax.xml.bind.DatatypeConverter;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;

@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-15T04:12:08-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class StringToCalendarTransformer
    extends AbstractTransformer
    implements DiscoverableTransformer
{

    private int priorityWeighting = 1;

    public StringToCalendarTransformer() {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnClass(Calendar.class);
        setName("StringToCalendarTransformer");
    }

    protected Object doTransform(Object src, String encoding)
        throws TransformerException
    {
        try {
            return DatatypeConverter.parseDateTime(src.toString());
        } catch (IllegalArgumentException e) {
            throw new TransformerException(MessageFactory.createStaticMessage(String.format("Could not parse %s as a valid xsd:dateTime", src)), this, e);
        }
    }

    /**
     * Retrieves priorityWeighting
     * 
     */
    public int getPriorityWeighting() {
        return this.priorityWeighting;
    }

    /**
     * Sets priorityWeighting
     * 
     * @param value Value to set
     */
    public void setPriorityWeighting(int value) {
        this.priorityWeighting = value;
    }

}
