package uk.ac.osswatch.simal.schedule;

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
