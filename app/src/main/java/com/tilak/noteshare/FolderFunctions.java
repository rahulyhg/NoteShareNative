package com.tilak.noteshare;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Jay on 12-01-2016.
 */
public class FolderFunctions {

    //noteshare to noteshare share // email
    public static void noteshareFolderShare(final Context context, final String id) {
        final Dialog shareDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.share_noteshare_email, null, false);
        TextView tvShareTitleAlert = (TextView) contentView.findViewById(R.id.tvEmailShareTitle);
        tvShareTitleAlert.setText("SHARE FOLDER");
        tvShareTitleAlert.setTextColor(Color.WHITE);

        final EditText emailTo = (EditText) contentView.findViewById(R.id.textViewTitleAlertMessage);

        Button buttonShareCancel = (Button) contentView.findViewById(R.id.buttonAlertCancel);
        Button buttonShareOk = (Button) contentView.findViewById(R.id.buttonAlertOk);

        buttonShareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
            }
        });

        buttonShareOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTo.getText().toString().toLowerCase().trim();
                if (email.equals("")) {
                    emailTo.setError("Enter Email id.");
                } else if (!RegularFunctions.isValidEmail(email)) {
                    emailTo.setError("Invalid Email");
                } else {
                    Log.e("jay text", emailTo.getText().toString().toLowerCase());
                    Log.e("jay id", id);
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                    //final String email = emailTo.getText().toString();

                    new AsyncTask<Void, Void, String>() {

                        boolean shared = false;

                        @Override
                        protected String doInBackground(Void... params) {
                            RegularFunctions.syncNow();
                            try
                            {
                                String shareEmailJson = shareJson(id, email).toString();
                                Log.e("jay sharejson", shareEmailJson);

                                String response = RegularFunctions.post(RegularFunctions.SERVER_URL + "share/save", shareEmailJson);
                                Log.e("jay response", response);

                                JSONObject jsonObject = new JSONObject(response);

                                String value = jsonObject.optString("value");
                                if (value.equals("true")) {
                                    shareDialog.dismiss();
                                    progressDialog.dismiss();
                                    shared = true;
                                } else {
                                    shareDialog.dismiss();
                                    progressDialog.dismiss();
                                    shared = false;
                                }
                            }
                            catch(JSONException e)
                            {
                                e.printStackTrace();
                            }
                            catch(IOException io)
                            {
                                io.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if(shared)
                                Toast.makeText(context, "Folder shared successfully!", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(context, "Oops, Something went wrong!", Toast.LENGTH_LONG).show();

                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    }.execute(null,null,null);
                }
            }
        });

        shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shareDialog.setCancelable(false);
        shareDialog.setContentView(contentView);
        shareDialog.setCanceledOnTouchOutside(true);

        shareDialog.show();
    }

    public static JSONObject shareJson(String id, String email){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("userfrom",RegularFunctions.getUserId());
            jsonObject.put("email",email);
            jsonObject.put("folder", RegularFunctions.getServerFolderId(id).trim());
        }catch(JSONException je){
        }
        return jsonObject;
    }
}
