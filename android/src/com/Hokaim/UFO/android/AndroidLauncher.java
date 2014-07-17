package com.Hokaim.UFO.android;

import android.os.Bundle;

import com.Hokaim.UFO.UFOGameStart;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.Hokaim.UFO.UFOGameStart;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new UFOGameStart(), config);
	}
}
