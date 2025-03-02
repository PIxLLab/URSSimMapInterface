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
import gov.nasa.worldwind.view.orbit.OrbitView;

import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.io.*;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import java.net.UnknownHostException;

import pb_wearable.Wearable.WearableRequest;
import pb_wearable.Wearable.WearableRequest.SetPoseRepeated;
import pb_wearable.Wearable.WearableRequest.SetPoseWaypointRepeated;
import pb_wearable.Wearable.WearableResponse;
import pb_wearable.Wearable.WearableResponse.WearableResponseType;

import javax.imageio.ImageIO;
import javax.swing.*;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Service;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.ServiceCallback;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.handler.RosHandler;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;
import edu.wpi.rail.jrosbridge.services.ServiceResponse;


/**
 * @author Skyforce
 */
public class URSSimulationMapInterface extends ApplicationTemplate {
	static boolean LBArm = false;
	static int droneid;
	static double dronelatitude = 32.2771;
	static double dronelongitude = -106.7201;
	// static double droneelevation = 10.00;
	static InetAddress host = null;
	static Socket socket = null;
	// static boolean init = true;
	static double lon = -106.7500, lat = 32.2787, droneElevation = 10d; // drone location, elevation is in meters
	static double elevationOffset = 2.5e3d; // camera height from surface (meters)
	static Position targetPos = null;
	static int theDroneId = 3;
	// static WearableResponse objgotoresponse = null;
	static double[] droneLon = new double[4]; // x
	static double[] droneLat = new double[4]; // y
	static double[] droneAlt = new double[4]; // z
	static ArrayList<Marker> markers = null;
	static private checkDroneLoc tt = new checkDroneLoc();
	static private WorldWindow wwd2 = null;
	static double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
	static double m0 = 0, m1 = 0, n0 = 0, n1 = 0;
    static String uav0pose = null;
    static String uav1pose = null;
    static String uav2pose = null;
    static String uav3pose = null;
    static double[] uavX = new double[4];
    static double[] uavY = new double[4];
    static double[] uavZ = new double[4];
    static Ros ros = null;
    
    

	private static class LinePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final WorldWindow wwd;
		private final LineBuilder lineBuilder;
		private JButton newButton;
		private JButton pauseButton;
		private JButton endButton;
		private JButton sendButton;
		private JButton picButton;
		private JLabel[] pointLabels;
		private JButton drone0Button;
		private JButton drone1Button;
		private JButton drone2Button;
		private JButton drone3Button;
		private JTextField tf1;
		private JButton setDroneAltitude;
		private double droneAltitude;

