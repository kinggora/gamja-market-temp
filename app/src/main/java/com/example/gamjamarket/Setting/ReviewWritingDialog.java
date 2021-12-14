package com.example.gamjamarket.Setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gamjamarket.Model.ChatModel;
import com.example.gamjamarket.Model.ReviewModel;
import com.example.gamjamarket.Model.UserModel;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReviewWritingDialog {
    private static final String TAG = "ReviewWritingDialog";
    private Dialog dialog;
    private Context context;
    private UserModel userModel;

    public ReviewWritingDialog(Context context, UserModel model){
        this.context = context;
        this.userModel = model;
    }

    public void callDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_writingreview);
        setDialog(dialog);

        try {
            WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point deviceSize = new Point();
            display.getSize(deviceSize);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = deviceSize.x;
            params.height = (int)(deviceSize.y * 0.8);
            params.horizontalMargin = 0.0f;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();
    }

    public void setDialog(Dialog dialog){
        ProfileImg profileImg = new ProfileImg();
        ImageView image = ((Dialog) dialog).findViewById(R.id.writereview_userimage);
        image.setImageResource(profileImg.getSrc(userModel.getProfileImageUrl()));
        TextView nickname = ((Dialog) dialog).findViewById(R.id.writereview_usernickname);
        nickname.setText(userModel.getUsernickname());

        RatingBar ratingBar = ((Dialog) dialog).findViewById(R.id.writereview_ratingBar);
        ratingBar.setStepSize(1);
        ratingBar.setIsIndicator(false);
        EditText editExplain = ((Dialog) dialog).findViewById(R.id.writereview_explain);
        Button writeBtn = ((Dialog) dialog).findViewById(R.id.writereview_btn);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String explain = editExplain.getText().toString();
                if(explain.length() > 10){
                    if(ratingBar.getRating() != 0){
                        ReviewModel reviewModel = new ReviewModel();
                        reviewModel.setProfileimg(userModel.getProfileImageUrl());
                        reviewModel.setNickname(userModel.getUsernickname());
                        reviewModel.setExplain(explain);
                        reviewModel.setRating(ratingBar.getRating());
                        writeReview(reviewModel);
                    }
                    else{
                        Toast.makeText(context, "거래 만족도를 표시해주세요",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "거래 후기를 10자이상 작성해주세요",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void writeReview(ReviewModel reviewModel){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userModel.getUid())
                .collection("review").add(reviewModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Dialog successDialog = new Dialog(context);
                successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                successDialog.setContentView(R.layout.popup_wrotereview);
                successDialog.setCancelable(false);
                Button btn = successDialog.findViewById(R.id.wrotereview_btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        successDialog.dismiss();
                        dialog.dismiss();
                    }
                });
                successDialog.show();

            }
        });

    }
}
