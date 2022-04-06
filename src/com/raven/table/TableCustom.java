package com.raven.table;

import com.raven.table.model.TableRowData;
import com.raven.table.cell.TableCustomCelLEditor;
import com.raven.table.cell.TableCustomCell;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class TableCustom extends JTable {

    private final List<TableRowData> datas = new ArrayList<>();
    private Animator animatorEdit;
    private Animator animatorDelete;
    private Animator animatorInsert;
    private int updateRow;
    private int deleteRow;
    private int insertRow;
    private boolean isEdit;
    private boolean insertAndUpdate;
    private int animateRowHeight = 200;

    public TableCustom() {
        init();
    }

    private void init() {
        setRowHeight(30);
        setDefaultRenderer(Object.class, new TableCustomCellRender());
        initAnimatorEdit();
        initAnimatorDelete();
        initAnimatorInsert();
    }

    private void initAnimatorEdit() {
        TimingTarget target = new TimingTargetAdapter() {
            private boolean show;

            @Override
            public void begin() {
                show = isEdit;
            }

            @Override
            public void end() {
                if (isEdit == false) {
                    TableRowData model = getModelData(updateRow);
                    Object[] data = model.toTableRow();
                    for (int i = 0; i < getColumnCount(); i++) {
                        if (i < data.length) {
                            setValueAt(data[i], updateRow, i);
                        }
                    }
                    model.setEditing(false);
                }
            }

            @Override
            public void timingEvent(float fraction) {
                if (show) {
                    int height = (int) (fraction * animateRowHeight) + getRowHeight();
                    setRowHeight(updateRow, height);
                } else {
                    int height = (int) ((1f - fraction) * animateRowHeight) + getRowHeight();
                    setRowHeight(updateRow, height);
                }
            }
        };
        animatorEdit = new Animator(300, target);
        animatorEdit.setAcceleration(.5f);
        animatorEdit.setDeceleration(.5f);
        animatorEdit.setResolution(0);
    }

    private void initAnimatorDelete() {
        TimingTarget target = new TimingTargetAdapter() {
            private int rowHeight;

            @Override
            public void begin() {
                rowHeight = getRowHeight(deleteRow);
            }

            @Override
            public void timingEvent(float fraction) {
                int height = (int) ((1f - fraction) * rowHeight) + 1;
                setRowHeight(deleteRow, height);
            }

            @Override
            public void end() {
                removeRow(deleteRow);
            }
        };
        animatorDelete = new Animator(300, target);
        animatorDelete.setAcceleration(.5f);
        animatorDelete.setDeceleration(.5f);
        animatorDelete.setResolution(0);
    }

    private void initAnimatorInsert() {
        TimingTarget target = new TimingTargetAdapter() {
            private int rowHeight;

            @Override
            public void begin() {
                if (insertAndUpdate) {
                    rowHeight = animateRowHeight + getRowHeight();
                } else {
                    rowHeight = getRowHeight();
                }
            }

            @Override
            public void end() {
                insertAndUpdate = false;
            }

            @Override
            public void timingEvent(float fraction) {
                int height = (int) (fraction * rowHeight);
                if (height < 1) {
                    height = 1;
                }
                setRowHeight(insertRow, height);
            }
        };
        animatorInsert = new Animator(300, target);
        animatorInsert.setAcceleration(.5f);
        animatorInsert.setDeceleration(.5f);
        animatorInsert.setResolution(0);
    }

    public TableRowData getModelData(int row) {
        row = convertRowIndexToModel(row);
        return datas.get(row);
    }

    public void updateModelData(int row, TableRowData data) {
        row = convertRowIndexToModel(row);
        data.setEditing(datas.get(row).isEditing());
        datas.set(row, data);
        repaint();
    }

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) getModel();
    }

    public void addTableCell(TableCustomCell cell, int index) {
        getColumnModel().getColumn(index).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int row, int column) {
                Component com = super.getTableCellRendererComponent(jtable, o, bln, bln1, row, column);
                TableRowData model = getModelData(row);
                if (model.isEditing()) {
                    Component c = getEditor(cell.createComponentCellEditor(TableCustom.this, model, o, row, column), getModelData(row), com.getBackground(), row, column);
                    return c;
                } else {
                    Component c = cell.createComponentCellRender(TableCustom.this, getModelData(row), row, column);
                    if (c != null) {
                        c.setBackground(com.getBackground());
                        return c;
                    } else {
                        return com;
                    }
                }
            }
        });
        getColumnModel().getColumn(index).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private TableCustomCell tableCell;

            @Override
            public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column) {
                DefaultTableCellRenderer com = new DefaultTableCellRenderer();
                com.setOpaque(false);
                TableRowData model = getModelData(row);
                if (model.toTableRow().length > column) {
                    com.setText(model.toTableRow()[column].toString());
                }
                tableCell = cell.createComponentCellEditor(TableCustom.this, model, o, row, column);
                return getEditor(tableCell, model, jtable.getSelectionBackground(), row, column);
            }

            @Override
            public Object getCellEditorValue() {
                return tableCell.getData();
            }
        });
    }

    private Component getEditor(TableCustomCell cellEditor, TableRowData data, Color color, int row, int column) {
        cellEditor.setRowColumn(row, column);
        TableCustomCelLEditor cell = new TableCustomCelLEditor(getRowHeight());
        Component com = cellEditor.createComponentCellRenderOnEditor(TableCustom.this, data, row, column);
        if (com != null) {
            cell.add(com);
            JComponent cc = (JComponent) com;
            cc.setOpaque(false);
        }
        cell.add(cellEditor);
        cell.setBackground(color);
        return cell;
    }

    private void removeRow(int row) {
        stopCellEditing();
        datas.remove(row);
        getTableModel().removeRow(row);
    }

    public void editRowAt(int row) {
        if (!animatorEdit.isRunning()) {
            updateRow = row;
            isEdit = true;
            getModelData(row).setEditing(true);
            animatorEdit.start();
        }
    }

    public void cancelEditRowAt(int row) {
        if (!animatorEdit.isRunning()) {
            isEdit = false;
            updateRow = row;
            animatorEdit.start();
        }
    }

    public void addRow(TableRowData data, boolean animate) {
        if (!animatorInsert.isRunning()) {
            stopCellEditing();
            datas.add(data);
            getTableModel().addRow(data.toTableRow());
            insertRow = getRowCount() - 1;
            if (animate) {
                setRowHeight(insertRow, 1);
                animatorInsert.start();
            }
        }
    }

    public void insertRow(TableRowData data, int index, boolean animate) {
        if (!animatorInsert.isRunning()) {
            stopCellEditing();
            datas.add(index, data);
            getTableModel().insertRow(index, data.toTableRow());
            insertRow = index;
            if (animate) {
                setRowHeight(insertRow, 1);
                animatorInsert.start();
            }
        }
    }

    public void insertRowWithEdit(TableRowData data, int index, boolean animate) {
        if (!animatorInsert.isRunning()) {
            stopCellEditing();
            datas.add(index, data);
            getTableModel().insertRow(index, data.toTableRow());
            setRowSelectionInterval(index, index);
            insertRow = index;
            if (animate) {
                setRowHeight(insertRow, 1);
                insertAndUpdate = true;
                getModelData(index).setEditing(true);
                animatorInsert.start();
            }
        }
    }

    public void deleteRowAt(int row, boolean animate) {
        if (animate) {
            if (!animatorDelete.isRunning()) {
                deleteRow = row;
                animatorDelete.start();
            }
        } else {
            removeRow(row);
        }
    }

    public void removeAllRows() {
        getTableModel().setRowCount(0);
        datas.clear();
    }

    public void scrollToIndex(int index) {
        getSelectionModel().setSelectionInterval(index, index);
        Rectangle r = new Rectangle(getCellRect(index, 0, true));
        r.setSize(new Dimension(1, getRowHeight()));
        scrollRectToVisible(r);
    }

    public void stopCellEditing() {
        if (isEditing()) {
            getCellEditor().stopCellEditing();
        }
    }

    public void autoRowHeight() {
        for (int row = 0; row < getRowCount(); row++) {
            int r = convertRowIndexToModel(row);
            if (datas.get(r).isEditing()) {
                int height = getRowHeight();
                for (int col = 0; col < getColumnCount(); col++) {
                    Component comp = prepareRenderer(getCellRenderer(row, col), row, col);
                    if (comp.getPreferredSize().height > height) {
                        height = comp.getPreferredSize().height;
                    }
                }
                setRowHeight(row, height);
            }
        }
    }

    private class TableCustomCellRender extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
            Component com = super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            setOpaque(false);
            com.setForeground(jtable.getForeground());
            TableCustomCelLEditor cell = new TableCustomCelLEditor(jtable.getRowHeight());
            cell.setBackground(com.getBackground());
            cell.add(com);
            return cell;
        }

        @Override
        protected void paintBorder(Graphics grphcs) {
        }
    }

    public int getAnimateRowHeight() {
        return animateRowHeight + getRowHeight();
    }

    public void setAnimateRowHeight(int animateRowHeight) {
        this.animateRowHeight = animateRowHeight - getRowHeight();
    }
}
