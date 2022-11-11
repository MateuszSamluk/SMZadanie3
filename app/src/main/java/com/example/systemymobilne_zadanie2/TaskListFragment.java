package com.example.systemymobilne_zadanie2;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskListFragment extends Fragment {
    public static final String KEY_EXTRA_TASK_ID = "KEY_EXTRA_TASK_ID";
    public static final String SUBTITLE_KEY = "SUBTITLE_KEY";
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private boolean subtitleVisible;
    @Override
    public void onCreate(@NonNull Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(savedInstanceState != null){
            subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_KEY);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateView();
        return view;
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Task task;
        private TextView nameTextView;
        private TextView dateTextView;
        private ImageView iconImageView;
        private CheckBox itemCheckBox;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            iconImageView = itemView.findViewById(R.id.task_item_view);
            itemCheckBox = itemView.findViewById(R.id.task_item_checkBox);
            //itemCheckBox.setChecked(task.isDone());

        }
        public void bind(Task task){
            this.task = task;
            nameTextView.setText(task.getName());
            //dateTextView.setText(task.getDate().toString());
            setupDateFieldValue(task.getDate());
            if(task.getCategory().equals(Category.HOME)){
                iconImageView.setImageResource(R.drawable.ic_house);
            }
            else{
                iconImageView.setImageResource(R.drawable.ic_studies);
            }
            if(task.isDone()){
                nameTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
        private void setupDateFieldValue(Date date) {
            Locale locale  = new Locale("pl", "PL");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);
            dateTextView.setText(dateFormat.format(date));
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }

        public CheckBox getCheckBox() {
            return itemCheckBox;
        }
        public TextView getTextView() { return  nameTextView;}
    }

    private class TaskAdapter extends  RecyclerView.Adapter<TaskHolder>{
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks){
            this.tasks =tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
            CheckBox checkBox = holder.getCheckBox();
            TextView textView = holder.getTextView();
            checkBox.setChecked(tasks.get(position).isDone());
            //checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
            //tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked);
                        textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        updateSubtitle();
                    }
                    else
                    {
                        tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked);
                        textView.setPaintFlags(0);
                        updateSubtitle();
                    }
                }

            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }

    private void updateView(){
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        if(adapter == null){
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        }
        else{
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
    @Override
    public void onResume() {

        super.onResume();
        updateView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_task:
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                subtitleVisible =! subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }
    public void updateSubtitle(){
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        int toDoTaskCount = 0;
        for(Task task : tasks){
            if(!task.isDone()){
                toDoTaskCount++;
            }
        }
        String subtitle = getString( R.string.subtitle_format, toDoTaskCount);
        if(!subtitleVisible){
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);

    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putBoolean(SUBTITLE_KEY, subtitleVisible);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
