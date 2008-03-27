package uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * A utility class for creating a ProperyColumn for DataTables that
 * is also a hyperlink.
 * 
 * Your HTML needs:
 * 
 * <![CDATA[
 * 
 * ]]>
 */
public abstract class LinkPropertyColumn extends PropertyColumn {
	PopupSettings popupSettings;
	IModel labelModel;

	public LinkPropertyColumn(IModel displayModel, String sortProperty,
			String propertyExpression, PopupSettings popupSettings) {
		this(displayModel, sortProperty, propertyExpression);
		this.popupSettings = popupSettings;
	}

	public LinkPropertyColumn(IModel displayModel, IModel labelModel) {
		super(displayModel, null);
		this.labelModel = labelModel;
	}

	public LinkPropertyColumn(IModel displayModel, String sortProperty,
			String propertyExpression) {
		super(displayModel, sortProperty, propertyExpression);
	}

	public LinkPropertyColumn(IModel displayModel, String propertyExpressions) {
		super(displayModel, propertyExpressions);
	}

	@Override
	public void populateItem(Item item, String componentId, IModel model) {
		item.add(new LinkPanel(item, componentId, model));
	}

	public abstract void onClick(Item item, String componentId, IModel model);

	@SuppressWarnings("serial")
	public class LinkPanel extends Panel {
		public LinkPanel(final Item item, final String componentId,
				final IModel model) {
			super(componentId);

			Link link = new Link("link") {

				@Override
				public void onClick() {
					LinkPropertyColumn.this.onClick(item, componentId, model);
				}
			};
			link.setPopupSettings(popupSettings);

			add(link);

			IModel tmpLabelModel = labelModel;

			if (labelModel == null) {
				tmpLabelModel = createLabelModel(model);
			}

			link.add(new Label("label", tmpLabelModel));
		}
	}
}
