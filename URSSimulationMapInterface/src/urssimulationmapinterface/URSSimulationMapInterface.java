package urssimulationmapinterface;
import gov.nasa.worldwind.*;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.event.SelectEvent;
import java.util.ArrayList;
import java.util.ListIterator;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.LineBuilder;

import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import gov.nasa.worldwind.layers.RenderableLayer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import pb_wearable.Wearable.WearableRequest;
import pb_wearable.Wearable.WearableRequest.SetDestRepeated;
import pb_wearable.Wearable.WearableResponse;
//import pb_wearable.Wearable.SetDestRepeated;
import pb_wearable.Wearable.WearableResponse.WearableResponseType;

import javax.imageio.ImageIO;
//import LineBuilder.LinePanel;
import javax.swing.*;

/**
 * @author Skyforce
 */
public class URSSimulationMapInterface extends ApplicationTemplate{
	static boolean LBArm = false;
	static int droneid;
	static double dronelatitude = 32.2771;
	static double dronelongitude = -106.7201;
	static double droneelevation = 20.00;
	static InetAddress host = null;
	static Socket socket =null;
	static double gazebox0 = -50.00;
	static double gazebox1 = 50.00;
	static double gazeboy0 = -50.00;
	static double gazeboy1 = 50.00;
	static double lon = -106.7500946, lat = 32.2787745, droneElevation = 1d; // drone location, elevation is in meters
	static double elevationOffset=2.5e3d; // camera height from surface (meters)
	
	private static class LinePanel extends JPanel{
		private static final long serialVersionUID = 1L;
		private final WorldWindow wwd;
		private final LineBuilder lineBuilder;
		private JButton newButton;
		private JButton pauseButton;
		private JButton endButton;
		private JButton sendButton;
		private JButton picButton;
		private JLabel[] pointLabels;
	    
