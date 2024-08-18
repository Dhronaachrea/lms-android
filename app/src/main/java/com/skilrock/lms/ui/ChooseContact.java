package com.skilrock.lms.ui;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.skilrock.bean.ContactBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.utils.Utils;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.utils.GlobalVariables;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseContact extends AlertDialog {
    private Context context;
    private ImageView send;
    private ImageView close;
    private TextView header;
    private int count;
    private RobotoTextView noContact;
    private EditText searchContact;
    private ListView contactList;
    private StringBuffer buffer;
    private String numbers;
    private ArrayList<ContactBean> contactBeans = new ArrayList<ContactBean>();
    private HashMap<String, String> data;
    private ContactAdapater contactAdapter;

    public ChooseContact(Context context) {
        super(context, R.style.DialogZoomAnim);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void show() {
        super.show();
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        final View view = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.contact_dialog, null);
        header = (TextView) view.findViewById(R.id.header_name);
        searchContact = (EditText) view.findViewById(R.id.search_contact);
        contactList = (ListView) view.findViewById(R.id.contact_list);
        noContact = (RobotoTextView) view.findViewById(R.id.nCon);
        searchContact.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                contactAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        Display mDisplay = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        final int width = mDisplay.getWidth();
        final int height = mDisplay.getHeight();
        getWindow().setLayout(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        ((TextView) view.findViewById(R.id.header_name))
                .setText("Select Contacts");
        send = (ImageView) view.findViewById(R.id.done);
        close = (ImageView) view.findViewById(R.id.close);
        getContactList();

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (contactBeans.get(position).isSelected()) {
                    contactBeans.get(position).setSelected(false);
                } else
                    contactBeans.get(position).setSelected(true);

                contactAdapter.notifyDataSetChanged();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!GlobalVariables.connectivityExists(context)) {
                    GlobalVariables.showDataAlert(context);
                    return;
                }
                count = 0;
                buffer = new StringBuffer();
                ArrayList<ContactBean> list = ContactAdapater.copyOfModals;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelected()) {
                        count++;
                        if (!buffer.toString().contains(list.get(i).getNumber()))
                            buffer.append(list.get(i).getNumber() + ",");
                    }
                }
                if (count > 0) {
                    numbers = buffer.toString();
                    Utils.logPrint("Numbers-" + numbers);
                    numbers = numbers.substring(0, numbers.length() - 1);
//					Thread thread = new Thread(new Runnable() {
//
//						@Override
//						public void run() {
//							data = Communication.inviteFriend(
//									DataSource.Login.username, numbers);
//							handler.sendEmptyMessage(0);
//						}
//					});
//					thread.start();

                    //after zim merge validation
                    if (chkValidation(numbers)) {
                        String path = "/com/skilrock/pms/mobile/playerInfo/Action/invitePlayerRequest.action?";
                        String params = "userName=" + VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME) + "&mobileNum=" + numbers;
                        new PMSWebTask((Activity) context, path + params, "GET", new JSONObject(), "CONTACT", null, "Sending invitation...").execute();
                    }
                    // Toast.makeText(context, numbers,
                    // Toast.LENGTH_LONG).show();
                } else {
                    Utils.Toast(context, "No contact has been selected");
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private boolean chkValidation(String numbers) {
        if (numbers == null && numbers.equalsIgnoreCase("")) {
            return false;
        } else {
            if (chkNumberLength(numbers)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean chkNumberLength(String numbers) {
        String[] num = numbers.split(",");
        for (int i = 0; i < num.length; i++) {
            String mobNo = num[i].contains("-") ? num[i].replaceAll("-", "") : num[i];
            if ((mobNo.length() < 6 || mobNo.length() > 15)) {
                Utils.Toast(context, "mobile number length should be between 6 to 15 digits");
                return false;
            } else {
            }
        }
        return true;
    }

    private void getContactList() {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        if (cur.getCount() > 0) {
//			searchContact.setVisibility(View.VISIBLE);
            contactList.setVisibility(View.VISIBLE);
            send.setVisibility(View.VISIBLE);
            noContact.setVisibility(View.GONE);
            while (cur.moveToNext()) {
                String name = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumber = phoneNumber.trim().replace(" ", "-");
                if (!phoneNumber.matches("\\(?\\+?\\d+\\)?[-.]?\\(?\\d+\\)?[-.]?\\(?\\d+\\)?[-.]?\\(?\\d+\\)?"))
                    continue;

                ContactBean bean = new ContactBean();
                bean.setName(name);
                bean.setNumber(phoneNumber);
                bean.setSelected(false);
                contactBeans.add(bean);
            }
            contactAdapter = new ContactAdapater(context,
                    R.layout.contact_selection_row, contactBeans);
            contactList.setAdapter(contactAdapter);
        } else {
            searchContact.setVisibility(View.GONE);
            contactList.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
            dismiss();
            noContact.setVisibility(View.VISIBLE);
            Utils.Toast(context, "No contacts");
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // phoneNumber.setText("");
            if (data != null) {
                if (data.get("isSuccess").equalsIgnoreCase("true")) {
                    Builder alertDialog = new Builder(
                            context);
                    alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Invitation sent to your friend(s)");
                    alertDialog.setPositiveButton("OK",
                            new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    Builder alertDialog = new Builder(
                            context);
                    alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(data.get("errorMsg"));
                    alertDialog.setPositiveButton("OK",
                            new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } else {
                Builder alertDialog = new Builder(
                        context);
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Network Error, please try later");
                alertDialog.setPositiveButton("OK",
                        new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    };
}