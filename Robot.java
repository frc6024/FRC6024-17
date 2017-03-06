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
	public static DriverStation ds;
	public static Solenoid backLight, blueLight, redLight,temp;
	public void robotInit(){
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
		Vision.camera.putString("task", "stall");
		Vision.visionInit();
		Auto.chooser = new SendableChooser<Integer>();
		Auto.typeChooser = new SendableChooser<Integer>();
		Auto.allianceChooser =  new SendableChooser<Integer>();
		Auto.choose();
		
		dashboard();
		ds = DriverStation.getInstance();

		redLight = new Solenoid(1);
		blueLight = new Solenoid(3);
		backLight = new Solenoid(4);
	}
	
	public void disabled(){
		Movement.drive(0,0);
		dashboard();
		redLight.set(false);
		blueLight.set(false);
		backLight.set(false);
	}
	
	public void autonomousInit(){
		Robot.navX.reset();
		dashboard();
		Auto.autoInit();
		Movement.drive(0, 0);
	}

	
	public void autonomousPeriodic(){
		dashboard();
		long randomvariable = 0;
		randomvariable++;
	}
	boolean edge;
	long tim;
	double ls=0;
	double sp=0;
	public void testPeriodic(){
		sp=((logitech.getRawAxis(3)*-1)+1)/2;
		ls=((second.getRawAxis(3)*-1)+1)/2;
		if(logitech.getRawButton(1)){
//			if(edge){
//				tim=System.currentTimeMillis();
//				edge=false;
//			}
//			
//			if((System.currentTimeMillis()-tim)>2000){
//				
//				loader.set(ls);
//			}
			 
			shooter.set(sp);
			if(Math.abs(Auto.ES.getRate()) > 35)
				loader.set(ls);
			
		}
		else{
			edge=true;
			shooter.set(0);
			loader.set(0);
		}
		System.out.println(sp+" | "+ls+" | "+Auto.ES.getRate());
		//loader.set(-0.1);
		if(logitech.getRawButton(2)){
			redLight.set(true);
		}
		else{
			redLight.set(false);
		}
		if(logitech.getRawButton(3)){
			blueLight.set(true);
		}
		else{
			blueLight.set(false);
		}
		
	}
	
	public void disabledInit(){
		Vision.camera.putString("task", "stall");
	}
	
	
	public static double xDist = 0, yDist = 0;
	
	public void teleopInit(){
		
		dashboard();
		Movement.drive(0, 0);
		Vision.camera.putString("task", "stall");
		int col = Auto.allianceChooser.getSelected();
		 backLight.set(true);
		 if(col==1){
			redLight.set(false);
			blueLight.set(false);
		 }
		 if(col==2){
			redLight.set(true);
			blueLight.set(false);
		 }
		 if(col==3){
			redLight.set(false);
			blueLight.set(true);
		 }
	}

	public void teleopPeriodic(){
		dashboard();
		Movement.teleOpMove();
		if(logitech.getRawButton(10) && Vision.canRunGear()){
			Vision.runGear();
		}
		secondJoystick();
	}	
	
	
	public static void dashboard(){
		table.putNumber("TL", TL.get());
		table.putNumber("TR", TR.get());
		table.putNumber("BL", BL.get());
		table.putNumber("BR", BR.get());
		table.putNumber("HE", navX.getFusedHeading());
		table.putNumber("Time", System.currentTimeMillis());
		Auto.autoDash();
	}
	
	public static void secondJoystick(){

		if(second.getRawButton(9))
			winch.set(0.3);
		else if(second.getRawButton(10))
			winch.set(0.7);
		else if(second.getRawButton(11))
			winch.set(-0.3);
		else if(second.getRawButton(12))
			winch.set(-0.7);
		else
			winch.set(0);
		
		if(second.getRawButton(3))
			shooter.set(0.6);
		else if(second.getRawButton(4))
			shooter.set(-0.6);
		else if(second.getRawButton(5))
			shooter.set(0.8);
		else if(second.getRawButton(6))
			shooter.set(-0.8);
		else if(second.getRawButton(1))
			shooter.set(second.getRawAxis(3));
		else
			shooter.set(0);
		
		Auto.autoDash();
		table.putNumber("shoot", second.getRawAxis(3));
		table.putNumber("voltage", ds.getBatteryVoltage());
	}
}