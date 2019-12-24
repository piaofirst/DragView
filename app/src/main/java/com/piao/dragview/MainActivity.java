package com.piao.dragview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private DragView dragView;
    private ImageView image;
    private TextView txt_customService_num;
    private RelativeLayout kefu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        dragView = findViewById(R.id.dragView);
        image = findViewById(R.id.image);
        txt_customService_num = findViewById(R.id.txt_customService_num);
        kefu = findViewById(R.id.kefu);
        textView.setOnClickListener(listener);
//        dragView.setOnClickListener(listener);
        image.setOnClickListener(listener);
        txt_customService_num.setOnClickListener(listener);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) kefu.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.rightMargin = DensityUtil.dp2px(this,10);
        params.bottomMargin = DensityUtil.dp2px(this, 30);
        kefu.setLayoutParams(params);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text:
                    Toast.makeText(MainActivity.this, "aaa", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.dragView:
                    Toast.makeText(MainActivity.this, "bbb", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.image:
                    Toast.makeText(MainActivity.this, "ccc", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.txt_customService_num:
                    Toast.makeText(MainActivity.this, "ddd", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
