package com.liferay.example.rest.utils;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Helper {
	
	private static Log _log = LogFactoryUtil.getLog(Helper.class.getName());
	
	/*
	 * Load a resource file when the module is running in a bundled environment
	 */
	public Properties loadResourceBundled(String path) {
		final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
		
		Properties properties = new Properties();

		try {
			properties.load( stream );
		} catch (IOException e1) {
			_log.error("An error ocurred when getting the properties file");
		}
		
		return properties;
	}
}
