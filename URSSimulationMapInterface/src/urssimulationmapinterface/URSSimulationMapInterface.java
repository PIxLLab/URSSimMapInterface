/**
 * 
 */
package urssimulationmapinterface;
import gov.nasa.worldwind.*;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.event.SelectEvent;
import java.util.ArrayList;
import java.util.ListIterator;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.LineBuilder;
//import LineBuilder.LinePanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.html.parser.ParserDelegator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import gov.nasa.worldwind.layers.RenderableLayer;
import java.util.List;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import pb_wearable.Wearable.WearableRequest;
import pb_wearable.Wearable.WearableRequest.Builder;
import pb_wearable.Wearable.WearableResponse;
import pb_wearable.Wearable.GetPoseRepeated;
import pb_wearable.Wearable.SetDestRepeated;
import pb_wearable.Wearable.SetDestRepeated.SetDest;
import pb_wearable.Wearable.PoseRepeated;
import pb_wearable.Wearable.Region;
/**
 * @author Skyforce
 */
public class URSSimulationMapInterface extends ApplicationTemplate{
	
	// An inner class is used rather than directly subclassing JFrame in the main class so
	// that the main can configure system properties prior to invoking Swing. This is
	// necessary for instance on OS X (Macs) so that the application name can be specified.
	static boolean LBArm = false;
	
	/*static List <Integer> droneid = new ArrayList<Integer>();
	static List <Double> dronelatitude = new ArrayList<Double>();
	static List <Double> dronelongitude = new ArrayList<Double>();
	static List <Double> droneelevation = new ArrayList<Double>();*/
	static int droneid;
	static double dronelatitude;
	static double dronelongitude;
	static double droneelevation;
	static InetAddress host = null;
    static Socket socket =null;
    
    private static class LinePanel extends JPanel
	{
    	private static final long serialVersionUID = 1L;
		private final WorldWindow wwd;
		private final LineBuilder lineBuilder;
		private JButton newButton;
		private JButton pauseButton;
		private JButton endButton;
		private JButton sendButton;
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
	
