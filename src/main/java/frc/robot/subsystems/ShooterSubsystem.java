package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.ShooterConstants;

//imports for spark max code
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;


public class ShooterSubsystem extends SubsystemBase {

    private XboxController controller;

    private SparkMax shooterMotor;

    public ShooterSubsystem (XboxController controller){
        this.controller = controller;
        System.out.println("xboxcontroller var set up");

        System.out.println("[Init]Creating Shooter");
        shooterMotor = new SparkMax(ShooterConstants.shooterSparkMaxCANID, MotorType.kBrushless);
        SparkMaxConfig shooterMotorConfig = (SparkMaxConfig) new SparkMaxConfig()
                        .inverted(true)
                        .smartCurrentLimit(70)
                        .idleMode(IdleMode.kBrake)
                        .voltageCompensation(10);

        shooterMotor.configure(shooterMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    public void setVoltage(){
        shooterMotor.set(controller.getLeftTriggerAxis()); //reads only left trigger presses
        System.out.println("setVoltage cmd called");
        //note: if you set voltage to -voltage..i think it rotates in other direction?
    }

    public void stop(){
        shooterMotor.stopMotor(); 
        System.out.println("stop cmd called");
    }

    public void periodic(){
        // reading the trigger values from left and right trigger
         double left = controller.getLeftTriggerAxis();
         double right = controller.getRightTriggerAxis();

    //System.out.println("hello");

    //printing trigger values to console
         //System.out.println("LT" + left + "RT" + right);
    }



}