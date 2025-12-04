package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ServoSubsystem;

public class FlagUp extends Command{
    private final ServoSubsystem m_ServoSubsystem;

    public FlagUp(ServoSubsystem servoSubsystem){
        m_ServoSubsystem = servoSubsystem;
        addRequirements(m_ServoSubsystem);
    }

    @Override
    public void initialize(){
        m_ServoSubsystem.turnTo(90);
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
