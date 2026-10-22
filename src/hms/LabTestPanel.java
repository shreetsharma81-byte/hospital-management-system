package hms;

import java.math.BigDecimal;

public class LabTestPanel extends CrudPanel<LabTest> {
    public LabTestPanel() {
        super(new String[]{"Id", "Test", "Cost"},
            Hospital.get().labTests, LabTest::new,
            Field.text("name", "Test name", true),
            Field.number("cost", "Cost", true));
    }

    @Override
    protected void validate(LabTest c) {
        if (c.getCost().compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Cost cannot be negative.");
        if (repo.exists(t -> t.getId() != c.getId() && t.getName().equalsIgnoreCase(c.getName()))) {
            throw new IllegalArgumentException("This lab test already exists.");
        }
    }

    @Override
    protected void checkDelete(LabTest t) {
        int id = t.getId();
        if (Hospital.get().testResults.exists(r -> r.getLabTestId() == id)) {
            throw new IllegalStateException("Test results exist for this lab test.");
        }
    }
}
