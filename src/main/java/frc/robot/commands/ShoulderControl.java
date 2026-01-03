package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShoulderSubsystem;

public class ShoulderControl extends Command{
    
    private final ShoulderSubsystem m_ShoulderSubsystem;
    private final double m_degreesToRotate;

    public ShoulderControl(ShoulderSubsystem shoulderSubsystem, double degreesToRotate){
        m_ShoulderSubsystem = shoulderSubsystem;
        m_degreesToRotate = degreesToRotate;
        addRequirements(m_ShoulderSubsystem);
    }

    @Override
    public void initialize(){
        m_ShoulderSubsystem.rotateBy(m_degreesToRotate);
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
