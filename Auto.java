//Sunday 19th February @ 20:30
package org.usfirst.frc.team6024.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Auto {

	public static Encoder ETL,ETR,EBR,EBL;
	public static int autoType;
	static final double spt = 0.3;
	static final double spd = 0.6;
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
		Robot.table.putNumber("Xaxis", xDist);
		Robot.table.putNumber("Yaxis", yDist);
	}
	public static double xDist = 0, yDist = 0;
	public static void vectorAddition(){
		xDist = ETR.get()/(2);
		yDist = ETR.get()/(2);
		
		xDist -= ETL.get()/(2);
		yDist += ETL.get()/(2);
	}
	
	public static void resetEnc(){
		EBR.reset();
		EBL.reset();
		ETR.reset();
		ETL.reset();
	}
	
	public static void autoInit() {
		// get value of type from SmartDashboard
		//int type = 14;
		int type = chooser.getSelected()  - 1 ;
		System.out.println(type);
		if(type==0){
			//do nothing
		}
		else if (type == 1) {
			BlueLeftNoSensors();
		} else if (type == 2){
			BlueMidNoSensors();
		} else if (type == 3) {
			BlueRightNoSensors();
		}else if (type == 4) {
			RedLeftNoSensors();
		}else if (type == 5) {
			RedMidNoSensors();
		}else if (type == 6) {
			RedRightNoSensors();
		}else if (type == 7) {
			BlueLeftSen();
		}else if (type == 8) {
			BlueMidSen();
		}else if (type == 9) {
			BlueRightSen();
		}else if (type == 10) {
			RedLeftSen();
		}else if (type == 11) {
			RedMidSen();
		}else if (type == 12) {
			RedRightSen();
		}else if (type == 13) {
			BlueLeftSecondNavX();
		}else if (type == 14) {
			BlueMidSecondNavX();
		}else if (type == 15) {
			BlueRightSecondNavX();
		}else if (type == 16) {
			RedLeftSecondNavX();
		}else if (type == 17) {
			RedMidSecondNavX();
		}else if (type == 18) {
			RedRightSecondNavX();
		}
		
	}


	public static SendableChooser<Integer> chooser;

	public static void choose() {
		chooser.addDefault(" Nothing ", (Integer) 1);
		chooser.addObject("Blue Left Time", (Integer) 2);
		chooser.addObject("Blue Mid Time", (Integer) 3);
		chooser.addObject("Blue Right Time", (Integer) 4);
		chooser.addObject("Red Left Time", (Integer) 5);
		chooser.addObject("Red Mid Time", (Integer) 6);
		chooser.addObject("Red Right Time", (Integer) 7);
		chooser.addObject("Blue Left Encoder NavX", (Integer) 8);
		chooser.addObject("Blue Mid Encoder NavX", (Integer) 9);
		chooser.addObject("Blue Right Encoder NavX", (Integer) 10);
		chooser.addObject("Red Left Encoder NavX", (Integer) 11);
		chooser.addObject("Red Mid Encoder NavX", (Integer) 12);
		chooser.addObject("Red Right Encoder NavX", (Integer) 13);
		chooser.addObject("Blue Left Time NavX", (Integer) 14);
		chooser.addObject("Blue Mid Time NavX", (Integer) 15);
		chooser.addObject("Blue Right Time NavX", (Integer) 16);
		chooser.addObject("Red Left Time NavX", (Integer) 17);
		chooser.addObject("Red Mid Time NavX", (Integer) 18);
		chooser.addObject("Red Right Time NavX", (Integer) 19);
	}

	public static void moveSeconds(double speedTR, double speedTL, long time) {
		long initTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - initTime < time) {
			Robot.TL.set(speedTL);
			Robot.BR.set(speedTL);
			Robot.TR.set(speedTR);
			Robot.BL.set(speedTR);
			Robot.dashboard();
		}
		Movement.drive(0,0);
	}

	public static void moveDis(double speedTR, double speedTL, double distance) {
		moveSeconds(speedTR, speedTL, (long) (distance / 2.84 * 1000));
	}
	public static void turnDegrees(double degrees, boolean isAntiClockwise) {
		double mult = 0.3;
		long time = (long) ((degrees / 91) * 1000);
		if (isAntiClockwise) {
			mult = 0.3;
		} else {
			mult = -0.3;
		}
		long initTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - initTime <= time) {
			Robot.TL.set(-mult);
			Robot.BR.set(mult);
			Robot.TR.set(mult);
			Robot.BL.set(-mult);
		}
		Movement.drive(0,0);
	}

	public static void moveDistance(double x, double y, double fHeading, double distance){
		resetEnc();
		xDist = 0;
		yDist = 0;
		double tar = Math.pow(distance*18.95, 2);
		while(xDist*xDist + yDist*yDist < tar){
			Movement.move(x, y, fHeading);
			vectorAddition();
			Robot.dashboard();
			System.out.println(xDist + "  " + yDist);
		}
		Movement.drive(0, 0);
	}
	public static void moveSecondNavX(double x,double y,double fHeading, double distance)
	{
		long time = (long)((distance/8.4) * 1000);
		long initTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - initTime < time) {
			Movement.move(x, y, fHeading);
			Robot.dashboard();
		}
		Movement.drive(0, 0);
	}
	
	public static void BlueLeftNoSensors() {
		moveDis(-0.3, 0.3, 8.6);
		turnDegrees(60, false);
		moveDis(-0.3, 0.3, 4.2);
		moveDis(0, 0, 9);
		moveDis(0.3, -0.3, 3); // should be 7
		turnDegrees(15, false);
		moveDis(-0.3, -0.3, 1.5);
		Movement.move(0, 0);
		Movement.shoot(15000);
	}

	public static void BlueRightNoSensors() {
		moveDis(-0.3, 0.3, 7.3);
		turnDegrees(60, true);
		moveDis(-0.3, 0.3, 2.5);
		moveDis(0, 0, 9);
		moveDis(0.3, -0.3, 4);
		moveDis(-0.6, 0, 1); // should be 12.5 not 1
	}

	public static void BlueMidNoSensors() {
		moveDis(spt, spt, 5.5);
		turnDegrees(90,true);
		moveDis(-spt, spt,1.7);
		moveDis(0, 0, 9);
		moveDis(spt, -spt, 4);
		moveDis(spt, spt, 2); // should be 8.5 we have made it 2 to test
		turnDegrees(45, false);
		Movement.shoot(15000);     
	}
	public static void RedMidNoSensors(){
		moveDis(-0.35, 0.3, 7.2);
    	moveDis(0, 0, 9);
		moveDis(0.3, -0.3, 4);
		moveDis(-0.3, -0.3, 2); // should be 8.5 we have made it 2 to test
		turnDegrees(45, true);
		Movement.shoot(15000);
	}
	public static void RedLeftNoSensors() {
		moveDis(-spt, spt, 7.2);
		turnDegrees(60, false);
		moveDis(-spt, spt, 4.2);
		moveDis(0, 0, 9);
		moveDis(spt, -spt, 4);
		moveDis(0.6, 0, 1); // should be 12.5 not 1
	}
	public static void RedRightNoSensors() {
		moveDis(-0.3, 0.3, 8.6);
		turnDegrees(60, true);
		moveDis(-0.3, 0.3, 3.5);
		moveDis(0, 0, 9);
		moveDis(0.3, -0.3, 3); // should be 7
		turnDegrees(15, false);
		moveDis(0.3,0.3, 1.5);
		Movement.move(0, 0);
		Movement.shoot(15000);
	}
	
	
	
	public static void BlueMidSen(){
		Robot.navX.reset();
		Timer.delay(0.2);
		moveDistance(spd, 0, Robot.navX.getFusedHeading(), 80);
		Timer.delay(3);
		moveDistance(-spd, 0, Robot.navX.getFusedHeading(), 48);
		moveDistance(0,spd,Robot.navX.getFusedHeading(),24);
		Movement.orient(45);
		Movement.shoot(15000);
	}
	
	public static void BlueLeftSen(){
		Robot.navX.reset();
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
	
	public static void BlueRightSen(){
		Robot.navX.reset();
		Timer.delay(0.2);
		Robot.navX.reset();
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),103);
		Movement.orient(300);
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveDistance(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		moveDistance(0.4,0.4,Robot.navX.getFusedHeading() ,12);// should be 300
	}
	
	public static void RedLeftSen(){
		Robot.navX.reset();
		Timer.delay(0.2);
		Robot.navX.reset();
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),103);
		Movement.orient(60);
		moveDistance(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveDistance(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		moveDistance(0.4,0.4,Robot.navX.getFusedHeading() ,12);// should be 300
	}
	
	public static void RedMidSen(){
		Robot.navX.reset();
		Timer.delay(0.2);
		Robot.navX.reset();
		moveDistance(0.4, 0,Robot.navX.getFusedHeading(), 86);
		Timer.delay(3);
		moveDistance(-0.4, 0, Robot.navX.getFusedHeading(), 48);
		moveDistance(0,0.4,Robot.navX.getFusedHeading(),24);
		Movement.orient(315);
		Movement.shoot(15000);
	}

	public static void RedRightSen(){
		Robot.navX.reset();
		Timer.delay(0.2);
		Robot.navX.reset();
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
	public static void BlueMidSecondNavX(){
		Robot.navX.reset();
		Timer.delay(0.2);
		moveSecondNavX(0.4, 0, 0, 80); 
		Timer.delay(3);
		moveSecondNavX(-0.4, 0, Robot.navX.getFusedHeading(), 48);
		moveSecondNavX(0,0.4,Robot.navX.getFusedHeading(),24);
		Movement.orient(45);
		Movement.shoot(15000);
	}
	
	public static void BlueLeftSecondNavX(){
		Robot.navX.reset();
		Timer.delay(0.2);
		Robot.navX.reset();
		moveSecondNavX(0.4,0,0,103);
		Movement.orient(60);
		moveSecondNavX(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveSecondNavX(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		Movement.orient(345);
		moveSecondNavX(0, -0.4,Robot.navX.getFusedHeading(), 18);
		moveSecondNavX(0, 0,Robot.navX.getFusedHeading(), 1);
		Movement.shoot(15000);
		
		
	}
	
	public static void BlueRightSecondNavX(){
		Robot.navX.reset();
		Timer.delay(0.2);
		Robot.navX.reset();
		moveSecondNavX(0.4,0,0,103);
		Movement.orient(300);
		moveSecondNavX(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveSecondNavX(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		moveSecondNavX(0.4,0.4,Robot.navX.getFusedHeading() ,12);// should be 300
	}
	
	public static void RedLeftSecondNavX(){
		Robot.navX.reset();
		Timer.delay(0.2);
		Robot.navX.reset();
		moveSecondNavX(0.4,0,0,103);
		Movement.orient(60);
		moveSecondNavX(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveSecondNavX(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		moveSecondNavX(0.4,0.4,Robot.navX.getFusedHeading() ,12);// should be 300
	}
	
	public static void RedMidSecondNavX(){
		Robot.navX.reset();
		Timer.delay(0.2);
		Robot.navX.reset();
		moveSecondNavX(0.4, 0,0, 86);
		Timer.delay(3);
		moveSecondNavX(-0.4, 0, Robot.navX.getFusedHeading(), 48);
		moveSecondNavX(0,0.4,Robot.navX.getFusedHeading(),24);
		Movement.orient(315);
		Movement.shoot(15000);
	}

	public static void RedRightSecondNavX(){
		Robot.navX.reset();
		Timer.delay(0.2);
		Robot.navX.reset();
		moveSecondNavX(0.4,0,0,103);
		Movement.orient(300);
		moveSecondNavX(0.4,0,Robot.navX.getFusedHeading(),50);
		Timer.delay(3);
		moveSecondNavX(-0.4,0,Robot.navX.getFusedHeading() ,36); // should be 84
		Movement.orient(15);
		moveSecondNavX(0, -0.4,Robot.navX.getFusedHeading(), 18);
		moveSecondNavX(0, 0,Robot.navX.getFusedHeading(), 1);
		Movement.shoot(15000);
	}
}
