package hms;

public class AppointmentPanel extends CrudPanel<Appointment> {
    public AppointmentPanel() {
        super(new String[]{"Id", "Patient", "Doctor", "Date", "Time", "Reason", "Status"},
            Hospital.get().appointments, Appointment::new,
            Field.lookup("patientId", "Patient", Hospital.get()::patientOptions),
            Field.lookup("doctorId", "Doctor", Hospital.get()::doctorOptions),
            Field.date("appointmentDate", "Date", true),
            Field.time("appointmentTime", "Time", true),
            Field.text("reason", "Reason", false),
            Field.choice("status", "Status", true, "Scheduled", "Completed", "Cancelled"));
    }

    /** A doctor cannot have two appointments at the same date and time (the UNIQUE slot rule). */
    @Override
    protected void validate(Appointment c) {
        boolean taken = repo.exists(a -> a.getId() != c.getId()
                && a.getDoctorId().equals(c.getDoctorId())
                && a.getAppointmentDate().equals(c.getAppointmentDate())
                && a.getAppointmentTime().equals(c.getAppointmentTime()));
        if (taken) throw new IllegalArgumentException("This doctor already has an appointment at that date and time.");
    }

    @Override
    protected void checkDelete(Appointment ap) {
        Hospital h = Hospital.get();
        int id = ap.getId();
        if (h.prescriptions.exists(p -> p.getAppointmentId() == id) || h.testResults.exists(t -> t.getAppointmentId() == id)) {
            throw new IllegalStateException("A prescription or test result belongs to this appointment.");
        }
    }
}
