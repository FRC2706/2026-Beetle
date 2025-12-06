package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class IDKPRINT extends Command {
    private final double m_num;

    public IDKPRINT(double num){
        m_num = num;
    }

    @Override
    public void initialize(){
        System.out.println(m_num);
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
