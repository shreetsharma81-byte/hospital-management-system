package hms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Medicine extends Entity {

    private String name;
    private BigDecimal unitPrice;
    private Integer stockQuantity;

    public Medicine() { }

    public Medicine(String name, BigDecimal unitPrice, Integer stockQuantity) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
    }

    @Override
    public Object[] row() {
        return new Object[]{getId(), name, unitPrice, stockQuantity};
    }

    @Override
    public Map<String, String> values() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("name", s(name));
        m.put("unitPrice", s(unitPrice));
        m.put("stockQuantity", s(stockQuantity));
        return m;
    }

    @Override
    public void apply(Map<String, Object> v) {
        name = (String) v.get("name");
        unitPrice = (BigDecimal) v.get("unitPrice");
        stockQuantity = (Integer) v.get("stockQuantity");
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}
