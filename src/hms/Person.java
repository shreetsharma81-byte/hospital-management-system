package hms;

/** Common data of every human in the system. Patient and Doctor both extend this. */
public abstract class Person extends Entity {

    private String firstName;
    private String lastName;
    private String phone;

    /** POLYMORPHISM: each subclass decides how a person's full name is written. */
    public abstract String getFullName();

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
