package hms;

import java.util.List;
import java.util.function.Supplier;

/** Describes ONE input box of a form. Created with the static factory methods below. */
public class Field {

    public enum Type { TEXT, NUMBER, INTEGER, DATE, TIME, CHOICE, LOOKUP }

    private final String key;        // property name, e.g. "firstName"
    private final String label;
    private final Type type;
    private final boolean required;
    private String[] choices = new String[0];
    private Supplier<List<Option>> options;

    private Field(String key, String label, Type type, boolean required) {
        this.key = key;
        this.label = label;
        this.type = type;
        this.required = required;
    }

    public static Field text(String key, String label, boolean req) { return new Field(key, label, Type.TEXT, req); }
    public static Field number(String key, String label, boolean req) { return new Field(key, label, Type.NUMBER, req); }
    public static Field integer(String key, String label, boolean req) { return new Field(key, label, Type.INTEGER, req); }
    public static Field date(String key, String label, boolean req) { return new Field(key, label, Type.DATE, req); }
    public static Field time(String key, String label, boolean req) { return new Field(key, label, Type.TIME, req); }

    public static Field choice(String key, String label, boolean req, String... choices) {
        Field f = new Field(key, label, Type.CHOICE, req);
        f.choices = choices;
        return f;
    }

    /** Foreign key: a drop-down of names, filled fresh every time the form opens. */
    public static Field lookup(String key, String label, Supplier<List<Option>> options) {
        Field f = new Field(key, label, Type.LOOKUP, true);
        f.options = options;
        return f;
    }

    public String getKey() { return key; }
    public String getLabel() { return label; }
    public Type getType() { return type; }
    public boolean isRequired() { return required; }
    public String[] getChoices() { return choices; }
    public List<Option> getOptions() { return options.get(); }
}
