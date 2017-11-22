package com.corejsf;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * TimesheetList class that implements TimesheetCollection interface.
 * 
 * @author Philip
 * @version 1.0
 */
public class TimesheetList implements TimesheetCollection {

    /**
     * Three. 
     */
    private static final int THREE = 3;
    /**
     * Four.
     */
    private static final int FOUR = 4;
    /**
     * Five.
     */
    private static final int FIVE = 5;
    /**
     * Six.
     */
    private static final int SIX = 6;
    /** 
     * Seven. 
     */
    private static final int SEVEN = 7;
    /** 
     * Eight.
     */
    private static final int EIGHT = 8;
    /** 
     * Nine.
     */
    private static final int NINE = 9;
    /** 
     * Ten.
     */
    private static final int TEN = 10;
    /** 
     * Eleven.
     */
    private static final int ELEVEN = 11;
    /** 
     * Twelve.
     */
    private static final int TWELVE = 12;
    
    /** 
     * Link to SQL connection.
     */
    @Resource(mappedName = "java:jboss/datasources/inventory")
    
    /**
     * DataSource.
     */
    private DataSource datasource;
    
    /**
     * Timesheets.
     */
    private List<Timesheet> timesheets = new ArrayList<>();
    
    @Override
    public List<Timesheet> getTimesheets() {
        // TODO Auto-generated method stub
        return timesheets;
    }

