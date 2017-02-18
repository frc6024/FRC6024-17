package org.usfirst.frc.team6024.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {
	
	public static NetworkTable camera;
	public static final int stop = 12;
	public static final int screenCentre = 144;
	public static final double tooBig = 10000;
	public static double x, y, w, h, center;
	public static String task, current;
	
	public static void getData(){
		double data[] = camera.getNumberArray("GL", new double[]{-1, -1, -1, -1});
		x = data[0];
		y = data[1];
		w = data[2];
		h = data[3];
		center = x + w/2;
		task = camera.getString("task", "stall");
	}
	
	public static boolean canRunGear(){
		camera.putString("task", "gear");
		Timer.delay(0.075);
		getData();
		camera.putString("task", "stall");
		if(x == -1 || w*h > tooBig || task == "boiler")
			return false;
		else return true;
	}
	
	public static boolean canRunBioler(){
		return true;
	}		
	
	public static void runGear(){
		//Setup
		camera.putString("task", "gear");
		current = "Running Gear";
		Timer.delay(0.3);
		getData();
		//Allign
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
					Timer.delay(0.01);
				}
				if(x == -1){
					current = "stall";
					camera.putString("task", "stall");
					return;
				}
			}
			if(Math.abs(center - screenCentre) < 5)
				break;
			Movement.move((center - screenCentre)/432, 0, curAngle);
		}
		
		Movement.drive(0, 0);
		
		
		//Do PID with camera or navX and Go Forward
		double curAngle = Robot.navX.getFusedHeading();
		while(w*h < tooBig && !Robot.logitech.getRawButton(stop)){
			getData();
			Movement.move(0.5, 0, curAngle);
		}
		Movement.drive(0, 0);
		current = "stall";
		camera.putString("task", "stall");
	}
	
	public static void runBoiler(){
		
	}
}
