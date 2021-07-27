package ru.softcat.echodl;

import android.app.*;
import android.os.*;
import java.util.*;
import android.widget.*;
import android.content.*;
import android.view.*;

public class MainActivity extends Activity implements ProgramsView
{
	private Presenter presenter;
	
	private ListView progInfoList;
	
	private AlertDialog progressDialog;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("Exit").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		finishAndRemoveTask();
		return true;
	}
	
	@Override
	public void setProgramList(List<Program> programs)
	{
		final int listResId = android.R.layout.simple_list_item_1;
		progInfoList.setAdapter(new ArrayAdapter(this, listResId, programs));
	}

	@Override
	public void showProgress(String actionName)
	{
		progressDialog = new AlertDialog.Builder(this)
			.setMessage("Please wait....")
			.setTitle(actionName)
			.create();
			
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void showDonePercent(int percent)
	{
		progressDialog.setMessage(String.format("%d%% done", percent));
	}

	@Override
	public void hideProgress()
	{
		progressDialog.hide();
		progressDialog = null;
	}

	@Override
	public void showMessage(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		setTitle(getResources().getString(R.string.app_title));
		
        setContentView(R.layout.main);
		
		progInfoList = findViewById(R.id.progInfoList);
		progInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
				presenter.programToDownloadSelected(pos);
			}
		});
		
		presenter = new Presenter(this);
		presenter.downloadPrograms();
    }
}
