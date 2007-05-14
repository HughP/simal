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
package uk.ac.osswatch.simal.webGUI.controls;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class ViewPartHelper {


    /**
     * Add a text widget with label to the supplied parent.
     * @param labeltxt
     * @param parent
     * @return
     */
    protected static Text addText(String labeltxt, Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(labeltxt);
        Text text = new Text(parent, SWT.BORDER);
        text.setLayoutData(getGridData());
        return text;
    }

    /**
     * Add a list widget with label to the supplied parent with a
     * predefined set of items in the list.
     * 
     * @param labeltxt
     * @param listitems
     * @param parent
     * @param part 
     * @return
     */
    protected static List addList(String labeltxt, String[] listItems, Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(labeltxt);
        List list = new List(parent, SWT.BORDER);
        list.setLayoutData(getGridData());
        list.setItems(listItems);
        return list;
    }

    private static GridData getGridData() {
        GridData gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.verticalAlignment = GridData.CENTER;
        return gd;
    }

    public static String getDateAsString(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.toLocaleString();
        }
    }

}
