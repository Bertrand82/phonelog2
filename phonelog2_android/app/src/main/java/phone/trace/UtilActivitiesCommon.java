package phone.trace;

import java.io.Serializable;
import java.util.regex.Pattern;

import phone.trace.model.Contact;
import phone.trace.model.PhoneCall;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import phone.trace.R;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.beardedhen.androidbootstrap.font.IconSet;
import com.beardedhen.androidbootstrap.font.MaterialIcons;
import com.beardedhen.androidbootstrap.font.Typicon;

public class UtilActivitiesCommon {
    static {
        TypefaceProvider.registerDefaultIconSets();
        logIconSet_();
    }
    final static boolean editMode = true;
    final static IconSet fontAwesome = TypefaceProvider.retrieveRegisteredIconSet(FontAwesome.FONT_PATH, editMode);
    final static IconSet typicon = TypefaceProvider.retrieveRegisteredIconSet(Typicon.FONT_PATH, editMode);
    final static IconSet materialIcons = TypefaceProvider.retrieveRegisteredIconSet(MaterialIcons.FONT_PATH, editMode);

    private static String TAG = "UtilActivitiesCommon";

    // TODO
    // public static void openActivityPreferences(Activity activity) {
    // Intent intent = new Intent(activity, ActivityPreference3.class);
    // activity.startActivity(intent);
    // }

