package xxf.com.customcartogram.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import xxf.com.customcartogram.R;
import xxf.com.customcartogram.view.AnnularView;

/**
 * author：xxf
 */
public class AnnularActivity extends Activity {
    private AnnularView annularView;
    private Float[] datas = new Float[]{20f, 50f, 70f, 100f, 10f, 30f};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annular);
        annularView=findViewById(R.id.annular);
        annularView.setDatas(datas,true);
    }
}
