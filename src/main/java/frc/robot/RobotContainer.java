// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.wpilibj.XboxController.Button;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.ArcadeDrive;
//import frc.robot.commands.GrabHatch;
import frc.robot.commands.HalveDriveSpeed;
//import frc.robot.commands.ReleaseHatch;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HatchSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import java.lang.ModuleLayer.Controller;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj.Joystick;


public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  //private final HatchSubsystem m_hatchSubsystem = new HatchSubsystem();
  private final XboxController driverController = new XboxController(OIConstants.kOperatorControllerPort);
  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem(driverController);

  // The autonomous routines

  // A simple auto routine that drives forward a specified distance, and then stops.


  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

    // The driver's controller
  XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();


    m_robotDrive.setDefaultCommand(
        new ArcadeDrive(
            m_robotDrive,
            () -> -m_driverController.getLeftY(),
            () -> -m_driverController.getRightX()));
   

    // Add commands to the autonomous command chooser


    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);
    // Put subsystems to dashboard.
    Shuffleboard.getTab("Drivetrain").add(m_robotDrive);
    //Shuffleboard.getTab("HatchSubsystem").add(m_hatchSubsystem);
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
    //new JoystickButton(m_driverController, Button.kA.value).onTrue(new GrabHatch(m_hatchSubsystem));
    // Release the hatch when the 'B' button is pressed.
    //new JoystickButton(m_driverController, Button.kB.value)
    //    .onTrue(new ReleaseHatch(m_hatchSubsystem));
    // While holding the shoulder button, drive at half speed
    //new JoystickButton(m_driverController, Button.kRightBumper.value)
     //   .whileTrue(new HalveDriveSpeed(m_robotDrive));
  }

  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}