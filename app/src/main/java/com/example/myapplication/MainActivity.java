package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    private final static String APP_LOG = "my_private_app_log";

    private static int flipsCount = 0;

    private TextView scoreView;

    private ConstraintLayout mainContainer;

    private ContentStorage contentStorage;

    private boolean firstCardChosen;

    private int previousImageResID;

    private int previousImageViewID;

    private Handler handler;

    private void checkMatchOfPictures(int viewID, int resID) {
        if (!firstCardChosen) {//first card
            firstCardChosen = true;
            previousImageResID = resID;
            previousImageViewID = viewID;
        } else {
            if (previousImageResID == resID && previousImageViewID != viewID) {//second if it is not himself and its same pic
                findViewById(previousImageViewID).setVisibility(View.INVISIBLE);
                findViewById(viewID).setVisibility(View.INVISIBLE);
            } else {//different cards
                ((ImageView) findViewById(viewID)).setImageResource(R.drawable.deck);
                ((ImageView) findViewById(previousImageViewID)).setImageResource(R.drawable.deck);
            }
            firstCardChosen = false;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (v instanceof ImageView) {
                final int viewID = v.getId();
                if (firstCardChosen && viewID == previousImageViewID) return;//if its same card do not update flips or turn
                updateFlipsCount();
                String coordinates = v.getTag().toString();

                String pictureName = contentStorage.getPictureNameByCoordinates(coordinates);
                final int resID = getResources().getIdentifier(pictureName, "drawable", getPackageName());
                ((ImageView) v).setImageResource(resID);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkMatchOfPictures(viewID, resID);//check for match
                    }
                }, 500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(APP_LOG, "onCreate");

        handler = new Handler();

        String picNames = getResources().getString(R.string.pictureNames);
        int verticalSize = getResources().getInteger(R.integer.vertical_size);
        int horizontalSize = getResources().getInteger(R.integer.horizontal_size);
        contentStorage = new ContentStorage(picNames, verticalSize, horizontalSize);

        mainContainer = findViewById(R.id.constraintLayoutForDecks);
        scoreView = findViewById(R.id.ScoreLabel);

        for (int i = 0; i < mainContainer.getChildCount(); i++) {
            View view = mainContainer.getChildAt(i);
            if (view instanceof ImageView) {
                view.setOnClickListener(onClickListener);
            }
        }

        firstCardChosen = false;
    }

    private void updateFlipsCount() {
        ++flipsCount;
        scoreView.setText("Flips: " + flipsCount);
    }
}
