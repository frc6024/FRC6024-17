package org.usfirst.frc.team6024.robot;

public class ArchiveCode {
	//this is where the stuff we will keep for backup will go
	
	public static void moveSeconds(double speedTR, double speedTL, long time) {
		long initTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - initTime < time) {
			Robot.TL.set(speedTL);
			Robot.BR.set(speedTL);
			Robot.TR.set(speedTR);
			Robot.BL.set(speedTR);
		}
		Robot.TL.set(0);
		Robot.BR.set(0);
		Robot.TR.set(0);
		Robot.BL.set(0);

	}

	public static void moveDis(double speedTR, double speedTL, double distance) {
		moveSeconds(speedTR, speedTL, (long) (distance / 1.85 * 1000));
	}
	
	public static void BlueLeftwo() {
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

	public static void BlueRightwo() {
		moveDis(-0.3, 0.3, 7.3);
		turnDegrees(60, true);
		moveDis(-0.3, 0.3, 2.5);
		moveDis(0, 0, 9);
		
		moveDis(0.3, -0.3, 4);
		moveDis(-0.6, 0, 1); // should be 12.5 not 1
	}

	public static void BlueMidwo() {
		moveDis(-0.35, 0.3, 7.2);
		moveDis(0, 0, 9);
		moveDis(0.3, -0.3, 4);
		moveDis(0.3, 0.3, 2); // should be 8.5 we have made it 2 to test
		turnDegrees(45, false);
		Movement.shoot(15000);
	}
	public static void RedMidwo(){
		moveDis(-0.35, 0.3, 7.2);
		moveDis(0, 0, 9);
		moveDis(0.3, -0.3, 4);
		moveDis(-0.3, -0.3, 2); // should be 8.5 we have made it 2 to test
		turnDegrees(45, true);
		Movement.shoot(15000);
	}
	public static void RedLeftwo() {
		moveDis(-0.3, 0.3, 7.2);
		turnDegrees(60, false);
		moveDis(-0.3, 0.3, 4.2);
		moveDis(0, 0, 9);
		moveDis(0.3, -0.3, 4);
		moveDis(0.6, 0, 1); // should be 12.5 not 1
	}
	public static void RedRightwo() {
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
	
	public static void turnDegrees(double degrees, boolean isAntiClockwise) {
		long initTime = System.currentTimeMillis();
		long time = (long) ((degrees / 91) * 1000);
		if (isAntiClockwise) {
			while (System.currentTimeMillis() - initTime <= time) {
				Robot.TL.set(-0.3);
				Robot.BR.set(0.3);
				Robot.TR.set(0.3);
				Robot.BL.set(-0.3);
			}
		} else {
			while (System.currentTimeMillis() - initTime <= time) {
				Robot.TL.set(0.3);
				Robot.BR.set(-0.3);
				Robot.TR.set(-0.3);
				Robot.BL.set(0.3);
			}
		}
		Robot.TL.set(0);
		Robot.BR.set(0);
		Robot.TR.set(0);
		Robot.BL.set(0);
	}
	
	
}
