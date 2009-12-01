package uk.ac.osswatch.simal;

/*
 * Copyright 2009 University of Oxford 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * The HomepageLabelGenerator generates homepage labels based on the URL of the
 * homepage. It is intended to enrich information for projects without a label
 * for their home page. Although the home page in the DOAP file is technically a
 * URI, it will be treated as a URL, e.g. this generator will allow http:// or
 * https:// interchangeably. It uses a properties file for configuration of the
 * label depending on the URL.
 * 
 * @author svanderwaal
 */
public class HomepageLabelGenerator {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HomepageLabelGenerator.class);

	public static final String DEFAULT_HOMEPAGE_LABEL = "Webpage";

	private static Properties homepageLabels;

	private static void initHomepageLabels() throws SimalRepositoryException {
		homepageLabels = new Properties();

		String resourceFilename = SimalProperties
				.getProperty(SimalProperties.PROPERTY_SIMAL_HOMEPAGELABELS_PATH);
		URL defaultsLocation = HomepageLabelGenerator.class.getClassLoader()
				.getResource(resourceFilename);
		if (defaultsLocation != null) {
			try {
				homepageLabels.load(defaultsLocation.openStream());
			} catch (IOException e) {
				throw new SimalRepositoryException(
						"Could not load homepageLabels from resource "
								+ defaultsLocation, e);
			}
		}

		if (homepageLabels.isEmpty()) {
			LOGGER.warn("No homepagelabels loaded.");
		}
	}

	/**
	 * Generates a label for a home page based on the URL and configuration
	 * data.
	 * 
	 * @param url
	 * @return the label to be used to designate the home page.
	 */
	public static String getHomepageLabel(String url) {
		if (homepageLabels == null) {
			try {
				initHomepageLabels();
			} catch (SimalRepositoryException e) {
				LOGGER.warn(e.getMessage());
			}
		}

		String label = HomepageLabelGenerator.DEFAULT_HOMEPAGE_LABEL;
		if (url != null) {
			for (Object key : homepageLabels.keySet()) {
				String homepageLabelKey = (String) key;
				if (isUrlMatch(url, homepageLabelKey)) {
					label = homepageLabels.getProperty(homepageLabelKey);
					break;
				}
			}
		}

		return label;
	}

	/**
	 * 
	 * @param url
	 * @param homepageLabelKey
	 * @return boolean to
	 */
	private static boolean isUrlMatch(String url, String homepageLabelKey) {
		Pattern pattern = Pattern.compile("^(http://|https://)?" + ".*"
				+ homepageLabelKey + ".*");

		Matcher matcher = pattern.matcher(url);

		return matcher.find();
	}
}
