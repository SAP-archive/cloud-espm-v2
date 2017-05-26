package com.sap.espm.model.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.pdf.generator.CmisRead;

/**
 * 
 * Stream Helper
 * 
 */
public class StreamHelper {

	/**
	 * Read Stream content
	 * 
	 * @param stream
	 * @return stream as String
	 * @throws IOException
	 */
	/**
	 * {@link Logger} implementation.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StreamHelper.class);

	public static String readStreamContent(InputStream stream) throws IOException {
		StringBuilder str = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		while ((line = reader.readLine()) != null) {
			str.append(line + "\n");
		}
		String response = str.toString();
		return response;
	}

	/**
	 * Read File content
	 * 
	 * @param fileName
	 * @return file content as String
	 * @throws IOException
	 */
	public static String readFromFile(String fileName) {
		InputStream in = null;
		String result = null;
		ClassLoader cLoader = RequestExecutionHelper.class.getClassLoader();
		if (cLoader != null) {
			try {
				in = cLoader.getResourceAsStream(fileName);
				result = StreamHelper.readStreamContent(in);

			} catch (IOException ex) {
				LOGGER.error(ex.getMessage());
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());

				}
			}

		}
		return result;

	}

	private StreamHelper() {
	}

}
