package com.niroshan.peoplemanage.sqlite.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.niroshan.peoplemanage.R;
import com.niroshan.peoplemanage.sqlite.MainActivity;
import com.niroshan.peoplemanage.sqlite.db.DepartmentDAO;
import com.niroshan.peoplemanage.sqlite.db.EmployeeDAO;
import com.niroshan.peoplemanage.sqlite.dto.BeanDepartment;
import com.niroshan.peoplemanage.sqlite.dto.BeanEmployee;

/**
 * Created by niroshan on 8/16/2017.
 */

public class CustomEmpDialogFragment extends AppCompatDialogFragment {

	// UI references
	private EditText empNameEtxt;
	private EditText empSalaryEtxt;
	private EditText empDobEtxt;
	private Spinner deptSpinner;
	private LinearLayout submitLayout;

	private BeanEmployee beanEmployee;

	EmployeeDAO employeeDAO;
	ArrayAdapter<BeanDepartment> adapter;

	public static final String ARG_ITEM_ID = "emp_dialog_fragment";
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);
	
	/*
	 * Callback used to communicate with EmpListFragment to notify the list adapter.
	 * MainActivity implements this interface and communicates with EmpListFragment.
	 */
	public interface CustomEmpDialogFragmentListener {
		void onFinishDialog();
	}

	public CustomEmpDialogFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		employeeDAO = new EmployeeDAO(getActivity());

		Bundle bundle = this.getArguments();
		beanEmployee = bundle.getParcelable("selectedEmployee");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View customDialogView = inflater.inflate(R.layout.fragment_add_emp,
				null);
		builder.setView(customDialogView);

		empNameEtxt = (EditText) customDialogView.findViewById(R.id.etxt_name);
		empSalaryEtxt = (EditText) customDialogView
				.findViewById(R.id.etxt_salary);
		empDobEtxt = (EditText) customDialogView.findViewById(R.id.etxt_dob);
		deptSpinner = (Spinner) customDialogView
				.findViewById(R.id.spinner_dept);
		submitLayout = (LinearLayout) customDialogView
				.findViewById(R.id.layout_submit);
		submitLayout.setVisibility(View.GONE);
		setValue();

		builder.setTitle(R.string.update_emp);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.update,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							beanEmployee.setDateOfBirth(formatter.parse(empDobEtxt.getText().toString()));
						} catch (ParseException e) {
							Toast.makeText(getActivity(),
									"Invalid date format!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						beanEmployee.setName(empNameEtxt.getText().toString());
						beanEmployee.setSalary(Double.parseDouble(empSalaryEtxt
								.getText().toString()));
						BeanDepartment dept = (BeanDepartment) adapter
								.getItem(deptSpinner.getSelectedItemPosition());
						beanEmployee.setBeanDepartment(dept);
						long result = employeeDAO.update(beanEmployee);
						if (result > 0) {
							MainActivity activity = (MainActivity) getActivity();
							activity.onFinishDialog();
						} else {
							Toast.makeText(getActivity(),
									"Unable to update employee",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

		AlertDialog alertDialog = builder.create();

		return alertDialog;
	}

	private void setValue() {
		DepartmentDAO departmentDAO = new DepartmentDAO(getActivity());

		List<BeanDepartment> beanDepartments = departmentDAO.getDepartments();
		adapter = new ArrayAdapter<BeanDepartment>(getActivity(),
				android.R.layout.simple_list_item_1, beanDepartments);
		deptSpinner.setAdapter(adapter);
		int pos = adapter.getPosition(beanEmployee.getBeanDepartment());

		if (beanEmployee != null) {
			empNameEtxt.setText(beanEmployee.getName());
			empSalaryEtxt.setText(beanEmployee.getSalary() + "");
			empDobEtxt.setText(formatter.format(beanEmployee.getDateOfBirth()));
			deptSpinner.setSelection(pos);
		}
	}
}
