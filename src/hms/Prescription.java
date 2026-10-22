package hms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Prescription extends Entity {

    private Integer appointmentId;
    private LocalDate prescriptionDate;
    private String notes;

    /** The medicines on this prescription (the many-to-many link, held as a list of objects). */
    private List<PrescriptionItem> items = new ArrayList<>();

    public List<PrescriptionItem> getItems() { return items; }

    @Override
    public void copyHiddenFrom(Entity old) { this.items = ((Prescription) old).items; }

    public Prescription() { }

    public Prescription(Integer appointmentId, LocalDate prescriptionDate, String notes) {
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.notes = notes;
    }

    @Override
    public Object[] row() {
        return new Object[]{getId(), h().appointmentPatient(appointmentId), prescriptionDate, notes, items.size() + " medicine(s)"};
    }

    @Override
    public Map<String, String> values() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("appointmentId", s(appointmentId));
        m.put("prescriptionDate", s(prescriptionDate));
        m.put("notes", s(notes));
        return m;
    }

    @Override
    public void apply(Map<String, Object> v) {
        appointmentId = (Integer) v.get("appointmentId");
        prescriptionDate = (LocalDate) v.get("prescriptionDate");
        notes = (String) v.get("notes");
    }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDate prescriptionDate) { this.prescriptionDate = prescriptionDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
