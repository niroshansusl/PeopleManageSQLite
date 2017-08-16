package com.niroshan.peoplemanage.sqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.niroshan.peoplemanage.sqlite.dto.BeanDepartment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by niroshan on 8/16/2017.
 */

public class DepartmentDAO extends EmployeeDBDAO {

	private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN
			+ " =?";

	public DepartmentDAO(Context context) {
		super(context);
	}

	public long save(BeanDepartment beanDepartment) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NAME_COLUMN, beanDepartment.getName());

		return database.insert(DataBaseHelper.DEPARTMENT_TABLE, null, values);
	}

	public long update(BeanDepartment beanDepartment) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NAME_COLUMN, beanDepartment.getName());

		long result = database.update(DataBaseHelper.DEPARTMENT_TABLE, values,
				WHERE_ID_EQUALS,
				new String[] { String.valueOf(beanDepartment.getId()) });
		Log.d("Update Result:", "=" + result);
		return result;

	}

	public int deleteDept(BeanDepartment beanDepartment) {
		return database.delete(DataBaseHelper.DEPARTMENT_TABLE,
				WHERE_ID_EQUALS, new String[] { beanDepartment.getId() + "" });
	}

	public List<BeanDepartment> getDepartments() {
		List<BeanDepartment> beanDepartments = new ArrayList<BeanDepartment>();
		Cursor cursor = database.query(DataBaseHelper.DEPARTMENT_TABLE,
				new String[] { DataBaseHelper.ID_COLUMN,
						DataBaseHelper.NAME_COLUMN }, null, null, null, null,
				null);

		while (cursor.moveToNext()) {
			BeanDepartment beanDepartment = new BeanDepartment();
			beanDepartment.setId(cursor.getInt(0));
			beanDepartment.setName(cursor.getString(1));
			beanDepartments.add(beanDepartment);
		}
		return beanDepartments;
	}

	public void loadDepartments() {
		BeanDepartment beanDepartment = new BeanDepartment("Development");
		BeanDepartment beanDepartment1 = new BeanDepartment("R and D");
		BeanDepartment beanDepartment2 = new BeanDepartment("Human Resource");
		BeanDepartment beanDepartment3 = new BeanDepartment("Financial");
		BeanDepartment beanDepartment4 = new BeanDepartment("Marketing");
		BeanDepartment beanDepartment5 = new BeanDepartment("Sales");

		List<BeanDepartment> beanDepartments = new ArrayList<BeanDepartment>();
		beanDepartments.add(beanDepartment);
		beanDepartments.add(beanDepartment1);
		beanDepartments.add(beanDepartment2);
		beanDepartments.add(beanDepartment3);
		beanDepartments.add(beanDepartment4);
		beanDepartments.add(beanDepartment5);
		for (BeanDepartment dept : beanDepartments) {
			ContentValues values = new ContentValues();
			values.put(DataBaseHelper.NAME_COLUMN, dept.getName());
			database.insert(DataBaseHelper.DEPARTMENT_TABLE, null, values);
		}
	}

}
