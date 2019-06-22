package cn.aysst.www.flowerfish.DialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import cn.aysst.www.flowerfish.R;


/**
 * Created by 蒲公英之流 on 2019-02-27.
 */

public class InputTextDialogFragment extends DialogFragment {

    private String mData;
    private String mMessage;
    public InputTextDialogListener mListener;

    public interface InputTextDialogListener {
        public void onDialogPositiveClick(InputTextDialogFragment dialogFragment);
        public void onDialogNegativeClick(InputTextDialogFragment dialogFragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (InputTextDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_ondotask_fortext,null);
        Bundle bundle = getArguments();
        if (bundle != null){
            mMessage = bundle.getString("message");
        }
        builder.setMessage(mMessage)
                .setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mData = ((TextView)(view.findViewById(R.id.provide_text_on_dialog))).getText().toString();
                        mListener.onDialogPositiveClick(InputTextDialogFragment.this);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick(InputTextDialogFragment.this);
                    }
                });
        return builder.create();
    }

    public String getmData() {
        return mData;
    }
    public void setmData(String mData) {
        this.mData = mData;
    }
}
