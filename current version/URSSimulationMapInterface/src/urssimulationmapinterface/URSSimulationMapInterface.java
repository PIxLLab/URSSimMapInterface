// NEW SYSTEM

package urssimulationmapinterface;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.*;
import com.thalmic.myo.DeviceListener;
import gov.nasa.worldwind.*;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.symbology.TacticalSymbol;
import gov.nasa.worldwind.symbology.milstd2525.MilStd2525TacticalSymbol;
import gov.nasa.worldwind.util.LevelSet;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.ScreenAnnotation;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfaceCircle;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.formats.shapefile.Shapefile;
import gov.nasa.worldwind.event.SelectEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

import gov.nasa.worldwindx.applications.worldwindow.core.ToolTipAnnotation;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.LineBuilder;
import gov.nasa.worldwindx.examples.util.ShapefileLoader;
import gov.nasa.worldwindx.examples.util.ToolTip;
import gov.nasa.worldwind.view.orbit.OrbitView;

import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.mercator.MercatorSector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonParsingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Service;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.ServiceCallback;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.handler.RosHandler;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;
import edu.wpi.rail.jrosbridge.services.ServiceResponse;

import java.util.concurrent.TimeUnit;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.*;
import java.io.IOException;
import java.util.*;



import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;



public class URSSimulationMapInterface extends ApplicationTemplate {
	
	
	public static final  int NUMBER_OF_DRONES = 4;//change it if number of drones in action is changed
	
	static URSSimulationMapInterface ursinterface;
	
	boolean LBArm = false;
	long gameScore = 0;
	int playerID = 46;
	int wearableInterface = 3;
	ArrayList<Long> droneCluePos = new ArrayList<Long>();
	ArrayList<Long> humanCluePos = new ArrayList<Long>();
	ArrayList<Long> buildingNumber = new ArrayList<Long>();
	ArrayList<Long> dCiD = new ArrayList<Long>();
	ArrayList<Long> hCiD = new ArrayList<Long>();

	
	
	
	double originlongitude = 0.0;
	double originlatitude = 0.0;
	double dronelatitude = 0.0;
	double dronelongitude = 0.0;
	double droneElevation = 0.0; // drone location, elevation is in meters
	double elevationOffset = 0.0; // camera height from surface (meters)
	
	
	/*
	 * //NMSU
	 */
	/*
	double originlongitude = -106.75239801428688;
	double originlatitude = 32.2810102009863;
	double dronelatitude = 32.2771;
	double dronelongitude = -106.7201;
		
	
	//Redwood City
	double originlongitude = -122.236115;
	double originlatitude = 37.487846;
	double dronelatitude = 32.2771; 
	double dronelongitude =	-106.7201;
	
	
	double droneElevation = 10d; // drone location, elevation is in meters
	double elevationOffset = 2.3e3d; // camera height from surface (meters)
	*/
	
	
	Position targetPos = null;
	ArrayList<Position> pinPos = new ArrayList<Position>();

	/*
	ToolTipAnnotation drone0 = new ToolTipAnnotation("");
	ToolTipAnnotation drone1 = new ToolTipAnnotation("");
	ToolTipAnnotation drone2 = new ToolTipAnnotation("");
	ToolTipAnnotation drone3 = new ToolTipAnnotation("");
	*/
	
	ToolTipAnnotation [] drone  = new ToolTipAnnotation[4];
	
	
	
	
	Point[] toolTipOffSet = new Point[4];
	int theDroneId = 3;
	double[] droneLon = new double[5]; // x
	double[] droneLat = new double[5]; // y
	double[] droneAlt = new double[5]; // z
	double userLat = 0;
	double userLon = 0;

	double[] droneX = new double[4];
	double[] droneY = new double[4];
	double[] droneZ = new double[4];

	ArrayList<Marker> markers = null;
	private WorldWindow wwd2 = null;
	double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
	double m0 = 0, m1 = 0, n0 = 0, n1 = 0;

	JLabel theClues = new JLabel("");

	String[] uavsPose = new String[4];
	double[] uavX = new double[4];
	double[] uavY = new double[4];
	double[] uavZ = new double[4];
	Ros ros = null;
	int globalCounter = 0;
	//public JTextField tf1;
	static LeapData listener = null;
	MyoData mData = new MyoData();
	static voiceCommand voice = null;
	static Controller controller = null;
	static multiThread multiTh = null;
	 JButton drone0Button;
	 JButton drone1Button;
	 JButton drone2Button;
	 JButton drone3Button;
	 JLabel gsValue;
	 JLabel gestureInput;
	boolean altitudeGate = false;
	boolean userLocationGate = false;
	

	private class LinePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final WorldWindow wwd;
		private final LineBuilder lineBuilder;
		private JButton sendButton;
		private JButton searchButton;
		private JButton landButton;
		

		private JButton setDroneAltitudeL;
		private JButton setDroneAltitudeH;

		
		
		public LinePanel(WorldWindow wwd, LineBuilder lineBuilder) {
			super(new BorderLayout());
			
			this.wwd = wwd;
			wwd2 = wwd;
			this.lineBuilder = lineBuilder;
			this.makePanel(new Dimension(200, 400));
			
			
			
			
		}
		
		

// --------- Includes button and line panel functionalities ----------//
		private void makePanel(Dimension size) {
			JPanel scorePanel = new JPanel(new GridLayout(1, 2, 4, 4));
			JPanel gestureCommand = new JPanel(new GridLayout(1, 2, 4, 4));
			//JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5)); // ...For Holding the Line Buttons...//
			JPanel buttonPanel1 = new JPanel(new GridLayout(2, 2, 2, 2)); // ...For Holding the Command Buttons...//
			JPanel buttonPanel2 = new JPanel(new GridLayout(2, 2, 4, 4)); // Drone selection
			JPanel buttonPanel3 = new JPanel(new GridLayout(1, 2, 4, 4)); // Set Drone altitude
			JPanel symbolGuide = new JPanel(new GridLayout(5, 2));
			Font font = new Font("Arial",Font.BOLD,15);
			
			//---------------- gesture Command------------//
			JLabel gcomm = new JLabel("Command:", JLabel.LEFT);
		    gestureInput = new JLabel("");
		    //gestureInput.setText(String.valueOf(gameScore));
			gcomm.setFont(font);
			gestureInput.setFont(font);
		    gestureCommand.add(gcomm);
		    gestureCommand.add(gestureInput);
	//---------------- game score------------//
			JLabel gs = new JLabel("Score:", JLabel.LEFT);
		    gsValue = new JLabel("");
			gsValue.setText(String.valueOf(gameScore));
			gs.setFont(font);
			gsValue.setFont(font);
			scorePanel.add(gs);
			scorePanel.add(gsValue);
			
//-------- add symbol guide panel ------------//
			//ImageIcon iconLogo1 = new ImageIcon("/Users/urs-wearable/Documents/URSSimulationMapInterface/images/pushpins/plain-green.png");
			//ImageIcon iconLogo2 = new ImageIcon("/Users/urs-wearable/Documents/URSSimulationMapInterface/images/pushpins/plain-blue.png");
			//ImageIcon iconLogo3 = new ImageIcon("/Users/urs-wearable/Documents/URSSimulationMapInterface/images/redDot-H.png");
			//ImageIcon iconLogo4 = new ImageIcon("/Users/urs-wearable/Documents/URSSimulationMapInterface/images/redDot.png");
			//ImageIcon iconLogo5 = new ImageIcon("/Users/urs-wearable/Documents/URSSimulationMapInterface/images/bn.png");

			/*
			ImageIcon iconLogo1 = new ImageIcon("/home/redwan/Desktop/NMSU/RA/PIXLRepoGIT/URSSimMapInterface/current version/URSSimulationMapInterface/images/pushpins/plain-green.png");
			ImageIcon iconLogo2 = new ImageIcon("/home/redwan/Desktop/NMSU/RA/PIXLRepoGIT/URSSimMapInterface/current version/URSSimulationMapInterface/images/pushpins/plain-blue.png");
			ImageIcon iconLogo3 = new ImageIcon("/home/redwan/Desktop/NMSU/RA/PIXLRepoGIT/URSSimMapInterface/current version/URSSimulationMapInterface/images/redDot-H.png");
			ImageIcon iconLogo4 = new ImageIcon("/home/redwan/Desktop/NMSU/RA/PIXLRepoGIT/URSSimMapInterface/current version/URSSimulationMapInterface/images/redDot.png");
			ImageIcon iconLogo5 = new ImageIcon("/home/redwan/Desktop/NMSU/RA/PIXLRepoGIT/URSSimMapInterface/current version/URSSimulationMapInterface/images/bn.png");
			*/
			
			
			ImageIcon iconLogo1 = new ImageIcon("resources/pushpins/plain-green.png");
			ImageIcon iconLogo2 = new ImageIcon("resources/pushpins/plain-blue.png");
			ImageIcon iconLogo3 = new ImageIcon("resources/redDot-H.png");
			ImageIcon iconLogo4 = new ImageIcon("resources/redDot.png");
			ImageIcon iconLogo5 = new ImageIcon("resources/bn.png");
			
			JLabel DroneClue = new JLabel("Drone Object:", JLabel.LEFT);
			JLabel DroneClueImg = new JLabel();
			DroneClueImg.setIcon(iconLogo1);
		
			JLabel HumanClue = new JLabel("Human Object:", JLabel.LEFT);
			JLabel HumanClueImg = new JLabel();
			HumanClueImg.setIcon(iconLogo2);
			
			JLabel HDZone = new JLabel("Human Danger Zone:", JLabel.LEFT);
			JLabel HDZoneImg = new JLabel();
			HDZoneImg.setIcon(iconLogo3);
			
			JLabel DDZone = new JLabel("Drone Danger Zone", JLabel.LEFT);
			JLabel DDZoneImg = new JLabel();
			DDZoneImg.setIcon(iconLogo4);
			
			/*JLabel BN = new JLabel("Building Number:", JLabel.LEFT);
			JLabel BNImg = new JLabel();
			BNImg.setIcon(iconLogo5);*/
			
			symbolGuide.add(DroneClue);
			symbolGuide.add(DroneClueImg);
			symbolGuide.add(HumanClue);
			symbolGuide.add(HumanClueImg);
			symbolGuide.add(HDZone);
			symbolGuide.add(HDZoneImg);
			symbolGuide.add(DDZone);
			symbolGuide.add(DDZoneImg);
			//symbolGuide.add(BN);
			//symbolGuide.add(BNImg);

			

// -------------------- Drone Altitude panel------------//
			//tf1 = new JTextField();
			setDroneAltitudeL = new JButton("Low Altitude");

			setDroneAltitudeL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					try {
						add_location(theDroneId, 15);

					} catch (Exception e) {
						System.out.println(e);
					}

				}
			});
			setDroneAltitudeH = new JButton("High Altitude");

			setDroneAltitudeH.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					try {
						add_location(theDroneId, 25);

					} catch (Exception e) {
						System.out.println(e);
					}

				}
			});

			buttonPanel3.add(setDroneAltitudeL);
			buttonPanel3.add(setDroneAltitudeH);
			//buttonPanel3.add(tf1);

