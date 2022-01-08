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
	
	private static Presenter instance;
	
	private List<Program> programs;

	public static Presenter getInstance(ProgramsView view) {
		if(instance == null) {
			instance = new Presenter(view);
		} else {
			instance.updateView(view);
		}

		return instance;
	}
	
	private void updateView(ProgramsView view) {
		this.view = view;
	}
	
	private Presenter(ProgramsView view) {
		this.view = view;
		this.context = (Context)view;
		Config.initialize(context);
	}
	
	public void downloadPrograms() {
		if(programs == null) {
			view.showProgress("Loading program list...");
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
		
		Utils.applyUrlExposureHack();
		
		Intent openIntent = new Intent(Intent.ACTION_VIEW);
		Uri url = Uri.parse("file://".concat(savedFileName));
		openIntent.setDataAndType(url, "audio/mp3");
		openIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		context.startActivity(openIntent);
	}
	
	public void downloadFailed() {
		view.hideProgress();
		view.showMessage("Program download error!");
	}
}

