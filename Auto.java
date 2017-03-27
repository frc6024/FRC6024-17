//Wednesday 22nd February @
package org.usfirst.frc.team6024.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto {

	public static Encoder ETL, ETR, EBR, EBL, ES;
	public static int autoType;
	static double spd = 0.6;
	static double inchesPerSecond = 7;
	static double wheelCirc = 18.84;
	static int shootTime = 5000;
	static int wait = 3;
	static double driftConstant = 4;

	public static void autoSetup() {
		EBL = new Encoder(7, 8, true, Encoder.EncodingType.k2X);
		EBR = new Encoder(5, 6, false, Encoder.EncodingType.k2X);
		ETR = new Encoder(3, 4, false, Encoder.EncodingType.k2X);
		ETL = new Encoder(1, 2, true, Encoder.EncodingType.k2X);
		ES = new Encoder(0, 9, true, Encoder.EncodingType.k2X);
		ES.setDistancePerPulse(0.05);
	}
	
	public static void autoDash() {
		Robot.table.putNumber("ETL", ETL.getRate());
		Robot.table.putNumber("ETR", ETR.getRate());
		Robot.table.putNumber("EBL", EBL.getRate());
		Robot.table.putNumber("EBR", EBR.getRate());
		Robot.table.putNumber("ES", ES.get());
		Robot.table.putNumber("Rate", ES.getRate());
		Robot.table.putNumber("Xaxis", xDist);
		Robot.table.putNumber("Yaxis", yDist);

	}

	public static double xDist = 0, yDist = 0;

	public static void vectorAddition() {
		xDist = ETR.get() / (2);
		yDist = ETR.get() / (2);

		xDist -= ETL.get() / (2);
		yDist += ETL.get() / (2);
	}

	public static void resetEnc() {
		EBR.reset();
		EBL.reset();
		ETR.reset();
		ETL.reset();
	}

	public static void autoInit() {
		// get value of type from SmartDashboard
		int col = allianceChooser.getSelected();
		Robot.navX.reset();
		if (col == 1) {
			Robot.redLight.set(false);
			Robot.blueLight.set(false);
			Robot.backLight.set(false);
		}
		if (col == 2) {
			Robot.redLight.set(true);
			Robot.backLight.set(true);
			Robot.blueLight.set(false);
		}
		if (col == 3) {
			Robot.redLight.set(false);
			Robot.backLight.set(false);
			Robot.blueLight.set(true);
		}
		int numchoose = chooser.getSelected();
		//numchoose = 2;
		if (numchoose == 1)
			mid();//moveDistance(1.4, 0, Robot.navX.getFusedHeading(), 130);
		if (numchoose == 2)
			left();
		if (numchoose == 3)
			mid();
		if (numchoose == 4)
			right();
		if (numchoose == 5)
			LeftVis();
		if (numchoose == 6)
			MidVis();
		if (numchoose == 7)
			RightVis();
		if (numchoose == 8)
			shootRedDirect();
		if (numchoose == 9)
			shooterRedHopper();
		if (numchoose == 10)
			shooterBlueHopper();

	}

	public static SendableChooser<Integer> chooser, typeChooser, allianceChooser;

	public static void choose() {
		chooser.addDefault("Nothing", (Integer) 1);
		chooser.addObject("Left", (Integer) 2);
		chooser.addObject("Mid", (Integer) 3);
		chooser.addObject("Right", (Integer) 4);
		chooser.addObject("LeftVis", (Integer) 5);
		chooser.addObject("MidVis", (Integer) 6);
		chooser.addObject("RightVis", (Integer) 7);
		chooser.addObject("Shooter Redd", (Integer) 8);
		chooser.addObject("Shooter&HopperRED", (Integer) 9);
		chooser.addObject("Shooter&HopperBLUE", (Integer) 10);

		/*
		 * typeChooser.addDefault("Sensors", (Integer) 1);
		 * typeChooser.addObject("NavX only", (Integer) 2);
		 * typeChooser.addObject("No sensors", (Integer) 3);
		 * typeChooser.addObject("VISION!!", (Integer) 4);
		 */
		allianceChooser.addDefault("Off", (Integer) 1);
		allianceChooser.addObject("Red", (Integer) 2);
		allianceChooser.addObject("Blue", (Integer) 3);

		SmartDashboard.putData("ChooserPos", chooser);
		SmartDashboard.putData("ChooserAlliance", allianceChooser);
	}

	public static void left() {
		moveDistance(0, spd, Robot.navX.getFusedHeading(), 83);
		epicTurn(-30, 1);
		moveDistance(1.2, 0, Robot.navX.getFusedHeading(), 60);
	}

	public static void mid() {
		Timer.delay(0.3);
		moveDistance(0, spd, Robot.navX.getFusedHeading(), 50);
		epicTurn(-90, 1);
		moveDistance(1, 0, Robot.navX.getFusedHeading(), 50);
	}

	public static void right() {
		Robot.navX.setAngleAdjustment(180);
		moveDistance(0, -spd, Robot.navX.getFusedHeading(), 85.5);
		epicTurn(30, 1);
		moveDistance(1.2, 0, Robot.navX.getFusedHeading(), 60);
	}

	public static void shootRedDirect() {
		Robot.navX.setAngleAdjustment(190);
		Movement.shoot(12000);
		moveauto(0.0, 1.3, 3);
		// moveDistance(0,0.9,Robot.navX.getFusedHeading(),80);
	}
	public static void shootBlueDirect() {
		Robot.navX.setAngleAdjustment(340);
		Movement.shoot(12000);
		moveauto(0.0, 1.3, 3);
		// moveDistance(0,0.9,Robot.navX.getFusedHeading(),80);
	}

	public static void shooterRedHopper() {
		Robot.navX.setAngleAdjustment(190);
		Movement.shoot(4000);
		moveauto(0.0, 0.6, 2);
		moveDistance(0, -1.3, Robot.navX.getFusedHeading(), 80);
		moveauto(-1.5, 0.0, 4);
		moveauto(1, 0, 1.5);
		moveDistance(0, 1, Robot.navX.getFusedHeading(), 60);
		if (Timer.getMatchTime() < 15) {
			epicTurn(45, 1);
			Robot.shooter.set(-0.4);
			Robot.loader.set(-0.3);
			Timer.delay(0.4);
			Robot.shooter.set(0);
			Robot.loader.set(0);
		}
		Movement.shoot((long) Math.max(0.0, (15 - Timer.getMatchTime()) *1000));
	}

	public static void shooterBlueHopper() {
		Robot.navX.setAngleAdjustment(340);
		Movement.shoot(9000);
		moveauto(0.0, -0.6, 2);
		moveDistance(0, 0.9, Robot.navX.getFusedHeading(), 80);
		moveauto(-1.5, 0.0, 4);
		moveauto(1.5, 0, 1.5);
		moveDistance(0, -0.6, Robot.navX.getFusedHeading(), 60);
		epicTurn(-45, 1);
		Robot.shooter.set(-0.4);
		Robot.loader.set(-0.3);
		Timer.delay(0.4);
		Robot.shooter.set(0);
		Robot.loader.set(0);
		Movement.shoot((long) Math.max(0.0, (15 - Timer.getMatchTime()) *1000));
	}

	public static void moveauto(double x, double y, double moveautotime) {
		long iniTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - iniTime < moveautotime * 1000) {
			if (Timer.getMatchTime() > 15 || Robot.logitech.getRawButton(12)){
				Movement.drive(0, 0);
				return;
			}
			double tl = (x + y) / 2;
			double br = (x + y) / 2;
			double tr = ((y - x) / 2);
			double bl = ((y - x) / 2);
			Robot.TL.set(tl);
			Robot.BR.set(br);
			Robot.TR.set(tr);
			Robot.BL.set(bl);
		}
		Movement.drive(0, 0);
	}

	public static void epicTurn(double degrees, int type) {
		Movement.orient((Robot.navX.getFusedHeading() + 360 + degrees) % 360);
	}

	public static void moveDistance(double x, double y, double fHeading, double distance) {
		distance -= driftConstant;
		long startTime = System.currentTimeMillis();
		resetEnc();
		xDist = 0;
		yDist = 0;
		double tar = Math.pow(distance * wheelCirc, 2);
		while (xDist * xDist + yDist * yDist < tar) {
			if (Timer.getMatchTime() > 15 || Robot.logitech.getRawButton(12))
				break;
			Movement.move(x, y, Robot.navX.getFusedHeading(), fHeading);
			vectorAddition();
			Robot.dashboard();
			// System.out.println(xDist + " " + yDist);
		}
		Movement.drive(0, 0);
	}

	public static void MidVis() {
		System.out.println("mid vis");
		moveDistance(0, spd, Robot.navX.getFusedHeading(), 40);
		epicTurn(-90, 1);
		if (Vision.canRunGear())
			Vision.runGear(0);

		// Movement.shoot(5000);
	}

	public static void LeftVis() {
		moveDistance(0, spd, Robot.navX.getFusedHeading(), 73);
		epicTurn(-30, 1);
		moveDistance(spd, 0, Robot.navX.getFusedHeading(), 10);
		if (Vision.canRunGear())
			Vision.runGear(330);

	}

	public static void RightVis() {
		Robot.navX.setAngleAdjustment(180);
		moveDistance(0, -spd, Robot.navX.getFusedHeading(), 73);
		epicTurn(30, 1);
		moveDistance(spd, 0, Robot.navX.getFusedHeading(), 10);
		if (Vision.canRunGear())
			Vision.runGear(210);
	}

}
