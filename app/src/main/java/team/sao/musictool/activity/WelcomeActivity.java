package team.sao.musictool.activity;

import android.app.Activity;
import android.os.Bundle;
import team.sao.musictool.MainActivity;
import team.sao.musictool.R;
import team.sao.musictool.util.IntentBuilder;
import team.sao.musictool.util.StatusBarUtil;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/9
 * \* Time: 13:17
 * \* Description:
 **/
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        StatusBarUtil.setStatusBarMode(this, false, R.color.color_netease_red);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new IntentBuilder().clazz(WelcomeActivity.this, MainActivity.class).build());
                        WelcomeActivity.this.finish();
                    }
                });
            }
        }).start();
    }


}
