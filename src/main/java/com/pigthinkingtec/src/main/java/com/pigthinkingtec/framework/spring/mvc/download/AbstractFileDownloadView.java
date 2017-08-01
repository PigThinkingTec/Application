package com.pigthinkingtec.framework.spring.mvc.download;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.AbstractView;

/**
 * DownLoad機能のベースクラス
 * 
 * @author yizhou
 */
public abstract class AbstractFileDownloadView
	extends AbstractView implements InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(AbstractFileDownloadView.class);

	private int chunkSize = 256;

	/**
	 * Renders the response.
	 * @param model Model object
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @throws IOException Input/output exception
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.debug("FileDownload start.");

		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			// Fetching download file data
			try {
				inputStream = getInputStream(model, request);
			} catch (IOException e) {
				// In case download fails
				logger.error(
						"FileDownload Failed with getInputStream(). cause message is {}.",
						e.getMessage());
				throw e;
			}
			if (inputStream == null) {
				throw new IOException("FileDownload Failed. InputStream is null.");
			}

			// Writing to output stream of HTTP response
			try {
				outputStream = new BufferedOutputStream(response
						.getOutputStream());
			} catch (IOException e) {
				// In case download fails
				logger.error(
						"FileDownload Failed with getOutputStream(). cause message is {}.",
						e.getMessage());
				throw e;
			}

			// Editing header part
			addResponseHeader(model, request, response);

			try {
				writeResponseStream(inputStream, outputStream);
			} catch (IOException e) {
				// In case download fails
				logger.error(
						"FileDownload Failed with writeResponseStream(). cause message is {}.",
						e.getMessage());
				throw e;
			}
			try {
				outputStream.flush();
			} catch (IOException e) {
				// In case download fails
				logger.error(
						"FileDownload Failed with flush. cause message is {}.",
						e.getMessage());
				throw e;
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ioe) {
					logger.warn("Cannot close InputStream.", ioe);
				}
			}
		}
	}

	/**
	 * Fetches the stream which writes to the response body
	 * @param model Model object
	 * @param request current HTTP Request
	 * @return stream that will write to the request
	 * @throws IOException Input/output exception
	 */
	protected abstract InputStream getInputStream(Map<String, Object> model,
			HttpServletRequest request) throws IOException;

	/**
	 * Writes the download file to the stream of HTTP response.
	 * @param inputStream InputStream of file data to be downloaded
	 * @param outputStream OutputStream of the response
	 * @throws IOException Input/output exception (Exception handling should be done at the caller side)
	 */
	protected void writeResponseStream(InputStream inputStream,
			OutputStream outputStream) throws IOException {
		if (inputStream == null || outputStream == null) {
			return;
		}

		byte[] buffer = new byte[chunkSize];
		int length = 0;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
	}

	/**
	 * Adds response header
	 * @param model Model object
	 * @param request current HTTP request
	 * @param response current HTTP response
	 */
	protected abstract void addResponseHeader(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response);

	/**
	 * Set a chunk size.
	 * @param chunkSize chunk size to buffer
	 */
	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	/**
	 * Initializes the exception filter.
	 * <p>
	 * validate the chunkSize field.
	 * </p>
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (chunkSize <= 0) {
			throw new IllegalArgumentException("chunkSize must be over 1. specified chunkSize is \""
					+ chunkSize + "\".");
		}
	}

}
