package com.example.mehrabi_task_java_view_based.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mehrabi_task_java_view_based.R;
import com.example.mehrabi_task_java_view_based.db.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private Context context;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.users = userList;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = users.get(position);

        holder.tvFullName.setText(currentUser.fullName);
        holder.tvEmail.setText(currentUser.email);
        holder.tvPassword.setText(currentUser.password);
        holder.tvTime.setText(simpleDateFormat.format(new Date(currentUser.registrationTimestamp)));
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    public void updateUsers(List<User> newUsers) {
        this.users.clear();
        if (newUsers != null) {
            this.users.addAll(newUsers);
        }
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvEmail, tvPassword, tvTime;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.txt_fullname);
            tvEmail = itemView.findViewById(R.id.txt_email);
            tvPassword = itemView.findViewById(R.id.txt_pass);
            tvTime = itemView.findViewById(R.id.txt_time);
        }
    }
}
