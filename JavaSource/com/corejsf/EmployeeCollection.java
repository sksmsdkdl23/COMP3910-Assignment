package com.corejsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * EmployeeList class that implements.
 * @author Philip
 * @version 1.0
 */
public class EmployeeCollection implements EmployeeList {

    private Map<String, Employee> empMap;
    
    private Map<String, String> empCredential;
    
    private Employee currentEmployee;

    private Employee admin;
    
    private boolean editable;
    
    /**
     * EmployeeCollection constructor.
     */
    public EmployeeCollection() {
        empMap = new HashMap<>();
        empCredential = new HashMap<>();
        empCredential.put("admin", "pass");
        empCredential.put("aaa", "pass");
        empCredential.put("bbb", "pass");
        
        empMap.put("admin", new Employee("admin", 0, "admin"));
        admin = empMap.get("admin");
        empMap.put("aaa", new Employee("aaa", 0, "aaa"));
        empMap.put("bbb", new Employee("bbb", 0, "bbb"));
        editable = false;
    }
//    
//    public List<Employee> copyEmployees() {
//        List<Employee> clone = new ArrayList<>(getEmployees().size());
//        for (Employee e : getEmployees()) {
//            clone.add(e.);
//        }
//        return 
//    }
    @Override
    public List<Employee> getEmployees() {
        // TODO Auto-generated method stub
        return new ArrayList<Employee>(empMap.values());
    }

    @Override
    public Employee getEmployee(String name) {
        // TODO Auto-generated method stub
        for (Employee e : empMap.values()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * Get user.
     * @param userName user name
     * @return return employee.
     */
    public Employee getUser(String userName) {
        // TODO Auto-generated method stub
        for (Employee e : empMap.values()) {
            if (e.getUserName().equalsIgnoreCase(userName)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getLoginCombos() {
        // TODO Auto-generated method stub
        return empCredential;
    }

    @Override
    public Employee getCurrentEmployee() {
        // TODO Auto-generated method stub
        return currentEmployee;
    }

    @Override
    public Employee getAdministrator() {
        // TODO Auto-generated method stub
        return admin;
    }

    @Override
    public boolean verifyUser(Credentials credential) {
        // TODO Auto-generated method stub
        if (credential.getPassword()
                .equals(empCredential.get(credential.getUserName()))) {
            return true;
        }
        return false;
    }

    @Override
    public String logout(Employee employee) {
        // TODO Auto-generated method stub
        currentEmployee = null;
        return "logout";
    }

    @Override
    public void deleteEmployee(Employee userToDelete) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addEmployee(Employee newEmployee) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Admin.
     * @param emp employee
     * @return true or false.
     */
    public boolean adminPriviledge(Employee emp) {
        if (emp == getAdministrator()) {
            System.out.println("U R ADMIN");
            return true;
        }
        System.out.println("U R NOT An ADMIN");
        return false;
    }
    
    /**
     * Set current employee.
     * @param currentEmployee the currentEmployee to set
     */
    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }
    
    /**
     * Checks if the field is editable.
     * @return editable
     *              true if the field(s) is editable; otherwise false
     */
    public boolean getEditable() { 
        return editable; 
    }
    
    /**
     * Sets editable to true or false.
     * @param newValue
     *              true or false.
     * 
     */
    public void setEditable(boolean newValue) { 
        editable = newValue;
    }
    
    /**
     * Set editable to true.
     */
    public void edit() {
        setEditable(true);
        System.out.println(editable);
    }
    
    /**
     * Set inputText fields not editable.
     */
    public void save() {
        setEditable(false);
        System.out.println(editable);
    }
}
