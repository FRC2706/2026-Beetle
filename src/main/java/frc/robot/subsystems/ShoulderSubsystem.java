package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Servo;

public class ShoulderSubsystem extends SubsystemBase{
    private final Servo m_servo = new Servo(3);

    public void rotateTo(double degree){
        m_servo.setAngle(degree);
    }

    public void rotateBy(double degree){
        m_servo.setAngle(Math.min(Math.max(m_servo.getAngle() + degree, 0), 180));
    }
}
