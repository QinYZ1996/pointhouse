package com.pointhouse.chiguan.k1_12;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.jsonObject.ExerciseDetailGetBean;
import com.pointhouse.chiguan.common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gyh on 2017/7/25.
 */

public class ExerciseAdapter extends BaseAdapter {

    // 问题格式：Q1.XXXX
    private static final String QUESTION_PATTERN = "Q%s.%s";
    private LayoutInflater mInflater;
    private List<Exercise> exerciseList;
    private int lessonId;
    private ExerciseParameter mParam;

    public ExerciseAdapter(Context context, int lessonId, ExerciseParameter param) {
        mInflater = LayoutInflater.from(context);
        exerciseList = new ArrayList<>();
        mParam = param;
        this.lessonId = lessonId;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    /**
     * 追加问答题
     *
     * @param stem
     * @param no   题号
     */
    public void addQA(String no, ExerciseDetailGetBean.ResultObjectBean.StemBean stem) {
        Exercise e = new Exercise();
        e.setQuestionNo(no);
        e.setQuestion(stem.getQuestion());
        List<String> answer = stem.getAnswer();
        if (answer != null && answer.size() > 0)
            e.setQaAnswer(answer.get(0));

        exerciseList.add(e);
    }

    /**
     * 追加选择题
     *
     * @param stem
     * @param no   题号
     */
    public void addChoice(String no, ExerciseDetailGetBean.ResultObjectBean.StemBean stem) {
        Exercise e = new Exercise();
        e.setQuestionNo(no);
        e.setQuestion(stem.getQuestion());
        e.setChoiceAnswer(stem.getAnswer() == null ? new ArrayList<>() : stem.getAnswer());

        List<String> options = stem.getOption();
        if (options != null) {
            for (int i = 0; i < options.size(); i++) {
                Option opt = new Option();
                opt.setOptionNo(String.valueOf(i + 1));
                opt.setOptionNoLabel(ExerciseUtil.getOptionNoLabel(i));
                opt.setOptionValue(options.get(i));
                e.getOptions().add(opt);
            }
        }

        exerciseList.add(e);
    }

    /**
     * 追加问答题或选择题答案
     *
     * @param list
     */
    public void addAnswer(List<Exercise> list) {
        exerciseList = list;
    }

    /**
     * 追加按钮行
     */
    public void addButton() {
        Exercise e = new Exercise();
        e.setBtn(true);
        exerciseList.add(e);
    }

    class ChoiceHolder {
        TextView question;
        TextView userAnswer;
        TextView answer;
        EditText editAnswer;
        List<OptionRowHolder> options;
        ExerciseTextWatcher watcher;

        public void updatePosition(int position) {
            watcher.updatePosition(position);
        }

        public void setQAView(View v) {
            question = (TextView) v.findViewById(R.id.tv_question);
            editAnswer = (EditText) v.findViewById(R.id.et_useranswer);
        }

        public void setQAAnswerView(View v) {
            question = (TextView) v.findViewById(R.id.tv_question);
            userAnswer = (TextView) v.findViewById(R.id.tv_useranswer);
            answer = (TextView) v.findViewById(R.id.tv_answer);
        }
    }

    class OptionRowHolder {
        RelativeLayout optionRow;
        ImageView optionIcon;
        TextView optionNoLabel;
        TextView optionValue;

        public OptionRowHolder(RelativeLayout optionRow, ImageView optionIcon, TextView optionNoLabel, TextView optionValue) {
            this.optionRow = optionRow;
            this.optionIcon = optionIcon;
            this.optionNoLabel = optionNoLabel;
            this.optionValue = optionValue;
        }
    }

    @Override
    public int getCount() {
        return exerciseList.size();
    }

    @Override
    public Object getItem(int position) {
        return exerciseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Exercise e = (Exercise) getItem(position);
        return e.isBtn() ? getSubmitView(convertView, e) : getCurrentView(convertView, e, position);
    }

    /**
     * 数据行画面处理
     *
     * @param v
     * @param e
     * @param position
     * @return
     */
    private View getCurrentView(View v, Exercise e, int position) {
        switch (mParam.getLayoutType()) {
            case ExerciseParameter.LAYOUT_QA:
                return getQAView(v, e, position);
            case ExerciseParameter.LAYOUT_QA_ANSWER:
                return getQAAnswerView(v, e);
            case ExerciseParameter.LAYOUT_CHOICE:
            case ExerciseParameter.LAYOUT_CHOICE_ANSWER:
                return getChoiceView(v, e);
            default:
                return v;
        }
    }

    /**
     * 提交按钮画面处理
     *
     * @param v
     * @param e
     * @return
     */
    private View getSubmitView(View v, Exercise e) {
        if (v == null || v.getId() != R.id.layout_tijiao) {
            v = mInflater.inflate(R.layout.k1_12_item_tijiao, null);
        }

        Button btn = (Button) v.findViewById(R.id.btn_tijiao);
        if (ExerciseUtil.isAnswerLayout(mParam.getLayoutType())) {
            btn.setVisibility(View.INVISIBLE);
        } else {
            Context context = v.getContext();
            btn.setOnClickListener(v1 -> {
                if (exerciseList.size() > 1) {

                    // 检查是否全部答完
                    boolean canCommit = true;
                    if (mParam.getLayoutType() == ExerciseParameter.LAYOUT_QA) {
                        for (int i = 0; i < exerciseList.size() - 1; i++) {
                            Exercise q = exerciseList.get(i);
                            if (q.getQaUserAnswer() == null || "".equals(q.getQaUserAnswer())) {
                                canCommit = false;
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < exerciseList.size() - 1; i++) {
                            Exercise q = exerciseList.get(i);
                            if (q == null || q.getChoiceUserAnswer().size() == 0) {
                                canCommit = false;
                                break;
                            }
                        }
                    }

                    if (canCommit) {
                        // 跳转到答案画面
                        Intent intent = new Intent(context, ExerciseActivity.class);
                        ExerciseParameter param = ExerciseParameter.create(mParam);
                        param.setExerciseList(exerciseList);
                        param.setLayoutType(param.getLayoutType() == ExerciseParameter.LAYOUT_QA ? ExerciseParameter.LAYOUT_QA_ANSWER : ExerciseParameter.LAYOUT_CHOICE_ANSWER);
                        intent.putExtra("param", param);
                        intent.putExtra("lessonId", lessonId);
                        context.startActivity(intent);
                    } else {
                        ToastUtil.getToast(context, "有未填写或选择的习题，请完成后再提交。", "center", 0, 180).show();
                    }
                }
            });
        }

        return v;
    }

    //定义成员变量mTouchItemPosition,用来记录手指触摸的EditText的位置
    private int mTouchItemPosition = -1;

    /**
     * 获取问答题view
     *
     * @param v
     * @param e
     * @param position
     * @return
     */
    private View getQAView(View v, Exercise e, int position) {
        ChoiceHolder holder;
        if (v == null || v.getId() != getCurrentLayoutId()) {
            v = mInflater.inflate(getCurrentLayout(), null);
            holder = new ChoiceHolder();
            holder.setQAView(v);
            holder.watcher = new ExerciseTextWatcher();
            holder.editAnswer.addTextChangedListener(holder.watcher);
            holder.updatePosition(position);
            holder.editAnswer.setOnTouchListener((view, event) -> {
                //注意，此处必须使用getTag的方式，不能将position定义为final，写成mTouchItemPosition = position
                mTouchItemPosition = (Integer) view.getTag();
                //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((view.getId() == R.id.et_useranswer && canVerticalScroll((EditText) view))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            });
            v.setTag(holder);
        } else {
            holder = (ChoiceHolder) v.getTag();
            holder.updatePosition(position);
        }

        holder.question.setText(String.format(QUESTION_PATTERN, e.getQuestionNo(), e.getQuestion()));
        holder.editAnswer.setText(e.getQaUserAnswer());
        holder.editAnswer.setTag(position);
        if (mTouchItemPosition == position) {
            holder.editAnswer.requestFocus();
            holder.editAnswer.setSelection(holder.editAnswer.getText().length());
        } else {
            holder.editAnswer.clearFocus();
        }

        return v;
    }

    class ExerciseTextWatcher implements TextWatcher {

        private int mPosition;

        public void updatePosition(int position) {
            mPosition = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            exerciseList.get(mPosition).setQaUserAnswer(s.toString());
        }
    }

    /**
     * 获取问答题答案view
     *
     * @param v
     * @param e
     * @return
     */
    private View getQAAnswerView(View v, Exercise e) {
        ChoiceHolder holder;
        if (v == null || v.getId() != getCurrentLayoutId()) {
            v = mInflater.inflate(getCurrentLayout(), null);
            holder = new ChoiceHolder();
            holder.setQAAnswerView(v);
            v.setTag(holder);
        } else {
            holder = (ChoiceHolder) v.getTag();
        }

        holder.question.setText(String.format(QUESTION_PATTERN, e.getQuestionNo(), e.getQuestion()));
        holder.userAnswer.setText(e.getQaUserAnswer());
        holder.answer.setText(e.getQaAnswer());

        return v;
    }

    /**
     * 获取选择题或选择题答案view
     *
     * @param v
     * @param e
     * @return
     */
    private View getChoiceView(View v, Exercise e) {
        ChoiceHolder choiceHolder;
        boolean isAnswer = mParam.getLayoutType() == ExerciseParameter.LAYOUT_CHOICE_ANSWER;
        boolean isCreate;
        // 因为选项动态加载，所以还要根据选项数量是否一致判断可不可以复用layout
        if (v == null || v.getId() != getCurrentLayoutId()) {
            isCreate = true;
            choiceHolder = new ChoiceHolder();
        } else {
            choiceHolder = (ChoiceHolder) v.getTag();
            isCreate = e.getOptions().size() != choiceHolder.options.size();
            choiceHolder = isCreate ? new ChoiceHolder() : choiceHolder;
        }

        if (isCreate) {
            v = mInflater.inflate(getCurrentLayout(), null);
            TextView question = (TextView) v.findViewById(R.id.tv_question);
            choiceHolder.question = question;

            LinearLayout ll = (LinearLayout) v;
            List<OptionRowHolder> optionRowHolders = new ArrayList<>();
            // 动态加载选项行，不确定选项是否永远为4个
            for (int i = 0; i < e.getOptions().size(); i++) {
                View optionView = mInflater.inflate(R.layout.k1_12_item_choiceoption, null);
                ll.addView(optionView);

                RelativeLayout rl = (RelativeLayout) optionView.findViewById(R.id.rl_option);
                ImageView optionIcon = (ImageView) optionView.findViewById(R.id.iv_option);
                TextView optionNoLabel = (TextView) optionView.findViewById(R.id.tv_option_no_label);
                TextView optionValue = (TextView) optionView.findViewById(R.id.tv_option);
                optionRowHolders.add(new OptionRowHolder(rl, optionIcon, optionNoLabel, optionValue));
            }

            choiceHolder.options = optionRowHolders;
            v.setTag(choiceHolder);
        }

        List<OptionRowHolder> optionRowHolders = choiceHolder.options;
        if (!isAnswer) { // 选择题
            // 选项点击事件绑定
            for (int i = 0; i < optionRowHolders.size(); i++) {
                OptionRowHolder holder = optionRowHolders.get(i);
                final int index = i;
                holder.optionRow.setOnClickListener(rowView -> {
                    Option targetOpt = e.getOptions().get(index);
                    if (targetOpt.isSelected()) {
                        targetOpt.setSelected(false);
                        e.getChoiceUserAnswer().remove(targetOpt.getOptionNo());
                        holder.optionNoLabel.setTextColor(ContextCompat.getColor(rowView.getContext(), R.color.k1_12_choice_text_normal));
                        holder.optionValue.setTextColor(ContextCompat.getColor(rowView.getContext(), R.color.k1_12_choice_text_normal));
                        holder.optionRow.setBackgroundResource(R.drawable.k1_12_border_normal);
                    } else {
                        targetOpt.setSelected(true);
                        e.getChoiceUserAnswer().add(targetOpt.getOptionNo());
                        holder.optionNoLabel.setTextColor(ContextCompat.getColor(rowView.getContext(), R.color.k1_12_choice_text_select));
                        holder.optionValue.setTextColor(ContextCompat.getColor(rowView.getContext(), R.color.k1_12_choice_text_select));
                        holder.optionRow.setBackgroundResource(R.drawable.k1_12_border_select);
                    }
                });
            }
        }

        choiceHolder.question.setText(String.format(QUESTION_PATTERN, e.getQuestionNo(), e.getQuestion()));

        for (int i = 0; i < e.getOptions().size(); i++) {
            // 共通部分 => 选项号、选项内容绑定
            Option option = e.getOptions().get(i);
            OptionRowHolder optionRowHolder = optionRowHolders.get(i);
            optionRowHolder.optionNoLabel.setText(option.getOptionNoLabel());
            optionRowHolder.optionValue.setText(option.getOptionValue());

            String optionNo = option.getOptionNo();
            if (isAnswer) { // 选择题答案
                if (e.getChoiceAnswer().contains(optionNo)) { // 正确答案就显示对勾
                    optionRowHolder.optionNoLabel.setVisibility(View.INVISIBLE);
                    optionRowHolder.optionIcon.setImageResource(R.mipmap.duigou);
                    optionRowHolder.optionIcon.setVisibility(View.VISIBLE);
                    if (e.getChoiceUserAnswer().contains(optionNo)) { // 用户选择：蓝色
                        optionRowHolder.optionRow.setBackgroundResource(R.drawable.k1_12_border_answer);
                        optionRowHolder.optionValue.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_answer));
                    } else { // 非用户选择：黑色
                        optionRowHolder.optionRow.setBackgroundResource(R.drawable.k1_12_border_normal);
                        optionRowHolder.optionValue.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_normal));
                    }
                } else { // 不是正确答案就显示选项号
                    optionRowHolder.optionNoLabel.setVisibility(View.VISIBLE);
                    optionRowHolder.optionIcon.setVisibility(View.INVISIBLE);
                    if (e.getChoiceUserAnswer().contains(optionNo)) { // 用户选择：灰色
                        optionRowHolder.optionRow.setBackgroundResource(R.drawable.k1_12_border_select);
                        optionRowHolder.optionNoLabel.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_select));
                        optionRowHolder.optionValue.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_select));
                    } else { // 非用户选择：黑色
                        optionRowHolder.optionRow.setBackgroundResource(R.drawable.k1_12_border_normal);
                        optionRowHolder.optionNoLabel.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_normal));
                        optionRowHolder.optionValue.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_normal));
                    }
                }
            } else { // 选择题，根据选中情况变更颜色
                if (option.isSelected()) {
                    optionRowHolder.optionNoLabel.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_select));
                    optionRowHolder.optionValue.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_select));
                    optionRowHolder.optionRow.setBackgroundResource(R.drawable.k1_12_border_select);
                } else {
                    optionRowHolder.optionNoLabel.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_normal));
                    optionRowHolder.optionValue.setTextColor(ContextCompat.getColor(v.getContext(), R.color.k1_12_choice_text_normal));
                    optionRowHolder.optionRow.setBackgroundResource(R.drawable.k1_12_border_normal);
                }
            }
        }

        return v;
    }

    /**
     * 获取数据行画面
     *
     * @return
     */
    private int getCurrentLayout() {
        switch (mParam.getLayoutType()) {
            case ExerciseParameter.LAYOUT_QA:
                return R.layout.k1_12_item_qa;
            case ExerciseParameter.LAYOUT_QA_ANSWER:
                return R.layout.k1_12_item_qaanswer;
            case ExerciseParameter.LAYOUT_CHOICE:
            case ExerciseParameter.LAYOUT_CHOICE_ANSWER:
                return R.layout.k1_12_item_choice;
            default:
                return R.layout.k1_12_item_qa;
        }
    }

    /**
     * 获取数据行画面id
     *
     * @return
     */
    private int getCurrentLayoutId() {
        switch (mParam.getLayoutType()) {
            case ExerciseParameter.LAYOUT_QA:
                return R.id.layout_qa;
            case ExerciseParameter.LAYOUT_QA_ANSWER:
                return R.id.layout_qaanswer;
            case ExerciseParameter.LAYOUT_CHOICE:
            case ExerciseParameter.LAYOUT_CHOICE_ANSWER:
                return R.id.layout_choice;
            default:
                return R.id.layout_qa;
        }
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
