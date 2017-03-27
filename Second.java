package org.usfirst.frc.team6024.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Second {
	public static Joystick joy;
	public static Toggle camSwitch;
	public static void run(){
		
		//Loader and Shooter
		if(joy.getRawButton(1)){
			double shootE=Math.abs(Auto.ES.getRate());
			Robot.shooter.set(0.765 + 0.1*(36.5 - Auto.ES.getRate()));
			if(shootE > 36)
				Robot.loader.set(0.32);
			else
				Robot.loader.set(0);
		}
		else{
			if(joy.getRawButton(7))
				Robot.shooter.set(-0.4);
			else
				Robot.shooter.set(0);
			
			if(joy.getRawButton(8))
				Robot.loader.set(-0.4);
			else	
				Robot.loader.set(0);
		}
		
		if(joy.getRawButton(5)){
			System.out.println("Vision");
			if(Vision.canRunGear())
				Vision.runGear(330);
		}else if(joy.getRawButton(3)){
			System.out.println("Vision");
			if(Vision.canRunGear())
				Vision.runGear(0);
		}else if(joy.getRawButton(4)){
			System.out.println("Vision");
			if(Vision.canRunGear())
				Vision.runGear(210);
		}
		
		//Winch
		if(joy.getRawButton(9))
			Robot.winch.set(0.65);
		else if(joy.getRawButton(10))
			Robot.winch.set(0.95);
		else if(joy.getRawButton(11))
			Robot.winch.set(0.3);
		else if(joy.getRawButton(12))
			Robot.winch.set(-0.85);
		else
			Robot.winch.set(0);
		camSwitch.isTrue();
		if(camSwitch.toggleState == 0 && Vision.camera.getString("task", "gear") != "gear"){
			Vision.camera.putString("task", "gear");
		}else if(camSwitch.toggleState == 1 && Vision.camera.getString("task", "gear") != "boil"){
			Vision.camera.putString("task", "boil");
		}else if(camSwitch.toggleState == 2 && Vision.camera.getString("task", "gear") != "wnch"){
			Vision.camera.putString("task", "wnch");
		}
		
		if(joy.getRawButton(2))
			Robot.navX.reset();
	}
}
