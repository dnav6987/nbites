package nbtool.gui;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.Element;

import java.io.IOException;
import java.util.*;

import nbtool.data.*;
import nbtool.gui.utilitypanes.*;
import nbtool.util.*;
import nbtool.io.ControlIO;
import nbtool.io.ControlIO.ControlInstance;
import nbtool.util.NBConstants.*;
import nbtool.util.Logger;

//import messages.WalkEnginePreferences;
import messages.EngineParameters.WalkEnginePreferences;


public class WalkingEngineParameters {
	
	
    //Constructor
    public WalkingEngineParameters(){

        buildV5ParamList();
    }


    /* List of arguments and their setup values as BH put them initially
     * Several might not be used, but adding all of them for now.
     *
     * If there is not a description of the value's purpose next to the parameter,
     * then the value is part of the last description.
     *
     * Default values are next to the param description, in parenthesis
     * if there are different values for v4 or v5 robots then it is also specified
     *
     * Descriptions taken from WalkingEngine.h , in the B-human walking engine code.
     * Don't blame me for their unhelpfulness
     */
	private String[] listOfV4Params = new String[] {

    "StandComPosition_x (50.f)"                         ,    /** The position of the center of mass relative to the right foot when standing */
    "StandComPos_y (262.0f)"                            ,
    "standBodyTilt (0.f)"                               ,    /** The tilt of the torso when standing */
    "standArmJointAngles_x (0.2f)"                      ,    /** The joint angles of the left arm when standing */
    "standArmJointAngles_y (0.f)"                       ,
    "standHardnessAnklePitch (85)"                      ,    /** The hardness of the ankle pitch joint for standing and walking */
    "standHardnessAnkleRoll (85)"                       ,    /** The hardness of the ankle roll joint for standing and walking */
    "walkRef_x (16.f)"                                  ,    /** The position of the pendulum pivot point in Q */
    "walkRef_y (50.f)"                                  ,
    "walkRefAtFullSpeed_x (9.5f)"                       ,    /** The position of the pendulum pivot point when walking forwards with maximum speed */
    "walkRefAtFullSpeed_y (40.f)"                       ,
    "walkRefXPlanningLimit_low (-2.f)"                  ,    /** The limit for shifting the pendulum pivot point towards the x-axis when
                                                                    planning the next step size */
    "walkRefXPlanningLimit_high (3.f)"                  ,
    "walkRefXLimit_low  (-30.f)"                        ,    /** The limit for shifting the pendulum pivot point towards the x-axis when balancing */
    "walkRefXLimit_high (30.f)"                         ,
    "walkRefYLimit_low  (-30.f)"                        ,    /** The limit for shifting the pendulum pivot point towards the y-axis when balancing */
    "walkRefYLimit_high (30.f)"                         ,

    "walkStepSizeXPlanningLimit_low (-50.f)"            ,    /** The minimum and maximum step size used to plan the next step size */
    "walkStepSizeXPlanningLimit_high (60.f)"            ,
    "walkStepSizeXLimit_low (-55.f)"                    ,    /** The minimum and maximum step size when balancing */
    "walkStepSizeXLimit_high (66.f)"                    ,

    "walkStepDuration (v5=535.f)(v4=525.f)"             , /** the duration of a full step cycle (two half steps) */
    "walkStepDurationAtFullSpeedX (v5=535.f)(v4=525.f)" , /** the duration of a full step cycle when walking forwards with maximum speed */

    "walkStepDurationAtFullSpeedY (180.f)"              , /** The duration of a full step cycle when walking sidewards with maximum speed */
    "walkHeight_x (262.f)"                              , /** The height of the 3d linear inverted pendulum plane
                                                                  (for the pendulum motion towards the x-axis and the pendulum motion towards the y-axis) */
    "walkHeight_y (262.f)"                              ,
    "walkArmRotationAtFullSpeedX (0.1f)"                , /** The maximum deflection for the arm swinging motion */
    "walkMovePhase_beginning (0.f)"                     , /** The beginning and length of the trajectory used to move the swinging foot to its new position */
    "walkMovePhase_length (1.f)"                        ,

    "walkLiftPhase_beginning (0.f)"                     , /** The beginning and length of the trajectory used to lift the swinging foot */
    "walkLiftPhase_length (1.f)"                        ,

    "walkLiftOffset_x"                                  , /** The height the swinging foot is lifted */
    "walkLiftOffset_y"                                  ,
    "walkLiftOffset_z(v5=22.f)(v4=17.f)"                ,
    
    "walkLiftOffsetAtFullSpeedX_x"                      , /** The height the swinging foot is lifted when walking in x direction*/
    "walkLiftOffsetAtFullSpeedX_y"                      ,
    "walkLiftOffsetAtFullSpeedX_z (v4=25.f)(v5=22.f)"   ,

    "walkLiftOffsetAtFullSpeedY_x (0.f)"                , /** The height the swinging foot is lifted when walking full speed in y-direction */
    "walkLiftOffsetAtFullSpeedY_y (20.f)"               ,
    "walkLiftOffsetAtFullSpeedY_z (25.f) "              ,

    "walkLiftRotation_x (-0.05f)"                       , /** The amount the swinging foot is rotated while getting lifted */
    "walkLiftRotation_y (-0.1f)"                        ,
    "walkLiftRotation_z (0.f)"                          ,

    "walkSupportRotation (0.f)"                         , /** A rotation added to the supporting foot to boost the com acceleration */

    "walkComLiftOffset_x (0.f)"                         , /** The height the center of mass is lifted within a single support phase */
    "walkComLiftOffset_y (0.f)"                         ,
    "walkComLiftOffset_z (2.3f)"                        ,

    "walkComBodyRotation (v5 = 0.07f)(v4 = 0.05f)"      , /** How much the torso is rotated to achieve the center of mass shift along the y-axis */
    "speedMax_rot (0.5f)"                               , /** The maximum walking speed (in "size of two steps") */
    "speedMax_x (120.f)"                                ,    
    "speedMax_y (50.f)"                                 ,    
        
    "speedMaxBackwards (80.f)"                          , /** The maximum walking speed for backwards walking (in "size of two steps") */

    "speedMaxChange_rot (0.1f)"                         , /** The maximum walking speed deceleration that is used to avoid overshooting of the walking target */
    "speedMaxChange_x (8.f)"                            ,
    "speedMaxChange_y (20.f)"                           ,

    "balance (true)"                                    , /**  Whether sensory feedback should be used or not */
    "balanceBodyRotation_x (0.8f)"                      , /** A  torso rotation p-control factor */
    "balanceBodyRotation_y (0.f)"                       ,

    "balanceCom_x (0.054f)"                             , /** A measured center of mass position adoption factor */
    "balanceCom_y (0.054f)"                             ,
    "balanceComVelocity_x (0.14f)"                      , /** A measured center of mass velocity adoption factor */
    "balanceComVelocity_y (0.14f)"                      ,
    "balanceRef_x (0.f)"                                , /** A pendulum pivot point p-control factor */
    "balanceRef_y (0.08f)"                              ,

    "balanceNextRef_x (0.2f)"                           , /** A pendulum pivot point of the upcoming single support phase p-control factor */
    "balanceNextRef_y (0.f)"                            ,

    "balanceStepSize_x (0.1f)"                          , /** A step size i-control factor */
    "balanceStepSize_y (-0.04f)"                        ,

    "observerMeasurementDelay (40.f)"                   , /** The delay between setting a joint angle and
                                                                the ability of measuring the result */
    "observerMeasurementDeviation (2.f)"                , /** The measurement uncertainty of the computed "measured" center of mass position */
    "observerMeasurementDeviation (2.f)"                ,

    "observerProcessDeviation_x (0.1f)"                 , /** The noise of the filtering process that estimates the position of the center of mass */
    "observerProcessDeviation_y (0.1f)"                 ,
    "observerProcessDeviation_z (3.f)"                  ,
    "observerProcessDeviation_w (3.f)"                  ,
     
    "odometryScale_rot (1.f)"                           , /** A scaling factor for computed odometry data */
    "odometryScale_x   (1.f)"                           ,
    "odometryScale_y   (1.f)"                           ,

    /* Parameters to calculate the correction of the torso's angular velocity. */
    "gyroStateGain (0.01f)"                             , /** Control weight (P) of the torso's angular velocity error. */
    "gyroDerivativeGain (0.0001f)"                      , /** Control weight (D) of the approximated rate of change of the angular velocity error. */
    "gyroSmoothing (0.5f)"                              , /** Smoothing (between 0 and 1!) to calculate the moving average of the y-axis gyro measurements. */
    "minRotationToReduceStepSize (1.3f)"                , /** I have no idea what im doing! Colin pls fix this! (Actual B-Human comment)**/
	};

