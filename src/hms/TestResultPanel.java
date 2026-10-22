package hms;

public class TestResultPanel extends CrudPanel<TestResult> {
    public TestResultPanel() {
        super(new String[]{"Id", "Patient", "Test", "Appointment", "Result", "Remarks", "Date"},
            Hospital.get().testResults, TestResult::new,
            Field.lookup("patientId", "Patient", Hospital.get()::patientOptions),
            Field.lookup("labTestId", "Lab test", Hospital.get()::labTestOptions),
            Field.lookup("appointmentId", "Appointment", Hospital.get()::appointmentOptions),
            Field.text("resultValue", "Result", false),
            Field.text("remarks", "Remarks", false),
            Field.date("testDate", "Test date", true));
    }
}
