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

//class
public class PhotonSubsystem extends SubsystemBase {

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
 
  public static PhotonSubsystem getInstance(){
    if (instance == null){
      SubsystemChecker.subsystemConstructed(SubsystemType.PhotonSubsystem);
      instance = new PhotonSubsystem();
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
  
  //publishes yaw
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

  @Override
  public void periodic() {

    Pose2d fieldToTarget = null;
    
    // Must be set by 2D or 3D mode;
    //@todo: test 2D and 3D mode both
   // PhotonPipelineResult result = camera1.getLatestResult();
    //@todo: to test...
    List<PhotonPipelineResult> listResult;

    try {
      listResult = camera1.getAllUnreadResults();
      isCameraConnected = true;
    } catch (Exception e) {

      // Print the error message only once when the camera is disconnects
      if (isCameraConnected) {
        DriverStation.reportError("PhotonSubsystem no camera found: \n" + e, false);
        isCameraConnected = false;
      }
      return; // No camera connected
    }

    if (listResult.isEmpty() == true)
    {
      return;
    }
    PhotonPipelineResult result = listResult.get(listResult.size() - 1);
    if (result == null)
      return;

    if (result.getTimestampSeconds() == recentTimeStamp){
      //--System.out.println("time stamp is stale");
      return;
    }
    recentTimeStamp = result.getTimestampSeconds();

    if(result.hasTargets()==false)
    { 
     // System.out.println("result does not have any target");
      return;
    }

    //if (PhotonConfig.USE_3D_TAGS) 
    //====================
    {     
      //Get the best target
      PhotonTrackedTarget target = result.getBestTarget();

      if (target == null)
      {
        //System.out.println("Best target is null");
        return;
      }

      bestTagId = target.getFiducialId();
      targetOffset = Config.PhotonConfig.targetOffsetMap.get(bestTagId);
      pubBestTagId.accept(bestTagId);
      pubTargetOffset.accept(new double[]{targetOffset.getX(), targetOffset.getY()});
      
      //@todo: validate bestTagId based on red or blue. May not need to check red or blue
      //if not valid tagId, return
      Optional<Pose3d> tagPose = aprilTagFieldLayout.getTagPose(bestTagId);
      Pose3d tagIdPose;
      if (tagPose.isEmpty())
      {
        //reset to default values
        pubBestTagHeading.accept(-1);
        pubTargetRobotHeading.accept(-1);
        return;
      }
      else
      {
        tagIdPose = tagPose.get();
        bestTagHeading = tagIdPose.getRotation().toRotation2d();
  
        //due to the camera is at the back
        targetRobotHeading = bestTagHeading;

       //for red tags: no need to add 180
       //--targetRobotHeading = bestTagHeading.plus(Rotation2d.fromDegrees(180));
       //targetRobotHeading = bestTagHeading;
        pubBestTagHeading.accept(bestTagHeading.getDegrees());
        pubTargetRobotHeading.accept(targetRobotHeading.getDegrees());
       
      }

      ////New option
      /// ===========================================================
     {
      //get the swerve pose at the time that the result was gotten
      Optional<Pose2d> optPose= SwerveSubsystem.getInstance().getPoseAtTimestamp(result.getTimestampSeconds());
      //for security reasons
      if (optPose.isEmpty()){
        //System.out.println("the odometryPose is empty");
        return;
      }
      else
      {
        Pose2d odometryPose = optPose.get();

        //@todo: need to update cameraTransform for the new camera location
        Transform3d robotToTarget3d = PhotonConfig.rightReefCameraTransform.plus(target.getBestCameraToTarget());
        Transform2d robotToTarget = new Transform2d(robotToTarget3d.getTranslation().toTranslation2d(), robotToTarget3d.getRotation().toRotation2d());

        // Map the position of the tag relative to the current odometry pose with latency compensation
        fieldToTarget = odometryPose.plus(robotToTarget);

        pub3DTagsDebugMsg.accept("Transform2d from robot to tag: " + robotToTarget.toString());
      } 
    }

     //previous poseEstimation option
    //********************************/
    // {
    //   Optional<EstimatedRobotPose> optEstPose = photonPoseEstimator.update(result);
    //   if (optEstPose.isEmpty()) {
    //     pub3DTagsDebugMsg.accept("EmptyEstimatedRobotPose"); 
    //     return;
    //   }      
    //   // Grab the pose from when the image was taken to compensate for how much the robot has moved since the image was taken
    //   Optional<Pose2d> odometryPose = SwerveSubsystem.getInstance().getPoseAtTimestamp(optEstPose.get().timestampSeconds);
    //   if (odometryPose.isEmpty()) {
    //     pub3DTagsDebugMsg.accept("No odometry pose at timestamp: " + optEstPose.get().timestampSeconds);
    //     return;
    //   }
    //   else
    //   {
    //       // Create a transform that maps the change in Pose between the robot estimated position and the true tag position
    //     Transform2d robotToTarget = new Transform2d(optEstPose.get().estimatedPose.toPose2d(), tagPose.get().toPose2d());

    //     // Map the position of the tag relative to the current odometry pose with latency compensation
    //     fieldToTarget = odometryPose.get().plus(robotToTarget);
    //     pub3DTagsDebugMsg.accept("Transform2d from robot to tag:" + robotToTarget.toString());

    //   }
    // }


    } 
 
    if (fieldToTarget != null) {
      //update rolling averages
      targetPos = new Translation2d(
          filterX.calculate(fieldToTarget.getX()),
          filterY.calculate(fieldToTarget.getY()));
      targetRotation = Rotation2d.fromDegrees(filteryaw.calculate(fieldToTarget.getRotation().getDegrees()));
      numSamples++;

      if (numSamples == PhotonConfig.maxNumSamples)
      {
        //save the set piont
        newTargetPos = targetPos;

      }

      //publish to networktables
      pubSetPoint.accept(new double[]{targetPos.getX(), targetPos.getY(), targetRotation.getRadians()});
      pubNewSetPoint.accept(new double[]{newTargetPos.getX(), newTargetPos.getY(), -1.0});
    
  }
  
  pubHasData.accept(hasData());
}
}