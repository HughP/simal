package uk.ac.osswatch.simal.wicket;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import uk.ac.osswatch.simal.wicket.panel.GadgetPanel;

/**
 * A Base page used for pages that contain open social gadgets. This page is
 * extends the standard BasePage to provide Open Social container code.
 * 
 */
public class OpenSocialPage extends BasePage {
  private static final long serialVersionUID = -7829195954936598277L;
  private static final String SERVER_BASE = "http://localhost:8080/";
  private static final String FILES_DIR = "container/";
  private static final CompressedResourceReference GADGET_CSS = new CompressedResourceReference(
      GadgetPanel.class, "gadget.css");
  private List<GadgetPanel> gadgetPanels = new ArrayList<GadgetPanel>();

  public OpenSocialPage() {
    super();

    add(HeaderContributor.forCss(GADGET_CSS));
    add(HeaderContributor.forJavaScript(SERVER_BASE + "js/rpc.js?c=1"));
    add(HeaderContributor.forJavaScript(SERVER_BASE + FILES_DIR + "cookies.js"));
    add(HeaderContributor.forJavaScript(SERVER_BASE + FILES_DIR + "gadgets.js"));
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
    for (int i = 0; i < gadgetPanels.size(); i++) {
      config
          .append("  var gadget = gadgets.container.createGadget({specUrl: \"");
      config.append(gadgetPanels.get(i).getSpecURL());
      config.append("\"});\n");
      config.append("  gadget.setServerBase(\"");
      config.append(SERVER_BASE);
      config.append("\");\n");
      config.append("  gadgets.container.addGadget(gadget);\n");
      config.append("  gadget.secureToken = generateSecureToken();\n");
      chromeIDs.append("\"");
      chromeIDs.append(gadgetPanels.get(i).getUid());
      if (i+1 <  gadgetPanels.size()) {
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
   */
  protected void addGadget(String wicketID, String gadgetURL) {
    GadgetPanel gadgetPanel = new GadgetPanel(wicketID, gadgetURL);
    gadgetPanels.add(gadgetPanel);
    add(gadgetPanel);
  }
}
