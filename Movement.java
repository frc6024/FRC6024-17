//Sunday 10th March, 4:10
package org.usfirst.frc.team6024.robot;

import edu.wpi.first.wpilibj.Timer;

public class Movement {

	public static final double buf = 0.1; //Ignore small movements on the joystick
	public static double heading = 0;
	 
	public static boolean changeHeading = false;
	public static void teleOpMove(){
		// Control the robot with a joystick
		
		double xaxis = Robot.logitech.getRawAxis(0);
		double yaxis = Robot.logitech.getRawAxis(1)*-1;
		double zaxis = Robot.logitech.getRawAxis(2);
		double slider = Robot.logitech.getRawAxis(3);
		double angle = Robot.navX.getFusedHeading();
		double tx = scale(xaxis, buf);
		double ty = scale(yaxis, buf);
		double tz = scale(zaxis, buf + 0.3);
		boolean boostButton = Robot.logitech.getRawButton(2);
		
		double boost = map(slider, 1, -1, 0.15, 0.65);
		if(boostButton) boost=0.95;
		
		double turnBoost = map(slider, 1, -1, 0.15, 0.65);
		if(boostButton) turnBoost=0.75;
	
		double multiplier = Math.max(Math.abs(tx), Math.abs(ty))/(Math.sqrt(tx*tx + ty*ty));
		tx *= 2*boost*multiplier;
		ty *= 2*boost*multiplier;
		tz *= turnBoost;
		
		if(Robot.logitech.getRawButton(1)){   //Brake
			drive(0, 0);
			changeHeading = true;
		}else if(tz!=0){ //Point Turn
			drive(tz,-tz);
			changeHeading = true;
		}else if(boostButton){ // Move Diagonal
			if(changeHeading){
				drive(0, 0);
				changeHeading = false;
				heading = angle;
			}
			move(tx,ty, angle, heading);
		}
		else if(Robot.logitech.getRawButton(4)){ // Swerve Drive
			swerveDrive(tx,ty,0);
		}
		else if(tx != 0 || ty != 0){      //Move
			if(changeHeading){
				drive(0, 0);
				heading = angle;
				changeHeading = false;
			}
			if(Math.abs(ty)>=Math.abs(tx))
				move(0,ty, angle, heading);	
			else
				move(tx,0, angle, heading);
			
		}else if(Robot.logitech.getRawButton(5)){
			orient(Robot.logitech.getPOV());
		}else if(Robot.logitech.getRawButton(6)){
			orient(180);
		}else if(Robot.logitech.getRawButton(7)){
			orient(60);
		}else if(Robot.logitech.getRawButton(8)){
			orient(300);
		}else if(Robot.logitech.getRawButton(9)){
			orient(45);
		}else{
			drive(0, 0);
			changeHeading = true;
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
	
	public static void move(double x, double y, double curAngle, double fHeading){
		double pr = -0.4*sig(findHeading(curAngle,fHeading));
		
		Robot.table.putNumber("DH", fHeading);
		
		double tl=(x+y)/2;
		double br=(x+y)/2;
		double tr=(y-x)/2; 
		double bl=(y-x)/2;
		
		if(y == 0 || x != 0){
			if(y == 0 && x > 0){
				br += pr;
				tr += pr;
			}else if(y == 0 && x < 0){
				br += pr;
				tr += pr;
			}else{
				br += pr;
				tr += pr;
			}
		}
		
		tr *= Math.abs(((tr/br)*(Auto.EBR.getRate()/Auto.ETR.getRate())));
		bl *= Math.abs(((bl/tl)*(Auto.ETL.getRate()/Auto.EBL.getRate())));
		
		Robot.TL.set(tl);
		Robot.BR.set(br);
		Robot.TR.set(tr);
		Robot.BL.set(bl);
	}
	
	static double ptl = 0.85;
	static double ptr = 0.85;
	static double pbl = 0.85;
	static double pbr = 0.85;
	
	public static void encoderMove(double x, double y){
		double mult = 1800, div = 10000;
		double ftl = (x+y)*0.4;
		double fbr = (x+y)*0.4;
		double ftr = (y-x)*0.4;
		double fbl = (y-x)*0.4;
		
		Robot.table.putNumber("TTL", ftl*mult);
		Robot.table.putNumber("TTR", ftr*mult);
		Robot.table.putNumber("TBL", fbl*mult);
		Robot.table.putNumber("TBR", fbr*mult);
		
		ptl = (Math.abs(ftl*mult) - Math.abs(Auto.ETL.getRate()))/div;
		ptr = (Math.abs(ftr*mult) - Math.abs(Auto.ETR.getRate()))/div;
		pbl = (Math.abs(fbl*mult) - Math.abs(Auto.EBL.getRate()))/div;
		pbr = (Math.abs(fbr*mult) - Math.abs(Auto.EBR.getRate()))/div;
		
		Robot.TL.set(ftl+ptl);
		Robot.BR.set(fbr+pbr);
		Robot.TR.set(ftr+ptr);
		Robot.BL.set(fbl+pbl);
	}

	public static void movewo(double x, double y){

		double tl=(x+y)/2;
		double br=(x+y)/2;
		double tr=((y-x)/2); 
		double bl=((y-x)/2);
		Robot.TL.set(tl);
		Robot.BR.set(br);
		Robot.TR.set(tr);
		Robot.BL.set(bl);
	}
	
	public static double scale(double axis, double buff){
		if(Math.abs(axis) > buff)
			return Math.signum(axis) * Math.pow (((Math.abs(axis) - buff)/(1 - buff)), 2);
		else return 0;
	}
	
	public static void orient(double fin){
		double orientA=findHeading(Robot.navX.getFusedHeading(), fin);
		long startTime = System.currentTimeMillis();
		double orientS;
		while(Math.abs(orientA)>4 && !Robot.logitech.getRawButton(12) && System.currentTimeMillis() - startTime < 5000){ 	
			orientS=Math.signum(orientA)*Math.max(0.3,Math.sqrt(Math.abs(orientA/720)));
			orientS = Math.signum(orientS)*Math.max(Math.abs(orientS), 0.08);
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
	
	
	public static double[] moveRelative(double x, double y){
		double jPol=Math.toDegrees(Math.atan2(y, x));
		double r=Math.sqrt(x*x + y*y);
		double relMove=findHeading(Robot.navX.getFusedHeading(),jPol);
		double nY =r*Math.sin(Math.toRadians(-relMove - 90));
		double nX =r*Math.cos(Math.toRadians(-relMove - 90));
		
		double tl=(nX+nY);
		double br=(nX+nY);
		double tr=(nY-nX); 
		double bl=(nY-nX);
		return new double[]{tl, tr, br, bl}; 
	}
	
	public static void swerveDrive(double x,double y,double z){
		double[] array = moveRelative(x, y);
		Robot.TL.set(array[0]*0.4 + z*0.15);
		Robot.TR.set(array[1]*0.4 - z*0.15);
		Robot.BR.set(array[2]*0.4 - z*0.15);
		Robot.BL.set(array[3]*0.4 + z*0.15);
	}
	
	public static void shoot(long time){
		long initTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - initTime <= time){
			double shootE=Math.abs(Auto.ES.getRate());
			Robot.shooter.set(0.7 + 0.1*(35 - Auto.ES.getRate()));
			if(shootE > 34)
				Robot.loader.set(0.38);
			else
				Robot.loader.set(0);
		}
		Robot.shooter.set(0);
		Robot.loader.set(0);
	}
	
}
