package org.usfirst.frc.team6024.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Toggle {
	Joystick finalstick;
	int buttonvalue;
	boolean currentState= false, previousState= false;
	public int toggleState;
	
	public Toggle(Joystick stick, int button){
		finalstick= stick;
		buttonvalue = button;
		toggleState = 0;
	}
	
	public int isTrue(){
		currentState= finalstick.getRawButton(buttonvalue);
		if(!previousState && currentState)			toggleState++;
		toggleState %= 3;
		previousState = currentState;
		return toggleState;
	}
}
