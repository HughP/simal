package uk.ac.osswatch.simal.webGUI.controls;

import java.util.Date;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.layout.GridData;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.rwt.widgets.Label;
import org.eclipse.rap.rwt.widgets.List;
import org.eclipse.rap.rwt.widgets.Text;

public class ViewPartHelper {


    /**
     * Add a text widget with label to the supplied parent.
     * @param labeltxt
     * @param parent
     * @return
     */
    protected static Text addText(String labeltxt, Composite parent) {
        Label label = new Label(parent, RWT.NONE);
        label.setText(labeltxt);
        Text text = new Text(parent, RWT.BORDER);
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
        Label label = new Label(parent, RWT.NONE);
        label.setText(labeltxt);
        List list = new List(parent, RWT.BORDER);
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
