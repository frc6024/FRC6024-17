//Sunday 21st February @ 20:30
package org.usfirst.frc.team6024.robot;

import edu.wpi.first.wpilibj.Timer;

public class Movement {


	public static final double buf = 0.1; //Ignore small movements on the joystick
	public static double heading = 0;
	public static double pr = 0;
	
	static void teleOpMove(){
		// Control the robot with a joystick
		
		double xaxis = Robot.logitech.getRawAxis(0);
		double yaxis = Robot.logitech.getRawAxis(1)*-1;
		double zaxis = Robot.logitech.getRawAxis(2);
		double slider = Robot.logitech.getRawAxis(3);

		double tx = scale(xaxis, buf);
		double ty = scale(yaxis, buf);
		double tz = scale(zaxis, buf + 0.2);
		boolean trigger = Robot.logitech.getRawButton(1);
		
		double boost = map(slider, 1, -1, 0.2, 0.8);
		if(Robot.logitech.getRawButton(11)) boost=1.2;
		
		tx*=boost;
		ty*=boost;
		tz*=boost;
		
		if(trigger){   //Brake
			heading=Robot.navX.getFusedHeading();
			drive(0, 0);
		}else if(Robot.logitech.getRawButton(2)){ // Move Diagonal
				move(tx,ty);
				
		}

		else if(tx != 0 || ty != 0){      //Move
			if(Math.abs(ty)>=Math.abs(tx))
				move(0,ty);	
			else
				move(tx,0);
		}else if(tz!=0){ //Point Turn
			drive(0.5*tz,-0.5*tz);
			heading=Robot.navX.getFusedHeading();
		}else if(Robot.logitech.getRawButton(5)){
			orient(Robot.logitech.getPOV());
		}
		else{
			heading = Robot.navX.getFusedHeading();
			drive(0, 0);
		}
		
		if(Robot.logitech.getRawButton(8)){
			Robot.navX.reset();
		}
	}
	
	public static double findHeading(double current, double head){
		double ans = (head - current + 360)%360;
		if(ans > 180)
			return ans - 360;
		else return ans;
	}
	public static double sig(double po){
		return (2/(1+Math.exp(-po/10)))-1;
	}
	
	public static void move(double x, double y){
		move(x, y, heading);
	}
	
	public static void move(double x, double y, double fHeading){
		pr= -sig(findHeading(Robot.navX.getFusedHeading(),fHeading));
		
		Robot.table.putNumber("DH", fHeading);
		
		double tl=(x+y)/2;
		double br=(x+y)/2;
		double tr=((y-x)/2); 
		double bl=((y-x)/2);
		
		br += pr;
		tr += pr;
	
		//double exc=Math.max(Math.max(Math.max(tl,br),tr),bl);
		//if(exc>1) {tl/=exc;br/=exc;tr/=exc;bl/=exc;} //Validation
		Robot.TL.set(tl);
		Robot.BR.set(br);
		Robot.TR.set(tr);
		Robot.BL.set(bl);
	}
	
	public static double scale(double axis, double buff){
		if(Math.abs(axis) > buff)
			return Math.signum(axis) * Math.pow (((Math.abs(axis) - buf)/(1 - buf)), 2);
		else return 0;
	}
	
	public static void orient(double fin){
		double orientA=findHeading(Robot.navX.getFusedHeading(), fin);
		double orientS;
		while(Math.abs(orientA)>3 && !Robot.logitech.getRawButton(12)){ 	
			orientS=Math.signum(orientA)*Math.max(0.15,Math.sqrt(Math.abs(orientA/720)));
			drive(orientS,-orientS);
			orientA=findHeading(Robot.navX.getFusedHeading(), fin);
			Timer.delay(0.005);
		}
		heading = Robot.navX.getFusedHeading();
		drive(0, 0);
	}
	

	public static double map(double input, double domainL, double domainR, double outL, double outR){
		double slope = 1.0 * (outR - outL) / (domainR - domainL);
		return outL + slope * (input - domainL);
	}
	
	public static void drive(double left, double right){
		Robot.TL.set(left);
		Robot.TR.set(right);
		Robot.BL.set(left);
		Robot.BR.set(right);
	}
	
	public static void shoot(long time){
		long initTime = System.currentTimeMillis();
		Robot.shooter.set(0.65);
		while(System.currentTimeMillis() - initTime <= time){};
		Robot.shooter.set(0);
	}
	
}
