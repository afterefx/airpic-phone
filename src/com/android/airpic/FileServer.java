/**
 * Copyright (c) 2010, Christopher Carlisle
 * 
 * This work is licensed under the Creative Commons 
 * Attribution-Noncommercial-No Derivative Works 3.0 United States 
 * License. To view a copy of this license, visit 
 * 
 * http://creativecommons.org/licenses/by-nc-nd/3.0/us/ 
 * 
 * or send a letter to Creative Commons, 171 Second Street, 
 * Suite 300, San Francisco, California, 94105, USA.
 */

package com.android.airpic;

import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec_1_4.binary.Base64;
//import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class FileServer 
{
	private static final String TAG = "Airpic.FileServer";
	private SharedPreferences prefs;
	private Context context;
	
	FileServer(final Context context)
	{
		this.context = context;
		Log.i(TAG + " context == ", context.toString());
	}
	
	public void upload() throws IOException
	{
    	Log.i(TAG + ".upload: ", "about to try to get shared prefs");
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		byte[] pictureData = readPictureData();
		byte[] pictureEncoded = Base64.encodeBase64(pictureData);
    	uploadImage(new String(pictureEncoded));
	}
	
    private void uploadImage(String pictureBase64) {
    	HttpClient httpClient = new DefaultHttpClient();  
    	HttpPost httpPost = new HttpPost("http://mobile.afterpeanuts.com/upload.php");
    	Log.i(this.getClass().getName(), "about to make a null thing");

    	String username = prefs.getString("username", "");
    	String apikey = prefs.getString("apikey", "");
    	try {  
    		List<NameValuePair> postContent = new ArrayList<NameValuePair>(2);  
    		//TODO add in api key and user key to check for upload auth
    		Log.i(TAG + ".uploadImage: Username == ", username);
    		Log.i(TAG + ".uploadImage: API Key == ", apikey);
    		postContent.add(new BasicNameValuePair("username", username));  
    		postContent.add(new BasicNameValuePair("key", apikey));  
    		postContent.add(new BasicNameValuePair("img", pictureBase64));  
    		httpPost.setEntity(new UrlEncodedFormEntity(postContent));  
    		httpPost.addHeader("Accept-Encoding", "html/xml");
    		
    		httpClient.execute(httpPost);  
    		//HttpResponse httpResponse = httpClient.execute(httpPost);  
    	} catch (ClientProtocolException e) {  
    		e.printStackTrace();
    	} catch (IOException e) {  
    		e.printStackTrace();
    	}  
    }
	
	private byte[] readPictureData()
	{
		Log.i(TAG + ".readPictureData: ", "making string...........");
		String _path = String.format("%s%s", Environment.getExternalStorageDirectory(), "/images/AirPicTest.jpg");
		
		Log.i(TAG + ".readPictureData: ", "opening file...........");
		File file=new File(_path.toString()); //open file
		
		Log.i(TAG + ".readPictureData: ", "made buffer...........");
			try {
			BufferedInputStream fIn = new BufferedInputStream(new FileInputStream(file));
			DataInputStream dataStream = new DataInputStream(fIn);

	        byte[] pictureData = new byte[dataStream.available()];
	        dataStream.read(pictureData);
	        
	        return pictureData;
			}
			catch (FileNotFoundException e) {
				Log.e(TAG + ".readPictureData: ", e.toString());
			} //reads file
			catch (IOException e) {
				Log.e(TAG + ".readPictureData: ", e.toString());
			}
        return null;
		
	}

}	
	
	
	
/*	
	public static void upload2 () throws IOException
	{
		String _path; //holds path for image to uplaod
		
		Log.i(TAG, "making string...........");
		_path = String.format("%s%s", Environment.getExternalStorageDirectory(), "/images/AirPicTest.jpg");
		
		Log.i(TAG, "opening file...........");
		File file=new File(_path.toString()); //open file
		
		Socket sock = new Socket("afterpeanuts.com", 13267); //create socket
		Log.i(TAG, "Connecting...........");
		
		byte[] buffer = new byte[35000];
		BufferedOutputStream toServer = new BufferedOutputStream(sock.getOutputStream());
		
		int size = 0;
		long start = System.currentTimeMillis();
		System.err.println("UploadSender.send: Opened port "+13267+" to server.");
		BufferedInputStream fIn = null; 
		try 
		{
			fIn = new BufferedInputStream(new FileInputStream(file)); //reads file
			int n = -1;
			while ( (n=fIn.read(buffer)) != -1 ) //read 1 byte
			{
				size += n;
				toServer.write(buffer, 0, n); //write to buffer
				toServer.flush();
			}
		} 
		finally 
		{
			if(fIn!=null) fIn.close();
				sock.close();
		}
		long end = System.currentTimeMillis();
		System.err.println("UploadSender.send: Done! Sent "+size+" bytes in "+(end-start)+" milliseconds");

    }
	
	public static void upload3() throws IOException
	{
		
		String _path; //holds path for image to uplaod
		
		Log.i(TAG, "making string...........");
		_path = String.format("%s%s", Environment.getExternalStorageDirectory(), "/images/AirPicTest.jpg");
		
		Log.i(TAG, "opening file...........");
		File file=new File(_path.toString()); //open file
		
		//create a new http client and post header
		HttpClient httpclient = new DefaultHttpClient();  
	    HttpPost httpPost = new HttpPost("http://mobile.afterpeanuts.com/upload.php"); 
	    
		byte[] buffer = new byte[35000];
		String fileToSend = "hello";
		
		int size = 0;
		long start = System.currentTimeMillis();
		//System.err.println("UploadSender.send: Opened port "+13267+" to server.");
		BufferedInputStream fIn = new BufferedInputStream(new FileInputStream(file)); //reads file
		try 
		{
			//fileToSend = fIn.toString();
			Log.i(TAG, fileToSend );
			int n = -1;
			while ( (n=fIn.read(buffer)) != -1 ) //read 1 byte
			{
				Log.i(TAG, "number of bytes [n]");
				Log.i(TAG, Integer.toString(n));
				//toServer.write(buffer, 0, n); //write to buffer
				size += n;
				//toServer.flush();
			}
		} 
		finally 
		{
			if(fIn!=null) fIn.close();
		}
		 try {  
				Log.i(TAG, "Buffer contains:");
				Log.i(TAG, buffer.toString());
				fileToSend = buffer.toString();
				Log.i(TAG, "filetosend contains:");
				Log.i(TAG, fileToSend);
		        // Add your data  
				Log.i(TAG, "making post...........");
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);  
		        nameValuePairs.add(new BasicNameValuePair("user", "12345"));  
		        nameValuePairs.add(new BasicNameValuePair("key", "12345"));  
		        nameValuePairs.add(new BasicNameValuePair("img", fileToSend));  
		        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
		  
		        // Execute HTTP Post Request  
		        HttpResponse response = httpclient.execute(httpPost);  
				Log.i(TAG, "sent post...........");
		        Log.i(TAG, "Response:");
		        Log.i(TAG, response.toString());
		          
		    } catch (ClientProtocolException e) {  
		    	Log.e(TAG, e.toString());
		        // TODO Auto-generated catch block  
		    } catch (IOException e) {  
		    	Log.e(TAG, e.toString());
		        // TODO Auto-generated catch block  
		    }  
		
		
		long end = System.currentTimeMillis();
		System.err.println("UploadSender.send: Done! Sent "+size+" bytes in "+(end-start)+" milliseconds");

    }*/


