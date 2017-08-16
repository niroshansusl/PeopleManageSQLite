package com.niroshan.peoplemanage.sqlite.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.niroshan.peoplemanage.R;
import com.niroshan.peoplemanage.sqlite.dto.BeanEmployee;

/**
 * Created by niroshan on 8/16/2017.
 */

public class EmpListAdapter extends ArrayAdapter<BeanEmployee> {

	private Context context;
	List<BeanEmployee> beanEmployees;

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);
	
	public EmpListAdapter(Context context, List<BeanEmployee> beanEmployees) {
		super(context, R.layout.list_item, beanEmployees);
		this.context = context;
		this.beanEmployees = beanEmployees;
	}

	private class ViewHolder {
		TextView empIdTxt;
		TextView empNameTxt;
		TextView empDobTxt;
		TextView empSalaryTxt;
		TextView empDeptNameTxt;
	}

	@Override
	public int getCount() {
		return beanEmployees.size();
	}

	@Override
	public BeanEmployee getItem(int position) {
		return beanEmployees.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			
			holder.empIdTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_id);
			holder.empNameTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_name);
			holder.empDobTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_dob);
			holder.empSalaryTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_salary);
			holder.empDeptNameTxt = (TextView) convertView
					.findViewById(R.id.txt_emp_dept);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BeanEmployee beanEmployee = (BeanEmployee) getItem(position);
		holder.empIdTxt.setText(beanEmployee.getId() + "");
		holder.empNameTxt.setText(beanEmployee.getName());
		holder.empSalaryTxt.setText(beanEmployee.getSalary() + "");
		holder.empDeptNameTxt.setText(beanEmployee.getBeanDepartment().getName());
		
		holder.empDobTxt.setText(formatter.format(beanEmployee.getDateOfBirth()));
		
		return convertView;
	}

	@Override
	public void add(BeanEmployee beanEmployee) {
		beanEmployees.add(beanEmployee);
		notifyDataSetChanged();
		super.add(beanEmployee);
	}

	@Override
	public void remove(BeanEmployee beanEmployee) {
		beanEmployees.remove(beanEmployee);
		notifyDataSetChanged();
		super.remove(beanEmployee);
	}	
}

