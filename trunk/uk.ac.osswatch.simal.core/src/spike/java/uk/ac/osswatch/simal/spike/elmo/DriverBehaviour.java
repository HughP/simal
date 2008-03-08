package uk.ac.osswatch.simal.spike.elmo;

import org.openrdf.elmo.annotations.rdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@rdf("http://example.org/0.1/elmoExample#Engine")
public class DriverBehaviour implements IDriverBehaviour {
  private static final Logger logger = LoggerFactory
      .getLogger(DriverBehaviour.class);

  private IEngine engine;

  public DriverBehaviour(IEngine engine) {
    this.engine = engine;
  }

  /**
   * Start the vehicle after perfoming the necessary safety checks.
   * 
   * @return true if started
   */
  public void startEngine() {
    logger.info("Starting the engine");
    if (engine != null) {
      engine.setIsStarted(true);
    } else {
      logger.error("There is no engine");
    }
  }
}
