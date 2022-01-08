package ru.softcat.echodl;
import android.content.res.*;
import android.content.*;
import org.xmlpull.v1.*;
import java.io.*;

public class Config
{
	private static Config instance;
	
	private String baseUrl;
	
	public static void initialize(Context ctx) {
		if(instance == null) {
			instance = new Config(ctx);
		}
	}
	
	public static Config getInstance() {
		return instance;
	}
	
	private Config(Context ctx) {
		Resources res = ctx.getResources();
		XmlPullParser confXml = res.getXml(R.xml.config);
		
		readConfValues(confXml);
	}
	
	private void readConfValues(XmlPullParser parser) {
		try {
			int evt = parser.next();

			while(evt != parser.END_DOCUMENT) {
				if(evt == parser.START_TAG) {
					String tagName = parser.getName();
					if(tagName.equals("baseUrl")) {
						parser.next();
						baseUrl = parser.getText();
					}
				}
				
				evt = parser.next();
			}
		} catch(XmlPullParserException _) { 
		} catch(IOException _) {
		}
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
}
