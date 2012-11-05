package comp4104.imsg.core.net.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IRequestResponse;
import comp4104.imsg.core.net.IResponse;
import comp4104.imsg.core.net.Request;
import comp4104.imsg.core.net.Response;

/**
 * An http input stream.
 */
public class HttpInputStream extends InputStream
{
	private static final String BAD_FORMAT 		= "Invalid HTTP Request/Response format";
	private static final String RX_RQ_LINE		= "^(GET|POST)\\s(\\S+)\\sHTTP/(1.1|1.0|0.9)$";
	private static final String RX_RS_LINE		= "^HTTP/(1.1|1.0|0.9)\\s(\\d+)\\s(.+)$";
	private static final String RX_HD_LINE		= "^(\\S+):\\s(.+)$";
	
	private static final int 	BUFFER_SIZE 	= 1024 << 1;
	
	private byte[]			buffer;
	private int				bufferSize;
	
	private InputStream		inputStream;
	
	/**
	 * Creates a new <code>HttpInputStream</code> instance.
	 * @param inputStream
	 * 		  The input stream to wrap.
	 */
	public HttpInputStream(InputStream inputStream) {
		this.buffer = new byte[BUFFER_SIZE];
		this.inputStream = inputStream;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	public int read() throws IOException {
		throw new NotImplementedException();
	}
	
	/**
	 * Reads an http request or response.
	 * @return The http request or response.
	 * @throws IOException
	 */
	public IRequestResponse readRequestResponse() throws IOException {
		readHttpMethod();
		String line = readLine();
		if (line.matches(RX_RQ_LINE)) {
			return readRequest(line);
		} else if (line.matches(RX_RS_LINE)) {
			return readResponse(line);
		} else {
			throw new IOException(BAD_FORMAT);
		}
	}
	
	private void readHttpMethod() throws IOException {
		while (bufferSize < 4) {
			int bytesRead = inputStream.read(buffer, bufferSize, 4 - bufferSize);
			bufferSize += bytesRead;
		}
		
		String method = new String(buffer, 0, 4);
		if (!method.matches("^(GET|POST|HTTP).*")) {
			throw new IOException(BAD_FORMAT);
		}
	}
	
	private IRequest readRequest(String requestLine) throws IOException {
		Pattern pattern = Pattern.compile(RX_RQ_LINE);
		Matcher matcher = pattern.matcher(requestLine);
		if (matcher.matches()) {
			IRequest request = new Request();
			request.setCommand(matcher.group(2));
			readHeaders(request);
			readBody(request);
			return request;
		}
		
		throw new IOException(BAD_FORMAT);
	}
	
	private IResponse readResponse(String responseLine) throws IOException {
		Pattern pattern = Pattern.compile(RX_RS_LINE);
		Matcher matcher = pattern.matcher(responseLine);
		if (matcher.matches()) {
			IResponse response = new Response();
			response.setStatus(Integer.parseInt(matcher.group(2)));
			readHeaders(response);
			readBody(response);
			return response;
		}
		
		throw new IOException(BAD_FORMAT);
	}
	
	private void readHeaders(IRequestResponse requestResponse) throws IOException {
		Pattern pattern = Pattern.compile(RX_HD_LINE);
		do {
			String line = readLine();
			if (line.length() == 0) {
				break;
			}
			
			Matcher matcher = pattern.matcher(line);
			if (!matcher.matches()) {
				throw new IOException(BAD_FORMAT);
			}
			
			requestResponse.addHeader(matcher.group(1), matcher.group(2));
		} while (true);
	}
	
	private void readBody(IRequestResponse requestResponse) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int bytesLeft = getContentLength(requestResponse);
		while (bytesLeft > 0) {
			if (bufferSize == 0) {
				int bytesRead = inputStream.read(buffer, 0, buffer.length);
				bufferSize += bytesRead;
			}
			
			if (bytesLeft <= bufferSize) {
				bos.write(buffer, 0, bytesLeft);
				updateBuffer(bytesLeft);
				bytesLeft = 0;
			} else {
				bytesLeft -= bufferSize;
				bos.write(buffer, 0, bufferSize);
				updateBuffer(bufferSize);
			}
		}
		
		requestResponse.setBody(bos.toByteArray());
	}
	
	private void updateBuffer(int numBytes)
	{
		for (int i = numBytes; i < bufferSize; ++i) {
			buffer[i - numBytes] = buffer[i];
		}
		bufferSize -= numBytes;
	}
	
	private int getContentLength(IRequestResponse requestResponse) throws IOException {
		String length = requestResponse.getHeader("Content-Length");
		if (length != null) {
			try { return Integer.parseInt(length); }
			catch (NumberFormatException nfe) { throw new IOException(BAD_FORMAT, nfe); }
		}
		
		throw new IOException(BAD_FORMAT + " (Content-Length) missing");
	}
	
	private String readLine() throws IOException {
		String line = null;
		ByteArrayOutputStream lineBytes = new ByteArrayOutputStream();
		while (line == null) {
			if (bufferSize == 0) {
				int bytesRead = inputStream.read(buffer, 0, buffer.length);
				bufferSize += bytesRead;
			}
			
			int i = indexOfCRLF(buffer, bufferSize);
			if (i < 0) {
				lineBytes.write(buffer, 0, bufferSize);
				updateBuffer(bufferSize);
			}
			else {
				lineBytes.write(buffer, 0, i);
				updateBuffer(i+2);
				line = lineBytes.size() > 0 ? new String(lineBytes.toByteArray()) : "";
			}
		}
		return line;
	}
	
	private int indexOfCRLF(byte[] bytes, int length) throws IOException {
		for (int i = 0; i < (length - 1); ++i) {
			if (bytes[i] == '\r' && bytes[i+1] == '\n') {
				return i;
			} else if (bytes[i] == '\r') {
				throw new IOException(BAD_FORMAT);
			}
		}
		
		return -1;
	}
}