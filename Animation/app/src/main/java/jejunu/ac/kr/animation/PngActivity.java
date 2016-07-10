package jejunu.ac.kr.animation;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class PngActivity extends AppCompatActivity {

    private static final String TAG = "PngActivity";

    private ImageSwitcher imageSwitcher;
    private Handler handler = new Handler();
    private Thread thread;
    private boolean running;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_png);

        imageSwitcher = (ImageSwitcher) findViewById(R.id.image_switcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setBackgroundColor(0xFFFFFFFF);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation();
            }
        });

        findViewById(R.id.stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAnimation();
            }
        });
    }


    private void startAnimation() {
        imageSwitcher.setVisibility(View.VISIBLE);

        thread = new ImageThread();
        thread.start();
    }

    private void stopAnimation() {

        running = false;

        try {
            thread.join();
        } catch (InterruptedException ex) {
            Log.e(TAG, ex.toString());
        }

        imageSwitcher.setVisibility(View.INVISIBLE);
    }

    private class ImageThread extends Thread {

        int duration = 100;
        final int imageId[] = {
                R.drawable.number1,
                R.drawable.number2,
                R.drawable.number3,
                R.drawable.number4,
                R.drawable.number5,
                R.drawable.number6,
                R.drawable.number7
        };

        int currentIndex = 0;

        @Override
        public void run() {
            running = true;
            Log.d(TAG, "START");
            while (running) {
                synchronized (this) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageSwitcher.setImageResource(imageId[currentIndex]);
                        }
                    });

                    currentIndex++;
                    if (currentIndex == imageId.length) {
                        currentIndex = 0;
                    }

                    try {
                        Thread.sleep(duration);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }

        }
    }
}
