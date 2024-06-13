package com.ascon.subdivformer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ascon.subdivformer.ColorPicker.ColorPicker;
import com.ascon.subdivformer.ColorPicker.SaturationBar;
import com.ascon.subdivformer.ColorPicker.ValueBar;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class SelectColor extends Activity {
    public static final String LAST_COLOR1 = "LAST_COLOR1";
    public static final String LAST_COLOR2 = "LAST_COLOR2";
    public static final String LAST_COLOR3 = "LAST_COLOR3";
    public static final String RESULT_COLOR = "RESULT_COLOR";
    Button lastColor1Btn = null;
    Button lastColor2Btn = null;
    Button lastColor3Btn = null;
    int lastColor1_ = 8421504;
    int lastColor2_ = 8421504;
    int lastColor3_ = 8421504;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_color);
        final ColorPicker picker = (ColorPicker) findViewById(R.id.ColorPicker);
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationBar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valueBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        picker.getColor();
        picker.setOldCenterColor(picker.getColor());
        picker.setShowOldCenterColor(false);
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SelectColor.this.setResult(0, SelectColor.this.getIntent());
                SelectColor.this.finish();
            }
        });
        Button buttonWhite = (Button) findViewById(R.id.colorWhite);
        buttonWhite.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(-1);
            }
        });
        Button buttonBlack = (Button) findViewById(R.id.colorBlack);
        buttonBlack.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(-16777216);
            }
        });
        Button buttonGray = (Button) findViewById(R.id.colorGray);
        buttonGray.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(-8355712);
            }
        });
        Button buttonRed = (Button) findViewById(R.id.colorRed);
        buttonRed.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(-65536);
            }
        });
        Button buttonGreen = (Button) findViewById(R.id.colorGreen);
        buttonGreen.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(-16711936);
            }
        });
        Button buttonBlue = (Button) findViewById(R.id.colorBlue);
        buttonBlue.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(-16776961);
            }
        });
        Button buttonYellow = (Button) findViewById(R.id.colorYellow);
        buttonYellow.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(-256);
            }
        });
        Intent intent = getIntent();
        this.lastColor1_ = intent.getIntExtra(LAST_COLOR1, -8355712);
        this.lastColor2_ = intent.getIntExtra(LAST_COLOR2, -8355712);
        this.lastColor3_ = intent.getIntExtra(LAST_COLOR3, -8355712);
        this.lastColor1Btn = (Button) findViewById(R.id.lastColor1);
        this.lastColor1Btn.setBackgroundColor(this.lastColor1_ | (-16777216));
        this.lastColor1Btn.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(SelectColor.this.lastColor1_);
            }
        });
        this.lastColor2Btn = (Button) findViewById(R.id.lastColor2);
        this.lastColor2Btn.setBackgroundColor(this.lastColor2_ | (-16777216));
        this.lastColor2Btn.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(SelectColor.this.lastColor2_);
            }
        });
        this.lastColor3Btn = (Button) findViewById(R.id.lastColor3);
        this.lastColor3Btn.setBackgroundColor(this.lastColor3_ | (-16777216));
        this.lastColor3Btn.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                picker.setColor(SelectColor.this.lastColor3_);
            }
        });
        Button buttonOk = (Button) findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SelectColor.12
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ColorPicker picker2 = (ColorPicker) SelectColor.this.findViewById(R.id.ColorPicker);
                int color = picker2.getColor();
                SelectColor.this.getIntent().putExtra(SelectColor.RESULT_COLOR, color);
                SelectColor.this.setResult(-1, SelectColor.this.getIntent());
                SelectColor.this.finish();
            }
        });
    }
}
