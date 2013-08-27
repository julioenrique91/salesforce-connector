package com.sforce.lazystreams;

import java.io.IOException;
import java.io.InputStream;

import com.sforce.async.BulkConnection;

/**
 * This class encapsulates an InputStream and the 
 * 
 */
abstract public class LazyInputStream extends InputStream {
	
	private InputStream inputStream;
	private LazyInputStreamStates inputStreamState;
	private BulkConnection connection;	
	private Object inputStreamStateLock;
	
	
	public LazyInputStream(BulkConnection connection) {
		super();
		this.connection = connection;
		inputStreamState = LazyInputStreamStates.PENDING;
		inputStreamStateLock = new Object();
	}

	protected BulkConnection getBulkConnection() {
		return connection;
	}
		
	public LazyInputStreamStates getInputStreamState() {
		return inputStreamState;
	}

	@Override
	public int read() throws IOException {
		openLazyStream();
		return inputStream.read();
	}


	@Override
	public int read(byte[] b) throws IOException {
		openLazyStream();
		return inputStream.read(b);
	}


	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		openLazyStream();
		return inputStream.read(b, off, len);
	}


	@Override
	public long skip(long n) throws IOException {
		openLazyStream();
		return inputStream.skip(n);
	}


	@Override
	public int available() throws IOException {
		openLazyStream();
		return inputStream.available();
	}


	@Override
	public void close() throws IOException {
		// Sync the access to the state of the inputStream to prevent from openLazyStream() if close was called first
		synchronized (inputStreamStateLock) {
			inputStreamState = LazyInputStreamStates.CLOSED;
			connection = null; // If the stream is closed, no more call are allowed. Any other call with end in an IOException
		}
		// The inputStram might hadn't been opened
		if (inputStream != null) {
			inputStream.close();
		}
	}


	@Override
	public synchronized void mark(int readlimit) {
		// Because this method does not throw any exception, if there is an error generating the stream, we should throw an exception
		// and the only way to leave this method as Override is not adding any throws clausule. That why we use RuntimeExecption
		try {
			openLazyStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		inputStream.mark(readlimit);
	}


	@Override
	public synchronized void reset() throws IOException {
		openLazyStream();
		inputStream.reset();
	}


	@Override
	public boolean markSupported() {
		// Because this method does not throw any exception, if there is an error generating the stream, we should throw an exception
		// and the only way to leave this method as Override is not adding any throws clausule. That why we use RuntimeExecption
		try {
			openLazyStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return inputStream.markSupported();
	}

	/**
	 * Check the state ({@link LazyInputStreamStates}) of the InputStream and if it is pending, call the connector operation to open it 
	 * @throws IOException
	 */
	private void openLazyStream() throws IOException {
		// Sync the access to the state of the inputStream to prevent asynchronous calls opening again the same stream
		synchronized (inputStreamStateLock) {
			// If some operation has been called that needs to use the inputStream and is still pending, load the stream
			if (inputStreamState.equals(LazyInputStreamStates.PENDING)) {
				inputStreamState = LazyInputStreamStates.OPENED;
				inputStream = openLazyInputStream();
			}
		}
	}
	
	/**
	 * This operation is the one that must be override in order to being able to open the InputStream.
	 * This operation will be called on demand, when some of the operations of the InputStream are maded
	 * 
	 * @return Returns the lazy {@link InputStream}.
	 */
	abstract protected InputStream openLazyInputStream() throws IOException;
}
