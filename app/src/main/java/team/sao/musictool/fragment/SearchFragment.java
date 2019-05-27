package team.sao.musictool.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import team.sao.musictool.R;
import team.sao.musictool.annotation.ViewID;

import static team.sao.musictool.annotation.AnnotationProcesser.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 0:57
 * \* Description:
 **/
public class SearchFragment extends Fragment {

//    @ViewID(R.id.keyword_input)
//    private EditText keywordInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        inject(this, view);
//        Drawable drawable = getResources().getDrawable(R.drawable.search);
//        drawable.setBounds(0, 0, keywordInput.getHeight(), keywordInput.getHeight());
//        keywordInput.setCompoundDrawablesRelative(drawable, null, null, null);
        return view;
    }
}
