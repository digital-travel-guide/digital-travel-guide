/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package io.github.digital_travel_guide.digitaltravelguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/** Main {@code Activity} class for the Camera app. */
public class CameraActivity extends AppCompatActivity {

  private Camera2BasicFragment cameraFragment;
  private boolean mBellagio;

  public boolean ismBellagio() {
    return mBellagio;
  }

  public void setmBellagio(boolean mBellagio) {
    this.mBellagio = mBellagio;
  }

  public boolean ismCaesarsPalace() {
    return mCaesarsPalace;
  }

  public void setmCaesarsPalace(boolean mCaesarsPalace) {
    this.mCaesarsPalace = mCaesarsPalace;
  }

  private boolean mCaesarsPalace;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    cameraFragment = Camera2BasicFragment.newInstance();
    if (null == savedInstanceState) {
      getFragmentManager()
          .beginTransaction()
          .replace(R.id.container, cameraFragment)
          .commit();
    }
  }

    @Override
    protected void onResume() {
        super.onResume();
        //checkStatus();
    }

    void checkStatus() {
        boolean blar = true;
        while (blar) {
            if (this.mBellagio) {
                Toast.makeText(getApplicationContext(), "I saw Bellagio!", Toast.LENGTH_SHORT).show();
                blar = false;
            } else if (this.mCaesarsPalace) {
                Toast.makeText(getApplicationContext(), "I saw Caesars Palace!", Toast.LENGTH_SHORT).show();
                blar = false;
            }
        }
    }
}
