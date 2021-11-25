 package com.example.gamjamarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gamjamarket.Home1.PostviewActivity;
import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.Model.WriteinfoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.Date;

public class WritingActivity extends AppCompatActivity {
    private static final String TAG = "WritingActivity";
    private FirebaseUser user;

    private String title;
    private String explain;
    private String contentsList;
    private Button btnUpload;
    private ImageView addImage;
    private Uri imageUri;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private int imageCount = 0;
    private static final int PICK_FROM_ALBUM = 10;


    private Button categoryBtn;
    private ArrayList<String> categoryNameList = new ArrayList<String>();
    private int categoryIdx = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        user = FirebaseAuth.getInstance().getCurrentUser();

        categoryBtn = (Button)findViewById(R.id.categoryButton);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListDialog();
            }
        });

        parent = findViewById(R.id.addphoto_imagelist);

        btnUpload = (Button)findViewById(R.id.addphoto_btn_upload);
        addImage = (ImageView)findViewById(R.id.addphoto_imageview);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = ((EditText) findViewById(R.id.addphoto_edit_title)).getText().toString();
                explain = ((EditText) findViewById(R.id.addphoto_edit_explain)).getText().toString();

                if (title.length() > 0 && categoryIdx != 999) {
                    //ArrayList<String> contentsList = new ArrayList<>();
                    FirebaseStorage.getInstance().getReference().child("images").child(user.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                            while(!imageUrl.isComplete());
                            //contentsList.add(imageUrl.getResult().toString());
                            contentsList = imageUrl.getResult().toString();
                            WriteinfoModel writeinfoModel = new WriteinfoModel(title, categoryNameList.get(categoryIdx), explain, contentsList, user.getUid(), new Date());
                            storeUploader(writeinfoModel);
                        }
                    });

                    /*FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();

                    for (int i=0; i<parent.getChildCount(); i++){
                        View view = parent.getChildAt(i);
                        if (view instanceof EditText) {
                            contentsList.add(pathList.get(i));
                            final StorageReference mountainImageRef = storageReference.child("users/" + user.getUid() + "/" + i + ".jpg");

                            try {
                                InputStream stream = new FileInputStream(new File(pathList.get(i)));
                                StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                                UploadTask uploadTask = mountainImageRef.putStream(stream, metadata);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                        mountainImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                            @Override
                                            public void onSuccess(Uri uri) {
                                                contentsList.set(index, uri.toString());
                                                imageCount++;
                                                if (pathList.size() == imageCount) {
                                                    WriteinfoModel writeinfoModel = new WriteinfoModel(title, explain, contentsList, user.getUid(), new Date());
                                                    storeUploader(writeinfoModel);
                                                }
                                            }
                                        });
                                    }
                                });
                            } catch (FileNotFoundException e) {
                                Log.e("로그", "에러: " + e.toString());
                            }
                        }
                    }*/
                }else {
                    Toast.makeText(WritingActivity.this, "글 작성을 완료해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            addImage.setImageURI(data.getData());
            imageUri = data.getData();
        }

    }

    private void storeUploader(WriteinfoModel writeinfoModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDoc = db.collection("users").document(user.getUid());
        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String dongcode = documentSnapshot.getString("dongcode");
                String dongname = documentSnapshot.getString("dongname");
                writeinfoModel.setDongcode(dongcode);
                writeinfoModel.setDongname(dongname);

                db.collection("board1").add(writeinfoModel)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: "+ documentReference.getId());
                        Toast.makeText(WritingActivity.this, "글 작성 완료", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("writeinfoModel", writeinfoModel);
                        //Intent PostviewActivity = new Intent(WritingActivity.this, PostviewActivity.class);
                        //PostviewActivity.putExtras(bundle);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error adding document", e);
                                Toast.makeText(WritingActivity.this, "글 작성 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WritingActivity.this, "유저 데이터 불러오기 실패", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    public void categoryListInitialization(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories").orderBy("index")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            categoryNameList.add(document.getString("name"));
                        }
                        createListDialog();
                    }
                    else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                        System.out.println("Error getting documents");

                    }
                }
            });
    }

    public void createListDialog(){
        if(categoryNameList.isEmpty()){
            categoryListInitialization();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(WritingActivity.this);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryNameList);
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    categoryIdx = which;
                    categoryBtn.setText(categoryNameList.get(categoryIdx));
                    categoryBtn.invalidate();
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }




}