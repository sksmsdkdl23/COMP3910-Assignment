package com.corejsf;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Login Bean.
 * @author Philip
 * @version 1.0
 */
@Named
@ApplicationScoped
public class Login implements Serializable {
    
    /**
     * FIVE.
     */
    public static final int FIVE = 5;
    
    /**
     * TimesheetList object.
     */
    @Inject
    private TimesheetList timesheetList;
    
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
     * Old userName.
     */
    private String oldUserName;
    
    /**
     * New timesheet.
     */
    private Timesheet newSheet;
    
    /**
     * Current Sheet.
     */
    private Timesheet viewSheet;

    /**
     * @return the newSheet
     */
    public Timesheet getNewSheet() {
        return newSheet;
    }

    /**
     * @param newSheet the newSheet to set
     */
    public void setNewSheet(Timesheet newSheet) {
        this.newSheet = newSheet;
    }

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
    
    /**
     * Ajax event.
     * @param event ajax Event
     */
    public void onRowEdit(RowEditEvent event) {
        Employee emp = (Employee) event.getObject();
        Map<String, Employee> tempEmp = empList.getEmpMap();
        Map<String, String> tempCred = empList.getLoginCombos();
        
        tempCred.remove(oldUserName);
        tempCred.remove(emp.getUserName());
        tempCred.put(emp.getUserName(), password);
        oldUserName = null;
    }
    
    /**
     * Ajax event.
     * @param event ajax Event
     */
    public void onRowEditInit(RowEditEvent event) {
        Employee oldEmp = (Employee) event.getObject();
        oldUserName = oldEmp.getUserName();
    }
    
    public String getPassword(Employee emp) {
        if (emp.getUserName() == null) {
            return "";
        }
        return empList.getLoginCombos().get(emp.getUserName());
    }
    
    public void newEmployee() {
        Employee newEmp = new Employee();
        empList.addEmployee(newEmp);
    }
    
    public void deleteEmployee(Employee emp) {
        empList.deleteEmployee(emp);
    }
    
    public String viewAll() {
        return "viewAll";
    }
    
    public List<Timesheet> getUserTimesheets() {
        Employee emp = empList.getCurrentEmployee();
        return timesheetList.getTimesheets(emp);
    }
    
    @PostConstruct
    public void sampleSheet() {
        Timesheet newSheet = new Timesheet();
        newSheet.setEmployee(empList.getAdministrator());
        timesheetList.addTimesheet(newSheet);
    }
    
    public String newTimesheet() {
        newSheet = new Timesheet();
        for (int i = 0; i < FIVE; i++) {
            newSheet.addRow();
        }
        newSheet.setEmployee(empList.getCurrentEmployee());
        
        return "newSheet";
    }
    
    public void addRow() {
        newSheet.addRow();
    }
    
    public void deleteRow(TimesheetRow row) {
        newSheet.deleteRow(row);
    }
    
    public String save() {
        timesheetList.addTimesheet(newSheet);
        return "back";
    }
    
    public String viewTimesheet(Timesheet currentSheet) {
        viewSheet = currentSheet;
        return "view";
    }

    /**
     * @return the viewSheet
     */
    public Timesheet getViewSheet() {
        return viewSheet;
    }

    /**
     * @param viewSheet the viewSheet to set
     */
    public void setViewSheet(Timesheet viewSheet) {
        this.viewSheet = viewSheet;
    }
    
    
}
