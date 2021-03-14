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
		if(ursinterface.getwearableInterface() == 1)
		{
			if (e.getKeyCode() == 16) 
			{
				System.out.println("16 is being pressed");
				controller.addListener(listener);
			
			}
		
			if (e.getKeyCode() == 17) 
			{
				System.out.println("17 is being pressed");
			
				if(ursinterface.getgestureInput().getText().equals("Land"))
				{
					ursinterface.droneLand(ursinterface.gettheDroneId());
				}
				if(ursinterface.getgestureInput().getText().equals("Send"))
				{
					ursinterface.add_location(ursinterface.gettheDroneId());
				}
				if(ursinterface.getgestureInput().getText().equals("Search"))
				{
					ursinterface.search();
				}
				if(ursinterface.getgestureInput().getText().equals("Low Altitude"))
				{
					ursinterface.add_location(ursinterface.gettheDroneId(), 15);
				}
				if(ursinterface.getgestureInput().getText().equals("High Altitude"))
				{
					ursinterface.add_location(ursinterface.gettheDroneId(), 25);
				}
			}
		

			if (e.getKeyCode() == 65) 
			{
				ursinterface.getdrone0Button().setEnabled(false);
				ursinterface.getdrone1Button().setEnabled(true);
				ursinterface.getdrone2Button().setEnabled(true);
				ursinterface.getdrone3Button().setEnabled(true);
				ursinterface.settheDroneId(0);
			}
			if (e.getKeyCode() == 66) 
			{
				ursinterface.getdrone0Button().setEnabled(true);
				ursinterface.getdrone1Button().setEnabled(false);
				ursinterface.getdrone2Button().setEnabled(true);
				ursinterface.getdrone3Button().setEnabled(true);
				ursinterface.settheDroneId(1);
			}
			if (e.getKeyCode() == 67) 
			{
				ursinterface.getdrone0Button().setEnabled(true);
				ursinterface.getdrone1Button().setEnabled(true);
				ursinterface.getdrone2Button().setEnabled(false);
				ursinterface.getdrone3Button().setEnabled(true);
				ursinterface.settheDroneId(2);
			}
			if (e.getKeyCode() == 68) 
			{
				ursinterface.getdrone0Button().setEnabled(true);
				ursinterface.getdrone1Button().setEnabled(true);
				ursinterface.getdrone2Button().setEnabled(true);
				ursinterface.getdrone3Button().setEnabled(false);
				ursinterface.settheDroneId(3);
			}
		}
		
		if (ursinterface.getwearableInterface() == 2)
		{
			if (e.getKeyCode() == 16) 
			{
				System.out.println("16 is being pressed");
				this.mData.testtheMyo();
				//gestureInput.setText(mData.gesture);
				if(this.mData.gesture.equals("REST"))
				{
					ursinterface.getgestureInput().setText("Land");
				}
				if(this.mData.gesture.equals("FIST"))
				{
					ursinterface.getgestureInput().setText("Send");
				}
				if(this.mData.gesture.equals("FINGERS_SPREAD"))
				{
					ursinterface.getgestureInput().setText("Search");
				}
				if(this.mData.gesture.equals("WAVE_IN"))
				{
					ursinterface.getgestureInput().setText("Low Altitude");
				}
				if(this.mData.gesture.equals("WAVE_OUT"))
				{
					ursinterface.getgestureInput().setText("High Altitude");
				}
			
				System.out.println(this.mData.gesture);
			}
			
			if (e.getKeyCode() == 17) 
			{
				System.out.println("17 is being pressed");
			
			if(ursinterface.getgestureInput().getText().equals("Land"))
			{
				System.out.println("test1");
				ursinterface.droneLand(ursinterface.gettheDroneId());
			}
			if(ursinterface.getgestureInput().getText().equals("Send"))
			{
				System.out.println("test2");
				ursinterface.add_location(ursinterface.gettheDroneId());
			}
			if(ursinterface.getgestureInput().getText().equals("Search"))
			{
				System.out.println("test3");
				ursinterface.search();
			}
			if(ursinterface.getgestureInput().getText().equals("Low Altitude"))
			{
				System.out.println("test4");
				ursinterface.add_location(ursinterface.gettheDroneId(), 15);
			}
			if(ursinterface.getgestureInput().getText().equals("High Altitude"))
			{
				System.out.println("test5");
				ursinterface.add_location(ursinterface.gettheDroneId(), 25);
			}
		}
			if (e.getKeyCode() == 65) 
			{
				ursinterface.getdrone0Button().setEnabled(false);
				ursinterface.getdrone1Button().setEnabled(true);
				ursinterface.getdrone2Button().setEnabled(true);
				ursinterface.getdrone3Button().setEnabled(true);
				ursinterface.settheDroneId(0);
			}
			
			if (e.getKeyCode() == 66) 
			{
				ursinterface.getdrone0Button().setEnabled(true);
				ursinterface.getdrone1Button().setEnabled(false);
				ursinterface.getdrone2Button().setEnabled(true);
				ursinterface.getdrone3Button().setEnabled(true);
				ursinterface.settheDroneId(1);
			}
			if (e.getKeyCode() == 67) 
			{
				ursinterface.getdrone0Button().setEnabled(true);
				ursinterface.getdrone1Button().setEnabled(true);
				ursinterface.getdrone2Button().setEnabled(false);
				ursinterface.getdrone3Button().setEnabled(true);
				ursinterface.settheDroneId(2);
			}
		
			if (e.getKeyCode() == 68) 
			{
				ursinterface.getdrone0Button().setEnabled(true);
				ursinterface.getdrone1Button().setEnabled(true);
				ursinterface.getdrone2Button().setEnabled(true);
				ursinterface.getdrone3Button().setEnabled(false);
				ursinterface.settheDroneId(3);
			}
		
		
		}
	
		if (ursinterface.getwearableInterface() == 3)
		{
		
			if (e.getKeyCode() == 83) 
			{
			
				ursinterface.getgestureInput().setText("Send");
		
			}
		
		
			if (e.getKeyCode() == 81) 
			{
			
				ursinterface.getgestureInput().setText("Search");
		
			}
		
			if (e.getKeyCode() == 76) 
			{
				ursinterface.getgestureInput().setText("Land");
		
			}
		
			if (e.getKeyCode() == 84) 
			{
				ursinterface.getgestureInput().setText("Low Altitude");
			}
		
		
			if (e.getKeyCode() == 72) 
			{
				ursinterface.getgestureInput().setText("High Altitude");
			}
		
			if (e.getKeyCode() == 8) 
			{
			//gestureInput.setText("High Altitude");
				if(ursinterface.getgestureInput().getText().equals("Land"))
			
				{
					System.out.println("test1");
					ursinterface.droneLand(ursinterface.gettheDroneId());
			
				}
			
				if(ursinterface.getgestureInput().getText().equals("Send"))
				{
					System.out.println("test2");
					ursinterface.add_location(ursinterface.gettheDroneId());
				}
			
				if(ursinterface.getgestureInput().getText().equals("Search"))
				{
				
					System.out.println("test3");
				
					ursinterface.search();
			
				}
			
				if(ursinterface.getgestureInput().getText().equals("Low Altitude"))
				{
					System.out.println("test4");
				
					ursinterface.add_location(ursinterface.gettheDroneId(), 15);
			
				}
				if(ursinterface.getgestureInput().getText().equals("High Altitude"))
				{
					System.out.println("test5");
					ursinterface.add_location(ursinterface.gettheDroneId(), 25);
				}
			}
		
		
			if (e.getKeyCode() == 65) 
			{
				ursinterface.getdrone0Button().setEnabled(false);
				ursinterface.getdrone1Button().setEnabled(true);
				ursinterface.getdrone2Button().setEnabled(true);
				ursinterface.getdrone3Button().setEnabled(true);
				ursinterface.settheDroneId(0);
			}
		
			if (e.getKeyCode() == 66) 
			{
			
				ursinterface.getdrone0Button().setEnabled(true);
				ursinterface.getdrone1Button().setEnabled(false);
				ursinterface.getdrone2Button().setEnabled(true);
				ursinterface.getdrone3Button().setEnabled(true);
				ursinterface.settheDroneId(1);
		
			}
		
			if (e.getKeyCode() == 67) 
			{
				ursinterface.getdrone0Button().setEnabled(true);
				ursinterface.getdrone1Button().setEnabled(true);
				ursinterface.getdrone2Button().setEnabled(false);
				ursinterface.getdrone3Button().setEnabled(true);
				ursinterface.settheDroneId(2);
			}
			
			if (e.getKeyCode() == 68) 
			{
				ursinterface.getdrone0Button().setEnabled(true);
				ursinterface.getdrone1Button().setEnabled(true);
				ursinterface.getdrone2Button().setEnabled(true);
				ursinterface.getdrone3Button().setEnabled(false);
				ursinterface.settheDroneId (3);
			}
		
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		// TODO Auto-generated method stub
		if(ursinterface.getwearableInterface() == 1)
		{
			if (e.getKeyCode() == 16) 
			{
				controller.removeListener(listener);
				
				if(listener.closeHand)
					ursinterface.getgestureInput().setText("Send");
				if(listener.openHand)
					ursinterface.getgestureInput().setText("Search");
				if(listener.oneFinger)
					ursinterface.getgestureInput().setText("Land");
				if(listener.twoFinger)
					ursinterface.getgestureInput().setText("Low Altitude");
				if(listener.threeFinger)
					ursinterface.getgestureInput().setText("High Altitude");
			}	
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
}
