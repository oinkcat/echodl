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
	
	private static String currentAction;

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
		System.exit(0);
		return true;
	}
	
	@Override
	public void setProgramList(List<Program> programs)
	{
		final int listResId = android.R.layout.simple_list_item_2;
		final LayoutInflater inflater = getLayoutInflater();
		
		progInfoList.setAdapter(new ArrayAdapter(this, listResId, programs) {
			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				if(convertView == null) {
					convertView = inflater.inflate(listResId, null);
				}
				
				Program item = (Program)this.getItem(pos);
				
				TextView captionView = convertView.findViewById(android.R.id.text1);
				captionView.setText(item.getName());
				TextView detailView = convertView.findViewById(android.R.id.text2);
				detailView.setText(item.getDetails());
				
				return convertView;
			}
		});
	}

	@Override
	public void showProgress(String actionName)
	{
		currentAction = actionName;
		
		progressDialog = new AlertDialog.Builder(this)
			.setMessage("Please wait....")
			.setTitle(currentAction)
			.create();
			
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void showDonePercent(int percent)
	{
		if(progressDialog == null) {
			showProgress(currentAction);
		}
		
		progressDialog.setMessage(String.format("%d%% done", percent));
	}

	@Override
	public void hideProgress()
	{
		if(progressDialog == null) { return; }
		
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
		
		presenter = Presenter.getInstance(this);
		presenter.downloadPrograms();
    }
}
