package com.sahaware.resysbni.view.custom;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.sahaware.resysbni.R;

public class CustomLoadingDialog extends ProgressDialog {
    String msg = "";
        TextView _txt_message;
    public static ProgressDialog loading(Context context) {
        CustomLoadingDialog dialog = new CustomLoadingDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public CustomLoadingDialog(Context context) {
        super(context);
    }

    public CustomLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
    }

    @Override
    public void setMessage(CharSequence message){
        super.setMessage(message);
    }

    public void setMsg(String message){
        _txt_message.setText(message);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
