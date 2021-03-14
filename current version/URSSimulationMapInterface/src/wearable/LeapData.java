package wearable;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;

import urssimulationmapinterface.URSSimulationMapInterface;

//Wearable_Seperate
class LeapData extends Listener{
    	URSSimulationMapInterface  ursInterface = new URSSimulationMapInterface();
    	public float handAltitude = 0;
    	public String setDroneAlt= "2";
    	//boolean circle = false;
    	String userGesture = "";
    	boolean closeHand = false;
    	boolean openHand = false;
    	boolean oneFinger = false;
    	boolean twoFinger = false;
    	boolean threeFinger = false;
    	
    	public void onInit(Controller controller) {
    		System.out.println("init");
    	}
    	
    	public void onConnect(Controller controller) {
    		System.out.println("Connected to LM");
    		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
    		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
    		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
    		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    	}
    	public void onDisconnect(Controller controller) {
    		System.out.println("LM disconnected");
    	}
    	
    	public void onExit(Controller controller) {
    		System.out.println("Exited");
    	}
    	

    	public void onFrame(Controller controller) {
    		int counter = 0;
    		Frame frame = controller.frame();
    		//System.out.println(frame.fingers().count());
    	/*	for(Hand hand:frame.hands())
    		{
    			//HandList hand = frame.hands();
    			//System.out.println(hand.palmPosition().getY()/20);
    			handAltitude = hand.palmPosition().getY()/20;
    			setDroneAlt = Float.toString(handAltitude);
    			//System.out.println(setDroneAlt);
    		}*/
    		/*for(Gesture gesture:frame.gestures())
    		{
    			
    			if(gesture.type().equals(Gesture.Type.TYPE_CIRCLE))
    			{
    				System.out.println("CIRCLE");
    				circle = true;
    				closeHand = false;
    			}
    		}*/
    		for (Finger finger:frame.fingers().extended())
    		{
    			counter++;
    		}
    		if(counter==0)
    		{
    			System.out.println("Close Hand");
				closeHand = true;
				openHand = false;
				oneFinger = false;
				twoFinger = false;
				threeFinger = false;
				
    		}
    		if(counter==5)
    		{
    			System.out.println("open Hand");
				closeHand = false;
				openHand = true;
				oneFinger = false;
				twoFinger = false;
				threeFinger = false;
    		}
    		
    		if(counter==1)
    		{
    			System.out.println("one");
				closeHand = false;
				openHand = false;
				oneFinger = true;
				twoFinger = false;
				threeFinger = false;
				
    		}
    		if(counter==2)
    		{
    			System.out.println("two");
				closeHand = false;
				openHand = false;
				oneFinger = false;
				twoFinger = true;
				threeFinger = false;
				
    		}
    		if(counter==3)
    		{
    			System.out.println("three");
				closeHand = false;
				openHand = false;
				oneFinger = false;
				twoFinger = false;
				threeFinger = true;
				
    		}
    	}
    	
    }