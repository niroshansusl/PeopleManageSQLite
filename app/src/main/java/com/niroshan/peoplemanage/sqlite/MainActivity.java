package com.niroshan.peoplemanage.sqlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.niroshan.peoplemanage.R;
import com.niroshan.peoplemanage.sqlite.db.DepartmentDAO;
import com.niroshan.peoplemanage.sqlite.fragment.CustomEmpDialogFragment;
import com.niroshan.peoplemanage.sqlite.fragment.EmpAddFragment;
import com.niroshan.peoplemanage.sqlite.fragment.EmpListFragment;

/**
 * Created by niroshan on 8/16/2017.
 */

public class MainActivity extends AppCompatActivity implements CustomEmpDialogFragment.CustomEmpDialogFragmentListener {

	private Fragment contentFragment;
	private EmpListFragment employeeListFragment;
	private EmpAddFragment employeeAddFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager fragmentManager = getSupportFragmentManager();

		DepartmentDAO deptDAO = new DepartmentDAO(this);
		
		//Initially loads departments
		if(deptDAO.getDepartments().size() <= 0)
			deptDAO.loadDepartments();
		
		/*
		 * This is called when orientation is changed.
		 */
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("content")) {
				String content = savedInstanceState.getString("content");
				if (content.equals(EmpAddFragment.ARG_ITEM_ID)) {
					if (fragmentManager
							.findFragmentByTag(EmpAddFragment.ARG_ITEM_ID) != null) {
						setFragmentTitle(R.string.add_emp);
						contentFragment = fragmentManager
								.findFragmentByTag(EmpAddFragment.ARG_ITEM_ID);
					}
				}
			}
			if (fragmentManager.findFragmentByTag(EmpListFragment.ARG_ITEM_ID) != null) {
				employeeListFragment = (EmpListFragment) fragmentManager
						.findFragmentByTag(EmpListFragment.ARG_ITEM_ID);
				contentFragment = employeeListFragment;
			}
		} else {
			employeeListFragment = new EmpListFragment();
			setFragmentTitle(R.string.app_name);
			switchContent(employeeListFragment, EmpListFragment.ARG_ITEM_ID);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			setFragmentTitle(R.string.add_emp);
			employeeAddFragment = new EmpAddFragment();
			switchContent(employeeAddFragment, EmpAddFragment.ARG_ITEM_ID);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (contentFragment instanceof EmpAddFragment) {
			outState.putString("content", EmpAddFragment.ARG_ITEM_ID);
		} else {
			outState.putString("content", EmpListFragment.ARG_ITEM_ID);
		}
		super.onSaveInstanceState(outState);
	}

	/*
	 * I consider EmpListFragment as the home fragment and it is not added to
	 * the back stack.
	 */
	public void switchContent(Fragment fragment, String tag) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		while (fragmentManager.popBackStackImmediate())
			;

		if (fragment != null) {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.content_frame, fragment, tag);
			// Only EmpAddFragment is added to the back stack.
			if (!(fragment instanceof EmpListFragment)) {
				transaction.addToBackStack(tag);
			}
			transaction.commit();
			contentFragment = fragment;
		}
	}

	protected void setFragmentTitle(int resourseId) {
		setTitle(resourseId);
		getSupportActionBar().setTitle(resourseId);

	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			super.onBackPressed();
		} else if (contentFragment instanceof EmpListFragment
				|| fm.getBackStackEntryCount() == 0) {
			//finish();
			onShowQuitDialog();
		}
	}
	
	public void onShowQuitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);

		builder.setMessage("Do You Want To Quit?");
		builder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	/*
	 * Callback used to communicate with EmpListFragment to notify the list adapter.
	 * Communication between fragments goes via their Activity class.
	 */
	@Override
	public void onFinishDialog() {
		if (employeeListFragment != null) {
			employeeListFragment.updateView();
		}
	}
}
