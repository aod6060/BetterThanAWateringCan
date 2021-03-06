package com.derf.btawc.util;

public class Vec3 {
	private double x;
	private double y;
	private double z;
	
	public Vec3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Vec3 [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	// Statics
	public static Vec3 add(Vec3 a, Vec3 b) {
		return new Vec3(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
	}
	
	public static Vec3 sub(Vec3 a, Vec3 b) {
		return new Vec3(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
	}
	
	public static Vec3 mul(Vec3 a, double b) {
		return new Vec3(a.getX() * b, a.getY() * b, a.getZ() * b);
	}
	
	public static Vec3 div(Vec3 a, double b) {
		return new Vec3(a.getX() / b, a.getY() / b, a.getZ() / b);
	}
	
	public static double length(Vec3 a) {
		return Math.sqrt(a.getX() * a.getX() + a.getY() * a.getY() + a.getZ() * a.getZ() );
	}
	
	public static Vec3 unit(Vec3 a) {
		double l = length(a);
		return div(a, l);
	}
	
}
