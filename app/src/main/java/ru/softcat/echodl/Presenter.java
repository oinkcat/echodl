package ru.softcat.echodl;
import java.util.*;
import android.content.*;
import android.net.*;

public class Presenter
{
	private ProgramsView view;
	
	private Context context;
	
	private static List<Program> programs;
	
	public Presenter(ProgramsView view) {
		this.view = view;
		this.context = (Context)view;
	}
	
	public void downloadPrograms() {
		view.showProgress("Downloading programs list...");
		
		if(programs == null) {
			ListDownloader downloadTask = new ListDownloader(this);
			downloadTask.execute();
		} else {
			gotPrograms(programs);
		}
	}
	
	public void programToDownloadSelected(int programIdx) {
		String audioUrl = programs.get(programIdx).getDefaultLink();
		Intent urlOpen = new Intent(Intent.ACTION_VIEW, Uri.parse(audioUrl));
		context.startActivity(urlOpen);
	}
	
	public void gotPrograms(List<Program> programs) {
		this.programs = programs;
		
		view.setProgramList(programs);
		view.hideProgress();
	}
}
