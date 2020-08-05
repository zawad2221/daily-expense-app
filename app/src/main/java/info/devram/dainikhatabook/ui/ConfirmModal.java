package info.devram.dainikhatabook.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import info.devram.dainikhatabook.R;

public class ConfirmModal extends DialogFragment {

    public interface ConfirmModalListener {
        void onOkClick(DialogFragment dialogFragment);
        void onCancelClick(DialogFragment dialogFragment);
    }

    private ConfirmModalListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setMessage("Are You Sure You Want To Delete");
        builder.setTitle("Alert!");

        listener = (ConfirmModalListener) getTargetFragment();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOkClick(ConfirmModal.this);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCancelClick(ConfirmModal.this);
            }
        });

//        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
//
//        View view = layoutInflater.inflate(R.layout.modal,null);
//
//        Button okButton = view.findViewById(R.id.modal_btn_ok);
//        Button cancelButton = view.findViewById(R.id.modal_btn_cancel);
//
//        listener = (ConfirmModalListener) getTargetFragment();
//
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onOkClick(ConfirmModal.this);
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onCancelClick(ConfirmModal.this);
//            }
//        });
//
//        builder.setView(view);

        return builder.create();
    }


}
