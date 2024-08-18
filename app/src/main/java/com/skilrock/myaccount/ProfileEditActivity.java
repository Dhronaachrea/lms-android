package com.skilrock.myaccount;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.skilrock.bean.ProfileDetail;
import com.skilrock.bean.ProfileDetailEdited;
import com.skilrock.config.Config;
import com.skilrock.config.DummyJson;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CircleImageView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.RobotoButton;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.CropSquareTransformation;
import com.skilrock.lms.communication.JSONParser;
import com.skilrock.utils.Utils;
import com.skilrock.lms.communication.Communication;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.UploadFileToServer;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;
import com.squareup.picasso.Picasso;
import com.weidget.LotteryEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by stpl on 7/7/2015.
 */
public class ProfileEditActivity extends DrawerBaseActivity implements WebServicesListener {

    private static final int GALLARY_REQUEST = 10;
    private static final int CAMERA_REQUEST = 11;
    private CircleImageView imgProfile;
    private RobotoTextView txtPlayerName;
    private RobotoTextView txtState;
    private RobotoTextView txtCity;
    private RobotoButton btnSave;
    private LotteryEditText edFirstName;
    private LotteryEditText edLastName;
    private LotteryEditText edMobileNumber;
    private LotteryEditText edEmailId;
    private LotteryEditText edAddress;
    //    private LotteryEditText state;
//    private LotteryEditText city;
    private LotteryEditText edDOB;
    private RadioButton rbMale;
    private RadioButton rbFemale;

    private String playerName;
    private ProfileDetail.PersonalInfo personalInfo;
    private Intent intent;

