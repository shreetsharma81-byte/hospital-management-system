package hms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LabTest extends Entity {

    private String name;
    private BigDecimal cost;

    public LabTest() { }

    public LabTest(String name, BigDecimal cost) {
        this.name = name;
        this.cost = cost;
    }

    @Override
    public Object[] row() {
        return new Object[]{getId(), name, cost};
    }

    @Override
    public Map<String, String> values() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("name", s(name));
        m.put("cost", s(cost));
        return m;
    }

    @Override
    public void apply(Map<String, Object> v) {
        name = (String) v.get("name");
        cost = (BigDecimal) v.get("cost");
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
}
