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

public class DriveSubsystem extends SubsystemBase {

  private final WPI_TalonSRX m_leftLeader = new WPI_TalonSRX(2);

  private final WPI_TalonSRX m_rightLeader = new WPI_TalonSRX(1);
  
  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftLeader, m_rightLeader);

/** 

  private final Encoder m_leftEncoder = 
      

  
  private final Encoder m_rightEncoder = 
*/
  
  public DriveSubsystem() {
    SendableRegistry.addChild(m_drive, m_leftLeader);
    SendableRegistry.addChild(m_drive, m_rightLeader);

   
    m_leftLeader.configFactoryDefault();
    m_rightLeader.configFactoryDefault();

    m_leftLeader.setInverted(false);  
    m_rightLeader.setInverted(false); 

  
    setNeutralMode(NeutralMode.Brake);

/**
    m_leftEncoder.setDistancePerPulse(DriveConstants.kEncoderDistancePerPulse);
    m_rightEncoder.setDistancePerPulse(DriveConstants.kEncoderDistancePerPulse);
 */
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

  
  public void resetEncoders() {
    /**
    m_leftEncoder.reset();
    m_rightEncoder.reset();
    */
  }
  


   
  public double getAverageEncoderDistance() {
    /** 
    return (m_leftEncoder.getDistance() + m_rightEncoder.getDistance()) / 2.0;
    */
    return 0.0;
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
}