package hms;

public class DoctorPanel extends CrudPanel<Doctor> {
    public DoctorPanel() {
        super(new String[]{"Id", "Name", "Phone", "Email", "Specialization", "Department"},
            Hospital.get().doctors, Doctor::new,
            Field.text("firstName", "First name", true),
            Field.text("lastName", "Last name", true),
            Field.text("phone", "Phone", true),
            Field.text("email", "Email", false),
            Field.text("specialization", "Specialization", false),
            Field.lookup("departmentId", "Department", Hospital.get()::departmentOptions));
    }

    @Override
    protected void checkDelete(Doctor d) {
        Hospital h = Hospital.get();
        int id = d.getId();
        if (h.appointments.exists(a -> a.getDoctorId() == id) || h.admissions.exists(a -> a.getDoctorId() == id)) {
            throw new IllegalStateException("This doctor still has appointments or admissions.");
        }
    }
}
