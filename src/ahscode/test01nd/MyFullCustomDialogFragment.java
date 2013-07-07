package ahscode.test01nd;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

/**
 * このダイアログフラグメントは
 * http://dev.classmethod.jp/smartphone/android/android-tips-45-custom-dialog/
 * を参考にして作りました。この場を借りてお礼申し上げます。
 * */
public class MyFullCustomDialogFragment extends DialogFragment {

	interface EventPassFromCustomDialogListener{

		void clickPositive();

		void clickClose();
		
	}

	private EventPassFromCustomDialogListener mEventPassFromCustomDialogListenerCallback;
	public MyFullCustomDialogFragment() {
		//system use
	}

	public static DialogFragment newInstance() {
		return new MyFullCustomDialogFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof EventPassFromCustomDialogListener){
			mEventPassFromCustomDialogListenerCallback = (EventPassFromCustomDialogListener)activity;
		}else if((this.getTargetFragment() instanceof EventPassFromCustomDialogListener)&&(this.getTargetFragment() != null)){
			mEventPassFromCustomDialogListenerCallback = (EventPassFromCustomDialogListener)this.getTargetFragment();
		}else{
			mEventPassFromCustomDialogListenerCallback = new EventPassFromCustomDialogListener(){

				@Override
				public void clickPositive() {
					//do nothing
				}

				@Override
				public void clickClose() {
					//do nothing
				}
				
			};
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        // タイトル非表示
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // フルスクリーン
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_custom);
        // 背景を透明にする
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         
        // OK ボタンのリスナ
        dialog.findViewById(R.id.positive_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mEventPassFromCustomDialogListenerCallback.clickPositive();
            }
        });
        // Close ボタンのリスナ
        dialog.findViewById(R.id.close_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mEventPassFromCustomDialogListenerCallback.clickClose();
            }
        });
         dialog.setCanceledOnTouchOutside(false);//dialog表示領域外タッチ制御
        return dialog;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		mEventPassFromCustomDialogListenerCallback.clickClose();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

}
