package ahscode.test01nd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class MyDialogWithEditOkCancel extends DialogFragment {
	interface WithEditOkCancelListener{

		void onDialogButtonClick(int which, String string);

		void onDialogCanceled(String message);

	}
	private WithEditOkCancelListener mWithEditOkCancelListenerCallback;
	private OnClickListener mDialogButtonClickListener;
	private EditText mEd;

	public MyDialogWithEditOkCancel() {
	}

	public static DialogFragment newInstance() {
		return new MyDialogWithEditOkCancel();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof WithEditOkCancelListener){
			mWithEditOkCancelListenerCallback = (WithEditOkCancelListener)activity;
		}else if((this.getTargetFragment() instanceof WithEditOkCancelListener)&&(this.getTargetFragment() != null)){
			mWithEditOkCancelListenerCallback = (WithEditOkCancelListener)this.getTargetFragment();
		}else{
			mWithEditOkCancelListenerCallback = new WithEditOkCancelListener(){

				@Override
				public void onDialogCanceled(String message) {
					//do nothing
				}

				@Override
				public void onDialogButtonClick(int which, String string) {
				}};
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getArguments().getString("dialog_right"));
		View v = ((LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.layout_dialog_rd, null, false);
		mEd = (EditText) v.findViewById(R.id.mydf_ed00);
		mDialogButtonClickListener = new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String message = null;
				if(mEd.getText().length() == 0){
					message = "ぬるぽ";//ねたです
				}else{
					message = mEd.getText().toString();
				}
				mWithEditOkCancelListenerCallback.onDialogButtonClick(which,message);
				onDismiss(dialog);
			}};
			builder.setView(v);
			builder.setIcon(R.drawable.ic_menu_edit);
			builder.setPositiveButton("ok", mDialogButtonClickListener);
			builder.setNegativeButton("cancel", mDialogButtonClickListener);
			return builder.create();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		//		super.onCancel(dialog);
		String message = null;
		if(mEd.getText().equals(null)){
			message = "ぬるぽ";
		}else{
			message = mEd.getText().toString();
		}
		this.mWithEditOkCancelListenerCallback.onDialogCanceled(message);
	}



}