		public LinePanel(WorldWindow wwd, LineBuilder lineBuilder) {
			super(new BorderLayout());
			this.wwd = wwd;
			wwd2 = wwd;

			this.lineBuilder = lineBuilder;
			this.makePanel(new Dimension(200, 400));

			lineBuilder.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
					fillPointsPanel();
					
					
				}
			});
		}
		
		private void cornerPoints()
		{
			//wwd.set
		}

		// --------- Includes button and line panel functionalities ----------//
		private void makePanel(Dimension size) {
			JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5)); // ...For Holding the Line Buttons...//
			JPanel buttonPanel1 = new JPanel(new GridLayout(1, 4, 2, 2)); // ...For Holding the Command Buttons...//
			JPanel buttonPanel2 = new JPanel(new GridLayout(2, 2, 4, 4)); // Drone selection
			JPanel buttonPanel3 = new JPanel(new GridLayout(1, 2, 4, 4)); // Set Drone altitude

			// -------------------- Drone Altitude panel------------//
			tf1 = new JTextField();
			setDroneAltitude = new JButton("Drone Altitude");

			setDroneAltitude.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					try {
						 droneAltitude = Double.parseDouble(tf1.getText());
						 add_location(theDroneId, droneAltitude);
						 
					} catch (Exception e) {
						System.out.println(e);
					}

				}
			});

			buttonPanel3.add(setDroneAltitude);
			buttonPanel3.add(tf1);

			// -------------------- New Button --------------------//
			drone0Button = new JButton("1");
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
			drone1Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {

					drone0Button.setEnabled(true);
					drone1Button.setEnabled(false);
					drone2Button.setEnabled(true);
					drone3Button.setEnabled(true);
					theDroneId = 1;
				}
			});

			drone2Button = new JButton("3");
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
			drone3Button.setEnabled(false);
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

			newButton = new JButton("New");
			newButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
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
			// -------------------- Pause Button --------------------//
			pauseButton = new JButton("Pause");
			pauseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					lineBuilder.setArmed(!lineBuilder.isArmed());
					pauseButton.setText(!lineBuilder.isArmed() ? "Resume" : "Pause");
					((Component) wwd).setCursor(Cursor.getDefaultCursor());
				}
			});
			buttonPanel.add(pauseButton);
			pauseButton.setEnabled(false);
			// -------------------- End Button --------------------//
			endButton = new JButton("End");
			endButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
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
			// -------------------- Send Button --------------------//
			sendButton = new JButton("Send");

			sendButton.addActionListener(new ActionListener() {
				// .....Connects to the exec_monitor.....//
				public void actionPerformed(ActionEvent actionEvent) {
				//	sendDrone();
				}
			});
			buttonPanel1.add(sendButton);
			sendButton.setEnabled(true);

			// .......Picture Button.....//
			picButton = new JButton("Picture");

			// ......Taking the Screen Shot................//
			picButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					try {
						Robot robot = new Robot();
						BufferedImage screenShot = robot
								.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
						ImageIO.write(screenShot, "JPG", new File("ScreenShot.jpg")); // ....Saving the Image into the
																						// Root Directory of the
																						// Project....//
						JOptionPane.showMessageDialog(null, "Screen Shot has been Taken and Saved.....!!!",
								"Message Box", JOptionPane.INFORMATION_MESSAGE);

					} catch (IOException | AWTException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			});

			buttonPanel1.add(picButton);
			picButton.setEnabled(true);

			// --------- Line Panel settings: Point Panel shows pin line coordinates on the
			// line panel ----------//
			JPanel pointPanel = new JPanel(new GridLayout(0, 1, 0, 10));
			pointPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			this.pointLabels = new JLabel[20];
			for (int i = 0; i < this.pointLabels.length; i++) {
				this.pointLabels[i] = new JLabel("");
				pointPanel.add(this.pointLabels[i]);
			}

			// Put the point panel in a container to prevent scroll panel from stretching
			// the vertical spacing.
			JPanel dummyPanel = new JPanel(new BorderLayout());
			dummyPanel.add(pointPanel, BorderLayout.NORTH);
			// Put the point panel in a scroll bar.
			JScrollPane scrollPane = new JScrollPane(dummyPanel);
			scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			if (size != null)
				scrollPane.setPreferredSize(size);

			// Add the buttons, scroll bar and inner panel to a titled panel that will
			// resize with the main window.

			// .......Main Panel, Which Holds All the Panels....//
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

			// ....Panel for Line Controlling........//
			JPanel outerPanel = new JPanel(new BorderLayout());
			outerPanel.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9), new TitledBorder("Line")));
			outerPanel.setToolTipText("Line control and info");
			outerPanel.add(buttonPanel, BorderLayout.NORTH);
			// outerPanel.add(scrollPane, BorderLayout.CENTER);

			// .....Panel for Command Works.....//
			JPanel commandPanel = new JPanel(new BorderLayout());
			commandPanel.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9), new TitledBorder("Command")));
			commandPanel.setToolTipText("Command Info");
			commandPanel.add(buttonPanel1, BorderLayout.NORTH);

			JPanel dronePanel = new JPanel(new BorderLayout());
			dronePanel.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9), new TitledBorder("Drone")));
			dronePanel.setToolTipText("Drone Selection");
			dronePanel.add(buttonPanel2, BorderLayout.NORTH);

			JPanel droneAltitudePanel = new JPanel(new BorderLayout());
			droneAltitudePanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 0, 9),
					new TitledBorder("Set Drone Altitude")));
			droneAltitudePanel.setToolTipText("SetDroneAltitude");
			droneAltitudePanel.add(buttonPanel3, BorderLayout.NORTH);

			// ....Panel for Message Displaying......//
			JPanel scrollPanel = new JPanel(new BorderLayout());
			scrollPanel.setBorder(
					new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 400, 9), new TitledBorder("Message Log")));
			scrollPanel.setToolTipText("Messages");
			scrollPanel.add(scrollPane, BorderLayout.CENTER);

			// ....Adding All Panels to the Main Panel.......//
			mainPanel.add(outerPanel, BorderLayout.CENTER);
			mainPanel.add(commandPanel, BorderLayout.CENTER);
			mainPanel.add(dronePanel, BorderLayout.CENTER);
			mainPanel.add(droneAltitudePanel, BorderLayout.CENTER);
			mainPanel.add(scrollPanel, BorderLayout.CENTER);

			// ....Adding Main Panel to the JFrame....//
			this.add(mainPanel);

			// this.add(outerPanel, BorderLayout.CENTER);
			// this.add(commandPanel, BorderLayout.AFTER_LINE_ENDS);
		}

		// --------- Feed Point Panel shows pin line coordinates on the line panel
		// ----------//
		private void fillPointsPanel() {
			int i = 0;
			for (Position pos : lineBuilder.getLine().getPositions()) {
				if (i == this.pointLabels.length)
					break;
				String las = String.format("Lat %7.4f\u00B0", pos.getLatitude().getDegrees());
				String los = String.format("Lon %7.4f\u00B0", pos.getLongitude().getDegrees());
				pointLabels[i++].setText(las + " " + los);
			}
			for (; i < this.pointLabels.length; i++)
				pointLabels[i++].setText("");
		}
		
		
		
	}// ....End of Class LinePanel...//

	private static class AppFrame extends ApplicationTemplate.AppFrame // javax.swing.JFrame
	{
		private static final MarkerAttributes[] attrs = new BasicMarkerAttributes[] {
				new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.SPHERE, 1d, 10, 5),
				new BasicMarkerAttributes(Material.BLUE, BasicMarkerShape.SPHERE, 1d, 10, 5),
				new BasicMarkerAttributes(Material.GREEN, BasicMarkerShape.SPHERE, 1d, 10, 5),
				new BasicMarkerAttributes(Material.PINK, BasicMarkerShape.SPHERE, 1d, 10, 5) };

		private Marker lastHighlit;
		private BasicMarkerAttributes lastAttrs;

		@SuppressWarnings("deprecation")
		public AppFrame() {

			super(true, true, false);

			// create the markers for the drones
			markers = new ArrayList<Marker>();
			// ........Pointing out only yellow drones.........//
			for (int i = 0; i < attrs.length; i++) {
				Marker marker = new BasicMarker(Position.fromDegrees(droneLat[i], droneLon[i], droneAlt[i]), attrs[i]);
				marker.setPosition(Position.fromDegrees(droneLat[i], droneLon[i], droneAlt[i]));
				marker.setHeading(Angle.fromDegrees(0));
				marker.setPitch(Angle.fromDegrees(90));
				markers.add(marker);
			}

			// ........ Getting the IDs of the Drone ........//
			/*
			 * for (Marker tmpmarker : markers) { droneid = tmpdroneid; tmpdroneid++; }
			 */
			//getMapPoints();
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
							// droneelevation = po.getPosition().getElevation(); //.....Drone Elevation...//
							System.out.printf("Track position %s, %s\n", po.getValue(AVKey.PICKED_OBJECT_ID).toString(),
									po.getPosition());

						}
					}
				}
			});

			// this.getWindowFocusListeners();
			// ............Restrict the area .............//
			// double minlat=32.2840;
			// double maxlat = 32.2740 ;
			// double minlon = -106.7551;
			// double maxlon = -106.7451;
			// .......... Add new pin by LEFT_CLICK .........//
			this.getWwd().getInputHandler().addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent mouseEvent) {
					if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
						targetPos = getWwd().getCurrentPosition();
						double lat = targetPos.latitude.degrees;
						// System.out.println("Pin Lat: " + lat);
						double lon = targetPos.longitude.degrees;
						// System.out.println("Pin Lon: " + lon);
						/*
						 * if(lat>minlat || lat < maxlat || lon < minlon || lon > maxlon){ System.out.
						 * printf("New Pin OUT of permitted region, try again within the range of NMSU!\n"
						 * ); } else {
						 */
						System.out.printf("New Pin ADDED!\n");
						//add_Location();

						final RenderableLayer layer = new RenderableLayer();
						PointPlacemark pp = new PointPlacemark(
								Position.fromDegrees(lat, lon, targetPos.getElevation() + droneElevation));

						PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
						attrs = new PointPlacemarkAttributes();
						attrs.setScale(0.6); // pin size
						attrs.setImageOffset(new Offset(19d, 8d, AVKey.PIXELS, AVKey.PIXELS));
						attrs.setLabelOffset(new Offset(0.9d, 0.6d, AVKey.FRACTION, AVKey.FRACTION));
						// attrs.setLineMaterial(new Material(WWUtil.makeRandomColor(null)));
						attrs.setLineWidth(2d);
						attrs.setImageAddress("images/pushpins/plain-red.png");
						pp.setAttributes(attrs);
						layer.addRenderable(pp);

						// Add the layer to the model.
						insertBeforeCompass(getWwd(), layer);
						// markers.add(marker);

						View view = getWwd().getView();
						// Use a PanToIterator to iterate view to target position
						if (view != null) {
							// The elevation component of 'targetPos' here is not the surface elevation,
							// so we ignore it when specifying the view center position.
							// view.goTo(new Position(targetPos, 0), targetPos.getElevation() +
							// elevationOffset);
						}
					}
					if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
						System.out.println("This is button2");
						OrbitView view = (OrbitView) getWwd().getView();
						System.out.println(view.getZoom());

						view.setZoom(view.getZoom() - 20);
						//OrbitView view = (OrbitView) wwd2.getView();
						
						wwd2.getView().getGlobe();
						 view = (OrbitView) wwd2.getView();
						view.setZoom(view.getZoom() + 1);
						Rectangle viewport = view.getViewport();
						
						Position topRight = view.computePositionFromScreenPoint(viewport.x + viewport.width, viewport.y);
				        Position bottomLeft = view.computePositionFromScreenPoint(viewport.x, viewport.y + viewport.height);
				       
				        m0 = (Double)bottomLeft.getLongitude().degrees;
				        
				        n0 = bottomLeft.getLatitude().degrees;
				        
				        m1 = topRight.getLongitude().degrees;
				        
				        n1 = topRight.getLatitude().degrees;
				        
				        System.out.println(m0 + " " + m1 + " " + n0 + " " + n1);

					}
					if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
						System.out.println("This is button3");
						OrbitView view = (OrbitView) getWwd().getView();
						System.out.println(view.getZoom());

						view.setZoom(view.getZoom() + 20);
						
						wwd2.getView().getGlobe();
						 view = (OrbitView) wwd2.getView();
						view.setZoom(view.getZoom() + 1);
						Rectangle viewport = view.getViewport();
						
						Position topRight = view.computePositionFromScreenPoint(viewport.x + viewport.width, viewport.y);
				        Position bottomLeft = view.computePositionFromScreenPoint(viewport.x, viewport.y + viewport.height);
				        
				        m0 = (Double)bottomLeft.getLongitude().degrees;
				        
				        n0 = bottomLeft.getLatitude().degrees;
				        
				        m1 = topRight.getLongitude().degrees;
				       
				        n1 = topRight.getLatitude().degrees;
				        
				        System.out.println(m0 + " " + m1 + " " + n0 + " " + n1);
					}

					// }
				}

			});

			this.getWwd().getInputHandler().addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyPressed(KeyEvent e) {
					// if(e.getButton() == KeyEvent.isSpace())
					// {
					// System.out.println(e.getKeyCode());
					if (e.getKeyCode() == 32) {
						//sendDrone();
					}

					// }
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}

			});

			// .......... Adjust the view on the drones locations .........//
			View view = getWwd().getView();
			view.setEyePosition(Position.fromDegrees(lat, lon, elevationOffset));
			insertBeforeCompass(this.getWwd(), layer);

			this.getLayerPanel().update(this.getWwd());
			layer.setOverrideMarkerElevation(true);
			layer.setKeepSeparated(false);
			layer.setElevation(droneElevation);
			layer.setMarkers(markers); // .....For Red Pins....//
			insertBeforePlacenames(this.getWwd(), layer);

			LineBuilder lineBuilder = new LineBuilder(this.getWwd(), null, null);
			this.getContentPane().add(new LinePanel(this.getWwd(), lineBuilder), BorderLayout.EAST);
			this.enableNAIPLayer();

			// SocketConnection(); //.....Calling the Socket Connection Method....//

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
				// this.getLayerPanel().hide();
				this.getControlPanel().hide();
				
			}
		//	System.out.println("?????" + this.wwjPanel.size());
		//	System.out.println("?????" + this.getControlPanel().size());
		//	System.out.println("?????" + this.getLayerPanel().size());
			
		}
		 
	} // ...End of AppFrame Class...//

	
