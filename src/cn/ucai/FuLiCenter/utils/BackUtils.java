package cn.ucai.FuLiCenter.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import cn.ucai.FuLiCenter.R;

/**
 * Created by Zhou on 2016/8/4.
 */
public class BackUtils {
    public static void ActivityBack(final Activity activity, String title){
        ((TextView)activity.findViewById(R.id.back_title)).setText(title);
        activity.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });

    }

    public static void ActivityBack(final Activity activity){
        activity.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }
}
