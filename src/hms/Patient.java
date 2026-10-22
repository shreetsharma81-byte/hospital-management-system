package hms;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Patient extends Person {

    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String address;

    public Patient() { }

    public Patient(String first, String last, LocalDate dob, String gender,
                   String bloodGroup, String phone, String address) {
        setFirstName(first);
        setLastName(last);
        setPhone(phone);
        this.dateOfBirth = dob;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.address = address;
    }

    @Override
    public String getFullName() { return getFirstName() + " " + getLastName(); }

    @Override
    public Object[] row() {
        return new Object[]{getId(), getFullName(), dateOfBirth, gender, bloodGroup, getPhone(), address};
    }

    @Override
    public Map<String, String> values() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("firstName", getFirstName());
        m.put("lastName", getLastName());
        m.put("dateOfBirth", s(dateOfBirth));
        m.put("gender", gender);
        m.put("bloodGroup", s(bloodGroup));
        m.put("phone", getPhone());
        m.put("address", s(address));
        return m;
    }

    @Override
    public void apply(Map<String, Object> v) {
        setFirstName((String) v.get("firstName"));
        setLastName((String) v.get("lastName"));
        setPhone((String) v.get("phone"));
        dateOfBirth = (LocalDate) v.get("dateOfBirth");
        gender = (String) v.get("gender");
        bloodGroup = (String) v.get("bloodGroup");
        address = (String) v.get("address");
    }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getBloodGroup() { return bloodGroup; }
    public String getAddress() { return address; }
}
