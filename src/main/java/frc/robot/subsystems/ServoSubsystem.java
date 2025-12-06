package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Servo;

public class ServoSubsystem extends SubsystemBase{
    // Servo is connected to PWM 0
    private final Servo m_Servo = new Servo(0);

    public void turnTo(int degree){
        m_Servo.setAngle(degree);
    }
}
