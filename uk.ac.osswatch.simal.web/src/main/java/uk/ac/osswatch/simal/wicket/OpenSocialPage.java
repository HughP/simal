package uk.ac.osswatch.simal.wicket;
/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */


import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.wicket.panel.GadgetPanel;

/**
 * A Base page used for pages that contain open social gadgets. This page is
 * extends the standard BasePage to provide Open Social container code.
 * 
 */
public class OpenSocialPage extends BasePage {
  private static final long serialVersionUID = -7829195954936598277L;
  private static final String FILES_DIR = "container/";
  private static final CompressedResourceReference GADGET_CSS = new CompressedResourceReference(
      GadgetPanel.class, "gadget.css");
  private List<GadgetPanel> gadgetPanels = new ArrayList<GadgetPanel>();

  private String baseurl;
  
  public OpenSocialPage() {
    super();
    baseurl = SimalRepository.getProperty(SimalRepository.PROPERTY_USER_WEBAPP_BASEURL);

    add(HeaderContributor.forCss(GADGET_CSS));
    add(HeaderContributor.forJavaScript(baseurl + "js/rpc.js?c=1"));
    add(HeaderContributor.forJavaScript(baseurl + FILES_DIR + "cookies.js"));
    add(HeaderContributor.forJavaScript(baseurl  + FILES_DIR + "util.js"));
    add(HeaderContributor.forJavaScript(baseurl + FILES_DIR + "gadgets.js"));
    // add(HeaderContributor.forJavaScript(SERVER_BASE + FILES_DIR +
    // "cookiebaseduserprefstore.js"));
  }

  /**
   * Prepare for render by adding the gadget initialisation code.
   */
  @Override
  public void onBeforeRender() {
    super.onBeforeRender();
    StringBuffer chromeIDs = new StringBuffer();
    StringBuffer config = new StringBuffer();

    config.append("<script type=\"text/javascript\">\n");

    config.append("function initGadgets() {\n");
    config.append("initOpenSocial();");
    for (int i = 0; i < gadgetPanels.size(); i++) {
      config
          .append("  var gadget = gadgets.container.createGadget({specUrl: \"");
      config.append(gadgetPanels.get(i).getSpecURL());
      config.append("\"});\n");
      config.append("  gadget.setServerBase(\"");
      config.append(baseurl);
      config.append("\");\n");
      config.append("  gadgets.container.addGadget(gadget);\n");
      // token = ownerId:viewerId:appId:domain:gadgetUrl:"0"];
      config.append("  gadget.secureToken = generateSecureToken(\"" + gadgetPanels.get(i).getSpecURL() + "\", \"" + gadgetPanels.get(i).getAppID() + "\");");
      chromeIDs.append("\"");
      chromeIDs.append(gadgetPanels.get(i).getUid());
      if (i + 1 < gadgetPanels.size()) {
        chromeIDs.append("\", ");
      } else {
        chromeIDs.append("\"");
      }
    }
    config.append("gadgets.container.layoutManager.setGadgetChromeIds([");
    config.append(chromeIDs);
    config.append("]);\n");
    config.append("};\n\n");

    config.append("function renderGadgets() {\n");
    config.append("  gadgets.container.renderGadgets();\n");
    config.append("};\n\n");
    config.append("</script>");

    add(new StringHeaderContributor(config.toString()));
  }

  /**
   * Add a gadget to this page. All gadgets must be added using this method in
   * order to ensure that they are correctly initialised. Adding them directly
   * to the page using the normal add(component) wicket methods will fail to see
   * the gadgets initialised correctly.
   * 
   * @param wicketID
   * @param gadgetURL
   * @param appID
   */
  protected void addGadget(String wicketID, String gadgetURL, String appID) {
    GadgetPanel gadgetPanel = new GadgetPanel(wicketID, gadgetURL, appID);
    gadgetPanels.add(gadgetPanel);
    add(gadgetPanel);
  }
}
