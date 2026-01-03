package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClawSubsystem;

public class ClawGrab extends Command{
    private final ClawSubsystem m_ClawSubsystem;

    public ClawGrab(ClawSubsystem clawSubsystem){
        m_ClawSubsystem = clawSubsystem;
        addRequirements(m_ClawSubsystem);
    }

    @Override
    public void initialize(){
        m_ClawSubsystem.closeClaw();
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
