package com.example.gamjamarket.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gamjamarket.MainActivity;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DongRegisterActivity extends Activity {
    private FirebaseAuth mAuth;

    private EditText editDong;
    private Button btnSearch;
    private ListView donglistView;

    private AdresSpceAPI mAdresApceAPI = new AdresSpceAPI();;
    private ArrayAdapter adapter;
    private ArrayList<String> items = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongne);
        mAuth = FirebaseAuth.getInstance();

        editDong = (EditText)findViewById(R.id.dongInput);
        btnSearch = (Button)findViewById(R.id.btn_searchDong);
        donglistView = (ListView)findViewById(R.id.dongList);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        donglistView.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editDong.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editDong.getWindowToken(), 0);
                
                if(input.length() != 0){
                    items.clear();
                    items.addAll(mAdresApceAPI.search(input));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                else{
                    Toast.makeText(DongRegisterActivity.this, "검색어를 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });

        donglistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String data = (String)parent.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(DongRegisterActivity.this);

                builder.setTitle("동네 선택").setMessage("'"+data+"' "+"으로 지정하시겠습니까?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        settingDB(position);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(DongRegisterActivity.this, "Cancel Click", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    public void settingDB(int i) {
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String dongCode = mAdresApceAPI.getId(i);
        String dongName = mAdresApceAPI.getName(i);

        DocumentReference userDoc = db.collection("users").document(uid);
        userDoc.update(
                "dongcode", dongCode,
                "dongname", dongName
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent mainActivity = new Intent(DongRegisterActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }
}
