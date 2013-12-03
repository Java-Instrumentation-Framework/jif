package edu.mbl.jif.gui.imaging.player;

import edu.mbl.jif.utils.StaticSwingUtils;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import edu.mbl.jif.utils.JifUtils;
import edu.mbl.jif.utils.prefs.Prefs;


import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.JButton;


public class SeriesPlayerController extends PlayerControlHoriz {
    SeriesViewer viewer;

    //ViewerAnalyzerPanel viewer;
    //SeriesPlayerFrame viewerSeries;
    MultipageTiffFile mTiffFile;
    int numSlices = 0;
    int slice = 0;
    boolean stop = false;
    int fps = Prefs.usr.getInt("seriesPlayerIncrFPS", 2);
    boolean goFwd = true;
    JButton buttonFPSreset = new JButton();

    public SeriesPlayerController() {
        this(new MultipageTiffFile(""), new SeriesPlayerFrame());
    }

    public SeriesPlayerController(MultipageTiffFile mTiffFile, SeriesViewer replayFrame) {
        super();
        this.mTiffFile = mTiffFile;
        viewer = replayFrame;
        setNumFrames(mTiffFile.getNumImages());
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setEnabledAll(true);
        this.setbackground(Color.lightGray);
        //this.setLayout(new FlowLayout());
        fps = Prefs.usr.getInt("seriesPlayerIncrFPS", 2);
        super.setFPS(fps);
        enableSliderListener(true);
        button_Pause.setEnabled(false);
        buttonFPSreset.setBackground(new Color(212, 239, 200));
        buttonFPSreset.setFont(new java.awt.Font("Dialog", 0, 10));
        buttonFPSreset.setOpaque(true);
        buttonFPSreset.setMargin(new Insets(2, 4, 2, 4));
        buttonFPSreset.setText("Reset FPS");
        //    this.add(buttonFPSreset, null);
        this.setPreferredSize(new Dimension(500, 55));
        this.validate();
    }

    public void setViewer(SeriesViewer replayFrame) {
        viewer = replayFrame;
    }

    public void setSeriesFile(MultipageTiffFile _vFile) {
        mTiffFile = _vFile;
    }

    public void setNumFrames(int _numFrames) {
        numSlices = _numFrames;
        setLastFrame(numSlices);
    }

    public boolean isOnLastSlice() {
         return (slice == (numSlices-1));
    }
    //----------------------------------------------------------------
    void button_Begin_actionPerformed(ActionEvent e) {
        gotoSlice(0);
    }

    void button_PlayRev_actionPerformed(ActionEvent e) {
        stop = false;
        goFwd = false;
        runPlay();
    }

    void button_StepBack_actionPerformed(ActionEvent e) {
        if (slice > 0) {
            slice--;
        }
        gotoSlice(slice);
    }

    synchronized void button_Pause_actionPerformed(ActionEvent e) {
        stop = true;
    }

    public synchronized void killPlayer() {
        stop = true;
    }

    void button_StepFwd_actionPerformed(ActionEvent e) {
        if (slice < (numSlices - 1)) {
            slice++;
        }
        gotoSlice(slice);
    }

    synchronized void button_PlayFwd_actionPerformed(ActionEvent e) {
        stop = false;
        goFwd = true;
        runPlay();
    }

    void runPlay() {
        final Thread worker = new Thread() {
                public void run() {
                    // @todo Suspend camera display for player...
                    //            if (Camera.display != null) {
                    //               Camera.displaySuspend();
                    //            }
                    edu.mbl.jif.utils.time.NanoTimer timer = new edu.mbl.jif.utils.time.NanoTimer();
                    setButtonsPlaying(true);
                    if (slice >= numSlices) {
                        slice = 0;
                    }
                    while (!stop) {
                        timer.reset();
                        timer.start();
                        if (goFwd) {
                            slice++;
                        } else {
                            slice--;
                        }
                        if (repeat) {
                            if (slice == numSlices) {
                                slice = 0;
                            }
                        } else if (bounce) {
                            if ((slice == 0) || (slice == (numSlices - 1))) {
                                goFwd = !goFwd;
                            }
                        } else {
                            if (slice == numSlices) {
                                break;
                            }
                        }
                        gotoSlice(slice);

                        int toGo = (int) ((1 / (float) fps * 1000) - timer.elapsedMillis());
                        if (toGo > 0) {
                            JifUtils.waitFor(toGo);
                        }
                    }
                    setButtonsPlaying(false);
                    return ;
                }
            };
        worker.start();
    }

    void button_End_actionPerformed(ActionEvent e) {
        gotoSlice(numSlices - 1);
    }

    void setButtonsPlaying(final boolean t) {
        StaticSwingUtils.dispatchToEDT(new Runnable() {

            public void run() {
                setEnabledAll(!t);
                enableSliderListener(!t);
                button_Pause.setEnabled(t);
                togglePlayStop.setSelected(t);
            }
        });
        // 
//        if (viewer != null) {
//            if (viewer.analysisPanel != null) {
//                viewer.analysisPanel.enableObservers(!t);
//            }
//        }
    }

    void sliderMoved(int n) {
        //System.out.println("sliderMoved " + n);
        slice = n;
        setCurrentDisplay(n + 1);
        showImage(n);
    }

    public void gotoSlice(int n) {
        if (n >= numSlices) {
            slice = n - 1;
        } else {
            slice = n;
        }
        updateSliderPosition(n); //<<<<<<<<<<<<<<===================== 09/03
        showImage(n);
    }

    void showImage(int _slice) {
        try {
            if (viewer != null) {
                StaticSwingUtils.dispatchToEDT(new Runnable() {

                    public void run() {

                        viewer.showImage(slice);

                    }

                });
                //viewer.showImage(slice, mTiffFile.getImage(slice));
            }

            //      if (viewerSeries != null)
            //         {
            //         viewerSeries.showImage(slice);
            //         //showImage(slice, mTiffFile.getImage(slice));
            //         }
        } catch (Exception ex) {
        }
    }

    void fps_Changed(int value) {
        fps = value;
        Prefs.usr.putInt("seriesPlayerIncrFPS", value);
    }

    public static void main(String[] args) {
    }
}
