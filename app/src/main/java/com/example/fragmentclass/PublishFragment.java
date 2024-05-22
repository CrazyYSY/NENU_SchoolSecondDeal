package com.example.fragmentclass;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.secondhandtransaction.LoginActivity;
import com.example.secondhandtransaction.R;
import com.example.secondhandtransaction.ReleaseActivity;
import com.example.secondhandtransaction.TestActivity;


public class PublishFragment extends Fragment implements View.OnClickListener {
    private Button btn_publish;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1=null;
        if (view1==null){
            view1=inflater.inflate(R.layout.activity_publish,container,false);
            RelativeLayout button=(RelativeLayout) view1.findViewById(R.id.re_fabu);
            button.setOnClickListener(this);
        }

        return  view1;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.re_fabu:
                Intent intent =new Intent(getActivity(), ReleaseActivity.class);
                startActivity(intent);
                break;
        }

    }
}

