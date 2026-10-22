package hms;

public class DepartmentPanel extends CrudPanel<Department> {
    public DepartmentPanel() {
        super(new String[]{"Id", "Department", "Location", "Phone"},
            Hospital.get().departments, Department::new,
            Field.text("name", "Department name", true),
            Field.text("location", "Location", false),
            Field.text("phone", "Phone", false));
    }

    /** Same job as UNIQUE in SQL. */
    @Override
    protected void validate(Department c) {
        if (repo.exists(d -> d.getId() != c.getId() && d.getName().equalsIgnoreCase(c.getName()))) {
            throw new IllegalArgumentException("A department with this name already exists.");
        }
    }

    @Override
    protected void checkDelete(Department dep) {
        int id = dep.getId();
        if (Hospital.get().doctors.exists(d -> d.getDepartmentId() == id)) {
            throw new IllegalStateException("Doctors still work in this department.");
        }
    }
}
