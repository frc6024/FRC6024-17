//Sunday 21st February @ 20:30
package org.usfirst.frc.team6024.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {

	public static NetworkTable camera;
	public static final int stop = 12;
	public static final int screenCentre = 180;
	public static final double tooBig = 7000;
	public static double x, y, w, h, center;
	public static String task;
	
	public static void visionInit(){
		camera.putString("task", "gear");
	}
	public static double sig(double po){
		return (2/(1+Math.exp(-po))) - 1;
	}
	public static void getData(){
		double data[] = camera.getNumberArray("GR", new double[]{-1, -1, -1, -1});
		x = data[0];
		y = data[1];
		w = data[2];
		h = data[3];
		center = x + w/2;
	}
	
	public static boolean canRunGear(){
		camera.putString("task", "gear");
		//Timer.delay(0.1);
		getData();
		if(x == -1)
			return false;
		return true;
	}
	
	public static void runGear(double angleToTurn){
		Movement.orient(angleToTurn);
		camera.putString("task", "gear");
		Timer.delay(0.4);
		getData();
		
		//Align
		while(!Robot.logitech.getRawButton(stop) && x != -1){
			//Keep moving left or right until centre is in screen center
			double curAngle = Robot.navX.getFusedHeading();
			getData();
			if(x == -1){
				long startTime = System.currentTimeMillis();
				while(System.currentTimeMillis() - startTime < 300){
					getData();
					if(x != -1)
						break;
					//Timer.delay(0.01);
				}
				if(x == -1){
					return;
				}
				
			}
			if(Math.abs(center - screenCentre) < 30)
				break;
			double xSpeed = -0.6*sig((center- screenCentre)*(30/w));
			Movement.move(0,xSpeed, Robot.navX.getFusedHeading(), curAngle);
			camera.putNumber("moveData", -0.4*sig((center- screenCentre)/80));
		}
		Movement.drive(0, 0);
		
		
		//Do PID with camera or navX and Go Forward
		double curAngle = Robot.navX.getFusedHeading();
		while(w*h < tooBig && !Robot.logitech.getRawButton(stop) && x != -1){
			getData();
			double yVal = -0.3*sig((center- screenCentre)*(300/w));
			if(Math.abs(screenCentre - center) < 10)
				yVal = 0;
			Movement.move(1,yVal,Robot.navX.getFusedHeading(), curAngle);
			camera.putNumber("moveData", yVal);
		}
		long tim=System.currentTimeMillis();
		while(System.currentTimeMillis()-tim<1800 || Robot.logitech.getRawButton(12))
			Movement.move(0.8, 0, Robot.navX.getFusedHeading(), curAngle);
		Movement.drive(0, 0);
	}
	
	public static boolean canRunBoiler(){
		return false;
	}
	
	public static void runBoiler(){
		
	}
}
