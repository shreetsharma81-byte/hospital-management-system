package hms;

/** One entry of a drop-down: the user sees a NAME, the program keeps the ID. */
public record Option(int id, String label) {
    @Override
    public String toString() { return label; }
}
