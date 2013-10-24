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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mule.modules.salesforce.lazystream.LazyInputStream;
import org.mule.modules.salesforce.lazystream.LazyInputStreamStates;

import com.sforce.async.BulkConnection;

public class SalesforceLazyStreamTest {

	@Test
	public void testLazyStream() throws IOException {
		LazyInputStreamTest is1 = new LazyInputStreamTest(null, new ByteArrayInputStream("1".getBytes()));
		LazyInputStreamTest is2 = new LazyInputStreamTest(null, new ByteArrayInputStream("2".getBytes()));
		LazyInputStreamTest is3 = new LazyInputStreamTest(null, new ByteArrayInputStream("3".getBytes()));
		
		List<InputStream> isList = new LinkedList<InputStream>();
		isList.add(is1);
		isList.add(is2);
		isList.add(is3);
		
		// The LazyInputStream will only open the Stream when the first operation is used, no before
		SequenceInputStream seq = new SequenceInputStream(Collections.enumeration(isList));
		int b = 0;
		for (int x = 0; x < 3 ; x++) {
			b = seq.read();
			Assert.assertTrue(b != -1);
			
			switch (x) {
				case 0:
					Assert.assertEquals(LazyInputStreamStates.OPENED, is1.getInputStreamState());
					Assert.assertEquals(LazyInputStreamStates.PENDING, is2.getInputStreamState());
					Assert.assertEquals(LazyInputStreamStates.PENDING, is3.getInputStreamState());
					break;
				case 1:
					Assert.assertEquals(LazyInputStreamStates.CLOSED, is1.getInputStreamState());
					Assert.assertEquals(LazyInputStreamStates.OPENED, is2.getInputStreamState());
					Assert.assertEquals(LazyInputStreamStates.PENDING, is3.getInputStreamState());
					break;
				case 2:
					Assert.assertEquals(LazyInputStreamStates.CLOSED, is1.getInputStreamState());
					Assert.assertEquals(LazyInputStreamStates.CLOSED, is2.getInputStreamState());
					Assert.assertEquals(LazyInputStreamStates.OPENED, is3.getInputStreamState());
					break;
			}
		}
		b = seq.read();
		Assert.assertEquals(LazyInputStreamStates.CLOSED, is1.getInputStreamState());
		Assert.assertEquals(LazyInputStreamStates.CLOSED, is2.getInputStreamState());
		Assert.assertEquals(LazyInputStreamStates.CLOSED, is3.getInputStreamState());
		
		Assert.assertTrue(b == -1);
		
		seq.close();
	}
	
	private class LazyInputStreamTest extends LazyInputStream {

		private InputStream is;

		public LazyInputStreamTest(BulkConnection connection, InputStream is) {
			super(connection);
			this.is = is;
		}

		@Override
		protected InputStream openLazyInputStream() throws IOException {
			return is;
		}
		
	}
}
