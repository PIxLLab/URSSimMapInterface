/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */

package wearable;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import urssimulationmapinterface.URSSimulationMapInterface;

import com.leapmotion.leap.Listener;


/**
 * A simple HelloWorld demo showing a simple speech application 
 * built using Sphinx-4. This application uses the Sphinx-4 endpointer,
 * which automatically segments incoming audio into utterances and silences.
 */

//Wearable_Seperate
public class voiceCommand extends Thread
{
 boolean voiceGate = false;
 boolean d1 = false;
 boolean d2 = false;
 boolean d3 = false;
 boolean d4 = false;
 boolean send = false;
 boolean search = false;
 int counter = 0;
 //URSSimulationMapInterface  ursInterface = new URSSimulationMapInterface();
 
 URSSimulationMapInterface  ursInterface;
 
 public voiceCommand(final URSSimulationMapInterface ursInterface)
 {
	 this.ursInterface = ursInterface;
 }
 
 
 /**
     * Main method for running the HelloWorld demo.
     */
    public  void runTest(){
        try {
            URL url;
          //  if (args.length > 0) {
            //    url = new File(args[0]).toURI().toURL();
            //} else {
                url = voiceCommand.class.getResource("helloworld.config.xml");
            //}

            //System.out.println("Loading...");

            ConfigurationManager cm = new ConfigurationManager(url);

	    Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
	    Microphone microphone = (Microphone) cm.lookup("microphone");


            /* allocate the resource necessary for the recognizer */
            recognizer.allocate();

            /* the microphone will keep recording until the program exits */
	    if (microphone.startRecording()) {

		

		while (true) {
		  //  System.out.println
		//	("Start speaking. Press Ctrl-C to quit.\n");
			System.out.println(counter++);
                    /*
                     * This method will return when the end of speech
                     * is reached. Note that the endpointer will determine
                     * the end of speech.
                     */ 
		    Result result = recognizer.recognize();
		    
		    if (result != null) {
			String resultText = result.getBestFinalResultNoFiller();
			//System.out.println("The Command: " + resultText + "\n");
			//break;
			if(resultText.equalsIgnoreCase("start voice"))
				{voiceGate = true;
				 System.out.println("Start Voice Command");
				}
			if(resultText.equalsIgnoreCase("end voice"))
				{
				voiceGate = false;
				System.out.println("Stop Voice Command");
				}
			while(voiceGate) {
			if(resultText.equalsIgnoreCase("select one"))
			{
				System.out.println("one is selected");
				d1 = true;
				d2 = false;
				d3 = false;
				d4 = false;
				send = false;
				search = false;
				break;
			}
			if(resultText.equalsIgnoreCase("select two"))
			{
				System.out.println("two is selected");
				d1 = false;
				d2 = true;
				d3 = false;
				d4 = false;
				send = false;
				search = false;
				break;
			}
			
			if(resultText.equalsIgnoreCase("select three"))
			{
				System.out.println("three is selected");
				d1 = false;
				d2 = false;
				d3 = true;
				d4 = false;
				send = false;
				search = false;
				break;
			}
			
			if(resultText.equalsIgnoreCase("select four"))
			{
				System.out.println("four is selected");
				d1 = false;
				d2 = false;
				d3 = false;
				d4 = true;
				send = false;
				search = false;
				break;
			}
			
			if(resultText.equalsIgnoreCase("send drone"))
			{
				System.out.println("send drone");
				send = true;
				search = false;
				break;
			}
			
			if(resultText.equalsIgnoreCase("search area"))
			{
				System.out.println("searach area");
				send = false;
				search = true;
				break;
			}
			
			else
			{
				send = false;
				search = false;
				break;
			}
			}
			}
		    else {
			System.out.println("I can't hear what you said.\n");
			break;
		    }
		}
	    } else {
		System.out.println("Cannot start microphone.");
		recognizer.deallocate();
		System.exit(1);
	    }
        } catch (IOException e) {
            System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Problem creating HelloWorld: " + e);
            e.printStackTrace();
        }
    }
    
    
	 public void voiceCommands()
	 {   
		 if(this.d1) 
		 { 
			 ursInterface.getdrone0Button().setEnabled(false);
			 ursInterface.getdrone1Button().setEnabled(true); 
			 ursInterface.getdrone2Button().setEnabled(true);
			 ursInterface.getdrone3Button().setEnabled(true); 
			 ursInterface.settheDroneId(0); 
		 } 
		 	
		 if(this.d2) 
		 {
			 ursInterface.getdrone0Button().setEnabled(true); 
			 ursInterface.getdrone1Button().setEnabled(false);
			 ursInterface.getdrone2Button().setEnabled(true); 
			 ursInterface.getdrone3Button().setEnabled(true); 
			 ursInterface.settheDroneId(1); 
		 }
		 
		 if(this.d3) 
		 { 
			 ursInterface.getdrone0Button().setEnabled(true); 
			 ursInterface.getdrone1Button().setEnabled(true);
			 ursInterface.getdrone2Button().setEnabled(false); 
			 ursInterface.getdrone3Button().setEnabled(true); 
			 ursInterface.settheDroneId(2);
		 } 
		 
		 if(this.d4) 
		 { 
			 ursInterface.getdrone0Button().setEnabled(true);
			 ursInterface.getdrone1Button().setEnabled(true); 
			 ursInterface.getdrone2Button().setEnabled(true);
			 ursInterface.getdrone3Button().setEnabled(false); 
			 ursInterface.settheDroneId(3); 
		 } 
		 if(this.send) 
		 {
			 ursInterface.add_location(ursInterface.gettheDroneId()); 
		 } 
		 
		 if(this.search) 
		 { 
			 ursInterface.search(); 
		 } 
	 }
	 
    
    public void run() {
        
        try {
        while(!Thread.interrupted())
        {
            
        	    runTest();
        	    //ursInterface.voiceCommands();
                sleep(10);
                System.out.println("*******");
        }
        } catch (Exception e) {
            e.printStackTrace();
            }
       }
}
