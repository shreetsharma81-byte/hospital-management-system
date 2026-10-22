package hms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Department extends Entity {

    private String name;
    private String location;
    private String phone;

    public Department() { }

    public Department(String name, String location, String phone) {
        this.name = name;
        this.location = location;
        this.phone = phone;
    }

    @Override
    public Object[] row() {
        return new Object[]{getId(), name, location, phone};
    }

    @Override
    public Map<String, String> values() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("name", s(name));
        m.put("location", s(location));
        m.put("phone", s(phone));
        return m;
    }

    @Override
    public void apply(Map<String, Object> v) {
        name = (String) v.get("name");
        location = (String) v.get("location");
        phone = (String) v.get("phone");
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
