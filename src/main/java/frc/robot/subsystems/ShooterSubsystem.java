package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.ShooterConstants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//imports for spark max code
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;


public class ShooterSubsystem extends SubsystemBase {

    private CommandXboxController controller;

    private final WPI_TalonSRX shooterMotor = new WPI_TalonSRX(4);

    public ShooterSubsystem (CommandXboxController controller){
        this.controller = controller;
        System.out.println("xboxcontroller var set up");

        System.out.println("[Init]Creating Shooter");
        
        shooterMotor.configFactoryDefault();
        shooterMotor.setInverted(false);

        //setNeutralMode(NeutralMode.Brake);
    }

    public void setVoltage(){
        System.out.println("setVoltage cmd called " + controller.getLeftTriggerAxis());
        shooterMotor.set(controller.getLeftTriggerAxis()); 
        //note: if you set voltage to -voltage..i think it rotates in other direction?
    }

    public void stop(){
        System.out.println("stop cmd called");
        shooterMotor.stopMotor(); 
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