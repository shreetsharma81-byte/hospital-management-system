package hms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestResult extends Entity {

    private Integer patientId;
    private Integer labTestId;
    private Integer appointmentId;
    private String resultValue;
    private String remarks;
    private LocalDate testDate;

    public TestResult() { }

    public TestResult(Integer patientId, Integer labTestId, Integer appointmentId, String resultValue, String remarks, LocalDate testDate) {
        this.patientId = patientId;
        this.labTestId = labTestId;
        this.appointmentId = appointmentId;
        this.resultValue = resultValue;
        this.remarks = remarks;
        this.testDate = testDate;
    }

    @Override
    public Object[] row() {
        return new Object[]{getId(), h().patientName(patientId), h().labTestName(labTestId), appointmentId, resultValue, remarks, testDate};
    }

    @Override
    public Map<String, String> values() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("patientId", s(patientId));
        m.put("labTestId", s(labTestId));
        m.put("appointmentId", s(appointmentId));
        m.put("resultValue", s(resultValue));
        m.put("remarks", s(remarks));
        m.put("testDate", s(testDate));
        return m;
    }

    @Override
    public void apply(Map<String, Object> v) {
        patientId = (Integer) v.get("patientId");
        labTestId = (Integer) v.get("labTestId");
        appointmentId = (Integer) v.get("appointmentId");
        resultValue = (String) v.get("resultValue");
        remarks = (String) v.get("remarks");
        testDate = (LocalDate) v.get("testDate");
    }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public Integer getLabTestId() { return labTestId; }
    public void setLabTestId(Integer labTestId) { this.labTestId = labTestId; }
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public String getResultValue() { return resultValue; }
    public void setResultValue(String resultValue) { this.resultValue = resultValue; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDate getTestDate() { return testDate; }
    public void setTestDate(LocalDate testDate) { this.testDate = testDate; }
}
