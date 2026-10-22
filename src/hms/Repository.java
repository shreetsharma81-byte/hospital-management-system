package hms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/** GENERICS: one class that stores ANY kind of Entity in memory and gives out ids (like AUTO_INCREMENT). */
public class Repository<T extends Entity> {

    private final List<T> items = new ArrayList<>();
    private int nextId = 1;

    public List<T> all() { return Collections.unmodifiableList(items); }

    public T add(T e) {
        e.setId(nextId++);
        items.add(e);
        return e;
    }

    public void replace(T e) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == e.getId()) { items.set(i, e); return; }
        }
    }

    public void remove(T e) { items.removeIf(x -> x.getId() == e.getId()); }

    public Optional<T> find(Integer id) {
        if (id == null) return Optional.empty();
        return items.stream().filter(x -> x.getId() == id).findFirst();
    }

    public boolean exists(Predicate<T> test) { return items.stream().anyMatch(test); }

    public int count() { return items.size(); }
}
