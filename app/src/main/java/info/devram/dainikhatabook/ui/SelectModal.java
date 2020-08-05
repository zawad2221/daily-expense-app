package info.devram.dainikhatabook.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.style.AlignmentSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import info.devram.dainikhatabook.R;

public class SelectModal extends DialogFragment {

    private static final String TAG = "SelectModal";

    private final SelectModal.OnSelectListener mCallback;

    public interface OnSelectListener {
        void onItemSelected(String selectedItem);
    }

    public SelectModal(SelectModal.OnSelectListener listener) {
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        final String[] replyValues = getResources()
                .getStringArray(R.array.dashboard_select_values);

        builder.setSingleChoiceItems(R.array.dashboard_select_entries, -1,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCallback.onItemSelected(replyValues[which]);
            }
        });
        return builder.create();
    }
}
