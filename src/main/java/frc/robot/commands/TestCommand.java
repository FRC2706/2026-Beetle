package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ServoArmSubsystem;

public class TestCommand extends Command{
    private final ServoArmSubsystem m_ServoArmSubsystem;

    public TestCommand(ServoArmSubsystem servoArmSubsystem){
        m_ServoArmSubsystem = servoArmSubsystem;
        addRequirements(m_ServoArmSubsystem);
    }

    @Override
    public void initialize(){
        m_ServoArmSubsystem.rotateArmTo(180);
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}