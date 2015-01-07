/*
 * Copyright (c) 2012 Socialize Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.socialize.demo.snippets;

//begin-snippet-0

import android.app.Activity;
import android.os.Bundle;
import com.socialize.Socialize;
import com.socialize.ui.DefaultSocializeActivityLifecycleListener;
import com.socialize.ui.SocializeUIActivity;

public class SampleActivityWithListener extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set a lifecycle listener
        Socialize.setSocializeActivityLifecycleListener(new DefaultSocializeActivityLifecycleListener() {

            @Override
            public void onPause(SocializeUIActivity activity) {
                // Add your code here
            }

            @Override
            public void onResume(SocializeUIActivity activity) {
                // Add your code here
            }

            // Implement other methods as required
        });

        // Call Socialize in onCreate
        Socialize.onCreate(this, savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Call Socialize in onPause
        Socialize.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Call Socialize in onResume
        Socialize.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call Socialize in onStart
        Socialize.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Call Socialize in onStop
        Socialize.onStop(this);
    }

    @Override
    protected void onDestroy() {
        // Call Socialize in onDestroy before the activity is destroyed
        Socialize.onDestroy(this);

        super.onDestroy();
    }
}
//end-snippet-0
