package com.raven.table.cell;

import com.raven.table.model.TableRowData;
import com.raven.table.TableCustom;

public class CellGender extends TableCustomCell {

    public CellGender() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jrFemale = new javax.swing.JRadioButton();
        jrMale = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();

        buttonGroup1.add(jrFemale);
        jrFemale.setText("Female");

        buttonGroup1.add(jrMale);
        jrMale.setSelected(true);
        jrMale.setText("Male");

        jLabel2.setText("Gender");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrMale)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jrFemale)
                .addContainerGap(160, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jrFemale, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jrMale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton jrFemale;
    private javax.swing.JRadioButton jrMale;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setData(Object data) {
        if (data.toString().equals("Male")) {
            jrMale.setSelected(true);
        } else {
            jrFemale.setSelected(true);
        }
    }

    @Override
    public Object getData() {
        return jrMale.isSelected() ? "Male" : "Female";
    }

    @Override
    public TableCustomCell createComponentCellEditor(TableCustom table, TableRowData data, Object cellData, int row, int column) {
        CellGender cell = new CellGender();
        cell.setData(cellData);
        return cell;
    }
}
