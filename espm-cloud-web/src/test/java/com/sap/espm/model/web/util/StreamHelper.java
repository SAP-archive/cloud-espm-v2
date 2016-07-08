package com.sap.espm.model.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	public static String readStreamContent(InputStream stream)
			throws IOException {
		StringBuilder str = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
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
	public static String readFromFile(String fileName) throws IOException {
		InputStream in = RequestExecutionHelper.class.getClassLoader()
				.getResourceAsStream(fileName);
		try {
			String result = StreamHelper.readStreamContent(in);
			return result;
		} finally {
			in.close();
		}
	}

	private StreamHelper() {
	}

}
