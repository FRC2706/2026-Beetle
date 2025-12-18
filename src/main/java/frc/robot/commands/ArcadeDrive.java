package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import java.util.function.DoubleSupplier;

public class ArcadeDrive extends Command {
    private final DriveSubsystem m_driveSubsystem;
    private final DoubleSupplier m_forward;
    private final DoubleSupplier m_rotation;

    public ArcadeDrive(DriveSubsystem driveSubsystem, DoubleSupplier forward, DoubleSupplier rotation) {
        m_driveSubsystem = driveSubsystem;
        m_forward = forward;
        m_rotation = rotation;
        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        m_driveSubsystem.arcadeDrive(m_forward.getAsDouble(), m_rotation.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        m_driveSubsystem.arcadeDrive(0, 0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}