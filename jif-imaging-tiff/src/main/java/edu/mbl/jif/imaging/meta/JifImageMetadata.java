package edu.mbl.jif.imaging.meta;

import java.util.Date;
import java.awt.Rectangle;


/**
 * <p>Title: JifImageMetadata </p>
 *
// Speculative.... GBH: Adapted from OME...

 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: MBL </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JifImageMetadata
{

   // ID
   public String polStackID = "0000";

   // Project / Session (original)
   public String project;
   public String session;

   // Acquisition
   // Camera settings
   public long width;
   public long height;
   public long binning;
   public long depth;
   public long exposure;
   public long gain;
   public long offset;
   public Rectangle roi;

   // Multi-frame integration or averaging
   public int frames = 1;
   public int averaging = 1;

   // Series acq.
   public int NthInSeries = 0;
   public int inSeriesOf = 0;

   // Z

   // Other
   Date dateAcquired = new Date();
   public long timeAcquired = 0;
   public long timeToAcquire = 0;

   // Averaging (old, 4:1 type, no longer used)
   public boolean averaged = false;

   // VariLC settings
   public float[] retarderA = {
                              0,
                              0,
                              0,
                              0,
                              0
   };
   public float[] retarderB = {
                              0,
                              0,
                              0,
                              0,
                              0
   };

   // PolStack parameters
   public float retardanceMax = 0; // Maximum,regardless of retCeiling.
   public float swingFraction = 0.03f; // =swing/wavelength ~ 0.03
   public float wavelength = 546f; // wavelength in nm ~ 546 nm
   public float zeroIntensity = 0.0f;
   public int retCeiling = 10; // maximum image retardance in nm
  // public int algorithm = PSj.CALC_FOUR;
   public int azimuthRef = 0; // azimuth reference in (whole) degrees
   public float dynamicRange = 1.0f; // dynamic range of ...

   // Background correction - filename of stack used for correction in magImage
   public String bkgdStackFile = null;
   public String bkgdStackMD5 = null;
   public boolean doBkgdCorrection = false;
   public String bkgdCorrectionMethod = null; // ["PolStack" | "subtract"];

   // Ratioing Correction
   public Rectangle ratioingRoi = new Rectangle(0, 0, 0, 0);
   public double[] ratioingAvg = null; // for each slice
   public boolean doRatioing = false;

   // ??? Mirror ??
   
   // / / / / / / / /   From O M E   / / / / / / / /

   // -- Constants --
   public static final String ID = "ID";

   //Experiment
   public static final String EXPERIMENT = "Experiment";
   public static final String EXPERIMENT_TYPE = "Type";
   public static final String EXPERIMENT_DESCRIPTION = "Description";
   public static final String EXPERIMENT_EXPERIMENTER = "Experimenter";

   //Experimenter
   public static final String EXPERIMENTER = "Experimenter";
   public static final String EXPERIMENTER_FIRST_NAME = "FirstName";
   public static final String EXPERIMENTER_LAST_NAME = "LastName";
   public static final String EXPERIMENTER_EMAIL = "Email";
   public static final String EXPERIMENTER_OME_NAME = "OMEName";

   //ExperimenterGroup
   public static final String EXPERIMENTER_GROUP = "ExperimenterGroup";
   public static final String EXPERIMENTER_GROUP_EXPERIMENTER =
         "Experimenter";
   public static final String EXPERIMENTER_GROUP_GROUP = "Group";

   //Detector
   public static final String DETECTOR = "Detector";
   public static final String DETECTOR_MANUFACTURER = "Manufacturer";
   public static final String DETECTOR_MODEL = "Model";
   public static final String DETECTOR_SERIAL_NUMBER = "SerialNumber";
   public static final String DETECTOR_TYPE = "Type";
   public static final String DETECTOR_INSTRUMENT = "Instrument";

   //Dimensions
   public static final String DIMENSIONS = "Dimensions";
   public static final String DIMENSIONS_PIXEL_SIZE_X = "PixelSizeX";
   public static final String DIMENSIONS_PIXEL_SIZE_Y = "PixelSizeY";
   public static final String DIMENSIONS_PIXEL_SIZE_Z = "PixelSizeZ";
   public static final String DIMENSIONS_PIXEL_SIZE_C = "PixelSizeC";
   public static final String DIMENSIONS_PIXEL_SIZE_T = "PixelSizeT";

   //DisplayChannel
   public static final String DISPLAY_CHANNEL = "DisplayChannel";
   public static final String DISPLAY_CHANNEL_CHANNEL_NUMBER = "ChannelNumber";
   public static final String DISPLAY_CHANNEL_BLACK_LEVEL = "BlackLevel";
   public static final String DISPLAY_CHANNEL_WHITE_LEVEL = "WhiteLevel";
   public static final String DISPLAY_CHANNEL_GAMMA = "Gamma";

   //DisplayOptions
   public static final String DISPLAY_OPTIONS = "DisplayOptions";
   public static final String DISPLAY_OPTIONS_ZOOM = "Zoom";
   public static final String DISPLAY_OPTIONS_Z_START = "ZStart";
   public static final String DISPLAY_OPTIONS_Z_STOP = "ZStop";
   public static final String DISPLAY_OPTIONS_T_START = "TStart";
   public static final String DISPLAY_OPTIONS_T_STOP = "TStop";
   public static final String DISPLAY_OPTIONS_RED_CHANNEL = "RedChannel";
   public static final String DISPLAY_OPTIONS_GREEN_CHANNEL = "GreenChannel";
   public static final String DISPLAY_OPTIONS_BLUE_CHANNEL = "BlueChannel";

   //DisplayROI
   public static final String DISPLAY_ROI = "DisplayROI";
   public static final String DISPLAY_ROI_X0 = "X0";
   public static final String DISPLAY_ROI_Y0 = "Y0";
   public static final String DISPLAY_ROI_Z0 = "Z0";
   public static final String DISPLAY_ROI_X1 = "X1";
   public static final String DISPLAY_ROI_Y1 = "Y1";
   public static final String DISPLAY_ROI_Z1 = "Z1";
   public static final String DISPLAY_ROI_T0 = "T0";
   public static final String DISPLAY_ROI_T1 = "T1";
   public static final String DISPLAY_ROI_DISPLAY_OPTIONS = "DisplayOptions";

   //Filter
   public static final String FILTER = "Filter";
   public static final String FILTER_INSTRUMENT = "Instrument";

   //FilterSet
   public static final String FILTER_SET = "FilterSet";
   public static final String FILTER_SET_MANUFACTURER = "Manufacturer";
   public static final String FILTER_SET_MODEL = "Model";
   public static final String FILTER_SET_LOT_NUMBER = "LotNumber";
   public static final String FILTER_SET_FILTER = "Filter";

   //Group
   public static final String GROUP = "Group";
   public static final String GROUP_NAME = "Name";
   public static final String GROUP_LEADER = "Leader";
   public static final String GROUP_CONTACT = "Contact";

   //ImageExperiment
   public static final String IMAGE_EXPERIMENT = "ImageExperiment";
   public static final String IMAGE_EXPERIMENT_EXPERIMENT = "Experiment";

   //ImageInstrument
   public static final String IMAGE_INSTRUMENT = "ImageInstrument";
   public static final String IMAGE_INSTRUMENT_INSTRUMENT = "Instrument";

   //ImagePlate
   public static final String IMAGE_PLATE = "ImagePlate";
   public static final String IMAGE_PLATE_WELL = "Well";
   public static final String IMAGE_PLATE_PLATE = "Plate";
   public static final String IMAGE_PLATE_SAMPLE = "Sample";

   //ImagingEnvironment
   public static final String IMAGING_ENVIRONMENT = "ImagingEnvironment";
   public static final String IMAGING_ENVIRONMENT_TEMPERATURE =         "Temperature";
   public static final String IMAGING_ENVIRONMENT_AIR_PRESSURE =         "AirPressure";
   public static final String IMAGING_ENVIRONMENT_HUMIDITY = "Humidity";
   public static final String IMAGING_ENVIRONMENT_CO2_PERCENT =
         "CO2Percent";

   //Instrument
   public static final String INSTRUMENT = "Instrument";
   public static final String INSTRUMENT_MANUFACTURER = "Manufacturer";
   public static final String INSTRUMENT_MODEL = "Model";
   public static final String INSTRUMENT_SERIAL_NUMBER = "SerialNumber";
   public static final String INSTRUMENT_TYPE = "Type";

   //LightSource
   public static final String LIGHT_SOURCE = "LightSource";
   public static final String LIGHT_SOURCE_MANUFACTURER = "Manufacturer";
   public static final String LIGHT_SOURCE_MODEL = "Model";
   public static final String LIGHT_SOURCE_SERIAL_NUMBER = "SerialNumber";
   public static final String LIGHT_SOURCE_INSTRUMENT = "Instrument";

   //LogicalChannel
   public static final String LOGICAL_CHANNEL = "LogicalChannel";
   public static final String LOGICAL_CHANNEL_NAME = "Name";
   public static final String LOGICAL_CHANNEL_ILLUMINATION_TYPE = "IlluminationType";
   public static final String LOGICAL_CHANNEL_AUX_TECHNIQUE = "AuxTechnique";
   public static final String LOGICAL_CHANNEL_EX_WAVE = "ExWave";
   public static final String LOGICAL_CHANNEL_EM_WAVE = "EmWave";
   public static final String LOGICAL_CHANNEL_FLOUR = "Flour";
   public static final String LOGICAL_CHANNEL_ND_FILTER = "NDfilter";
   public static final String LOGICAL_CHANNEL_LIGHT_SOURCE = "LightSource";
   public static final String LOGICAL_CHANNEL_AUX_LIGHT_SOURCE = "AuxLightSource";
   public static final String LOGICAL_CHANNEL_DETECTOR = "Detector";
   public static final String LOGICAL_CHANNEL_OTF = "OTF";
   public static final String LOGICAL_CHANNEL_FILTER = "Filter";

   //Objective
   public static final String OBJECTIVE = "Objective";
   public static final String OBJECTIVE_MANUFACTURER = "Manufacturer";
   public static final String OBJECTIVE_MODEL = "Model";
   public static final String OBJECTIVE_SERIAL_NUMBER = "SerialNumber";
   public static final String OBJECTIVE_LENS_NA = "LensNA";
   public static final String OBJECTIVE_MAGNIFICATION = "Magnification";
   public static final String OBJECTIVE_INSTRUMENT = "Instrument";

   //OpticalTransferFunction
   public static final String OPTICAL_TRANSFER_FUNCTION = "OpticalTransferFunction";
   public static final String OPTICAL_TRANSFER_FUNCTION_SIZE_X = "SizeX";
   public static final String OPTICAL_TRANSFER_FUNCTION_SIZE_Y = "SizeY";
   public static final String OPTICAL_TRANSFER_FUNCTION_PIXEL_TYPE = "PixelType";
   public static final String
         OPTICAL_TRANSFER_FUNCTION_OPTICAL_AXIS_AVERAGE = "OpticalAxisAverage";
   public static final String OPTICAL_TRANSFER_FUNCTION_INSTRUMENT = "Instrument";
   public static final String OPTICAL_TRANSFER_FUNCTION_OBJECTIVE = "Objective";
   public static final String OPTICAL_TRANSFER_FUNCTION_FILTER = "Filter";

   //PixelChannelComponent
   public static final String PIXEL_CHANNEL_COMPONENT = "PixelChannelComponent";
   public static final String PIXEL_CHANNEL_COMPONENT_PIXELS = "Pixels";
   public static final String PIXEL_CHANNEL_COMPONENT_INDEX = "Index";
   public static final String PIXEL_CHANNEL_COMPONENT_COLOR_DOMAIN = "ColorDomain";
   public static final String PIXEL_CHANNEL_COMPONENT_LOGICAL_CHANNEL = "LogicalChannel";

   //Plate
   public static final String PLATE = "Plate";
   public static final String PLATE_NAME = "Name";
   public static final String PLATE_EXTERNAL_REFERENCE = "ExternalReference";

   //PlateScreen
   public static final String PLATE_SCREEN = "PlateScreen";
   public static final String PLATE_SCREEN_PLATE = "PlateScreenPlate";
   public static final String PLATE_SCREEN_SCREEN = "PlateScreenScreen";

   //Screen
   public static final String SCREEN = "Screen";
   public static final String SCREEN_NAME = "Name";
   public static final String SCREEN_EXTERNAL_REFERENCE = "ExternalReference";

   //StageLabel
   public static final String STAGE_LABEL = "StageLabel";
   public static final String STAGE_LABEL_NAME = "Name";
   public static final String STAGE_LABEL_X = "X";
   public static final String STAGE_LABEL_Y = "Y";
   public static final String STAGE_LABEL_Z = "Z";

   public JifImageMetadata () {
   }

}
