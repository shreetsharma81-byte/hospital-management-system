package hms;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.math.BigDecimal;

public class BillPanel extends CrudPanel<Bill> {

    private JLabel pending;   // created in extraButtons() (called by the parent constructor)

    public BillPanel() {
        super(new String[]{"Id", "Patient", "Date", "Total (Rs.)", "Status"},
            Hospital.get().bills, Bill::new,
            Field.lookup("patientId", "Patient", Hospital.get()::patientOptions),
            Field.date("billDate", "Bill date", true),
            Field.number("totalAmount", "Total amount", true),
            Field.choice("paymentStatus", "Payment status", true, "Pending", "Paid"));
    }

    @Override
    protected void validate(Bill c) {
        if (c.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Amount cannot be negative.");
    }

    @Override
    protected void extraButtons(JPanel bar) {
        JButton paid = new JButton("Mark as Paid");
        paid.addActionListener(e -> {
            Bill b = selected();
            if (b == null) return;
            b.setPaymentStatus("Paid");
            refresh();
        });
        JButton print = new JButton("Print");
        print.addActionListener(e -> {
            Bill b = selected();
            if (b != null) printBill(b);
        });
        pending = new JLabel();
        pending.setForeground(new Color(180, 60, 0));
        bar.add(paid);
        bar.add(print);
        bar.add(pending);
    }

    @Override
    protected void afterRefresh() {
        BigDecimal total = BigDecimal.ZERO;
        for (Bill b : repo.all()) {
            if (!"Paid".equals(b.getPaymentStatus())) total = total.add(b.getTotalAmount());
        }
        pending.setText("   Total pending: Rs. " + total);
    }

    private void printBill(Bill b) {
        JTextArea area = new JTextArea(
            "        CITY HOSPITAL - BILL\n\n"
          + "Bill no     : " + b.getId() + "\nPatient     : " + Hospital.get().patientName(b.getPatientId())
          + "\nDate        : " + b.getBillDate() + "\nAmount (Rs) : " + b.getTotalAmount()
          + "\nStatus      : " + b.getPaymentStatus() + "\n");
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        area.setEditable(false);
        JButton go = new JButton("Print");
        go.addActionListener(e -> {
            try { area.print(); } catch (PrinterException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.add(area, BorderLayout.CENTER);
        p.add(go, BorderLayout.SOUTH);
        JOptionPane.showMessageDialog(this, p, "Bill", JOptionPane.PLAIN_MESSAGE);
    }
}
