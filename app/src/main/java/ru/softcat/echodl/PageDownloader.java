package ru.softcat.echodl;
import java.net.*;
import java.io.*;
import java.util.function.*;

public class PageDownloader
{
	private final String PAGE_URL = "https://echo.msk.ru";
	
	public String download() {
		URL echoUrl;
		
		try {
			echoUrl = new URL(PAGE_URL);
		} catch(MalformedURLException e) {
			return null;
		}
		
		try {
			URLConnection conn = echoUrl.openConnection();
			InputStream respStream = conn.getInputStream();
			BufferedReader respReader = new BufferedReader(new InputStreamReader(respStream));
			
			final StringBuilder pageContents = new StringBuilder();
			
			respReader.lines().forEach(new Consumer<String>() {
				public void accept(String line) {
					pageContents.append(line);
				}
			});
			
			return pageContents.toString();
		} catch(IOException e) {
			return null;
		}
	}
}
