package com.word.sample.camera2.util;

import android.hardware.camera2.CameraAccessException;

import java.io.IOException;

public interface Camera2Listener {

    void onCameraException(CameraAccessException cae);

    void onNullPointerException(NullPointerException npe);

    void onInterruptedException(InterruptedException ie);

    void onIOException(IOException ioe);

    void onConfigurationFailed();
}