		private void makePanel(Dimension size)
		{
			JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
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
			
			sendButton = new JButton ("Send");
			
			//.....Connects to the exec_monitor.....//
			sendButton.addActionListener( new ActionListener()
      {
        public void actionPerformed(ActionEvent actionEvent) {

          try {
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
            System.out.println(objgotoresponse);
          //Translating the ww coordinates to gazebo            
            double wwx0=32.2833;
            double wwx1=32.2743;		
            double pinx=dronelatitude;                  
            double Xsim =(((pinx - wwx0)/(wwx1 - wwx0))*(objgotoresponse.getRegion().getX1() - objgotoresponse.getRegion().getX0()))+objgotoresponse.getRegion().getX0();
            System.out.println("Pin Latitude in WW: "+pinx);
            System.out.println("Pin Longitude in Gazebo: "+Xsim);            
            double wwy0=-106.7557;
            double wwy1=-106.7447;		
            double piny=dronelongitude;                  
            double Ysim =(((piny - wwy0)/(wwy1 - wwy0))*(objgotoresponse.getRegion().getY1() - objgotoresponse.getRegion().getY0()))+objgotoresponse.getRegion().getY0();
            System.out.println("Pin Longitude in WW: "+piny);
            System.out.println("Pin Longitude in Gazebo: "+Ysim);           
            System.out.println();
          //sending translated pin coordinates 
            System.out.println("......Sending Data.......");
            WearableRequest.Builder objrequest = WearableRequest.newBuilder();
            objrequest.setType(WearableRequest.WearableRequestType.SET_DEST_REPEATED);
            objrequest.setSetDestRepeated(SetDestRepeated.newBuilder()
        		                              .addSetDest(SetDestRepeated.SetDest.newBuilder()
        				                                                          .setUavId(droneid)
        				                                                          .setX(Xsim)
        				                                                          .setY(Ysim)
        				                                                          .setZ(droneelevation)));
          System.out.println(objrequest);
          objrequest.build().writeDelimitedTo(oos);
        
        	//	  .addSetDestBuilder().setUavId(1).setX(40.00).setY(40.00).setZ(5.00).build());
                  
//          SetDestRepeated.Builder objgotoSetDest = SetDestRepeated.newBuilder();
//          objgotoSetDest.setSetDest(SetDestRepeated.SetDest.UAV_ID_FIELD_NUMBER, 1);
//          objgotoSetDest.setSetDest(SetDestRepeated.SetDest.X_FIELD_NUMBER, 40.00);
//          objgotoSetDest.setSetDest(SetDestRepeated.SetDest.Y_FIELD_NUMBER, 40.00);
//          objgotoSetDest.setSetDest(SetDestRepeated.SetDest.Z_FIELD_NUMBER, 5.00);
          
         //objrequest.setSetDestRepeated(objgotoSetDest.build());

//          System.out.println("......Sending Data.......");
//          System.out.println(objgotorequest);
//          objgotorequest.build().writeDelimitedTo(oos); 

//            Region objgotoregion = null;
//            objgotoregion = Region.parseDelimitedFrom(ois);
//            System.out.println(objgotoregion);
         
//            System.out.println ("Drone ID:"+ droneid);
//            System.out.println ("Latitude:"+ dronelatitude);
//            System.out.println ("Lon:"+ dronelongitude);
//            System.out.println ("Ele:"+ droneelevation);
         
//            objgotorequest.setUavId(droneid);// ...Set Drone ID...//
//            objgotorequest.setX(dronelatitude); // ....Set Drone Latitude...//
//            objgotorequest.setY(dronelongitude); // ....Set Drone Longitude...//
//            objgotorequest.setZ(droneelevation); // ....Set Drone Elevation...//
            
//            objgotorequest.setUavId(1);// ...Set Drone ID...//
//            objgotorequest.setX(2); // ....Set Drone Latitude...//
//            objgotorequest.setY(3); // ....Set Drone Longitude...//
//            objgotorequest.setZ(4); // ....Set Drone Longitude...//
            socket.close(); // ....Closing the Socket....//
            System.out.println("......Communication Ends.......");
            SocketConnection(); //....Calling the Socket Connection Method...//

          }

          catch (IOException e) {
            e.printStackTrace();
          }

        }
      });
			
			buttonPanel.add(sendButton);
			sendButton.setEnabled(false);
		
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
			JPanel outerPanel = new JPanel(new BorderLayout());
			outerPanel.setBorder(
			new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), new TitledBorder("Line")));
			outerPanel.setToolTipText("Line control and info");
			outerPanel.add(buttonPanel, BorderLayout.NORTH);
			outerPanel.add(scrollPane, BorderLayout.CENTER);
			this.add(outerPanel, BorderLayout.CENTER);
		}
	
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
	}

    private static class AppFrame extends ApplicationTemplate.AppFrame //javax.swing.JFrame
	{
		private static final MarkerAttributes[] attrs = new BasicMarkerAttributes[]
		{
			new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.SPHERE, 1d, 10, 5),
			new BasicMarkerAttributes(Material.RED, BasicMarkerShape.ORIENTED_CUBE, 0d),
			new BasicMarkerAttributes(Material.GRAY, BasicMarkerShape.HEADING_ARROW, 1d, 10, 5),
			new BasicMarkerAttributes(Material.BLUE, BasicMarkerShape.SPHERE, 1d, 10, 5)
	    };
		
		private Marker lastHighlit;
		private BasicMarkerAttributes lastAttrs;
	
		public AppFrame()
		{
			super(true, true, false);
		
			double lon = -106.7500946, lat = 32.2787745, droneElevation = 1d; // drone location, elevation is in meters
			double elevationOffset=2.5e3d; // camera height from surface (meters)
		
			// create the markers for the drones
			ArrayList<Marker> markers = new ArrayList<Marker>();
//			Marker marker = new BasicMarker(Position.fromDegrees(lat, lon, droneElevation), attrs[0]);
//			marker.setPosition(Position.fromDegrees(lat, lon, droneElevation));
//			marker.setHeading(Angle.fromDegrees(0));
//			marker.setPitch(Angle.fromDegrees(90));
//			markers.add(marker);
//		
			// put 4 markers on the corners of the permitted region around nmsu
			double minlat=32.284841;
			double maxlat = 32.270353 ;
			double minlon = -106.761522 ;
			double maxlon = -106.736765;
//			Marker marker1 = new BasicMarker(Position.fromDegrees(minlat, minlon, droneElevation), attrs[3]);
//			marker1.setPosition(Position.fromDegrees(minlat, minlon, droneElevation));
//			marker1.setHeading(Angle.fromDegrees(0));
//			marker1.setPitch(Angle.fromDegrees(90));
//			markers.add(marker1);
//			Marker marker2 = new BasicMarker(Position.fromDegrees(minlat, maxlon, droneElevation), attrs[3]);
//			marker2.setPosition(Position.fromDegrees(minlat, maxlon, droneElevation));
//			marker2.setHeading(Angle.fromDegrees(0));
//			marker2.setPitch(Angle.fromDegrees(90));
//			markers.add(marker2);
//			Marker marker3 = new BasicMarker(Position.fromDegrees(maxlat, minlon, droneElevation), attrs[3]);
//			marker3.setPosition(Position.fromDegrees(maxlat, minlon, droneElevation));
//			marker3.setHeading(Angle.fromDegrees(0));
//			marker3.setPitch(Angle.fromDegrees(90));
//			markers.add(marker3);
//			Marker marker4 = new BasicMarker(Position.fromDegrees(maxlat, maxlon, droneElevation), attrs[3]);
//			marker4.setPosition(Position.fromDegrees(maxlat, maxlon, droneElevation));
//			marker4.setHeading(Angle.fromDegrees(0));
//			marker4.setPitch(Angle.fromDegrees(90));
//			markers.add(marker4);
		
			final MarkerLayer layer = new MarkerLayer();
		
			// highlight selected drones
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
		   // get coordinates of the selected drone
			this.getWwd().addSelectListener(new SelectListener()
			{
				public void selected(SelectEvent event)
				{
					if (event.getTopObject() != null)
					{
						//if (event.getTopPickedObject().getParentLayer() instanceof RenderableLayer)
						if (event.getTopPickedObject().getParentLayer() instanceof MarkerLayer)
						{
							PickedObject po = event.getTopPickedObject();
							
							/*int id= Integer.parseInt(po.getValue(AVKey.PICKED_OBJECT_ID).toString()); 
							droneid.add(id); //.....Adding Drone ID...//*/
							
							droneid= Integer.parseInt(po.getValue(AVKey.PICKED_OBJECT_ID).toString()); 
							
							//System.out.println("Drone ID:"+id);
							
							/*double latitude = po.getPosition().getLatitude().degrees;
							dronelatitude.add(latitude); //....Adding Drone Latitude...//*/
							
							dronelatitude = po.getPosition().getLatitude().degrees;
							
							//System.out.println("Latitude:"+latitude);
							
							/*double longitude = po.getPosition().getLongitude().degrees;
							dronelongitude.add(longitude); //....Adding Drone Longitude...//*/
							
							dronelongitude = po.getPosition().getLongitude().degrees;
							
							//System.out.println("Longitude:"+longitude);
							
							/*double elevation = po.getPosition().getElevation();
							droneelevation.add(elevation); //....Adding Drone Elevation...//*/
							
							droneelevation = po.getPosition().getElevation();
							
							//System.out.println("Elevation:"+elevation);
							
					        System.out.printf("Track position %s, %s\n",
							po.getValue(AVKey.PICKED_OBJECT_ID).toString(),
							po.getPosition());
						}
					}
					
				}
				
				
			});
		
			// add new pine by LEFT_CLICK
			this.getWwd().getInputHandler().addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent mouseEvent)
				{
					if(mouseEvent.getButton() == MouseEvent.BUTTON1)
					{
						Position targetPos = getWwd().getCurrentPosition();
					
						double lat = targetPos.latitude.degrees;
						double lon = targetPos.longitude.degrees;
					
						//if(lat>32.284841 || lat < 32.270353 || lon < -106.761522 || lon > -106.736765){
						if(lat>minlat || lat < maxlat || lon < minlon || lon > maxlon){
						System.out.printf("New Drone OUT of permitted region, try again within the range of NMSU!\n");
						} else {
						System.out.printf("New Drone ADDED!\n");
					
						Marker marker = new BasicMarker(Position.fromDegrees(lat, lon, droneElevation), attrs[1]);
						marker.setPosition(Position.fromDegrees(lat, lon, droneElevation));
						marker.setHeading(Angle.fromDegrees(0));
						marker.setPitch(Angle.fromDegrees(90));
					
					    //sam
						final RenderableLayer layer = new RenderableLayer();
					
						PointPlacemark pp = new PointPlacemark(Position.fromDegrees(lat, lon, targetPos.getElevation() + droneElevation));
						pp.setValue(AVKey.DISPLAY_NAME, "Absolute, Label, Red pin icon, Line in random color and 2 wide");
						pp.setLineEnabled(true);
						pp.setAltitudeMode(WorldWind.ABSOLUTE);
						PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
						attrs = new PointPlacemarkAttributes();
						attrs.setScale(0.6);
						attrs.setImageOffset(new Offset(19d, 8d, AVKey.PIXELS, AVKey.PIXELS));
						attrs.setLabelOffset(new Offset(0.9d, 0.6d, AVKey.FRACTION, AVKey.FRACTION));
						attrs.setLineMaterial(new Material(WWUtil.makeRandomColor(null)));
						attrs.setLineWidth(2d);
						attrs.setImageAddress("images/pushpins/plain-red.png");
						pp.setAttributes(attrs);
						layer.addRenderable(pp);
					
						// Add the layer to the model.
						insertBeforeCompass(getWwd(), layer);
		     			//sam
				    	markers.add(marker);
					
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
				//}
				//}
			});
		
		    //WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
			//wwd.setPreferredSize(new java.awt.Dimension(1000, 800));
			//this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			//this.getContentPane().add(wwd, java.awt.BorderLayout.WEST);
			//this.pack();
			// Adjust the view on the drones locations
			View view = getWwd().getView();
			view.setEyePosition(Position.fromDegrees(lat, lon, elevationOffset));
			insertBeforeCompass(this.getWwd(), layer);
			//wwd.setModel(new BasicModel());
		
			layer.setOverrideMarkerElevation(true);
			layer.setKeepSeparated(false);
			layer.setElevation(droneElevation);
			layer.setMarkers(markers);
			insertBeforePlacenames(this.getWwd(), layer);
		
			LineBuilder lineBuilder = new LineBuilder(this.getWwd(), null, null);
			this.getContentPane().add(new LinePanel(this.getWwd(), lineBuilder), BorderLayout.EAST);
		
			this.enableNAIPLayer();
			
			SocketConnection();  //....Calling the Socket Connection Method...//
			   
		}
		
		public void enableNAIPLayer()
		{
			LayerList list = this.getWwd().getModel().getLayers();
			ListIterator iterator = list.listIterator();
			while (iterator.hasNext())
			{
				Layer layer = (Layer) iterator.next();
				
				if (layer.getName().contains("NAIP"))
				{
					layer.setEnabled(true);
					break;
				}
			}
		}
	}
    
    static private void SocketConnection()
    {
    	try {
			host = InetAddress.getLocalHost();
			socket=new Socket(host.getHostName(), 8080);
		} 
    	
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }

   public static void main(String[] args) {
		// TODO Auto-generated method stub
	   
	    if (Configuration.isMacOS())
		{
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Hello World Wind");
		}
		//ApplicationTemplate.start("World Wind Line Builder", URS.AppFrame.class);
		java.awt.EventQueue.invokeLater(new Runnable()
		{
		public void run()
		{
		// Create an AppFrame and immediately make it visible. As per Swing convention, this
		// is done within an invokeLater call so that it executes on an AWT thread.
		new AppFrame().setVisible(true);

		}
		});
	
		
  }//...End of Main Function...//

}//....End of URSSimulationMapInterface Class....//
