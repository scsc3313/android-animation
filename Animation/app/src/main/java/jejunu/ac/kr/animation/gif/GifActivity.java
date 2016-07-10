package jejunu.ac.kr.animation.gif;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;

import jejunu.ac.kr.animation.R;

public class GifActivity extends AppCompatActivity implements View.OnClickListener{

    private GifImageView gifImageView;
    private Button startButton;
    private static final String TAG = "GifActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        gifImageView = (GifImageView) findViewById(R.id.gif_image_view);
        startButton = (Button) findViewById(R.id.gif_start_button);

        gifImageView.setOnAnimationStop(new GifImageView.OnAnimationStop() {
            @Override
            public void onAnimationStop() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GifActivity.this, "Animation stopped", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        startButton.setOnClickListener(this);

        new GifDataDownloader() {
            @Override
            protected void onPostExecute(final byte[] bytes) {
                gifImageView.setBytes(bytes);
                gifImageView.startAnimation();
                startButton.setText("STOP");
                Log.d(TAG, "GIF width is " + gifImageView.getGifWidth());
                Log.d(TAG, "GIF height is " + gifImageView.getGifHeight());
            }
        }.execute("http://katemobile.ru/tmp/sample3.gif");
    }

    @Override
    public void onClick(final View v) {
        if (v.equals(startButton)) {
            if (gifImageView.isAnimating()) {
                gifImageView.stopAnimation();
                startButton.setText("START");
                Log.d(TAG, "stopAnimation");
            } else {
                startButton.setText("STOP");
                gifImageView.startAnimation();
                Log.d(TAG, "startAnimation");
            }
        }
    }

    class GifDataDownloader extends AsyncTask<String, Void, byte[]> {
        private static final String TAG = "GifDataDownloader";

        @Override
        protected byte[] doInBackground(final String... params) {
            final String gifUrl = params[0];

            if (gifUrl == null)
                return null;

            try {
                return ByteArrayHttpClient.get(gifUrl);
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "GifDecode OOM: " + gifUrl, e);
                return null;
            }
        }
    }
}
