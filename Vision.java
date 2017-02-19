package org.usfirst.frc.team6024.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {
	
	public static NetworkTable camera;
	public static final int stop = 12;
	public static final int screenCentre = 160;
	public static final double tooBig = 6000000;
	public static double x, y, w, h, center;
	public static String task, current;
	
	public static void visionInit(){
		current = "stall";
		camera.putString("task", "stall");
		camera.putString("current", current);
	}
	
	public static void getData(){
		double data[] = camera.getNumberArray("GR", new double[]{-1, -1, -1, -1});
		x = data[0];
		y = data[1];
		w = data[2];
		h = data[3];
		center = x + w/2;
		task = camera.getString("task", "stall");
	}
	
	public static boolean canRunGear(){
		camera.putString("task", "gear");
		Timer.delay(0.1);
		getData();
		camera.putString("task", "stall");
		if(x == -1 || task == "boiler")
			return false;
		else return true;
	}
	
	public static boolean canRunBoiler(){
		return true;
	}		
	
	public static void runGear(){
		//Setup
		camera.putString("task", "gear");
		current = "Running Gear";
		Timer.delay(0.3);
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
					current = "waiting";
					camera.putString("current", current);
				}
				if(x == -1){
					current = "stall";
					camera.putString("task", "stall");
					camera.putString("current", current);
					return;
				}
				
			}
			if(Math.abs(center - screenCentre) < 2)
				break;
			//Movement.move(0, (center - screenCentre)/1152, curAngle);\
			camera.putNumber("moveData", center - screenCentre);
			System.out.println(center - screenCentre);
		}
		System.out.println("Move forward now");
		Movement.drive(0, 0);
		
		
		//Do PID with camera or navX and Go Forward
		double curAngle = Robot.navX.getFusedHeading();
		while(w*h < tooBig && !Robot.logitech.getRawButton(stop)){
			getData();
			//Movement.move(0.5, 0, curAngle);
		}
		Movement.drive(0, 0);
		current = "stall";
		camera.putString("task", "stall");
	}
	
	public static void runBoiler(){
		
	}
}
