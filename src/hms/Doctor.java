package hms;

import java.util.LinkedHashMap;
import java.util.Map;

public class Doctor extends Person {

    private String email;
    private String specialization;
    private Integer departmentId;

    public Doctor() { }

    public Doctor(String first, String last, String phone, String email,
                  String specialization, Integer departmentId) {
        setFirstName(first);
        setLastName(last);
        setPhone(phone);
        this.email = email;
        this.specialization = specialization;
        this.departmentId = departmentId;
    }

    @Override
    public String getFullName() { return "Dr. " + getFirstName() + " " + getLastName(); }

    @Override
    public Object[] row() {
        return new Object[]{getId(), getFullName(), getPhone(), email, specialization,
                h().departmentName(departmentId)};
    }

    @Override
    public Map<String, String> values() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("firstName", getFirstName());
        m.put("lastName", getLastName());
        m.put("phone", getPhone());
        m.put("email", s(email));
        m.put("specialization", s(specialization));
        m.put("departmentId", s(departmentId));
        return m;
    }

    @Override
    public void apply(Map<String, Object> v) {
        setFirstName((String) v.get("firstName"));
        setLastName((String) v.get("lastName"));
        setPhone((String) v.get("phone"));
        email = (String) v.get("email");
        specialization = (String) v.get("specialization");
        departmentId = (Integer) v.get("departmentId");
    }

    public String getEmail() { return email; }
    public String getSpecialization() { return specialization; }
    public Integer getDepartmentId() { return departmentId; }
}
