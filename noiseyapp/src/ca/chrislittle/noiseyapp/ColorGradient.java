package ca.chrislittle.noiseyapp;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.LinkedList;
import java.util.ListIterator;

public class ColorGradient {
	
	private LinkedList<ColorGradientEntry> map;
	
	
	public ColorGradient() {
		map = new LinkedList<ColorGradientEntry>();
	}
	
	/**
	 * Add a colour entry to the colour map
	 * 
	 * @param position Value between [0.0, 1.0] 
	 * @param color RGB colour for this entry
	 */
	public void addColor(float position, Color color) {
		if (position > 1.0f || position < 0.0f)
			// Ignore the entry if the position is out of bounds
			return;
		
		ColorGradientEntry entry = new ColorGradientEntry(position, color);

		// Add the element immediately if the colour map is empty
		if (map.size() == 0) {
			map.add(entry);
			return;
		}
		
		// Find the position to add the colour entry to the map
		boolean entryAdded = false;
		for(int i=0; i<map.size(); ++i) {
			if (map.get(i).lowerBound > position) {
				map.add(i, entry);
				entryAdded = true;
				break;
			}
		}
		
		// Append entry to the end of the map if all other entries have a
		// position lower than the new entry
		if(!entryAdded) {
			map.add(entry);
		}
	}
	
	/**
	 * Retrieve an interpolated colour object
	 * 
	 * @param position Value between 0.0 and 1.0
	 * @return Colour value retrieved and interpolated from the colour map
	 */
	public Color getColor(float position) {
		// Return a default colour if no colours have been added to the map
		if (map.size() == 0 || position < 0.0f || position > 1.0f)
			return new Color(0xFFFFFF);
		// Return the first colour if there is only one entry in the map, or
		// the first entry has a position greater than the requested position
		else if (map.size() == 1 || map.getFirst().lowerBound >= position)
			return map.getFirst().color;
		
		ColorGradientEntry entry = null;
		ColorGradientEntry entry2 = null;

		// Find the colour map range for the requested position
		for (int i=0; i<map.size(); ++i) {
			entry = map.get(i);
			
			if (entry.lowerBound <= position) {
				// Return the entry's colour if it is the last entry in the map
				if (i == map.size()-1) {
					return entry.color;
				}
				// Otherwise retrieve the next entry in the colour map
				else {
					entry2 = map.get(i+1);
					if (entry2.lowerBound >= position) {
						break;
					}
				}
			}
		}

		// Interpolate colour between colour map entries
		float t = (position - entry.lowerBound) / (entry2.lowerBound - entry.lowerBound);
		float u = 1.0f - t;

		float[] color = new float[3];
		float[] c1 = entry.color.getComponents(null);
		float[] c2 = entry2.color.getComponents(null);
		for (int i=0; i<3; ++i) {
			color[i] = c1[i]*u + c2[i]*t;
		}
		
		return new Color(color[0], color[1], color[2]);
	}
	
	
	public ColorGradientEntry[] getGradientPoints() {
		ColorGradientEntry[] arr = new ColorGradientEntry[map.size()];
		map.toArray(arr);
		return arr;
	}
	
}
