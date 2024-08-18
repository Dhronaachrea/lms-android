package com.skilrock.lms.communication;//package com.skilrock.lms.communication;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.Settings;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.util.DisplayMetrics;
//
//public class GlobalVariables {
//
//	public static boolean isLagos = false;
//
//	public static boolean isLive = false;
//	public static String BASE_URL = "http://192.168.124.37:8080/PMS/";
//	public static String deviceName = "android";
//	public static int selectedPosition = -1;
//	public static String deviceType = "phone";
//	public static String[] junk = new String[] { "Draw Result",
//			"Scratch Games", "Inbox", "Second Chance Of Winning",
//			"Change Password", "Locate Rtailer", "About" };
//
//	public int dpToPx(int dp, Context context) {
//		DisplayMetrics displayMetrics = context.getResources()
//				.getDisplayMetrics();
//		int px = Math.round(dp
//				* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
//		return px;
//	}
//
//	public static void showFragment(Fragment fragment, int position,
//			int layout, FragmentManager fManager) {
//		Bundle data = new Bundle();
//		data.putInt("position", position);
//		fragment.setArguments(data);
//		FragmentTransaction ft = fManager.beginTransaction();
//		// fManager.popBackStack(position + "",
//		// FragmentManager.POP_BACK_STACK_INCLUSIVE);
//		ft.addToBackStack(position + "");
//		ft.replace(layout, fragment, position + "");
//		ft.commit();
//	}
//
//	public static void showFragmentWihoutTag(Fragment fragment, int position,
//			int layout, FragmentManager fManager, int id) {
//		Bundle data = new Bundle();
//		data.putInt("id", id);
//		fragment.setArguments(data);
//		FragmentTransaction ft = fManager.beginTransaction();
//		// fManager.popBackStack(position + "",
//		// FragmentManager.POP_BACK_STACK_INCLUSIVE);
//		ft.addToBackStack(position + "");
//		ft.replace(layout, fragment/* , position + "" */);
//		ft.commit();
//	}
//
//	// public static Object fakeParser(String json, Class<?> modalClass) {
//	// Object object = null;
//	// Gson gson = new Gson();
//	// object = gson.fromJson(json, modalClass);
//	// return object;
//	//
//	// }
//
//	public static void fakeLoading(Context context) {
//		final ProgressDialog dialog = new ProgressDialog(context);
//		dialog.setMessage("Loading...");
//		dialog.setCancelable(false);
//		dialog.show();
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				dialog.dismiss();
//			}
//		}, 0);
//
//	}
//
//	public static void fakeLoadingThis(final Context context,
//			final Class<?> android) {
//		final ProgressDialog dialog = new ProgressDialog(context);
//		dialog.setMessage("Loading...");
//		dialog.setCancelable(false);
//		dialog.show();
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				dialog.dismiss();
//				((Activity) context)
//						.startActivity(new Intent(context, android));
//			}
//		}, 0);
//
//	}
//
//	public static void showDataAlert(final Context context) {
//		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//		alertDialog.setCancelable(false);
//		alertDialog.setMessage("No Network Connectivity");
//		alertDialog.setPositiveButton("Settings",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						((Activity) context).startActivity(new Intent(
//								Settings.ACTION_WIRELESS_SETTINGS));
//					}
//				});
//		alertDialog.setNegativeButton("OK", null);
//		alertDialog.show();
//	}
//
//
//}
