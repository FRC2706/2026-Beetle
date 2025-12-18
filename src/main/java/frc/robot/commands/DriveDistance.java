package frc.robot.commands;


import frc.robot.subsystems.DriveSubsystem;

public class DriveDistance {
    private final DriveSubsystem m_driveSubsystem;
    private final double distance;
    private final double speed;

    public DriveDistance(DriveSubsystem driveSubsystem, double distance, double speed) {
        m_driveSubsystem = driveSubsystem;
        this.distance = distance;
        this.speed = speed;
    }

    public void initialize() {
        m_driveSubsystem.resetEncoders();
    }

    public void execute() {
        m_driveSubsystem.setVoltage(speed, speed);
        
    }   

    public void end(boolean interrupted) {
        m_driveSubsystem.arcadeDrive(0, 0);
    }

    public boolean isFinished() {
        return Math.abs(m_driveSubsystem.averageDistanceDriven()) >= distance;
    }
        
}

    
