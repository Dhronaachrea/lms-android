package com.skilrock.myaccount;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.skilrock.customui.CircleImageView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.deposit.InitialDepositScreen;
import com.skilrock.utils.DataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyProfileScreen extends Fragment {
    public CircleImageView userPic;
    public CustomTextView userName, availableBalance, winningBalance,
            bonusBalance, depositBalance, redeemBalance, email, mobile,
            address, deposit;
    private Context context;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        View view = inflater.inflate(R.layout.my_profile, null);
        bindViewIds(view);
        userPic.setImageResource(R.drawable.no_img);
        userPic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        // userName.setText(com.skilrock.utils.DataSource.Login.username);
        userName.setText("VIJAY JOSHI");
        availableBalance.setText(Html.fromHtml("Available Bal: " + "<b>"
                + "$" + DataSource.Login.currentBalance + "</b>"));
        deposit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                replaceFragment(new InitialDepositScreen());
                ((MyAccountActivity) getActivity()).getSpinner()
                        .setSelection(1);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void bindViewIds(View view) {
        userName = (CustomTextView) view.findViewById(R.id.user_name);
        userPic = (CircleImageView) view.findViewById(R.id.user_pic);
        availableBalance = (CustomTextView) view.findViewById(R.id.user_bal);
        winningBalance = (CustomTextView) view.findViewById(R.id.winning_bal);
        bonusBalance = (CustomTextView) view.findViewById(R.id.bonus_bal);
        depositBalance = (CustomTextView) view.findViewById(R.id.depo_bal);
        redeemBalance = (CustomTextView) view.findViewById(R.id.red_bal);
        email = (CustomTextView) view.findViewById(R.id.email);
        mobile = (CustomTextView) view.findViewById(R.id.mobile);
        address = (CustomTextView) view.findViewById(R.id.address);
        deposit = (CustomTextView) view.findViewById(R.id.deposit);
    }

    void replaceFragment(Fragment fragment) {
        FragmentTransaction ft;
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragments_frame, fragment);
        // ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.commit();
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    userPic.setImageBitmap(bitmap);

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                userPic.setImageBitmap(thumbnail);
            }
        }
    }
}
