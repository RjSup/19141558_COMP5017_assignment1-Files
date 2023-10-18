package comp5017.cw1.pkg2023;

public class StaffHash implements IStaffDB {

    private Employee[] table; // The hash table
    private int size; // size of table

    private static final int initialCapacity = 16; // the initial capacity of the hash table
    private static final double loadFactorThreshold = 0.5; // load factor threshold

    public StaffHash() {
        this.table = new Employee[initialCapacity];
        this.size = 0;
    }

    @Override
    public void clearDB() {
        // for each employee of Employee object within the table
        for(Employee employee : table){
            // if the employee exists
            if(employee != null){
                // so print their details
                System.out.println(employee.getName() + ": " + employee.getAffiliation());
            }
        }
    }

    @Override
    public boolean containsName(String name) {
        // use index to find names in hash table
        int index = hash(name);
        // while a name exists
        while(table[index] != null) {
            // if the name at index of table equals the name then true
            if(table[index].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // research more hash methods
    public int hash(String name){
        int hash = 0;
        for(int i = 0; i < name.length(); i++) {
            hash = hash * hash % table.length;
        }
        return hash;
    }

    // get employee by name
    @Override
    public Employee get(String name) {
        // the index is the hash of the name
        int index = hash(name);
        // while a name exists
        while(table[index] != null){
            // if name exists return it
            if(table[index].getName().equals(name)){
                return table[index];
            }
        }
        return null;
    }

    @Override
    // current size of table
    public int size() {
        // to return the size
        return size;
    }

    // check if table is empty
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // add an employee into the table (make the collision handles quadratic probing)
    @Override
    public Employee put(Employee employee) {
        // calculate load factor
        double loadFactor = (double) (size) / table.length;

        // if load factor too big resize the table
        if(loadFactor > loadFactorThreshold) {
            // create a resize method
            resize();
        }

        // calculate index with hash
        int index = hash(employee.getName());
        // attempt
        int attempt = 0;
        // a max size
        int maxSize = table.length;

        //while the index is less than the max size
        while(attempt < maxSize) {
            // if there is no employee with name
            if(table[index] == nul){
                // add employee name
                table[index] = employee;
                // increase the size of table
                size++;
                return null;
                // if the employee name already exists
            } else if(table[index].getName().equals(employee.getName())){
                // return it
                return employee;
                // else employee not found and doesnt already exist or error = collision
            } else {
                //implement the probing here - probe method needed

            }
        }
        return null;
    }

    private void resize() {
        // create a new array
        Employee[] tableTwo = new Employee[table.length * 2];
        // allow the new array to be used
        for(Employee employee : table) {
            if( employee != null){

            }
        }
    }

    @Override
    public Employee remove(String name) {
        return null;
    }

    @Override
    public void displayDB() {

    }
}
