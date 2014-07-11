/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.automation;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.mule.modules.tests.ConnectorTestCase;

import com.sforce.async.BatchResult;
import com.sforce.async.Result;



public class SalesforceTestParent extends ConnectorTestCase {
	
	public static final long AFTER_UPDATE_DELAY = 120000;
	public static final long BATCH_PROCESSING_DELAY = 10000;
	public static final long BEFORE_SEARCH_DELAY = 90000;
	public static final long AFTER_DELETE_DELAY = 90000;
	
	
	protected void assertBatchSucessAndGetSObjectIds(BatchResult batchResult) {
		
		List<String> createdSObjectsIds = new ArrayList<String>();
		
		boolean isSuccess = true;
		Result[] results = batchResult.getResult();
		
		for (int index=0; index<results.length; index++) {
			
			if (results[index].isSuccess()) {
				createdSObjectsIds.add(results[index].getId());
			} else {
				isSuccess = false;	
			}
	
		}
		
		upsertOnTestRunMessage("idsToDeleteFromMessage", createdSObjectsIds);
		
		assertTrue(isSuccess);

	}
	
	protected void assertBatchSucessAndCompareSObjectIds(BatchResult batchResult, List<String> createdSObjectsIds) {
		
		boolean isSuccess = true;
		Result[] results = batchResult.getResult();
		
		for (int index=0; index<results.length; index++) {
			
			if (!(results[index].isSuccess() && createdSObjectsIds.contains(results[index].getId()))) {
				isSuccess = false;
			}
	
		}
		
		upsertOnTestRunMessage("idsToDeleteFromMessage", createdSObjectsIds);
		
		assertTrue(isSuccess);

	}

}