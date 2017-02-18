package org.usfirst.frc.team6024.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Auto {

	public static Encoder ETL,ETR,EBR,EBL;
	public static int autoType;
	
	public static void autoSetup(){
		EBL = new Encoder(6, 7,false,Encoder.EncodingType.k2X);
		EBR = new Encoder(4, 5,false,Encoder.EncodingType.k2X);
		ETR = new Encoder(2, 3,true,Encoder.EncodingType.k2X);
		ETL = new Encoder(0, 1,true,Encoder.EncodingType.k2X);
	}
	
	public static void autoDash(){
		Robot.table.putNumber("ETL", ETL.get());
		Robot.table.putNumber("ETR", ETR.get());
		Robot.table.putNumber("EBL", EBL.get());
		Robot.table.putNumber("EBR", EBR.get());
		Robot.table.putNumber("Xaxis", xDist*0.0523);
		Robot.table.putNumber("Yaxis", yDist*0.0523);
	}
	
	public static void autoInit() {
		// get value of type from SmartDashboard
		int type = 0;
		autoType = chooser.getSelected();
		System.out.println(autoType);
		if (type == 1) {
			BlueLeft();
		} else if (type == 2){
			BlueMid();
		} else if (type == 3) {
			BlueRight();
		}else if (type == 4) {
			RedLeft();
		}else if (type == 5) {
			RedMid();
		}else if (type == 6) {
			RedRight();
		}
	}

	public static SendableChooser<Integer> chooser;

	public static void choose() {
		chooser.addDefault(" Nothing ", (Integer) 1);
		chooser.addObject("Blue Left", (Integer) 2);
		chooser.addObject("Blue Mid", (Integer) 3);
		chooser.addObject("Blue Right", (Integer) 4);
	}


	public static void moveDistance(double x, double y, double fHeading, double distance){
		distance -= 5;
		resetEnc();
		xDist = 0;
		yDist = 0;
		double tar = Math.pow(distance*19.1, 2);
		while(xDist*xDist + yDist*yDist < tar){
			Movement.move(x, y, fHeading);
			vectorAddition();
			Robot.dashboard();
			System.out.println(xDist + "  " + yDist);
		}
		Movement.drive(0, 0);
	}
	
	public static void BlueMid(){
		moveDistance(0.4, 0, Robot.navX.getFusedHeading(), 86);
		Timer.delay(3);
		moveDistance(-0.4, 0, Robot.navX.getFusedHeading(), 48);
		moveDistance(0,0.4,Robot.navX.getFusedHeading(),24);
		Movement.orient(45);
		Movement.shoot(15000);
	}
	
	public static void BlueLeft(){
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),103);
		Movement.orient(60);
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveDistance(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		Movement.orient(345);
		moveDistance(0, -0.4,Robot.navX.getFusedHeading(), 18);
		moveDistance(0, 0,Robot.navX.getFusedHeading(), 1);
		Movement.shoot(15000);
		
		
	}
	
	public static void BlueRight(){
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),103);
		Movement.orient(300);
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveDistance(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		moveDistance(0.4,0.4,Robot.navX.getFusedHeading() ,12);// should be 300
	}
	
	public static void RedLeft(){
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),103);
		Movement.orient(60);
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveDistance(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		moveDistance(0.4,0.4,Robot.navX.getFusedHeading() ,12);// should be 300
	}
	
	public static void RedMid(){
		moveDistance(0.4, 0, Robot.navX.getFusedHeading(), 86);
		Timer.delay(3);
		moveDistance(-0.4, 0, Robot.navX.getFusedHeading(), 48);
		moveDistance(0,0.4,Robot.navX.getFusedHeading(),24);
		Movement.orient(315);
		Movement.shoot(15000);
	}

	public static void RedRight(){
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),103);
		Movement.orient(300);
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveDistance(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		Movement.orient(15);
		moveDistance(0, -0.4,Robot.navX.getFusedHeading(), 18);
		moveDistance(0, 0,Robot.navX.getFusedHeading(), 1);
		Movement.shoot(15000);
	}
		
		
	
	public static double xDist = 0, yDist = 0;
	public static void vectorAddition(){
		xDist = ETR.get()/(2);
		yDist = ETR.get()/(2);
		
		xDist -= EBR.get()/(2);
		yDist += EBR.get()/(2);
	}
	
	public static void resetEnc(){
		EBR.reset();
		EBL.reset();
		ETR.reset();
		ETL.reset();
	}
	
}
