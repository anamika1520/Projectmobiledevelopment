package com.project.fttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.project.fttracker.databinding.ActivityMatchBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MatchActivity extends AppCompatActivity implements RecyclerMatchAdapter.onClick {

    private ActivityMatchBinding binding;
    private ArrayList<FixtureItems> list;
    private RecyclerMatchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        binding.clMatch.setVisibility(View.GONE);

        Intent intent = getIntent();
        String id = intent.getStringExtra("EXTRA_ID");
        String season = intent.getStringExtra("EXTRA_SEASON");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api-football-v1.p.rapidapi.com/v3/fixtures?league="+id+"&season="+season)
                .get()
                .addHeader("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "0acf6a13d8msh6b66e3f088f9a5ap199328jsn423dcd94ed5e")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MatchActivity.this, "APi call failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String rsp = Objects.requireNonNull(response.body()).string();
                    MatchActivity.this.runOnUiThread(() -> {
                        try {
                            JSONObject object = new JSONObject(rsp);
                            JSONArray array = object.getJSONArray("response");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject wholeObject = array.getJSONObject(i);
                                JSONObject fixtureObject = wholeObject.getJSONObject("fixture");
                                String id = fixtureObject.getString("id");

                                JSONObject teamObject = wholeObject.getJSONObject("teams");
                                JSONObject teamHomeObject = teamObject.getJSONObject("home");
                                String teamHomeName = teamHomeObject.getString("name");

                                JSONObject teamAwayObject = teamObject.getJSONObject("away");
                                String teamAwayName = teamAwayObject.getString("name");

                                JSONObject statusObject = fixtureObject.getJSONObject("status");
                                String matchStatus = statusObject.getString("long");

                                JSONObject leagueObject = wholeObject.getJSONObject("league");
                                String leagueName = leagueObject.getString("name");

                                FixtureItems item = new FixtureItems(id, matchStatus, teamHomeName, teamAwayName, leagueName);
                                list.add(item);
                            }
                            binding.tvLeagueName.setText(list.get(0).getLeague());
                            setRecyclerView(list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Toast.makeText(MatchActivity.this, "Couldn't load data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onRecyclerItemClick(int position) {
        Intent intent = new Intent(this,MatchDetailsActivity.class);
        intent.putExtra("EXTRA_ID",list.get(position).getId());
        startActivity(intent);
    }

    private void setRecyclerView(ArrayList<FixtureItems> list) {
        adapter = new RecyclerMatchAdapter(list,this);
        binding.recyclerMatch.setAdapter(adapter);
        binding.recyclerMatch.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerMatch.hasFixedSize();
        binding.progressBarMatch.setVisibility(View.GONE);
        binding.clMatch.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.match_menu_items, menu);

        MenuItem item = menu.findItem(R.id.match_menu_search);
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
}