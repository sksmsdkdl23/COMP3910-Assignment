package com.corejsf;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

/**
 * Login Bean.
 * @author Philip
 * @version 1.0
 */
@Named
@ApplicationScoped
public class Login implements Serializable {
    
    /**
     * EmployeeCollection object.
     */
    @Inject
    private EmployeeCollection empList;
    
    /**
     * User name.
     */
    private String userName;
    
    /**
     * Password.
     */
    private String password;

    /**
     * Get user name.
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set user name.
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Login function.
     * @return success or fail
     */
    public String login() {
        Credentials cred = new Credentials();
        cred.setUserName(userName);
        cred.setPassword(password);
        
        if (empList.verifyUser(cred)) {
            empList.setCurrentEmployee(empList.getUser(cred.getUserName()));
            return "success";
        }
        return "fail";
    }
    
    /**
     * Login function.
     * @return logout
     */
    public String logout() {
        return empList.logout(empList.getCurrentEmployee());
    }
    
    /**
     * Get logged user.
     * @return the loggedUser
     */
    public Employee getLoggedUser() {
        return empList.getCurrentEmployee();
    }

    /**
     * Set logged user.
     * @param loggedUser the loggedUser to set
     */
    public void setLoggedUser(Employee loggedUser) {
        empList.setCurrentEmployee(loggedUser);
    }

    /**
     * Get empList.
     * @return the empList
     */
    public EmployeeCollection getEmpList() {
        return empList;
    }

    /**
     * Set empList.
     * @param empList the empList to set
     */
    public void setEmpList(EmployeeCollection empList) {
        this.empList = empList;
    }
    
    /**
     * Navigation.
     * @return editUser string
     */
    public String editUser() {
        return "editUser";
    }
    
    /**
     * Returns string.
     * @return back string
     */
    public String back() {
        return "back";
    }
}
