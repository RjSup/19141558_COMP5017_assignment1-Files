package comp5017.cw1.pkg2023;

public class StaffBST implements IStaffDB {

    private Node root;

    public StaffBST() {
        System.out.println("Binary Search Tree");
    }

    private static class Node {
        Employee employee;
        Node left;
        Node right;

        public Node(Employee employee) {
            this.employee = employee;
            left = null;
            right = null;
        }
    }

    @Override
    public void clearDB() {
        root = null;
    }

    @Override
    public boolean containsName(String name) {
        return search(root, name) != null;
    }

    private Node search(Node node, String name) {
        if(node == null){
            return null;
        }

        int compareResult = name.compareTo(node.employee.getName());

        if(compareResult == 0){
            return node;
        }
        else if(compareResult < 0){
            return search(node.left, name);
        } else {
            return search(node.right, name);
        }
    }

    @Override
    public Employee get(String name) {
        Node result = search(root, name);
        return result != null ? result.employee : null;
    }

    @Override
    public int size() {
        return size(root);
    }

     private int size(Node node) {
         if(node == null) {
             return 0;
         }
         return 1 + size(node.left) + size(node.right);
    }


    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public Employee put(Employee employee) {
        Node newNode = new Node(employee);
        if(root == null){
            root = newNode;
            return null;
        }
        return insert(root, newNode);
    }

    private Employee insert(Node currNode, Node newNode){
        int compareResult = newNode.employee.getName().compareTo(currNode.employee.getName());

        if(compareResult == 0) {
            Employee prevEmployee = currNode.employee;
            currNode.employee = newNode.employee;
            return prevEmployee;
        }
        else if(compareResult< 0) {
            if(currNode.left == null) {
                currNode.left = newNode;
                return null;
            } else {
                return insert(currNode.left, newNode);
            }
        }  else {
            if(currNode.right == null) {
                currNode.right = newNode;
                return null;
            } else {
                return insert(currNode.right, newNode);
            }
        }
    }

    private static class EmployeeWrapper {
        Employee employee;
    }
    @Override
    public Employee remove(String name) {
        EmployeeWrapper removedEmployee = new EmployeeWrapper();
        root = remove(root, name, removedEmployee);
        return removedEmployee.employee;
    }

    private Node remove(Node node, String name, EmployeeWrapper removedEmployee) {
        if(node == null){
            return null;
        }

        int compareResult = name.compareTo(node.employee.getName());

        if(compareResult < 0) {
            node.left = remove(node.left, name, removedEmployee);
        } else if(compareResult > 0) {
            node.right = remove(node.right, name, removedEmployee);
        } else {
            if(node.left == null) {
                return node.right;
            } else if(node.right == null){
                return node.left;
            }
            node.employee = minValueNode(node.right).employee;
            node.right = remove(node.right, node.employee.getName(), removedEmployee);
        }
        return node;
    }

    private Node minValueNode(Node node){
        Node current = node;
        while(current.left != null){
            current = current.left;
        }
        return current;
    }


    private void displayInOrder(Node node){
        if(node != null){
            displayInOrder(node.left);
            System.out.println(node.employee.getName());
            displayInOrder(node.right);
        }
    }

    @Override
    public void displayDB() {
        displayInOrder(root);
    }

}
