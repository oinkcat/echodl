package ru.softcat.echodl;
import java.util.*;
import android.os.*;

public class ListDownloader extends AsyncTask<Void, Integer, List<Program>>
{
	private Presenter presenter;
	
	public ListDownloader(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	protected List<Program> doInBackground(Void[] p)
	{
		ArrayList<Program> testPrograms = new ArrayList<>();
		
		try {
			PageDownloader echoPageDl = new PageDownloader();
			String pageContents = echoPageDl.download();
			
			ProgramListParser progParser = new ProgramListParser();
			return progParser.parse(pageContents);
		} catch(Exception _) {}

		for(int i = 0; i < 10; i++) {
			Program test = new Program(String.format("Sample program #%d", i));
			testPrograms.add(test);
		}
	
		return testPrograms;
	}

	@Override
	protected void onPostExecute(List<Program> result)
	{
		presenter.gotPrograms(result);
	}
}
