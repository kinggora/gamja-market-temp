package com.example.gamjamarket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.gamjamarket.R;
import com.google.android.material.tabs.TabLayout;

public class ChatParentFragment extends Fragment {
    TabLayout tabs;
    ChatFragment chatFragment;
    ChatFragment2 chatFragment2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        chatFragment = new ChatFragment();
        chatFragment2 = new ChatFragment2();
        getFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        tabs = view.findViewById(R.id.chatActivity_tab);
        tabs.addTab(tabs.newTab().setText("물물교환"));
        tabs.addTab(tabs.newTab().setText("기부"));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if (position == 0)
                    selected = chatFragment;
                else if (position == 1)
                    selected = chatFragment2;
                getFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        return view;
    }
}
