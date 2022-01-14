package ru.softcat.echodl;

import android.app.*;
import android.os.*;
import java.util.*;
import android.widget.*;
import android.content.*;
import android.view.*;

public class MainActivity extends Activity implements ProgramsView
{
	private final int MENU_ITEM_ID_RELOAD = 0;
	
	private final int MENU_ITEM_ID_EXIT = 1;
	
	private final int MENU_ITEM_ID_SITE = 2;
	
	private Presenter presenter;
	
	private ListView progInfoList;
	
	private AlertDialog progressDialog;
	
	private static String currentAction;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, MENU_ITEM_ID_RELOAD, Menu.NONE, "Reload")
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		menu.add(Menu.NONE, MENU_ITEM_ID_SITE, Menu.NONE, "Go to Web Site")
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			
		menu.add(Menu.NONE, MENU_ITEM_ID_EXIT, Menu.NONE, "Exit")
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch(item.getItemId()) {
			case MENU_ITEM_ID_RELOAD:
				presenter.downloadPrograms();
				break;
			case MENU_ITEM_ID_SITE:
				presenter.goToWebSite();
				break;
			case MENU_ITEM_ID_EXIT:
				finishAndRemoveTask();
				System.exit(0);
				break;
			default:
				return false;
		}
		
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

        setContentView(R.layout.main);
		setTitle(getResources().getString(R.string.app_title));
		
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
