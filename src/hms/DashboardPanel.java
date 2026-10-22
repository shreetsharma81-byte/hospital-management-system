package hms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/** Home screen: summary cards and the list of upcoming (scheduled) appointments. */
public class DashboardPanel extends JPanel implements Refreshable {

    private final Map<String, Supplier<Object>> stats = new LinkedHashMap<>();
    private final Map<String, JLabel> values = new LinkedHashMap<>();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Date", "Time", "Patient", "Doctor", "Department", "Reason"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    public DashboardPanel() {
        Hospital h = Hospital.get();
        stats.put("Patients", h.patients::count);
        stats.put("Doctors", h.doctors::count);
        stats.put("Scheduled appointments",
                () -> h.appointments.all().stream().filter(a -> "Scheduled".equals(a.getStatus())).count());
        stats.put("Admitted now",
                () -> h.admissions.all().stream().filter(a -> "Admitted".equals(a.getStatus())).count());
        stats.put("Low-stock medicines",
                () -> h.medicines.all().stream().filter(m -> m.getStockQuantity() < 200).count());
        stats.put("Pending bills (Rs.)", () -> h.bills.all().stream()
                .filter(b -> !"Paid".equals(b.getPaymentStatus()))
                .map(Bill::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel cards = new JPanel(new GridLayout(1, 0, 12, 12));
        for (String title : stats.keySet()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 220, 225)), new EmptyBorder(14, 14, 14, 14)));
            JLabel value = new JLabel("-", SwingConstants.CENTER);
            value.setFont(value.getFont().deriveFont(Font.BOLD, 28f));
            value.setForeground(new Color(0, 105, 120));
            card.add(value, BorderLayout.CENTER);
            card.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.SOUTH);
            values.put(title, value);
            cards.add(card);
        }

        JTable grid = new JTable(model);
        grid.setRowHeight(26);
        JPanel lower = new JPanel(new BorderLayout(6, 6));
        lower.add(new JLabel("Upcoming (scheduled) appointments"), BorderLayout.NORTH);
        lower.add(new JScrollPane(grid), BorderLayout.CENTER);

        add(cards, BorderLayout.NORTH);
        add(lower, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        for (Map.Entry<String, Supplier<Object>> s : stats.entrySet()) {
            values.get(s.getKey()).setText(String.valueOf(s.getValue().get()));
        }
        model.setRowCount(0);
        Hospital h = Hospital.get();
        h.appointments.all().stream()
                .filter(a -> "Scheduled".equals(a.getStatus()))
                .sorted(Comparator.comparing(Appointment::getAppointmentDate)
                        .thenComparing(Appointment::getAppointmentTime))
                .forEach(a -> {
                    Doctor d = h.doctors.find(a.getDoctorId()).orElse(null);
                    model.addRow(new Object[]{a.getAppointmentDate(), a.getAppointmentTime(),
                            h.patientName(a.getPatientId()), h.doctorName(a.getDoctorId()),
                            d == null ? "?" : h.departmentName(d.getDepartmentId()), a.getReason()});
                });
    }
}
