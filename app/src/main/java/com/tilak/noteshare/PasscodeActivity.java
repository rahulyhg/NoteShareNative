package com.tilak.noteshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tilak.db.Config;
import com.tilak.db.Note;

public class PasscodeActivity extends DrawerActivity {
    EditText et1,et2,et3,et4;
    AlertDialog confirmPassword=null;
    String passcode="";
    String newpasscode="";
    String confirm_passcode="";
    boolean confirm =false;
    TextView t=null;
    int i=0, j = 0;
    String check, fileId = null;
    int dbPass;
    TextView tv;

    boolean oldConfirm = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        Intent intent = this.getIntent();
        check = intent.getStringExtra("Check");
        fileId = intent.getStringExtra("FileId");

        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        t=(TextView) findViewById(R.id.textView);

        Config con = Config.findById(Config.class, 1L);
        dbPass = con.passcode;
        if(check.equals("1") || check.equals("2") || check.equals("3")){
            t.setText("Confirm Passcode");
        }
        if (check.equals("4")) {
            t.setText("Old Passcode");
        }

    }
    public void keyPressed(View v){
        tv = (TextView) v;
        i++;
        if(check.equals("1")){
            setNotePassTrue();
        }else if(check.equals("4")){
            if (i <= 4 && oldConfirm == false) {
                passcode += tv.getText().toString();
                if(passcode.length() == 4) {
                    et4.setText("*");
                    if (Integer.parseInt(passcode) == dbPass) {
                        clearBox();
                        t.setText("Set New Passcode");
                        oldConfirm = true;
                    } else {
                        shake();
                        clearBox();
                        //Toast.makeText(PasscodeActivity.this, "Invalid Old Passcode", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    lessThanFour(i);
                }
            }
            else {
                if (i <= 4) {
                    newPassCode();
                }
            }

        } else if(check.equals("2")) {
            passcode += tv.getText().toString();
            if (passcode.length() == 4) {
                et4.setText("*");
                if (Integer.parseInt(passcode) == dbPass) {
                    finish();
                    Intent i = new Intent(PasscodeActivity.this, NoteMainActivity.class);
                    i.putExtra("NoteId", fileId);
                    startActivity(i);
                } else {
                    shake();
                    clearBox();
                    t.setText("Enter Valid Passcode to Open file.");
                    //Toast.makeText(PasscodeActivity.this, "Invalid Passcode", Toast.LENGTH_LONG).show();
                }
            }
            else{
                lessThanFour(i);
            }
        } else if(check.equals("3")){
            passcode += tv.getText().toString();
            if(passcode.length() == 4) {
                et4.setText("*");
                if (Integer.parseInt(passcode) == dbPass) {
                    Note n = Note.findById(Note.class, Long.parseLong(fileId));
                    n.islocked = 0;
                    n.save();
                    finish();
                } else {
                    shake();
                    clearBox();
                    t.setText("Enter Valid Passcode to remove passcode.");
                    //Toast.makeText(PasscodeActivity.this, "Invalid Passcode", Toast.LENGTH_LONG).show();
                }
            }
            else{
                lessThanFour(i);
            }
        } else if(check.equals("5")) {
            passcode += tv.getText().toString();
            if (passcode.length() == 4) {
                et4.setText("*");
                t.setText("Confirm New Passcode");
                if (confirm == false) {
                    confirm_passcode = passcode;
                    clearBox();
                    confirm = true;
                } else {
                    if (passcode.equals(confirm_passcode)) {
                        Config c = Config.findById(Config.class, Long.valueOf(1));
                        c.setPasscode(Integer.parseInt(confirm_passcode));
                        c.save();
                        Toast.makeText(PasscodeActivity.this, "New Passcode Saved", Toast.LENGTH_LONG).show();
                        Note n = Note.findById(Note.class, Long.parseLong(fileId));
                        n.islocked = 1;
                        n.save();
                        finish();
                        //finish();
                    } else {
                        Toast.makeText(PasscodeActivity.this, "Invalid Passcode", Toast.LENGTH_LONG).show();
                        confirm = true;
                        shake();
                        clearBox();
                    }
                }
                //Check for passcode
            } else {
                lessThanFour(i);
            }
        }else {
            if (i <= 4) {
                newPassCode();
            }
        }
    }
    public void clearPressed(View v){
        finish();
    }
    public void backPressed(View v){
        clearBox();
    }

    public void lessThanFour(int i){
        if (i == 1) {
            et1.setText("*");
        } else if (i == 2) {
            et2.setText("*");
        } else if (i == 3) {
            et3.setText("*");
        }
    }

    public void clearBox(){
        passcode="";
        i=0;
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
    }

    public void newPassCode(){
        passcode += tv.getText().toString();
        if (passcode.length() == 4) {
            et4.setText("*");
            t.setText("Confirm New Passcode");
            if (confirm == false) {
                confirm_passcode = passcode;
                clearBox();
                confirm = true;
            } else {
                if (passcode.equals(confirm_passcode)) {
                    Config c = Config.findById(Config.class, Long.valueOf(1));
                    c.setPasscode(Integer.parseInt(confirm_passcode));
                    c.save();
                    Toast.makeText(PasscodeActivity.this, "New Passcode Saved", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(PasscodeActivity.this, "Invalid Passcode", Toast.LENGTH_LONG).show();
                    confirm = true;
                    shake();
                    clearBox();
                }
            }
            //Check for passcode
        } else {
            lessThanFour(i);
        }
    }

    public void setNotePassTrue(){
        passcode += tv.getText().toString();
        if(passcode.length() == 4) {
            et4.setText("*");
            if (Integer.parseInt(passcode) == dbPass) {
                Note n = Note.findById(Note.class, Long.parseLong(fileId));
                n.islocked = 1;
                n.save();
                finish();
            } else {
                shake();
                clearBox();
                t.setText("Enter Valid Passcode to Open file.");
                Toast.makeText(PasscodeActivity.this, "Invalid Passcode", Toast.LENGTH_LONG).show();
            }
        }
        else{
            lessThanFour(i);
        }
    }

    public void shake(){
        Vibrator vb = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(100);
        Animation shake = AnimationUtils.loadAnimation(PasscodeActivity.this, R.anim.shake);
        et1.startAnimation(shake);
        et2.startAnimation(shake);
        et3.startAnimation(shake);
        et4.startAnimation(shake);
    }
}
