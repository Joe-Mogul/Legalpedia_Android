package com.legalpedia.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.legalpedia.android.app.views.IntroFragment;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class DefaultIntro extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(IntroFragment.newInstance(R.layout.intro));
        addSlide(IntroFragment.newInstance(R.layout.intro2));
        addSlide(IntroFragment.newInstance(R.layout.intro3));
        addSlide(IntroFragment.newInstance(R.layout.intro4));

    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
        Toast.makeText(getApplicationContext(),getString(R.string.skip),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}

