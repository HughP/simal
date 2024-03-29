package uk.ac.osswatch.simal.schedule;
/*
 * Copyright 2007 University of Oxford
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
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.importData.PTSWImport;
import uk.ac.osswatch.simal.rdf.SimalException;

/**
 * A task that will import all new Data from PTSW.
 *
 */
public class ImportPTSWTask extends TimerTask {
  private static final Logger logger = LoggerFactory
      .getLogger(ImportPTSWTask.class);

	@Override
	public void run() {
		PTSWImport importer = new PTSWImport();
		try {
			importer.importLatestDOAP();
		} catch (SimalException e) {
			logger.error("Unable to import data from PTSW", e);
		} catch (IOException e) {
			logger.error("Unable to import data from PTSW", e);
		}
	}

}
