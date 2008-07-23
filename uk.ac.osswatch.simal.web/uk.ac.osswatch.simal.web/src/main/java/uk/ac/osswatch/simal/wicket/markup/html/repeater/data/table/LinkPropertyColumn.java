package uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table;
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
