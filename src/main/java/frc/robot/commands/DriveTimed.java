package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class DriveTimed extends Command {
    private final DriveSubsystem m_drive;
    private final double m_time;
    private final double m_speed;
    private final Timer m_timer = new Timer();

    public DriveTimed(double time, double speed, DriveSubsystem drive) {
        m_time = time;
        m_speed = speed;
        m_drive = drive;
        addRequirements(m_drive);
    }

    @Override
    public void initialize() {
        m_timer.reset();
        m_timer.start();
        m_drive.arcadeDrive(m_speed, 0);
    }

    @Override
    public void execute() {
        m_drive.arcadeDrive(m_speed, 0);
    }

    @Override
    public void end(boolean interrupted) {
        m_drive.arcadeDrive(0, 0);
        m_timer.stop();
    }

    @Override
    public boolean isFinished() {
        return m_timer.get() >= m_time;
    }
}
