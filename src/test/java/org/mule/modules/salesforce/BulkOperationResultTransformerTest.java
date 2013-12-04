/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.Transformer;
import org.mule.common.bulk.BulkOperationResult;
import org.mule.modules.salesforce.bulk.SaveResultToBulkOperationTransformer;
import org.mule.tck.junit4.AbstractMuleContextTestCase;
import org.mule.transformer.types.DataTypeFactory;

import com.sforce.soap.partner.SaveResult;

public class BulkOperationResultTransformerTest extends
		AbstractMuleContextTestCase {
	
	
	
	@Test
	public void testTransform() throws Exception {
		muleContext.getRegistry().registerTransformer(new SaveResultToBulkOperationTransformer());
		
		List<SaveResult> results = new ArrayList<SaveResult>();
//		DataType<List<SaveResult>> source = DataTypeFactory.create(results.getClass(), SaveResult.class);
		DataType source = DataTypeFactory.create(results.getClass());
		DataType<BulkOperationResult> target = DataTypeFactory.create(BulkOperationResult.class);
				
		Transformer t = muleContext.getRegistry().lookupTransformer(source, target);
		Assert.assertNotNull(t);
		Assert.assertEquals(SaveResultToBulkOperationTransformer.class, t.getClass());
	}

}