    //    private byte[] profilePhoto;
    private String profilePhoto;
    private Map<String, String> states;
    private Map<String, String> cities;
    private String selectedState = "", selectedCity = "";
    private String selectedStateCode = "", selectedCityCode = "";
    private boolean isUploadImageToServer;
    private String imagePath;
    private Uri fileUri;
    private long MIN_FILE_SIZE_TO_COMPRESS = 200;
    private ImageView cancelBut;
    private Analytics analytics;
    private Context context;
    ProfileDetailEdited profile;
    private String loadingText;
    private GlobalPref globalPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_activity);
        context = this;
        globalPref = GlobalPref.getInstance(this);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.EDIT_PROFILE);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        intent = getIntent();
        playerName = intent.getStringExtra(ProfileFragment.PLAYER_NAME);
        personalInfo = (ProfileDetail.PersonalInfo) intent.getSerializableExtra(ProfileFragment.PERSONAL_INFO_KEY);
        sHeader();
        setDrawerItems();
        bindIds();
        setListners();
        init();
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText.setText(getResources().getString(R.string.account_edit_header));
        profile = new ProfileDetailEdited();
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageHeader();
        if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
            edAddress.setVisibility(View.GONE);
            findViewById(R.id.address_view).setVisibility(View.GONE);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void init() {
        txtPlayerName.setText(playerName);
        if (checkString(personalInfo.getFirstName()))
            edFirstName.setText(personalInfo.getFirstName());
        if (checkString(personalInfo.getLastName()))
            edLastName.setText(personalInfo.getLastName());
        if (checkString(personalInfo.getMobileNum()))
            edMobileNumber.setText(personalInfo.getMobileNum());
        if (checkString(personalInfo.getEmailId()))
            edEmailId.setText(personalInfo.getEmailId());
        if (checkString(personalInfo.getAddress()))
            edAddress.setText(personalInfo.getAddress());
        if (checkString(personalInfo.getDob()))
            edDOB.setText(personalInfo.getDob());
        if (checkString(personalInfo.getProfilePhoto())) {
            Picasso.with(getApplicationContext()).load(personalInfo.getProfilePhoto()).transform(new CropSquareTransformation()).placeholder(R.drawable.no_img).into(imgProfile);
            isUploadImageToServer = false;
        } else {
            imgProfile.setImageResource(R.drawable.no_img);
            isUploadImageToServer = false;
        }
        if (checkString(personalInfo.getState())) {
            txtState.setText(personalInfo.getState());
            selectedState = personalInfo.getState();
            selectedStateCode = personalInfo.getStateCode();
        }

        if (checkString(personalInfo.getCity())) {
            txtCity.setText(personalInfo.getCity());
            selectedCity = personalInfo.getCity();
            selectedCityCode = personalInfo.getCityCode();
        }
        if (checkString(personalInfo.getGender())) {
            if (personalInfo.getGender().equals("male"))
                rbMale.setChecked(true);
            else
                rbFemale.setChecked(true);
        }
    }

    private void setListners() {
        imgProfile.setOnClickListener(debouncedOnClickListener);
        txtState.setOnClickListener(debouncedOnClickListener);
        txtCity.setOnClickListener(debouncedOnClickListener);
        btnSave.setOnClickListener(debouncedOnClickListener);
        cancelBut.setOnClickListener(debouncedOnClickListener);
        rbMale.setOnTouchListener(touchListener);
        rbFemale.setOnTouchListener(touchListener);
    }

    private void bindIds() {
        imgProfile = (CircleImageView) findViewById(R.id.img_profile);
        txtPlayerName = (RobotoTextView) findViewById(R.id.txt_player_name);
        txtState = (RobotoTextView) findViewById(R.id.txt_state);
        txtCity = (RobotoTextView) findViewById(R.id.txt_city);
        edDOB = (LotteryEditText) findViewById(R.id.ed_dob);
        edDOB.setEnabled(false);
        btnSave = (RobotoButton) findViewById(R.id.btn_save);
        edFirstName = (LotteryEditText) findViewById(R.id.ed_first_name);

        edLastName = (LotteryEditText) findViewById(R.id.ed_last_name);
        edMobileNumber = (LotteryEditText) findViewById(R.id.ed_mobile_number);
        edMobileNumber.setEnabled(false);
        edEmailId = (LotteryEditText) findViewById(R.id.ed_email_id);
        edAddress = (LotteryEditText) findViewById(R.id.ed_address);
        rbMale = (RadioButton) findViewById(R.id.rb_male);

        rbFemale = (RadioButton) findViewById(R.id.rb_female);
        cancelBut = (ImageView) findViewById(R.id.cancel_but);
        /* request Focus for edFirstName By Mehul */
        edFirstName.requestFocus();
//        state = (LotteryEditText) findViewById(R.id.enter_other_state);
//        city = (LotteryEditText) findViewById(R.id.enter_other_city);
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fileUri != null) {
            outState.putString("cameraImageUri", fileUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            fileUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    private void selectImage() {
        clearFocus();
        hide_keyboard(this);
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    if (fileUri != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(intent, CAMERA_REQUEST);
//                        cancelBut.setVisibility(View.VISIBLE);
                    } else {
                        Utils.Toast(getApplicationContext(), "No Storage Found!!");
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLARY_REQUEST);
//                    cancelBut.setVisibility(View.VISIBLE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
//                    cancelBut.setVisibility(View.GONE);
                }
            }
        });
        builder.show();
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utils.Toast(getApplicationContext(), "dismiss");
                isUploadImageToServer = false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            cancelBut.setVisibility(View.VISIBLE);
            if (requestCode == CAMERA_REQUEST) {
                imagePath = fileUri.getPath();
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeFile(imagePath, bitmapOptions);
                    File file = new File(imagePath);
                    if (file.length() / 1024 > MIN_FILE_SIZE_TO_COMPRESS) {
                        compressBitmap(file);
                    } else {
                        bitmap = BitmapFactory.decodeFile(imagePath, bitmapOptions);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(getImageOrientation(imagePath));
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), matrix, true);
                        imgProfile.setImageBitmap(rotatedBitmap);
                    }
                    isUploadImageToServer = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    isUploadImageToServer = false;
                }
            } else if (requestCode == GALLARY_REQUEST) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = this.getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    imagePath = picturePath;
                    c.close();

                    File file = new File(picturePath);
                    long length = file.length();
                    if (length / 1024 > MIN_FILE_SIZE_TO_COMPRESS) {
                        compressBitmap(file);
                    } else {
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bitmapOptions.inSampleSize = 8;
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bitmapOptions);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(getImageOrientation(imagePath));
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), matrix, true);
                        imgProfile.setImageBitmap(rotatedBitmap);
                    }
                    isUploadImageToServer = true;

                } catch (Exception e) {
                    e.printStackTrace();
                    isUploadImageToServer = false;
                }
            }
        }/* else {
            cancelBut.setVisibility(View.GONE);
        }*/
    }

    private void compressBitmap(File file) {
        FileOutputStream out = null;
        try {
            Bitmap reducedBitmap = decodeFile(file);
            out = new FileOutputStream(getCompressFile());

            Matrix matrix = new Matrix();
            matrix.postRotate(getImageOrientation(file.getPath()));
            Bitmap rotatedBitmap = Bitmap.createBitmap(reducedBitmap, 0, 0, reducedBitmap.getWidth(),
                    reducedBitmap.getHeight(), matrix, true);
            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            imgProfile.setImageBitmap(rotatedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getImageOrientation(String imagePath) {
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

//    @Override
//    public void onDebouncedClick(View v) {
//
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_profile:
//                analytics.sendAction(Fields.Category.PROFILE_EDIT, Fields.Action.CLICK);
//                selectImage();
//                //cancelBut.setVisibility(View.VISIBLE);
//                break;
//
//            case R.id.txt_state:
//                if (states == null)
//                    new Place("Loading States...").execute();
//                else
//                    openDialog(getKeys(states), "States", false);
//                break;
//
//            case R.id.txt_city:
//                if (cities == null)
//                    new Place(selectedStateCode, "Loading Cities...").execute();
//                else
//                    openDialog(getKeys(cities), "Cities", true);
//                break;
//
//            case R.id.cancel_but:
//                Picasso.with(getApplicationContext()).load(VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_PIC_URL)).placeholder(R.drawable.no_img).into(imgProfile);
////                cancelBut.setVisibility(View.GONE);
//                break;
//
//            case R.id.btn_save:
//                analytics.sendAction(Fields.Category.PROFILE_EDIT, Fields.Action.CLICK);
//                try {
//                    if (GlobalVariables.connectivityExists(ProfileEditActivity.this)) {
//                        post();
//                    } else {
//                        GlobalVariables.showDataAlert(ProfileEditActivity.this);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }

    private void post() throws JSONException {
        ProfileDetailEdited profile = new ProfileDetailEdited();
        profile.setPlayerId(VariableStorage.UserPref.getStringData(this, VariableStorage.UserPref.PLAYER_ID));
        profile.setSessionId(VariableStorage.UserPref.getStringData(this, VariableStorage.UserPref.SESSION_ID));
        profile.setPlayerName(playerName);
        profile.setFirstName(getText(edFirstName));
        profile.setLastName(getText(edLastName));
        profile.setEmailId(getText(edEmailId));
        profile.setAddress(getText(edAddress));

//        // get Value from others field of state
//        if (!state.getText().toString().trim().equalsIgnoreCase("") && Config.COUNTRY.equalsIgnoreCase("ghana")) {
//            selectedStateCode = state.getText().toString().trim();
//            Utils.Toast(this, selectedStateCode, Toast.LENGTH_LONG);
//        }

//        // get Value from others field of state
//        if (!city.getText().toString().trim().equalsIgnoreCase("") && Config.COUNTRY.equalsIgnoreCase("ghana")) {
//            selectedCityCode = city.getText().toString().trim();
//            Utils.Toast(this, selectedCityCode, Toast.LENGTH_LONG);
//        }

        // check both state and city are selected
        //   if (checkString(selectedStateCode))
        profile.setStateCode(selectedStateCode);
        profile.setState(selectedState);
//        else if (state.getText().toString().trim().equalsIgnoreCase("") && Config.COUNTRY.equalsIgnoreCase("ghana")) {
//            Utils.Toast(this, getResources().getString(R.string.toast_state_string_enter), Toast.LENGTH_SHORT);
//            return;
//        }

        //------------------------comment by sagar starts-----------------------------//

//        else {
//            Utils.Toast(this, getResources().getString(R.string.toast_state_string_select), Toast.LENGTH_SHORT);
//            return;
//        }

        //------------------------comment by sagar ends-----------------------------//

        //   if (checkString(selectedCityCode))
        profile.setCityCode(selectedCityCode);

//        else if (city.getText().toString().trim().equalsIgnoreCase("") && Config.COUNTRY.equalsIgnoreCase("ghana")) {
//            Utils.Toast(this, getResources().getString(R.string.toast_city_string_enter), Toast.LENGTH_SHORT);
//            return;
//        }


        //------------------------comment by sagar starts-----------------------------//
//        else {
//            Utils.Toast(this, getResources().getString(R.string.toast_city_string_select), Toast.LENGTH_SHORT);
//            return;
//        }

        //------------------------comment by sagar ends-----------------------------//
        profile.setDob(edDOB.getText().toString());
        if (rbMale.isChecked())
            profile.setGender("male");
        else
            profile.setGender("female");
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        Utils.consolePrint(json);
        JSONObject jsonObject = new JSONObject(json);
//        Log.v("Image url in request", imagePath != null ? imagePath : "");
        if (isUploadImageToServer) {
            new UploadFileToServer(ProfileEditActivity.this, imagePath, jsonObject).execute();
        } else {
            jsonObject.put("profilePhoto", "");
            PMSWebTask PMSWebTask = new PMSWebTask(this, "/rest/playerMgmt/saveUserProfile", "N/A", jsonObject, "SAVE_PROFILE", ProfileDetail.class, "Saving...");
            PMSWebTask.execute();
        }
    }

    private String getText(EditText view) {
        return view.getText().toString();
    }

    private boolean checkString(String stn) {
        if (stn != null && !stn.equals(""))
            return true;
        return false;
    }

    private CharSequence[] getKeys(Map<String, String> map) {
        CharSequence[] keys;
        // enter OTHERS Field
//        if (Config.COUNTRY.equalsIgnoreCase("ghana")) {
//            keys = new CharSequence[map.size() + 1];
//        } else {
        keys = new CharSequence[map.size()];
//        }
        Set<Map.Entry<String, String>> set = map.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : set) {
            keys[i] = entry.getKey();
            i++;
        }
//        if (Config.COUNTRY.equalsIgnoreCase("ghana"))
//            keys[i] = "Others";
        return keys;
    }

    private void openDialog(final CharSequence[] options, String title, final boolean isCity) {

        new StateCityDialog(this, options, isCity).show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title);
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (isCity) {
//                    selectedCity = options[item].toString();
//                    selectedCityCode = cities.get(selectedCity);
//                    txtCity.setText(selectedCity);
//                } else {
//                    if (!selectedState.equals(options[item].toString()) || cities == null) {
//                        selectedState = options[item].toString();
//                        selectedStateCode = states.get(selectedState);
//                        txtState.setText(selectedState);
//                        new Place(states.get(selectedState), "Loading Cities...").execute();
//                    }
//                }
//            }
//        });
//        builder.show();
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
//        Log.v("Image url in onresult", imagePath + "\n" + resultData != null ? resultData.toString() : "");
//        String image = imagePath;
        if (resultData != null) {
            ProfileDetail profileDetail = (ProfileDetail) resultData;
            if (profileDetail.getResponseCode() == 0) {
                analytics.sendAll(Fields.Category.PROFILE_EDIT, Fields.Action.GET, Fields.Label.SUCCESS);
                Intent intent = new Intent();
                VariableStorage.UserPref.setStringPreferences(getApplicationContext(), VariableStorage.UserPref.USER_PIC_URL, profileDetail.getPersonalInfo().getProfilePhoto());
                intent.putExtra(ProfileFragment.PERSONAL_INFO_KEY, profileDetail.getPersonalInfo());
                intent.putExtra(ProfileFragment.WALLET_INFO_KEY, profileDetail.getWalletInfo());
                setResult(RESULT_OK, intent);
                finish();
                dialog.dismiss();
            } else if (profileDetail.getResponseCode() == 118 || profileDetail.getResponseCode() == 3009) {
                View.OnClickListener okClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo.setLogout(ProfileEditActivity.this);
                        Intent intent = new Intent(ProfileEditActivity.this, MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                        finish();
                    }
                };
                dialog.dismiss();
                new DownloadDialogBox(ProfileEditActivity.this, profileDetail.getResponseMsg(), "", false, true, okClickListener, null).show();
            } else {
                analytics.sendAll(Fields.Category.PROFILE_EDIT, Fields.Action.GET, Fields.Label.FAILURE);
                dialog.dismiss();
                Utils.Toast(getApplicationContext(), profileDetail.getResponseMsg());
            }
        } else {
            analytics.sendAll(Fields.Category.PROFILE_EDIT, Fields.Action.GET, Fields.Label.FAILURE);
            dialog.dismiss();
            GlobalVariables.showServerErr(ProfileEditActivity.this);
        }
    }

    private class Place extends PMSWebTask {
        private String message;
        private String stateCode;

        Place(String message) {
            this(null, message);
        }

        Place(String stateCode, String message) {
            super(ProfileEditActivity.this, message);
            this.message = message;
            this.stateCode = stateCode;
        }

        @Override
        protected void onPreExecute() {
//            dialog = new ProgressDialog(ProfileEditActivity.this);
//            dialog.setCancelable(false);
//
//            if (!message.equals("")) {
//                dialog.setMessage(message);
//            } else {
//                dialog.setMessage("Loading...");
//            }
//
//            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            JSONObject stateObject, cityObject;
            try {

                if (stateCode == null) {
                    if (Config.isStatic && GlobalVariables.connectivityExists(getApplicationContext()) && GlobalVariables.loadDummyData) {
                        stateObject = JSONParser.parse(DummyJson.dummyStateString);
                    } else {
                        stateObject = Communication.getStateList();
                    }
                    return states = getStatesCities(stateObject, "stateData", "stateName", "stateCode");
                } else {
                    if (Config.isStatic && GlobalVariables.connectivityExists(getApplicationContext()) && GlobalVariables.loadDummyData) {
                        cityObject = JSONParser.parse(DummyJson.dummyCityList);
                    } else {
                        cityObject = Communication.getCityList(stateCode);
                    }
                    return cities = getStatesCities(cityObject, "cityData", "cityName", "cityCode");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
            if (stateCode == null)
                openDialog(getKeys((Map<String, String>) result), "States", false);
            else
                openDialog(getKeys((Map<String, String>) result), "Cities", true);
            if (dialog.isShowing()) {
                dialog.cancel();
            }
        }

        // data[0] = arrayName, data[1] = city/state name, data[2] = city/state code this is belongs to json
        private Map<String, String> getStatesCities(JSONObject jsonObject, String... data) throws JSONException {
            Map<String, String> map = new HashMap<>();
            JSONArray stateData = jsonObject.getJSONArray(data[0]);
            for (int i = 0; i < stateData.length(); i++) {
                JSONObject object = stateData.getJSONObject(i);
                map.put(object.getString(data[1]), object.getString(data[2]));
            }
            return map;
        }
    }

    public Uri getOutputMediaFileUri() {
        try {
            return Uri.fromFile(getOutputMediaFile());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.US).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    private File getCompressFile() {
        // Internal sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME + File.separator + Config.subDir);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.US).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        imagePath = Uri.fromFile(mediaFile).getPath();
        return mediaFile;
    }

    private class StateCityDialog extends android.app.AlertDialog {
        private Context context;
        private ListView contactList;
        private ArrayAdapter<CharSequence> betAdapater;
        private CharSequence[] stringss;
        private ImageView done, close;
        private CustomTextView headerDialog;
        private boolean isCity;

        public StateCityDialog(Context context, CharSequence[] stringss, boolean isCity) {
            super(context, R.style.DialogZoomAnim);
            this.context = context;
            this.stringss = stringss;
            this.isCity = isCity;
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        }

        public StateCityDialog(CharSequence[] stringss, Context context) {
            super(context, R.style.DialogZoomAnim);
            this.context = context;
            this.stringss = stringss;
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        public void show() {
            super.show();
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            final View view = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.bet_dialog, null);
            View headerView = view.findViewById(R.id.header_id);
            done = (ImageView) headerView.findViewById(R.id.done);
            close = (ImageView) headerView.findViewById(R.id.close);
            headerDialog = (CustomTextView) headerView.findViewById(R.id.header_name);
            close.setVisibility(View.INVISIBLE);
            done.setImageResource(R.drawable.close);

            //new code
            String cityDialogHeaderName;
            String statesDialogHeaderName;
            if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
                cityDialogHeaderName = "Towns";
                statesDialogHeaderName = "Regions";
            } else {
                cityDialogHeaderName = "Cities";
                statesDialogHeaderName = "States";
            }

            if (isCity)
                headerDialog.setText(cityDialogHeaderName);
            else
                headerDialog.setText(statesDialogHeaderName);

            done.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            contactList = (ListView) view.findViewById(R.id.contact_list);
            betAdapater = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_expandable_list_item_1, stringss);
            contactList.setAdapter(betAdapater);
            contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    if (isCity) {
                        selectedCity = stringss[position].toString();
                        if (!selectedCity.equalsIgnoreCase("Others")) {
                            selectedCityCode = cities.get(selectedCity);
//                            city.setVisibility(View.GONE);
                        } else {
//                            city.setVisibility(View.VISIBLE);
//                            city.requestFocus();
                            selectedCityCode = "";
                        }
                        txtCity.setText(selectedCity);
                    } else {
                        if (!selectedState.equals(stringss[position].toString()) || cities == null) {
                            selectedState = stringss[position].toString();
                            if (!selectedState.equalsIgnoreCase("Others")) {
                                selectedStateCode = states.get(selectedState);
                                txtCity.setClickable(true);
//                                state.setVisibility(View.GONE);
//                                city.setVisibility(View.GONE);
                                if (globalPref.getCountry().equalsIgnoreCase("Ghana")) {
                                    selectedCity = "Select Town";
                                } else {
                                    selectedCity = "Select City";
                                }
                                selectedCityCode = "";
                                txtCity.setText(selectedCity);
                                if (globalPref.getCountry().equalsIgnoreCase("ghana"))
                                    loadingText = "Loading Towns...";
                                else loadingText = "Loading Cities...";
                                new Place(states.get(selectedState), loadingText).execute();
                            } else {
//                                state.setVisibility(View.VISIBLE);
//                                city.setVisibility(View.VISIBLE);
//                                state.requestFocus();
//                                state.setNextFocusDownId(R.id.enter_other_city);
                                txtCity.setClickable(false);
                                txtCity.setText(selectedState);
                                selectedCityCode = "";
                                selectedStateCode = "";
                            }
                            txtState.setText(selectedState);
                        }
                    }
                    cancel();
                }
            });
            this.setContentView(view);
        }

    }

    DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(500) {
        @Override
        public void onDebouncedClick(View v) {
            clearFocus();
            switch (v.getId()) {
                case R.id.img_profile:
                    analytics.sendAction(Fields.Category.PROFILE_EDIT, Fields.Action.CLICK);
                    selectImage();
                    //cancelBut.setVisibility(View.VISIBLE);
                    break;

                case R.id.txt_state:
                    if (!GlobalVariables.connectivityExists(ProfileEditActivity.this)) {
                        GlobalVariables.showDataAlert(ProfileEditActivity.this);
                        return;
                    }
                    if (states == null) {
                        if (globalPref.getCountry().equalsIgnoreCase("ghana"))
                            loadingText = "Loading Regions...";
                        else loadingText = "Loading States...";
                        new Place(loadingText).execute();
                    } else
                        openDialog(getKeys(states), "States", false);
                    break;

                case R.id.txt_city:
                    if (!GlobalVariables.connectivityExists(ProfileEditActivity.this)) {
                        GlobalVariables.showDataAlert(ProfileEditActivity.this);
                        return;
                    }
                    if (cities == null) {
                        if (globalPref.getCountry().equalsIgnoreCase("ghana"))
                            loadingText = "Loading Towns...";
                        else loadingText = "Loading Cities...";
                        new Place(selectedStateCode, loadingText).execute();
                    } else

                        openDialog(getKeys(cities), "Cities", true);
                    break;

                case R.id.cancel_but:
                    Picasso.with(getApplicationContext()).load(VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_PIC_URL)).placeholder(R.drawable.no_img).into(imgProfile);
//                cancelBut.setVisibility(View.GONE);
                    break;

                case R.id.btn_save:
                    analytics.sendAction(Fields.Category.PROFILE_EDIT, Fields.Action.CLICK);
                    try {
                        if (GlobalVariables.connectivityExists(ProfileEditActivity.this)) {
                            post();
                        } else {
                            GlobalVariables.showDataAlert(ProfileEditActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hide_keyboard(ProfileEditActivity.this);
            clearFocus();
//            Utils.ToastFargi(ProfileEditActivity.this, "this is fargi toast");

//            ((Activity) context).getCurrentFocus().clearFocus();
//            v.setFocusable(true);
//            v.requestFocus();
            return false;
        }
    };

    private void clearFocus() {
        getWindow().getDecorView().clearFocus();
        edFirstName.clearFocus();
        edLastName.clearFocus();
        edEmailId.clearFocus();
        edAddress.clearFocus();
        btnSave.requestFocus();
        btnSave.setFocusable(true);
    }
}