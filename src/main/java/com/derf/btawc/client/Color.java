package com.derf.btawc.client;

public class Color {
	// static fields
	private static final float COLOR_R = 1.0f / 255.0f;
	// Staic Colors
	public static final Color BLACK = new Color();
	public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f);
	public static final Color DARK_RED = new Color(0.5f, 0.0f, 0.0f);
	public static final Color RED = new Color(0.75f, 0.0f, 0.0f);
	public static final Color LIGHT_RED = new Color(1.0f, 0.0f, 0.0f);
	public static final Color DARK_GREEN = new Color(0.0f, 0.5f, 0.0f);
	public static final Color GREEN = new Color(0.0f, 0.75f, 0.0f);
	public static final Color LIGHT_GREEN = new Color(0.0f, 1.0f, 0.0f);
	public static final Color DARK_BLUE = new Color(0.0f, 0.0f, 0.5f);
	public static final Color BLUE = new Color(0.0f, 0.0f, 0.75f);
	public static final Color LIGHT_BLUE = new Color(0.0f, 0.0f, 1.0f);
	public static final Color YELLOW = new Color(1.0f, 1.0f, 0.0f);
	public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f);
	public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f);
	public static final Color ORANGE = new Color(1.0f, 0.5f, 0.0f);
	public static final Color YELLOW_GREEN = new Color(0.5f, 1.0f, 0.0f);
	public static final Color PINK = new Color(1.0f, 0.0f, 0.5f);
	public static final Color PURPLE = new Color(0.5f, 0.0f, 1.0f);
	public static final Color GREEN_BLUE = new Color(0.0f, 1.0f, 0.5f);
	public static final Color SKY_BLUE = new Color(0.0f, 0.5f, 1.0f);
	public static final Color DARK_GREY = new Color(0.25f);
	public static final Color GREY = new Color(0.5f);
	public static final Color LIGHT_GREY = new Color(0.75f);
	
	private float red;
	private float green;
	private float blue;
	private float alpha;
	
	public Color() {
		this(0.0f, 0.0f, 0.0f);
	}
	
	public Color(int color) {
		int red = color >> 16 & 0xff;
		int green = color >> 8 & 0xff;
		int blue = color & 0xff;
		
		this.red = this.toFloatColor(red);
		this.green = this.toFloatColor(green);
		this.blue = this.toFloatColor(blue);
	}
	
	public Color(float grey) {
		this(grey, grey, grey);
	}
	
	public Color(float red, float green, float blue) {
		this(red, green, blue, 1.0f);
	}
	
	public Color(float red, float green, float blue, float alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public Color(int red, int green, int blue) {
		this(red, green, blue, 1);
	}
	
	public Color(int red, int green, int blue, int alpha) {	
		this(toFloatColor(red), toFloatColor(green), toFloatColor(blue), toFloatColor(alpha));
	}
	
	protected static int toIntColor(float f) {
		return (int) (f * 255.0f);
	}
	
	protected static float toFloatColor(int i) {
		return i * COLOR_R;
	}

	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public int getConvertedRed() {
		return toIntColor(this.getRed());
	}
	
	public int getConvertedGreen() {
		return toIntColor(this.getGreen());
	}
	
	public int getConvertedBlue() {
		return toIntColor(this.getBlue());
	}
	
	public int getConvertedAlpha() {
		return toIntColor(this.getAlpha());
	}
	
	public int toColor16() {
		return (getConvertedRed() << 16) | (getConvertedGreen() << 8) | getConvertedBlue();
	}
	
	public int toColor24() {
		return (getConvertedAlpha() << 24) | (getConvertedRed() << 16) | (getConvertedGreen() << 8) | getConvertedBlue();
	}
}
