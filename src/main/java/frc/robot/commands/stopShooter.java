package frc.robot.commands;

import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.Command;


public class stopShooter extends Command {
    
    private final ShooterSubsystem m_ShooterSubsystem;

    public stopShooter(ShooterSubsystem subsystem) {
        m_ShooterSubsystem = subsystem;
        addRequirements(m_ShooterSubsystem);
    }

    @Override
    public void initialize() {
        System.out.println("stop shooter");
        m_ShooterSubsystem.stop();
        
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
