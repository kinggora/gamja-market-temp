package com.example.gamjamarket.Setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyItemDialog2 {
    private Dialog dialog;
    private Context context;

    private PostlistItem item;

    public MyItemDialog2 (Context context, PostlistItem item){
        this.context = context;
        this.item = item;
    }

    public void callDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_myitem2);
        setDialog(dialog);


        try {
            WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point deviceSize = new Point();
            display.getSize(deviceSize);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = deviceSize.x;
            params.height = (int)(deviceSize.y * 0.2);
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
        Button changeBtn = ((Dialog) dialog).findViewById(R.id.myitem2_changebtn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnsale(true);
            }
        });

        Button modifyBtn = ((Dialog) dialog).findViewById(R.id.myitem2_modifybtn);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPost();
            }
        });

        Button deleteBtn = ((Dialog) dialog).findViewById(R.id.myitem2_deletebtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder((MyItemActivity)context);
                builder.setTitle("?????? ???????????? ????????? ?????????????????????????");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        deletePost();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                dialog.dismiss();
            }
        });
    }

    public void modifyPost(){

    }

    private void deletePost(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("board1").document(item.getPid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "???????????? ?????????????????????",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void setOnsale(boolean onsale){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("board1").document(item.getPid()).update("onsale", onsale).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "??????????????? ????????? ?????????????????????",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
