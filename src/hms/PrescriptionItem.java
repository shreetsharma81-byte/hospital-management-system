package hms;

/** One medicine line on a prescription (the "junction table" row, as an object). */
public class PrescriptionItem {
    private final int medicineId;
    private final String dosage;
    private final String frequency;
    private final int durationDays;

    public PrescriptionItem(int medicineId, String dosage, String frequency, int durationDays) {
        this.medicineId = medicineId;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
    }

    public int getMedicineId() { return medicineId; }
    public String getDosage() { return dosage; }
    public String getFrequency() { return frequency; }
    public int getDurationDays() { return durationDays; }
}
