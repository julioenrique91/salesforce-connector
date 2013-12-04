/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.bulk;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.common.bulk.BulkItem;
import org.mule.common.bulk.BulkOperationResult;
import org.mule.modules.salesforce.SalesforceUtils;
import org.mule.modules.salesforce.exception.SalesforceBulkException;

import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;

public class SaveResultToBulkOperationTransformerTest extends AbstractBulkOperationTransformerTest {

	@Override
	protected Class<?> getSourceClass() {
		return SaveResult.class;
	}
	
	@Override
	protected Class<? extends DiscoverableTransformer> getTransformerClass() {
		return SaveResultToBulkOperationTransformer.class;
	}
	
	@Test
	public void testCreate() throws Exception {
		final int objectCount = 10;
		final int failedIndex = 5;
		Error error = null;

		SObject[] objects = new SObject[objectCount];
		SaveResult[] results = new SaveResult[objectCount];

		for (int i = 0; i < objectCount; i++) {
			objects[i] = Mockito.mock(SObject.class);
			SaveResult result = Mockito.mock(SaveResult.class);

			if (i == failedIndex) {
				Mockito.when(result.isSuccess()).thenReturn(false);
				error = Mockito.mock(Error.class);
				Mockito.when(result.getErrors()).thenReturn(new Error[] { error });
			} else {
				Mockito.when(result.isSuccess()).thenReturn(true);
			}

			results[i] = result;
		}

		List<SaveResult> saveResults = SalesforceUtils.enrichWithPayload(objects, results);

		@SuppressWarnings("unchecked")
		BulkOperationResult<SObject> result = (BulkOperationResult<SObject>) this.getTransformer().transform(saveResults);
		
		for (int i = 0; i < objectCount; i++) {
			BulkItem<SObject> item = result.getItems().get(i);
			Assert.assertSame(objects[i], item.getPayload());

			if (i == failedIndex) {
				Assert.assertFalse(item.isSuccessful());
				Exception e = item.getException();
				Assert.assertTrue(e instanceof SalesforceBulkException);
				SalesforceBulkException sbe = (SalesforceBulkException) e;
				Assert.assertEquals(1, sbe.getErrors().size());
				Assert.assertSame(error, sbe.getErrors().get(0));
			} else {
				Assert.assertTrue(item.isSuccessful());
			}
		}
	}
	
}
