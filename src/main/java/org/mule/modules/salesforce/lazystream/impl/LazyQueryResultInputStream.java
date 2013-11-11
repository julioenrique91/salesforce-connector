package org.mule.modules.salesforce.lazystream.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.modules.salesforce.lazystream.LazyInputStream;

import com.sforce.async.AsyncApiException;
import com.sforce.async.BulkConnection;

public class LazyQueryResultInputStream extends LazyInputStream {

	static final private Log logger = LogFactory.getLog(LazyInputStream.class);
	
	private String jobId;
	private String batchId;
	private String jobResultId;
	
	
	public LazyQueryResultInputStream(BulkConnection connection, String jobId, String batchId, String jobResultId) {
		super(connection);
		this.jobId = jobId;
		this.batchId = batchId;
		this.jobResultId = jobResultId;
	}

	@Override
	protected InputStream openLazyInputStream() throws IOException {
		try {
			logger.debug(String.format("OPENING LAZY STREAM -- JobId[%s] BatchId[%s] JobResultId[%s]", jobId, batchId, jobResultId));
			InputStream queryResultStream = getBulkConnection().getQueryResultStream(jobId, batchId, jobResultId);
			logger.debug(String.format("OPENED LAZY STREAM -- JobId[%s] BatchId[%s] JobResultId[%s]", jobId, batchId, jobResultId));
			return queryResultStream;
		} catch (AsyncApiException e) {
			throw new IOException(e);
		}
	}

}
