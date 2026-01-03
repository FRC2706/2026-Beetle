package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElbowSubsystem;

public class ElbowControl extends Command{
    private final ElbowSubsystem m_ElbowSubsystem;
    private final double m_degreesToRotate;

    public ElbowControl(ElbowSubsystem elbowSubsystem,double degreesToRotate){
        m_ElbowSubsystem = elbowSubsystem;
        m_degreesToRotate = degreesToRotate;
        addRequirements(m_ElbowSubsystem);
    }

    @Override
    public void initialize(){
        m_ElbowSubsystem.rotateBy(m_degreesToRotate);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
