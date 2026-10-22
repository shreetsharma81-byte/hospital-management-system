package hms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Manages the medicines of ONE prescription (the many-to-many link between Prescription and Medicine). */
public class PrescriptionItemsDialog extends JDialog {

    private final Prescription prescription;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Medicine", "Unit Price", "Dosage", "Frequency", "Days"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable grid = new JTable(model);

    public PrescriptionItemsDialog(Component parent, Prescription prescription) {
        super(SwingUtilities.getWindowAncestor(parent), "Medicines of prescription #" + prescription.getId(),
                ModalityType.APPLICATION_MODAL);
        this.prescription = prescription;

        grid.setRowHeight(24);
        grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton add = new JButton("Add medicine");
        JButton remove = new JButton("Remove selected");
        JButton close = new JButton("Close");
        add.addActionListener(e -> addMedicine());
        remove.addActionListener(e -> removeSelected());
        close.addActionListener(e -> dispose());

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.add(add);
        bar.add(remove);
        bar.add(close);

        add(new JScrollPane(grid), BorderLayout.CENTER);
        add(bar, BorderLayout.SOUTH);
        setSize(700, 340);
        setLocationRelativeTo(parent);
        load();
    }

    private void load() {
        model.setRowCount(0);
        Hospital h = Hospital.get();
        for (PrescriptionItem i : prescription.getItems()) {
            Medicine m = h.medicines.find(i.getMedicineId()).orElse(null);
            model.addRow(new Object[]{h.medicineName(i.getMedicineId()), m == null ? "" : m.getUnitPrice(),
                    i.getDosage(), i.getFrequency(), i.getDurationDays()});
        }
    }

    private void addMedicine() {
        Hospital h = Hospital.get();
        JComboBox<Option> medicine = new JComboBox<>();
        for (Option o : h.medicineOptions()) medicine.addItem(o);
        JTextField dosage = new JTextField("1 tablet");
        JTextField frequency = new JTextField("Twice a day");
        JTextField days = new JTextField("5");

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Medicine *"));        form.add(medicine);
        form.add(new JLabel("Dosage"));            form.add(dosage);
        form.add(new JLabel("Frequency"));         form.add(frequency);
        form.add(new JLabel("Duration (days) *")); form.add(days);

        if (JOptionPane.showConfirmDialog(this, form, "Add medicine",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        Option chosen = (Option) medicine.getSelectedItem();
        if (chosen == null) return;
        try {
            int d = Integer.parseInt(days.getText().trim());
            if (d <= 0) throw new NumberFormatException();
            boolean duplicate = prescription.getItems().stream().anyMatch(i -> i.getMedicineId() == chosen.id());
            if (duplicate) {
                JOptionPane.showMessageDialog(this, "This medicine is already on the prescription.");
                return;
            }
            Medicine m = h.medicines.find(chosen.id()).orElseThrow();
            if (m.getStockQuantity() <= 0) {
                JOptionPane.showMessageDialog(this, "Out of stock.");
                return;
            }
            m.setStockQuantity(m.getStockQuantity() - 1);        // same job as the SQL trigger
            prescription.getItems().add(new PrescriptionItem(chosen.id(), dosage.getText().trim(),
                    frequency.getText().trim(), d));
            load();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Duration must be a whole number above 0.");
        }
    }

    private void removeSelected() {
        int row = grid.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a medicine first.");
            return;
        }
        prescription.getItems().remove(row);
        load();
    }
}
