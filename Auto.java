//Wednesday 22nd February @
package org.usfirst.frc.team6024.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto {

	public static Encoder ETL,ETR,EBR,EBL, ES, EL;
	public static int autoType;
	static final double spd = 0.4;
	
	public static void autoSetup(){
		EBL = new Encoder(7, 8,true,Encoder.EncodingType.k2X);
		EBR = new Encoder(5, 6,false,Encoder.EncodingType.k2X);
		ETR = new Encoder(3, 4,false,Encoder.EncodingType.k2X);
		ETL = new Encoder(1, 2,true,Encoder.EncodingType.k2X);
		ES = new Encoder(9, 0, true, Encoder.EncodingType.k2X);
		EL = new Encoder(8,0,true, Encoder.EncodingType.k2X);
		ES.setDistancePerPulse(0.05);
	}
	
	public static void autoDash(){
		Robot.table.putNumber("ETL", ETL.get());
		Robot.table.putNumber("ETR", ETR.get());
		Robot.table.putNumber("EBL", EBL.get());
		Robot.table.putNumber("EBR", EBR.get());
		Robot.table.putNumber("ES", ES.get());
		//Robot.table.putNumber("EL", EL.get());	
		Robot.table.putNumber("Rate", ES.getRate());
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
		 int pos = chooser.getSelected();
		 int type = typeChooser.getSelected();
		 int col = allianceChooser.getSelected();
		 Robot.backLight.set(true);
		 if(col==1){
			Robot.redLight.set(false);
			Robot.blueLight.set(false);
		 }
		 if(col==2){
				Robot.redLight.set(true);
				Robot.blueLight.set(false);
		 }
		 if(col==3){
				Robot.redLight.set(false);
				Robot.blueLight.set(true);
		 }
		 if(pos == 4 && type == 4)
			 BlueMidVis();
		 
		 if(pos == 1){
			 //Run Nothing
		 }else if(pos == 2){
			 BlueLeft(type);
		 }else if(pos == 3){
			 BlueRight(type);
		 }else if(pos == 4){
			 BlueMid(type);
		 }else if(pos == 5){
			 RedLeft(type);
		 }else if(pos == 6){
			 RedMid(type);
		 }else if(pos == 7){
			 RedRight(type);
		 }
		 
		 
	}

	public static SendableChooser<Integer> chooser, typeChooser, allianceChooser;

	public static void choose() {
		chooser.addDefault("Nothing", (Integer) 1);
		chooser.addObject("BlueLeft", (Integer)2);
		chooser.addObject("BlueRight", (Integer)3);
		chooser.addObject("BlueMid", (Integer)4);
		chooser.addObject("RedLeft", (Integer)5);
		chooser.addObject("RedMid", (Integer)6);
		chooser.addObject("RedRight", (Integer)7);
		
		typeChooser.addDefault("Sensors", (Integer)1);
		typeChooser.addObject("NavX only", (Integer)2);
		typeChooser.addObject("No sensors", (Integer)3);
		typeChooser.addObject("VISION!!", (Integer)4);
		
		allianceChooser.addDefault("Off", (Integer)1);
		allianceChooser.addObject("Red", (Integer)2);
		allianceChooser.addObject("Blue", (Integer)3);
		
		
		SmartDashboard.putData("ChooserPos", chooser);
		SmartDashboard.putData("ChooserType", typeChooser);
		SmartDashboard.putData("ChooserAll", allianceChooser);
	}
	
	public static void epicMove(double x, double y, double distance, int type){
		if(type == 1){ 			//Move with encoders and navX
			moveDistance(x,y, Robot.navX.getFusedHeading(), distance);
		}else if(type == 2){	//Move with navX
			moveSecondNavX(x, y, Robot.navX.getFusedHeading(), distance);
		}else if(type == 3){	//Move without sensors
			moveTime(x,y, distance);
		}else{					//Verification
			System.out.println("ERROR");
		}
	}
	
	public static void epicTurn(double degrees, int type){
		if(type == 1 || type == 2){			//Turn with navX
			Movement.orient((Robot.navX.getFusedHeading() + 360 + degrees)%360);
		}else if(type == 3){				//Turn in seconds
			turnDegrees(degrees);
		}
	}
	
	public static void moveTime(double x, double y, double distance){
		distance = (distance /8.4 * 1000);
		long initTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - initTime < distance) {
			Movement.movewo(x, y);
			Robot.dashboard();
		}
		Movement.drive(0,0);
	}
	
	public static void turnDegrees(double degrees) {
		double mult = 0.3;
		long time = (long) ((degrees / 91) * 1000);
		long initTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - initTime <= time) {
			Robot.TL.set(Math.signum(degrees)* mult );
			Robot.BR.set(Math.signum(degrees)* -mult);
			Robot.TR.set(Math.signum(degrees)* -mult);
			Robot.BL.set(Math.signum(degrees)* mult );
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
	
//	public static void moveDistance(double x, double y,double fHeading, double fDist){
//		resetEnc();
//		double Dist = 0; 
//		while(fDist > Dist)
//		{
//			Movement.move(x, y, fHeading);
//			Dist = (ETR.get()/360) * 18.95;
//		}
//	}
	

	
	public static void moveSecondNavX(double x,double y,double fHeading, double distance){
		long time = (long)((distance/8.4) * 1000);
		long initTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - initTime < time) {
			Movement.move(x, y, fHeading);
			Robot.dashboard();
		}
		Movement.drive(0, 0);
	}
	
	public static void BlueMid(int type){
		Timer.delay(0.2);
		epicMove(spd, 0, 80, type);
		moveDistance(spd, 0, 0, 80);
		Timer.delay(3);
		epicMove(-spd, 0, 48, type);
		epicMove(0, spd, 24, type);
		epicTurn(45, type);
		Movement.shoot(15000);
	}
	
	public static void BlueLeft(int type){
		epicMove(0, spd, 82, type);
		epicTurn(-30, type);
		epicMove(spd, 0, 22, type);
		Timer.delay(3);
		epicMove(-spd, 0, 36, type);
		epicTurn(15, type);
		epicMove(0, -spd, 18, type);
		Movement.shoot(15000);
	}
	
	public static void BlueRight(int type){
		Timer.delay(0.2);
		epicMove(0, spd, 103, type);
		epicTurn(30, type);
		epicMove(spd, 0, 50, type);
		Timer.delay(3);
		epicMove(-spd, 0, 36, type); //should be 84
		epicMove(spd, spd, 12, type);//should be 300
	}
	
	
	public static void RedLeft(int type){
		Timer.delay(0.2);
		epicMove(0, spd, 82, type);
		epicTurn(-30, type);
		epicMove(spd, 0, 50, type);
		Timer.delay(3);
		epicMove(-spd, 0, 36, type); //should be 84
		epicMove(spd, spd, 12, type);//should be 300
	}
	
	public static void RedMid(int type){
		epicMove(spd,0,86,type);
		Timer.delay(3);
		epicMove(-spd, 0,48,type);
		epicMove(0,spd,24,type);
		epicTurn(-45,type);
		Movement.shoot(15000);
	}

	public static void RedRight(int type){
		epicMove(0, spd, 82, type);
		epicTurn(30, type);
		epicMove(spd, 0, 22, type);
		Timer.delay(3);
		epicMove(-spd, 0, 36, type);
		epicTurn(-15, type);
		epicMove(0, -spd, 18, type);
		Movement.shoot(15000);
		
	}
	public static void BlueMidVis(){
		Robot.navX.reset();
		moveDistance(0.6, 0, 0, 20);
		
		Vision.runGear();
		Timer.delay(3);
		moveDistance(-spd, 0, Robot.navX.getFusedHeading(), 48);
		moveDistance(0,spd,Robot.navX.getFusedHeading(),24);
		Movement.orient(45);
		Movement.shoot(15000);
	}
		
	public static void BlueLeftVis(){
		Robot.navX.reset();
		moveDistance(0,spd,0,82);
		Movement.orient(330);
		Vision.runGear();
		Timer.delay(3);
		moveDistance(-spd,0,Robot.navX.getFusedHeading() ,36); // should be 84
		Movement.orient(345);
		moveDistance(0, -spd,Robot.navX.getFusedHeading(), 18);
		Movement.shoot(15000);
	}
			
	public static void BlueRightVis(){
		Robot.navX.reset();
		Timer.delay(0.2);
		moveDistance(0,spd,Robot.navX.getFusedHeading(),103);
		Movement.orient(30);
		Vision.runGear();
		Timer.delay(3);
		moveDistance(-spd,0,Robot.navX.getFusedHeading() ,36);           // should be 84
		moveDistance(spd,spd,Robot.navX.getFusedHeading() ,12);// should be 300
	}
}