    public static void openActivityPrivateList(Activity activity) {
        Intent intent = new Intent(activity, ActivityDisplayPrivateList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }

    public static void openCommentLastCall(Activity activity) {
        Intent intent = new Intent(activity, ActivityComment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }

    private static void showHome_(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void openLogs(Activity activity) {
        Intent intent = new Intent(activity, ActivityLogs2.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// Ne met pas
        // a jour la liste des Calendar dans spinner
        activity.startActivity(intent);
    }

    public static void openAddEvent(Activity activity) {
        Intent intent = new Intent(activity, ActivityAddEvent.class);
        activity.startActivity(intent);
    }

    public static void openSearchContact(Activity activity) {
        Intent intent = new Intent(activity, ActivitySearchContact.class);
        activity.startActivity(intent);
    }

    public static void openCalendars(Activity activity) {
        long startMillis = System.currentTimeMillis() - 5l * 24l * 60l * 60l * 1000l;

        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);

        Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());

        activity.startActivity(intent);
    }

    public static void openListCalendars(Activity activity) {
        Intent intent = new Intent(activity, ActivityListCalendars2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        activity.startActivity(intent);
    }

    private static void openSettings_(Activity activity) {
        Intent intent = new Intent(activity, ActivitySetting.class);
        activity.startActivity(intent);
    }

    public static void openSettingsEmail_(Activity activity) {
        Intent intent = new Intent(activity, ActivitySettingEmailSMTP.class);
        activity.startActivity(intent);
    }

    private static void openImportContacts(Activity activity) {
        Intent intent = new Intent(activity, ActivityImportContacts.class);
        activity.startActivity(intent);
    }

    private static void openImportEventsCRM(Activity activity) {
        // popUp(activity, "Inport Events", "No Implemented Yet");
        Intent intent = new Intent(activity, ActivityImportEventsCRM.class);
        activity.startActivity(intent);
    }

    private static void openAbout(Activity activity) {
        // popUp(activity, "Inport Events", "No Implemented Yet");
        Intent intent = new Intent(activity, ActivityAbout.class);
        activity.startActivity(intent);
    }

    private static void openTraceDebug(Activity activity) {
        ApplicationBg applicationBg = (ApplicationBg) activity.getApplication();
        popUp(activity, "Trace Debug", applicationBg.getTracesDebug());

    }

    public static void openMain(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void openPrivateList(Activity activity) {
        Intent intent = new Intent(activity, ActivityDisplayPrivateList.class);
        activity.startActivity(intent);
    }

    public static boolean onOptionsItemSelected(MenuItem item, Activity activity) {
        Log.i("bg2", "onOptionsItemSelected ");
        if (item.getItemId() == R.id.action_display_private_list) {
            UtilActivitiesCommon.openActivityPrivateList(activity);
            return true;
        } else if (item.getItemId() == R.id.action_CommentLastCall) {
            UtilActivitiesCommon.openCommentLastCall(activity);
            return true;
        } else if (item.getItemId() == R.id.action_display_logs) {
            UtilActivitiesCommon.openLogs(activity);
            return true;
        } else if (item.getItemId() == R.id.action_display_calendars) {
            UtilActivitiesCommon.openCalendars(activity);
            return true;
        } else if (item.getItemId() == R.id.action_list_calendar) {
            UtilActivitiesCommon.openListCalendars(activity);
            return true;
        } else if (item.getItemId() == R.id.action_setting_preferences) {
            UtilActivitiesCommon.openSettings_(activity);
            return true;
        } else if (item.getItemId() == R.id.action_setting_preferences_mail_smtp) {
            UtilActivitiesCommon.openSettingsEmail_(activity);
            return true;
        } else if (item.getItemId() == R.id.action_import_contact) {
            UtilActivitiesCommon.openImportContacts(activity);
            return true;
        } else if (item.getItemId() == R.id.action_import_event_crm) {
            UtilActivitiesCommon.openImportEventsCRM(activity);
            return true;
        } else if (item.getItemId() == R.id.action_about) {
            UtilActivitiesCommon.openAbout(activity);
            return true;

        } else if (item.getItemId() == R.id.action_trace_debug) {
            UtilActivitiesCommon.openTraceDebug(activity);
            return true;

        } else if (item.getItemId() == R.id.action_add_event) {
            UtilActivitiesCommon.openAddEvent(activity);
            return true;

        } else if (item.getItemId() == R.id.action_search_contact) {
            UtilActivitiesCommon.openSearchContact(activity);
            return true;

        } else if (item.getItemId() == R.id.action_send_cafe_crm_link_by_sms) {
            UtilActivitiesCommon.sendSms("http://cafe-crm.appspot.com");
            return true;
        } else {
            Log.i("bg2", "onOptionsItemSelected  false");
            return false;
        }

    }

    private static void sendSms(String msg) {
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(null, null, msg, null, null);
    }

    private static void sendEmail( Activity activity){
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:"+
                "?subject=" + Uri.encode("the subject") +
                "&body=" + Uri.encode("the body of the message");
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        activity.startActivity(Intent.createChooser(send, "Send mail..."));
    }

    public static void initButtonNavigation_(final Activity activity, int n) {

        Button buttonLogs = (Button) activity.findViewById(R.id.buttonLogs);
        Button buttonPrivateList = (Button) activity.findViewById(R.id.buttonPrivateList);
        // Button buttonOptions = (Button)
        // activity.findViewById(R.id.buttonOptions);
        Button buttonLastCall = (Button) activity.findViewById(R.id.buttonLastCall);
        Button buttonAndroidPhoneSystem = (Button) activity.findViewById(R.id.buttonAndroidPhoneSystem);

        if (n == 1) {
            select(buttonLogs);
        } else if (n == 2) {
            select(buttonPrivateList);
        } else if (n == 3) {
            // select(buttonOptions);
        } else if (n == 4) {
            select(buttonLastCall);
        }
        initButtonNavigation(activity, buttonLogs, buttonPrivateList, null, buttonLastCall, buttonAndroidPhoneSystem);
    }

    private static void select(Button button) {
        button.setTextColor(Color.RED);
        button.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private static void initButtonNavigation(final Activity activity, Button buttonLogs, Button buttonPrivateList, Button buttonOptions, Button buttonLastCall, Button buttonAndroidPhoneSystem) {
        if (buttonLogs == null) {
            Log.i(TAG, "initButtonNavigation buttonLogs is null !!!!");
        } else {
            Log.i(TAG, "initButtonNavigation buttonLogs " + buttonLogs);
            buttonLogs.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "initButtonNavigation buttonLogs onClick");
                    UtilActivitiesCommon.openLogs(activity);
                    ;
                }
            });
        }
        if (buttonOptions != null) {
            buttonOptions.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO
                    // UtilActivitiesCommon.openPreference(activity);
                }
            });
        }
        if (buttonPrivateList != null) {
            buttonPrivateList.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UtilActivitiesCommon.openPrivateList(activity);
                }
            });
        }
        if (buttonLastCall != null) {
            buttonLastCall.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UtilActivitiesCommon.openCommentLastCall(activity);
                }
            });
        }
        if (buttonAndroidPhoneSystem != null) {
            buttonAndroidPhoneSystem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UtilActivitiesCommon.openAndroidPhoneSystem(activity);
                }
            });
        }

    }

    public static void callNumber(Activity activity, String number) {
        Log.i(TAG, "Call Number");
        String url = "tel:" + number;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        activity.startActivity(intent);
    }

    private static void openAndroidPhoneSystem(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        // intent.setData(Uri.parse("tel:1231231234"));
        activity.startActivity(intent);
    }

    public static void setImage(int type, AwesomeTextView faText) {
        Log.i("bg2"," setImage:  "+type+" | faText:"+faText);
        Log.i("bg2"," setImage:     fontAwesome :"+fontAwesome);
        logIconSet_();
        if (faText == null) {
            Log.e("bg2","setImage Oups! faText is null  Should never happen");
        }else if (type == PhoneCall.TYPE_INCOMING_CALL) {// 1
            faText.setIcon("fa-level-down",fontAwesome);
            faText.setTextColor(Color.parseColor("#ff428bca"));
        } else if (type == PhoneCall.TYPE_OUTGOING_CALL) {// 2
            faText.setIcon("fa-level-up",fontAwesome);
            faText.setTextColor(Color.parseColor("#ff5cb85c"));
        } else if (type == PhoneCall.TYPE_MISSED_CALL) {// 3
            faText.setIcon("fa-level-down",fontAwesome);
            faText.setTextColor(Color.parseColor("#ffd9534f"));
        } else if (type == PhoneCall.TYPE_INCOMING_SMS) {// 3
            faText.setIcon("fa-level-down",null);
            faText.setTextColor(Color.parseColor("#ff428bca"));
        } else if (type == PhoneCall.TYPE_OUTGOING_SMS) {// 3
            faText.setIcon("fa-level-up",fontAwesome);
            faText.setTextColor(Color.parseColor("#ff5cb85c"));
        } else {
           faText.setIcon("",fontAwesome);
        }
    }

    static void logIconSet_ (){
       Log.i("bg2", "IconSet2 logIconSet TypefaceProvider.getRegisteredIconSets().size "+TypefaceProvider.getRegisteredIconSets().size());
       for(IconSet iconSet : TypefaceProvider.getRegisteredIconSets()){
           Log.i("bg2", "logIconSet "+iconSet);
       }
    }



    public static void setImagePhoneOuMessage(int type,AwesomeTextView faText) {
        if (fontAwesome == null){
            Log.w("bg2"," setImagePhoneOuMessage fontAwesome null"+fontAwesome);
            logIconSet_();
        }else if (type == PhoneCall.TYPE_INCOMING_CALL) {// 1
            faText.setIcon("fa-phone",fontAwesome);
        } else if (type == PhoneCall.TYPE_OUTGOING_CALL) {// 2
            faText.setIcon("fa-phone",fontAwesome);
        } else if (type == PhoneCall.TYPE_MISSED_CALL) {// 3
            faText.setIcon("fa-phone",fontAwesome);
        } else if (type == PhoneCall.TYPE_INCOMING_SMS) {// 3
            faText.setIcon("fa-envelope",fontAwesome);
        } else if (type == PhoneCall.TYPE_OUTGOING_SMS) {// 3
            faText.setIcon("fa-envelope",fontAwesome);
        } else {
            faText.setIcon("fa-phone",fontAwesome);
        }
    }

    public static void popUp(Context context, String title, String message) {
        Log.i("bg2", "Pop up");
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.show();
    }

    public static void popUp(Context context, String title, String message, String labelButton) {
        popUp(context, title, message, labelButton, null);
    }

    public static void popUp(final Context context, String title, String message, String labelButton_2, final Class<?> clazz) {
        popUp(context, title, message, null, labelButton_2, clazz);
    }

    public static void popUp(final Context context, String title, String message, String labelButton_1, String labelButton_2, final Class<?> clazz) {
        PopUpListener listener = new PopUpListener() {
            @Override
            public void postProcess() {
                if (clazz != null) {
                    Intent intent = new Intent(context, clazz);
                    context.startActivity(intent);
                }
            }
        };
        popUp(context, title, message, labelButton_1, labelButton_2, listener);
    }

    public static void popUp(final Context context, String title, String message, String labelButton_1, String labelButton_2, final PopUpListener listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        DialogInterface.OnClickListener listener_1 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, close
                // current activity
                dialog.cancel();

            }
        };
        DialogInterface.OnClickListener listener_2 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, close
                // current activity
                dialog.cancel();
                if (listener != null) {
                    listener.postProcess();
                }

            }
        };
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton(labelButton_1, listener_1);
        if (labelButton_2 != null) {
            alertDialogBuilder.setPositiveButton(labelButton_2, listener_2);
        }

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public interface PopUpListener {
        void postProcess();
    }

    public static String getPossiblePrimaryEmailAdress(Context context) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                return possibleEmail;
            }
        }
        return "";
    }

    public static void displayActivityLogDetatil(Context context, Contact contact, Serializable storage, boolean newActivity) {
        displayActivity(context, contact, storage, newActivity, ActivityLogDetail.class);
    }

    private static void displayActivity(Context context, Contact contact, Serializable storage, boolean newActivity, @SuppressWarnings("rawtypes") Class clazz) {

        Log.i("bg2", "ActivityLogs___OLD.displayActivityLogDetail " + contact + "  Storage:" + storage + "   class: " + clazz);

        Intent intent = new Intent(context, clazz);
        Bundle b = new Bundle();
        b.putSerializable("contact", contact);
        b.putSerializable("storage", storage);
        Log.i(TAG, "Start ----  " + b.getSerializable("contact"));
        intent.putExtras(b);
        if (newActivity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

        // context.overridePendingTransition(R.anim.slide_in_right,
        // R.anim.slide_out_right);
    }
}