package comp5017.cw1.pkg2023;

public class StaffHash implements IStaffDB {

    // The hash table
    private Employee[] table;
    // size of the table
    private int size;
    // the initial capacity of the hash table
    private static final int initialCapacity = 11;
    // load factor threshold
    private static final double loadFactorThreshold = 0.5;

    public StaffHash() {
        System.out.println("Hash Table");
        // Initialise the hash table with the initial capacity
        this.table = new Employee[initialCapacity];
        // Set the initial size of the table to 0
        this.size = 0;
    }

    @Override
    public void clearDB() {
        // Check that the table is not null
        assert table != null : "Hash table must not be null";
        for (int i = 0; i < table.length; i++) {
            // Clear all entries in the hash table
            table[i] = null;
            // check table is empty
            isEmpty();
        }
        // Reset the size to 0
        size = 0;
    }

    @Override
    public boolean containsName(String name) {
        // Check for valid input
        assert name != null && !name.isEmpty() : "Employee name must not be empty";
        // Calculate the initial hash index for the given name
        int index = hash(name);
        // Initialise an attempt counter
        int attempt = 0;
        while (table[index] != null) {
            if (!table[index].isDeleted() && table[index].getName().equals(name)) {
                // Log information about the operation
                logInfo(table[index], index, attempt);
                // Employee with the given name found
                return true;
            }
            attempt++;
            // Move to the next slot using probe
            index = probe(index, attempt + 1);
        }
        // Log information about the operation
        logInfo(null, index, attempt);
        // Employee with the given name not found
        return false;
    }

    public int hash(String name) {
        int hash = 0;
        for (int i = 0; i < name.length(); i++) {
            // Multiply the current hash by 31 and add the ASCII value of each character in the name
            hash = (hash * 31 + name.charAt(i)) % table.length;
        }
        // Ensure the computed hash value is within the bounds of the hash table
        return (hash + table.length) % table.length;
    }


    @Override
    public Employee get(String name) {
        // Check for valid input
        assert name != null && !name.isEmpty() : "Employee cannot be empty";
        // Calculate the initial hash index for the given name
        int index = hash(name);
        // Initialise an attempt counter
        int attempt = 0;
        // Continue until an empty slot is found
        while (table[index] != null) {
            if (!table[index].isDeleted() && table[index].getName().equals(name)) {
                // Log information about the operation
                logInfo(table[index], index, attempt);
                // Return the found employee
                return table[index];
            }
            attempt++;
            // Move to the next slot using probing
            index = probe(index, attempt + 1);
        }
        // Log information about the operation
        logInfo(table[index], index, attempt);
        // Employee with the given name not found
        return null;
    }

    public int size() {
        // Initialise a counter to keep track of the number of active employees
        int count = 0;
        for (Employee employee : table) {
            if (employee != null && !employee.isDeleted()) {
                // Increment the count for each active employee found
                count++;
            }
        }
        // Return the total count of active employees in the database
        return count;
    }

    public boolean isEmpty() {
        for (Employee employee : table) {
            if (employee != null && !employee.isDeleted()) {
                // If an active employee is found, the database is not empty
                return false;
            }
        }
        // If no active employees are found, the database is considered empty
        return true;
    }

    @Override
    public Employee put(Employee employee) {
        // Check for valid input
        assert employee.getName() != null && !employee.getName().isEmpty() : "The Employee cannot be empty or null";

        double loadFactor = (double) (size) / table.length;

        if (loadFactor > loadFactorThreshold) {
            // If the load factor exceeds the threshold, trigger resizing
            resize();
        }

        int index = hash(employee.getName());
        int attempt = 0;
        int maxSize = table.length;

        while (attempt < maxSize) {
            if (isEmpty()) {
                table[hash(employee.getName())] = employee;
                size++;
                // If the database is empty, add the employee and return null
                return null;
            }
            if (table[index] == null || table[index].isDeleted()) {
                table[index] = employee;
                size++;
                logInfo(employee, index, attempt);
                // If an empty slot is found, add the employee and return null
                return null;
            } else if (table[index].getName().equals(employee.getName())) {
                logInfo(employee, index, attempt);
                // If an employee with the same name exists, return that employee
                return table[index];
            } else {
                attempt++;
                // Move to the next slot using probing
                index = probe(index, attempt + 1);
            }
        }
        logInfo(employee, index, attempt);
        // If the method reaches this point, it means no suitable slot was found
        return null;
    }

