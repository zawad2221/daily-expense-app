package info.devram.dainikhatabook.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmModal extends DialogFragment {

    //private static final String TAG = "ConfirmModal";

    private String mMessage;
    private String mTitle;
    private boolean withOkButton;
    private ConfirmModalListener listener;


    public interface ConfirmModalListener {
        void onOkClick(DialogFragment dialogFragment);
        void onCancelClick(DialogFragment dialogFragment);
    }

    public ConfirmModal(String message, String title, boolean withOkButton, ConfirmModalListener listener) {
        this.mMessage = message;
        this.mTitle = title;
        this.withOkButton = withOkButton;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setMessage(mMessage);
        builder.setTitle(mTitle);

        if (withOkButton) {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onOkClick(ConfirmModal.this);
                }
            });
        }


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCancelClick(ConfirmModal.this);
            }
        });

        return builder.create();
    }


}
