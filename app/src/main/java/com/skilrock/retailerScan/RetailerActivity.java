package com.skilrock.retailerScan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skilrock.adapters.RetailerTicketAdapter;
import com.skilrock.bean.MyRetailerBean;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by stpl on 10/3/2016.
 */
public class RetailerActivity extends Activity implements WebServicesListener {

    private Camera mCamera;
    private Preview mPreview;
    private ImageScanner imageScanner;
    private Handler autoFocusHandler;
    private Button save, check;
    private RetailerTicketAdapter retailer_adapter;
    private ArrayList<MyRetailerBean.TicketData> ticketBeans;
    private ListView itemList;
    private Dialog dialog;
    private String ticketNo;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mCamera != null)
            mCamera.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);
        autoFocusHandler = new Handler();
        imageScanner = new ImageScanner();
        imageScanner.setConfig(0, Config.X_DENSITY, 3);
        imageScanner.setConfig(0, Config.Y_DENSITY, 3);
//        mCamera = getCameraInstance();
        mPreview = new Preview(this);
        save = (Button) findViewById(R.id.upload);
        check = (Button) findViewById(R.id.check);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVariables.connectivityExists(RetailerActivity.this)) {
                    String path = "/rest/playerMgmt/uploadPlayerTicket";
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("playerId", VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.PLAYER_ID));
                        jsonObject.put("ticketNumber", ticketNo);
                        jsonObject.put("sessionId", VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.SESSION_ID));
                    } catch (Exception e) {
                        e.printStackTrace();
                        jsonObject = null;
                    }
                    if (jsonObject != null)
                        new PMSWebTask(RetailerActivity.this, path, "N/A", jsonObject, "SAVE", null, "Loading...").execute();
                    else
                        Toast.makeText(RetailerActivity.this, "Error in uploading", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    class Preview extends SurfaceView implements SurfaceHolder.Callback {
        private static final String TAG = "Preview";

        SurfaceHolder mHolder;
        public Camera camera;
        //    private Button scanButton;
        private ImageScanner scanner;

        Preview(Activity context) {
            super(context);


            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            autoFocusHandler = new Handler();
            mHolder = getHolder();
            // Instance barcode scanner
            scanner = new ImageScanner();
            scanner.setConfig(0, Config.X_DENSITY, 3);
            scanner.setConfig(0, Config.Y_DENSITY, 3);
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mHolder.setFormat(PixelFormat.TRANSLUCENT | WindowManager.LayoutParams.FLAG_BLUR_BEHIND);


        }

        private Camera openFrontFacingCameraGingerbread() {
            int cameraCount = 0;
            Camera cam = null;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        cam = Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        Log.e("", "Camera failed to open: " + e.getLocalizedMessage());
                    }
                }
            }

            return cam;
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, acquire the camera and tell it where
            // to draw.

            try {

                camera = Camera.open();
                camera.setDisplayOrientation(90);
            } catch (Exception e) {

            }

            try {
                camera.setPreviewDisplay(mHolder);


                camera.setPreviewCallback(new Camera.PreviewCallback() {

                    public void onPreviewFrame(byte[] data, Camera arg1) {
                        try {
                            Camera.Parameters parameters = camera.getParameters();
                            Camera.Size size = parameters.getPreviewSize();
                            Image barcode = new Image(size.width, size.height, "Y800");
                            barcode.setData(data);

                            int result = scanner.scanImage(barcode);
                            if (result != 0 && previewing) {
                                previewing = false;
                                SymbolSet syms = scanner.getResults();
                                for (final Symbol sym : syms) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ticketNo = sym.getData().trim().toString();
                                            Intent intent = new Intent();
                                            intent.putExtra("ticketNo", ticketNo);
                                            setResult(0012, intent);
                                            Toast.makeText(RetailerActivity.this, sym.getData().trim().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            Log.e("", "");
                            Log.d(TAG, "onPreviewFrame - wrote bytes: " + data.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                        }
                        Preview.this.invalidate();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void showAlertDialog(final String message) {


        }

        public void releaseCamera() {
            if (camera != null) {
                previewing = false;
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // Surface will be destroyed when we return, so stop the preview.
            // Because the CameraDevice object is not a shared resource, it's very
            // important to release it when the activity is paused.
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }


        public boolean previewing = true;

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // Now that the size is known, set up the camera parameters and begin
            // the preview.
            Camera.Parameters parameters = camera.getParameters();
//        parameters.setPreviewSize(w/2, h/2);
            camera.setParameters(parameters);
            camera.startPreview();
            camera.autoFocus(autoFocusCB);
        }

        private Handler autoFocusHandler;

        private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing && camera != null)
                    camera.autoFocus(autoFocusCB);
            }
        };


        // Mimic continuous auto-focusing
        Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);

            Log.d(TAG, "draw");
//            canvas.drawText("PREVIEW", canvas.getWidth() / 2, canvas.getHeight() / 2, p);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(retailer_adapter != null)
//        retailer_adapter.notifyDataSetChanged();
    }

    public void onResult(String methodType, Object resultData, final Dialog dialog) {
        this.dialog = dialog;

        try {
            JSONObject jsonObject = new JSONObject(resultData.toString());
            if (resultData != null) {
                switch (methodType) {
                    case "SAVE":
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        if (jsonObject.optInt("responseCode") == 1032) {
                            String messg = jsonObject.optString("responseMsg");
                            Toast.makeText(RetailerActivity.this, messg, Toast.LENGTH_SHORT).show();
                            return;
                        } else if (jsonObject.optInt("responseCode") == 1008) {
                            String messg = jsonObject.optString("responseMsg");
                            Toast.makeText(RetailerActivity.this, messg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MyRetailerBean retailerBean = new Gson().fromJson(resultData.toString(), MyRetailerBean.class);
                        if (retailerBean.getResponseCode() == 0) {
//                    ticketBeans = new ArrayList<>();
//                    ticketBeans = retailerBean.getTicketData();
//                    if (ticketBeans.size() > 0) {
//                        itemList.setVisibility(View.VISIBLE);
//                        retailer_adapter = new RetailerTicketAdapter(RetailerActivity.this, R.layout.retailer_ticket, ticketBeans);
//                        itemList.setAdapter(retailer_adapter);
                            Intent intent = new Intent();
                            intent.putExtra("refreshList", true);
                            setResult(0012, intent);
                            finish();
//                    }
                        } else if (retailerBean.getResponseCode() == 118) {
                            View.OnClickListener okClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UserInfo.setLogout(getApplicationContext());
                                    Intent intent = new Intent(getApplicationContext(),
                                            MainScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(
                                            GlobalVariables.startAmin,
                                            GlobalVariables.endAmin);
                                }
                            };
                            new DownloadDialogBox(RetailerActivity.this, retailerBean.getResponseMsg(), "", false, true,
                                    okClickListener, null).show();
                        }
                }
            } else {
                GlobalVariables.showServerErr(getApplicationContext());
            }
            if (dialog != null)
                dialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}

