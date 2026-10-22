package hms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Admission extends Entity {

    private Integer patientId;
    private Integer doctorId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private String roomNo;
    private String reason;
    private String status = "Admitted";

    public Admission() { }

    public Admission(Integer patientId, Integer doctorId, LocalDate admissionDate, LocalDate dischargeDate, String roomNo, String reason, String status) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.roomNo = roomNo;
        this.reason = reason;
        this.status = status;
    }

    @Override
    public Object[] row() {
        return new Object[]{getId(), h().patientName(patientId), h().doctorName(doctorId), admissionDate, dischargeDate, roomNo, reason, status};
    }

    @Override
    public Map<String, String> values() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("patientId", s(patientId));
        m.put("doctorId", s(doctorId));
        m.put("admissionDate", s(admissionDate));
        m.put("dischargeDate", s(dischargeDate));
        m.put("roomNo", s(roomNo));
        m.put("reason", s(reason));
        m.put("status", s(status));
        return m;
    }

    @Override
    public void apply(Map<String, Object> v) {
        patientId = (Integer) v.get("patientId");
        doctorId = (Integer) v.get("doctorId");
        admissionDate = (LocalDate) v.get("admissionDate");
        dischargeDate = (LocalDate) v.get("dischargeDate");
        roomNo = (String) v.get("roomNo");
        reason = (String) v.get("reason");
        status = (String) v.get("status");
    }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }
    public LocalDate getDischargeDate() { return dischargeDate; }
    public void setDischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
