package com.corejsf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * EmployeeList class that implements.
 * @author Philip
 * @version 1.0
 */
public class EmployeeCollection implements EmployeeList {

    /**
     * Three.
     */
    public static final int THREE = 3;
    
    /**
     * Four.
     */
    public static final int FOUR = 4;
    
    /** 
     * Link to SQL connection.
     */
    @Resource(mappedName = "java:jboss/datasources/inventory")
    
    /**
     * DataSource.
     */
    private static DataSource datasource;
    
    /**
     * Employee List.
     */
    private Map<String, Employee> empMap;
    
    /**
     * Employee Credentials.
     */
    private Map<String, String> empCredential;
    
    /**
     * Current Employee.
     */
    private Employee currentEmployee;

    /**
     * Administrator.
     */
    private Employee admin;
    
    /**
     * Employee ID.
     */
    private int employeeId;
    
    /**
     * Gets user data.
     */
    @PostConstruct
    public void init() {
        employeeId = 0;
        empCredential = new HashMap<>();
        empMap = new HashMap<>();
        empCredential.clear();
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = datasource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Employee");
            try {
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    int employeeNum = result.getInt("EmployeeNum");
                    String employeeName = result.getString("EmployeeName");
                    String empUsername = result.getString("UserName");
                    System.out.println(empUsername);
                    String employeePassword = result.getString("Pass");
                    System.out.println(employeePassword);
                    Employee employee = new Employee(
                            employeeName, employeeNum, empUsername);
                    //Retrieves from the SQL database and puts a credential into
                    //the credential container
                    empCredential.put(empUsername, employeePassword);
                    //Retrieves from the SQL database and puts an employee into
                    //the employee storage container.
                    empMap.put(empUsername, employee);
                    admin = empMap.get("admin");
                }
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqlex) {
                    sqlex.printStackTrace();
                }
            }
        }
    }

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
        empCredential.remove(userToDelete.getUserName());
        empMap.remove(userToDelete.getUserName());
        PreparedStatement stmt = null;
        Connection connection = null;
        
        try {
            try {
                connection = datasource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM Employee WHERE UserName LIKE ?");
                    stmt.setString(1, userToDelete.getUserName());
                    stmt.executeUpdate();
                    
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
                
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException sqlex) {
            System.out.println("Error in EmployeeCollection.deleteEmployee()");
            System.out.println(stmt.toString() 
                    + " was the statment being sent.");
            sqlex.printStackTrace();
        }
        
    }

    @Override
    public void addEmployee(Employee newEmployee) {
        newEmployee.setEmpNumber(employeeId);
        empMap.put(newEmployee.getUserName(), newEmployee);
        empCredential.put(newEmployee.getUserName(), "pass" + employeeId);
        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = datasource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO Employee VALUES (?,?,?,?)");
                    stmt.setInt(1, newEmployee.getEmpNumber());
                    stmt.setString(2, "Default");
                    stmt.setString(THREE, "Default" + employeeId);
                    stmt.setString(FOUR, "pass" + employeeId);
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                } 
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException sqlex) {
            System.out.println("Error in EmployeeCollection.addEmployee()");
            System.out.println(stmt.toString() 
                    + " was the statment being sent.");
            sqlex.printStackTrace();
        }
    }
    
    /**
     * Admin.
     * @param emp employee
     * @return true or false.
     */
    public boolean adminPriviledge(Employee emp) {
        if (emp == getAdministrator()) {
            return true;
        }
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
     * Get Employee Map.
     * @return the empMap
     */
    public Map<String, Employee> getEmpMap() {
        return empMap;
    }
    /**
     * Set employee map.
     * @param empMap the empMap to set
     */
    public void setEmpMap(Map<String, Employee> empMap) {
        this.empMap = empMap;
    }
    
    /**
     * Update Users table in inventory database.
     * @param employee
     *              employee Object
     */
    public void merge(Employee employee) {
        PreparedStatement stmt = null;
        Connection connection = null;
        
        try {
            try {
                connection = datasource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE Employee SET " 
                            + "UserName = ?, EmployeeName = ? "
                            + "WHERE EmployeeNum = ?");
                    stmt.setString(1, employee.getUserName());
                    stmt.setString(2, employee.getName());
                    stmt.setInt(THREE, employee.getEmpNumber());
                    stmt.executeUpdate();
                    
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
                
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException sqlex) {
            System.out.println("Error in EmployeeCollection.merge()");
            System.out.println(stmt.toString() 
                    + " was the statment being sent.");
            sqlex.printStackTrace();
        }
    }

    /**
     * Get id.
     * @return the employeeId
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Set id.
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    /**
     * Find employee.
     * @param id    
     *          employee's id;
     * @return employee
     */
    public static Employee find(int id) {
        PreparedStatement stmt = null;
        Connection connection = null;
        
        try {
            try {
                connection = datasource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "SELECT * FROM Employee where EmployeeNum = ?");
                    stmt.setInt(1, id);
                    ResultSet result = stmt.executeQuery();
                    if (result.next()) {
                        return new Employee(result.getString("EmployeeName"),
                                result.getInt("EmployeeNum"),
                                result.getString("UserName"));
                    } else {
                        return null;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in find " + id);
            ex.printStackTrace();
            return null;
        }
    }
    
}