    @Override
    public List<Timesheet> getTimesheets(Employee e) {
        // TODO Auto-generated method stub
        List<Timesheet> empTimesheet = new ArrayList<Timesheet>();
        List<TimesheetRow> timesheetRow = new ArrayList<>();
        
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            
            try {
                connection = datasource.getConnection();
                try {
                    statement = connection.prepareStatement("SELECT * FROM " 
                            + "Timesheet WHERE EmployeeNum = ?");
                    statement.setInt(1, e.getEmpNumber());
                    ResultSet result = statement.executeQuery();
                    while (result.next()) {
                        int employeeNum = result.getInt("EmployeeNum");
                        Date endDate = result.getDate("EndWeek");
                        int weekNum = result.getInt("WeekNumber");
                        try {
                            statement = connection.prepareStatement("SELECT * "
                                    + "FROM TimesheetRow WHERE "
                                    + "EndWeek = ? AND EmployeeNum = ?");
                            statement.setDate(1, (java.sql.Date) endDate);
                            statement.setInt(2, e.getEmpNumber());
                            ResultSet resSet = statement.executeQuery();
                            while (resSet.next()) {
                                int project = resSet.getInt("ProjectID");
                                String workPackage = resSet.getString("Work"
                                        + "Package");
                                BigDecimal[] hours = 
                                      { resSet.getBigDecimal("SAT"),
                                        resSet.getBigDecimal("SUN"),
                                        resSet.getBigDecimal("MON"),
                                        resSet.getBigDecimal("TUE"),
                                        resSet.getBigDecimal("WED"),
                                        resSet.getBigDecimal("THU"),
                                        resSet.getBigDecimal("FRI") };
                                String notes = resSet.getString("Notes");
                                
                                timesheetRow.add(new TimesheetRow(
                                        project, workPackage, hours, notes));
                            }
                        } finally {
                            if (statement != null) {
                                statement.close();
                            }
                        }

                        Timesheet timesheet = new Timesheet(
                                EmployeeCollection.find(employeeNum), 
                                endDate, timesheetRow);
                        empTimesheet.add(timesheet);
                        
                    }
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
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
        return empTimesheet;
    }

    @Override
    public Timesheet getCurrentTimesheet(Employee e) {
        // TODO Auto-generated method stub
        Timesheet currentSheet = null;
        for (Timesheet sheet : timesheets) {
            if (sheet.getEmployee() == e) {
                if (currentSheet == null 
                        || sheet.getEndWeek()
                            .after(currentSheet.getEndWeek())) {
                    currentSheet = sheet;
                }
            }
        }
        return currentSheet;
    }

    @Override
    public String addTimesheet() {
        return "newSheet";
    }

    /**
     * Adds a new timesheet to timesheets.
     * @param sheet timesheet to add
     */
    public void addTimesheet(Timesheet sheet) {
        PreparedStatement statement = null;
        Connection connection = null;
        
        System.out.println(sheet.getEmployee().getEmpNumber());
        
        try {
            try {
                connection = datasource.getConnection();
                try {
                    timesheets.add(sheet);
                    if (!find(sheet.getEndWeek(), 
                            sheet.getEmployee().getEmpNumber())) {
                        statement = connection.prepareStatement(
                                "INSERT INTO Timesheet " 
                                    + "VALUES (?,?,?)");
                        statement.setInt(1, 
                                sheet.getEmployee().getEmpNumber());
                        
                        java.sql.Date date = new java.sql.Date(
                                sheet.getEndWeek().getTime());
                        statement.setDate(2, date);
                        statement.setInt(2 + 1, sheet.getWeekNumber());
                        
                        for (TimesheetRow row : sheet.getDetails()) {
                            addTimesheetRow(row, sheet.getEndWeek(), 
                                    sheet.getEmployee().getEmpNumber());
                        }
                        statement.executeUpdate();
                    } else {
                        for (TimesheetRow row : sheet.getDetails()) {
                            addTimesheetRow(row, sheet.getEndWeek(), 
                                    sheet.getEmployee().getEmpNumber());
                        }
                    }
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException sqlex) {
            System.out.println("Error in TimesheetList.addTimesheet()");
            System.out.println(statement.toString() 
                    + " was the statment being sent.");
            sqlex.printStackTrace();
        }
    }
    
    /**
     * Find timesheet by date.
     * @param endWeek WeekEnd
     * @param id employee ID
     * @return true or false.
     */
    private boolean find(Date endWeek, int id) {
        PreparedStatement statement = null;
        Connection connection = null;
        
        try {
            try {
                connection = datasource.getConnection();
                try {
                    statement = connection.prepareStatement("SELECT * FROM " 
                            + "Timesheet WHERE " 
                            + " EndWeek = ? AND EmployeeNum = ?");
                    java.sql.Date date = new java.sql.Date(
                            endWeek.getTime());
                    statement.setDate(1, date);
                    statement.setInt(2, id);
                    ResultSet result = statement.executeQuery();
                    
                    if (result.next()) {
                        return true;
                    }
                    return false;
                    
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException sqlex) {
            System.out.println("Error in TimesheetList.find()");
            System.out.println(statement.toString() 
                    + " was the statment being sent.");
            sqlex.printStackTrace();
        }
        return false;
    }
    
    /**
     * Add row to timesheet. (database)
     * @param row timesheet row
     * @param timesheetDate date
     * @param id employeeId
     */
    public void addTimesheetRow(TimesheetRow row, Date timesheetDate, int id) {
        PreparedStatement statement = null;
        Connection connection = null;
        
        try {
            try {
                connection = datasource.getConnection();
                try {
                    
                    statement = connection.prepareStatement(
                            "INSERT INTO TimesheetRow " 
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                    java.sql.Date date = new java.sql.Date(
                            timesheetDate.getTime());
                    
                    statement.setDate(1, date);
                    statement.setInt(2, row.getProjectID());
                    statement.setString(THREE, row.getWorkPackage());
                    statement.setBigDecimal(FOUR, 
                            row.getHour(TimesheetRow.SAT));
                    statement.setBigDecimal(FIVE, 
                            row.getHour(TimesheetRow.SUN));
                    statement.setBigDecimal(SIX, 
                            row.getHour(TimesheetRow.MON));
                    statement.setBigDecimal(SEVEN, 
                            row.getHour(TimesheetRow.TUE));
                    statement.setBigDecimal(EIGHT, 
                            row.getHour(TimesheetRow.WED));
                    statement.setBigDecimal(NINE, 
                            row.getHour(TimesheetRow.THU));
                    statement.setBigDecimal(TEN, 
                            row.getHour(TimesheetRow.FRI));
                    statement.setString(ELEVEN, row.getNotes());
                    statement.setInt(ELEVEN + 1, id);

                    statement.executeUpdate();
                    
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException sqlex) {
            System.out.println("Error in TimesheetList.addTimesheetRow()");
            System.out.println(statement.toString() 
                    + " was the statment being sent.");
            sqlex.printStackTrace();
        }
    }
    /** 
     * Update Timesheet database.
     * @param timesheet
     *              Timesheet object
     */
    public void merge(Timesheet timesheet) {
        PreparedStatement statement = null;
        Connection connection = null;
        
        try {
            try {
                connection = datasource.getConnection();
                try {
                    statement = connection.prepareStatement("UPDATE Timesheet "
                            + "SET UserName = ?, EmployeeName = ? "
                                    + "WHERE EmployeeNum = ?");
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException sqlex) {
            System.out.println("Error in TimesheetList.merge()");
            System.out.println(statement.toString() 
                    + " was the statment being sent.");
            sqlex.printStackTrace();
        }
    }
    
    /**
     * Update timesheetRow.
     * @param row
     *              TimesheetRow object
     * @param sheetDate
     *              Timesheet end date
     */
    public void mergeRow(TimesheetRow row, Date sheetDate) {
        PreparedStatement statement = null;
        Connection connection = null;
        
        try {
            try {
                connection = datasource.getConnection();
                try {
                    statement = connection.prepareStatement(
                            "UPDATE TimesheetRow SET ProjectID = ?,"
                            + "WorkPackage = ?, "
                            + "SAT = ?, SUN = ?, MON = ?, TUE = ?, "
                            + "WED = ?, THU = ?, FRI = ?, Notes = ?"
                            + " WHERE EndWeek = ? AND"
                            + " (ProjectID = ? AND WorkPackage = ?)");
                    
                    java.sql.Date date = new java.sql.Date(
                            sheetDate.getTime());
                    
                    statement.setInt(1, row.getProjectID());
                    statement.setString(2, row.getWorkPackage());
                    statement.setBigDecimal(THREE, 
                            row.getHour(TimesheetRow.SAT));
                    statement.setBigDecimal(FOUR, 
                            row.getHour(TimesheetRow.SUN));
                    statement.setBigDecimal(FIVE, 
                            row.getHour(TimesheetRow.MON));
                    statement.setBigDecimal(SIX, 
                            row.getHour(TimesheetRow.TUE));
                    statement.setBigDecimal(SEVEN, 
                            row.getHour(TimesheetRow.WED));
                    statement.setBigDecimal(EIGHT, 
                            row.getHour(TimesheetRow.THU));
                    statement.setBigDecimal(NINE, 
                            row.getHour(TimesheetRow.FRI));
                    statement.setString(TEN, row.getNotes());
                    statement.setDate(ELEVEN, date);
                    statement.setInt(TWELVE, 0);
                    statement.setString(TWELVE + 1, "NULL");
                    statement.executeUpdate();
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException sqlex) {
            System.out.println("Error in TimesheetList.mergeRow()");
            System.out.println(statement.toString() 
                    + " was the statment being sent.");
            sqlex.printStackTrace();
        }
    }
}
