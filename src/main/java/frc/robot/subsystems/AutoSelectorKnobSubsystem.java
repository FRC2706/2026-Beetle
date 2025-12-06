package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AutoSelectorKnobSubsystem extends SubsystemBase{

    private final AnalogInput m_knob = new AnalogInput(0);

    public AutoSelectorKnobSubsystem(){

    }

    public double getVoltage(){
        return m_knob.getVoltage();
    }
}