// -------------------- New Button --------------------//
			drone0Button = new JButton("1");
			drone0Button.setBackground(Color.YELLOW);
			drone0Button.setOpaque(true);
			drone0Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {

					drone0Button.setEnabled(false);
					drone1Button.setEnabled(true);
					drone2Button.setEnabled(true);
					drone3Button.setEnabled(true);
					theDroneId = 0;
				}
			});

			drone1Button = new JButton("2");
			drone1Button.setBackground(Color.BLUE);
			drone1Button.setOpaque(true);
			drone1Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {

					drone0Button.setEnabled(true);
					drone1Button.setEnabled(false);
					//drone1Button.setForeground(Color.WHITE);
					drone2Button.setEnabled(true);
					drone3Button.setEnabled(true);
					theDroneId = 1;
				}
			});

			drone2Button = new JButton("3");
			drone2Button.setBackground(Color.GREEN);
			drone2Button.setOpaque(true);
			drone2Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {

					drone0Button.setEnabled(true);
					drone1Button.setEnabled(true);
					drone2Button.setEnabled(false);
					drone3Button.setEnabled(true);
					theDroneId = 2;
				}
			});

			drone3Button = new JButton("4");
			drone3Button.setBackground(Color.MAGENTA);
			drone3Button.setOpaque(true);
			//drone3Button.setEnabled(false);
			drone3Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {

					drone0Button.setEnabled(true);
					drone1Button.setEnabled(true);
					drone2Button.setEnabled(true);
					drone3Button.setEnabled(false);
					theDroneId = 3;
				}
			});

			buttonPanel2.add(drone0Button);
			buttonPanel2.add(drone1Button);
			buttonPanel2.add(drone2Button);
			buttonPanel2.add(drone3Button);

// -------------------- Send Button --------------------//
			sendButton = new JButton("Send");

			sendButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					add_location(theDroneId);
				}
			});
			buttonPanel1.add(sendButton);
			sendButton.setEnabled(true);
			
			
	// -------------------- Land Button --------------------//
			landButton = new JButton("Land");

			landButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					droneLand(theDroneId);
				}
			});
			buttonPanel1.add(landButton);
			landButton.setEnabled(true);

			// -------------------- Search Button --------------------//
			searchButton = new JButton("Search");
			searchButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
// TODO Auto-generated method stub

					search();
				}
			});

			buttonPanel1.add(searchButton);
			searchButton.setEnabled(true);



// .......Main Panel, Which Holds All the Panels....//
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));



// .....Panel for Command Works.....//
			JPanel theScore = new JPanel(new BorderLayout());
			theScore.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9), new TitledBorder("")));
			theScore.setToolTipText("Game Score");
			theScore.add(scorePanel, BorderLayout.NORTH);
			
			JPanel uGesture = new JPanel(new BorderLayout());
			uGesture.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9), new TitledBorder("Gesture Command")));
			uGesture.setToolTipText("Gesture");
			uGesture.add(gestureCommand, BorderLayout.NORTH);
			
			JPanel commandPanel = new JPanel(new BorderLayout());
			commandPanel.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9), new TitledBorder("Command")));
			commandPanel.setToolTipText("Command Info");
			commandPanel.add(buttonPanel1, BorderLayout.NORTH);

			JPanel dronePanel = new JPanel(new BorderLayout());
			dronePanel.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9), new TitledBorder("Drones")));
			dronePanel.setToolTipText("Drone Selection");
			dronePanel.add(buttonPanel2, BorderLayout.NORTH);

			JPanel droneAltitudePanel = new JPanel(new BorderLayout());
			droneAltitudePanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9),
					new TitledBorder("Set Drone Altitude")));
			droneAltitudePanel.setToolTipText("SetDroneAltitude");
			droneAltitudePanel.add(buttonPanel3, BorderLayout.NORTH);

// ... Drones' panel
			JPanel guideline = new JPanel(new BorderLayout());
			guideline.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9), new TitledBorder("Symbol Guide")));
//JLabel drone1label = new JLabel("test",JLabel.LEFT);
			guideline.add(symbolGuide);



// ....Panel for Message Displaying......//

			JPanel scrollPanel = new JPanel(new GridLayout(1, 2));
			scrollPanel.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 400, 9), new TitledBorder("Drone Object")));
			scrollPanel.setToolTipText("Messages");
			JLabel dc = new JLabel("Values: ");
			scrollPanel.add(dc);
			scrollPanel.add(theClues);
			
			//////////////////////

			mainPanel.add(theScore,BorderLayout.CENTER);
			//if(wearableInterface==1 || wearableInterface == 2)
			mainPanel.add(uGesture,BorderLayout.CENTER);
			mainPanel.add(dronePanel, BorderLayout.CENTER);
			mainPanel.add(commandPanel, BorderLayout.CENTER);
			mainPanel.add(droneAltitudePanel, BorderLayout.CENTER);
			mainPanel.add(guideline, BorderLayout.CENTER);
			mainPanel.add(scrollPanel, BorderLayout.CENTER);

// ....Adding Main Panel to the JFrame....//
			this.add(mainPanel);
		}

