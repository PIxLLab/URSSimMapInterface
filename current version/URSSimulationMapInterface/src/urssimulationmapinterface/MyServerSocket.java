package urssimulationmapinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyServerSocket {
	public String test1 = "";
	public Socket socket = null;
	
    
	public  void readMsg() throws IOException {
		 try {
	        	socket = new Socket("192.168.2.3",11000);
	        	System.out.println("Connection established");
	        	
    	InputStream ois = socket.getInputStream();
    	System.out.println(ois.toString());   
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(ois));
        //String out = "";//new StringBuilder();
        String line;
        
        while (true)//((line = reader.readLine()) != null) {
        	{
        	line = reader.readLine();
            //out.append(line);
            
             test1 = line.toString();
             //System.out.println(test1.toString());
             String[] test2 = test1.split(",");
             if(test2[0].equals("$GPGGA"))
             {
             NMEA nmea = new NMEA();
             nmea.parse(test1);
             System.out.println(nmea.position);
             break;
             }
        }
        ois.close();
        socket.close();
		 }
        catch (IOException e) {
            e.printStackTrace();
        }
		 }
       // System.out.println("test" + out.toString());   //Prints the string content read from input stream
        //reader.close();
        //return test1;
    public void SocketConnection(){

        try {
        	socket = new Socket("192.168.2.3",11000);
        	System.out.println("Connection established");
        }   

        catch (IOException e) {
            e.printStackTrace();
        }

    } //....End of Socket Connection Function....//
    

    public static void main(String[] args) throws Exception {
        MyServerSocket app = new MyServerSocket();
        app.SocketConnection();
        app.readMsg();
        
  
        
        

    }
}
