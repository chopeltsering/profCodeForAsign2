package comp4104.imsg.core.net.http;

import java.io.IOException;
import java.io.OutputStream;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import comp4104.imsg.core.net.IRequest;
import comp4104.imsg.core.net.IRequestResponse;
import comp4104.imsg.core.net.IResponse;

/**
 * An Http output stream.
 */
public class HttpOutputStream extends OutputStream
{
	private OutputStream	outputStream;
	
	/**
	 * Creates a new <code>HttpOutputStream</code> instance.
	 * @param outputStream
	 * 		  The output stream to wrap.
	 */
	public HttpOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int b) throws IOException {
		throw new NotImplementedException();
	}
	
	/**
	 * Writes a request or response to the output stream.
	 * @param requestResponse
	 * 		  The request or response to write.
	 * @throws IOException
	 */
	public void write(IRequestResponse requestResponse) throws IOException {
		if (requestResponse instanceof IRequest) {
			write((IRequest)requestResponse);
		} else {
			write((IResponse)requestResponse);
		}
	}
	
	private void write(IRequest request) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("POST %s HTTP/1.1\r\n", request.getCommand()));
		write(builder, request);
	}
	
	private void write(StringBuilder builder, IRequestResponse requestResponse) throws IOException {
		int bodyLength = requestResponse.getBody() != null ? requestResponse.getBody().length : 0;
		requestResponse.addHeader("Content-Length", new Integer(bodyLength).toString());
		for (String key : requestResponse.headerKeys()) {
			builder.append(String.format("%s: %s\r\n", key, requestResponse.getHeader(key)));
		}
		
		builder.append("\r\n");
		byte[] bytes = builder.toString().getBytes();
		outputStream.write(bytes);
		if (bodyLength > 0) {
			outputStream.write(requestResponse.getBody());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	public void flush() throws IOException {
		outputStream.flush();
	}
	
	private void write(IResponse response) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("HTTP/1.1 %s\r\n", responseStatus(response.getStatus())));
		write(builder, response);
	}
	
	private String responseStatus(int status) {
		switch (status) {
			case IResponse.OK:
				return "200 OK";
			case IResponse.BAD_REQUEST:
				return "400 BAD REQUEST";
			case IResponse.NOT_AUTHORIZED:
				return "401 NOT AUTHORIZED";
			case IResponse.NOT_FOUND:
				return "404 NOT FOUND";
			case IResponse.SERVER_ERROR:
				return "500 SERVER ERROR";
		}
		
		return "";
	}
}