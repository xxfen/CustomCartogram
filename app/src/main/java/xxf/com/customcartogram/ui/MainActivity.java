package xxf.com.customcartogram.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import xxf.com.customcartogram.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Btn_A(View v) {
        Intent intent = new Intent(this, AnnularActivity.class);
        startActivity(intent);
    }
}
