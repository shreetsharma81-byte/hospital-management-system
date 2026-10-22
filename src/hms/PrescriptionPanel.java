package hms;

import javax.swing.*;

public class PrescriptionPanel extends CrudPanel<Prescription> {
    public PrescriptionPanel() {
        super(new String[]{"Id", "Patient", "Date", "Notes", "Medicines"},
            Hospital.get().prescriptions, Prescription::new,
            Field.lookup("appointmentId", "Appointment", Hospital.get()::appointmentOptions),
            Field.date("prescriptionDate", "Prescription date", true),
            Field.text("notes", "Notes", false));
    }

    @Override
    protected void extraButtons(JPanel bar) {
        JButton b = new JButton("Medicines");
        b.addActionListener(e -> {
            Prescription p = selected();
            if (p != null) {
                new PrescriptionItemsDialog(this, p).setVisible(true);
                refresh();
            }
        });
        bar.add(b);
    }
}