// --------- Feed Point Panel shows pin line coordinates on the line panel
// ----------//
		
	}// ....End of Class LinePanel...//
	
	// Drones markers + human marker

	private class AppFrame extends ApplicationTemplate.AppFrame // javax.swing.JFrame
	{
		


		private final MarkerAttributes[] attrs = new BasicMarkerAttributes[] {
				new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.SPHERE, 1d, 7, 5),
				new BasicMarkerAttributes(Material.BLUE, BasicMarkerShape.SPHERE, 1d, 7, 5),
				new BasicMarkerAttributes(Material.GREEN, BasicMarkerShape.SPHERE, 1d, 7, 5),
				new BasicMarkerAttributes(Material.MAGENTA, BasicMarkerShape.SPHERE, 1d, 7, 5),
				new BasicMarkerAttributes(Material.RED, BasicMarkerShape.CUBE, 1d, 7, 5) };

		private Marker lastHighlit;
		private BasicMarkerAttributes lastAttrs;

		@SuppressWarnings("deprecation")
		public AppFrame() {

			super(true, true, false);
			

// create the markers for the drones
			markers = new ArrayList<Marker>();
// ........Pointing out only yellow drones.........//
			
			for (int i = 0; i < attrs.length; i++) {
				
				//System.out.println("AppFrame/(Before)DroneLat: " + droneLat[i]);
				Marker marker = new BasicMarker(Position.fromDegrees(droneLat[i], droneLon[i], droneAlt[i]), attrs[i]);
				marker.setPosition(Position.fromDegrees(droneLat[i], droneLon[i], droneAlt[i]));
				//System.out.println("AppFrame/(After)DroneLat: " + droneLat[i]);
				marker.setHeading(Angle.fromDegrees(0));
				marker.setPitch(Angle.fromDegrees(90));
				markers.add(marker);
			}
			
			
			final MarkerLayer layer = new MarkerLayer();
// ........... Highlight selected drones ..........//
			this.getWwd().addSelectListener(new SelectListener() {
				public void selected(SelectEvent event) {
					if (lastHighlit != null
							&& (event.getTopObject() == null || !event.getTopObject().equals(lastHighlit))) {
						lastHighlit.setAttributes(lastAttrs);
						lastHighlit = null;
					}

					if (!event.getEventAction().equals(SelectEvent.ROLLOVER))
						return;

					if (event.getTopObject() == null || event.getTopPickedObject().getParentLayer() == null)
						return;

					if (event.getTopPickedObject().getParentLayer() != layer)
						return;

					if (lastHighlit == null && event.getTopObject() instanceof Marker) {
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
			this.getWwd().addSelectListener(new SelectListener() {
				public void selected(SelectEvent event) {
					if (event.getTopObject() != null) {
						if (event.getTopPickedObject().getParentLayer() instanceof MarkerLayer) {
							PickedObject po = event.getTopPickedObject();
							dronelatitude = po.getPosition().getLatitude().degrees; // .....Drone Latitude...//
							dronelongitude = po.getPosition().getLongitude().degrees; // ... Drone Longitude...//
							System.out.printf("Track position %s, %s\n", po.getValue(AVKey.PICKED_OBJECT_ID).toString(),
									po.getPosition());

						}
					}
				}
			});

// .......... Add new pin by LEFT_CLICK .........//
			
			this.getWwd().getInputHandler().addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent mouseEvent) {
					if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
						targetPos = getWwd().getCurrentPosition();
						double lat = targetPos.latitude.degrees;
						double lon = targetPos.longitude.degrees;
						pinPos.add(targetPos);

						final RenderableLayer layer = new RenderableLayer();
						PointPlacemark pp = new PointPlacemark(
						Position.fromDegrees(lat, lon, targetPos.getElevation() + droneElevation));
						PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
						attrs = new PointPlacemarkAttributes();
						attrs.setScale(0.6); // pin size
						attrs.setImageOffset(new Offset(19d, 8d, AVKey.PIXELS, AVKey.PIXELS));
						attrs.setLabelOffset(new Offset(0.9d, 0.6d, AVKey.FRACTION, AVKey.FRACTION));
						attrs.setLineWidth(2d);
						attrs.setImageAddress("images/pushpins/plain-red.png");
						pp.setAttributes(attrs);
						layer.addRenderable(pp);
						insertBeforeCompass(getWwd(), layer);
					}
					
			
				}

			});
			
			//Wearable_Seperate
			MyKeyListener wearableKeyListener = new MyKeyListener(ursinterface);
			this.getWwd().getInputHandler().addKeyListener((KeyListener)wearableKeyListener);

			/*
			this.getWwd().getInputHandler().addKeyListener(new KeyListener() 
			{
				@Override
				public void keyPressed(KeyEvent e) 
				{
					// TODO Auto-generated method stub

					if(wearableInterface == 1)
					{
						if (e.getKeyCode() == 16) 
						{
							System.out.println("16 is being pressed");
							controller.addListener(listener);
						
						}
					
						if (e.getKeyCode() == 17) 
						{
							System.out.println("17 is being pressed");
						
							if(gestureInput.getText().equals("Land"))
							{
								droneLand(theDroneId);
							}
							if(gestureInput.getText().equals("Send"))
							{
								add_location(theDroneId);
							}
							if(gestureInput.getText().equals("Search"))
							{
								search();
							}
							if(gestureInput.getText().equals("Low Altitude"))
							{
								add_location(theDroneId, 15);
							}
							if(gestureInput.getText().equals("High Altitude"))
							{
								add_location(theDroneId, 25);
							}
						}
					

						if (e.getKeyCode() == 65) 
						{
							drone0Button.setEnabled(false);
							drone1Button.setEnabled(true);
							drone2Button.setEnabled(true);
							drone3Button.setEnabled(true);
							theDroneId = 0;
						}
						if (e.getKeyCode() == 66) 
						{
							drone0Button.setEnabled(true);
							drone1Button.setEnabled(false);
							drone2Button.setEnabled(true);
							drone3Button.setEnabled(true);
							theDroneId = 1;
						}
						if (e.getKeyCode() == 67) 
						{
							drone0Button.setEnabled(true);
							drone1Button.setEnabled(true);
							drone2Button.setEnabled(false);
							drone3Button.setEnabled(true);
							theDroneId = 2;
						}
						if (e.getKeyCode() == 68) 
						{
							drone0Button.setEnabled(true);
							drone1Button.setEnabled(true);
							drone2Button.setEnabled(true);
							drone3Button.setEnabled(false);
							theDroneId = 3;
						}
					}
					
					if (wearableInterface == 2)
					{
						if (e.getKeyCode() == 16) 
						{
							System.out.println("16 is being pressed");
							mData.testtheMyo();
							//gestureInput.setText(mData.gesture);
							if(mData.gesture.equals("REST"))
							{
								gestureInput.setText("Land");
							}
							if(mData.gesture.equals("FIST"))
							{
								gestureInput.setText("Send");
							}
							if(mData.gesture.equals("FINGERS_SPREAD"))
							{
								gestureInput.setText("Search");
							}
							if(mData.gesture.equals("WAVE_IN"))
							{
								gestureInput.setText("Low Altitude");
							}
							if(mData.gesture.equals("WAVE_OUT"))
							{
								gestureInput.setText("High Altitude");
							}
						
							System.out.println(mData.gesture);
						}
						
						if (e.getKeyCode() == 17) 
						{
							System.out.println("17 is being pressed");
						
						if(gestureInput.getText().equals("Land"))
						{
							System.out.println("test1");
							droneLand(theDroneId);
						}
						if(gestureInput.getText().equals("Send"))
						{
							System.out.println("test2");
							add_location(theDroneId);
						}
						if(gestureInput.getText().equals("Search"))
						{
							System.out.println("test3");
							search();
						}
						if(gestureInput.getText().equals("Low Altitude"))
						{
							System.out.println("test4");
							add_location(ursinterface.theDroneId, 15);
						}
						if(gestureInput.getText().equals("High Altitude"))
						{
							System.out.println("test5");
							add_location(ursinterface.theDroneId, 25);
						}
					}
						if (e.getKeyCode() == 65) 
						{
							drone0Button.setEnabled(false);
							drone1Button.setEnabled(true);
							drone2Button.setEnabled(true);
							drone3Button.setEnabled(true);
							theDroneId = 0;
						}
						
						if (e.getKeyCode() == 66) 
						{
							drone0Button.setEnabled(true);
							drone1Button.setEnabled(false);
							drone2Button.setEnabled(true);
							drone3Button.setEnabled(true);
							theDroneId = 1;
						}
						if (e.getKeyCode() == 67) 
						{
							drone0Button.setEnabled(true);
							drone1Button.setEnabled(true);
							drone2Button.setEnabled(false);
							drone3Button.setEnabled(true);
							theDroneId = 2;
						}
					
						if (e.getKeyCode() == 68) 
						{
							drone0Button.setEnabled(true);
							drone1Button.setEnabled(true);
							drone2Button.setEnabled(true);
							drone3Button.setEnabled(false);
							theDroneId = 3;
						}
					
					
					}
				
					if (wearableInterface==3)
					{
					
						if (e.getKeyCode() == 83) 
						{
						
							gestureInput.setText("Send");
					
						}
					
					
						if (e.getKeyCode() == 81) 
						{
						
							gestureInput.setText("Search");
					
						}
					
						if (e.getKeyCode() == 76) 
						{
							gestureInput.setText("Land");
					
						}
					
						if (e.getKeyCode() == 84) 
						{
							gestureInput.setText("Low Altitude");
						}
					
					
						if (e.getKeyCode() == 72) 
						{
							gestureInput.setText("High Altitude");
						}
					
						if (e.getKeyCode() == 8) 
						{
						//gestureInput.setText("High Altitude");
							if(gestureInput.getText().equals("Land"))
						
							{
								System.out.println("test1");
								droneLand(ursinterface.theDroneId);
						
							}
						
							if(ursinterface.gestureInput.getText().equals("Send"))
							{
								System.out.println("test2");
								add_location(ursinterface.theDroneId);
							}
						
							if(gestureInput.getText().equals("Search"))
							{
								System.out.println("test3");
								search();
							}
						
							if(gestureInput.getText().equals("Low Altitude"))
							{
								System.out.println("test4");
								add_location(ursinterface.theDroneId, 15);
							}
							if(gestureInput.getText().equals("High Altitude"))
							{
								System.out.println("test5");
								add_location(ursinterface.theDroneId, 25);
							}
						}
					
					
						if (e.getKeyCode() == 65) 
						{
							drone0Button.setEnabled(false);
							drone1Button.setEnabled(true);
							drone2Button.setEnabled(true);
							drone3Button.setEnabled(true);
						
							theDroneId = 0;
						}
					
						if (e.getKeyCode() == 66) 
						{
						
							drone0Button.setEnabled(true);
							drone1Button.setEnabled(false);
							drone2Button.setEnabled(true);
							drone3Button.setEnabled(true);
						
							theDroneId = 1;
						}
					
						if (e.getKeyCode() == 67) 
						{
							drone0Button.setEnabled(true);
							drone1Button.setEnabled(true);
							drone2Button.setEnabled(false);
							drone3Button.setEnabled(true);
							theDroneId = 2;
						}
						
						if (e.getKeyCode() == 68) 
						{
							drone0Button.setEnabled(true);
							drone1Button.setEnabled(true);
							drone2Button.setEnabled(true);
							drone3Button.setEnabled(false);
							theDroneId = 3;
						}
					
					}
				}

				@Override
				public void keyReleased(KeyEvent e) 
				{
					// TODO Auto-generated method stub
					if(wearableInterface==1)
					{
						if (e.getKeyCode() == 16) 
						{
							controller.removeListener(listener);
							if(listener.closeHand)
								gestureInput.setText("Send");
							if(listener.openHand)
								gestureInput.setText("Search");
							if(listener.oneFinger)
								gestureInput.setText("Land");
							if(listener.twoFinger)
								gestureInput.setText("Low Altitude");
							if(listener.threeFinger)
								gestureInput.setText("High Altitude");
						}	
					}
					
				}

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			*/
			
			
// .......... Adjust the view on the drones locations .........//
			View view = getWwd().getView();
			view.setEyePosition(Position.fromDegrees(originlatitude, originlongitude, elevationOffset));
			
			//redwood city, SF
			//view.setEyePosition(Position.fromDegrees(37.487846, -122.236115, elevationOffset));
			
			insertBeforeCompass(this.getWwd(), layer);

			this.getLayerPanel().update(this.getWwd());
			layer.setOverrideMarkerElevation(true);
			layer.setKeepSeparated(false);
			layer.setElevation(droneElevation);
			layer.setMarkers(markers); // .....For Red Pins....//
			
			
			System.out.print("AppFrame/markers: "+ markers.toString());
			
			insertBeforePlacenames(this.getWwd(), layer);
//////////////////////////////////////
///// add shape files /////////////// YOU NEED TO CHANGE THE PATH 

			//Shapefile sf2 = new Shapefile(new File("/Users/urs-wearable/Desktop/shape2/lines.shp"));
			
			//Shapefile sf2 = new Shapefile(new File("/home/redwan/Desktop/NMSU/RA/PIXLRepoGIT/URSSimMapInterface/current version/shape2/lines.shp"));
			
			Shapefile sf2 = new Shapefile(new File("resources/shapes/lines.shp"));
			
			 this.getWwd().getModel().getLayers().add(new ShapefileLoader().createLayerFromShapefile(sf2));
			
			//Shapefile sf3 = new Shapefile(new File("/Users/urs-wearable/Desktop/shape2/GameMap.shp"));
			
			//Shapefile sf3 = new Shapefile(new File("/home/redwan/Desktop/NMSU/RA/PIXLRepoGIT/URSSimMapInterface/current version/shape2/GameMap.shp"));
			
			 Shapefile sf3 = new Shapefile(new File("resources/shapes/GameMap.shp"));
			
			this.getWwd().getModel().getLayers().add(new ShapefileLoader().createLayerFromShapefile(sf3));
			
			  final RenderableLayer toolTipLayer = new RenderableLayer();
			  /*
			  toolTipLayer.addRenderable(drone0);
			  this.getWwd().getModel().getLayers().add(toolTipLayer);
			  toolTipLayer.addRenderable(drone1);
			  this.getWwd().getModel().getLayers().add(toolTipLayer);
			  toolTipLayer.addRenderable(drone2);
			  this.getWwd().getModel().getLayers().add(toolTipLayer);
			  toolTipLayer.addRenderable(drone3);
			  */
			  

			  
			  for(int i=0 ; i<NUMBER_OF_DRONES; i++)
			  {
				  toolTipLayer.addRenderable(drone[i]);
				  this.getWwd().getModel().getLayers().add(toolTipLayer);
			  }
			  
			  this.getWwd().getModel().getLayers().add(toolTipLayer);
			 

////////////////////////////////////////
			LineBuilder lineBuilder = new LineBuilder(this.getWwd(), null, null);
			this.getContentPane().add(new LinePanel(this.getWwd(), lineBuilder), BorderLayout.EAST);
			this.enableNAIPLayer();
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		}// ....End of AppFrame Constructor....//

		public void enableNAIPLayer() {
			LayerList list = this.getWwd().getModel().getLayers();
			ListIterator iterator = list.listIterator();
			while (iterator.hasNext()) {
				Layer layer = (Layer) iterator.next();
				if (layer.getName().contains("NAIP")) {
					layer.setEnabled(true);
					break;
				}
				this.getWwd().getModel().getLayers().remove(17);
				this.getControlPanel().hide();
			}

		}

	} // ...End of AppFrame Class...//

	
	//drone land
	public void droneLand(int id)
	{
		JSONObject pred2 = getPredicate(PredicateType.TYPE_DRONE_HOVERED, false);
		pred2.put("hovered", getPredicateHovered(id));
		JSONArray goal = new JSONArray();
		goal.add(pred2);
		callSetGoalService(goal);
	}

	// add a location based on the pin position
	public void add_location(int x)
	{		
		//this formula is used to convert coordinates (latitude, longitude) to (x,y)
		 double Ysim = ((targetPos.latitude.degrees - originlatitude) / 0.0000089);
		 System.out.println(Ysim);
		 double Xsim = ((targetPos.longitude.degrees - originlongitude) /  0.0000089 * Math.cos(originlatitude*0.018));
			 double Zsim = uavZ[x];
			 if(Zsim < 8.5)
			 {
			 	 Zsim = 15;
			 }
			 System.out.println(Zsim);
			 sendDrone(x,Xsim,Ysim,Zsim);
	}
		
	//add a location based on the pin position (used if you only want to change the altitude) 
	public void add_location(int x, double z) {
		double Zsim = z;
		System.out.println("add_location2//Z: "+ Zsim);
		sendDrone(x, droneX[x], droneY[x], Zsim);
	}

	
	//to call set_goal_service and passed a send command
	public void sendDrone(int id, double x, double y, double z) {
		double yCoordinate = y;
		double xCoordinate = x;
		double zCoordinate = z;
		int locID = callLocationAddService(xCoordinate, yCoordinate, zCoordinate, 0, 0, 0);
		JSONObject pred2 = getPredicate(PredicateType.TYPE_DRONE_AT, true);
		pred2.put("at", getPredicateDroneAt(id, locID));
		JSONArray goal = new JSONArray();
		goal.add(pred2);
		callSetGoalService(goal);
	}
	
	/*public void sendDrone(int id, double[] x, double[] y, double z) {
		
		JSONArray goal = new JSONArray();
		
		for(int i = 0; i < x.length;i++)
		{
			int locID = callLocationAddService(x[i], y[i], z, 0, 0, 0);
			JSONObject pred2 = getPredicate(PredicateType.TYPE_DRONE_AT, true);
			pred2.put("at", getPredicateDroneAt(id, locID));
			goal.add(pred2);
		}
		
		
		
		System.out.print(goal.toJSONString());
		callSetGoalService(goal);
	}*/

	
	// search (the user need to enter two points)
	public void search() {
		int counter = 0;
		double[] Ysim = new double[2];
		double[] Xsim = new double[2];
		double[] Zsim = new double[2];
		int droneNo = theDroneId + 1;
		if (pinPos.size() < 2) {
			System.out.println("You need to enter two points");
		} else {
			for (int i = pinPos.size() - 1; i > pinPos.size() - 3; i--) {
				Ysim[counter] = ((pinPos.get(i).latitude.degrees - originlatitude) / 0.0000089);
				Xsim[counter] = ((pinPos.get(i).longitude.degrees - originlongitude) / 0.0000089
								* Math.cos(originlatitude * 0.018));
				Zsim[counter] = uavZ[theDroneId];
				if (Zsim[counter] < 1.0) {
					Zsim[counter] = 13;
				}
				counter++;
			}

			pinPos.clear();
			scanArea(Xsim, Ysim, Zsim);
		}

	}
	
	//to call set_goal_service and passed a scan command
	public void scanArea(double[] x, double[] y, double[] z) {
		int left, right, area;

		if (x[0] <= x[1]) {
			left = callLocationAddService(x[0], y[0], z[0], 0, 0, 0);
			right = callLocationAddService(x[1], y[1], z[1], 0, 0, 0);

		} else {
			left = callLocationAddService(x[1], y[1], z[1], 0, 0, 0);
			right = callLocationAddService(x[0], y[0], z[0], 0, 0, 0);
		}

		area = callAreaAddService(left, right);
		JSONObject pred2 = getPredicate(PredicateType.TYPE_SCANNED, true);
		pred2.put("scanned", getPredicateScan(theDroneId, area));
		JSONArray goal = new JSONArray();
		goal.add(pred2);
		System.out.println("Goal from scanArea"+goal);
		callSetGoalService(goal);
	}

	/*public void updateUserLocation() {
		// System.out.println("Just test: ");
		userLat = multiTh.userLat1;
		userLon = multiTh.userLon1;
		markers.get(4).setPosition(Position.fromDegrees(userLat, userLon, 10.0));
		// wwd2.redraw();
	}*/
	
	
	public int callLocationAddService(double x, double y, double z, double ox, double oy, double oz) {
		Map<String, Double> position = new HashMap<String, Double>(3);
		position.put("x", x);
		position.put("y", y);
		position.put("z", z);
		Map<String, Double> orientation = new HashMap<String, Double>(3);
		orientation.put("x", ox);
		orientation.put("y", oy);
		orientation.put("z", oz);
		Map<String, Map> poseEuler = new HashMap<String, Map>(2);
		poseEuler.put("position", position);
		poseEuler.put("orientation", orientation);
		JSONObject pose = new JSONObject();
		pose.put("pose", poseEuler);
		ServiceRequest request = new ServiceRequest(pose.toJSONString());
		Service locationAddService = new Service(ros, "/urs_wearable/add_location", "urs_wearable/LocationAdd");
		ServiceResponse response = locationAddService.callServiceAndWait(request);
		System.out.println("CallLocationAddService/location_id: "+ response);
		return response.toJsonObject().getInt("loc_id");
	}


	public JSONObject getPredicateDroneAt(int droneID, int locationID) {
		Map<String, Integer> droneIDObject = new HashMap<String, Integer>(1);
		droneIDObject.put("value", droneID);
		Map<String, Integer> locationIDObject = new HashMap<String, Integer>(1);
		locationIDObject.put("value", locationID);
		JSONObject predicateDroneAt = new JSONObject();
		predicateDroneAt.put("d", droneIDObject);
		predicateDroneAt.put("l", locationIDObject);
		return predicateDroneAt;
	}

	public JSONObject getPredicateScan(int droneID, int areaID) {
		Map<String, Integer> droneIDObject = new HashMap<String, Integer>(1);
		droneIDObject.put("value", droneID);
		Map<String, Integer> locationIDObject = new HashMap<String, Integer>(1);
		locationIDObject.put("value", areaID);
		JSONObject predicateScan = new JSONObject();
		predicateScan.put("d", droneIDObject);
		predicateScan.put("a", locationIDObject);
		return predicateScan;
	}

	public int callAreaAddService(int left, int right) {
		JSONObject area = new JSONObject();
		area.put("loc_id_left", left);
		area.put("loc_id_right", right);
		ServiceRequest request = new ServiceRequest(area.toJSONString());
		Service areaAddService = new Service(ros, "/urs_wearable/add_area", "urs_wearable/AddArea");
		ServiceResponse response = areaAddService.callServiceAndWait(request);
		System.out.println("callAreaAddService/ Add Area Response: "+response.toString());
		return response.toJsonObject().getInt("area_id");
	}


	public JSONObject getPredicateHovered(int droneID) {
		Map<String, Integer> droneIDObject = new HashMap<String, Integer>(1);
		droneIDObject.put("value", droneID);
		JSONObject droneHovered = new JSONObject();
		droneHovered.put("d", droneIDObject);
		return droneHovered;
	}

	public enum PredicateType {
		TYPE_DRONE_ABOVE(0),
		TYPE_DRONE_ALIGNED(1),
		TYPE_DRONE_AT(2),
		TYPE_TOOK_COLLIDED(3),
		TYPE_DRONE_HOVERED(4),
		TYPE_DRONE_IN(5),
		TYPE_DRONE_LOW_BATTERY(6),
		TYPE_SCANNED(7);

		private int value;

		PredicateType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public JSONObject getPredicate(PredicateType type, boolean val) {
		JSONObject predicate = new JSONObject();
		predicate.put("type", type.getValue());
		predicate.put("at", getPredicateDroneAt(0, 0));
		predicate.put("hovered", getPredicateHovered(0));
		predicate.put("scanned", getPredicateScan(0, 0));
		predicate.put("truth_value", val);
		return predicate;
	}

	public void callSetGoalService(JSONArray goal) {
		JSONObject requestObject = new JSONObject();
		requestObject.put("player_id", playerID);
		requestObject.put("goal", goal);
		ServiceRequest request = new ServiceRequest(requestObject.toJSONString());
		System.out.println("callSetGoalService/Goal: "+ requestObject.toJSONString());
		Service setGoalService = new Service(ros, "/urs_wearable/set_goal", "urs_wearable/SetGoal");
		ServiceResponse response = setGoalService.callServiceAndWait(request);
		System.out.print("callSetGoalService/Response:" + response);
		setGoalService.callService(request, null);
	}

	public void callGetStateService() {
		Topic Gstate = new Topic(ros, "/urs_wearable/state", "urs_wearable/State");
		Gstate.subscribe(new TopicCallback() {
			@Override
			public void handleMessage(Message message) {
				try {
					String t1 = message.toString();
					JSONParser t = new JSONParser();
					JSONObject theState = (JSONObject) t.parse(t1);
					System.out.println("callGetStateService/ response: "+theState);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				{

				}
			}
		});
		ServiceRequest stateRequest = new ServiceRequest();
		Service getState = new Service(ros, "/urs_wearable/get_state", "urs_wearable/GetState");
		getState.callService(stateRequest, null);
	}
	
	// just for test
	/*public void newTestM() {
		RenderableLayer layer = new RenderableLayer();
		LatLon latlon = new LatLon(Angle.fromDegrees(32.280777691888545), Angle.fromDegrees(-106.7513664059102));
		double radius = 10.0;
		ShapeAttributes normalAttrs = new BasicShapeAttributes();
		normalAttrs.setImageSource("/Users/urs-wearable/Documents/URSSimulationMapInterface/images/HDZP.png");
		normalAttrs.setOutlineMaterial(Material.RED);
		normalAttrs.setInteriorOpacity(0.75);
		SurfaceCircle surfaceCircle = new SurfaceCircle(normalAttrs, latlon, radius);
		layer.addRenderable(surfaceCircle);
		insertBeforeCompass(wwd2, layer);
	}*/

	// to get the position of each drone and display it on the map
	public void uavPosition(int x) {
		
		//System.out.println("uavPosition/call trace/ drone # : " + x);
		
		String[] parts = uavsPose[x].split("}");
		String[] parts2 = parts[2].split(":");
		String[] parts3 = parts2[3].split(",");
		String yPose = parts3[0];
		parts3 = parts2[4].split(",");
		String xPose = parts3[0];
		droneX[x] = Double.parseDouble(xPose);
		droneY[x] = Double.parseDouble(yPose);
		double coef = (Double.parseDouble(yPose)) * 0.0000089;
		double coef2 = (Double.parseDouble(xPose)) * 0.0000089;
		
		
		uavY[x] = originlatitude + coef;
		uavX[x] = originlongitude + coef2 / Math.cos(originlatitude * 0.018);
		
		
		String zPose = parts2[5];
		droneZ[x] = Double.parseDouble(zPose);
		uavZ[x] = (Double.parseDouble(zPose));
		markers.get(x).setPosition(Position.fromDegrees(uavY[x], uavX[x], uavZ[x]));

		//markers.get(4).setPosition(Position.fromDegrees(32.28091248359882,-106.75242456217656, uavZ[x]));
		
 
			 
		drone[x].setPosition(Position.fromDegrees(uavY[x],uavX[x],uavZ[x]));
		drone[x].setText(new DecimalFormat("##.#").format(uavZ[x])); 
		
		
		/*
		
		if(x==0) { 
		 drone0.setPosition(Position.fromDegrees(uavY[x],uavX[x],uavZ[x]));
		 drone0.setText(new DecimalFormat("##.#").format(uavZ[x]));
		 //toolTipOffSet[0] = new Point(5,-33);
		 //drone0.setTooltipOffset(toolTipOffSet[0]); 
		  //https://www.programcreek.com/java-api-examples/index.php?api=gov.nasa.
		 
		  } 
		  if(x==1) {
		  drone1.setPosition(Position.fromDegrees(uavY[x],uavX[x],uavZ[x]));
		  drone1.setText(new DecimalFormat("##.#").format(uavZ[x]));
		  } 
		  if(x==2) {
		  drone2.setPosition(Position.fromDegrees(uavY[x],uavX[x],uavZ[x]));
		  drone2.setText(new DecimalFormat("##.#").format(uavZ[x])); } 
		  if(x==3) {
		  drone3.setPosition(Position.fromDegrees(uavY[x],uavX[x],uavZ[x]));
		  drone3.setText(new DecimalFormat("##.#").format(uavZ[x])); }
		 */
		 
		wwd2.redraw();
	}

	//to display the drone danger zone
	public void DZpin(String m) throws ParseException {
		String doubleQoute = m.replace('\'', '\"');
		Object obj = new JSONParser().parse(doubleQoute);
		JSONObject jo = (JSONObject) obj;
		double ddzlon = (double) jo.get("longitude");
		double ddzlat = (double) jo.get("latitude");

		RenderableLayer layer = new RenderableLayer();
		// Globe globe = wwd2.getModel().getGlobe();
		// This position is on the North America map.
		LatLon latlon = new LatLon(Angle.fromDegrees(ddzlat), Angle.fromDegrees(ddzlon));
		double radius = 10.0;
		ShapeAttributes normalAttrs = new BasicShapeAttributes();
		normalAttrs.setOutlineMaterial(Material.RED);
		normalAttrs.setInteriorMaterial(Material.RED);
		normalAttrs.setInteriorOpacity(0.5);
		SurfaceCircle surfaceCircle = new SurfaceCircle(normalAttrs, latlon, radius);
		layer.addRenderable(surfaceCircle);
		insertBeforeCompass(wwd2, layer);
	}


	// to display building number   // not used 
	public void BuildingAnnotation(String m) throws Exception {
		String doubleQoute = m.replace('\'', '\"');
		Object obj = new JSONParser().parse(doubleQoute);
		JSONObject jo = (JSONObject) obj;
		double bNlon = (double) jo.get("longitude");
		double bNlat = (double) jo.get("latitude");
		long buildingNo = (long) jo.get("building_number");
		boolean newFlag = true;

		if (buildingNumber.size() == 0) {
			// System.out.println
			buildingNumber.add(buildingNo);
		} else {
			for (int i = 0; i < buildingNumber.size(); i++) {
				if (buildingNumber.get(i) == buildingNo) {
					newFlag = false;
					break;
				}

			}
		}
		if (newFlag) {
			buildingNumber.add(buildingNo);
			System.out.println("flag");
			ToolTipAnnotation display = new ToolTipAnnotation("");
			final RenderableLayer toolTipBuilding = new RenderableLayer();
			toolTipBuilding.addRenderable(display);
			wwd2.getModel().getLayers().add(toolTipBuilding);
			Point offset = new Point(0, 0);
			display.setTooltipOffset(offset);
			display.setPosition(Position.fromDegrees(bNlat, bNlon, 10));
			display.setText(Long.toString(buildingNo));

		}
		wwd2.redraw();
	}

	//to display the location of drone objects
	public void DCpos(String m) throws ParseException {

		String doubleQoute = m.replace('\'', '\"');
		Object obj = new JSONParser().parse(doubleQoute);
		JSONObject jo = (JSONObject) obj;
		double dclon = (double) jo.get("longitude");
		double dclat = (double) jo.get("latitude");
		long dcVal = (long) jo.get("drone_clue_value");
		long dcid = (long) jo.get("drone_clue_id");
		String currentClues = theClues.getText();
		if(dcVal!=0)
		{
			boolean flag2 = true;
			if (dCiD.size() == 0) {
				dCiD.add(dcid);
				theClues.setText(currentClues + Long.toString(dcVal) + ", ");
				flag2 = false;
			    gameScore += 10;
			    gsValue.setText(String.valueOf(gameScore));
			    sendData();
			}
			else
			{
				for (int i = 0; i < dCiD.size(); i++) {
					if (dCiD.get(i) == dcid) {
						flag2 = false;
						break;
					}
			}
		}
			if(flag2)
			{
				dCiD.add(dcid);
				theClues.setText(currentClues + Long.toString(dcVal) + ", ");
				gameScore += 10;
				gsValue.setText(String.valueOf(gameScore));
				sendData();
			}
		}
		System.out.println("drone clue value: " + dcVal);
		boolean flag = true;

		if (droneCluePos.size() == 0) {
			droneCluePos.add(dcid);
			
		} else {
			for (int i = 0; i < droneCluePos.size(); i++) {
				if (droneCluePos.get(i) == dcid) {
					flag = false;
					break;
				}

			}
		}
		if (flag) {
			gameScore += 5;
			gsValue.setText(String.valueOf(gameScore));
			sendData();
			droneCluePos.add(dcid);
			System.out.println("print");
			final RenderableLayer layer2 = new RenderableLayer();
			PointPlacemark pp = new PointPlacemark(Position.fromDegrees(dclat, dclon, 1200.0));
			PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
			attrs = new PointPlacemarkAttributes();
			attrs.setScale(0.6); // pin size
			attrs.setImageOffset(new Offset(19d, 8d, AVKey.PIXELS, AVKey.PIXELS));
			attrs.setLabelOffset(new Offset(0.9d, 0.6d, AVKey.FRACTION, AVKey.FRACTION));
			// attrs.setLineMaterial(new Material(WWUtil.makeRandomColor(null)));
			attrs.setLineWidth(2d);
			attrs.setImageAddress("images/pushpins/plain-green.png");
			pp.setAttributes(attrs);
			layer2.addRenderable(pp);
			// wwd2.getModel().getLayers().add(layer2);
			insertBeforeCompass(wwd2, layer2);
		}
		System.out.println("PlayerScore= " + gameScore);
	}

	//to display the location of human objects
	public void HCpos(String m) throws ParseException {

		String doubleQoute = m.replace('\'', '\"');
		Object obj = new JSONParser().parse(doubleQoute);
		JSONObject jo = (JSONObject) obj;
		double hclon = (double) jo.get("longitude");
		double hclat = (double) jo.get("latitude");
		long hcid = (long) jo.get("drone_clue_id");
		boolean flag = true;

		if (humanCluePos.size() == 0) {
			humanCluePos.add(hcid);
			
		} else {
			for (int i = 0; i < humanCluePos.size(); i++) {
				if (humanCluePos.get(i) == hcid) {
					flag = false;
					break;
				}

			}
		}
		if (flag) {
			gameScore += 5;
			gsValue.setText(String.valueOf(gameScore));
			sendData();
			humanCluePos.add(hcid);
			System.out.println("print");
			final RenderableLayer layer2 = new RenderableLayer();
			PointPlacemark pp = new PointPlacemark(Position.fromDegrees(hclat, hclon, 1200.0));
			PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
			attrs = new PointPlacemarkAttributes();
			attrs.setScale(0.6); // pin size
			attrs.setImageOffset(new Offset(19d, 8d, AVKey.PIXELS, AVKey.PIXELS));
			attrs.setLabelOffset(new Offset(0.9d, 0.6d, AVKey.FRACTION, AVKey.FRACTION));
			// attrs.setLineMaterial(new Material(WWUtil.makeRandomColor(null)));
			attrs.setLineWidth(2d);
			attrs.setImageAddress("images/pushpins/plain-blue.png");
			pp.setAttributes(attrs);
			layer2.addRenderable(pp);
			// wwd2.getModel().getLayers().add(layer2);
			insertBeforeCompass(wwd2, layer2);
		}
		System.out.println("PlayerScore= " + gameScore);

	}

	//to display the human danger zone
	public void HDZpin(String m) throws ParseException {

		String doubleQoute = m.replace('\'', '\"');
		Object obj = new JSONParser().parse(doubleQoute);
		JSONObject jo = (JSONObject) obj;
		double hdzlon = (double) jo.get("longitude");
		double hdzlat = (double) jo.get("latitude");

		RenderableLayer layer = new RenderableLayer();
		// Globe globe = wwd2.getModel().getGlobe();
		// This position is on the North America map.
		LatLon latlon = new LatLon(Angle.fromDegrees(hdzlat), Angle.fromDegrees(hdzlon));
		double radius = 10.0;
		ShapeAttributes normalAttrs = new BasicShapeAttributes();
		normalAttrs.setOutlineMaterial(Material.RED);
		normalAttrs.setOutlineStippleFactor(3);
		//normalAttrs.setImageSource("/home/redwan/Desktop/NMSU/RA/PIXLRepoGIT/URSSimMapInterface/current version/URSSimulationMapInterface/images/pushpins/HDZP.png");
		normalAttrs.setImageSource("/resources/pushpins/HDZP.png");
		normalAttrs.setInteriorOpacity(0.75);
		SurfaceCircle surfaceCircle = new SurfaceCircle(normalAttrs, latlon, radius);
		layer.addRenderable(surfaceCircle);
		insertBeforeCompass(wwd2, layer);
	}

	// to display the position of the player   // not used
	public void humanPosition(String m) throws ParseException {
		String doubleQoute = m.replace('\'', '\"');
		Object obj = new JSONParser().parse(doubleQoute);
		JSONObject jo = (JSONObject) obj;
		double hlon = (double) jo.get("longitude");
		double hlat = (double) jo.get("latitude");
		markers.get(4).setPosition(Position.fromDegrees(hlat, hlon, 10));
		wwd2.redraw();

	}
	
	// to update the scores
	public void checkPlayerScore(String m) throws ParseException {
		String doubleQoute = m.replace('\'', '\"');
		Object obj = new JSONParser().parse(doubleQoute);
		JSONObject jo = (JSONObject) obj;
		long tScore = (long) jo.get("point");
		gameScore+= tScore;
		if (gameScore < 0)
			gameScore = 0;
		gsValue.setText(String.valueOf(gameScore));
		System.out.println("PlayerScore= " + gameScore);
		sendData();
	}
	
	// to publish a score topic
	public void sendData() {
		Topic currentScore = new Topic(ros, "/game_score", "std_msgs/String");
		/*JSONObject obj = new JSONObject();
	      obj.put("player_id", playerID);
	      obj.put("score", gameScore);*/
		
		Message toSend = new Message("{\"data\": \"player_id:" + playerID + ", score:" + gameScore + "\"}");//(obj.toString());
		System.out.println("TEST: "+toSend.toString());
		currentScore.publish(toSend);
	}
	
	public void toolTipAnnotationInit()
	{
		for(int i=0 ; i<NUMBER_OF_DRONES; i++)
			drone[i] = new ToolTipAnnotation(" ");
	
	}
	
	public void parseAndinitMapCoordinates()
	{
		try 
		{
		    File fXmlFile = new File("maps/map-nmsu.xml");//Change the URL if new map needs to be loaded
		    //File fXmlFile = new File("maps/map-RedwoodCity.xml");//Change the URL if new map needs to be loaded
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(fXmlFile);
		    doc.getDocumentElement().normalize();

		    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		    NodeList nList = doc.getElementsByTagName("map-name");
		    System.out.println("----------------------------");

		    for (int temp = 0; temp < nList.getLength(); temp++) 
		    {
		        Node nNode = nList.item(temp);
		        System.out.println("\nCurrent Element :" + nNode.getNodeName());
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) 
		        {
		            Element eElement = (Element) nNode;
		            
		            /*
		            System.out.println("MAP NAME : "+ eElement.getAttribute("name"));
		            
		            System.out.println("originlongitude : "
		                               + eElement.getElementsByTagName("originlongitude")
		                                 .item(0).getTextContent());
		            System.out.println("originlatitude : "
		                               + eElement.getElementsByTagName("originlatitude")
		                                 .item(0).getTextContent());
		            
		            
		            System.out.println("dronelongitude : "
		                               + eElement.getElementsByTagName("dronelongitude")
		                                 .item(0).getTextContent());
		            System.out.println("dronelatitude : "
                            + eElement.getElementsByTagName("dronelatitude")
                              .item(0).getTextContent());
		            System.out.println("droneElevation : "
		                               + eElement.getElementsByTagName("droneElevation")
		                                 .item(0).getTextContent());
		            
		            
		            System.out.println("elevationOffset : "
                            + eElement.getElementsByTagName("elevationOffset")
                            		.item(0).getTextContent());
		            */
		            
		          
		        	String s = new String("");
		        	
		        	s = eElement.getElementsByTagName("originlongitude").item(0).getTextContent();
		        	originlongitude = Double.parseDouble(s);
		        	
		        	s = eElement.getElementsByTagName("originlatitude").item(0).getTextContent();
		        	originlatitude = Double.parseDouble(s);
		        	
		        	s = eElement.getElementsByTagName("dronelatitude").item(0).getTextContent();
		        	dronelatitude = Double.parseDouble(s);
		        	
		        	s = eElement.getElementsByTagName("dronelongitude").item(0).getTextContent();
		        	dronelongitude = Double.parseDouble(s);
		        	
		        	s = eElement.getElementsByTagName("droneElevation").item(0).getTextContent();
		        	droneElevation = Double.parseDouble(s);
		   
		        	s = eElement.getElementsByTagName("elevationOffset").item(0).getTextContent();
		        	elevationOffset = Double.parseDouble(s);	
		        	
		        }
		    }
		} 
		catch (Exception e) 
		{
		    e.printStackTrace();
		}
		
	}

//....Function for Socket Connection....//
	public void SocketConnection() {

		//ros = new Ros("192.168.1.3", 9090);
		ros = new Ros("spitfire.cs.nmsu.edu", 9090);
		ros.connect();

		// subscribe to drone danger zone topic
		Topic dDangerousZone = new Topic(ros, "/w_ddzcoordinates", "std_msgs/String");
		dDangerousZone.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
				if (message != null) {
					String dDanZone = message.toString();
					Object obj;
					try {
						obj = new JSONParser().parse(dDanZone);
						JSONObject jo = (JSONObject) obj;
						String theMessage = (String) jo.get("data");
					    System.out.println("Drone Danger Zone: "+theMessage);
						DZpin(theMessage);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// System.out.println("drone dangerous zone:" + dDanZone);

				}
			}
		});
		
		// subscribe to the player position topic
		Topic playerCoordinate = new Topic(ros, "/w_personcoordinates", "std_msgs/String");
		playerCoordinate.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
				if (message != null) {

					String playerPosition = message.toString();
					Object obj;
					try {
						obj = new JSONParser().parse(playerPosition);
						JSONObject jo = (JSONObject) obj;
						String theMessage = (String) jo.get("data");
						 System.out.println("Player Position: "+theMessage);
						humanPosition(theMessage);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

		// subscribe to the human danger zone topic
		Topic hDangerousZone = new Topic(ros, "/w_hdzcoordinates", "std_msgs/String");
		hDangerousZone.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
				if (message != null) {

					String hDanZone = message.toString();

					Object obj;
					try {
						obj = new JSONParser().parse(hDanZone);
						JSONObject jo = (JSONObject) obj;
						String theMessage = (String) jo.get("data");
						// System.out.println(theMessage);
						System.out.println("Human Danger Zone: "+theMessage);
						HDZpin(theMessage);
					} catch (ParseException e) {
						e.printStackTrace();
					}

				}
			}
		});
		
		// subscribe to the player objects topic
		Topic hClue = new Topic(ros, "/w_hccoordinates", "std_msgs/String");
		hClue.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
				if (message != null) {

					String hCluePosition = message.toString();
					Object obj;
					try {
						obj = new JSONParser().parse(hCluePosition);
						JSONObject jo = (JSONObject) obj;
						String theMessage = (String) jo.get("data");
						System.out.println("Player Object topic: "+theMessage);
						HCpos(theMessage);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		
		// subscribe to the player scores topic (not used)
		Topic pScore = new Topic(ros, "/w_player_score", "std_msgs/String");
		pScore.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
				if (message != null) {

					String playerScore = message.toString();
					Object obj;
					try {
						obj = new JSONParser().parse(playerScore);
						JSONObject jo = (JSONObject) obj;
						String theMessage = (String) jo.get("data");
						System.out.println("Player Score(not used): "+theMessage);
						checkPlayerScore(theMessage);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		// subscribe to the player scores topic  
		Topic pScore2 = new Topic(ros, "/w_player_score2", "std_msgs/String");
		pScore2.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
				if (message != null) {

					String playerScore2 = message.toString();
					Object obj;
					try {
						obj = new JSONParser().parse(playerScore2);
						JSONObject jo = (JSONObject) obj;
						String theMessage = (String) jo.get("data");
						 System.out.println("Player score topic: "+theMessage);
						checkPlayerScore(theMessage);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

		//subscribe to drone objects topic
		Topic dClue = new Topic(ros, "/w_dccoordinates", "std_msgs/String");
		dClue.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
				if (message != null) {

					String dCluePosition = message.toString();
					Object obj;
					try {
						obj = new JSONParser().parse(dCluePosition);
						JSONObject jo = (JSONObject) obj;
						String theMessage = (String) jo.get("data");
						System.out.println("drone objects topic: "+ theMessage);
						DCpos(theMessage);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

	/*s	Topic bNumbers = new Topic(ros, "/w_building_numbers", "std_msgs/String");
		bNumbers.subscribe(new TopicCallback() {
			@Override
			public void handleMessage(Message message) {
				if (message != null) {

					String bNo = message.toString();
					Object obj;
					try {
						obj = new JSONParser().parse(bNo);
						JSONObject jo = (JSONObject) obj;
						String theMessage = (String) jo.get("data");
						// System.out.println(theMessage);
						BuildingAnnotation(theMessage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		Topic bValue = new Topic(ros, "/w_battery_value", "std_msgs/String");
		bValue.subscribe(new TopicCallback() {
			@Override
			public void handleMessage(Message message) {
				if (message != null) {

					String bVal = message.toString();
					// System.out.println(bVal);
					Object obj;
					try {
						obj = new JSONParser().parse(bVal);
						JSONObject jo = (JSONObject) obj;
						String status = (String) jo.get("data");
						// System.out.println(status);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});*/

		//subscribe to drones positions topics (four topics)
		Topic uav0 = new Topic(ros, "/uav0/ground_truth_to_tf/pose", "geometry_msgs/PoseStamped");
		uav0.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
//System.out.println(message.toString());
				if (message != null) {
					uavsPose[0] = message.toString();
					//System.out.println("UAV0: "+message.toString());
					uavPosition(0);
				}
			}
		});
		Topic uav1 = new Topic(ros, "/uav1/ground_truth_to_tf/pose", "geometry_msgs/PoseStamped");
		uav1.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
//System.out.println(message.toString());
				if (message != null) {
					uavsPose[1] = message.toString();
					//System.out.println("UAV1: "+message.toString());
					uavPosition(1);
				}
			}
		});
		Topic uav2 = new Topic(ros, "/uav2/ground_truth_to_tf/pose", "geometry_msgs/PoseStamped");
		uav2.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
				if (message != null) {
					uavsPose[2] = message.toString();
					//System.out.println("UAV2: "+message.toString());
					uavPosition(2);
				}
			}
		});
		Topic uav3 = new Topic(ros, "/uav3/ground_truth_to_tf/pose", "geometry_msgs/PoseStamped");
		uav3.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
