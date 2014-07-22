package com.Hokaim.UFO.android;

import android.os.Bundle;

import com.Hokaim.UFO.UFOGameStart;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
      config.useAccelerometer = true;
      config.useWakelock = true;
      config.useImmersiveMode = true;
		initialize(new UFOGameStart(), config);
	}
}
