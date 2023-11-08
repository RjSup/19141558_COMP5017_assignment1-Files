package comp5017.cw1.pkg2023;

public class Employee implements IEmployee{

    private String name;
    private String affiliation;

    private boolean deleted;

    public Employee(String name, String affiliation) {
        this.name = name;
        this.affiliation = affiliation;
        this.deleted = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    @Override
    public String toString() {
        return name + ": " +affiliation;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void markAsDeleted() {
        this.deleted = true;
    }
}
