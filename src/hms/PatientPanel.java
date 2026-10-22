package hms;

public class PatientPanel extends CrudPanel<Patient> {
    public PatientPanel() {
        super(new String[]{"Id", "Name", "Date of Birth", "Gender", "Blood Group", "Phone", "Address"},
            Hospital.get().patients, Patient::new,
            Field.text("firstName", "First name", true),
            Field.text("lastName", "Last name", true),
            Field.date("dateOfBirth", "Date of birth", true),
            Field.choice("gender", "Gender", true, "Male", "Female", "Other"),
            Field.choice("bloodGroup", "Blood group", false, "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"),
            Field.text("phone", "Phone", true),
            Field.text("address", "Address", false));
    }

    /** Replaces the foreign keys of the database: a patient with history cannot be deleted. */
    @Override
    protected void checkDelete(Patient p) {
        Hospital h = Hospital.get();
        int id = p.getId();
        if (h.appointments.exists(a -> a.getPatientId() == id) || h.admissions.exists(a -> a.getPatientId() == id)
                || h.bills.exists(b -> b.getPatientId() == id) || h.testResults.exists(t -> t.getPatientId() == id)) {
            throw new IllegalStateException("This patient has appointments, admissions, bills or test results.");
        }
    }
}
