package com.example.cam.categoryUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
    private static final String TAG = ExceptionUtil.class.getSimpleName();
    private ExceptionUtil() { }
    
	/**
	 * Get exception call stack information
	 * 
	 * @param e
	 * @return
	 */
	public static String exception(Exception e) {
		StringWriter writer = new StringWriter();
		String rvalue = "";
		
		try {	
			e.printStackTrace(new PrintWriter(writer));
			rvalue = writer.toString();
			return rvalue.substring(0, rvalue.length() -1);
		} catch(Exception ex) {
			// ignore
		}
		
		return rvalue;
	}

    public static String error(Error e) {
        StringWriter writer = new StringWriter();
        String rvalue = "";

        try {
            e.printStackTrace(new PrintWriter(writer));
            rvalue = writer.toString();
            return rvalue.substring(0, rvalue.length() -1);
        } catch(Exception ex) {
            // ignore
        }

        return rvalue;
    }
}
