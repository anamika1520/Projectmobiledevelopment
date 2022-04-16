package com.project.fttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements RecyclerLeagueAdapter.OnLeagueClick {

    private ArrayList<LeagueItems> list;
    private RecyclerView recyclerView;
    private RecyclerLeagueAdapter adapter;
    private FirebaseAuth auth;
    private CircularProgressIndicator progressBar;
    private ConstraintLayout clMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clMain = findViewById(R.id.clMain);
        clMain.setVisibility(View.GONE);

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerMain);
        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarMain);

        Date date = new Date();
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api-football-v1.p.rapidapi.com/v3/fixtures?date=" + modifiedDate)
                .get()
                .addHeader("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "0acf6a13d8msh6b66e3f088f9a5ap199328jsn423dcd94ed5e")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MainActivity.this, "APi call failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String rsp = Objects.requireNonNull(response.body()).string();
                    MainActivity.this.runOnUiThread(() -> {
                        try {
                            JSONObject object = new JSONObject(rsp);
                            JSONArray array = object.getJSONArray("response");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject wholeObject = array.getJSONObject(i);

                                JSONObject leagueObject = wholeObject.getJSONObject("league");
                                String leagueId = leagueObject.getString("id");
                                String leagueName = leagueObject.getString("name");
                                String season = leagueObject.getString("season");
                                String flag = leagueObject.getString("logo");

                                LeagueItems item = new LeagueItems(leagueName, leagueId, season, flag);

                                if (!list.contains(item)) {
                                    list.add(item);
                                }
                            }
                            setRecyclerView(list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else if (!response.isSuccessful()) {
                    Log.e("TAG", "onResponse: " + response.networkResponse());
                }
            }
        });
    }

    private void setRecyclerView(ArrayList<LeagueItems> list) {
        adapter = new RecyclerLeagueAdapter(list, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.hasFixedSize();
        progressBar.setVisibility(View.GONE);
        clMain.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_items, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView search = (SearchView) item.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null)
                    adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onLeagueItemClick(int position) {
        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra("EXTRA_ID", list.get(position).getLeagueId());
        intent.putExtra("EXTRA_SEASON", list.get(position).getSeason());
        startActivity(intent);
    }
}