package com.android.airpic;

import java.net.*;
import java.io.*;

import android.os.*;
import android.util.Log;
import android.content.res.*;


/**
 * Creates a socket to the server and
 * uploads a picture from the intent
 * @author chris
 */
public class FileServer
{
	private static final int BUFFER_SIZE = 35000;
	private static final int PORT_TO_CONNECT = 13267;
	private static final String URL_TO_CONNECT = "airpic.org";
	private static final String PATH_TO_IMAGE = "/images/airpic.jpg"; //this should not be hardcoded
	private static final String TAG = "Airpic.upload: ";

	public static void upload () throws IOException
	{
		String _path;

		Log.i( TAG, "making string..........." );
		_path = String.format( "%s%s", Environment.getExternalStorageDirectory(), PATH_TO_IMAGE );

		Log.i( TAG, "opening file..........." );
		File file=new File( _path.toString() );

		Socket sock = new Socket( URL_TO_CONNECT, PORT_TO_CONNECT );
		Log.i( TAG, "Connecting..........." );

		byte[] buffer = new byte[BUFFER_SIZE];
		BufferedOutputStream toServer = new BufferedOutputStream( sock.getOutputStream() );

		int size = 0;
		long start = System.currentTimeMillis();
		System.err.println( "UploadSender.send: Opened port "+ PORT_TO_CONNECT +" to server." );
		BufferedInputStream fIn = null;
		try
		{
			fIn = new BufferedInputStream( new FileInputStream(file) );
			int n = -1;
			while ( ( n=fIn.read(buffer) ) != -1 )
			{
				size += n;
				toServer.write(buffer, 0, n);
				toServer.flush();
			}
		}
		finally
		{
			if( null != fIn )
			{
				fIn.close();
			}
			sock.close();
		}
		long end = System.currentTimeMillis();
		System.err.println( "UploadSender.send: Done! Sent " + size + " bytes in " + ( end - start ) +" milliseconds" );

    }
}

