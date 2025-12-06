// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.wpilibj.XboxController.Button;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.DriveDistance;
import frc.robot.commands.DriveTimed;
import frc.robot.commands.GrabHatch;
import frc.robot.commands.IDKPRINT;
import frc.robot.commands.HalveDriveSpeed;
import frc.robot.commands.ReleaseHatch;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HatchSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.AnalogInput;

public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final HatchSubsystem m_hatchSubsystem = new HatchSubsystem();
  private final XboxController driverController = new XboxController(0);
  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem(driverController);
  

  // The autonomous routines

  // The driver's controller
  XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);

  // configering the knob
  private final AnalogInput autoSelectorKnob = new AnalogInput(0); // port for knob
  private static final int NUM_AUTOS = 12; // 12 knob positions


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    m_robotDrive.setDefaultCommand(
        new ArcadeDrive(
            m_robotDrive,
            () -> -m_driverController.getLeftY(),
            () -> -m_driverController.getRightX()));

    // Put subsystems to dashboard.
    Shuffleboard.getTab("Drivetrain").add(m_robotDrive);
    Shuffleboard.getTab("HatchSubsystem").add(m_hatchSubsystem);
    Shuffleboard.getTab("ShooterSubsystem").add(m_shooterSubsystem);

    // Log Shuffleboard events for command initialize, execute, finish, interrupt
    CommandScheduler.getInstance()
        .onCommandInitialize(
            command ->
                Shuffleboard.addEventMarker(
                    "Command initialized", command.getName(), EventImportance.kNormal));
    CommandScheduler.getInstance()
        .onCommandExecute(
            command ->
                Shuffleboard.addEventMarker(
                    "Command executed", command.getName(), EventImportance.kNormal));
    CommandScheduler.getInstance()
        .onCommandFinish(
            command ->
                Shuffleboard.addEventMarker(
                    "Command finished", command.getName(), EventImportance.kNormal));
    CommandScheduler.getInstance()
        .onCommandInterrupt(
            command ->
                Shuffleboard.addEventMarker(
                    "Command interrupted", command.getName(), EventImportance.kNormal));
  }

  private void configureButtonBindings() {
    // Grab the hatch when the 'A' button is pressed.
    new JoystickButton(m_driverController, Button.kA.value).onTrue(new GrabHatch(m_hatchSubsystem));
    // Release the hatch when the 'B' button is pressed.
    new JoystickButton(m_driverController, Button.kB.value)
        .onTrue(new ReleaseHatch(m_hatchSubsystem));
    // While holding the shoulder button, drive at half speed
    new JoystickButton(m_driverController, Button.kRightBumper.value)
        .whileTrue(new HalveDriveSpeed(m_robotDrive));
  }

  /** Read the knob voltage and convert it into an autonomous mode number (0-11). */
  private double getAutoMode() {
    double voltage = autoSelectorKnob.getVoltage(); // 0-5V
    double normalized = voltage / 5.0;
    return voltage;
  }
  
  /** This function returns the autonomous command based on the knob position. */
  public Command getAutonomousCommand() {
    double mode = getAutoMode();
    return new IDKPRINT(getAutoMode());
   // switch (mode) {
      //case 0:
       // return new PrintCommand("knob #0");
      //case 1:
       //return new PrintCommand("knob #1");
// DriveDistance(39, 0.3, m_robotDrive);
     // case 2:
       // return new PrintCommand("knob #2");
//DriveTimed(2.0, 0.3, m_robotDrive);
      
      //default:
        //return null;
   
        //}
        
  }
    
}

/** volts
 * 1 = 2.59
 * 2 = 2.96
 * 3 = 3.22
 * 4 = 3.58
 * 5 = 3.81
 * 6 = 3.96
 * 7 = 4.09
 * 8 = 4.18
 * 9 = 4.25
 * 10 = 4.35
 * 11 = 4.42
 * 12 = 4.49 **/