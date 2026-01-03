package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.robot.subsystems.BlingSubsystem;

import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.RgbFadeAnimation;
import com.ctre.phoenix.led.StrobeAnimation;

public class BlingCommands {
    
    public BlingSubsystem bling = BlingSubsystem.getINSTANCE();
  addRequirements(bling);

  public void setPurple() {
    bling.setPurple();
  }

  public void setRainbow() {
    RainbowAnimation rainbowAnim = new RainbowAnimation(1, 0.01, 64);

    bling.setAnimation(rainbowAnim);

  }

  public void setFire()
  {
    FireAnimation fireAnimation = new FireAnimation(1, 0.000001, 64, 0.8, 0.4);

    bling.setAnimation(fireAnimation);
  }

  public void setWhiteStrobe()
  {
    StrobeAnimation strobeAnimation = new StrobeAnimation(255, 255, 255, 255, 0.8, 64);

    bling.setAnimation(strobeAnimation);
  }

  public void setPurpleStrobe()
  {
    StrobeAnimation strobeAnimation = new StrobeAnimation(138, 43, 226, 127, 0.001, 64); //TODO: test all of the rgbw bling values

    bling.setAnimation(strobeAnimation);
  }

  public void setRedStrobe()
  {
    StrobeAnimation strobeAnimation = new StrobeAnimation(255, 0, 0, 127, 0.001, 64);

    bling.setAnimation(strobeAnimation);
  }

  public void setBlueStrobe()
  {
    StrobeAnimation strobeAnimation = new StrobeAnimation(0, 0, 255, 127, 0.001, 64);

    bling.setAnimation(strobeAnimation);
  }

  public void setYellowStrobe()
  {
    StrobeAnimation strobeAnimation = new StrobeAnimation(255, 255, 0, 127, 0.001, 64);

    bling.setAnimation(strobeAnimation);
  }

  public void setRgbFade()
  {
    RgbFadeAnimation rgbFadeAnimation = new RgbFadeAnimation(0.7, 0.1, 64);

    bling.setAnimation(rgbFadeAnimation);
  }
 
  
  @Override
  public void teleopInit() {
    System.out.println("LED set to purple");
  }

  @Override
  public void peridodic()
  {
    setPurple()
    }

}
