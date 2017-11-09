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
        empMap.put("aaa", new Employee("aaa", 1, "aaa"));
        empMap.put("bbb", new Employee("bbb", 2, "bbb"));
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
     * @return the empMap
     */
    public Map<String, Employee> getEmpMap() {
        return empMap;
    }
    /**
     * @param empMap the empMap to set
     */
    public void setEmpMap(Map<String, Employee> empMap) {
        this.empMap = empMap;
    }
    
    
}
