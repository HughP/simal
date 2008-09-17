package uk.ac.osswatch.simal.wicket.markup.html.list;
/*
 * 
 * Copyright 2007 University of Oxford
 * 
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


import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import uk.ac.osswatch.simal.model.IDoapHomepage;

/**
 * ListView for web pages. Each address will be rendered as a clickable
 * link. The parent component must provide HTML of the form:
 * 
 * <[![CDATA
 * <span wicket:id="webPageList">
 *   <a href="#" wicket:id="linkURL"><span wicket:id="linkText">Link Text</span>
 *   </a>
 * </span>
 * ]]>
 * 
 */
public class WebPageListView extends ListView<IDoapHomepage> {
  private static final long serialVersionUID = 1L;

  /**
   * Construct.
   * 
   * @param name
   *          Component name
   * @param urls
   *          The url list model
   */
  public WebPageListView(String name, final IModel<List<IDoapHomepage>> urls) {
    super(name, urls);
  }

  /**
   * @see ListView#populateItem(ListItem)
   */
  protected void populateItem(ListItem<IDoapHomepage> listItem) {
    final IDoapHomepage page = (IDoapHomepage) listItem.getModelObject();
    ExternalLink externalLink = new ExternalLink("linkURL", page.getURI());
    externalLink.add(new Label("linkText", page.getLabel()));
    listItem.add(externalLink);
  }
}
