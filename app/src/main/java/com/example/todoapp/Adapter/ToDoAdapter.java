package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList;
    private  MainActivity activity;
    private DatabaseHandler db;


    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        ViewHolder(View view){
            super(view);
            checkBox =view.findViewById(R.id.todoCheckBox);
        }
    }

    public ToDoAdapter(DatabaseHandler db,MainActivity activity) {
        this.db=db;
        this.activity = activity;
   }



    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout,parent,false);

        return new ViewHolder(itemView);

    }
    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
         final ToDoModel item = todoList.get(position);
        holder.checkBox.setText(item.getTask());
        // checks if the checkbox is 1 or 0
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        // applies changes to checkbox
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    db.updateStatus(item.getId(),1);
                }
                else{
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

  public boolean toBoolean(int n){
       return n!=0;
    }

    public void setTasks(List<ToDoModel> todoList){

        this.todoList=todoList;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return todoList.size();
    }

    public Context getContext(){
        return activity;
    }


    public void deleteItem(int position) {
        ToDoModel item=todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);

    }
    public void editItem(int position){
        // get the position of the in the list
        ToDoModel item=todoList.get(position);
        Bundle bundle=new Bundle();
        // store the id and task of that position in bundle
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());

        AddNewTask fragment=new AddNewTask();
        fragment.setArguments(bundle);
       fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
    }




}
