package org.phoenixctms.ctsms.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class FileServletBase extends HttpServlet {

	interface FileStream {

		public String getFileName();

		public Long getFileSize();

		public String getMimeType();

		public InputStream getStream();
	}

	private static final int DEFAULT_BUFFER_SIZE = 96 * 1024; // 100KB.

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
			}
		}
	}

	protected abstract FileStream createFileStream(HttpServletRequest request, HttpServletResponse response) throws IOException;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		FileStream stream = createFileStream(request, response);
		if (stream != null) {
			response.reset();
			response.setBufferSize(DEFAULT_BUFFER_SIZE);
			response.setContentType(stream.getMimeType());
			response.setHeader("Content-Length", stream.getFileSize().toString());
			response.setHeader("Content-Disposition", "inline; filename=\"" + stream.getFileName() + "\"");
			// Prepare streams.
			BufferedInputStream input = null;
			BufferedOutputStream output = null;
			try {
				// Open streams.
				input = new BufferedInputStream(stream.getStream(), DEFAULT_BUFFER_SIZE);
				output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
				// Write file contents to response.
				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int length;
				while ((length = input.read(buffer)) > 0) {
					output.write(buffer, 0, length);
				}
			} finally {
				// Gently close streams.
				close(output);
				close(input);
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		}
	}
}
