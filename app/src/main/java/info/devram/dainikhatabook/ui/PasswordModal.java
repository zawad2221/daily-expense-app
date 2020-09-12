package info.devram.dainikhatabook.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import info.devram.dainikhatabook.R;

public class PasswordModal extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setMessage("Password Required");
        builder.setTitle("Enter Password");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        LinearLayout.LayoutParams passwordLayout = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        passwordLayout.setMargins(8, 0, 8, 0);
//
//        EditText editText = new EditText(requireActivity());
//        TextInputLayout textInputLayout = new TextInputLayout(requireActivity());
//        textInputLayout.setLayoutParams(passwordLayout);
//        textInputLayout.addView(editText);
//        textInputLayout.setHint("Password");
//        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inflater.inflate(R.layout.set_password_layout, null));


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //listener.onOkClick(ConfirmModal.this);
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //listener.onCancelClick(ConfirmModal.this);
            }
        });


        return builder.create();
    }
}
