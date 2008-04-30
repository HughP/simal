package uk.ac.osswatch.simal.wicket.data;
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


import javax.xml.namespace.QName;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

public class DetachableCategoryModel extends LoadableDetachableModel implements
		IModel {
	private static final long serialVersionUID = -9017519516676203598L;
	QName qname;

	public DetachableCategoryModel(IDoapCategory category) {
		this.qname = category.getQName();
	}

	public DetachableCategoryModel(QName qname) {
		this.qname = qname;
	}

	@Override
	protected Object load() {
		IDoapCategory category;
		try {
			category = UserApplication.getRepository().findCategory(qname);
		} catch (SimalRepositoryException e) {
			e.printStackTrace();
			category= null;
		}
		return category;
	}

}
