package com.example.mehrabi_task_java_view_based;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mehrabi_task_java_view_based.adapters.UserAdapter;
import com.example.mehrabi_task_java_view_based.db.AppDatabase;
import com.example.mehrabi_task_java_view_based.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResultActivity extends AppCompatActivity {
    private RecyclerView rvUsers;
    private TextView tvNoUsers;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();

    private AppDatabase appDatabase;
    private ExecutorService databaseExecutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appDatabase = AppDatabase.getInstance(getApplicationContext());
        databaseExecutor = Executors.newSingleThreadExecutor();

        rvUsers = findViewById(R.id.users_rv);
        tvNoUsers = findViewById(R.id.no_users_txt);

        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(this, userList);
        rvUsers.setAdapter(userAdapter);

        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        databaseExecutor.execute(() -> {
            List<User> fetchedUsers = appDatabase.userDao().getAll();

            runOnUiThread(() -> {
                if (fetchedUsers != null && !fetchedUsers.isEmpty()) {
                    userAdapter.updateUsers(fetchedUsers);
                    rvUsers.setVisibility(View.VISIBLE);
                    tvNoUsers.setVisibility(View.GONE);
                } else {
                    userAdapter.updateUsers(null);
                    rvUsers.setVisibility(View.GONE);
                    tvNoUsers.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseExecutor != null && !databaseExecutor.isShutdown()) {
            databaseExecutor.shutdown();
        }
    }
}