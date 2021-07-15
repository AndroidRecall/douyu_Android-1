package com.swbg.mlivestreaming.view.popupwindow;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.swbg.mlivestreaming.R;

import java.util.Objects;


public class MessageAlarmDialog extends DialogFragment {

    private DialogInterface.OnClickListener onClickListener;

    public static MessageAlarmDialog newInstance(String title, String message, String positive,
                                                 String negative) {
        MessageAlarmDialog dialog = new MessageAlarmDialog();
        Bundle arguments = new Bundle();
        arguments.putString("title", title);
        arguments.putString("message", message);
        arguments.putString("positive", positive);
        arguments.putString("negative", negative);
        dialog.setArguments(arguments);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String title = Objects.requireNonNull(arguments).getString("title");
        String message = arguments.getString("message");
        String positive = arguments.getString("positive");
        String negative = arguments.getString("negative");
        return createDialog(getActivity(), title, message, positive, negative, onClickListener);
    }

    public MessageAlarmDialog setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public AlertDialog createDialog(Context context, String title, String message, String positive,
                                    String negative, final Dialog.OnClickListener onClickListener) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_alarm, null);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtMessage = view.findViewById(R.id.txt_message);
        TextView txtPositive = view.findViewById(R.id.txt_positive);
        if (TextUtils.isEmpty(title)) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setText(title);
        }
        if (TextUtils.isEmpty(message)) {
            txtMessage.setVisibility(View.GONE);
        } else {
            txtMessage.setText(message);
        }
        if (!TextUtils.isEmpty(positive)) {
            txtPositive.setText(positive);
        } else {
            txtPositive.setVisibility(View.GONE);
        }
        txtPositive.setOnClickListener(v -> {
            dismiss();
            if (onClickListener != null)
                onClickListener.onClick(getDialog(), Dialog.BUTTON_POSITIVE);
        });
        AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.Dialog_Global).setView
                (view).create();
        alertDialog.setOnShowListener(dialog -> {
            if (dialog instanceof AlertDialog) {
                Window window = Objects.requireNonNull(((AlertDialog) dialog).getWindow());
                WindowManager.LayoutParams attributes = window.getAttributes();
                Point p = new Point();
                window.getWindowManager().getDefaultDisplay().getSize(p);
                attributes.width = (int) (p.x * 0.85f);
                window.setAttributes(attributes);
            }
        });
        return alertDialog;
    }


}