//System.out.println(message.toString());
				if (message != null) {
					uavsPose[3] = message.toString();
					//System.out.println("UAV3: "+message.toString());
					uavPosition(3);

				}
			}
		});

	} // ....End of Socket Connection Function....//
	
	

	/*
	 * public void voiceCommands(){ if(voice.d1) { drone0Button.setEnabled(false);
	 * drone1Button.setEnabled(true); drone2Button.setEnabled(true);
	 * drone3Button.setEnabled(true); theDroneId=0; } if(voice.d2) {
	 * drone0Button.setEnabled(true); drone1Button.setEnabled(false);
	 * drone2Button.setEnabled(true); drone3Button.setEnabled(true); theDroneId=1; }
	 * if(voice.d3) { drone0Button.setEnabled(true); drone1Button.setEnabled(true);
	 * drone2Button.setEnabled(false); drone3Button.setEnabled(true); theDroneId=2;
	 * } if(voice.d4) { drone0Button.setEnabled(true);
	 * drone1Button.setEnabled(true); drone2Button.setEnabled(true);
	 * drone3Button.setEnabled(false); theDroneId=3; } if(voice.send) {
	 * add_location(theDroneId); } if(voice.search) { search(); } }
	 */
// ......Main Function Starts.....//
	public static void main(String[] args) {
// TODO Auto-generated method stub
		ursinterface = new URSSimulationMapInterface();
		ursinterface.toolTipAnnotationInit();
		ursinterface.parseAndinitMapCoordinates();
		
		//Wearable_Seperate
		listener = new LeapData();
		
		controller = new Controller();

		final AppFrame appFrame = ursinterface.new AppFrame();
		multiTh = new multiThread();
		//voice = new voiceCommand();

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
			//	multiTh.start();
				appFrame.setVisible(true);
				ursinterface.SocketConnection();
				//ursinterface.callGetStateService();
				//ursinterface.newTestM();
				
				

			}
		});
	}// ...End of Main Function...//
}// ....End of URSSimulationMapInterface Class....//
