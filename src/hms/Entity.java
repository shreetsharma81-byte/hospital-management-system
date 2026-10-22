package hms;

import java.util.Map;

/**
 * ABSTRACT parent of every record in the hospital (Patient, Doctor, Bill ...).
 * It owns the id (ENCAPSULATION) and declares what every record must be able to do.
 */
public abstract class Entity {

    private int id;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    /** One row of the table shown on screen (first element must be the id). */
    public abstract Object[] row();

    /** Current values as text, used to pre-fill the Edit form. Keys = field keys. */
    public abstract Map<String, String> values();

    /** Copies the validated values from the form into this object. */
    public abstract void apply(Map<String, Object> v);

    /** Hook: lets an entity keep data that is not on the form (e.g. the medicines of a prescription). */
    public void copyHiddenFrom(Entity old) { }

    protected static String s(Object o) { return o == null ? "" : String.valueOf(o); }

    protected static Hospital h() { return Hospital.get(); }
}
