package hms;

import javax.swing.*;
import java.time.LocalDate;

public class AdmissionPanel extends CrudPanel<Admission> {
    public AdmissionPanel() {
        super(new String[]{"Id", "Patient", "Doctor", "Admitted", "Discharged", "Room", "Reason", "Status"},
            Hospital.get().admissions, Admission::new,
            Field.lookup("patientId", "Patient", Hospital.get()::patientOptions),
            Field.lookup("doctorId", "Doctor", Hospital.get()::doctorOptions),
            Field.date("admissionDate", "Admission date", true),
            Field.date("dischargeDate", "Discharge date", false),
            Field.text("roomNo", "Room no", true),
            Field.text("reason", "Reason", false),
            Field.choice("status", "Status", true, "Admitted", "Discharged"));
    }

    /** Same job as the CHECK constraint in SQL. */
    @Override
    protected void validate(Admission c) {
        if (c.getDischargeDate() != null && c.getDischargeDate().isBefore(c.getAdmissionDate())) {
            throw new IllegalArgumentException("Discharge date cannot be before the admission date.");
        }
    }

    /** POLYMORPHISM: only this screen has a Discharge button. */
    @Override
    protected void extraButtons(JPanel bar) {
        JButton b = new JButton("Discharge");
        b.addActionListener(e -> {
            Admission a = selected();
            if (a == null) return;
            if (!"Admitted".equals(a.getStatus())) {
                JOptionPane.showMessageDialog(this, "Already discharged.");
                return;
            }
            a.setDischargeDate(LocalDate.now());
            a.setStatus("Discharged");
            refresh();
            JOptionPane.showMessageDialog(this, "Patient discharged.");
        });
        bar.add(b);
    }
}
