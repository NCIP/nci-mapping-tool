/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-mapping-tool/LICENSE.txt for details.
 */

package gov.nih.nci.evs.browser.utils;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */

public class TimeStamp {

  public static String toString(int i) {
	 return Integer.valueOf(i).toString();
  }

  public static String AMorPM(int i) {
	  if (i == Calendar.AM) {
		  return "AM";
	  } else {
		  return "PM";
	  }
  }

  public static String formatter(int i) {
	  if (i < 10) {
		  return "0" + Integer.valueOf(i).toString();
	  }
	  return Integer.valueOf(i).toString();
  }


  public static String getTimeStamp() {

	  Calendar calender = Calendar.getInstance();


	  String timestamp = toString(calender.get(Calendar.DAY_OF_MONTH))
	                   + "/"
	                   + toString(calender.get(Calendar.MONTH))
	                   + "/"
	                   + toString(calender.get(Calendar.YEAR))
	                   + " "
	                   + toString(calender.get(Calendar.HOUR))
	                   + ":"
	                   + formatter(calender.get(Calendar.MINUTE))
	                   + AMorPM(calender.get(Calendar.AM_PM));
	  return timestamp;
  }

}

