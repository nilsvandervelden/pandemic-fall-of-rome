package com.groep6.pfor.models;

/**
 * Represents a color
 * @author Nils van der Velden
 */

import com.groep6.pfor.util.Vector3f;

public class Color {
	private final Vector3f rgb;
	private final String colorName;

	/**
	 * Initializes a new Color with the given components.
	 * @param rgb The Vector3f of a specific color
	 * @param ColorName The name of the specific color
	 */
	public Color(Vector3f rgb, String ColorName) {
		this.rgb = rgb;
		this.colorName = ColorName;
	}

	public Vector3f getColor() {
		return rgb;
	}

	public String getColorName() {
		return colorName;
	}
}