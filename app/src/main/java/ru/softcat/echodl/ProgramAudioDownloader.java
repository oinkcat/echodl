package ru.softcat.echodl;
import android.os.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ProgramAudioDownloader extends AsyncTask<Program, Integer, String>
{
	private final int BUFFER_SIZE = 64 * 1024;
	
	private Presenter presenter;
	
	public ProgramAudioDownloader(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	protected String doInBackground(Program[] args) {
		String audioUrl = args[0].getDefaultLink();
		String fileName = audioUrl.substring(audioUrl.lastIndexOf("/") + 1);
		
		File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		String outFilePath = outDir.getAbsolutePath().concat("/").concat(fileName);
		
		try {
			URLConnection conn = new URL(audioUrl).openConnection();
			
			try(InputStream respStream = conn.getInputStream()) {
				try(FileOutputStream fos = new FileOutputStream(outFilePath)) {
					byte buffer[] = new byte[BUFFER_SIZE];
					long respSize = conn.getContentLengthLong();
					
					int read;
					long totalRead = 0;
					
					while((read = respStream.read(buffer)) > 0) {
						fos.write(buffer, 0, read);
						totalRead += read;
						
						long pct = totalRead * 100 / respSize;
						publishProgress(new Integer[] { (int)pct });
					}
				}
			}
			
			return outFilePath;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	protected void onProgressUpdate(Integer[] values)
	{
		presenter.gotProgress(values[0]);
	}

	@Override
	protected void onPostExecute(String savedFilePath)
	{
		if(savedFilePath != null) {
			presenter.programDownloaded(savedFilePath);
		} else {
			presenter.downloadFailed();
		}
	}
}
