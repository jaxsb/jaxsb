/* Copyright (c) 2008 JAX-SB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxsb.sample;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jaxsb.runtime.Bindings;
import org.jaxsb.www.sample.list.xAA.$EmployeeType;
import org.jaxsb.www.sample.list.xAA.$StaffType;
import org.jaxsb.www.sample.list.xAA.$VolunteerType;
import org.jaxsb.www.sample.list.xAA.Roster;
import org.openjax.xml.datatype.Date;
import org.openjax.xml.datatype.Time;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.xml.sax.SAXException;

public class ListSample {
  private static void printCommon(final $StaffType staffType) {
    final String name = staffType.getName().text();
    System.out.println("Name: " + name);

    final List<String> workDays = staffType.getWorkDays().text();
    System.out.println("Work Days: " + name);
    for (final String workDay : workDays) // [L]
      System.out.println("\t" + workDay);
  }

  public $AnyType<?> runSample() throws IOException, SAXException {
    final URL url = getClass().getResource("/list.xml");
    final Roster roster = (Roster)Bindings.parse(url);
    if (roster.getEmployees() != null) {
      final List<$EmployeeType> employees = roster.getEmployees(0).getEmployee();
      for (final $EmployeeType employee : employees) { // [L]
        printCommon(employee);

        final String position = employee.getPosition().text();
        System.out.println("Position: " + position);

        final List<Date> vacationDates = employee.getVacationDates().text();
        System.out.println("Vacation Dates:");
        for (final Date vacationDate : vacationDates) // [L]
          System.out.println("\t" + vacationDate);
      }

      final Roster.Employees.Employee employee = new Roster.Employees.Employee();
      employee.setName(new Roster.Employees.Employee.Name("Woody Harold"));
      employee.setWorkDays(new Roster.Employees.Employee.WorkDays(Roster.Employees.Employee.WorkDays.mon, Roster.Employees.Employee.WorkDays.tue, Roster.Employees.Employee.WorkDays.wed));
      employee.setPosition(new Roster.Employees.Employee.Position(Roster.Employees.Employee.Position.stockroom));
      employee.setVacationDates(new Roster.Employees.Employee.VacationDates(new Date(2008, 8, 12), new Date(2008, 9, 22), new Date(2008, 10, 30)));
      employees.add(employee);
    }

    if (roster.getVolunteers() != null) {
      final List<$VolunteerType> volunteers = roster.getVolunteers(0).getVolunteer();
      for (final $VolunteerType volunteer : volunteers) { // [L]
        printCommon(volunteer);

        final List<Time> breakTimes = volunteer.getBreakTimes().text();
        System.out.println("Break Times:");
        for (final Time breakTime : breakTimes) // [L]
          System.out.println("\t" + breakTime);
      }

      final Roster.Volunteers.Volunteer volunteer = new Roster.Volunteers.Volunteer();
      volunteer.setName(new Roster.Employees.Employee.Name("Michelle Smith"));
      volunteer.setWorkDays(new Roster.Employees.Employee.WorkDays(Roster.Employees.Employee.WorkDays.mon, Roster.Employees.Employee.WorkDays.tue, Roster.Employees.Employee.WorkDays.wed));
      volunteer.setBreakTimes(new Roster.Volunteers.Volunteer.BreakTimes(new Time(10, 15, 0), new Time(12, 0, 0), new Time(15, 30, 0)));
      volunteers.add(volunteer);
    }

    return roster;
  }
}