    /* Values for the v4 robot. v5 values are in side comment.
     * The two versions share most of the same parameter values
     * (As B-Human put them).
     */
    private String[] defaultValuesV4 = new String [] {
                                      "50.f"   ,   
                                      "262.0f" ,
                                      "0.f"    ,   
                                      "0.2f"   ,   
                                      "0.f"    ,
                                      "85"     ,   
                                      "85"     ,   
                                      "16.f"   ,   
                                      "50.f"   ,
                                      "9.5f"   ,   
                                      "40.f"   ,
                                      "-2.f"   ,   
                                      "3.f"    ,
                                      "-30.f"  ,   
                                      "30.f"   ,
                                      "-30.f"  ,   
                                      "30.f"   ,
                                      "-50.f"  ,   
                                      "60.f"   ,
                                      "-55.f"  ,   
                                      "66.f"   ,
                                      "525.f"  , //(v5 = 535.f) Walkstep duration (index 21)
                                      "525.f"  , //(v5 = 535.f) Walkstep duration at full speed.(index 22)
                                      "180.f"  ,   
                                      "262.f"  ,   
                                      "262.f"  ,   
                                      "0.1f"   ,   
                                      "0.f"    ,   
                                      "1.f"    ,   
                                      "0.f"    ,   
                                      "1.f"    ,   

                                      "0.f"    , //(v5 = (0.f, 5.f, 22.f) 
                                      "5.f"    , //These are in the same vector
                                      "17.f"   , //(v5 = 22.f) (index 33) 

                                      "0.f"    , //(v5 = (0.f, 5.f, 22.f)
                                      "5.f"    , //These are in the same vector
                                      "25.f"   , //(v5 = 22.f) (index 36)

                                      "0.f"    ,   
                                      "20.f"   ,   
                                      "25.f"   ,   
                                      "-0.05f" ,   
                                      "-0.1f"  ,   
                                      "0.f"    ,   
                                      "0.f"    ,   
                                      "0.f"    ,   
                                      "0.f"    ,   
                                      "2.3f"   ,   
                                      "0.05f"  , //(v5 = 0.07f) Single value (index 47)
                                      "0.5f"   , 
                                      "120.f"  , 
                                      "50.f"   , 
                                      "80.f"   , 
                                      "0.1f"   , 
                                      "8.f"    , 
                                      "20.f"   , 
                                      "true"   , //only boolean value. balance (index 55) used for testing
                                      "0.8f"   ,   
                                      "0.f"    ,   
                                      "0.054f" ,   
                                      "0.054f" ,   
                                      "0.14f"  ,   
                                      "0.14f"  ,   
                                      "0.f"    ,   
                                      "0.08f"  ,   
                                      "0.2f"   ,   
                                      "0.f"    ,   
                                      "0.1f"   ,   
                                      "-0.04f" ,   
                                      "40.f"   ,   
                                      "2.f"    ,   
                                      "2.f"    ,
                                      "0.1f"   ,   
                                      "0.1f"   ,   
                                      "3.f"    ,   
                                      "3.f"    ,   
                                      "1.f"    ,   
                                      "1.f"    ,   
                                      "1.f"    ,   
                                      "0.01f"  ,   
                                      "0.0001f",   
                                      "0.5f"   ,   
                                      "1.3f"   ,   
    };

