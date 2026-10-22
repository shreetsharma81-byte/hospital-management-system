package hms;

import javax.swing.*;
import java.awt.*;

/** Starts the program: one window, one tab per screen. No database, no server - just Java. */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::openWindow);
    }

    private static void openWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JFrame frame = new JFrame("City Hospital - Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1150, 680);
        frame.setLocationRelativeTo(null);

        JLabel banner = new JLabel("  City Hospital Management System", SwingConstants.LEFT);
        banner.setOpaque(true);
        banner.setBackground(new Color(0, 121, 140));
        banner.setForeground(Color.WHITE);
        banner.setFont(banner.getFont().deriveFont(Font.BOLD, 20f));
        banner.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.addTab("Dashboard", new DashboardPanel());
        tabs.addTab("Patients", new PatientPanel());
        tabs.addTab("Doctors", new DoctorPanel());
        tabs.addTab("Departments", new DepartmentPanel());
        tabs.addTab("Appointments", new AppointmentPanel());
        tabs.addTab("Admissions", new AdmissionPanel());
        tabs.addTab("Prescriptions", new PrescriptionPanel());
        tabs.addTab("Medicines", new MedicinePanel());
        tabs.addTab("Lab Tests", new LabTestPanel());
        tabs.addTab("Test Results", new TestResultPanel());
        tabs.addTab("Billing", new BillPanel());

        // POLYMORPHISM: every tab is handled through the same Refreshable interface
        tabs.addChangeListener(e -> {
            Component c = tabs.getSelectedComponent();
            if (c instanceof Refreshable r) r.refresh();
        });

        frame.add(banner, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);
        frame.setVisible(true);
        ((Refreshable) tabs.getSelectedComponent()).refresh();
    }
}
