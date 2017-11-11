package com.corejsf;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;

/**
 * TimesheetList class that implements TimesheetCollection interface.
 * 
 * @author Philip
 * @version 1.0
 */
public class TimesheetList implements TimesheetCollection {

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
        for (Timesheet sheet : timesheets) {
            if (sheet.getEmployee() == e) {
                empTimesheet.add(sheet);
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

    public void addTimesheet(Timesheet sheet) {
        timesheets.add(sheet);
    }
}