/*	static public void getTheRegion() {
		try {
			OutputStream oos = socket.getOutputStream();
			WearableRequest.Builder objgotorequest = WearableRequest.newBuilder();
			objgotorequest.setType(WearableRequest.WearableRequestType.GET_REGION);
			objgotorequest.build().writeDelimitedTo(oos);

			InputStream is = socket.getInputStream();
			WearableResponse objectResponse = WearableResponse.parseDelimitedFrom(is);

			while (!objectResponse.getType().equals(WearableResponseType.REGION)) {
				objectResponse = WearableResponse.parseDelimitedFrom(is);
			}

			if (objectResponse.getType().equals(WearableResponseType.REGION)) {
				System.out.println(objectResponse.getRegion().toString());
				String region = objectResponse.getRegion().toString();
				String[] lines = region.split(System.getProperty("line.separator"));
				for (int i = 0; i < lines.length; i++) {
					String line = lines[i].trim();

					if (line.indexOf("x0:") == 0) {
						x0 = Double.parseDouble(line.substring(3));
					}
					if (line.indexOf("y0:") == 0) {
						y0 = Double.parseDouble(line.substring(3));
					}
					if (line.indexOf("x1:") == 0) {
						x1 = Double.parseDouble(line.substring(3));
					}
					if (line.indexOf("y1:") == 0) {
						y1 = Double.parseDouble(line.substring(3));
					}
				}
				// System.out.println(x0 + " " + x1 + " " + y0 + " " + y1);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	static public void add_location(int x)
	{
		Service add_Location = new Service(ros, "/urs_wearable/location_add", "urs_wearable/Predicate" );
		double Ysim = ((targetPos.latitude.degrees * 10000) - 322790);
		double Xsim = ((targetPos.longitude.degrees * -10000) - 1067501) / -1;
		double Zsim = uavZ[x];
		ServiceRequest request = new ServiceRequest("{\"pose\": {\"position\": {\"x\": " + Xsim +", \"y\": " + Ysim + ", \"z\": " + Zsim + "},\"orientation\" : {\"x\": 0.0, \"y\": 0.0, \"z\": 1.0}}}");
		
		ServiceResponse response = add_Location.callServiceAndWait(request);
		//System.out.println(response);
		String[] locationIDpart = response.toString().split(":");
		String[] locationID = locationIDpart[1].split("}");
		sendDrone(theDroneId,Integer.parseInt(locationID[0]));
	}
	
	static public void add_location(int x, double z)
	{
		Service add_Location = new Service(ros, "/urs_wearable/location_add", "urs_wearable/Predicate" );
		double Ysim = uavY[x];
		double Xsim = uavX[x];
		double Zsim = z;
		ServiceRequest request = new ServiceRequest("{\"pose\": {\"position\": {\"x\": " + Xsim +", \"y\": " + Ysim + ", \"z\": " + Zsim + "},\"orientation\" : {\"x\": 0.0, \"y\": 0.0, \"z\": 1.0}}}");
		
		ServiceResponse response = add_Location.callServiceAndWait(request);
		//System.out.println(response);
		String[] locationIDpart = response.toString().split(":");
		String[] locationID = locationIDpart[1].split("}");
		sendDrone(theDroneId,Integer.parseInt(locationID[0]));
	}

	static public void sendDrone(int x, int y) {
		Service set_Goal = new Service(ros, "/urs_wearable/set_goal", "urs_wearable/Predicate" );
		ServiceRequest request = new ServiceRequest("{\"goal\": {"
				+ "\"- type\": 2, {"
				+ "{\"predicate_drone_at\": {"
				+ "\"drone_id\": {\"value\" :" + x +"}, \"location_id\": {\"value\" :" + x +"}, \"truth_value\": "
				+ true + "}}}}}");
		
		/*try {
			OutputStream oos = socket.getOutputStream();
			double Ysim = ((targetPos.latitude.degrees * 10000) - 322790);
			System.out.println("PinY Latitude in WW: " + targetPos.latitude.degrees);
			double Xsim = ((targetPos.longitude.degrees * -10000) - 1067501) / -1;
			System.out.println("PinX Latitude in WW: " + targetPos.longitude.degrees);
			System.out.println();
			System.out.println("......Sending Data.......");
			WearableRequest.Builder objrequest = WearableRequest.newBuilder();
			objrequest.setType(WearableRequest.WearableRequestType.SET_POSE_WAYPOINT_REPEATED);

			objrequest.setSetPoseWaypointRepeated(SetPoseWaypointRepeated.newBuilder().setUavId(theDroneId)
					.addSetPoseWaypoint(SetPoseWaypointRepeated.SetPoseWaypoint.newBuilder().setX(Xsim).setY(Ysim)
							.setZ(droneAlt[theDroneId])));
			System.out.println(objrequest);
			objrequest.build().writeDelimitedTo(oos);
			System.out.println("......Communication Ends.......");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	static public void uav0position() {
		String[] parts = uav0pose.split("}");
		//System.out.println(parts[2]);
		
		//String[] parts1 = uav0pose.split("}");
		String[] parts2 = parts[2].split(":");
		String[] parts3 = parts2[3].split(",");
		String yPose = parts3[0];
		uavY[0] = (Double.parseDouble(yPose) + 322790) / 10000;
		//System.out.println(yPose);
		parts3 = parts2[4].split(",");
		String xPose = parts3[0];
		uavX[0] = (-(Double.parseDouble(xPose)) + 1067501) / -10000;
		//System.out.println(xPose);
		String zPose = parts2[5];
		uavZ[0] = (Double.parseDouble(zPose));
		//System.out.println(zPose);
		markers.get(0).setPosition(Position.fromDegrees(uavY[0], uavX[0], uavZ[0]));
		wwd2.redraw(); // update wwd
		
	}
	
	static public void uav1position() {
		String[] parts = uav1pose.split("}");
		//System.out.println(parts[2]);
		
		//String[] parts1 = uav0pose.split("}");
		String[] parts2 = parts[2].split(":");
		String[] parts3 = parts2[3].split(",");
		String yPose = parts3[0];
		uavY[1] = (Double.parseDouble(yPose) + 322790) / 10000;
		//System.out.println(yPose);
		parts3 = parts2[4].split(",");
		String xPose = parts3[0];
		uavX[1] = (-(Double.parseDouble(xPose)) + 1067501) / -10000;
		//System.out.println(xPose);
		String zPose = parts2[5];
		uavZ[1] = (Double.parseDouble(zPose));
		//System.out.println(zPose);
		markers.get(1).setPosition(Position.fromDegrees(uavY[1], uavX[1], uavZ[1]));
		wwd2.redraw(); // update wwd
		
	}
	
	static public void uav2position() {
		String[] parts = uav2pose.split("}");
		//System.out.println(parts[2]);
		
		//String[] parts1 = uav0pose.split("}");
		String[] parts2 = parts[2].split(":");
		String[] parts3 = parts2[3].split(",");
		String yPose = parts3[0];
		uavY[2] = (Double.parseDouble(yPose) + 322790) / 10000;
		//System.out.println(yPose);
		parts3 = parts2[4].split(",");
		String xPose = parts3[0];
		uavX[2] = (-(Double.parseDouble(xPose)) + 1067501) / -10000;
		//System.out.println(xPose);
		String zPose = parts2[5];
		uavZ[2] = (Double.parseDouble(zPose));
		//System.out.println(zPose);
		markers.get(2).setPosition(Position.fromDegrees(uavY[2], uavX[2], uavZ[2]));
		wwd2.redraw(); // update wwd
		
	}

	static public void uav3position() {
		String[] parts = uav3pose.split("}");
		//System.out.println(parts[2]);
		
		//String[] parts1 = uav0pose.split("}");
		String[] parts2 = parts[2].split(":");
		String[] parts3 = parts2[3].split(",");
		String yPose = parts3[0];
		uavY[3] = (Double.parseDouble(yPose) + 322790) / 10000;
		//System.out.println(yPose);
		parts3 = parts2[4].split(",");
		String xPose = parts3[0];
		uavX[3] = (-(Double.parseDouble(xPose)) + 1067501) / -10000;
		//System.out.println(xPose);
		String zPose = parts2[5];
		uavZ[3] = (Double.parseDouble(zPose));
		//System.out.println(zPose);
		markers.get(3).setPosition(Position.fromDegrees(uavY[3], uavX[3], uavZ[3]));
		wwd2.redraw(); // update wwd
		
	}



	/*static public void getMapPoints()
	{
		 try {
		//wwd2.redraw();
		
	    wwd2.getView().getGlobe();
		OrbitView view = (OrbitView) wwd2.getView();
		view.setZoom(view.getZoom() + 1);
		Rectangle viewport = view.getViewport();
		System.out.println("-------------ok------------1");
		Position topRight = view.computePositionFromScreenPoint(viewport.x + viewport.width, viewport.y);
        Position bottomLeft = view.computePositionFromScreenPoint(viewport.x, viewport.y + viewport.height);
        System.out.println("-----------------ok--------2");
        m0 = (Double)bottomLeft.getLongitude().degrees;
        System.out.println("-------------ok------------3");
        n0 = bottomLeft.getLatitude().degrees;
        System.out.println("------------ok-------------4");
        m1 = topRight.getLongitude().degrees;
        System.out.println("-------------ok------------5");
        n1 = topRight.getLatitude().degrees;
        System.out.println("-------------ok------------6");
        System.out.println(m0 + " " + m1 + " " + n0 + " " + n1);
		 }
		 catch (Exception e)
		 {
			 System.out.println(e);
		 }
	}*/
	
	

	
	// ....Function for Socket Connection....//
	static public void SocketConnection(){

			//ros = new Ros("172.20.10.8", 9090);
			ros = new Ros("localhost", 9090);
			ros.connect();
			 
			// Topic echo = new Topic(ros, "/uav0/ground_truth_to_tf/pose", "std_msgs/String");
			 
			 Topic uav0 = new Topic(ros, "/uav0/ground_truth_to_tf/pose", "geometry_msgs/PoseStamped");
			 uav0.subscribe(new TopicCallback() {
					@Override
					public void handleMessage(Message message) {
						//System.out.println(message.toString());
						if(message!=null)
						{
							uav0pose = message.toString();
							uav0position();
						}
					}
				});
			 Topic uav1 = new Topic(ros, "/uav1/ground_truth_to_tf/pose", "geometry_msgs/PoseStamped");
			 uav1.subscribe(new TopicCallback() {
					@Override
					public void handleMessage(Message message) {
						//System.out.println(message.toString());
						if(message!=null)
						{
							uav1pose = message.toString();
							uav1position();
						}
					}
				});
			 Topic uav2 = new Topic(ros, "/uav2/ground_truth_to_tf/pose", "geometry_msgs/PoseStamped");
			 uav2.subscribe(new TopicCallback() {
					@Override
					public void handleMessage(Message message) {
						if(message!=null)
						{
							uav2pose = message.toString();
							uav2position();
						}
					}
				});
			 Topic uav3 = new Topic(ros, "/uav3/ground_truth_to_tf/pose", "geometry_msgs/PoseStamped");
			 uav3.subscribe(new TopicCallback() {
					@Override
					public void handleMessage(Message message) {
						//System.out.println(message.toString());
						if(message!=null)
						{
							uav3pose = message.toString();
							uav3position();
						}
					}
				});
			 
			 //ros.disconnect();
			// Service addTwoInts = new Service(ros, "/add_two_ints", "rospy_tutorials/AddTwoInts");

			 
			 ///uav3/controller/position/x/parameter_update
			 

			// socket=new Socket("192.168.1.142", 9090);//(host.getHostName(), 8080);
			// System.out.println("54565363546357674656875896776534uyhgdfsdjhggcfa");
			//socket = new Socket("spitfire.cs.nmsu.edu",8080);
	

	//	} //catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	} // ....End of Socket Connection Function....//
	
	static public int add_Location(){
		
		return 0;
	}
	
	static public void set_goal(){
		
	}
	// ......Main Function Starts.....//

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {

				// Create an AppFrame and immediately make it visible. As per Swing convention,
				// this
				// is done within an invokeLater call so that it executes on an AWT thread.

				new AppFrame().setVisible(true);
				SocketConnection();
				//System.out.println("{\"a\": 10, \"b\": 20}");
			//	add_Location();
				//set_goal();
			//	readMsgs();
			//	getTheRegion();
			//	tt.start();
				
				

			}
		});
		
	}// ...End of Main Function...//
}// ....End of URSSimulationMapInterface Class....//