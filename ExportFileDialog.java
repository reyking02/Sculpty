package com.ascon.subdivformer;

import android.app.ListActivity;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.File;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class ExportFileDialog extends ListActivity {
    public static File EXTERNAL_DIR = Environment.getExternalStorageDirectory();
    public static final String RESULT_PATH = "RESULT_PATH";
    public static final String START_PATH = "START_PATH";
    private TextView myPath;
    private Button selectButton;
    private String root = "/";
    private EditText fNameEdit = null;
    private String currentPath = this.root;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(0, getIntent());
        setContentView(R.layout.savefiledialog);
        this.myPath = (TextView) findViewById(R.id.path);
        this.fNameEdit = (EditText) findViewById(R.id.fileName);
        this.selectButton = (Button) findViewById(R.id.fdButtonSelect);
        this.selectButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.ExportFileDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (ExportFileDialog.this.fNameEdit.getText().length() > 0) {
                    String saveFileName = ExportFileDialog.this.currentPath + "/" + ((Object) ExportFileDialog.this.fNameEdit.getText()) + ".stl";
                    ExportFileDialog.this.getIntent().putExtra("RESULT_PATH", saveFileName);
                    ExportFileDialog.this.setResult(-1, ExportFileDialog.this.getIntent());
                    ExportFileDialog.this.finish();
                }
            }
        });
        String startPath = EXTERNAL_DIR.getAbsolutePath() + "/subdivformer";
        File dir = new File(startPath);
        if (!dir.exists()) {
            dir.mkdirs();
            MediaScannerConnection.scanFile(this, new String[]{startPath}, null, null);
        }
        this.myPath.setText(((Object) getText(R.string.location)) + ": " + startPath);
        this.currentPath = startPath;
    }
}
