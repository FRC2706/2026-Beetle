package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClawSubsystem;

public class ClawRelease extends Command{
    private final ClawSubsystem m_ClawSubsystem;

    public ClawRelease(ClawSubsystem clawSubsystem){
        m_ClawSubsystem = clawSubsystem;
        addRequirements(m_ClawSubsystem);
    }

    @Override
    public void initialize(){
        m_ClawSubsystem.openClaw();
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
