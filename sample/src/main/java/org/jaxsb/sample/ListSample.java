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

import java.net.URL;
import java.util.List;

import org.jaxsb.www.sample.list.xAA.$EmployeeType;
import org.jaxsb.www.sample.list.xAA.$StaffType;
import org.jaxsb.www.sample.list.xAA.$VolunteerType;
import org.jaxsb.www.sample.list.xAA.Roster;
import org.openjax.xml.datatype.Date;
import org.openjax.xml.datatype.Time;
import org.jaxsb.runtime.Binding;
import org.jaxsb.runtime.Bindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListSample {
  private static final Logger logger = LoggerFactory.getLogger(ListSample.class);

  private static void printCommon(final $StaffType staffType) {
    final String name = staffType.getName().text();
    logger.info("Name: " + name);

    final List<String> workDays = staffType.getWorkDays().text();
    logger.info("Work Days: " + name);
    for (final String workDay : workDays)
      logger.info("\t" + workDay);
  }

  public Binding runSample() throws Exception {
    final URL url = getClass().getResource("/list.xml");
    final Roster roster = (Roster)Bindings.parse(url.openStream());
    if (roster.getEmployees() != null && roster.getEmployees().size() != -1) {
      final List<$EmployeeType> employees = roster.getEmployees(0).getEmployee();
      for (final $EmployeeType employee : employees) {
        printCommon(employee);

        final String position = employee.getPosition().text();
        logger.info("Position: " + position);

        final List<Date> vacationDates = employee.getVacationDates().text();
        logger.info("Vacation Dates:");
        for (final Date vacationDate : vacationDates)
          logger.info("\t" + vacationDate);
      }

      final Roster.Employees.Employee employee = new Roster.Employees.Employee();
      employee.setName(new Roster.Employees.Employee.Name("Woody Harold"));
      employee.setWorkDays(new Roster.Employees.Employee.WorkDays(Roster.Employees.Employee.WorkDays.mon, Roster.Employees.Employee.WorkDays.tue, Roster.Employees.Employee.WorkDays.wed));
      employee.setPosition(new Roster.Employees.Employee.Position(Roster.Employees.Employee.Position.stockroom));
      employee.setVacationDates(new Roster.Employees.Employee.VacationDates(new Date(2008, 8, 12), new Date(2008, 9, 22), new Date(2008, 10, 30)));
      employees.add(employee);
    }

    if (roster.getVolunteers() != null && roster.getVolunteers().size() != -1) {
      final List<$VolunteerType> volunteers = roster.getVolunteers(0).getVolunteer();
      for (final $VolunteerType volunteer : volunteers) {
        printCommon(volunteer);

        final List<Time> breakTimes = volunteer.getBreakTimes().text();
        logger.info("Break Times:");
        for (final Time breakTime : breakTimes)
          logger.info("\t" + breakTime);
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