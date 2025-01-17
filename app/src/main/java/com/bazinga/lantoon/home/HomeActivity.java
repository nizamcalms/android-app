package com.bazinga.lantoon.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bazinga.lantoon.BuildConfig;
import com.bazinga.lantoon.GetStartActivity;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.SplashActivity;
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    public static SessionManager sessionManager;
    TextView tvLearnLanguage;
    private static final float END_SCALE = 0.85f;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavView;
    private CoordinatorLayout contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Lantoon cache " + getCacheDir().getPath() + Tags.FILE_DESTINATION_FOLDER);
        deleteDir(new File(getCacheDir().getPath() + File.separator + "1.zip"));
        deleteDir(new File(getCacheDir().getPath() + Tags.FILE_DESTINATION_FOLDER));
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sessionManager = new SessionManager(this);
        setContentView(R.layout.activity_home);
        /*Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));*/


        //Log.d("Loged in user ", new GsonBuilder().setPrettyPrinting().create().toJson(sessionManager.getUserDetails()));
        initToolbar();
        initNavigation();
        //showBottomNavigation(false);
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBack();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void onBack() {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_app_exit, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView tvMessage = popupView.findViewById(R.id.tvMessage);
        Button btnYes = popupView.findViewById(R.id.btnYes);
        Button btnNo = popupView.findViewById(R.id.btnNo);
        ImageButton imgBtnClose = popupView.findViewById(R.id.imgBtnClose);

        tvMessage.setText(getString(R.string.ad_back_button_pressed_msg));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finishAffinity();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.ad_home_button_pressed_msg))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Alert");
        alert.show();*/
    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        tvLearnLanguage = findViewById(R.id.atvHomeLearnLang);
        tvLearnLanguage.setVisibility(View.INVISIBLE);
        tvLearnLanguage.setText(sessionManager.getSpeakCode());
        setSupportActionBar(toolbar);
    }

    private void initNavigation() {

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavView = findViewById(R.id.bottom_nav_view);
        contentView = findViewById(R.id.content_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_change_password, R.id.nav_new_language,
                R.id.nav_payment, R.id.nav_share, R.id.nav_signout,
                R.id.bottom_lesson, R.id.bottom_target, R.id.bottom_leader, R.id.bottom_setting)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavView, navController);
        navigationView.getMenu().findItem(R.id.nav_signout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavView.setItemBackground(null);
                //Uncomment the below code to Set the message and title from the strings.xml file
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                //Setting message manually and performing action on button click
                builder.setMessage(getString(R.string.ad_signout_msg))
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                sessionManager.logoutUser();
                                //This is for maintaining the behavior of the Navigation view
                                NavigationUI.onNavDestinationSelected(item, navController);
                                //This is for closing the drawer after acting on it
                                drawer.closeDrawer(GravityCompat.START);
                                deleteDir(new File(getCacheDir().getPath() + File.separator + "1.zip"));
                                deleteDir(new File(getCacheDir().getPath() + Tags.FILE_DESTINATION_FOLDER));
                                Toast.makeText(getApplicationContext(), "Sign out Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                                drawer.closeDrawer(GravityCompat.START);

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Alert");
                alert.show();


                return true;
            }
        });
        navigationView.getMenu().findItem(R.id.nav_profile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavView.setItemBackground(null);
                navigationView.setItemBackground(getDrawable(R.drawable.nav_drawer_menu_item_bg));
                return false;
            }
        });
        if (sessionManager.getRegistrationType() != 1)
            navigationView.getMenu().findItem(R.id.nav_change_password).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_change_password).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavView.setItemBackground(null);
                navigationView.setItemBackground(getDrawable(R.drawable.nav_drawer_menu_item_bg));
                return false;
            }
        });
        navigationView.getMenu().findItem(R.id.nav_new_language).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavView.setItemBackground(null);
                navigationView.setItemBackground(getDrawable(R.drawable.nav_drawer_menu_item_bg));
                return false;
            }
        });
        navigationView.getMenu().findItem(R.id.nav_payment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavView.setItemBackground(null);
                navigationView.setItemBackground(getDrawable(R.drawable.nav_drawer_menu_item_bg));
                return false;
            }
        });

        navigationView.getMenu().findItem(R.id.nav_share).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.setItemBackground(null);
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Lantoon");
                    String shareMessage = "\nBuild language skills your own way - learn Spanish, French, Italian & more!\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                return false;
            }
        });
        bottomNavView.getMenu().findItem(R.id.bottom_lesson).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.setItemBackground(null);
                bottomNavView.setItemBackground(getDrawable(R.drawable.nav_bottom_bg_change_color));
                return false;
            }
        });
        bottomNavView.getMenu().findItem(R.id.bottom_target).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.setItemBackground(null);
                bottomNavView.setItemBackground(getDrawable(R.drawable.nav_bottom_bg_change_color));
                return false;
            }
        });
        bottomNavView.getMenu().findItem(R.id.bottom_leader).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.setItemBackground(null);
                bottomNavView.setItemBackground(getDrawable(R.drawable.nav_bottom_bg_change_color));
                return false;
            }
        });
        bottomNavView.getMenu().findItem(R.id.bottom_setting).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigationView.setItemBackground(null);
                bottomNavView.setItemBackground(getDrawable(R.drawable.nav_bottom_bg_change_color));
                return false;
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull @NotNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull @NotNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                setHeader();
            }
        });
        //animateNavigationDrawer();
        setHeader();
    }

    private void setHeader() {
        View view = navigationView.getHeaderView(0);
        ImageView ivNavHeaderUserImage = view.findViewById(R.id.ivNavHeaderUserImage);
        TextView tvNavHeaderUsername = view.findViewById(R.id.tvNavHeaderUsername);
        TextView tvNavHeaderUserId = view.findViewById(R.id.tvNavHeaderUserId);
        if (sessionManager != null) {
            tvNavHeaderUsername.setText(sessionManager.getUserName());
            tvNavHeaderUserId.setText(sessionManager.getUid());
            if (sessionManager.getProfilePic() != null) {

                Glide.with(this).load(sessionManager.getProfilePic()).circleCrop().addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ivNavHeaderUserImage.setBackground(getDrawable(R.drawable.icon_avatar));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(ivNavHeaderUserImage);

            }
        }
    }


    private void animateNavigationDrawer() {
//        drawerLayout.setScrimColor(getResources().getColor(R.color.text_brown));
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("code result" , String.valueOf(resultCode));
        Log.d("code req" , String.valueOf(requestCode));
        if (requestCode == 2) {
            navController.navigate(R.id.bottom_lesson);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      //  Log.d("code result" , String.valueOf(resultCode));
        Log.d("code req" , String.valueOf(requestCode));
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        try {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    System.out.println("Lantoon cache path children " + children[i]);
                    if (!success) {
                        return false;
                    }
                }
                return dir.delete();
            } else if (dir != null && dir.isFile()) {
                System.out.println("Lantoon cache path " + dir.getPath());
                return dir.delete();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
