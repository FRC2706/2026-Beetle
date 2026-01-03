package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Servo;

public class ElbowSubsystem extends SubsystemBase{
    private final Servo m_leftServo = new Servo(2);
    private final Servo m_rightServo = new Servo(3);

    // Rotates the servo to a certain degree
    public void rotateTo(double degree){
        m_leftServo.setAngle(degree);
        m_rightServo.setAngle(180 - degree);
    }

    // Rotates the servo from it's current position by some degree
    public void rotateBy(double degree){
        // Limit the possible angles to 0 and 180
        m_leftServo.setAngle( Math.min(Math.max(m_leftServo.getAngle() + degree,0),180));
        m_rightServo.setAngle(Math.min(Math.max(m_rightServo.getAngle() - degree,0),180));
    }
}