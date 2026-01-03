package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Servo;

public class ServoArmSubsystem extends SubsystemBase{
    // PWM's servo's are connecte to
     private final Servo m_leftJointServo = new Servo(3);
     private final Servo m_rightJointServo = new Servo(2);
     private final Servo m_bottomServo = new Servo(1);
     private final Servo m_leftClawServo = new Servo(4);
     private final Servo m_rightClawServo = new Servo(5);

    public ServoArmSubsystem(){

    }

    // Rotates the longest section of the arm connected to the bottom servo
    public void rotateArmTo(int degree){
        m_bottomServo.setAngle(degree);
    }

    // Rotates the middle section of the arm connected to the middle servos
    public void rotateJointTo(int degree){
        m_leftJointServo.setAngle(degree);
        m_rightJointServo.setAngle(180 - degree);
    }

    // Brings servos to eachother to grab something
    public void closeClaw(){
        m_leftClawServo.setAngle(90);
        m_rightClawServo.setAngle(90);
    }

    // Brings servos apart to release something
    public void openClaw(){
        m_leftClawServo.setAngle(180);
        m_rightClawServo.setAngle(0);
    }
}
