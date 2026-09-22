package com.telynet.telynetusers.feature.detail;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;
import com.telynet.telynetusers.core.models.entity.User;

import coil.Coil;
import coil.request.ImageRequest;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserDetailActivity extends AppCompatActivity {

    public static final String EXTRA_USER_CODE = "code";

    private ImageView photoView;
    private TextView nameView;
    private TextView addressView;
    private TextView companyView;
    private TextView phoneView;
    private TextView emailView;
    private View statusBadge;
    private View statusDot;
    private TextView statusText;

    private final UserDetailViewModel viewModel = new ViewModelProvider(this).get(UserDetailViewModel.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.topToolbar);
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        photoView = findViewById(R.id.imgDetailPhoto);
        nameView = findViewById(R.id.tvDetailName);
        addressView = findViewById(R.id.tvLocationAddress);
        companyView = findViewById(R.id.tvLocationSuite);
        phoneView = findViewById(R.id.tvCredentialPhone);
        emailView = findViewById(R.id.tvCredentialEmail);
        statusBadge = findViewById(R.id.includeDetailStatus);
        statusDot = findViewById(R.id.viewStatusDot);
        statusText = findViewById(R.id.tvStatusText);

        viewModel.getUiState().observe(this, this::render);
    }

    private void render(UserDetailUiState state) {
        if (state instanceof UserDetailUiState.Success) {
            bindUser(((UserDetailUiState.Success) state).getUser());
        } else if (state instanceof UserDetailUiState.NotFound) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
        } else if (state instanceof UserDetailUiState.Error) {
            Toast.makeText(this, ((UserDetailUiState.Error) state).getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void bindUser(User user) {
        loadPhoto(user.getImageUrl());
        nameView.setText(user.getName());
        addressView.setText(user.getAddress());
        companyView.setText(user.getCompany());
        phoneView.setText(user.getPhone());
        emailView.setText(user.getEmail());
        bindVisitStatus(user.isVisited());
    }

    private void loadPhoto(String imageUrl) {
        ImageRequest request = new ImageRequest.Builder(this)
                .data(imageUrl)
                .crossfade(true)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .target(photoView)
                .build();
        Coil.imageLoader(this).enqueue(request);
    }

    private void bindVisitStatus(boolean isVisited) {
        int background = isVisited ? R.color.status_visited_bg : R.color.status_pending_bg;
        int textColor = isVisited ? R.color.status_visited_text : R.color.status_pending_text;

        statusBadge.setBackground(roundedDrawable(ContextCompat.getColor(this, background), GradientDrawable.RECTANGLE));
        statusText.setTextColor(ContextCompat.getColor(this, textColor));
        statusText.setText(isVisited ? "✓ Visited" : "Pending");
        statusDot.setVisibility(isVisited ? View.GONE : View.VISIBLE);
        statusDot.setBackground(roundedDrawable(ContextCompat.getColor(this, R.color.status_pending_dot), GradientDrawable.OVAL));
    }

    private GradientDrawable roundedDrawable(int color, int shape) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(shape);
        drawable.setColor(color);
        if (shape == GradientDrawable.RECTANGLE) {
            drawable.setCornerRadius(getResources().getDisplayMetrics().density * 100);
        }
        return drawable;
    }
}
