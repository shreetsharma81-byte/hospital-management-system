package hms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SINGLETON: the one and only "database" of the program. It simply holds one Repository
 * per entity and is filled with hardcoded sample data when the program starts.
 */
public final class Hospital {

    private static final Hospital INSTANCE = new Hospital();
    public static Hospital get() { return INSTANCE; }

    public final Repository<Department> departments = new Repository<>();
    public final Repository<Doctor> doctors = new Repository<>();
    public final Repository<Patient> patients = new Repository<>();
    public final Repository<Appointment> appointments = new Repository<>();
    public final Repository<Admission> admissions = new Repository<>();
    public final Repository<Prescription> prescriptions = new Repository<>();
    public final Repository<Medicine> medicines = new Repository<>();
    public final Repository<LabTest> labTests = new Repository<>();
    public final Repository<TestResult> testResults = new Repository<>();
    public final Repository<Bill> bills = new Repository<>();

    private Hospital() { seed(); }

    // ------------------------------------------------------------------ sample data
    private void seed() {
        departments.add(new Department("Cardiology", "Block A, Floor 2", "EXT-201"));
        departments.add(new Department("Neurology", "Block A, Floor 3", "EXT-202"));
        departments.add(new Department("Orthopedics", "Block B, Floor 1", "EXT-203"));
        departments.add(new Department("Pediatrics", "Block C, Floor 1", "EXT-204"));
        departments.add(new Department("General Medicine", "Block B, Ground Floor", "EXT-205"));

        doctors.add(new Doctor("Rajesh", "Sharma", "9811100001", "rajesh.sharma@cityhospital.in", "Cardiologist", 1));
        doctors.add(new Doctor("Anita", "Verma", "9811100002", "anita.verma@cityhospital.in", "Neurologist", 2));
        doctors.add(new Doctor("Vikram", "Singh", "9811100003", "vikram.singh@cityhospital.in", "Orthopedic Surgeon", 3));
        doctors.add(new Doctor("Priya", "Mehta", "9811100004", "priya.mehta@cityhospital.in", "Pediatrician", 4));
        doctors.add(new Doctor("Suresh", "Gupta", "9811100005", "suresh.gupta@cityhospital.in", "General Physician", 5));
        doctors.add(new Doctor("Neha", "Kapoor", "9811100006", "neha.kapoor@cityhospital.in", "Interventional Cardiologist", 1));

        patients.add(new Patient("Aarav", "Jain", LocalDate.of(2001, 5, 14), "Male", "B+", "9812300001", "Jaipur"));
        patients.add(new Patient("Diya", "Patel", LocalDate.of(1995, 11, 2), "Female", "O+", "9812300002", "Ajmer"));
        patients.add(new Patient("Rohan", "Meena", LocalDate.of(1988, 3, 21), "Male", "A+", "9812300003", "Sikar"));
        patients.add(new Patient("Kavya", "Rathore", LocalDate.of(2015, 7, 9), "Female", "AB+", "9812300004", "Jodhpur"));
        patients.add(new Patient("Imran", "Khan", LocalDate.of(1972, 12, 30), "Male", "O-", "9812300005", "Kota"));
        patients.add(new Patient("Sneha", "Agarwal", LocalDate.of(1999, 1, 18), "Female", "B-", "9812300006", "Jaipur"));
        patients.add(new Patient("Mohan", "Lal", LocalDate.of(1960, 8, 25), "Male", "A-", "9812300007", "Bikaner"));
        patients.add(new Patient("Pooja", "Choudhary", LocalDate.of(1992, 4, 5), "Female", "O+", "9812300008", "Udaipur"));

        appointments.add(new Appointment(1, 1, LocalDate.of(2026, 10, 20), LocalTime.of(10, 0), "Chest pain checkup", "Scheduled"));
        appointments.add(new Appointment(2, 2, LocalDate.of(2026, 10, 20), LocalTime.of(11, 30), "Recurring migraine", "Scheduled"));
        appointments.add(new Appointment(3, 3, LocalDate.of(2026, 10, 15), LocalTime.of(9, 30), "Knee pain", "Completed"));
        appointments.add(new Appointment(4, 4, LocalDate.of(2026, 10, 16), LocalTime.of(12, 0), "Fever and cough", "Completed"));
        appointments.add(new Appointment(5, 1, LocalDate.of(2026, 10, 14), LocalTime.of(10, 30), "Blood pressure review", "Completed"));
        appointments.add(new Appointment(6, 5, LocalDate.of(2026, 10, 21), LocalTime.of(14, 0), "General checkup", "Scheduled"));
        appointments.add(new Appointment(7, 1, LocalDate.of(2026, 10, 13), LocalTime.of(11, 0), "Heart rhythm follow-up", "Completed"));
        appointments.add(new Appointment(8, 2, LocalDate.of(2026, 10, 22), LocalTime.of(15, 30), "Dizziness", "Cancelled"));

        medicines.add(new Medicine("Paracetamol 500mg", bd("2.50"), 500));
        medicines.add(new Medicine("Amoxicillin 250mg", bd("8.00"), 300));
        medicines.add(new Medicine("Ibuprofen 400mg", bd("3.75"), 400));
        medicines.add(new Medicine("Atorvastatin 10mg", bd("12.00"), 200));
        medicines.add(new Medicine("Amlodipine 5mg", bd("6.50"), 250));
        medicines.add(new Medicine("Cetirizine 10mg", bd("2.00"), 350));
        medicines.add(new Medicine("Pantoprazole 40mg", bd("9.00"), 220));
        medicines.add(new Medicine("ORS Sachet", bd("15.00"), 150));

        Prescription p1 = prescriptions.add(new Prescription(3, LocalDate.of(2026, 10, 15), "Rest and physiotherapy advised"));
        Prescription p2 = prescriptions.add(new Prescription(4, LocalDate.of(2026, 10, 16), "Drink plenty of fluids"));
        Prescription p3 = prescriptions.add(new Prescription(5, LocalDate.of(2026, 10, 14), "Reduce salt intake, review in 4 weeks"));
        Prescription p4 = prescriptions.add(new Prescription(7, LocalDate.of(2026, 10, 13), "Continue medication, avoid heavy exertion"));
        p1.getItems().add(new PrescriptionItem(3, "1 tablet", "Twice a day", 5));
        p1.getItems().add(new PrescriptionItem(7, "1 tablet", "Once, before breakfast", 5));
        p2.getItems().add(new PrescriptionItem(1, "1 tablet", "Three times a day", 3));
        p2.getItems().add(new PrescriptionItem(2, "1 capsule", "Twice a day", 5));
        p2.getItems().add(new PrescriptionItem(8, "1 sachet", "When needed", 3));
        p3.getItems().add(new PrescriptionItem(5, "1 tablet", "Once a day", 30));
        p3.getItems().add(new PrescriptionItem(4, "1 tablet", "Once at night", 30));
        p4.getItems().add(new PrescriptionItem(5, "1 tablet", "Once a day", 30));

        labTests.add(new LabTest("Complete Blood Count", bd("350.00")));
        labTests.add(new LabTest("Lipid Profile", bd("600.00")));
        labTests.add(new LabTest("X-Ray Knee", bd("500.00")));
        labTests.add(new LabTest("ECG", bd("300.00")));
        labTests.add(new LabTest("Blood Sugar (Fasting)", bd("150.00")));

        testResults.add(new TestResult(3, 3, 3, "Mild cartilage wear", "Physiotherapy recommended", LocalDate.of(2026, 10, 15)));
        testResults.add(new TestResult(5, 2, 5, "LDL 165 mg/dL", "High, medication started", LocalDate.of(2026, 10, 14)));
        testResults.add(new TestResult(7, 4, 7, "Normal sinus rhythm", "No abnormality", LocalDate.of(2026, 10, 13)));
        testResults.add(new TestResult(4, 1, 4, "WBC slightly elevated", "Likely viral infection", LocalDate.of(2026, 10, 16)));

        admissions.add(new Admission(7, 1, LocalDate.of(2026, 10, 17), null, "ICU-3", "Observation after arrhythmia", "Admitted"));
        admissions.add(new Admission(3, 3, LocalDate.of(2026, 10, 15), LocalDate.of(2026, 10, 17), "W2-12", "Knee surgery recovery", "Discharged"));

        bills.add(new Bill(3, LocalDate.of(2026, 10, 17), bd("18500.00"), "Paid"));
        bills.add(new Bill(4, LocalDate.of(2026, 10, 16), bd("950.00"), "Paid"));
        bills.add(new Bill(5, LocalDate.of(2026, 10, 14), bd("1800.00"), "Pending"));
        bills.add(new Bill(7, LocalDate.of(2026, 10, 17), bd("42000.00"), "Pending"));
    }

