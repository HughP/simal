package uk.ac.osswatch.simal.wicket.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortableCategoryDataProvider;
import uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table.LinkPropertyColumn;

/**
 * A panel for listing categories. This panel allows the user to navigate the people
 * the Simal repository and, optionally, allows some manipulation of those
 * records.
 */
public class CategoryListPanel extends Panel {
  private static final long serialVersionUID = -7641153470731218965L;

  public CategoryListPanel(String id) throws SimalRepositoryException {
    super(id);

    List<AbstractColumn> columns = new ArrayList<AbstractColumn>();
    /*
     * columns.add(new AbstractColumn(new Model("Actions")) { public void
     * populateItem(Item cellItem, String componentId, IModel model) {
     * cellItem.add(new ActionPanel(componentId, model)); } });
     */
    columns.add(new LinkPropertyColumn(new Model("Name"), "name", "name") {

      @Override
      public void onClick(Item item, String componentId, IModel model) {
        IDoapCategory person = (IDoapCategory) model.getObject();
        // getRequestCycle().setResponsePage(new PersonDetailPage(project));
      }

    });

    SortableDataProvider dataProvider = new SortableCategoryDataProvider();
    dataProvider.setSort(SortableCategoryDataProvider.SORT_PROPERTY_NAME, true);
    add(new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider, 15));
  }
}
