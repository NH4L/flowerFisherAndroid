package cn.aysst.www.flowerfish.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.aysst.www.flowerfish.R;
import cn.aysst.www.flowerfish.fragment.*;

public class DataGenerator {

    public static final int []mTabRes = new int[]{ R.drawable.flower, R.drawable.fish, R.drawable.my};
    public static final int []mTabResPressed = new int[]{ R.drawable.flower_pressed, R.drawable.fish_pressed, R.drawable.my_pressed};
    public static final String []mTabTitle = new String[]{"浇花", "养鱼", "我的"};

    public static Fragment[] getFragments(){
        Fragment fragments[] = new Fragment[3];
        fragments[0] = FragmentFlower.newInstance();
        fragments[1] = FragmentFish.newInstance();
        fragments[2] = FragmentPerson.newInstance();
        return fragments;
    }


    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}