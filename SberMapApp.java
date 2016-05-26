package newSber;
import processing.core.PApplet;
import processing.core.PImage;
import controlP5.CColor;
//CONTROLP5 Import
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.Textfield;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;



public class SberMapApp extends PApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ImageMarker lastSelected;
	
	UnfoldingMap map;
	List<Feature> offices;
	List<Marker> officeMarkers;

	
	PImage img, bankImg;
	
	// CONTROLP5 Variables
    ControlP5 cp5;
    String textValue = "";
    Textfield myTextfield;
    controlP5.Label label;
    boolean searchEvent;
    
    //For poiner marker
    SimplePointMarker bankMarker;
    Location markerLocation;


	public void setup() {
		size(1200, 800, P2D);
		
		// INIT CONTROLP5
        cp5 = new ControlP5(this);

        println(cp5);
        
        // set colors variable
        int yellow = color(255, 255, 0);
        int blue = color(0, 0, 255);
        int insideField = color(179, 217, 255);
        int strokeField = color(150,150,150);
        int black = color(0,0,0);
        
        CColor field = new CColor(yellow, insideField, strokeField, blue, black);
        ControlFont cf1 = new ControlFont(createFont("Arial",20));
        cp5.setColor(field);
        //Search field
        myTextfield = cp5.addTextfield("Search: ").setPosition(900, 30).setSize(250, 40).setFocus(true).setFont(cf1);
        myTextfield.setAutoClear(true).keepFocus(true);
        label = myTextfield.getCaptionLabel();
        //label text color
        label.setColor(0);
        
		
		map = new UnfoldingMap(this, new Google.GoogleMapProvider());
		map.zoomAndPanTo(13, new Location(43.1325, 131.9227)); // Zoom 13, placed on Vladivostok
		MapUtils.createDefaultEventDispatcher(this, map); 
		
		offices = GeoJSONReader.loadData(this, "vsp.geo.json"); // File with office GPS coordinates parser
		
		img = loadImage("favicon.png");
		bankImg = loadImage("bank.png");

		
		// add office markers to ArrayList or Marker type
		officeMarkers = new ArrayList<Marker>();
		for(Feature office : offices) {
			officeMarkers.add(new ImageMarker(office, img));
		}

		
		// add markers to the map
		map.addMarkers(officeMarkers);
		
	}

	public void draw() {
		map.draw();
//		addKey();
		map.addMarkers();
		
		//draw pointer marker
		for (Marker office: officeMarkers) {
			if (textValue.compareTo((String) office.getProperty("number")) == 0) {
				markerLocation = office.getLocation();
				bankMarker = new SimplePointMarker(markerLocation);
				ScreenPosition berlinPos = bankMarker.getScreenPosition(map);
				image(bankImg,berlinPos.x-8, berlinPos.y-35);
			}
		}
		
	}
	
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}

		selectMarkerIfHover(officeMarkers);
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			ImageMarker marker = (ImageMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	

	
	// ControlP5 methods
  
    public void controlEvent(ControlEvent theEvent) {
        if (theEvent.isAssignableFrom(Textfield.class)) {
            //println("controlEvent: accessing a string from controller '" + theEvent.getName() + "': "
            //       + theEvent.getStringValue());
            //searchEvent = true;
            textValue = theEvent.getStringValue();
        }
    }
    
    

}
