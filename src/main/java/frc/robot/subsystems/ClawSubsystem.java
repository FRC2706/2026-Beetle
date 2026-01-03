package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Servo;

public class ClawSubsystem extends SubsystemBase{
    private final Servo m_leftServo = new Servo(4);
    private final Servo m_rightServo = new Servo(5);

    public void openClaw(){
        m_leftServo.setAngle(180);
        m_rightServo.setAngle(0);
    }

    public void closeClaw(){
        m_leftServo.setAngle(180);
        m_rightServo.setAngle(0);
    }

}
