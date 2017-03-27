//Wednesday 22nd February @ 20:30
package org.usfirst.frc.team6024.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Robot extends IterativeRobot {
	public static Joystick logitech, second;
	public static NetworkTable table;
	public static VictorSP TL, TR, BR, BL;
	public static AHRS navX;
	public static VictorSP shooter, loader, winch;
	public static Solenoid backLight, blueLight, redLight;

	public void robotInit() {
		table = NetworkTable.getTable("datatable");
		logitech = new Joystick(0);
		second = new Joystick(1);
		navX = new AHRS(I2C.Port.kOnboard);
		navX.reset();
		TL = new VictorSP(2);
		TR = new VictorSP(1);
		BR = new VictorSP(9);
		BL = new VictorSP(7);
		shooter = new VictorSP(3);
		winch = new VictorSP(4);
		loader = new VictorSP(8);
		TR.setInverted(true);
		BR.setInverted(true);
		loader.setInverted(true);
		Auto.autoSetup();
		Movement.drive(0, 0);
		Vision.camera = NetworkTable.getTable("camera");
		Vision.visionInit();
		Auto.chooser = new SendableChooser<Integer>();
		Auto.typeChooser = new SendableChooser<Integer>();
		Auto.allianceChooser = new SendableChooser<Integer>();
		Auto.choose();

		dashboard();

		redLight = new Solenoid(1);
		blueLight = new Solenoid(6);
		backLight = new Solenoid(7);
		
		Second.joy = new Joystick(1);
		Second.camSwitch = new Toggle(Second.joy, 6);
	}

	public void disabled() {
		Movement.drive(0, 0);
		shooter.set(0);
		loader.set(0);
		dashboard();
		redLight.set(false);
		blueLight.set(false);
		backLight.set(false);
	}

	public void autonomousInit() {
		Robot.navX.reset();
		dashboard();
		Auto.autoInit();
		Movement.drive(0, 0);
		shooter.set(0);
		loader.set(0);
	}

	public void autonomousPeriodic() {
		dashboard();
		long randomvariable = 0;
		randomvariable++;
	}

	public void testPeriodic() {
		if (logitech.getRawButton(2)) {
			redLight.set(true);
		} else {
			redLight.set(false);
		}
		if (logitech.getRawButton(3)) {
			blueLight.set(true);
		} else {
			blueLight.set(false);
		}
		
		if(logitech.getRawButton(4)){
			backLight.set(true);
		}else{
			backLight.set(false);
		}

	}

	public void disabledInit() {
	}

	public static double xDist = 0, yDist = 0;

	public void teleopInit() {
		navX.reset();
		dashboard();
		Movement.drive(0, 0);
		int col = Auto.allianceChooser.getSelected();
		backLight.set(true);
		if (col == 1) {
			redLight.set(false);
			blueLight.set(false);
		}
		if (col == 2) {
			System.out.println("dkjbdsj,fv");
			// redLight.set(true);
			blueLight.set(false);
		}
		if (col == 3) {
			System.out.println("trgngn");
			redLight.set(false);
			blueLight.set(true);
		}
	}

	public void teleopPeriodic() {
		dashboard();
		Movement.teleOpMove();
		Second.run();
	}

	public static void dashboard() {
		table.putNumber("TL", TL.get());
		table.putNumber("TR", TR.get());
		table.putNumber("BL", BL.get());
		table.putNumber("BR", BR.get());
		table.putNumber("HE", navX.getFusedHeading());
		table.putNumber("Time", System.currentTimeMillis());
		
		table.putNumber("PTL", Movement.ptl);
		table.putNumber("PTR", Movement.ptr);
		table.putNumber("PBL", Movement.pbl);
		table.putNumber("PBR", Movement.pbr);
		Auto.autoDash();
	}
}