package hms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Bill extends Entity {

    private Integer patientId;
    private LocalDate billDate;
    private BigDecimal totalAmount;
    private String paymentStatus = "Pending";

    public Bill() { }

    public Bill(Integer patientId, LocalDate billDate, BigDecimal totalAmount, String paymentStatus) {
        this.patientId = patientId;
        this.billDate = billDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public Object[] row() {
        return new Object[]{getId(), h().patientName(patientId), billDate, totalAmount, paymentStatus};
    }

    @Override
    public Map<String, String> values() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("patientId", s(patientId));
        m.put("billDate", s(billDate));
        m.put("totalAmount", s(totalAmount));
        m.put("paymentStatus", s(paymentStatus));
        return m;
    }

    @Override
    public void apply(Map<String, Object> v) {
        patientId = (Integer) v.get("patientId");
        billDate = (LocalDate) v.get("billDate");
        totalAmount = (BigDecimal) v.get("totalAmount");
        paymentStatus = (String) v.get("paymentStatus");
    }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
