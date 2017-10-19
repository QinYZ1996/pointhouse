package com.pointhouse.chiguan.k1_12;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.jsonObject.ExerciseListGetBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gyh on 2017/7/24.
 */

public class ExerciseListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ExerciseListGetBean.ResultObjectBean.ExercisesBean> courseExerciseList;
    private int lessonId;

    public ExerciseListAdapter(Context context, int lessonId) {
        mInflater = LayoutInflater.from(context);
        courseExerciseList = new ArrayList<>();
        this.lessonId = lessonId;
    }

    /**
     * 追加练习题
     *
     * @param ebs
     */
    public void addAllExercise(List<ExerciseListGetBean.ResultObjectBean.ExercisesBean> ebs) {
        courseExerciseList.addAll(ebs);
    }

    @Override
    public int getCount() {
        return courseExerciseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseExerciseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.k1_12_item_exerciselist, null);
        }

        ExerciseListGetBean.ResultObjectBean.ExercisesBean eb = (ExerciseListGetBean.ResultObjectBean.ExercisesBean) getItem(position);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_goto_exercise);
        tv.setText(eb.getTitle());
        Context context = convertView.getContext();
        tv.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExerciseActivity.class);
            ExerciseParameter param = new ExerciseParameter();
            param.setExerciseId(eb.getId());
            param.setType(eb.getType());
            param.setLayoutType(ExerciseUtil.getLayoutType(eb.getQuestionType()));
            intent.putExtra("param", param);
            intent.putExtra("lessonId", lessonId);
            v.getContext().startActivity(intent);
        });

        return convertView;
    }
}
