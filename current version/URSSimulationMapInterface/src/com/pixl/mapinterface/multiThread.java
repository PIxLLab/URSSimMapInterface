package com.pixl.mapinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class multiThread extends Thread {
	Double userLon1;
	Double userLat1;
	Socket socket1;
    //URSSimulationMapInterface xxx= new URSSimulationMapInterface();
	public  void readNMEAMsg() {
	       	try {
		//Socket socket1 = new Socket("192.168.2.2",11000);
		
		//if(socket1.isConnected())
		InputStream ois = socket1.getInputStream();
		//System.out.println(ois.toString());   
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(ois));
	   //String out = "";//new StringBuilder();
	   String line;
	   
	   while (true)//((line = reader.readLine()) != null) {
	   	{
		   
	   	    line = reader.readLine(); 
	   	    if(line == null)
	   	    	break;
	        String test1 = line.toString();
	        String[] test2 = test1.split(",");
	        //ursinterface2.updateUserLocation();
	        //if(test2[0].equals("$GPGGA")||test2[0].equals("$GPGGL")||test2[0].equals("$GPRMC"))
	       // {
	          //xxx.altitudeGate = true;
	        	System.out.println(test1);
	        //System.out.println("Just test:ppp " + xxx.altitudeGate);
	        NMEA nmea = new NMEA();
	        nmea.parse(test1);
	        String[] parts = nmea.position.toString().split(",");
	        String[] part1 = parts[0].trim().split(":");
	         userLat1 = Double.parseDouble(part1[2]);
	        String[] part2 = parts[1].trim().split(":");
	         userLon1 = Double.parseDouble(part2[1]);
	        
	        break;
	       // }
	   }
	   
	       	}
	       	
	       	catch (Exception e){
	       		e.printStackTrace();
	       	}
		 
		 }
public void run() {
    
    try {
    while(!Thread.interrupted())
    {
        
    	    readNMEAMsg();
            sleep(2000);
    }
    } catch (Exception e) {
        e.printStackTrace();
        }
   }

public void start()
{
	try {
		//socket1 = new Socket("192.168.2.2",11000);
		socket1 = new Socket("172.24.109.70",11000);
		run();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}



}