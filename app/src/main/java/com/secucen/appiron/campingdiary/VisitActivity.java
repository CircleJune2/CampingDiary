package com.secucen.appiron.campingdiary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VisitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        //리스트뷰 내용

        final TextView tvSelect = findViewById(R.id.tv_select);
        ListView listView = findViewById(R.id.listView);

        List<String> list = new ArrayList<>();
        list.add("1. 난지캠핑장");
        list.add("2. 강동캠핑장");
        list.add("3. 서울숲캠핑장");
        list.add("4. 하늘숲캠핑장");
        list.add("5. 바다캠핑장");
        list.add("6. 섬캠핑장");
        list.add("7. 지리산캠핑장");
        list.add("8. 속리산캠핑장");
        list.add("9. K2캠핑장");
        list.add("10. 사나래캠핑장");
        list.add("11. 물아페캠핑장");
        list.add("12. 무리에캠핑장");
        list.add("13. 여기어때캠핑장");
        list.add("14. 불멍캠핑장");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data = (String) adapterView.getItemAtPosition(position);
                tvSelect.setText(data);
            }
        });
    }
}