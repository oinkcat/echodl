package ru.softcat.echodl;
import java.lang.reflect.*;
import android.os.*;

public final class Utils
{
	public static void applyUrlExposureHack() {
		try {
			Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure"); 
			m.invoke(null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Utils() {}
}
