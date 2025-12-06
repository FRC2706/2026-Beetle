package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.math.MathUtil;

public class ShooterSubsystem extends SubsystemBase {

    private final XboxController controller;

    public ShooterSubsystem(XboxController controller) {
        this.controller = controller;
    }

    @Override
    public void periodic() {
        double left = controller.getLeftTriggerAxis();
        double right = controller.getRightTriggerAxis();

        System.out.println("LT " + left + "  RT " + right);
    }
}
