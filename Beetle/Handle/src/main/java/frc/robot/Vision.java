// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;
//imports
import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.targeting.TargetCorner;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.IntegerArrayPublisher;
import edu.wpi.first.networktables.IntegerEntry;
import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.lib.lib2706.SubsystemChecker;
import frc.lib.lib2706.SubsystemChecker.SubsystemType;
import frc.robot.Config;
import frc.robot.Config.PhotonConfig;
import frc.robot.Config.PhotonConfig.PhotonPositions;
import frc.robot.commands.PhotonMoveToTarget;
import frc.robot.commands.RumbleJoystick;

public class Vision extends SubsystemBase{
  //constants
  
  //declarations
  private static PhotonSubsystem instance;
  private DoubleArrayPublisher pubSetPoint;
  private DoubleArrayPublisher pubNewSetPoint;
  public DoublePublisher pubBestTagHeading, pubTargetRobotHeading;
  private IntegerPublisher pubBestTagId;
  private DoubleArrayPublisher pubTargetOffset;
  private BooleanPublisher pubHasData;
  private BooleanSubscriber hasTarget;
  private StringPublisher pub3DTagsDebugMsg;
  private PhotonCamera camera1;
  private Translation2d targetPos;
  private Rotation2d targetRotation;
  private Translation2d targetOffset;
  private Translation2d newTargetPos = new Translation2d();
  private LinearFilter filteryaw = LinearFilter.movingAverage(PhotonConfig.maxNumSamples);
  private LinearFilter filterX = LinearFilter.movingAverage(PhotonConfig.maxNumSamples);
  private LinearFilter filterY = LinearFilter.movingAverage(PhotonConfig.maxNumSamples);
  private int numSamples;
  private double recentTimeStamp = 0;
  private int bestTagId;
  private Rotation2d bestTagHeading;
  private Rotation2d targetRobotHeading;
  private boolean isCameraConnected = false;

  private AprilTagFieldLayout aprilTagFieldLayout;
  //+++private PhotonPoseEstimator photonPoseEstimator;

  public static Vision getInstance(){
      if (instance == null){
        SubsystemChecker.subsystemConstructed(SubsystemType.PhotonSubsystem);
        instance = new Vision();
      }
      return instance;
  }

  /** Creates a new photonAprilTag. */
  private PhotonSubsystem() {
    //name of camera, change if using multiple cameras
    camera1 = new PhotonCamera(PhotonConfig.rightReefCameraName);

    //networktable publishers
    NetworkTable photonTable = NetworkTableInstance.getDefault().getTable(PhotonConfig.networkTableName);
    pubBestTagId = photonTable.getIntegerTopic("BestTagId").publish(PubSubOption.periodic(0.02));
    pubTargetOffset = photonTable.getDoubleArrayTopic("TargetOffset").publish(PubSubOption.periodic(0.02));
    pubBestTagHeading = photonTable.getDoubleTopic("BestTagHeading (deg)").publish(PubSubOption.periodic(0.02));
    pubTargetRobotHeading = photonTable.getDoubleTopic("TargetRobotHeading (deg)").publish(PubSubOption.periodic(0.02));
    pubHasData =  photonTable.getBooleanTopic("hasData").publish(PubSubOption.periodic(0.02));
    hasTarget = NetworkTableInstance.getDefault().getBooleanTopic("/photonvision/"+PhotonConfig.rightReefCameraName + "/hasTarget").subscribe(false, PubSubOption.periodic(0.02));
    pubSetPoint = photonTable.getDoubleArrayTopic("SetPoint(fieldToTarget)").publish(PubSubOption.periodic(0.02));
    pubNewSetPoint = photonTable.getDoubleArrayTopic("SetPoint(new)").publish(PubSubOption.periodic(0.02));
    pub3DTagsDebugMsg = photonTable.getStringTopic("3DTagsDebugMsg").publish(PubSubOption.periodic(0.02));
    SmartDashboard.putData("command reset id",Commands.runOnce(()->reset()));

    reset();

    try {
      aprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025Reefscape);

      //@todo: update cameraTransform
      //+++photonPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, PhotonConfig.leftReefCameraTransform);
    } catch (Exception e) {
      aprilTagFieldLayout = null;
      DriverStation.reportError("Merge's PhotonSubsystem failed to create the apriltag layout. ", false);
    }

  }

  public void reset() {
    filterX.reset();
    filterY.reset();
    filteryaw.reset();
    //set target info to the robot's info
    targetRotation = SwerveSubsystem.getInstance().getPose().getRotation();
    targetPos = SwerveSubsystem.getInstance().getPose().getTranslation();
    //initialize vars
    numSamples = 0;
  }

  public void resetTagAtBootup() {
    Optional<Alliance> alliance = DriverStation.getAlliance();
    // if (alliance.isEmpty() || alliance.get() == Alliance.Blue) {
    //   
    // }
    reset();
  }

  /**
   * Command for removing photon data and changing the target id to look for.
   * @param tagid
   * the id that the subsystem will now look for
   * @return
   * the command
   */
  public Command getResetCommand(){
    return runOnce(() -> reset());
  }

  //publishes yaw(left and right)
  public Rotation2d getYaw (){
    return targetRotation;
  }

  /**
   * Command that does nothing until photonSubsystem has enough data to track a target
   * @param tagid
   * resets the subsystem and its id
   * @return
   * the command
   */

  //use photonvision subsystem filter
  public Command getWaitForDataCommand(){
    return new FunctionalCommand(() -> reset(), ()->{}, (interrupted) ->{}, ()->hasData(), this);
  }

  // Directly use Photonvision: hasTarget. 
  // public Command getWaitForDataCommand(){
  //   return new FunctionalCommand(() -> {}, ()->{}, (interrupted) ->{}, ()->hasTarget.get(false), this);
  // }

  public Translation2d getTargetPos(){
    return targetPos;
  }

  public Rotation2d getTargetRotation(){
    return targetRotation;
  }

  public Translation2d getNewTargetPos() {
    return newTargetPos;
  }

  public Rotation2d getTargetRobotHeading(){
    return targetRobotHeading;
  }

  public Translation2d getTargetOffset() {
    return targetOffset;
  }

  public boolean hasData() {
    //if data is the max that the filters hold
    return(numSamples >= PhotonConfig.maxNumSamples);
  }

  public boolean hasTarget()
  {
    return hasTarget.get();
  }
  
  


}

