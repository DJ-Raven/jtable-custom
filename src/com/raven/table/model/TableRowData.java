package com.raven.table.model;

public abstract class TableRowData {

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    private boolean editing;

    public abstract Object[] toTableRow();
}
