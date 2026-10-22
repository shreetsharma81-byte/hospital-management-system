package hms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * ABSTRACT + GENERIC parent of every table screen. It contains ALL the logic for
 * list / search / add / edit / delete for ANY entity type T.
 * Subclasses only say which repository, which columns and which form fields to use,
 * and may override the hooks validate(), checkDelete(), extraButtons(), highlight()
 * (POLYMORPHISM + INHERITANCE).
 */
public abstract class CrudPanel<T extends Entity> extends JPanel implements Refreshable {

    protected final Repository<T> repo;
    private final Supplier<T> factory;
    private final List<Field> fields;

    private final DefaultTableModel model;
    protected final JTable grid;
    private final TableRowSorter<DefaultTableModel> sorter;

    protected CrudPanel(String[] columns, Repository<T> repo, Supplier<T> factory, Field... fields) {
        this.repo = repo;
        this.factory = factory;
        this.fields = List.of(fields);

        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        grid = new JTable(model);
        sorter = new TableRowSorter<>(model);

        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        grid.setRowSorter(sorter);
        grid.setRowHeight(26);
        grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        grid.getTableHeader().setReorderingAllowed(false);
        grid.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    Integer id = (Integer) model.getValueAt(t.convertRowIndexToModel(row), 0);
                    boolean hot = repo.find(id).map(CrudPanel.this::highlight).orElse(false);
                    c.setBackground(hot ? new Color(255, 213, 153) : Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");
        add.addActionListener(e -> openForm(null));
        edit.addActionListener(e -> { T t = selected(); if (t != null) openForm(t); });
        del.addActionListener(e -> delete());
        bar.add(add);
        bar.add(edit);
        bar.add(del);
        extraButtons(bar);                               // hook

        JTextField search = new JTextField(16);
        search.getDocument().addDocumentListener(new DocumentListener() {
            private void apply() {
                String s = search.getText().trim();
                sorter.setRowFilter(s.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(s)));
            }
            public void insertUpdate(DocumentEvent e) { apply(); }
            public void removeUpdate(DocumentEvent e) { apply(); }
            public void changedUpdate(DocumentEvent e) { apply(); }
        });
        bar.add(new JLabel("   Search:"));
        bar.add(search);

        add(bar, BorderLayout.NORTH);
        add(new JScrollPane(grid), BorderLayout.CENTER);
    }

    // ---------------------------------------------------------- hooks (override in subclasses)
    protected void extraButtons(JPanel bar) { }
    protected boolean highlight(T entity) { return false; }
    protected void afterRefresh() { }
    /** Business rules (replace the database constraints). Throw IllegalArgumentException to refuse. */
    protected void validate(T candidate) { }
    /** Referential integrity (replaces foreign keys). Throw IllegalStateException to refuse. */
    protected void checkDelete(T entity) { }

    // ---------------------------------------------------------- show data
    @Override
    public void refresh() {
        model.setRowCount(0);
        for (T e : repo.all()) model.addRow(e.row());
        afterRefresh();
    }

    /** The entity of the highlighted row, or null (after asking the user to pick a row). */
    protected T selected() {
        int viewRow = grid.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row first.");
            return null;
        }
        Integer id = (Integer) model.getValueAt(grid.convertRowIndexToModel(viewRow), 0);
        return repo.find(id).orElse(null);
    }

    protected void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Not allowed", JOptionPane.WARNING_MESSAGE);
    }

    // ---------------------------------------------------------- delete
    private void delete() {
        T e = selected();
        if (e == null) return;
        if (JOptionPane.showConfirmDialog(this, "Delete this record permanently?", "Confirm delete",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            checkDelete(e);
            repo.remove(e);
            refresh();
        } catch (IllegalStateException ex) {
            warn(ex.getMessage());
        }
    }

    // ---------------------------------------------------------- add / edit form
    private void openForm(T existing) {
        Map<String, String> old = existing == null ? Map.of() : existing.values();
        Map<Field, JComponent> inputs = new LinkedHashMap<>();
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        for (Field f : fields) {
            JComponent box = buildInput(f, old.getOrDefault(f.getKey(), ""));
            inputs.put(f, box);
            form.add(new JLabel(labelOf(f)));
            form.add(box);
        }

        while (true) {
            int ok = JOptionPane.showConfirmDialog(this, form, existing == null ? "Add record" : "Edit record",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) return;
            try {
                Map<String, Object> values = collect(inputs);
                T candidate = factory.get();
                if (existing != null) {
                    candidate.setId(existing.getId());
                    candidate.copyHiddenFrom(existing);
                }
                candidate.apply(values);
                validate(candidate);                       // business rules
                if (existing == null) repo.add(candidate); else repo.replace(candidate);
                refresh();
                return;
            } catch (IllegalArgumentException ex) {
                warn(ex.getMessage());                     // form stays filled so the user can fix it
            }
        }
    }

    private String labelOf(Field f) {
        String s = f.getLabel() + (f.isRequired() ? " *" : "");
        if (f.getType() == Field.Type.DATE) s += "  (YYYY-MM-DD)";
        if (f.getType() == Field.Type.TIME) s += "  (HH:MM)";
        return s;
    }

    private JComponent buildInput(Field f, String old) {
        switch (f.getType()) {
            case CHOICE: {
                JComboBox<String> box = new JComboBox<>();
                if (!f.isRequired()) box.addItem("");
                for (String c : f.getChoices()) box.addItem(c);
                box.setSelectedItem(old);
                return box;
            }
            case LOOKUP: {
                JComboBox<Option> box = new JComboBox<>();
                for (Option o : f.getOptions()) {
                    box.addItem(o);
                    if (String.valueOf(o.id()).equals(old)) box.setSelectedItem(o);
                }
                return box;
            }
            default:
                return new JTextField(old, 20);
        }
    }

    /** Reads every input box, checks it, and converts it to the right Java type. */
    private Map<String, Object> collect(Map<Field, JComponent> inputs) {
        Map<String, Object> values = new LinkedHashMap<>();
        for (Map.Entry<Field, JComponent> en : inputs.entrySet()) {
            Field f = en.getKey();
            JComponent c = en.getValue();
            String name = f.getLabel();

            if (f.getType() == Field.Type.LOOKUP) {
                Option o = (Option) ((JComboBox<?>) c).getSelectedItem();
                if (o == null) throw new IllegalArgumentException(name + " is required");
                values.put(f.getKey(), o.id());
                continue;
            }

            String text = (c instanceof JComboBox)
                    ? String.valueOf(((JComboBox<?>) c).getSelectedItem()).trim()
                    : ((JTextField) c).getText().trim();
            if (text.isEmpty() || text.equals("null")) {
                if (f.isRequired()) throw new IllegalArgumentException(name + " is required");
                values.put(f.getKey(), null);
                continue;
            }
            try {
                Object v;
                switch (f.getType()) {
                    case NUMBER:  v = new BigDecimal(text); break;
                    case INTEGER: v = Integer.valueOf(text); break;
                    case DATE:    v = LocalDate.parse(text); break;
                    case TIME:    v = LocalTime.parse(text); break;
                    default:      v = text;
                }
                values.put(f.getKey(), v);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(name + " must be a valid number");
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException(name + " has a wrong format");
            }
        }
        return values;
    }
}
