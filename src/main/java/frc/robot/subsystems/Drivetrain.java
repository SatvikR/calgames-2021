// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
  public static final class Config {
    public static final int kLeftMotorMaster = 8;
    public static final int kRightMotorMaster = 5;
    public static final int kLeftMotorSlave = 1;
    public static final int kRightMotorSlave = 2;

    public static final int kEncoderChanA = 0;
    public static final int kEncoderChanB = 1;

    public static final double kFeetPerRotation = 0.5 * Math.PI;
    public static final double kPulsesPerRotation = 128.0;

    public static final double kDegsPerRotation = 60.0;

    public static final double kP = 0.5;
    public static final double kI = 0.5;
    public static final double kD = 0.1;

    public static final double kPTurn = 0.5;
    public static final double kITurn = 0.0;
    public static final double kDTurn = 0.0;
  }

  private CANSparkMax m_leftMotorMaster = new CANSparkMax(Config.kLeftMotorMaster, MotorType.kBrushless);
  private CANSparkMax m_rightMotorMaster = new CANSparkMax(Config.kRightMotorMaster, MotorType.kBrushless);
  private CANSparkMax m_leftMotorSlave = new CANSparkMax(Config.kLeftMotorSlave, MotorType.kBrushless);
  private CANSparkMax m_rightMotorSlave = new CANSparkMax(Config.kRightMotorSlave, MotorType.kBrushless);

  private DifferentialDrive m_drive = new DifferentialDrive(m_leftMotorMaster, m_rightMotorMaster);

  private Encoder m_encoder = new Encoder(Config.kEncoderChanA, Config.kEncoderChanB);

  public enum EncoderCalculationType {
    Lateral, Turn
  }

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    // Set masters to inverted
    m_leftMotorMaster.setInverted(true);
    m_rightMotorMaster.setInverted(true);
    // Enable following
    m_leftMotorSlave.follow(m_leftMotorMaster);
    m_rightMotorSlave.follow(m_rightMotorMaster);

    // Set encoder distance constant
    setEncoderState(EncoderCalculationType.Lateral);
  }

  /**
   * Use this to access the DifferentialDrive instance of the drivetrain.
   * 
   * @return the differential drive instance
   */
  public DifferentialDrive getDrive() {
    return m_drive;
  }

  /**
   * Calculates the distance driven using the encoder.
   * 
   * @return Distance in feet/angles depending on the calculation mode set with
   *         setEncoderState (default is lateral)
   */
  public double getDistance() {
    return m_encoder.getDistance();
  }

  /**
   * Sets the encoder calculation mode.
   * 
   * @param e Can be either EncoderCalculationType.Lateral or
   *          EncoderCalculationType.Turn
   */
  public void setEncoderState(EncoderCalculationType e) {
    switch (e) {
      case Lateral:
        m_encoder.setDistancePerPulse(Config.kFeetPerRotation / Config.kPulsesPerRotation);
        break;
      case Turn:
        m_encoder.setDistancePerPulse(Config.kDegsPerRotation / Config.kPulsesPerRotation);
        break;
    }
  }

  /**
   * Resets the encoder value
   */
  public void resetEncoder() {
    m_encoder.reset();
  }
}
