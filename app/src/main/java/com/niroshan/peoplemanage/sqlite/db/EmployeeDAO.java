package com.niroshan.peoplemanage.sqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.niroshan.peoplemanage.sqlite.dto.BeanDepartment;
import com.niroshan.peoplemanage.sqlite.dto.BeanEmployee;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by niroshan on 8/16/2017.
 */

public class EmployeeDAO extends EmployeeDBDAO {

    public static final String EMPLOYEE_ID_WITH_PREFIX = "emp.id";
    public static final String EMPLOYEE_NAME_WITH_PREFIX = "emp.name";
    public static final String DEPT_NAME_WITH_PREFIX = "dept.name";

    private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN + " =?";

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    public EmployeeDAO(Context context) {
        super(context);
    }

    public long save(BeanEmployee beanEmployee) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, beanEmployee.getName());
        values.put(DataBaseHelper.EMPLOYEE_DOB, formatter.format(beanEmployee.getDateOfBirth()));
        values.put(DataBaseHelper.EMPLOYEE_SALARY, beanEmployee.getSalary());
        values.put(DataBaseHelper.EMPLOYEE_DEPARTMENT_ID, beanEmployee.getBeanDepartment().getId());

        return database.insert(DataBaseHelper.EMPLOYEE_TABLE, null, values);
    }

    public long update(BeanEmployee beanEmployee) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, beanEmployee.getName());
        values.put(DataBaseHelper.EMPLOYEE_DOB, formatter.format(beanEmployee.getDateOfBirth()));
        values.put(DataBaseHelper.EMPLOYEE_SALARY, beanEmployee.getSalary());
        values.put(DataBaseHelper.EMPLOYEE_DEPARTMENT_ID, beanEmployee.getBeanDepartment().getId());

        long result = database.update(DataBaseHelper.EMPLOYEE_TABLE, values,
                WHERE_ID_EQUALS,
                new String[]{String.valueOf(beanEmployee.getId())});
        Log.d("Update Result:", "=" + result);
        return result;
    }

    public int deleteEmployee(BeanEmployee beanEmployee) {
        return database.delete(DataBaseHelper.EMPLOYEE_TABLE, WHERE_ID_EQUALS,
                new String[]{beanEmployee.getId() + ""});
    }

    // METHOD 1
    // Uses rawQuery() to query multiple tables
    public ArrayList<BeanEmployee> getEmployees() {
        ArrayList<BeanEmployee> beanEmployees = new ArrayList<BeanEmployee>();
        String query = "SELECT " + EMPLOYEE_ID_WITH_PREFIX + ","
                + EMPLOYEE_NAME_WITH_PREFIX + "," + DataBaseHelper.EMPLOYEE_DOB
                + "," + DataBaseHelper.EMPLOYEE_SALARY + ","
                + DataBaseHelper.EMPLOYEE_DEPARTMENT_ID + ","
                + DEPT_NAME_WITH_PREFIX + " FROM "
                + DataBaseHelper.EMPLOYEE_TABLE + " emp, "
                + DataBaseHelper.DEPARTMENT_TABLE + " dept WHERE emp."
                + DataBaseHelper.EMPLOYEE_DEPARTMENT_ID + " = dept."
                + DataBaseHelper.ID_COLUMN;

        // Building query using INNER JOIN keyword
        /*String query = "SELECT " + EMPLOYEE_ID_WITH_PREFIX + ","
		+ EMPLOYEE_NAME_WITH_PREFIX + "," + DataBaseHelper.EMPLOYEE_DOB
		+ "," + DataBaseHelper.EMPLOYEE_SALARY + ","
		+ DataBaseHelper.EMPLOYEE_DEPARTMENT_ID + ","
		+ DEPT_NAME_WITH_PREFIX + " FROM "
		+ DataBaseHelper.EMPLOYEE_TABLE + " emp INNER JOIN "
		+ DataBaseHelper.DEPARTMENT_TABLE + " dept ON emp."
		+ DataBaseHelper.EMPLOYEE_DEPARTMENT_ID + " = dept."
		+ DataBaseHelper.ID_COLUMN;*/

        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            BeanEmployee beanEmployee = new BeanEmployee();
            beanEmployee.setId(cursor.getInt(0));
            beanEmployee.setName(cursor.getString(1));
            try {
                beanEmployee.setDateOfBirth(formatter.parse(cursor.getString(2)));
            } catch (ParseException e) {
                beanEmployee.setDateOfBirth(null);
            }
            beanEmployee.setSalary(cursor.getDouble(3));

            BeanDepartment beanDepartment = new BeanDepartment();
            beanDepartment.setId(cursor.getInt(4));
            beanDepartment.setName(cursor.getString(5));

            beanEmployee.setBeanDepartment(beanDepartment);

            beanEmployees.add(beanEmployee);
        }
        return beanEmployees;
    }

    // METHOD 2
    // Uses SQLiteQueryBuilder to query multiple tables
	/*public ArrayList<Employee> getEmployees() {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder
				.setTables(DataBaseHelper.EMPLOYEE_TABLE
						+ " INNER JOIN "
						+ DataBaseHelper.DEPARTMENT_TABLE
						+ " ON "
						+ DataBaseHelper.EMPLOYEE_DEPARTMENT_ID
						+ " = "
						+ (DataBaseHelper.DEPARTMENT_TABLE + "." + DataBaseHelper.ID_COLUMN));

		// Get cursor
		Cursor cursor = queryBuilder.query(database, new String[] {
				EMPLOYEE_ID_WITH_PREFIX,
				DataBaseHelper.EMPLOYEE_TABLE + "."
						+ DataBaseHelper.NAME_COLUMN,
				DataBaseHelper.EMPLOYEE_DOB,
				DataBaseHelper.EMPLOYEE_SALARY,
				DataBaseHelper.EMPLOYEE_DEPARTMENT_ID,
				DataBaseHelper.DEPARTMENT_TABLE + "."
						+ DataBaseHelper.NAME_COLUMN }, null, null, null, null,
				null);

		while (cursor.moveToNext()) {
			Employee employee = new Employee();
			employee.setId(cursor.getInt(0));
			employee.setName(cursor.getString(1));
			try {
				employee.setDateOfBirth(formatter.parse(cursor.getString(2)));
			} catch (ParseException e) {
				employee.setDateOfBirth(null);
			}
			employee.setSalary(cursor.getDouble(3));

			Department department = new Department();
			department.setId(cursor.getInt(4));
			department.setName(cursor.getString(5));

			employee.setDepartment(department);

			employees.add(employee);
		}
		return employees;
	}*/
}
