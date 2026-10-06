package org.yourcompany.yourproject.inventory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class InventoryPanel extends JPanel {

private JTable table;
private InventoryManager inventoryManager;
private DefaultTableModel tableModel;

private JComboBox<BloodGroup> bloodGroupCombo;
private JComboBox<BloodComponent> bloodComponentCombo;

private JTextField phoneField;
private JTextField collectionDateField;
private JTextField expiryDateField;

public InventoryPanel() {

    inventoryManager = new InventoryManager();

    setLayout(new BorderLayout());

    String[] columns = {
            "Unit ID",
            "Blood Group",
            "Blood Component",
            "Donor Phone",
            "Collection Date",
            "Expiration Date",
            "Status"
    };

    tableModel = new DefaultTableModel(columns, 0);
    table = new JTable(tableModel);

    add(new JScrollPane(table), BorderLayout.CENTER);

    JPanel formPanel = new JPanel(new FlowLayout());

    bloodGroupCombo = new JComboBox<>(BloodGroup.values());
    bloodComponentCombo = new JComboBox<>(BloodComponent.values());

    phoneField = new JTextField(10);

    collectionDateField =
            new JTextField(LocalDate.now().toString(), 10);

    expiryDateField =
            new JTextField(
                    LocalDate.now().plusDays(42).toString(),
                    10
            );

    JButton addButton = new JButton("Add Blood Unit");

    formPanel.add(new JLabel("Group:"));
    formPanel.add(bloodGroupCombo);

    formPanel.add(new JLabel("Component:"));
    formPanel.add(bloodComponentCombo);

    formPanel.add(new JLabel("Phone:"));
    formPanel.add(phoneField);

    formPanel.add(new JLabel("Collected:"));
    formPanel.add(collectionDateField);

    formPanel.add(new JLabel("Expires:"));
    formPanel.add(expiryDateField);

    formPanel.add(addButton);

    add(formPanel, BorderLayout.SOUTH);

    addButton.addActionListener(e -> addUnitAction());

    refreshTable();
}

private void refreshTable() {

    tableModel.setRowCount(0);

    List<BloodUnit> units =
            inventoryManager.getAllBloodUnits();

    for (BloodUnit u : units) {

        Object[] row = {
                u.getUnitId(),
                u.getBloodGroup(),
                u.getComponent(),
                u.getDonorPhone(),
                u.getCollectionDate(),
                u.getExpiryDate(),
                u.getStatus()
        };

        tableModel.addRow(row);
    }
}

private void addUnitAction() {

    try {

        BloodGroup bg =
                (BloodGroup) bloodGroupCombo.getSelectedItem();

        BloodComponent comp =
                (BloodComponent) bloodComponentCombo.getSelectedItem();

        String phone =
                phoneField.getText();

        LocalDate collectionDate =
                LocalDate.parse(collectionDateField.getText());

        BloodUnit newUnit =
                new BloodUnit(
                        0,
                        bg,
                        comp,
                        phone,
                        collectionDate,
                        BloodStatus.AVAILABLE
                );

        inventoryManager.addBloodUnit(newUnit);

        JOptionPane.showMessageDialog(
                this,
                "Blood unit added successfully!"
        );

        refreshTable();

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Invalid input format: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

}
