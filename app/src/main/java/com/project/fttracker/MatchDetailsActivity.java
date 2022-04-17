package com.project.fttracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.fttracker.databinding.ActivityMatchDetailsBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MatchDetailsActivity extends AppCompatActivity {

    private ActivityMatchDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.clMatchDetails.setVisibility(View.GONE);

        Intent intent = getIntent();
        String id = intent.getStringExtra("EXTRA_ID");

        ShapeableImageView ivHome = binding.ivTeamHome;
        ShapeableImageView ivAway = binding.ivTeamAway;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api-football-v1.p.rapidapi.com/v3/fixtures?id="+id)
                .get()
                .addHeader("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "0acf6a13d8msh6b66e3f088f9a5ap199328jsn423dcd94ed5e")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MatchDetailsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rsp = Objects.requireNonNull(response.body()).string();
                    MatchDetailsActivity.this.runOnUiThread(() -> {

                        try {
                            JSONObject object = new JSONObject(rsp);
                            JSONArray array = object.getJSONArray("response");

                            JSONObject wholeObject = array.getJSONObject(0);
                            JSONObject fixtureObject = wholeObject.getJSONObject("fixture");

                            JSONObject venueObject = fixtureObject.getJSONObject("venue");
                            String venueName = venueObject.getString("name");
                            String venueCity = venueObject.getString("city");

                            JSONObject statusObject = fixtureObject.getJSONObject("status");
                            String matchStatus = statusObject.getString("long");

                            JSONObject teamObject = wholeObject.getJSONObject("teams");
                            JSONObject teamHomeObject = teamObject.getJSONObject("home");
                            String teamHomeName = teamHomeObject.getString("name");
                            String teamHomeLogo = teamHomeObject.getString("logo");
                            String isHomeWInner = teamHomeObject.getString("winner");

                            JSONObject teamAwayObject = teamObject.getJSONObject("away");
                            String teamAwayName = teamAwayObject.getString("name");
                            String teamAwayLogo = teamAwayObject.getString("logo");
                            String isAwayWInner = teamAwayObject.getString("winner");

                            JSONObject goalsObject = wholeObject.getJSONObject("goals");
                            String homeGoals = goalsObject.getString("home");
                            String  awayGoals = goalsObject.getString("away");


                            JSONObject leagueObject = wholeObject.getJSONObject("league");
                            String leagueName = leagueObject.getString("name");

                            Glide.with(MatchDetailsActivity.this).load(teamHomeLogo).into(ivHome);
                            Glide.with(binding.getRoot()).load(teamAwayLogo)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            binding.progressBarDetails.hide();
                                            binding.clMatchDetails.setVisibility(View.VISIBLE);
                                            return false;
                                        }
                                    }).into(ivAway);

                            switch (matchStatus) {
                                case "Match Cancelled":

                                    binding.cv2.setCardBackgroundColor(Color.parseColor("#FF2E2E"));
                                    binding.tvMatchStatus.setTextColor(getResources().getColor(R.color.white));
                                    binding.tvMatchStatusText.setTextColor(getResources().getColor(R.color.white));

                                    break;

                                case "Match Finished":

                                    binding.cv2.setCardBackgroundColor(Color.parseColor("#00A300"));
                                    binding.tvMatchStatus.setTextColor(getResources().getColor(R.color.white));
                                    binding.tvMatchStatusText.setTextColor(getResources().getColor(R.color.white));

                                    break;
                                case "Not Started":

                                    binding.cv2.setCardBackgroundColor(Color.parseColor("#e8b923"));
                                    binding.tvMatchStatus.setTextColor(getResources().getColor(R.color.white));
                                    binding.tvMatchStatusText.setTextColor(getResources().getColor(R.color.white));
                                    break;
                            }

                            binding.tvMatchStatusText.setText(matchStatus);

                            if(venueName.equals("null") || venueCity.equals("null")){
                                binding.tvVenuePlace.setText("--");
                            }else{
                                binding.tvVenuePlace.setText(venueName+", "+venueCity);
                            }

                            binding.tvTeam1Name.setText(teamHomeName);
                            binding.tvTeam2Name.setText(teamAwayName);
                            binding.tvLeagueText.setText(leagueName);
                            if(homeGoals.equals("null") || awayGoals.equals("null")){
                                binding.tvGoalsText.setText("--");
                            }else{
                                binding.tvGoalsText.setText(homeGoals+" - "+awayGoals);
                            }

                            if(isHomeWInner.equals("true")){
                                binding.tvWinnerName.setText(teamHomeName);
                                binding.tvWinnerName.setTextColor(getResources().getColor(R.color.green));
                            }else if(isAwayWInner.equals("true")){
                                binding.tvWinnerName.setText(teamAwayName);
                                binding.tvWinnerName.setTextColor(getResources().getColor(R.color.green));
                            }else if(isAwayWInner.equals("null") || isHomeWInner.equals("null")){
                                binding.tvWinnerName.setTextColor(getResources().getColor(R.color.black));
                                binding.tvWinnerName.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                binding.tvWinnerName.setText("--");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        });
    }
}