    private void buildV5ParamList(){

        defaultValuesV5 = new String[defaultValuesV4.length];

        System.arraycopy(defaultValuesV4, 0, defaultValuesV5, 0, defaultValuesV4.length);        

        //Actual default values for V5 robot for specific parameters.
        defaultValuesV5[21] = "535.f"; //WalkStepDuration
        defaultValuesV5[22] = "535.f"; //WalkStepDurationAtFullSpeedX
        defaultValuesV5[33] = "22.f";
        defaultValuesV5[36] = "22.f";
        defaultValuesV5[47] = "0.07f";

        //test
        //defaultValuesV5[55] = "false";
    }

    public int getListOfParamsLength(){
        return listOfV4Params.length;
    }
    public String[] getListOfParams(){
        return listOfV4Params;
    }
    public String[] getDefaultValuesV4(){
        return defaultValuesV4;
    }
    public String[] getDefaultValuesV5(){
        return defaultValuesV5;
    }

    /* Called when the Set Param button is pressed. Doesnt check if the 
     * input for balance is "true" or "false" 
     * */
    public void sendDataOverNetwork(String[] packet){

        //System.out.println("Printing whole array");
        //System.out.println(Arrays.toString(packet));
        //for(i=0
        //System.out.println("list of v4 params length: " + listOfV4Params.length);
        //System.out.println("Length of array: " + packet.length + "| Last element: " + packet[81] + "|First element:" + packet[0]);

        WalkEnginePreferences walkingEngineParams = WalkEnginePreferences.newBuilder()
                              .setVectorStandComPosY(Float.parseFloat(packet[0]))
                              .setVectorStandComPosZ(Float.parseFloat(packet[1]))

                              .setStandBodyTilt(Float.parseFloat(packet[2]))

                              .setVectorStandArmJointAngleX(Float.parseFloat(packet[3]))
                              .setVectorStandArmJointAngleY(Float.parseFloat(packet[4]))

                              .setStandHardnessAnklePitch(Long.parseLong(packet[5]))    //These are long!
                              .setStandHardnessAnkleRoll(Long.parseLong(packet[6]))     //These are long!

                              .setVectorWalkRefX(Float.parseFloat(packet[7]))
                              .setVectorWalkRefY(Float.parseFloat(packet[8]))

                              .setVectorWalkRefAtFullSpeedX(Float.parseFloat(packet[9]))
                              .setVectorWalkRefAtFullSpeedY(Float.parseFloat(packet[10]))

                              .setRangeWalkRefPlanningLimitLow(Float.parseFloat(packet[11]))
                              .setRangeWalkRefPlanningLimitHigh(Float.parseFloat(packet[12]))

                              .setRangeWalkRefXLimitLow(Float.parseFloat(packet[13]))
                              .setRangeWalkRefXLimitHigh(Float.parseFloat(packet[14]))

                              .setRangeWalkRefYLimitLow(Float.parseFloat(packet[15]))
                              .setRangeWalkRefYLimitHigh(Float.parseFloat(packet[16]))

                              .setRangeWalkStepSizeXPlanningLimitLow(Float.parseFloat(packet[17]))
                              .setRangeWalkStepSizeXPlanningLimitHigh(Float.parseFloat(packet[18]))

                              .setRangeWalkStepSizeXLimitLow(Float.parseFloat(packet[19]))
                              .setRangeWalkStepSizeXLimitHigh(Float.parseFloat(packet[20]))

                              .setWalkStepDuration(Float.parseFloat(packet[21])) //This index must be 21
                              .setWalkStepDurationAtFullSpeedX(Float.parseFloat(packet[22]))//This index must be 22
                              .setWalkStepDurationAtFullSpeedY(Float.parseFloat(packet[23]))

                              .setVectorWalkHeightX(Float.parseFloat(packet[24]))
                              .setVectorWalkHeightY(Float.parseFloat(packet[25]))

                              .setWalkArmRotationAtFullSpeedX(Float.parseFloat(packet[26]))

                              .setWalkMovePhaseBeginning(Float.parseFloat(packet[27]))
                              .setWalkMovePhaseLength(Float.parseFloat(packet[28]))

                              .setWalkLiftPhaseBeginning(Float.parseFloat(packet[29]))
                              .setWalkLiftPhaseLength(Float.parseFloat(packet[30]))

                              .setVectorWalkLiftOffSetX(Float.parseFloat(packet[31]))
                              .setVectorWalkLiftOffSetY(Float.parseFloat(packet[32]))
                              .setVectorWalkLiftOffSetZ(Float.parseFloat(packet[33]))

                              .setVectorWalkLiftOffSetAtFullSpeedXX(Float.parseFloat(packet[34]))
                              .setVectorWalkLiftOffSetAtFullSpeedXY(Float.parseFloat(packet[35]))
                              .setVectorWalkLiftOffSetAtFullSpeedXZ(Float.parseFloat(packet[36]))

                              .setVectorWalkLiftOffSetAtFullSpeedYX(Float.parseFloat(packet[37]))
                              .setVectorWalkLiftOffSetAtFullSpeedYY(Float.parseFloat(packet[38]))
                              .setVectorWalkLiftOffSetAtFullSpeedYZ(Float.parseFloat(packet[39]))

                              .setVectorWalkLiftRotationX(Float.parseFloat(packet[40]))
                              .setVectorWalkLiftRotationY(Float.parseFloat(packet[41]))
                              .setVectorWalkLiftRotationZ(Float.parseFloat(packet[42]))

                              .setWalkSupportRotation(Float.parseFloat(packet[43]))

                              .setWalkComLiftOffSetX(Float.parseFloat(packet[44]))
                              .setWalkComLiftOffSetY(Float.parseFloat(packet[45]))
                              .setWalkComLiftOffSetZ(Float.parseFloat(packet[46]))

                              .setWalkComBodyRotation(Float.parseFloat(packet[47]))

                              .setSpeedMaxRot(Float.parseFloat(packet[48]))
                              .setSpeedMaxVectorX(Float.parseFloat(packet[49]))
                              .setSpeedMaxVectorY(Float.parseFloat(packet[50]))

                              .setSpeedMaxBackwards(Float.parseFloat(packet[51]))

                              .setSpeedMaxChangeRot(Float.parseFloat(packet[52]))
                              .setSpeedMaxChangeVectorX(Float.parseFloat(packet[53]))
                              .setSpeedMaxChangeVectorY(Float.parseFloat(packet[54]))

                              .setBalance(Boolean.parseBoolean(packet[55])) //Boolean Conversion! TODO!! This index must be 55

                              .setVectorBalanceBodyRotationX(Float.parseFloat(packet[56]))
                              .setVectorBalanceBodyRotationY(Float.parseFloat(packet[57]))

                              .setVectorBalanceComX(Float.parseFloat(packet[58]))
                              .setVectorBalanceComY(Float.parseFloat(packet[59]))

                              .setVectorBalanceComVelocityX(Float.parseFloat(packet[60]))
                              .setVectorBalanceComVelocityY(Float.parseFloat(packet[61]))

                              .setVectorBalanceRefX(Float.parseFloat(packet[62]))
                              .setVectorBalanceRefY(Float.parseFloat(packet[63]))

                              .setVectorBalanceNextRefX(Float.parseFloat(packet[64]))
                              .setVectorBalanceNextRefY(Float.parseFloat(packet[65]))

                              .setVectorBalanceStepSizeX(Float.parseFloat(packet[66]))
                              .setVectorBalanceStepSizeY(Float.parseFloat(packet[67]))

                              .setObserverMeasurementDelay(Float.parseFloat(packet[68]))

                              .setVectorObserverMeasurementDeviationX(Float.parseFloat(packet[69]))
                              .setVectorObserverMeasurementDeviationY(Float.parseFloat(packet[70]))

                              .setVectorObserverProcessDeviationX(Float.parseFloat(packet[71]))
                              .setVectorObserverProcessDeviationY(Float.parseFloat(packet[72]))
                              .setVectorObserverProcessDeviationZ(Float.parseFloat(packet[73]))
                              .setVectorObserverProcessDeviationW(Float.parseFloat(packet[74]))

                              .setOdometryScaleRot(Float.parseFloat(packet[75]))
                              .setOdometryScaleVectorX(Float.parseFloat(packet[76]))
                              .setOdometryScaleVectorY(Float.parseFloat(packet[77]))

                              .setGyroStateGain(Float.parseFloat(packet[78]))
                              .setGyroDerivativeGain(Float.parseFloat(packet[79]))
                              .setGyroSmoothing(Float.parseFloat(packet[80]))

                              .setMinRotationToReduceStepSize(Float.parseFloat(packet[81]))

                              .build(); //incredibly everything here is treated as in the same line
        
        ControlInstance inst = ControlIO.getByIndex(0);
        if (inst == null) {
			Logger.log(Logger.WARN, "EngineParameterPanel clicked while no ControlInstance available");
            //does not return anything
			return;
		}

        boolean success = inst.tryAddCmnd(ControlIO.createCmndSetWalkingEngineParameters(walkingEngineParams));

        if(success){ System.out.println("Successfully sent a walking engine parameters command through the tool");}

	    Logger.logf(Logger.INFO, "EngineParameterPanel: CommandIO.createCmdSendMotionCommands(%s) returned %B\n", packet, success);
    }

    //Variables used.
    private String[] defaultValuesV5;

}