    private static BigDecimal bd(String s) { return new BigDecimal(s); }

    // ------------------------------------------------------------------ name lookups
    public String patientName(Integer id) { return patients.find(id).map(Patient::getFullName).orElse("?"); }
    public String doctorName(Integer id) { return doctors.find(id).map(Doctor::getFullName).orElse("?"); }
    public String departmentName(Integer id) { return departments.find(id).map(Department::getName).orElse("?"); }
    public String medicineName(Integer id) { return medicines.find(id).map(Medicine::getName).orElse("?"); }
    public String labTestName(Integer id) { return labTests.find(id).map(LabTest::getName).orElse("?"); }
    public String appointmentPatient(Integer apptId) {
        return appointments.find(apptId).map(a -> patientName(a.getPatientId())).orElse("?");
    }

    // ------------------------------------------------------------------ drop-down lists
    public List<Option> patientOptions() {
        List<Option> l = new ArrayList<>();
        for (Patient p : patients.all()) l.add(new Option(p.getId(), p.getFullName()));
        return l;
    }
    public List<Option> doctorOptions() {
        List<Option> l = new ArrayList<>();
        for (Doctor d : doctors.all()) l.add(new Option(d.getId(), d.getFullName()));
        return l;
    }
    public List<Option> departmentOptions() {
        List<Option> l = new ArrayList<>();
        for (Department d : departments.all()) l.add(new Option(d.getId(), d.getName()));
        return l;
    }
    public List<Option> medicineOptions() {
        List<Option> l = new ArrayList<>();
        for (Medicine m : medicines.all()) l.add(new Option(m.getId(), m.getName()));
        return l;
    }
    public List<Option> labTestOptions() {
        List<Option> l = new ArrayList<>();
        for (LabTest t : labTests.all()) l.add(new Option(t.getId(), t.getName()));
        return l;
    }
    public List<Option> appointmentOptions() {
        List<Option> l = new ArrayList<>();
        for (Appointment a : appointments.all()) {
            l.add(new Option(a.getId(), "#" + a.getId() + " - " + patientName(a.getPatientId())
                    + " (" + a.getAppointmentDate() + ")"));
        }
        return l;
    }
}
