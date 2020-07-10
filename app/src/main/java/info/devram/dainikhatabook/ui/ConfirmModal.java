package info.devram.dainikhatabook.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import info.devram.dainikhatabook.R;

public class ConfirmModal extends DialogFragment {

    public interface ConfirmModalListener {
        void onOkClick(DialogFragment dialogFragment);
        void onCancelClick(DialogFragment dialogFragment);
    }

    private AlertDialog.Builder builder;
    private ConfirmModalListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.modal,null);

        Button okButton = view.findViewById(R.id.modal_btn_ok);
        Button cancelButton = view.findViewById(R.id.modal_btn_cancel);

        listener = (ConfirmModalListener) getTargetFragment();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOkClick(ConfirmModal.this);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelClick(ConfirmModal.this);
            }
        });

        builder.setView(view);

        return builder.create();
    }


}
