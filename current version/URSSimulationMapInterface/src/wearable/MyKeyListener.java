package wearable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import urssimulationmapinterface.URSSimulationMapInterface;

import com.leapmotion.leap.Controller;

public class MyKeyListener implements KeyListener
{
	URSSimulationMapInterface ursinterface;
	MyoData mData = new MyoData();
	static LeapData listener = null;
	static Controller controller = null;
		
	public MyKeyListener(final URSSimulationMapInterface ursinterface)
	{
		this.ursinterface = ursinterface; 
		listener = new LeapData();
		controller = new Controller();
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		// TODO Auto-generated method stub
		if(ursinterface.wearableInterface == 1)
		{
			if (e.getKeyCode() == 16) 
			{
				System.out.println("16 is being pressed");
				controller.addListener(listener);
			
			}
		
			if (e.getKeyCode() == 17) 
			{
				System.out.println("17 is being pressed");
			
				if(ursinterface.gestureInput.getText().equals("Land"))
				{
					ursinterface.droneLand(ursinterface.theDroneId);
				}
				if(ursinterface.gestureInput.getText().equals("Send"))
				{
					ursinterface.add_location(ursinterface.theDroneId);
				}
				if(ursinterface.gestureInput.getText().equals("Search"))
				{
					ursinterface.search();
				}
				if(ursinterface.gestureInput.getText().equals("Low Altitude"))
				{
					ursinterface.add_location(ursinterface.theDroneId, 15);
				}
				if(ursinterface.gestureInput.getText().equals("High Altitude"))
				{
					ursinterface.add_location(ursinterface.theDroneId, 25);
				}
			}
		

			if (e.getKeyCode() == 65) 
			{
				ursinterface.drone0Button.setEnabled(false);
				ursinterface.drone1Button.setEnabled(true);
				ursinterface.drone2Button.setEnabled(true);
				ursinterface.drone3Button.setEnabled(true);
				ursinterface.theDroneId = 0;
			}
			if (e.getKeyCode() == 66) 
			{
				ursinterface.drone0Button.setEnabled(true);
				ursinterface.drone1Button.setEnabled(false);
				ursinterface.drone2Button.setEnabled(true);
				ursinterface.drone3Button.setEnabled(true);
				ursinterface.theDroneId = 1;
			}
			if (e.getKeyCode() == 67) 
			{
				ursinterface.drone0Button.setEnabled(true);
				ursinterface.drone1Button.setEnabled(true);
				ursinterface.drone2Button.setEnabled(false);
				ursinterface.drone3Button.setEnabled(true);
				ursinterface.theDroneId = 2;
			}
			if (e.getKeyCode() == 68) 
			{
				ursinterface.drone0Button.setEnabled(true);
				ursinterface.drone1Button.setEnabled(true);
				ursinterface.drone2Button.setEnabled(true);
				ursinterface.drone3Button.setEnabled(false);
				ursinterface.theDroneId = 3;
			}
		}
		
		if (ursinterface.wearableInterface == 2)
		{
			if (e.getKeyCode() == 16) 
			{
				System.out.println("16 is being pressed");
				this.mData.testtheMyo();
				//gestureInput.setText(mData.gesture);
				if(this.mData.gesture.equals("REST"))
				{
					ursinterface.gestureInput.setText("Land");
				}
				if(this.mData.gesture.equals("FIST"))
				{
					ursinterface.gestureInput.setText("Send");
				}
				if(this.mData.gesture.equals("FINGERS_SPREAD"))
				{
					ursinterface.gestureInput.setText("Search");
				}
				if(this.mData.gesture.equals("WAVE_IN"))
				{
					ursinterface.gestureInput.setText("Low Altitude");
				}
				if(this.mData.gesture.equals("WAVE_OUT"))
				{
					ursinterface.gestureInput.setText("High Altitude");
				}
			
				System.out.println(this.mData.gesture);
			}
			
			if (e.getKeyCode() == 17) 
			{
				System.out.println("17 is being pressed");
			
			if(ursinterface.gestureInput.getText().equals("Land"))
			{
				System.out.println("test1");
				ursinterface.droneLand(ursinterface.theDroneId);
			}
			if(ursinterface.gestureInput.getText().equals("Send"))
			{
				System.out.println("test2");
				ursinterface.add_location(ursinterface.theDroneId);
			}
			if(ursinterface.gestureInput.getText().equals("Search"))
			{
				System.out.println("test3");
				ursinterface.search();
			}
			if(ursinterface.gestureInput.getText().equals("Low Altitude"))
			{
				System.out.println("test4");
				ursinterface.add_location(ursinterface.theDroneId, 15);
			}
			if(ursinterface.gestureInput.getText().equals("High Altitude"))
			{
				System.out.println("test5");
				ursinterface.add_location(ursinterface.theDroneId, 25);
			}
		}
			if (e.getKeyCode() == 65) 
			{
				ursinterface.drone0Button.setEnabled(false);
				ursinterface.drone1Button.setEnabled(true);
				ursinterface.drone2Button.setEnabled(true);
				ursinterface.drone3Button.setEnabled(true);
				ursinterface.theDroneId = 0;
			}
			
			if (e.getKeyCode() == 66) 
			{
				ursinterface.drone0Button.setEnabled(true);
				ursinterface.drone1Button.setEnabled(false);
				ursinterface.drone2Button.setEnabled(true);
				ursinterface.drone3Button.setEnabled(true);
				ursinterface.theDroneId = 1;
			}
			if (e.getKeyCode() == 67) 
			{
				ursinterface.drone0Button.setEnabled(true);
				ursinterface.drone1Button.setEnabled(true);
				ursinterface.drone2Button.setEnabled(false);
				ursinterface.drone3Button.setEnabled(true);
				ursinterface.theDroneId = 2;
			}
		
			if (e.getKeyCode() == 68) 
			{
				ursinterface.drone0Button.setEnabled(true);
				ursinterface.drone1Button.setEnabled(true);
				ursinterface.drone2Button.setEnabled(true);
				ursinterface.drone3Button.setEnabled(false);
				ursinterface.theDroneId = 3;
			}
		
		
		}
	
		if (ursinterface.wearableInterface==3)
		{
		
			if (e.getKeyCode() == 83) 
			{
			
				ursinterface.gestureInput.setText("Send");
		
			}
		
		
			if (e.getKeyCode() == 81) 
			{
			
				ursinterface.gestureInput.setText("Search");
		
			}
		
			if (e.getKeyCode() == 76) 
			{
				ursinterface.gestureInput.setText("Land");
		
			}
		
			if (e.getKeyCode() == 84) 
			{
				ursinterface.gestureInput.setText("Low Altitude");
			}
		
		
			if (e.getKeyCode() == 72) 
			{
				ursinterface.gestureInput.setText("High Altitude");
			}
		
			if (e.getKeyCode() == 8) 
			{
			//gestureInput.setText("High Altitude");
				if(ursinterface.gestureInput.getText().equals("Land"))
			
				{
					System.out.println("test1");
					ursinterface.droneLand(ursinterface.theDroneId);
			
				}
			
				if(ursinterface.gestureInput.getText().equals("Send"))
				{
					System.out.println("test2");
					ursinterface.add_location(ursinterface.theDroneId);
				}
			
				if(ursinterface.gestureInput.getText().equals("Search"))
				{
				
					System.out.println("test3");
				
					ursinterface.search();
			
				}
			
				if(ursinterface.gestureInput.getText().equals("Low Altitude"))
			
				{
				
					System.out.println("test4");
				
					ursinterface.add_location(ursinterface.theDroneId, 15);
			
				}
				if(ursinterface.gestureInput.getText().equals("High Altitude"))
				{
					System.out.println("test5");
					ursinterface.add_location(ursinterface.theDroneId, 25);
				}
			}
		
		
			if (e.getKeyCode() == 65) 
			{
				ursinterface.drone0Button.setEnabled(false);
			
				ursinterface.drone1Button.setEnabled(true);
			
				ursinterface.drone2Button.setEnabled(true);
			
				ursinterface.drone3Button.setEnabled(true);
			
				ursinterface.theDroneId = 0;
			}
		
			if (e.getKeyCode() == 66) 
			{
			
				ursinterface.drone0Button.setEnabled(true);
			
				ursinterface.drone1Button.setEnabled(false);
			
				ursinterface.drone2Button.setEnabled(true);
			
				ursinterface.drone3Button.setEnabled(true);
			
				ursinterface.theDroneId = 1;
		
			}
		
			if (e.getKeyCode() == 67) 
			{
				ursinterface.drone0Button.setEnabled(true);
			
				ursinterface.drone1Button.setEnabled(true);
			
				ursinterface.drone2Button.setEnabled(false);
			
				ursinterface.drone3Button.setEnabled(true);
			
				ursinterface.theDroneId = 2;
			}
			
			if (e.getKeyCode() == 68) 
			{
				ursinterface.drone0Button.setEnabled(true);
				ursinterface.drone1Button.setEnabled(true);
				ursinterface.drone2Button.setEnabled(true);
				ursinterface.drone3Button.setEnabled(false);
				ursinterface.theDroneId = 3;
			}
		
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		// TODO Auto-generated method stub
		if(ursinterface.wearableInterface==1)
		{
			if (e.getKeyCode() == 16) 
			{
				controller.removeListener(listener);
				
				if(listener.closeHand)
					ursinterface.gestureInput.setText("Send");
				if(listener.openHand)
					ursinterface.gestureInput.setText("Search");
				if(listener.oneFinger)
					ursinterface.gestureInput.setText("Land");
				if(listener.twoFinger)
					ursinterface.gestureInput.setText("Low Altitude");
				if(listener.threeFinger)
					ursinterface.gestureInput.setText("High Altitude");
			}	
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
}
