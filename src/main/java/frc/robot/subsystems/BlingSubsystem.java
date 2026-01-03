
package frc.robot.subsystems;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdleConfiguration;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BlingSubsystem extends SubsystemBase {

  private CANdle candle;
  private static BlingSubsystem INSTANCE = null;
  private static double Brightness = 0.5;
   
  private BlingSubsystem() {
    candle = new CANdle(15);

    CANdleConfiguration config = new CANdleConfiguration();
    config.stripType = LEDStripType.RGB; 
    config.brightnessScalar = Brightness; 

    candle.configAllSettings(config);
  }

  public static BlingSubsystem getINSTANCE() {

    if (INSTANCE == null) {
      INSTANCE = new BlingSubsystem();
    }

    return INSTANCE;
  }

  public void setBrightness() {
    candle.configBrightnessScalar(Brightness);
  }

  public void setColour(int red, int blue, int green) {
    candle.clearAnimation(0);
    candle.setLEDs(red, blue, green);
  }

  public void setPurple() {
    setColour(138, 43, 226);
  }

  public void setGreen() {
    setColour(0, 255, 0);
  }

  public void setRed() {
    setColour(255, 0, 0);
  }

  

  public void periodic() {
    // This method will be called once per scheduler run

  }

  public void setAnimation(Animation animation) {
    candle.animate(animation);
  }
}