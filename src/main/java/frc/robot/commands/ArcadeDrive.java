

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;



import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class ArcadeDrive {
    private final WPI_TalonSRX leftMaster = new WPI_TalonSRX(0);
    private final WPI_VictorSPX leftFollower = new WPI_VictorSPX(1);
    private final WPI_TalonSRX rightMaster = new WPI_TalonSRX(2);
    private final WPI_VictorSPX rightFollower = new WPI_VictorSPX(3);
    private final DifferentialDrive drive = new DifferentialDrive(leftMaster, rightMaster); 

    public ArcadeDrive() {
        leftFollower.follow(leftMaster);
        rightFollower.follow(rightMaster);

        leftMaster.setInverted(false);
        leftFollower.setInverted(InvertType.FollowMaster);
        rightMaster.setInverted(true);
        rightFollower.setInverted(InvertType.FollowMaster);

        leftMaster.setNeutralMode(NeutralMode.Brake);
        leftFollower.setNeutralMode(NeutralMode.Brake);
        rightMaster.setNeutralMode(NeutralMode.Brake);
        rightFollower.setNeutralMode(NeutralMode.Brake);
    }

    public void arcadeDrive(double forward, double rotation) {
        drive.arcadeDrive(forward, rotation);
    }
}