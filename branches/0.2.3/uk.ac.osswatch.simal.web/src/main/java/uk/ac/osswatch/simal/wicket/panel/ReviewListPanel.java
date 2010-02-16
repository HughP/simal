package uk.ac.osswatch.simal.wicket.panel;

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
import java.util.Set;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortableReviewDataProvider;

/**
 * A panel for listing reviews. 
 */
public class ReviewListPanel extends Panel {
  private static final long serialVersionUID = -7641153470731218965L;
  private Set<IReview> reviews;

  public ReviewListPanel(String id) throws SimalRepositoryException {
    super(id);
    this.reviews = SimalRepositoryFactory.getReviewService().getReviews();
    SortableReviewDataProvider dataProvider = new SortableReviewDataProvider(this.reviews);
    addReviewList(dataProvider);
  }

  public ReviewListPanel(String id, Set<IReview> reviews) {
    super(id);
    this.reviews = reviews;
    SortableReviewDataProvider dataProvider = new SortableReviewDataProvider(
        this.reviews);
    addReviewList(dataProvider);
  }

  @SuppressWarnings("unchecked")
  private void addReviewList(SortableReviewDataProvider dataProvider) {
    List<AbstractColumn> columns = new ArrayList<AbstractColumn>();
    columns.add(new PropertyColumn(new Model<String>("Project"), "project.label"));
    columns.add(new PropertyColumn(new Model("Reviewer"), "reviewer.label"));
    columns.add(new PropertyColumn(new Model("Date"), "shortDate"));
    columns.add(new PropertyColumn(new Model("Type"), "type"));
    
    //FIXME: dataProvider.setSort(SortableReviewDataProvider.SORT_PROPERTY_NAME, true);
    add(new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider, 15));
  }

  /**
   * Add a preview to the list being displayed. This method does not add the
   * review to the underlying data storage mechanism, it only adds it to the
   * GUI.
   * 
   * @param person
   */
  public void addReview(IReview review) {
    this.reviews.add(review);
  }
}
