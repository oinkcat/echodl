package ru.softcat.echodl;
import java.util.*;
import android.content.*;
import android.net.*;
import android.os.*;
import java.io.File;
import java.lang.reflect.*;

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
		view.showProgress("Loading program list...");
		
		if(programs == null) {
			ListDownloader downloadTask = new ListDownloader(this);
			downloadTask.execute();
		} else {
			gotPrograms(programs);
		}
	}
	
	public void programToDownloadSelected(int programIdx) {
		view.showProgress("Downloading program...");
		
		Program programToDownload = programs.get(programIdx);
		
		ProgramAudioDownloader downloader = new ProgramAudioDownloader(this);
		downloader.execute(new Program[] { programToDownload });
	}
	
	public void gotPrograms(List<Program> programs) {
		this.programs = programs;
		
		view.setProgramList(programs);
		view.hideProgress();
	}
	
	public void gotProgress(int percent) {
		view.showDonePercent(percent);
	}
	
	public void programDownloaded(String savedFileName) {
		view.hideProgress();
		view.showMessage("Program downloaded!");
		
		applyUrlExposureHack();
		
		Intent openIntent = new Intent(Intent.ACTION_VIEW);
		Uri url = Uri.parse("file://".concat(savedFileName));
		openIntent.setDataAndType(url, "audio/mp3");
		openIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		context.startActivity(openIntent);
	}
	
	private void applyUrlExposureHack() {
		try {
			Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure"); 
			m.invoke(null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFailed() {
		view.hideProgress();
		view.showMessage("Program download error!");
	}
}

