package com.cssweb.mytest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout2 = new LinearLayout(this);
        layout2.setOrientation(LinearLayout.VERTICAL);
        //        Button button = new Button(this);
        //        button.setText("重启");
        //        button.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                rebootDevice();
        //            }
        //        });
        //        layout2.addView(button);
        layout2.addView(getTabItem("1"));
        layout2.addView(getTabItem("2"));
        setContentView(layout2);
        RelativeLayout child = (RelativeLayout) layout2.getChildAt(0);
        View child2 = child.getChildAt(0);
        View child3 = child.getChildAt(1);
        Log.d("####", "####1 = " + (child2 instanceof TextView));
        Log.d("####", "####2 = " + (child3 instanceof ImageView));
    }

    /***
     * 重启手机
     */
    private void rebootDevice() {
        String cmd = "su -c reboot";//让手机从启
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "异常了 :: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private RelativeLayout getTabItem(String tabName) {
        RelativeLayout layout = new RelativeLayout(MainActivity.this);
        RelativeLayout.LayoutParams parentParams = new RelativeLayout.LayoutParams(100, 80);
        layout.setLayoutParams(parentParams);

        TextView textView = new TextView(MainActivity.this);
        RelativeLayout.LayoutParams childParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        childParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(childParams);
        textView.setTextSize(20);
        textView.setText(tabName);
        layout.addView(textView);

        ImageView imageView = new ImageView(MainActivity.this);
        RelativeLayout.LayoutParams childParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        childParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM | RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(childParams2);
        imageView.setBackgroundColor(getResources().getColor(R.color.aaa));
        imageView.setVisibility(View.VISIBLE);
        layout.addView(imageView);
        return layout;
    }


}