    private void resize() {
        // Create a new table with double the size
        Employee[] tableTwo = new Employee[table.length * 2];

        for (Employee employee : table) {
            if (employee != null && !employee.isDeleted()) {
                int index = hash(employee.getName());
                int attempt = 0;
                while (tableTwo[index] != null) {
                    attempt++;
                    // Move to the next slot using probing
                    index = probe(index, attempt + 1);
                }
                // Place the employee in the new table
                tableTwo[index] = employee;
            }
        }
        // Log a message indicating the resizing is done
        System.out.println("Resizing Complete");
        // Update the reference to the new table
        table = tableTwo;
    }

    private int probe(int index, int attempt) {
        // Calculate the next index using probing
        return (index + (attempt + 1) * (attempt + 1)) % table.length;
    }

    @Override
    public Employee remove(String name) {
        // Check for valid input
        assert name != null && !name.isEmpty() : "The Employee name cannot be empty or null";

        // Calculate the initial hash index for the given name
        int index = hash(name);
        // Initialize an attempt counter
        int attempt = 0;

        // Continue until an empty slot or the end of the table is reached
        while (table[index] != null && attempt < table.length) {
            if (!table[index].isDeleted() && table[index].getName().equals(name)) {
                // Store the employee to be removed
                Employee removedEmployee = table[index];
                // Mark the employee as deleted
                table[index].markAsDeleted();
                // Decrement the size of the database
                size--;
                // Log information about the operation
                logInfo(removedEmployee, index, attempt);
                // Return the removed employee
                return removedEmployee;
            } else {
                attempt++;
                // Move to the next slot using probing
                index = probe(index, attempt + 1);
            }
        }
        // Log information about the operation
        logInfo(null, index, attempt);
        // Employee with the given name not found
        return null;
    }

    private void logInfo(Employee employee, int hashValue, int index) {
        // Log the employee's name or "Not Found"
        System.out.println("Employee: " + (employee != null ? employee.getName() : "Not Found"));
        // Log the hash value
        System.out.println("Hash Value: " + hashValue);
        // Log the number of buckets visited
        System.out.println("Buckets Visited: " + index);
        // Log the current load factor
        System.out.println("Load Factor: " + ((double) size / table.length));
    }

    public void bubbleSortingAlgo() {
        // Get the number of active employees in the database
        int n = size();
        // Create an array to hold the employees for sorting
        Employee[] employees = new Employee[n];
        // Initialise an index counter
        int index = 0;

        // Populate the employees array with active employees from the table
        for (Employee employee : table) {
            if (employee != null && !employee.isDeleted()) {
                // Copy active employees to the employees array
                employees[index] = employee;
                index++;
            }
        }

        // Bubble Sort algorithm for sorting employees by name
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (employees[j].getName().compareTo(employees[j + 1].getName()) > 0) {
                    // Swap employees[j] and employees[j+1] to sort them in ascending order
                    Employee temp = employees[j];
                    employees[j] = employees[j + 1];
                    employees[j + 1] = temp;
                }
            }
        }

        // Update the hash table with the sorted employees
        index = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isDeleted()) {
                // Replace the unsorted employee with the sorted employee
                table[i] = employees[index];
                index++;
            }
        }
    }


    @Override
    public void displayDB() {
        // Send the table of employees to the bubble sorter to sort them alphabetically
        bubbleSortingAlgo();

        // Display the sorted employees with their affiliations
        for (Employee employee : table) {
            if (employee != null && !employee.isDeleted()) {
                // Print the employee's name and affiliation
                System.out.println(employee.getName() + ": " + employee.getAffiliation());
            }
        }
    }
}