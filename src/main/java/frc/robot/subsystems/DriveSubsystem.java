// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants.DriveConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.sensors.PigeonIMU;

public class DriveSubsystem extends SubsystemBase {

  private final WPI_TalonSRX m_leftLeader = new WPI_TalonSRX(2);

  private final WPI_TalonSRX m_rightLeader = new WPI_TalonSRX(1);
  
  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftLeader, m_rightLeader);

  private final PigeonIMU m_gyro = new PigeonIMU(27);
  
  public DriveSubsystem() {
    SendableRegistry.addChild(m_drive, m_leftLeader);
    SendableRegistry.addChild(m_drive, m_rightLeader);   
    m_leftLeader.configFactoryDefault();
    m_rightLeader.configFactoryDefault();

    m_leftLeader.setInverted(false);  
    m_rightLeader.setInverted(false); 
    
    // Set neutral mode to brake
    setNeutralMode(NeutralMode.Brake);
    resetEncoders();
    m_gyro.configFactoryDefault();

  }

  /**
   * 
   *
   * @param fwd 
   * @param rot 
   */
  public void arcadeDrive(double fwd, double rot) {
    m_drive.arcadeDrive(fwd, rot);
  }

  // Sets current position of encoders to be 0  
  public void resetEncoders() {
    m_leftLeader.setSelectedSensorPosition(0);
    m_rightLeader.setSelectedSensorPosition(0);
  }

  public void printGyro(){
    System.out.println("Pitch:" + getPitch());
    System.out.println("Yaw:" + getYaw());
    System.out.println("Roll:" + getRoll());
  }

  public void setVoltage(double leftVolts, double rightVolts) {
    m_leftLeader.setVoltage(leftVolts);
    m_rightLeader.setVoltage(rightVolts);
  }
  
  public double averageDistanceDriven() {
    return ((m_leftLeader.getSelectedSensorPosition() + m_rightLeader.getSelectedSensorPosition()) / 2.0) * DriveConstants.kEncoderDistancePerPulse;
  }

  public void setMaxOutput(double maxOutput) {
    m_drive.setMaxOutput(maxOutput);
  }

  public void stopMotors() {
    m_leftLeader.stopMotor();
    m_rightLeader.stopMotor();
  }

  public void setNeutralMode(NeutralMode mode) {
    m_leftLeader.setNeutralMode(mode);
    m_rightLeader.setNeutralMode(mode);
    
  }

  // Sets all different current rotations to 0
  public void resetGyro(){
    m_gyro.setYaw(0);
    m_gyro.setCompassAngle(0);
    m_gyro.setAccumZAngle(0);
  }

  public double getPitch(){
    return m_gyro.getPitch();
  }

  public double getYaw(){
    return m_gyro.getYaw();
  }

  public double getRoll(){
    return m_gyro.getRoll();
  }

}