		public LinePanel(WorldWindow wwd, LineBuilder lineBuilder)
		{
			super(new BorderLayout());
			this.wwd = wwd;
			this.lineBuilder = lineBuilder;
			this.makePanel(new Dimension(200,400));

			lineBuilder.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent propertyChangeEvent)
				{
					fillPointsPanel();
				}
			});
		}
		//--------- Includes button and line panel functionalities ----------//
		private void makePanel(Dimension size)
		{
			JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));   //...For Holding the Line Buttons...//
			JPanel buttonPanel1 = new JPanel(new GridLayout(1, 4, 2, 2)); //...For Holding the Command Buttons...//
			
			//-------------------- New Button --------------------//
			newButton = new JButton("New");
			newButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent actionEvent)
				{
					lineBuilder.clear();
					lineBuilder.setArmed(true);
					pauseButton.setText("Pause");
					pauseButton.setEnabled(true);
					endButton.setEnabled(true);
					sendButton.setEnabled(true);
					newButton.setEnabled(false);
					((Component) wwd).setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				}
			});
			buttonPanel.add(newButton);
			newButton.setEnabled(true);
			//-------------------- Pause Button --------------------//
			pauseButton = new JButton("Pause");
			pauseButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent actionEvent)
				{
					lineBuilder.setArmed(!lineBuilder.isArmed());
					pauseButton.setText(!lineBuilder.isArmed() ? "Resume" : "Pause");
					((Component) wwd).setCursor(Cursor.getDefaultCursor());
				}
			});
			buttonPanel.add(pauseButton);
			pauseButton.setEnabled(false);
			//-------------------- End Button --------------------//
			endButton = new JButton("End");
			endButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent actionEvent)
				{
					lineBuilder.setArmed(false);
					newButton.setEnabled(true);
					pauseButton.setEnabled(false);
					pauseButton.setText("Pause");
					endButton.setEnabled(false);
					((Component) wwd).setCursor(Cursor.getDefaultCursor());
				}
			});
			buttonPanel.add(endButton);
			endButton.setEnabled(false);
			//-------------------- Send Button --------------------//
			sendButton = new JButton ("Send");
			
			sendButton.addActionListener( new ActionListener()
			{
				//.....Connects to the exec_monitor.....//
				public void actionPerformed(ActionEvent actionEvent) {

					try {
						SocketConnection(); //.....Calling the Socket Connection Method....//
						OutputStream oos = socket.getOutputStream();
						System.out.println("......Communication Starts.......");
						//sending request for getting Gazebo region
						WearableRequest.Builder objgotorequest = WearableRequest.newBuilder();           
						objgotorequest.setType(WearableRequest.WearableRequestType.GET_REGION);
						System.out.println("......Sending Data.......");
						System.out.println(objgotorequest);
						objgotorequest.build().writeDelimitedTo(oos);
						//Receiving Gazebo region 
						System.out.println("......Receiving Data.......");
						InputStream ois = socket.getInputStream();
						WearableResponse objgotoresponse = null;
						objgotoresponse = WearableResponse.parseDelimitedFrom(ois);
						System.out.println(objgotoresponse.getType());
						

						if (objgotoresponse.getType()==WearableResponseType.REGION) {
							System.out.println("inside if means received region");
							 gazebox0=objgotoresponse.getRegion().getX0();
							 gazebox1=objgotoresponse.getRegion().getX1();
							 gazeboy0=objgotoresponse.getRegion().getY0();
							 gazeboy1=objgotoresponse.getRegion().getY1();
						}
						//Translating the ww coordinates to gazebo            
						double wwx0=32.2833;
						double wwx1=32.2743;		
						double pinx=dronelatitude;                  
						double Xsim =(((pinx - wwx0)/(wwx1 - wwx0))*(gazebox1 - gazebox0))+gazebox0;
						System.out.println("Pin Latitude in WW: "+pinx);
						System.out.println("Pin Longitude in Gazebo: "+Xsim);            
						double wwy0=-106.7557;
						double wwy1=-106.7447;		
						double piny=dronelongitude;                  
						double Ysim =(((piny - wwy0)/(wwy1 - wwy0))*(gazeboy1 - gazeboy0))+gazeboy0;
						System.out.println("Pin Longitude in WW: "+piny);
						System.out.println("Pin Longitude in Gazebo: "+Ysim);           
						System.out.println();
						//sending translated pin coordinates 
						System.out.println("......Sending Data.......");
						WearableRequest.Builder objrequest = WearableRequest.newBuilder();
						objrequest.setType(WearableRequest.WearableRequestType.SET_DEST_REPEATED);
						objrequest.setSetDestRepeated(SetDestRepeated.newBuilder().addSetDest(SetDestRepeated.SetDest.newBuilder().setUavId(3).setX(Xsim).setY(Ysim).setZ(droneelevation)));
						
//						objrequest.setSetDestRepeated(SetDestRepeated.newBuilder()
//								.addSetDest(SetDestRepeated.SetDest.newBuilder()
//										.setUavId(droneid)
//										.setX(Xsim)
//										.setY(Ysim)
//										.setZ(droneelevation)));
						
						System.out.println(objrequest);
						objrequest.build().writeDelimitedTo(oos);
				
						socket.close(); // ....Closing the Socket....//
						System.out.println("......Communication Ends.......");
					    SocketConnection(); //....Calling the Socket Connection Method Again for a Continuous Connection...//
						
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			buttonPanel1.add(sendButton);
			sendButton.setEnabled(true);
			
			//.......Picture Button.....//
			picButton = new JButton("Picture");
			
			//......Taking the Screen Shot................//
			picButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					try {
						Robot robot = new Robot();
						BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
						ImageIO.write(screenShot, "JPG", new File("ScreenShot.jpg")); //....Saving the Image into the Root Directory of the Project....//
						JOptionPane.showMessageDialog(null,"Screen Shot has been Taken and Saved.....!!!","Message Box", JOptionPane.INFORMATION_MESSAGE);
						
					} 
					catch (IOException | AWTException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			});
			
			
			buttonPanel1.add(picButton);
			picButton.setEnabled(true);
			
			
			//--------- Line Panel settings: Point Panel shows pin line coordinates on the line panel ----------//
			JPanel pointPanel = new JPanel(new GridLayout(0, 1, 0, 10));
			pointPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			this.pointLabels = new JLabel[20];
			for (int i = 0; i < this.pointLabels.length; i++)
			{
				this.pointLabels[i] = new JLabel("");
				pointPanel.add(this.pointLabels[i]);
			}
			// Put the point panel in a container to prevent scroll panel from stretching the vertical spacing.
			JPanel dummyPanel = new JPanel(new BorderLayout());
			dummyPanel.add(pointPanel, BorderLayout.NORTH);
			// Put the point panel in a scroll bar.
			JScrollPane scrollPane = new JScrollPane(dummyPanel);
			scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			if (size != null)
				scrollPane.setPreferredSize(size);
			
			// Add the buttons, scroll bar and inner panel to a titled panel that will resize with the main window.
			
			//.......Main Panel, Which Holds All the Panels....//
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			
			//....Panel for Line Controlling........//
			JPanel outerPanel = new JPanel(new BorderLayout());
			outerPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9), new TitledBorder("Line")));
			outerPanel.setToolTipText("Line control and info");
			outerPanel.add(buttonPanel, BorderLayout.NORTH);
			//outerPanel.add(scrollPane, BorderLayout.CENTER);
			
			//.....Panel for Command Works.....//
			JPanel commandPanel = new JPanel(new BorderLayout());
			commandPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9,9,0,9), new TitledBorder("Command")));
			commandPanel.setToolTipText("Command Info");
			commandPanel.add(buttonPanel1,BorderLayout.NORTH);
			
			//....Panel for Message Displaying......//
			JPanel scrollPanel= new JPanel(new BorderLayout());
			scrollPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9,9,400,9), new TitledBorder("Message Log")));
			scrollPanel.setToolTipText("Messages");
			scrollPanel.add(scrollPane, BorderLayout.CENTER);
			
			
			//....Adding All Panels to the Main Panel.......//
			mainPanel.add(outerPanel, BorderLayout.CENTER);
			mainPanel.add(commandPanel,BorderLayout.CENTER);
			mainPanel.add(scrollPanel,BorderLayout.CENTER);
			
			//....Adding Main Panel to the JFrame....//
			this.add(mainPanel);
			
			//this.add(outerPanel, BorderLayout.CENTER);
			//this.add(commandPanel, BorderLayout.AFTER_LINE_ENDS);
		}
		//--------- Feed Point Panel shows pin line coordinates on the line panel ----------//
		private void fillPointsPanel()
		{
			int i = 0;
			for (Position pos : lineBuilder.getLine().getPositions())
			{
				if (i == this.pointLabels.length)
					break;
				String las = String.format("Lat %7.4f\u00B0", pos.getLatitude().getDegrees());
				String los = String.format("Lon %7.4f\u00B0", pos.getLongitude().getDegrees());
				pointLabels[i++].setText(las + " " + los);
			}
			for (; i < this.pointLabels.length; i++)
				pointLabels[i++].setText("");
		}
	}//....End of Class LinePanel...//

	private static class AppFrame extends ApplicationTemplate.AppFrame //javax.swing.JFrame
	{
		private static final MarkerAttributes[] attrs = new BasicMarkerAttributes[]
				{
						new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.SPHERE, 1d, 10, 5)
				};
		private int tmpdroneid=0;
		private Marker lastHighlit;
		private BasicMarkerAttributes lastAttrs;

		@SuppressWarnings("deprecation")
		public AppFrame()
		{
				
			super(true, true, false);
			
			// create the markers for the drones
			ArrayList<Marker> markers = new ArrayList<Marker>();		
			//........Pointing out only yellow drones.........//
			Marker marker = new BasicMarker(Position.fromDegrees(lat, lon, droneElevation), attrs[0]);
			marker.setPosition(Position.fromDegrees(lat, lon, droneElevation));
			marker.setHeading(Angle.fromDegrees(0));
			marker.setPitch(Angle.fromDegrees(90));
			markers.add(marker);
			//........ Getting the IDs of the Drone ........//								
			for (Marker tmpmarker : markers) { 		      
				droneid = tmpdroneid;
				tmpdroneid++;
			} 
			
			final MarkerLayer layer = new MarkerLayer();
			//........... Highlight selected drones ..........//
			this.getWwd().addSelectListener(new SelectListener()
			{
				public void selected(SelectEvent event)
				{
					if (lastHighlit != null && (event.getTopObject() == null || !event.getTopObject().equals(lastHighlit)))
					{
						lastHighlit.setAttributes(lastAttrs);
						lastHighlit = null;
					}

					if (!event.getEventAction().equals(SelectEvent.ROLLOVER))
						return;

					if (event.getTopObject() == null || event.getTopPickedObject().getParentLayer() == null)
						return;

					if (event.getTopPickedObject().getParentLayer() != layer)
						return;

					if (lastHighlit == null && event.getTopObject() instanceof Marker)
					{
						lastHighlit = (Marker) event.getTopObject();
						lastAttrs = (BasicMarkerAttributes) lastHighlit.getAttributes();
						MarkerAttributes highliteAttrs = new BasicMarkerAttributes(lastAttrs);
						highliteAttrs.setMaterial(Material.WHITE);
						highliteAttrs.setOpacity(1d);
						highliteAttrs.setMarkerPixels(lastAttrs.getMarkerPixels() * 1.4);
						highliteAttrs.setMinMarkerSize(lastAttrs.getMinMarkerSize() * 1.4);
						
						
						lastHighlit.setAttributes(highliteAttrs);
					}
				}
			});
			// .......... Get the coordinates of the selected drone ..........//
			this.getWwd().addSelectListener(new SelectListener()
			{
				public void selected(SelectEvent event)
				{
					if (event.getTopObject() != null)
					{					
						if (event.getTopPickedObject().getParentLayer() instanceof MarkerLayer)
						{
							PickedObject po = event.getTopPickedObject();				
							dronelatitude = po.getPosition().getLatitude().degrees;   //.....Drone Latitude...//
							dronelongitude = po.getPosition().getLongitude().degrees; //... Drone Longitude...//
							droneelevation = po.getPosition().getElevation(); //.....Drone Elevation...//
							System.out.printf("Track position %s, %s\n",po.getValue(AVKey.PICKED_OBJECT_ID).toString(),po.getPosition());

						}
					}
				}
			});
			//............Restrict the area .............//
			double minlat=32.284841;
			double maxlat = 32.270353 ;
			double minlon = -106.761522 ;
			double maxlon = -106.736765;
			// .......... Add new pin by LEFT_CLICK .........//
			this.getWwd().getInputHandler().addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent mouseEvent)
				{
					if(mouseEvent.getButton() == MouseEvent.BUTTON1)
					{
						Position targetPos = getWwd().getCurrentPosition();
						double lat = targetPos.latitude.degrees;
						double lon = targetPos.longitude.degrees;				
						if(lat>minlat || lat < maxlat || lon < minlon || lon > maxlon){
							System.out.printf("New Pin OUT of permitted region, try again within the range of NMSU!\n");
						} else {
							System.out.printf("New Pin ADDED!\n");

//							Marker marker = new BasicMarker(Position.fromDegrees(lat, lon, droneElevation), attrs[1]);
//							marker.setPosition(Position.fromDegrees(lat, lon, droneElevation));
//							marker.setHeading(Angle.fromDegrees(0));
//							marker.setPitch(Angle.fromDegrees(90));

							final RenderableLayer layer = new RenderableLayer();
							PointPlacemark pp = new PointPlacemark(Position.fromDegrees(lat, lon, targetPos.getElevation() + droneElevation));
							//pp.setValue(AVKey.DISPLAY_NAME, "Absolute, Label, Red pin icon, Line in random color and 2 wide");
							//pp.setLineEnabled(true);
							//pp.setAltitudeMode(WorldWind.ABSOLUTE);													
							PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
							attrs = new PointPlacemarkAttributes();
							attrs.setScale(0.6); // pin size
							attrs.setImageOffset(new Offset(19d, 8d, AVKey.PIXELS, AVKey.PIXELS));
							attrs.setLabelOffset(new Offset(0.9d, 0.6d, AVKey.FRACTION, AVKey.FRACTION));
							//attrs.setLineMaterial(new Material(WWUtil.makeRandomColor(null)));
							attrs.setLineWidth(2d);
							attrs.setImageAddress("images/pushpins/plain-red.png");						
							pp.setAttributes(attrs);
							layer.addRenderable(pp);

							// Add the layer to the model.
							insertBeforeCompass(getWwd(), layer);
						//	markers.add(marker);

							View view = getWwd().getView();
							// Use a PanToIterator to iterate view to target position
							if(view != null)
							{
								// The elevation component of 'targetPos' here is not the surface elevation,
								// so we ignore it when specifying the view center position.
								//view.goTo(new Position(targetPos, 0), targetPos.getElevation() + elevationOffset);
							}
						}
					}
				}
			});
			
			//.......... Adjust the view on the drones locations .........//
			View view = getWwd().getView();
			view.setEyePosition(Position.fromDegrees(lat, lon, elevationOffset));
			insertBeforeCompass(this.getWwd(), layer);
			
			this.getLayerPanel().update(this.getWwd());
			layer.setOverrideMarkerElevation(true);
			layer.setKeepSeparated(false);
			layer.setElevation(droneElevation); 
			layer.setMarkers(markers);   //.....For Red Pins....//
			insertBeforePlacenames(this.getWwd(), layer);
			

			LineBuilder lineBuilder = new LineBuilder(this.getWwd(), null, null);
			this.getContentPane().add(new LinePanel(this.getWwd(), lineBuilder), BorderLayout.EAST);
			this.enableNAIPLayer();		
		    
			//SocketConnection(); //.....Calling the Socket Connection Method....//
			
		 }//....End of AppFrame Constructor....//

		public void enableNAIPLayer(){
			LayerList list = this.getWwd().getModel().getLayers();
			ListIterator iterator = list.listIterator();
			while (iterator.hasNext()){
				Layer layer = (Layer) iterator.next();
				if (layer.getName().contains("NAIP")){
					layer.setEnabled(true);
					break;
				}
			}
		}
		
	 
	} //...End of AppFrame Class...//

	//....Function for Socket Connection....//
	static private void SocketConnection(){
		try {
			host = InetAddress.getLocalHost();
			socket=new Socket(host.getHostName(), 8080);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

/*.......***Receiving Data from the Simulator (Gazebo).......
  .......***Required for the Future Works...........*/		
 
//		while (true)
//		{
//		    try
//		    {
//		    	InputStream ois = socket.getInputStream();
//				WearableResponse objgotoresponse = null;
//				objgotoresponse = WearableResponse.parseDelimitedFrom(ois);
//				lon= objgotoresponse.getPeriodicStatus().getPose(droneid).getX();
//				lat = objgotoresponse.getPeriodicStatus().getPose(droneid).getY();
//				droneElevation = objgotoresponse.getPeriodicStatus().getPose(droneid).getZ();
//				System.out.println(objgotoresponse.getPeriodicStatus().getPose(droneid));
//				System.out.println(lon);
//				System.out.println(lat);
//				System.out.println(droneElevation);
//
//				
//				//DroneMovement(); //.....Calling the Drone Movement Function....//
//				
//
//		    }
//		    catch (IOException e)
//		    {
//		        //error ("System: " + "Connection to server lost!");
//		        System.exit (1);
//		        break;
//		    }
//		}
	} //....End of Socket Connection Function....//


	//......Main Function Starts.....//
	
	public static void main(String[] args) {	
		// TODO Auto-generated method stub
		if (Configuration.isMacOS()){
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Hello World Wind");
		}
		//ApplicationTemplate.start("World Wind Line Builder", URS.AppFrame.class);
		java.awt.EventQueue.invokeLater(new Runnable(){
			public void run(){
				// Create an AppFrame and immediately make it visible. As per Swing convention, this
				// is done within an invokeLater call so that it executes on an AWT thread.
				new AppFrame().setVisible(true);
				
			}
		});
	}//...End of Main Function...//
}//....End of URSSimulationMapInterface Class....//






