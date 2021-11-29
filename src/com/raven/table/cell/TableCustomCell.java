package com.raven.table.cell;

import com.raven.table.model.TableRowData;
import com.raven.table.TableCustom;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;

public abstract class TableCustomCell extends Cell {

    private int row;
    private int column;

    public void setRowColumn(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public abstract void setData(Object data);

    public abstract Object getData();

    public Component createComponentCellRender(TableCustom table, TableRowData data, int row, int column) {
        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
        if (data.toTableRow().length > column) {
            c.setText(data.toTableRow()[column].toString());
        }
        return c;
    }

    public Component createComponentCellRenderOnEditor(TableCustom table, TableRowData data, int row, int column) {
        return createComponentCellRender(table, data, row, column);
    }

    public abstract TableCustomCell createComponentCellEditor(TableCustom table, TableRowData data, Object cellData, int row, int column);
}
