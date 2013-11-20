package org.mule.modules.salesforce.bulk;

import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.Transformer;
import org.mule.api.transformer.TransformerException;
import org.mule.common.bulk.BulkOperationResult;
import org.mule.modules.salesforce.SalesforceConnector;
import org.mule.tck.junit4.AbstractMuleContextTestCase;
import org.mule.transformer.types.DataTypeFactory;

public abstract class AbstractBulkOperationTransformerTest extends AbstractMuleContextTestCase {
	
	private SalesforceConnector connector;
	
	@Override
	protected void doSetUp() throws Exception {
		super.doSetUp();
		this.connector = new SalesforceConnector();
		this.connector.setMuleContext(muleContext);
		this.connector.init();
	}
	
	protected abstract Class<?> getSourceClass();
	
	protected abstract Class<? extends DiscoverableTransformer> getTransformerClass();
	
	@Test
	public void testRegistered() throws Exception {
		Transformer t = getTransformer();
		Assert.assertNotNull(t);
		Assert.assertEquals(this.getTransformerClass(), t.getClass());
	}
	
	@SuppressWarnings("rawtypes")
	protected DataType<BulkOperationResult> getTarget() {
		return DataTypeFactory.create(BulkOperationResult.class);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	protected DataType<Collection> getSource() {
		return DataTypeFactory.create(List.class, this.getSourceClass());
	}
	
	protected Transformer getTransformer() throws TransformerException {
		return muleContext.getRegistry().lookupTransformer(getSource(), getTarget());
	}


}
