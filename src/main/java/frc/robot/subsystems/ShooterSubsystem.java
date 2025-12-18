package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.math.MathUtil;

public class ShooterSubsystem extends SubsystemBase {

    private XboxController controller;

    public ShooterSubsystem (XboxController controller){
        this.controller = controller;
        System.out.println("xboxcontroller var set up");

    }

    public void periodic(){
        // reading the trigger values from left and right trigger
         double left = controller.getLeftTriggerAxis();
         double right = controller.getRightTriggerAxis();

    //System.out.println("hello");

    //printing trigger values to console
         System.out.println("LT" + left + "RT" + right);
    }



}