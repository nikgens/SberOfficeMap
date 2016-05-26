package newSber;


import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

/**
 * This marker displays an image at its location.
 */
public class ImageMarker extends AbstractMarker {

	PImage img;
	
	public static int STEP_SIZE = 16;

	public ImageMarker(Location location, PImage img) {
		super(location);
		this.img = img;
	}

	public ImageMarker(Feature office, PImage img) {
		super(((PointFeature)office).getLocation(), office.getProperties());
		this.img = img;
	}


	@Override
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.imageMode(PConstants.CORNER);
		// The image is drawn in object coordinates, i.e. the marker's origin (0,0) is at its geo-location.
		pg.image(img, x, y);
		pg.popStyle();
		
		
		
		if (selected) {
			showTitle(pg, x, y);
			showLabel(pg, x, y);
		}
		else {
			showLabel(pg, x, y);
		}
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return checkX > x && checkX < x + img.width && checkY > y && checkY < y + img.height;
	}
	
	
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getTitle() + " ";
		String pop = "Адрес: " + getAddress();
		String manager = "Руководитель: " + getManager();
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(10);

		pg.rect(x+STEP_SIZE, y+STEP_SIZE, Math.max(Math.max(pg.textWidth(name), pg.textWidth(pop)), pg.textWidth(manager)) + 6, 50);
		pg.fill(0, 0, 0);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+STEP_SIZE+2, y+STEP_SIZE + 3);
		pg.text(pop, x+STEP_SIZE+2, y+STEP_SIZE + 18);
		pg.text(manager, x+STEP_SIZE+2, y+STEP_SIZE + 33);
		
		pg.popStyle();
	}
	
	public void showLabel(PGraphics pg, float x, float y)
	{
		String number = getStringProperty("number");
		
		pg.pushStyle();
		
		pg.noStroke();
		pg.fill(255, 255, 255);
		pg.textSize(10);
		pg.rect(x, y-10, 20, 10);
		pg.fill(0, 0, 0);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(number, x, y-10);

		
		pg.popStyle();
	}
	
	
	private String getAddress()
	{
		return getStringProperty("address");
	}
	
	private String getTitle()
	{
		return getStringProperty("title");
	}
	
	private String getManager()
	{
		return getStringProperty("manager");
	}

}