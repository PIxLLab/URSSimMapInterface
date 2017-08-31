/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.worldwindx.examples;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.event.SelectEvent;
import java.util.ArrayList;
import java.util.Iterator;


import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.formats.gpx.GpxReader;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.WWIO;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.geom.LatLon;
/**
 * This is the most basic World Wind program.
 *
 * @version $Id: HelloWorldWind.java 1971 2014-04-29 21:31:28Z dcollins $
 */
public class URS extends ApplicationTemplate
{

    // An inner class is used rather than directly subclassing JFrame in the main class so
    // that the main can configure system properties prior to invoking Swing. This is
    // necessary for instance on OS X (Macs) so that the application name can be specified.

    private static class AppFrame extends ApplicationTemplate.AppFrame   //javax.swing.JFrame
    {
    	private static final MarkerAttributes[] attrs = new BasicMarkerAttributes[]
                {
                    new BasicMarkerAttributes(Material.YELLOW, BasicMarkerShape.SPHERE, 1d, 10, 5),
                    new BasicMarkerAttributes(Material.RED, BasicMarkerShape.ORIENTED_CUBE, 1d)
                };
    	private Marker lastHighlit;
        private BasicMarkerAttributes lastAttrs;
    	
        public AppFrame()
        {
            
        	double lon = -7.8, lat = 53.2, droneElevation = 1d;  // drone location, elevation is in meters
        	double elevationOffset=200e3;						 // camera height from surface (meters)
        	
        	
        	// create the markers for the drones 
        	ArrayList<Marker> markers = new ArrayList<Marker>();
        	Marker marker = new BasicMarker(Position.fromDegrees(lat, lon, droneElevation), attrs[0]);
            marker.setPosition(Position.fromDegrees(lat, lon, droneElevation));
            marker.setHeading(Angle.fromDegrees(0));
            marker.setPitch(Angle.fromDegrees(90));
            markers.add(marker);
            
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
                        if (event.getTopPickedObject().getParentLayer() instanceof MarkerLayer)
                        {
                            PickedObject po = event.getTopPickedObject();
                            //noinspection RedundantCast
                            //System.out.printf("Track position %s, %s, size = %f\n",
                            //    po.getValue(AVKey.PICKED_OBJECT_ID).toString(),
                            //    po.getPosition(), (Double) po.getValue(AVKey.PICKED_OBJECT_SIZE));
                            System.out.printf("Track position %s, %s\n",
                                    po.getValue(AVKey.PICKED_OBJECT_ID).toString(),
                                    po.getPosition());
                        }
                    }
                    
                }
            });
                    
                    
            
            	// add new drone by LEFT_CLICK
            this.getWwd().addSelectListener(new SelectListener()
            {
                public void selected(SelectEvent event)
                {     	
                    if (event.getEventAction().equals(SelectEvent.LEFT_CLICK))
                	{	
                			Position targetPos = event.getTopPickedObject().getPosition();
                			double lat = targetPos.latitude.degrees;
                			double lon = targetPos.longitude.degrees;
                			System.out.printf("New Drone ADDED!\n");
                			Marker marker = new BasicMarker(Position.fromDegrees(lat, lon, droneElevation), attrs[1]);
                			marker.setPosition(Position.fromDegrees(lat, lon, droneElevation));
                			marker.setHeading(Angle.fromDegrees(0));
                			marker.setPitch(Angle.fromDegrees(90));
                			markers.add(marker);
                        
                			View view = getWwd().getView();
                            // Use a PanToIterator to iterate view to target position
                			if(view != null)
                			{
                                // The elevation component of 'targetPos' here is not the surface elevation,
                                // so we ignore it when specifying the view center position.
                				view.goTo(new Position(targetPos, 0),
                                targetPos.getElevation() + elevationOffset);
                			}
                		
                	}
                }
            });
            

     layer.setOverrideMarkerElevation(true);
     layer.setKeepSeparated(false);
     layer.setElevation(droneElevation);
     layer.setMarkers(markers);
     insertBeforePlacenames(this.getWwd(), layer);
            
     WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
     wwd.setPreferredSize(new java.awt.Dimension(1000, 800));
     this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
     this.getContentPane().add(wwd, java.awt.BorderLayout.CENTER);
     this.pack();
     // Adjust the view 
     View view = getWwd().getView();
     view.setEyePosition(Position.fromDegrees(lat, lon, elevationOffset));
     insertBeforeCompass(this.getWwd(), layer);
     wwd.setModel(new BasicModel());
  }
}

    
    
   
    	
        
    

    
    
    
    public static void main(String[] args)
    {
        if (Configuration.isMacOS())
        {
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Hello World Wind");
        }

        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                // Create an AppFrame and immediately make it visible. As per Swing convention, this
                // is done within an invokeLater call so that it executes on an AWT thread.
                new AppFrame().setVisible(true);
            }
        });
    }
}



