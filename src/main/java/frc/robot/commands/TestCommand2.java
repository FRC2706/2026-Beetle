package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ServoArmSubsystem;

public class TestCommand2 extends Command{
    
    private final ServoArmSubsystem m_ServoArmSubsystem;

    public TestCommand2(ServoArmSubsystem servoArmSubsystem){
        m_ServoArmSubsystem = servoArmSubsystem;
        addRequirements(m_ServoArmSubsystem);
    }

    @Override
    public void initialize() {
        m_ServoArmSubsystem.rotateArmTo(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
