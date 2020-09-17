package top.mymytan.taninegridview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import top.mymytan.library.TaNineGridView;

public class MainActivity extends AppCompatActivity {

    private TaNineGridView mNineGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNineGridView = findViewById(R.id.nine_grid_view);
        ArrayList<String> test = new ArrayList<>();
        Random random = new Random();
        int i = random.nextInt(10);
        for (int j = 0; j < i; j++) {
            test.add("");
        }
        mNineGridView.setItemViewEngine(new TaNineGridView.ItemViewEngine() {
            @Override
            public View createView() {
                ImageView view = new ImageView(getBaseContext());
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                view.setBackgroundColor(Color.parseColor("#ff0000"));
                return view;
            }
        });
        mNineGridView.setData(test);
    }
}