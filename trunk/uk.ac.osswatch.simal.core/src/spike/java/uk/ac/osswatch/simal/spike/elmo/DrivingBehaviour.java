package uk.ac.osswatch.simal.spike.elmo;

import org.openrdf.elmo.annotations.rdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@rdf("http://example.org/0.1/elmoExample#Vehicle")
public class DrivingBehaviour implements IDrivingBehaviour {
  private static final Logger logger = LoggerFactory
      .getLogger(DrivingBehaviour.class);

  ICar car;

  public DrivingBehaviour(ICar car) {
    this.car = car;
  }

  /**
   * Accelerate up to the maximum speed.
   */
  public void accelerate() {
    logger.info("Accelerating...");
    // if (isStarted) {
    car.setSpeed(car.getSpeed() + 30);
    // } else {
    // logger.info("Hmmmm... maybe you ought to start the engine first");
    // }
  }
}
