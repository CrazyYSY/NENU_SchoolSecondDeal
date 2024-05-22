package com.example.fragmentclass;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.adapter.AllCommodityAdapter;
import com.example.database.DataBaseHelper;
import com.example.po.Commodity;
import com.example.secondhandtransaction.CommodityTypeActivity;
import com.example.secondhandtransaction.DetailInfoActivity;
import com.example.secondhandtransaction.R;
import com.example.secondhandtransaction.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.secondhandtransaction.LoginActivity.only_userid;

public class DetailFragment extends Fragment {

    ListView lvAllCommodity;
    List<Commodity> allCommodities = new ArrayList<>();
    TextView ibLearning, ibElectronic, ibDaily, ibSports;

    DataBaseHelper dbHelper;
    AllCommodityAdapter adapter;

    //String stuNum =null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail, container, false);


        view.findViewById(R.id.rl_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });


        lvAllCommodity = view.findViewById(R.id.lv_all_commodity);
        dbHelper = new DataBaseHelper(getActivity(), DataBaseHelper.DB_NAME, null, 1);
        adapter = new AllCommodityAdapter(getActivity().getApplicationContext());
        allCommodities = dbHelper.readAllCommodities();
        adapter.setData(allCommodities);
        lvAllCommodity.setAdapter(adapter);
        //final Bundle bundle = this.getIntent().getExtras();
        Bundle bundle = getActivity().getIntent().getExtras();

        //为每一个item设置点击事件
        lvAllCommodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Commodity commodity = (Commodity) lvAllCommodity.getAdapter().getItem(position);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", position);
                bundle1.putByteArray("picture", commodity.getPicture());
                bundle1.putString("title", commodity.getTitle());
                bundle1.putString("description", commodity.getDescription());
                bundle1.putFloat("price", commodity.getPrice());
                bundle1.putString("phone", commodity.getPhone());
                bundle1.putString("stuId", only_userid);
                bundle1.putInt("id",commodity.getId());
                Intent intent = new Intent(getActivity(), DetailInfoActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
//        lvAllCommodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Commodity commodity = (Commodity) lvAllCommodity.getAdapter().getItem(position);
//                Intent intent = new Intent(getActivity(), DetailInfoActivity.class);
//                intent.putExtra("picture",commodity.getPicture());
//                intent.putExtra("title",commodity.getTitle());
//                intent.putExtra("description",commodity.getDescription());
//                intent.putExtra("price",commodity.getPrice());
//                intent.putExtra("phone",commodity.getPhone());
//                startActivity(intent);
//            }
//        });
        //点击不同的类别,显示不同的商品信息
        ibLearning = view.findViewById(R.id.tv_learning_use);
        ibElectronic = view.findViewById(R.id.tv_electric_product);
        ibDaily = view.findViewById(R.id.tv_daily_use);
        ibSports = view.findViewById(R.id.tv_sports_good);
        final Bundle bundle2 = new Bundle();
        //学习用品
        ibLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 1);
                Intent intent = new Intent(getActivity(), CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //电子用品
        ibElectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 2);
                Intent intent = new Intent(getActivity(), CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //生活用品
        ibDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 3);
                Intent intent = new Intent(getActivity(), CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //体育用品
        ibSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 4);
                Intent intent = new Intent(getActivity(), CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });


        return view;

    }

}
