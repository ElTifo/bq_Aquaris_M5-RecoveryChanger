package com.eltifo.recoverychanger;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final ExecuteAsRootBase RootUser = new ExecuteAsRootBase();

    private final int RECTYPE_STK = 1;
    private final int RECTYPE_TWRP = 2;

    private boolean checkWriteExternalPermission() {

        String permission_write = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = this.checkCallingOrSelfPermission(permission_write);
        return (res == PackageManager.PERMISSION_GRANTED);

    }


    private String getRecoveryName(int paramInt)
    {
        String str = "";

        if (paramInt == 1) {
            str = getString(R.string.stock);
        }
        if (paramInt == 2) {
            str = getString(R.string.twrp);
        }

        return str;
    }

    private void disableButtons(){
        Button btn1 = (Button) findViewById(R.id.button);
        btn1.setEnabled(false);
        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setEnabled(false);
        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setEnabled(false);
    }

    private void alertClose(String paramString1, String paramString2)
    {
        AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
        localAlertDialog.setTitle(paramString1);
        localAlertDialog.setMessage(paramString2);
        localAlertDialog.setButton(-1, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
            }
        });
        localAlertDialog.setButton(-2, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        localAlertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView rootLabel = (TextView)findViewById(R.id.textView5);
        if (RootUser.canRunRootCommands()) {
            rootLabel.setTextColor(Color.parseColor("#aeea00"));
            rootLabel.append(getText(R.string.root_OK));
        }else{
            rootLabel.setTextColor(Color.parseColor("#ef5350"));
            rootLabel.append(getText(R.string.root_not_OK));
            disableButtons();
            alertClose(getString(R.string.root_not_OK_info1), getString(R.string.root_not_OK_info2));
        }
    }

    public void buttonOnClickStock(View v)
    {
        if(checkWriteExternalPermission()){
            String paramString1 = getString(R.string.stock);
            String paramString2 = getString(R.string.stock_confirm);
            AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
            localAlertDialog.setTitle(paramString1);
            localAlertDialog.setMessage(paramString2);
            localAlertDialog.setButton(-1, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        ProgressDialog progress = new ProgressDialog(new ContextThemeWrapper(com.eltifo.recoverychanger.MainActivity.this, R.style.dialog));
                        progress.setIndeterminateDrawable(new CircularProgressDrawable
                                .Builder(com.eltifo.recoverychanger.MainActivity.this)
                                .color(R.color.md_falcon_700)
                                .style(CircularProgressDrawable.STYLE_NORMAL)
                                .build());
                        progress.setMessage(getText(R.string.Installing) + getRecoveryName(RECTYPE_STK));
                        new copyRawToSD(progress, com.eltifo.recoverychanger.MainActivity.this).execute(RECTYPE_STK);
                }
            });
            localAlertDialog.setButton(-2, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    paramAnonymousDialogInterface.dismiss();
                }
            });
            localAlertDialog.show();
        } else {
            //ask for user permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void buttonOnClickTWRP(View v)
    {
        if(checkWriteExternalPermission()){
            String paramString1 = getString(R.string.twrp);
            String paramString2 = getString(R.string.twrp_confirm);
            AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
            localAlertDialog.setTitle(paramString1);
            localAlertDialog.setMessage(paramString2);
            localAlertDialog.setButton(-1, getString(android.R.string.ok), new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                {

                        ProgressDialog progress = new ProgressDialog(new ContextThemeWrapper(com.eltifo.recoverychanger.MainActivity.this, R.style.dialog));
                        progress.setIndeterminateDrawable(new CircularProgressDrawable
                                .Builder(com.eltifo.recoverychanger.MainActivity.this)
                                .color(R.color.md_falcon_700)
                                .style(CircularProgressDrawable.STYLE_NORMAL)
                                .build());
                        progress.setMessage(getText(R.string.Installing) + getRecoveryName(RECTYPE_TWRP));
                        new copyRawToSD(progress, com.eltifo.recoverychanger.MainActivity.this).execute(RECTYPE_TWRP);
                }
            });
            localAlertDialog.setButton(-2, getString(android.R.string.cancel), new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                {
                    paramAnonymousDialogInterface.dismiss();
                }
            });
            localAlertDialog.show();
        } else{
            //ask for user permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }


    private void showRecoveryMSG(){
        String paramString1 = getString(R.string.recovery);
        String paramString2 = getString(R.string.recovery_confirm);
        AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
        localAlertDialog.setTitle(paramString1);
        localAlertDialog.setMessage(paramString2);
        localAlertDialog.setButton(-1, getString(android.R.string.ok), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                ArrayList localArrayList = new ArrayList();
                localArrayList.add("reboot recovery");
                localArrayList.add("busybox reboot recovery");
                localArrayList.add("toolbox reboot recovery");
                RootUser.execute(localArrayList);
            }
        });
        localAlertDialog.setButton(-2, getString(android.R.string.cancel), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                paramAnonymousDialogInterface.dismiss();
            }
        });
        localAlertDialog.show();
    }

    public void buttonOnClickReboot(View v)
    {
        showRecoveryMSG();
    }

}