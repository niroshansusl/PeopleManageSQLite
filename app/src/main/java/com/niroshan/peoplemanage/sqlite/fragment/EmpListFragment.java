package com.niroshan.peoplemanage.sqlite.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.niroshan.peoplemanage.R;
import com.niroshan.peoplemanage.sqlite.MainActivity;
import com.niroshan.peoplemanage.sqlite.adapter.EmpListAdapter;
import com.niroshan.peoplemanage.sqlite.db.EmployeeDAO;
import com.niroshan.peoplemanage.sqlite.dto.BeanEmployee;

/**
 * Created by niroshan on 8/16/2017.
 */

public class EmpListFragment extends Fragment implements OnItemClickListener,
		OnItemLongClickListener {

	public static final String ARG_ITEM_ID = "employee_list";

	Activity activity;
	ListView employeeListView;
	ArrayList<BeanEmployee> beanEmployees;
	EmpListAdapter employeeListAdapter;
	EmployeeDAO employeeDAO;

	private GetEmpTask task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		employeeDAO = new EmployeeDAO(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_emp_list, container,
				false);
		findViewsById(view);

		task = new GetEmpTask(activity);
		task.execute((Void) null);

		employeeListView.setOnItemClickListener(this);
		employeeListView.setOnItemLongClickListener(this);
		return view;
	}

	private void findViewsById(View view) {
		employeeListView = (ListView) view.findViewById(R.id.list_emp);
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position,
			long id) {
		BeanEmployee beanEmployee = (BeanEmployee) list.getItemAtPosition(position);

		if (beanEmployee != null) {
			Bundle arguments = new Bundle();
			arguments.putParcelable("selectedEmployee", beanEmployee);
			CustomEmpDialogFragment customEmpDialogFragment = new CustomEmpDialogFragment();
			customEmpDialogFragment.setArguments(arguments);
			customEmpDialogFragment.show(getFragmentManager(),
					CustomEmpDialogFragment.ARG_ITEM_ID);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		BeanEmployee beanEmployee = (BeanEmployee) parent.getItemAtPosition(position);
		// Use AsyncTask to delete from database
		employeeDAO.deleteEmployee(beanEmployee);
		employeeListAdapter.remove(beanEmployee);

		return true;
	}

	public class GetEmpTask extends AsyncTask<Void, Void, ArrayList<BeanEmployee>> {

		private final WeakReference<Activity> activityWeakRef;

		public GetEmpTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected ArrayList<BeanEmployee> doInBackground(Void... arg0) {
			ArrayList<BeanEmployee> beanEmployeeList = employeeDAO.getEmployees();
			return beanEmployeeList;
		}

		@Override
		protected void onPostExecute(ArrayList<BeanEmployee> empList) {
			if (activityWeakRef.get() != null
					&& !activityWeakRef.get().isFinishing()) {
				beanEmployees = empList;
				if (empList != null) {
					if (empList.size() != 0) {
						employeeListAdapter = new EmpListAdapter(activity,
								empList);
						employeeListView.setAdapter(employeeListAdapter);
					} else {
						Toast.makeText(activity, "No Employee Records",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}

	/*
	 * This method is invoked from MainActivity onFinishDialog() method. It is
	 * called from CustomEmpDialogFragment when an employee record is updated.
	 * This is used for communicating between fragments.
	 */
	public void updateView() {
		task = new GetEmpTask(activity);
		task.execute((Void) null);
	}

	@Override
	public void onResume() {
		getActivity().setTitle(R.string.app_name);
		((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
		super.onResume();
	}
}
