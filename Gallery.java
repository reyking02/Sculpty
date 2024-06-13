package com.ascon.subdivformer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import com.ascon.subdivformer.BillingUtil.IabHelper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class Gallery extends Activity implements SeekBar.OnSeekBarChangeListener {
    public static final String GALLERY_RESULT = "GALLERY_RESULT";
    public static final String PREVIEW_PATH = "PREVIEW_PATH";
    private static final int REQUEST_LOAD = 0;
    public static final String RESULT_PATH = "RESULT_PATH";
    File file_;
    public int rowSize_ = 0;
    public int colCount_ = 0;
    ImageButton selected_ = null;
    TableRow lastRow_ = null;
    public int galCount_ = 0;
    List<String> filesList_ = null;
    String samplesPath_ = null;
    protected String fileTag_ = null;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_gallery);
        this.samplesPath_ = getFilesDir().getAbsolutePath() + "/samples";
        File dir = getFilesDir();
        this.file_ = new File(dir, "gallery.txt");
        if (this.file_.exists()) {
            readGalleryFile();
        }
        this.selected_ = null;
    }

    public void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        while (true) {
            int len = in.read(buf);
            if (len > 0) {
                out.write(buf, 0, len);
            } else {
                in.close();
                out.close();
                return;
            }
        }
    }

    void readGalleryFile() {
        this.filesList_ = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("gallery.txt")));
            while (true) {
                String str = br.readLine();
                if (str == null) {
                    break;
                }
                this.filesList_.add(str);
            }
            br.close();
            this.galCount_ = this.filesList_.size();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        File samplesDir = new File(this.samplesPath_);
        if (samplesDir.exists()) {
            String[] files = samplesDir.list();
            for (String filename : files) {
                if (!filename.contains(".png")) {
                    String fName = this.samplesPath_ + "/" + filename + ";" + this.samplesPath_ + "/" + filename + ".png";
                    this.filesList_.add(fName);
                }
            }
        }
    }

    void addGalleryFile(String newFile) {
        getFilesDir();
        try {
            File previewFile = File.createTempFile("prw", ".png");
            addGalleryFile(newFile, previewFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addGalleryFile(String newFile, File previewFile) {
        try {
            File dir = getFilesDir();
            this.file_ = new File(dir, "gallery.txt");
            if (this.file_.exists()) {
                this.file_.delete();
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("gallery.txt", 2)));
            bw.write(newFile);
            bw.write(";");
            bw.write(previewFile.getAbsolutePath());
            getIntent().putExtra(PREVIEW_PATH, previewFile.getAbsolutePath());
            if (this.filesList_ != null) {
                for (int i = 0; i < this.filesList_.size(); i++) {
                    if (!this.filesList_.get(i).contains(this.samplesPath_)) {
                        bw.newLine();
                        bw.write(this.filesList_.get(i));
                    }
                }
            }
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    void delGalleryFile(String delFile) {
        try {
            File dir = getFilesDir();
            int delimIndex = delFile.indexOf(";");
            if (delimIndex != -1) {
                String tiffName = delFile.substring(delimIndex + 1, delFile.length());
                File previewFile = new File(tiffName);
                if (previewFile.exists()) {
                    previewFile.delete();
                }
                String fileName = delFile.substring(0, delimIndex);
                File modelFile = new File(fileName);
                if (modelFile.exists()) {
                    modelFile.delete();
                }
            }
            this.file_ = new File(dir, "gallery.txt");
            if (this.file_.exists()) {
                this.file_.delete();
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("gallery.txt", 2)));
            for (int i = 0; i < this.filesList_.size(); i++) {
                String lineToWrite = this.filesList_.get(i);
                if (lineToWrite.compareTo(delFile) != 0 && !this.filesList_.get(i).contains(this.samplesPath_)) {
                    bw.write(lineToWrite);
                    bw.newLine();
                }
            }
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void delSelectedFile() {
        ImageButton openButton = (ImageButton) findViewById(R.id.filterBtn);
        openButton.setVisibility(0);
        ImageButton delButton = (ImageButton) findViewById(R.id.deleteBtn);
        delButton.setVisibility(8);
        ImageButton exportButton = (ImageButton) findViewById(R.id.exportBtn);
        exportButton.setVisibility(8);
        ImageButton sendButton = (ImageButton) findViewById(R.id.sendBtn);
        sendButton.setVisibility(8);
        ImageButton copyButton = (ImageButton) findViewById(R.id.copyBtn);
        copyButton.setVisibility(8);
        LinearLayout renameLayout = (LinearLayout) findViewById(R.id.renameLayout);
        renameLayout.setVisibility(8);
        String fileTag = (String) this.selected_.getTag();
        delGalleryFile(fileTag);
        refreshGallery();
    }

    protected boolean isAdvancedExportWorked() {
        return SubDivFormer.isAdvancedExportWorked();
    }

    protected void showExportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View layout = getLayoutInflater().inflate(R.layout.export, (ViewGroup) null);
        dialog.setView(layout, 0, 0, 0, 0);
        dialog.requestWindowFeature(0);
        ImageButton buyButton = (ImageButton) layout.findViewById(R.id.buyButton);
        Drawable buttonDrw = getResources().getDrawable(R.drawable.sdf_advanced_export);
        buyButton.setImageDrawable(buttonDrw);
        ImageButton closeButton = (ImageButton) layout.findViewById(R.id.closeButton);
        buyButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                dialog.cancel();
                BuyExportTask buyTask = new BuyExportTask();
                buyTask.execute(BuildConfig.FLAVOR);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        RadioButton radio_ply = (RadioButton) layout.findViewById(R.id.radio_ply);
        RadioButton radio_3ds = (RadioButton) layout.findViewById(R.id.radio_3ds);
        RadioButton radio_dae = (RadioButton) layout.findViewById(R.id.radio_dae);
        RadioButton radio_obj = (RadioButton) layout.findViewById(R.id.radio_obj);
        RadioButton radio_off = (RadioButton) layout.findViewById(R.id.radio_off);
        SeekBar exportBar = (SeekBar) layout.findViewById(R.id.exportBar);
        if (isAdvancedExportWorked()) {
            radio_ply.setEnabled(true);
            radio_3ds.setEnabled(true);
            radio_dae.setEnabled(true);
            radio_obj.setEnabled(true);
            radio_off.setEnabled(true);
            exportBar.setVisibility(0);
            buyButton.setVisibility(8);
        }
        Button exportButton = (Button) layout.findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LinearLayout layout2 = (LinearLayout) v.getParent();
                RadioGroup radioFormat = (RadioGroup) layout2.findViewById(R.id.radioFormat);
                int checkedId = radioFormat.getCheckedRadioButtonId();
                RadioButton checkedRadioButton = (RadioButton) radioFormat.findViewById(checkedId);
                int checkedIndex = radioFormat.indexOfChild(checkedRadioButton);
                SeekBar exportBar2 = (SeekBar) layout2.findViewById(R.id.exportBar);
                int level = exportBar2.getProgress();
                int delimIndex = Gallery.this.fileTag_.indexOf(";");
                String inputFileName = Gallery.this.fileTag_.substring(0, delimIndex);
                String outputFileName = BuildConfig.FLAVOR;
                switch (checkedIndex) {
                    case 0:
                        outputFileName = inputFileName + ".stl";
                        break;
                    case 1:
                        outputFileName = inputFileName + ".ply";
                        break;
                    case 2:
                        outputFileName = inputFileName + ".3ds";
                        break;
                    case 3:
                        outputFileName = inputFileName + ".dae";
                        break;
                    case 4:
                        outputFileName = inputFileName + ".obj";
                        break;
                    case 5:
                        outputFileName = inputFileName + ".off";
                        break;
                }
                Gallery.this.fileTag_ = null;
                dialog.cancel();
                Gallery.this.exportModel(inputFileName, outputFileName, level);
            }
        });
        dialog.show();
    }

    protected void exportModel(String inputFileName, String outputFileName, int level) {
        if (!inputFileName.isEmpty() && !outputFileName.isEmpty()) {
            int res = SubDivFormer.exportFile(inputFileName.getBytes(), outputFileName.getBytes(), level);
            MediaScannerConnection.scanFile(this, new String[]{outputFileName}, null, null);
            if (outputFileName.endsWith(".3ds") && res > 32000) {
                Toast toast = Toast.makeText(getApplicationContext(), "Model too large. Can`t save. Set low export quality. " + res + " faces.", 1);
                toast.show();
                return;
            }
            Toast toast2 = Toast.makeText(getApplicationContext(), outputFileName + " saved.", 0);
            toast2.show();
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (this.file_.exists()) {
            this.galCount_ = this.filesList_.size();
        } else {
            this.galCount_ = 0;
        }
        ImageButton openButton = (ImageButton) findViewById(R.id.filterBtn);
        openButton.setVisibility(0);
        openButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Gallery.this.addFile();
            }
        });
        ImageButton delButton = (ImageButton) findViewById(R.id.deleteBtn);
        delButton.setVisibility(8);
        delButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Gallery.this.delSelectedFile();
            }
        });
        ImageButton exportButton = (ImageButton) findViewById(R.id.exportBtn);
        exportButton.setVisibility(8);
        exportButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Gallery.this.fileTag_ = (String) Gallery.this.selected_.getTag();
                Gallery.this.showExportDialog();
            }
        });
        ImageButton sendButton = (ImageButton) findViewById(R.id.sendBtn);
        sendButton.setVisibility(8);
        sendButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String str = (String) Gallery.this.selected_.getTag();
                int delimIndex = str.indexOf(";");
                String fileName = str.substring(0, delimIndex);
                Intent shareIntent = new Intent();
                shareIntent.setAction("android.intent.action.SEND");
                Uri fileUri = Uri.fromFile(new File(fileName));
                shareIntent.putExtra("android.intent.extra.STREAM", fileUri);
                shareIntent.setType("*/*");
                Gallery.this.startActivity(Intent.createChooser(shareIntent, "Send to..."));
            }
        });
        ImageButton copyButton = (ImageButton) findViewById(R.id.copyBtn);
        copyButton.setVisibility(8);
        copyButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String str = (String) Gallery.this.selected_.getTag();
                int delimIndex = str.indexOf(";");
                String fileName = str.substring(0, delimIndex);
                File fileFrom = new File(fileName);
                File EXTERNAL_DIR = Environment.getExternalStorageDirectory();
                String newPath = EXTERNAL_DIR.getAbsolutePath() + "/subdivformer/copy_" + fileFrom.getName();
                File fileNew = new File(newPath);
                String previewFileName = str.substring(delimIndex + 1);
                File oldPreview = new File(previewFileName);
                Gallery.this.getFilesDir();
                try {
                    Gallery.this.copyFile(fileFrom, fileNew);
                    File previewFile = File.createTempFile("prw", ".png");
                    Gallery.this.copyFile(oldPreview, previewFile);
                    Gallery.this.addGalleryFile(newPath, previewFile);
                    Gallery.this.refreshGallery();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        LinearLayout renameLayout = (LinearLayout) findViewById(R.id.renameLayout);
        renameLayout.setVisibility(8);
        if (this.selected_ != null) {
            this.selected_.setBackgroundResource(R.drawable.gallery_button);
        }
        this.selected_ = null;
        ImageButton renameButton = (ImageButton) findViewById(R.id.renameBtn);
        renameButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String str = (String) Gallery.this.selected_.getTag();
                int delimIndex = str.indexOf(";");
                String oldName = str.substring(0, delimIndex);
                String previewFileName = str.substring(delimIndex + 1);
                EditText renameText = (EditText) Gallery.this.findViewById(R.id.renameText);
                String newName = renameText.getText().toString();
                String newString = newName + ";" + previewFileName;
                if (newName.compareTo(oldName) != 0) {
                    File renamedFile = new File(oldName);
                    if (renamedFile.renameTo(new File(newName))) {
                        try {
                            File dir = Gallery.this.getFilesDir();
                            Gallery.this.file_ = new File(dir, "gallery.txt");
                            if (Gallery.this.file_.exists()) {
                                Gallery.this.file_.delete();
                            }
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Gallery.this.openFileOutput("gallery.txt", 2)));
                            if (Gallery.this.filesList_ != null) {
                                for (int i = 0; i < Gallery.this.filesList_.size(); i++) {
                                    if (Gallery.this.filesList_.get(i).compareTo(str) == 0) {
                                        bw.write(newString);
                                        Toast toast = Toast.makeText(Gallery.this.getApplicationContext(), newName + " saved", 0);
                                        toast.show();
                                    } else if (!Gallery.this.filesList_.get(i).contains(Gallery.this.samplesPath_)) {
                                        bw.write(Gallery.this.filesList_.get(i));
                                    }
                                    if (i < Gallery.this.filesList_.size() + 1) {
                                        bw.newLine();
                                    }
                                }
                            }
                            bw.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    Gallery.this.selected_ = null;
                    ImageButton openButton2 = (ImageButton) Gallery.this.findViewById(R.id.filterBtn);
                    ImageButton delButton2 = (ImageButton) Gallery.this.findViewById(R.id.deleteBtn);
                    ImageButton sendButton2 = (ImageButton) Gallery.this.findViewById(R.id.sendBtn);
                    ImageButton copyButton2 = (ImageButton) Gallery.this.findViewById(R.id.copyBtn);
                    ImageButton exportButton2 = (ImageButton) Gallery.this.findViewById(R.id.exportBtn);
                    LinearLayout renameLayout2 = (LinearLayout) Gallery.this.findViewById(R.id.renameLayout);
                    openButton2.setVisibility(0);
                    copyButton2.setVisibility(8);
                    delButton2.setVisibility(8);
                    exportButton2.setVisibility(8);
                    sendButton2.setVisibility(8);
                    renameLayout2.setVisibility(8);
                    Gallery.this.refreshGallery();
                }
            }
        });
        ScrollView tableScroll = (ScrollView) findViewById(R.id.tableScroll);
        if (hasFocus && tableScroll.getChildCount() == 0) {
            int tableHeight = tableScroll.getHeight();
            int tableWidth = tableScroll.getWidth();
            if (tableHeight < tableWidth) {
                this.colCount_ = 5;
                this.rowSize_ = tableWidth / this.colCount_;
            } else {
                this.colCount_ = 3;
                this.rowSize_ = tableWidth / this.colCount_;
            }
            TableLayout table = new TableLayout(this);
            table.setStretchAllColumns(false);
            table.setShrinkAllColumns(false);
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            this.lastRow_ = tableRow;
            addPlusButton();
            for (int i = 0; i < this.galCount_; i++) {
                if (this.lastRow_.getChildCount() >= this.colCount_) {
                    TableRow newRow = new TableRow(this);
                    table.addView(newRow);
                    this.lastRow_ = newRow;
                }
                addImage(this.filesList_.get(i), this.lastRow_);
            }
            tableScroll.addView(table);
        }
    }

    public void addFile() {
        startActivityForResult(new Intent(getBaseContext(), FileDialogActivity.class), 0);
    }

    public void addPlusButton() {
        ImageButton imageButton = new ImageButton(this);
        imageButton.setBackgroundResource(R.drawable.gallery_button);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Gallery.this.getIntent().putExtra(Gallery.GALLERY_RESULT, "NEW");
                Gallery.this.setResult(-1, Gallery.this.getIntent());
                Gallery.this.finish();
            }
        });
        imageButton.setImageResource(R.drawable.plus);
        this.lastRow_.addView(imageButton);
        imageButton.getLayoutParams().height = this.rowSize_;
        imageButton.getLayoutParams().width = this.rowSize_;
    }

    public void addTiffToButton(ImageButton imageButton, String tiffName) {
        File file = new File(tiffName);
        imageButton.setImageURI(Uri.fromFile(file));
        imageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    public void addImage(String filename, TableRow row) {
        ImageButton imageButton = new ImageButton(this);
        imageButton.setBackgroundResource(R.drawable.gallery_button);
        int delimIndex = filename.indexOf(";");
        if (delimIndex != -1) {
            String tiffName = filename.substring(delimIndex + 1, filename.length());
            File tiffFile = new File(tiffName);
            if (tiffFile.exists()) {
                addTiffToButton(imageButton, tiffName);
            }
        }
        imageButton.setTag(filename);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.Gallery.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String fileTag = (String) v.getTag();
                int delimIndex2 = fileTag.indexOf(";");
                String fileName = fileTag;
                if (delimIndex2 != -1) {
                    fileName = fileTag.substring(0, fileTag.indexOf(";"));
                }
                File openingFile = new File(fileName);
                if (openingFile.exists()) {
                    Gallery.this.getIntent().putExtra(Gallery.GALLERY_RESULT, "OPEN");
                    Gallery.this.getIntent().putExtra("RESULT_PATH", fileName);
                    Gallery.this.setResult(-1, Gallery.this.getIntent());
                    Gallery.this.finish();
                    return;
                }
                Toast.makeText(Gallery.this, "Файл " + fileName + " отсутствует!", 0).show();
            }
        });
        imageButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.ascon.subdivformer.Gallery.12
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View arg0) {
                ImageButton currentButton = (ImageButton) arg0;
                ImageButton openButton = (ImageButton) Gallery.this.findViewById(R.id.filterBtn);
                ImageButton delButton = (ImageButton) Gallery.this.findViewById(R.id.deleteBtn);
                ImageButton sendButton = (ImageButton) Gallery.this.findViewById(R.id.sendBtn);
                ImageButton copyButton = (ImageButton) Gallery.this.findViewById(R.id.copyBtn);
                ImageButton exportButton = (ImageButton) Gallery.this.findViewById(R.id.exportBtn);
                EditText renameText = (EditText) Gallery.this.findViewById(R.id.renameText);
                LinearLayout renameLayout = (LinearLayout) Gallery.this.findViewById(R.id.renameLayout);
                if (currentButton == Gallery.this.selected_) {
                    Gallery.this.selected_.setBackgroundResource(R.drawable.gallery_button);
                    Gallery.this.selected_ = null;
                    openButton.setVisibility(0);
                    delButton.setVisibility(8);
                    copyButton.setVisibility(8);
                    exportButton.setVisibility(8);
                    sendButton.setVisibility(8);
                    renameLayout.setVisibility(8);
                    return true;
                }
                if (Gallery.this.selected_ != null) {
                    Gallery.this.selected_.setBackgroundResource(R.drawable.gallery_button);
                }
                Gallery.this.selected_ = currentButton;
                Gallery.this.selected_.setBackgroundResource(R.drawable.gallery_button_light);
                openButton.setVisibility(8);
                delButton.setVisibility(0);
                copyButton.setVisibility(0);
                exportButton.setVisibility(0);
                sendButton.setVisibility(0);
                renameLayout.setVisibility(0);
                String str = (String) Gallery.this.selected_.getTag();
                int delimIndex2 = str.indexOf(";");
                String fileName = str.substring(0, delimIndex2);
                renameText.setText(fileName);
                return true;
            }
        });
        row.addView(imageButton);
        imageButton.getLayoutParams().height = this.rowSize_;
        imageButton.getLayoutParams().width = this.rowSize_;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.app.Activity
    public synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (requestCode == 0) {
                String filePath = data.getStringExtra("RESULT_PATH");
                String fileExt = GetFileExt(filePath);
                if (fileExt.equalsIgnoreCase("sdf") || fileExt.equalsIgnoreCase("ply") || fileExt.equalsIgnoreCase("om")) {
                    getIntent().putExtra(GALLERY_RESULT, "OPEN");
                    addFile(filePath);
                    getIntent().putExtra("RESULT_PATH", filePath);
                    setResult(-1, getIntent());
                    finish();
                }
            }
        } else if (resultCode == 0) {
        }
    }

    public void addFile(String filePath) {
        addGalleryFile(filePath);
        refreshGallery();
    }

    public void refreshGallery() {
        readGalleryFile();
        this.galCount_ = this.filesList_.size();
        ScrollView tableScroll = (ScrollView) findViewById(R.id.tableScroll);
        tableScroll.removeAllViews();
        int tableHeight = tableScroll.getHeight();
        int tableWidth = tableScroll.getWidth();
        if (tableHeight < tableWidth) {
            this.colCount_ = 5;
            this.rowSize_ = tableWidth / this.colCount_;
        } else {
            this.colCount_ = 3;
            this.rowSize_ = tableWidth / this.colCount_;
        }
        TableLayout table = new TableLayout(this);
        table.setStretchAllColumns(false);
        table.setShrinkAllColumns(false);
        TableRow tableRow = new TableRow(this);
        table.addView(tableRow);
        this.lastRow_ = tableRow;
        addPlusButton();
        for (int i = 0; i < this.galCount_; i++) {
            if (this.lastRow_.getChildCount() >= this.colCount_) {
                TableRow newRow = new TableRow(this);
                table.addView(newRow);
                this.lastRow_ = newRow;
            }
            addImage(this.filesList_.get(i), this.lastRow_);
        }
        tableScroll.addView(table);
    }

    public String GetFileExt(String FileName) {
        String ext = FileName.substring(FileName.lastIndexOf(".") + 1, FileName.length());
        return ext;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    public class UpdateInAppTask extends AsyncTask<String, Integer, Integer> {
        UpdateInAppTask() {
        }

        int getResponseCodeFromBundle(Bundle b) {
            Object o = b.get(IabHelper.RESPONSE_CODE);
            if (o == null) {
                return 0;
            }
            if (o instanceof Integer) {
                return ((Integer) o).intValue();
            }
            if (o instanceof Long) {
                return (int) ((Long) o).longValue();
            }
            throw new RuntimeException("Unexpected type for bundle response code: " + o.getClass().getName());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(String... params) {
            ArrayList<String> skuList = new ArrayList<>();
            skuList.add("advanced_export");
            skuList.add("advanced_toolset");
            Bundle querySkus = new Bundle();
            querySkus.putStringArrayList(IabHelper.GET_SKU_DETAILS_ITEM_LIST, skuList);
            try {
                if (SubDivFormer.mService != null) {
                    Bundle ownedItems = SubDivFormer.mService.getPurchases(3, Gallery.this.getPackageName(), IabHelper.ITEM_TYPE_INAPP, (String) null);
                    int responseOwnedItems = getResponseCodeFromBundle(ownedItems);
                    if (responseOwnedItems == 0) {
                        SubDivFormer.writeLog("Billing purchasesList");
                        ArrayList<String> purchasesList = ownedItems.getStringArrayList(IabHelper.RESPONSE_INAPP_ITEM_LIST);
                        Iterator<String> it = purchasesList.iterator();
                        while (it.hasNext()) {
                            String thisResponse = it.next();
                            if (thisResponse.contains("advanced_export")) {
                                SubDivFormer.advEx = 1;
                            }
                            if (thisResponse.contains("advanced_toolset")) {
                                SubDivFormer.advTS = 1;
                            }
                            SubDivFormer.writeLog("Billing purchasesList: " + thisResponse);
                            Log.i("INAPP_PURCHASE_LIST: ", thisResponse);
                        }
                    } else {
                        SubDivFormer.writeLog("Billing getPurchases: error");
                    }
                } else {
                    SubDivFormer.writeLog("Billing getPurchases: error mService == null");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    class BuyExportTask extends AsyncTask<String, Integer, Integer> {
        BuyExportTask() {
        }

        int getResponseCodeFromBundle(Bundle b) {
            Object o = b.get(IabHelper.RESPONSE_CODE);
            if (o == null) {
                return 0;
            }
            if (o instanceof Integer) {
                return ((Integer) o).intValue();
            }
            if (o instanceof Long) {
                return (int) ((Long) o).longValue();
            }
            throw new RuntimeException("Unexpected type for bundle response code: " + o.getClass().getName());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(String... params) {
            try {
                if (SubDivFormer.mService != null) {
                    Bundle buyIntentBundle = SubDivFormer.mService.getBuyIntent(3, Gallery.this.getPackageName(), "advanced_export", IabHelper.ITEM_TYPE_INAPP, BuildConfig.FLAVOR);
                    int response = getResponseCodeFromBundle(buyIntentBundle);
                    if (response == 0) {
                        PendingIntent pendingIntent = (PendingIntent) buyIntentBundle.getParcelable(IabHelper.RESPONSE_BUY_INTENT);
                        if (pendingIntent != null) {
                            Gallery gallery = Gallery.this;
                            IntentSender intentSender = pendingIntent.getIntentSender();
                            Intent intent = new Intent();
                            Integer num = 0;
                            int intValue = num.intValue();
                            Integer num2 = 0;
                            int intValue2 = num2.intValue();
                            Integer num3 = 0;
                            gallery.startIntentSenderForResult(intentSender, 1001, intent, intValue, intValue2, num3.intValue());
                            SubDivFormer.writeLog("BUY_INTENT: PendingIntent");
                        }
                    } else {
                        SubDivFormer.writeLog("BUY_INTENT: error " + String.valueOf(response));
                    }
                } else {
                    SubDivFormer.writeLog("BUY_INTENT: error mService == null");
                }
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer result) {
            super.onPostExecute((BuyExportTask) result);
            UpdateInAppTask updateTask = new UpdateInAppTask();
            updateTask.execute(BuildConfig.FLAVOR);
        }
    }
}
