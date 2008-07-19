/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
