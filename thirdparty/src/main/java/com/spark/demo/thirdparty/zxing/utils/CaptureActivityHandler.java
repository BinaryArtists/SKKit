/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spark.demo.thirdparty.zxing.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.Result;
import com.spark.demo.thirdparty.R;
import com.spark.demo.thirdparty.zxing.activity.CaptureActivity;
import com.spark.demo.thirdparty.zxing.camera.CameraManager;
import com.spark.demo.thirdparty.zxing.decode.DecodeThread;


/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class CaptureActivityHandler extends Handler {

    private static final String TAG = "CaptureActivityHandler";
    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private final CameraManager cameraManager;
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(CaptureActivity activity, CameraManager cameraManager, int decodeMode) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity, decodeMode);
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
//        Logger.i("=-=============handleMessage:"+message.toString());
        int id = message.what;
        if (id == R.id.restart_preview) {
            Log.i(TAG, "-----------------------==R.id.restart_preview");
            restartPreviewAndDecode();
        } else if (id == R.id.decode_succeeded) {
            state = State.SUCCESS;
            Bundle bundle = message.getData();

            activity.handleDecode((Result) message.obj, bundle);
        } else if (id == R.id.decode_failed) {
            Log.i(TAG, "----------------------+R.id.decode_failed");
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        } else if (id == R.id.return_scan_result) {   // TODO: 2016/6/13   这个流程什么时候走？
            Log.i(TAG, "-------------R.id.return_scan_result");
            activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
            activity.finish();
        }
//		switch (message.what) {
//		case R.id.restart_preview:
//			restartPreviewAndDecode();
//			break;
//		case R.id.decode_succeeded:
//			state = State.SUCCESS;
//			Bundle bundle = message.getData();
//
//			activity.handleDecode((Result) message.obj, bundle);
//			break;
//		case R.id.decode_failed:
//			// We're decoding as fast as possible, so when one decode fails,
//			// start another.
//			state = State.PREVIEW;
//			cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
//			break;
//		case R.id.return_scan_result:
//			activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
//			activity.finish();
//			break;
//		}
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
		quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause()
            // will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
			cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        }
    }

}
