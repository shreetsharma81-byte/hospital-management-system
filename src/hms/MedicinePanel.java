package hms;

import java.math.BigDecimal;

public class MedicinePanel extends CrudPanel<Medicine> {
    public MedicinePanel() {
        super(new String[]{"Id", "Medicine", "Unit Price", "Stock"},
            Hospital.get().medicines, Medicine::new,
            Field.text("name", "Medicine name", true),
            Field.number("unitPrice", "Unit price", true),
            Field.integer("stockQuantity", "Stock quantity", true));
    }

    /** POLYMORPHISM: rows with stock below 200 are painted orange on this screen only. */
    @Override
    protected boolean highlight(Medicine m) { return m.getStockQuantity() < 200; }

    @Override
    protected void validate(Medicine c) {
        if (c.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Price cannot be negative.");
        if (c.getStockQuantity() < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        if (repo.exists(m -> m.getId() != c.getId() && m.getName().equalsIgnoreCase(c.getName()))) {
            throw new IllegalArgumentException("This medicine already exists.");
        }
    }

    @Override
    protected void checkDelete(Medicine m) {
        int id = m.getId();
        boolean used = Hospital.get().prescriptions.exists(
                p -> p.getItems().stream().anyMatch(i -> i.getMedicineId() == id));
        if (used) throw new IllegalStateException("This medicine is used in a prescription.");
    }
}
