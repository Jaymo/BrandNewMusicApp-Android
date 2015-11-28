package com.myitprovider.bnm.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;

import com.myitprovider.bnm.BNMfragmentHost;
import com.myitprovider.bnm.R;



public class Util {
	private static NetworkInfo networkInfo;
	private static final String VALID_EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
        + "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	private static final String VALID_PHONE_NUMBER = "^$";
	
	private static Pattern pattern;
    private static Matcher matcher;
	/**
     * Kuna internet connection?
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo = connectivity.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;

    }
    /**
     * Validates an email address Credits:
     * http://www.mkyong.com/regular-expressions
     * /how-to-validate-email-address-with-regular-expression/
     * 
     * @param String - email address to be validated
     * @return boolean
     */
    public static boolean validateEmail(String emailAddress) {
        if (!emailAddress.equals("")) {
            pattern = Pattern.compile(VALID_EMAIL_PATTERN);
            matcher = pattern.matcher(emailAddress);
            return matcher.matches();
        }
        return true;
    }
    
    public static boolean validatePhone(String phonenumber) {
        if (!phonenumber.equals("")) {
            pattern = Pattern.compile(VALID_PHONE_NUMBER);
            matcher = pattern.matcher(phonenumber);
            return matcher.matches();
        }
        return true;
    }
    /**
    *@param Context - appContext
    *Credits Ahmed Maaway Noma sana
    *Requires READ_PHONE_STATE
    */
    
    public static class CheckinUtil {
        public static String IMEI(Context appContext) {
            TelephonyManager TelephonyMgr = (TelephonyManager)appContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyMgr.getDeviceId();  
        }
    
    }
    
    /**
     * I googled this piece of code to help me solve the weird date 
     * that one gets from Messages in Android.. hii code si yangu
     * http://stackoverflow.com/questions/6596004/gettin-date-from-a-sms-message-android
     * @param long currentTime
     * 
     * Server conversent Format:
     */
    public static String millisToDate(long currentTime) {
        String finalDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        Date date = (Date) calendar.getTime();
        finalDate = date.toString();
        return finalDate;
    }
    
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024; //1MB
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";
 
        // Convert total duration into time
           int hours = (int)( milliseconds / (1000*60*60));
           int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
           int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
           // Add hours if there
           if(hours > 0){
               finalTimerString = hours + ":";
           }
 
           // Prepending 0 to seconds if it is one digit
           if(seconds < 10){
               secondsString = "0" + seconds;
           }else{
               secondsString = "" + seconds;}
 
           finalTimerString = finalTimerString + minutes + ":" + secondsString;
 
        // return timer string
        return finalTimerString;
    }
 
    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;
 
        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
 
        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;
 
        // return percentage
        return percentage.intValue();
    }
 
    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);
 
        // return current duration in milliseconds
        return currentDuration * 1000;
    }
    
    /**
     * Issues a welcome notification to inform the user of BNM.
     * @param Context context
     * @param String message
     * @return
     */
    public static  void generateNotification(Context context, String message ) {

   	    Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
        int smalIcon =R.drawable.notification_large;
        long when = System.currentTimeMillis();
        String title="Brand New Music";
        String ticker ="Brand New Music Notication!";
        
        
        Intent intent =new Intent(context, BNMfragmentHost.class);  
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)	
				.setContentTitle(title)
				.setContentText(message)
				.setTicker(ticker)
				.setWhen(when)
				.setLargeIcon(icon)	
				.setSmallIcon(smalIcon)
				.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND)
				.setContentIntent(pendingIntent);
		
		Notification notification=notificationBuilder.build();
		notificationManager.notify((int) when, notification);
		
       
  }
    
    /**
     * Issues an Update Notification
     * @param Context context
     * @param String message
     * @return
     */
    public static  void generateUpdateNotification(Context context, String message ) {
    	
    	
    	 Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
         int smalIcon =R.drawable.notification_large;
         long when = System.currentTimeMillis();
         String title="BNM App Update";
         String ticker ="Brand New Music Notication!";
         
         
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.myitprovider.bnm" )); 
 		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
 		NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
 		
 		
 		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)	
 				.setContentTitle(title)
 				.setContentText(message)
 				.setTicker(ticker)
 				.setWhen(when)
 				.setSmallIcon(smalIcon)
 				.setLargeIcon(icon)	
 				.setAutoCancel(true)
 				.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND)
 				.setContentIntent(pendingIntent);
 		
 		Notification notification=notificationBuilder.build();
 		notificationManager.notify((int) when, notification);
        
          

    }
  
 }
