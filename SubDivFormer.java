package com.ascon.subdivformer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.android.vending.billing.IInAppBillingService;
import com.ascon.subdivformer.BillingUtil.IabHelper;
import com.ascon.subdivformer.MeshRender;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class SubDivFormer extends Activity {
    public static final boolean DEV_MODE = false;
    private static final int GET_COLOR = 3;
    private static final int REQUEST_ADD = 4;
    private static final int REQUEST_EXPORT = 3;
    private static final int REQUEST_LOAD = 1;
    private static final int REQUEST_SAVE = 2;
    public static int advEx = 0;
    public static int advTS = 0;
    static IInAppBillingService mService;
    private TouchSurfaceView mGLSurfaceView_;
    private Mesh model_;
    private MeshRender render_;
    public int lastColor1 = 8421504;
    public int lastColor2 = 8421504;
    public int lastColor3 = 8421504;
    ServiceConnection mServiceConn = new ServiceConnection() { // from class: com.ascon.subdivformer.SubDivFormer.1
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            SubDivFormer.mService = null;
            SubDivFormer.writeLog("IInAppBillingService Disconnect");
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            SubDivFormer.mService = IInAppBillingService.Stub.asInterface(service);
            SubDivFormer.writeLog("IInAppBillingService Connect");
            UpdateInAppTask skuTask = new UpdateInAppTask();
            skuTask.execute(BuildConfig.FLAVOR);
        }
    };
    private int colorR = 127;
    private int colorG = 127;
    private int colorB = 127;
    public boolean showGuides = true;
    public boolean showColors = true;
    public boolean showButtonsText = true;
    public int rotateStep_ = 5;
    public int moveStep_ = 5;
    public int scaleStep_ = 5;
    public boolean toolState_ = false;
    public boolean colorizeState_ = false;
    public boolean pickColorState_ = false;
    public state state_ = state.navigate;
    private SelectMode selectMode_ = SelectMode.face;
    private String fileName_ = null;
    private String newPreviewPath_ = null;
    private String newFilePath_ = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    public enum SelectMode {
        face,
        edge,
        vertex
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    public enum state {
        navigate,
        moveX,
        moveY,
        moveZ,
        moveN,
        moveScreen,
        rotateX,
        rotateY,
        rotateZ,
        rotateN,
        rotateScreen,
        scaleX,
        scaleY,
        scaleZ,
        scaleAll
    }

    public static native String addFile(byte[] bArr);

    public static native void clearModel();

    public static native int exportFile(byte[] bArr, byte[] bArr2, int i);

    public static native float[] getDrawModelGabarit();

    public static native String loadFile(byte[] bArr, int i);

    public static native int saveFile(byte[] bArr, int i);

    public native void addCube();

    public native void addCube9x9();

    public native void addCylinder();

    public native void addGenus();

    public native void addIntersect();

    public native void addTorus();

    public native void alignSelectionN();

    public native void alignSelectionX();

    public native void alignSelectionY();

    public native void alignSelectionZ();

    public native int bevelSelection();

    public native int bridgeSelection();

    public native int bumpSelection();

    public native void clearSelection();

    public native int collapseSelection();

    public native void colorizeSelection();

    public native void copy();

    public native int createMirror();

    public native int createMirrorByPoints(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    public native int deleteSelection();

    public native int extractSelection();

    public native int extrudeSelection();

    public native ByteBuffer getBuf(float f, int i);

    public native int getColorFromSelection();

    public native ByteBuffer getIndexArray();

    public native int getIndexCount();

    public native ByteBuffer getNormalArray();

    public native int getNormalCount();

    public native ByteBuffer getVertexArray();

    public native int getVertexCount();

    public native int increaseSelection();

    public native int intrudeSelection();

    public native int mergeSelection();

    public native int mirrorModeByPointsOn(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    public native int mirrorModeOff();

    public native int mirrorModeOn();

    public native void moveSelection(float f, float f2, float f3);

    public native void moveSelectionN(float f);

    public native void paste();

    public native int redo();

    public native int reverseSelection();

    public native void rotateSelection(float f, float f2, float f3);

    public native void rotateSelectionN(float f);

    public native void scaleSelection(float f, float f2, float f3);

    public native void selectByColor();

    public native int selectRegion();

    public native void setColorizeMode(boolean z, int i, int i2, int i3);

    public native void setDrawColorMode(boolean z);

    public native void setDrawGuidesMode(boolean z);

    public native void setMultiSelect(int i);

    public native void setPickColorMode(boolean z);

    public native void setSelectMode(int i);

    public native void setToolMode(boolean z);

    public native int splitSelection();

    public native int startModeller();

    public native int stripSelection();

    public native int subdivideSelection();

    public native int undo();

    public native void updateUndoContainer();

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.subdivformer);
        createCommandGroups();
        showMainButtons();
        writeLog("\nStart new session");
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, this.mServiceConn, 1);
        this.model_ = (Mesh) getLastNonConfigurationInstance();
        if (this.model_ != null) {
            Log.d("SubDivFormer", "load model: " + this.model_.hashCode());
        }
        if (this.model_ == null) {
            this.model_ = new Mesh();
            File EXTERNAL_DIR = Environment.getExternalStorageDirectory();
            String startPath = EXTERNAL_DIR.getAbsolutePath() + "/subdivformer";
            File sdfDir = new File(startPath);
            if (!sdfDir.exists()) {
                sdfDir.mkdir();
            }
            Date curDate = new Date();
            this.fileName_ = startPath + "/sdf_" + curDate.toString() + ".sdf";
            Log.d("SubDivFormer", "new model: " + this.model_.hashCode());
        }
        Boolean needLoadLastModel = false;
        float renderScale = 1.0f;
        float[] modelMtr_ = null;
        float[] viewMtr_ = null;
        float[] navView_ = null;
        float[] projection_ = null;
        if (savedInstanceState != null) {
            this.fileName_ = savedInstanceState.getString("filename");
            this.showColors = savedInstanceState.getBoolean("showColors");
            this.showGuides = savedInstanceState.getBoolean("showGuides");
            this.showButtonsText = savedInstanceState.getBoolean("showButtonsText");
            renderScale = savedInstanceState.getFloat("scale_", 1.0f);
            modelMtr_ = savedInstanceState.getFloatArray("modelMtr_");
            viewMtr_ = savedInstanceState.getFloatArray("viewMtr_");
            navView_ = savedInstanceState.getFloatArray("navView_");
            projection_ = savedInstanceState.getFloatArray("projection_");
            this.rotateStep_ = savedInstanceState.getInt("rotateStep_");
            this.moveStep_ = savedInstanceState.getInt("moveStep_");
            this.scaleStep_ = savedInstanceState.getInt("scaleStep_");
        } else {
            copyAssets();
            System.loadLibrary("3ds");
            System.loadLibrary("openmesh");
            System.loadLibrary("sdfmeshlib");
            System.loadLibrary("sdfcore");
            if (this.model_.triangleCount_ == 0) {
                startModeller();
                needLoadLastModel = true;
            }
        }
        initRender();
        this.render_.scale_ = renderScale;
        if (modelMtr_ != null) {
            this.render_.modelMtr_ = modelMtr_;
        }
        if (viewMtr_ != null) {
            this.render_.viewMtr_ = viewMtr_;
        }
        if (navView_ != null) {
            this.render_.navView_ = navView_;
        }
        if (projection_ != null) {
            this.render_.projection_ = projection_;
        }
        this.mGLSurfaceView_.setDrawingCacheEnabled(true);
        createRightButtons();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        BitmapFactory.decodeResource(getResources(), R.drawable.add);
        if (needLoadLastModel.booleanValue()) {
            loadLastModel();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(this.mServiceConn);
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
    }

    public void copyAssets() {
        InputStream in;
        OutputStream out;
        InputStream in2;
        OutputStream out2;
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("models");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        String modelsPath = getFilesDir().getAbsolutePath() + "/models";
        File modelsDir = new File(modelsPath);
        if (!modelsDir.exists()) {
            modelsDir.mkdir();
        }
        for (String filename : files) {
            File outFile = new File(modelsPath, filename);
            Log.i("Copy file: ", outFile.getAbsolutePath());
            if (!outFile.exists()) {
                try {
                    in2 = assetManager.open("models/" + filename);
                    out2 = new FileOutputStream(outFile);
                } catch (IOException e2) {
                    e = e2;
                }
                try {
                    copyFile(in2, out2);
                    in2.close();
                    out2.flush();
                    out2.close();
                } catch (IOException e3) {
                    e = e3;
                    Log.e("tag", "Failed to copy asset file: " + filename, e);
                }
            }
        }
        try {
            files = assetManager.list("samples");
        } catch (IOException e4) {
            Log.e("tag", "Failed to get asset file list.", e4);
        }
        String samplesPath = getFilesDir().getAbsolutePath() + "/samples";
        File samplesDir = new File(samplesPath);
        if (!samplesDir.exists()) {
            samplesDir.mkdir();
        }
        for (String filename2 : files) {
            File outFile2 = new File(samplesPath, filename2);
            Log.i("Copy file: ", outFile2.getAbsolutePath());
            if (!outFile2.exists()) {
                try {
                    in = assetManager.open("samples/" + filename2);
                    out = new FileOutputStream(outFile2);
                } catch (IOException e5) {
                    e = e5;
                }
                try {
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e6) {
                    e = e6;
                    Log.e("tag", "Failed to copy asset file: " + filename2, e);
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int read = in.read(buffer);
            if (read != -1) {
                out.write(buffer, 0, read);
            } else {
                return;
            }
        }
    }

    public void fullScreenOff() {
        View topPanel = findViewById(R.id.topPanel);
        topPanel.setVisibility(0);
        View bottmPanel = findViewById(R.id.bottomPanel);
        bottmPanel.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawFreeRAM() {
    }

    public void openColorDialog() {
        Intent selectColorIntent = new Intent(getBaseContext(), SelectColor.class);
        selectColorIntent.putExtra(SelectColor.LAST_COLOR1, this.lastColor1);
        selectColorIntent.putExtra(SelectColor.LAST_COLOR2, this.lastColor2);
        selectColorIntent.putExtra(SelectColor.LAST_COLOR3, this.lastColor3);
        writeLog("startActivityForResult: GET_COLOR");
        startActivityForResult(selectColorIntent, 3);
    }

    private void createRightButtons() {
        drawFreeRAM();
        Button showAllButton = (Button) findViewById(R.id.showall);
        showAllButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showAll();
            }
        });
        Button fullScreenButton = (Button) findViewById(R.id.fullScreen);
        fullScreenButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                View topPanel = SubDivFormer.this.findViewById(R.id.topPanel);
                topPanel.setVisibility(8);
                View bottmPanel = SubDivFormer.this.findViewById(R.id.bottomPanel);
                bottmPanel.setVisibility(8);
            }
        });
        Button undoButton = (Button) findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.undo() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.undo_error, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button redoButton = (Button) findViewById(R.id.redo);
        redoButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.redo() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.undo_error, 0);
                toast.show();
            }
        });
        Button editcopyButton = (Button) findViewById(R.id.editcopy);
        editcopyButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.copy();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button editpasteButton = (Button) findViewById(R.id.editpaste);
        editpasteButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.paste();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button increaseButton = (Button) findViewById(R.id.increaseselection);
        increaseButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.increaseSelection();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button reverseButton = (Button) findViewById(R.id.reverseselection);
        reverseButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.reverseSelection();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button stripSelectionButton = (Button) findViewById(R.id.stripselection);
        stripSelectionButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.stripSelection();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button selectRegionButton = (Button) findViewById(R.id.selectregion);
        selectRegionButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.selectRegion();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button selectByColorButton = (Button) findViewById(R.id.ColorSelect);
        selectByColorButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.12
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!SubDivFormer.this.isToolsWorked()) {
                    SubDivFormer.this.showBuyToolsDialog();
                    return;
                }
                SubDivFormer.this.selectByColor();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button upButton = (Button) findViewById(R.id.rotate_up);
        upButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.13
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.rotate(0.0f, 10.0f);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button downButton = (Button) findViewById(R.id.rotate_down);
        downButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.14
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.rotate(0.0f, -10.0f);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button rightButton = (Button) findViewById(R.id.rotate_right);
        rightButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.15
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.rotate(10.0f, 0.0f);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button leftButton = (Button) findViewById(R.id.rotate_left);
        leftButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.16
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.rotate(-10.0f, 0.0f);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button enlargeButton = (Button) findViewById(R.id.enlarge);
        enlargeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.17
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.scale(1.0f, 0.25f);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button reduceButton = (Button) findViewById(R.id.reduce);
        reduceButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.18
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.scale(1.0f, -0.25f);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button moveUpButton = (Button) findViewById(R.id.move_up);
        moveUpButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.19
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.move(0.0f, 10.0f);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button moveDownButton = (Button) findViewById(R.id.move_down);
        moveDownButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.20
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.move(0.0f, -10.0f);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button moveRightButton = (Button) findViewById(R.id.move_right);
        moveRightButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.21
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.move(10.0f, 0.0f);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button selectColorButton = (Button) findViewById(R.id.SelectColor);
        selectColorButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.22
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!SubDivFormer.this.isToolsWorked()) {
                    SubDivFormer.this.showBuyToolsDialog();
                } else {
                    SubDivFormer.this.openColorDialog();
                }
            }
        });
        ToggleButton pickColorButton = (ToggleButton) findViewById(R.id.PickColor);
        pickColorButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.23
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.isToolsWorked()) {
                    SubDivFormer.this.pickColorState_ = SubDivFormer.this.pickColorState_ ? false : true;
                    SubDivFormer.this.setPickColorMode(SubDivFormer.this.pickColorState_);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                ((ToggleButton) v).setChecked(false);
                SubDivFormer.this.showBuyToolsDialog();
            }
        });
        Button colorizeSelectionButton = (Button) findViewById(R.id.ColorizeSelected);
        colorizeSelectionButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.24
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!SubDivFormer.this.isToolsWorked()) {
                    SubDivFormer.this.showBuyToolsDialog();
                    return;
                }
                SubDivFormer.this.colorizeSelection();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        ToggleButton drawColorButton = (ToggleButton) findViewById(R.id.DrawColor);
        drawColorButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.25
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.isToolsWorked()) {
                    if (((ToggleButton) v).isChecked()) {
                        SubDivFormer.this.updateUndoContainer();
                        SubDivFormer.this.colorizeState_ = true;
                        SubDivFormer.this.setColorizeMode(SubDivFormer.this.colorizeState_, SubDivFormer.this.colorR, SubDivFormer.this.colorG, SubDivFormer.this.colorB);
                    } else {
                        SubDivFormer.this.colorizeState_ = false;
                        SubDivFormer.this.setColorizeMode(SubDivFormer.this.colorizeState_, SubDivFormer.this.colorR, SubDivFormer.this.colorG, SubDivFormer.this.colorB);
                    }
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                ((ToggleButton) v).setChecked(false);
                SubDivFormer.this.showBuyToolsDialog();
            }
        });
        Button topViewButton = (Button) findViewById(R.id.topBtn);
        topViewButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.26
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.viewMode_ = MeshRender.ViewMode.top;
                SubDivFormer.this.render_.showAll();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button leftViewButton = (Button) findViewById(R.id.leftBtn);
        leftViewButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.27
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.viewMode_ = MeshRender.ViewMode.left;
                SubDivFormer.this.render_.showAll();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button frontViewButton = (Button) findViewById(R.id.frontBtn);
        frontViewButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.28
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.viewMode_ = MeshRender.ViewMode.front;
                SubDivFormer.this.render_.showAll();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button freeViewButton = (Button) findViewById(R.id.freeBtn);
        freeViewButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.29
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.render_.viewMode_ = MeshRender.ViewMode.iso;
                SubDivFormer.this.render_.showAll();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
    }

    public void setDrawColor(int color) {
        ToggleButton pickColorButton = (ToggleButton) findViewById(R.id.PickColor);
        pickColorButton.setChecked(false);
        this.pickColorState_ = false;
        setPickColorMode(this.pickColorState_);
        this.colorR = (16711680 & color) >>> 16;
        this.colorG = (65280 & color) >>> 8;
        this.colorB = color & 255;
        setColorizeMode(this.colorizeState_, this.colorR, this.colorG, this.colorB);
        this.mGLSurfaceView_.requestRender();
        drawFreeRAM();
    }

    protected void initRender() {
        this.mGLSurfaceView_ = (TouchSurfaceView) findViewById(R.id.surface);
        this.mGLSurfaceView_.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.mGLSurfaceView_.getHolder().setFormat(-3);
        this.render_ = new MeshRender(this.model_, this);
        this.mGLSurfaceView_.initRenderer(this.render_);
        this.mGLSurfaceView_.getHolder().setFormat(-3);
        this.mGLSurfaceView_.requestFocus();
        this.mGLSurfaceView_.setFocusableInTouchMode(true);
        this.mGLSurfaceView_.setZOrderOnTop(true);
        this.mGLSurfaceView_.setRenderMode(0);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.render_.btnZoom = Math.round(metrics.density);
        updateSettings();
        drawFreeRAM();
    }

    protected void createCommandGroups() {
        createMainButtons();
        createStorageButtons();
        createAddButtons();
        createShapeButtons();
        createTransformButtons();
        createLocalButtons();
        createMoveButtons();
        createRotateButtons();
        createScaleButtons();
        createAlignButtons();
        unCheckToolButtons();
    }

    protected void unCheckToolButtons() {
        ToggleButton ScaleXButton = (ToggleButton) findViewById(R.id.ScaleXButton);
        ScaleXButton.setChecked(false);
        ToggleButton ScaleYButton = (ToggleButton) findViewById(R.id.ScaleYButton);
        ScaleYButton.setChecked(false);
        ToggleButton ScaleZButton = (ToggleButton) findViewById(R.id.ScaleZButton);
        ScaleZButton.setChecked(false);
        ToggleButton ScaleAllButton = (ToggleButton) findViewById(R.id.ScaleAllButton);
        ScaleAllButton.setChecked(false);
        ToggleButton MoveXButton = (ToggleButton) findViewById(R.id.MoveXButton);
        MoveXButton.setChecked(false);
        ToggleButton MoveYButton = (ToggleButton) findViewById(R.id.MoveYButton);
        MoveYButton.setChecked(false);
        ToggleButton MoveZButton = (ToggleButton) findViewById(R.id.MoveZButton);
        MoveZButton.setChecked(false);
        ToggleButton MoveNButton = (ToggleButton) findViewById(R.id.MoveNButton);
        MoveNButton.setChecked(false);
        ToggleButton MoveScreenButton = (ToggleButton) findViewById(R.id.MoveScreenButton);
        MoveScreenButton.setChecked(false);
        ToggleButton RotateXButton = (ToggleButton) findViewById(R.id.RotateXButton);
        RotateXButton.setChecked(false);
        ToggleButton RotateYButton = (ToggleButton) findViewById(R.id.RotateYButton);
        RotateYButton.setChecked(false);
        ToggleButton RotateZButton = (ToggleButton) findViewById(R.id.RotateZButton);
        RotateZButton.setChecked(false);
        ToggleButton RotateNButton = (ToggleButton) findViewById(R.id.RotateNButton);
        RotateNButton.setChecked(false);
        ToggleButton RotateScreenButton = (ToggleButton) findViewById(R.id.RotateScreenButton);
        RotateScreenButton.setChecked(false);
        this.state_ = state.navigate;
    }

    private void createAlignButtons() {
        Button BackButton = (Button) findViewById(R.id.AlignBackButton);
        BackButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.30
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.hideButtons();
                View buttons = SubDivFormer.this.findViewById(R.id.transformButtons);
                buttons.setVisibility(0);
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.unCheckToolButtons();
            }
        });
        Button nButton = (Button) findViewById(R.id.AlignNButton);
        nButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.31
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.alignSelectionN();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
            }
        });
        Button xButton = (Button) findViewById(R.id.AlignXButton);
        xButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.32
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.alignSelectionX();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
            }
        });
        Button yButton = (Button) findViewById(R.id.AlignYButton);
        yButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.33
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.alignSelectionY();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
            }
        });
        Button zButton = (Button) findViewById(R.id.AlignZButton);
        zButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.34
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.alignSelectionZ();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
            }
        });
    }

    private void createScaleButtons() {
        Button BackButton = (Button) findViewById(R.id.ScaleBackButton);
        BackButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.35
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.unCheckToolButtons();
                SubDivFormer.this.hideButtons();
                View buttons = SubDivFormer.this.findViewById(R.id.transformButtons);
                buttons.setVisibility(0);
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
            }
        });
        ToggleButton allButton = (ToggleButton) findViewById(R.id.ScaleAllButton);
        allButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.36
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.scaleAll;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        ToggleButton xButton = (ToggleButton) findViewById(R.id.ScaleXButton);
        xButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.37
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.scaleX;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        ToggleButton yButton = (ToggleButton) findViewById(R.id.ScaleYButton);
        yButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.38
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.scaleY;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        ToggleButton zButton = (ToggleButton) findViewById(R.id.ScaleZButton);
        zButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.39
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.scaleZ;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
    }

    private void createRotateButtons() {
        Button BackButton = (Button) findViewById(R.id.RotateBackButton);
        BackButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.40
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.unCheckToolButtons();
                SubDivFormer.this.hideButtons();
                View buttons = SubDivFormer.this.findViewById(R.id.transformButtons);
                buttons.setVisibility(0);
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
            }
        });
        ToggleButton screenButton = (ToggleButton) findViewById(R.id.RotateScreenButton);
        screenButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.41
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.rotateScreen;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        ToggleButton nButton = (ToggleButton) findViewById(R.id.RotateNButton);
        nButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.42
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.rotateN;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        ToggleButton xButton = (ToggleButton) findViewById(R.id.RotateXButton);
        xButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.43
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.rotateX;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        ToggleButton yButton = (ToggleButton) findViewById(R.id.RotateYButton);
        yButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.44
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.rotateY;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        Button zButton = (Button) findViewById(R.id.RotateZButton);
        zButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.45
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.rotateZ;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
    }

    private void createMoveButtons() {
        Button BackButton = (Button) findViewById(R.id.MoveBackButton);
        BackButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.46
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.unCheckToolButtons();
                SubDivFormer.this.hideButtons();
                View buttons = SubDivFormer.this.findViewById(R.id.transformButtons);
                buttons.setVisibility(0);
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
            }
        });
        Button screenButton = (Button) findViewById(R.id.MoveScreenButton);
        screenButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.47
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.moveScreen;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        Button normalButton = (Button) findViewById(R.id.MoveNButton);
        normalButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.48
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.moveN;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        Button xButton = (Button) findViewById(R.id.MoveXButton);
        xButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.49
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.moveX;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        Button yButton = (Button) findViewById(R.id.MoveYButton);
        yButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.50
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.moveY;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        Button zButton = (Button) findViewById(R.id.MoveZButton);
        zButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.51
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    SubDivFormer.this.unCheckToolButtons();
                    ((ToggleButton) v).setChecked(true);
                    SubDivFormer.this.updateUndoContainer();
                    SubDivFormer.this.toolState_ = true;
                    SubDivFormer.this.changeToolState();
                    SubDivFormer.this.state_ = state.moveZ;
                    return;
                }
                SubDivFormer.this.toolState_ = false;
                SubDivFormer.this.changeToolState();
                SubDivFormer.this.state_ = state.navigate;
            }
        });
        Button selectModeButton = (Button) findViewById(R.id.SelectTypeButton);
        selectModeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.52
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.changeSelectMode();
            }
        });
        Button mirrorCommandButton = (Button) findViewById(R.id.MirrorCommand);
        mirrorCommandButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.53
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!SubDivFormer.this.isToolsWorked()) {
                    SubDivFormer.this.showBuyToolsDialog();
                    return;
                }
                SubDivFormer.this.createMirror();
                float[] array6 = SubDivFormer.getDrawModelGabarit();
                SubDivFormer.this.render_.setDrawGabarit(array6);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button mirrorR1CommandButton = (Button) findViewById(R.id.MirrorR1);
        mirrorR1CommandButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.54
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.createMirrorByPoints(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f);
                float[] array6 = SubDivFormer.getDrawModelGabarit();
                SubDivFormer.this.render_.setDrawGabarit(array6);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button mirrorR2CommandButton = (Button) findViewById(R.id.MirrorR2);
        mirrorR2CommandButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.55
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.createMirrorByPoints(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                float[] array6 = SubDivFormer.getDrawModelGabarit();
                SubDivFormer.this.render_.setDrawGabarit(array6);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button mirrorG1CommandButton = (Button) findViewById(R.id.MirrorG1);
        mirrorG1CommandButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.56
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.createMirrorByPoints(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f);
                float[] array6 = SubDivFormer.getDrawModelGabarit();
                SubDivFormer.this.render_.setDrawGabarit(array6);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button mirrorG2CommandButton = (Button) findViewById(R.id.MirrorG2);
        mirrorG2CommandButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.57
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.createMirrorByPoints(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                float[] array6 = SubDivFormer.getDrawModelGabarit();
                SubDivFormer.this.render_.setDrawGabarit(array6);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button mirrorB1CommandButton = (Button) findViewById(R.id.MirrorB1);
        mirrorB1CommandButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.58
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.createMirrorByPoints(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f);
                float[] array6 = SubDivFormer.getDrawModelGabarit();
                SubDivFormer.this.render_.setDrawGabarit(array6);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button mirrorB2CommandButton = (Button) findViewById(R.id.MirrorB2);
        mirrorB2CommandButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.59
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.createMirrorByPoints(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                float[] array6 = SubDivFormer.getDrawModelGabarit();
                SubDivFormer.this.render_.setDrawGabarit(array6);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        ToggleButton mirrorModeButton = (ToggleButton) findViewById(R.id.MirrorMode);
        mirrorModeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.60
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ToggleButton mirrorModeButton2 = (ToggleButton) SubDivFormer.this.findViewById(R.id.MirrorMode);
                if (SubDivFormer.this.isToolsWorked()) {
                    if (mirrorModeButton2.isChecked()) {
                        SubDivFormer.this.mirrorModeOn();
                    } else {
                        SubDivFormer.this.mirrorModeOff();
                    }
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                mirrorModeButton2.setChecked(false);
            }
        });
    }

    private void createLocalButtons() {
        Button BackButton = (Button) findViewById(R.id.LocalBackButton);
        BackButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.61
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showMainButtons();
            }
        });
        Button bridgeButton = (Button) findViewById(R.id.BridgeButton);
        bridgeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.62
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.bridgeSelection() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.operation_error, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button mergeButton = (Button) findViewById(R.id.MergeButton);
        mergeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.63
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.mergeSelection() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.operation_error, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button collapseButton = (Button) findViewById(R.id.CollapseButton);
        collapseButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.64
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.collapseSelection() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.operation_error, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button deleteButton = (Button) findViewById(R.id.DeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.65
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int res = SubDivFormer.this.deleteSelection();
                if (res >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                } else if (res == -2) {
                    Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.delete_error, 0);
                    toast.show();
                    SubDivFormer.this.drawFreeRAM();
                } else {
                    Toast toast2 = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.need_select, 0);
                    toast2.show();
                    SubDivFormer.this.drawFreeRAM();
                }
            }
        });
    }

    private void createTransformButtons() {
        Button BackButton = (Button) findViewById(R.id.TransformBackButton);
        BackButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.66
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showMainButtons();
            }
        });
        Button MoveButton = (Button) findViewById(R.id.MoveButton);
        MoveButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.67
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showMoveButtons();
            }
        });
        Button RotateButton = (Button) findViewById(R.id.RotateButton);
        RotateButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.68
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showRotateButtons();
            }
        });
        Button ScaleButton = (Button) findViewById(R.id.ScaleButton);
        ScaleButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.69
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showScaleButtons();
            }
        });
        Button AlignButton = (Button) findViewById(R.id.AlignButton);
        AlignButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.70
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showAlignButtons();
            }
        });
        Button transformPlusButton = (Button) findViewById(R.id.transformPlusButton);
        transformPlusButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.71
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.buttonsMove(1);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
            }
        });
        Button transformMinusButton = (Button) findViewById(R.id.transformMinusButton);
        transformMinusButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.72
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.buttonsMove(-1);
                SubDivFormer.this.mGLSurfaceView_.requestRender();
            }
        });
        transformPlusButton.setVisibility(4);
        transformMinusButton.setVisibility(4);
    }

    protected void hideButtons() {
        View mainButtons = findViewById(R.id.mainButtons);
        View storageButtons = findViewById(R.id.createButtons);
        View createButtons = findViewById(R.id.createButtons);
        View shapeButtons = findViewById(R.id.shapeButtons);
        View transformButtons = findViewById(R.id.transformButtons);
        View localButtons = findViewById(R.id.localButtons);
        View moveButtons = findViewById(R.id.moveButtons);
        View rotateButtons = findViewById(R.id.rotateButtons);
        View scaleButtons = findViewById(R.id.scaleButtons);
        View alignButtons = findViewById(R.id.alignButtons);
        mainButtons.setVisibility(8);
        storageButtons.setVisibility(8);
        createButtons.setVisibility(8);
        shapeButtons.setVisibility(8);
        transformButtons.setVisibility(8);
        localButtons.setVisibility(8);
        moveButtons.setVisibility(8);
        rotateButtons.setVisibility(8);
        scaleButtons.setVisibility(8);
        alignButtons.setVisibility(8);
    }

    protected void showAlignButtons() {
        hideButtons();
        View alignButtons = findViewById(R.id.alignButtons);
        alignButtons.setVisibility(0);
    }

    protected void showScaleButtons() {
        hideButtons();
        View scaleButtons = findViewById(R.id.scaleButtons);
        scaleButtons.setVisibility(0);
    }

    protected void showRotateButtons() {
        hideButtons();
        View rotateButtons = findViewById(R.id.rotateButtons);
        rotateButtons.setVisibility(0);
    }

    protected void showMoveButtons() {
        hideButtons();
        View moveButtons = findViewById(R.id.moveButtons);
        moveButtons.setVisibility(0);
    }

    private void createShapeButtons() {
        Button BackButton = (Button) findViewById(R.id.ShapeBackButton);
        BackButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.73
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showMainButtons();
            }
        });
        Button extrudeButton = (Button) findViewById(R.id.ExtrudeButton);
        extrudeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.74
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int res = SubDivFormer.this.extrudeSelection();
                if (res >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.need_select, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button intrudeButton = (Button) findViewById(R.id.IntrudeButton);
        intrudeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.75
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.intrudeSelection() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.need_select, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button subdivideButton = (Button) findViewById(R.id.SubdivideButton);
        subdivideButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.76
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.subdivideSelection() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.need_select, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button bevelButton = (Button) findViewById(R.id.BevelButton);
        bevelButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.77
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.bevelSelection() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.need_select, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button bumpButton = (Button) findViewById(R.id.BumpButton);
        bumpButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.78
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.bumpSelection() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.need_select, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button splitButton = (Button) findViewById(R.id.SplitButton);
        splitButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.79
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.splitSelection() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.split_error, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button extractButton = (Button) findViewById(R.id.ExtractButton);
        extractButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.80
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.extractSelection() >= 0) {
                    float[] array6 = SubDivFormer.getDrawModelGabarit();
                    SubDivFormer.this.render_.setDrawGabarit(array6);
                    SubDivFormer.this.mGLSurfaceView_.requestRender();
                    SubDivFormer.this.drawFreeRAM();
                    return;
                }
                Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), (int) R.string.need_select, 0);
                toast.show();
                SubDivFormer.this.drawFreeRAM();
            }
        });
    }

    public void showStorageButtons() {
        hideButtons();
        View storageButtons = findViewById(R.id.createButtons);
        storageButtons.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSaveDialogBeforeGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.need_save_message).setTitle(R.string.app_name).setCancelable(false);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.81
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                SubDivFormer.this.saveCurrentFile();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SubDivFormer.writeLog("startActivityForResult: REQUEST_LOAD");
                SubDivFormer.this.startActivityForResult(new Intent(SubDivFormer.this.getBaseContext(), Gallery.class), 1);
            }
        });
        builder.setNeutralButton(R.string.dontsave, new DialogInterface.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.82
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                SubDivFormer.writeLog("startActivityForResult: REQUEST_LOAD");
                SubDivFormer.this.startActivityForResult(new Intent(SubDivFormer.this.getBaseContext(), Gallery.class), 1);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.83
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View settingslayout = getLayoutInflater().inflate(R.layout.settings, (ViewGroup) null);
        builder.setView(settingslayout);
        final AlertDialog dialog = builder.create();
        Button okButton = (Button) settingslayout.findViewById(R.id.settingsOK);
        Button cancelButton = (Button) settingslayout.findViewById(R.id.settingsCancel);
        final CheckBox checkBoxGuides = (CheckBox) settingslayout.findViewById(R.id.checkBoxGuides);
        checkBoxGuides.setChecked(this.showGuides);
        final CheckBox checkBoxColors = (CheckBox) settingslayout.findViewById(R.id.checkBoxColors);
        checkBoxColors.setChecked(this.showColors);
        final CheckBox checkBoxButtonsText = (CheckBox) settingslayout.findViewById(R.id.checkBoxButtonsText);
        checkBoxButtonsText.setChecked(this.showButtonsText);
        final EditText rotateStepText = (EditText) settingslayout.findViewById(R.id.rotateStep);
        rotateStepText.setText(Integer.toString(this.rotateStep_));
        final EditText moveStepText = (EditText) settingslayout.findViewById(R.id.moveStep);
        moveStepText.setText(Integer.toString(this.moveStep_));
        final EditText scaleStepText = (EditText) settingslayout.findViewById(R.id.scaleStep);
        scaleStepText.setText(Integer.toString(this.scaleStep_));
        okButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.84
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showGuides = checkBoxGuides.isChecked();
                SubDivFormer.this.showColors = checkBoxColors.isChecked();
                SubDivFormer.this.showButtonsText = checkBoxButtonsText.isChecked();
                SubDivFormer.this.rotateStep_ = Integer.parseInt(rotateStepText.getText().toString());
                SubDivFormer.this.moveStep_ = Integer.parseInt(moveStepText.getText().toString());
                SubDivFormer.this.scaleStep_ = Integer.parseInt(scaleStepText.getText().toString());
                SubDivFormer.this.updateSettings();
                dialog.cancel();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.85
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isToolsWorked() {
        return advTS == 1;
    }

    public static boolean isAdvancedExportWorked() {
        return advEx == 1;
    }

    public static void writeLog(String message) {
        Log.i("SDF writeLog", message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBuyToolsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout = getLayoutInflater().inflate(R.layout.buy_button, (ViewGroup) null);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        if (layout.getWidth() < widthPixels) {
            layout.setLayoutParams(new FrameLayout.LayoutParams(widthPixels, -2));
        }
        final AlertDialog dialog = builder.create();
        dialog.setView(layout, 0, 0, 0, 0);
        dialog.requestWindowFeature(1);
        Window window = dialog.getWindow();
        window.setFlags(1024, 1024);
        ImageButton buyButton = (ImageButton) layout.findViewById(R.id.buyButton);
        Drawable buttonDrw = getResources().getDrawable(R.drawable.sdf_advanced_toolset);
        buyButton.setImageDrawable(buttonDrw);
        ImageButton closeButton = (ImageButton) layout.findViewById(R.id.closeButton);
        buyButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.86
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                dialog.cancel();
                BuyToolsetTask buyTask = new BuyToolsetTask();
                buyTask.execute(BuildConfig.FLAVOR);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.87
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void createStorageButtons() {
        Button BackButton = (Button) findViewById(R.id.CreateBackButton);
        BackButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.88
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showMainButtons();
            }
        });
        Button openFileButton = (Button) findViewById(R.id.ImportButton);
        openFileButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.89
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.writeLog("startActivityForResult: REQUEST_ADD");
                SubDivFormer.this.startActivityForResult(new Intent(SubDivFormer.this.getBaseContext(), Gallery.class), 4);
            }
        });
        Button galleryButton = (Button) findViewById(R.id.GalleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.90
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showSaveDialogBeforeGallery();
            }
        });
        Button saveFileButton = (Button) findViewById(R.id.SaveButton);
        saveFileButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.91
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubDivFormer.this.fileName_ != null) {
                    SubDivFormer.this.saveModel(SubDivFormer.this.fileName_);
                    SubDivFormer.this.addFileToGallery(SubDivFormer.this.fileName_);
                    Toast toast = Toast.makeText(SubDivFormer.this.getApplicationContext(), SubDivFormer.this.fileName_ + " saved", 0);
                    toast.show();
                }
            }
        });
        Button settingsButton = (Button) findViewById(R.id.settings_btn);
        settingsButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.92
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showSettingsDialog();
            }
        });
        Button exportFileButton = (Button) findViewById(R.id.ExportButton);
        exportFileButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.93
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.writeLog("startActivityForResult: REQUEST_EXPORT");
                SubDivFormer.this.startActivityForResult(new Intent(SubDivFormer.this.getBaseContext(), ExportFileDialog.class), 3);
            }
        });
    }

    private void showButtonsText(boolean show) {
        float size = show ? 15.0f : 0.0f;
        Button button = (Button) findViewById(R.id.GalleryButton);
        button.setTextSize(size);
        Button button2 = (Button) findViewById(R.id.settings_btn);
        button2.setTextSize(size);
        Button button3 = (Button) findViewById(R.id.AddButton);
        button3.setTextSize(size);
        Button button4 = (Button) findViewById(R.id.ShapeButton);
        button4.setTextSize(size);
        Button button5 = (Button) findViewById(R.id.TransformButton);
        button5.setTextSize(size);
        Button button6 = (Button) findViewById(R.id.LocalButton);
        button6.setTextSize(size);
        Button button7 = (Button) findViewById(R.id.CubeButton);
        button7.setTextSize(size);
        Button button8 = (Button) findViewById(R.id.LargeSphereButton);
        button8.setTextSize(size);
        Button button9 = (Button) findViewById(R.id.CylinderButton);
        button9.setTextSize(size);
        Button button10 = (Button) findViewById(R.id.TorusButton);
        button10.setTextSize(size);
        Button button11 = (Button) findViewById(R.id.Cube9x9Button);
        button11.setTextSize(size);
        Button button12 = (Button) findViewById(R.id.SphereBallButton);
        button12.setTextSize(size);
        Button button13 = (Button) findViewById(R.id.MediumTorusButton);
        button13.setTextSize(size);
        Button button14 = (Button) findViewById(R.id.ComplexTorusButton);
        button14.setTextSize(size);
        Button button15 = (Button) findViewById(R.id.BoxButton);
        button15.setTextSize(size);
        Button button16 = (Button) findViewById(R.id.ImportButton);
        button16.setTextSize(size);
        Button button17 = (Button) findViewById(R.id.ExtrudeButton);
        button17.setTextSize(size);
        Button button18 = (Button) findViewById(R.id.IntrudeButton);
        button18.setTextSize(size);
        Button button19 = (Button) findViewById(R.id.SubdivideButton);
        button19.setTextSize(size);
        Button button20 = (Button) findViewById(R.id.BevelButton);
        button20.setTextSize(size);
        Button button21 = (Button) findViewById(R.id.BumpButton);
        button21.setTextSize(size);
        Button button22 = (Button) findViewById(R.id.SplitButton);
        button22.setTextSize(size);
        Button button23 = (Button) findViewById(R.id.MoveButton);
        button23.setTextSize(size);
        Button button24 = (Button) findViewById(R.id.RotateButton);
        button24.setTextSize(size);
        Button button25 = (Button) findViewById(R.id.ScaleButton);
        button25.setTextSize(size);
        Button button26 = (Button) findViewById(R.id.AlignButton);
        button26.setTextSize(size);
        Button button27 = (Button) findViewById(R.id.BridgeButton);
        button27.setTextSize(size);
        Button button28 = (Button) findViewById(R.id.MergeButton);
        button28.setTextSize(size);
        Button button29 = (Button) findViewById(R.id.CollapseButton);
        button29.setTextSize(size);
        Button button30 = (Button) findViewById(R.id.DeleteButton);
        button30.setTextSize(size);
        Button button31 = (Button) findViewById(R.id.MoveNButton);
        button31.setTextSize(size);
        Button button32 = (Button) findViewById(R.id.MoveXButton);
        button32.setTextSize(size);
        Button button33 = (Button) findViewById(R.id.MoveYButton);
        button33.setTextSize(size);
        Button button34 = (Button) findViewById(R.id.MoveZButton);
        button34.setTextSize(size);
        Button button35 = (Button) findViewById(R.id.RotateNButton);
        button35.setTextSize(size);
        Button button36 = (Button) findViewById(R.id.RotateXButton);
        button36.setTextSize(size);
        Button button37 = (Button) findViewById(R.id.RotateYButton);
        button37.setTextSize(size);
        Button button38 = (Button) findViewById(R.id.RotateZButton);
        button38.setTextSize(size);
        Button button39 = (Button) findViewById(R.id.ScaleXButton);
        button39.setTextSize(size);
        Button button40 = (Button) findViewById(R.id.ScaleYButton);
        button40.setTextSize(size);
        Button button41 = (Button) findViewById(R.id.ScaleZButton);
        button41.setTextSize(size);
        Button button42 = (Button) findViewById(R.id.ScaleAllButton);
        button42.setTextSize(size);
        Button button43 = (Button) findViewById(R.id.AlignNButton);
        button43.setTextSize(size);
        Button button44 = (Button) findViewById(R.id.AlignXButton);
        button44.setTextSize(size);
        Button button45 = (Button) findViewById(R.id.AlignYButton);
        button45.setTextSize(size);
        Button button46 = (Button) findViewById(R.id.AlignZButton);
        button46.setTextSize(size);
        Button button47 = (Button) findViewById(R.id.showall);
        button47.setTextSize(size);
        Button button48 = (Button) findViewById(R.id.fullScreen);
        button48.setTextSize(size);
        Button button49 = (Button) findViewById(R.id.undo);
        button49.setTextSize(size);
        Button button50 = (Button) findViewById(R.id.redo);
        button50.setTextSize(size);
        Button button51 = (Button) findViewById(R.id.editcopy);
        button51.setTextSize(size);
        Button button52 = (Button) findViewById(R.id.editpaste);
        button52.setTextSize(size);
        Button button53 = (Button) findViewById(R.id.increaseselection);
        button53.setTextSize(size);
        Button button54 = (Button) findViewById(R.id.reverseselection);
        button54.setTextSize(size);
        Button button55 = (Button) findViewById(R.id.stripselection);
        button55.setTextSize(size);
        Button button56 = (Button) findViewById(R.id.selectregion);
        button56.setTextSize(size);
        Button button57 = (Button) findViewById(R.id.ColorSelect);
        button57.setTextSize(size);
        Button button58 = (Button) findViewById(R.id.SelectTypeButton);
        button58.setTextSize(size);
        Button button59 = (Button) findViewById(R.id.MirrorCommand);
        button59.setTextSize(size);
        Button button60 = (Button) findViewById(R.id.MirrorMode);
        button60.setTextSize(size);
        Button button61 = (Button) findViewById(R.id.SelectColor);
        button61.setTextSize(size);
        Button button62 = (Button) findViewById(R.id.PickColor);
        button62.setTextSize(size);
        Button button63 = (Button) findViewById(R.id.ColorizeSelected);
        button63.setTextSize(size);
        Button button64 = (Button) findViewById(R.id.DrawColor);
        button64.setTextSize(size);
    }

    private void createAddButtons() {
        Button BackButton = (Button) findViewById(R.id.CreateBackButton);
        BackButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.94
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.showMainButtons();
            }
        });
        Button cubeButton = (Button) findViewById(R.id.CubeButton);
        cubeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.95
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.updateUndoContainer();
                SubDivFormer.this.addCube();
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button sphereButton = (Button) findViewById(R.id.LargeSphereButton);
        sphereButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.96
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.updateUndoContainer();
                String modelPath = SubDivFormer.this.getFilesDir().getAbsolutePath() + "/models/sphere.om";
                SubDivFormer.addFile(modelPath.getBytes());
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button cylinderButton = (Button) findViewById(R.id.CylinderButton);
        cylinderButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.97
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.updateUndoContainer();
                String modelPath = SubDivFormer.this.getFilesDir().getAbsolutePath() + "/models/cylinder.om";
                SubDivFormer.addFile(modelPath.getBytes());
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button torusButton = (Button) findViewById(R.id.TorusButton);
        torusButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.98
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.updateUndoContainer();
                String modelPath = SubDivFormer.this.getFilesDir().getAbsolutePath() + "/models/torus.om";
                SubDivFormer.addFile(modelPath.getBytes());
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button largeCubeButton = (Button) findViewById(R.id.Cube9x9Button);
        largeCubeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.99
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.updateUndoContainer();
                String modelPath = SubDivFormer.this.getFilesDir().getAbsolutePath() + "/models/large_cube.om";
                SubDivFormer.addFile(modelPath.getBytes());
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button sphereBallButton = (Button) findViewById(R.id.SphereBallButton);
        sphereBallButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.100
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.updateUndoContainer();
                String modelPath = SubDivFormer.this.getFilesDir().getAbsolutePath() + "/models/sphereball.om";
                SubDivFormer.addFile(modelPath.getBytes());
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button mediumTorusButton = (Button) findViewById(R.id.MediumTorusButton);
        mediumTorusButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.101
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.updateUndoContainer();
                String modelPath = SubDivFormer.this.getFilesDir().getAbsolutePath() + "/models/medium_torus.om";
                SubDivFormer.addFile(modelPath.getBytes());
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button complexTorusButton = (Button) findViewById(R.id.ComplexTorusButton);
        complexTorusButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.102
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.updateUndoContainer();
                String modelPath = SubDivFormer.this.getFilesDir().getAbsolutePath() + "/models/complex_torus.om";
                SubDivFormer.addFile(modelPath.getBytes());
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
        Button boxButton = (Button) findViewById(R.id.BoxButton);
        boxButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.103
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.updateUndoContainer();
                String modelPath = SubDivFormer.this.getFilesDir().getAbsolutePath() + "/models/box.om";
                SubDivFormer.addFile(modelPath.getBytes());
                SubDivFormer.this.mGLSurfaceView_.requestRender();
                SubDivFormer.this.drawFreeRAM();
            }
        });
    }

    public void updateSettings() {
        setDrawGuidesMode(this.showGuides);
        setDrawColorMode(this.showColors);
        showButtonsText(this.showButtonsText);
        this.mGLSurfaceView_.requestRender();
        drawFreeRAM();
    }

    public void showMainButtons() {
        hideButtons();
        View mainButtons = findViewById(R.id.mainButtons);
        mainButtons.setVisibility(0);
    }

    private void createMainButtons() {
        Button StorageButton = (Button) findViewById(R.id.CubeButton);
        StorageButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.104
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.hideButtons();
                View createButtons = SubDivFormer.this.findViewById(R.id.createButtons);
                createButtons.setVisibility(0);
            }
        });
        Button CreateButton = (Button) findViewById(R.id.AddButton);
        CreateButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.105
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.hideButtons();
                View createButtons = SubDivFormer.this.findViewById(R.id.createButtons);
                createButtons.setVisibility(0);
            }
        });
        Button ShapeButton = (Button) findViewById(R.id.ShapeButton);
        ShapeButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.106
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.hideButtons();
                View shapeButtons = SubDivFormer.this.findViewById(R.id.shapeButtons);
                shapeButtons.setVisibility(0);
            }
        });
        Button TransformButton = (Button) findViewById(R.id.TransformButton);
        TransformButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.107
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.hideButtons();
                View transformButtons = SubDivFormer.this.findViewById(R.id.transformButtons);
                transformButtons.setVisibility(0);
            }
        });
        Button LocalButton = (Button) findViewById(R.id.LocalButton);
        LocalButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SubDivFormer.108
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SubDivFormer.this.hideButtons();
                View localButtons = SubDivFormer.this.findViewById(R.id.localButtons);
                localButtons.setVisibility(0);
            }
        });
    }

    protected void showAll() {
        this.render_.showAll();
        this.mGLSurfaceView_.requestRender();
        drawFreeRAM();
    }

    protected void saveModel(String fileName) {
        saveFile(fileName.getBytes(), 0);
        MediaScannerConnection.scanFile(this, new String[]{fileName}, null, null);
        drawFreeRAM();
    }

    public int exportModel(String fileName, int level) {
        saveFile(fileName.getBytes(), level);
        MediaScannerConnection.scanFile(this, new String[]{fileName}, null, null);
        return 0;
    }

    protected void loadModel(String fileName) {
        String loadResult = BuildConfig.FLAVOR;
        if (!fileName.isEmpty()) {
            loadResult = loadFile(fileName.getBytes(), 0);
        }
        if (loadResult.isEmpty()) {
            clearModel();
            if (!fileName.isEmpty()) {
                loadFile(fileName.getBytes(), 1);
            }
        }
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        float[] array6 = getDrawModelGabarit();
        this.render_.setDrawGabarit(array6);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filename", this.fileName_);
        outState.putBoolean("showColors", this.showColors);
        outState.putBoolean("showGuides", this.showGuides);
        outState.putBoolean("showButtonsText", this.showButtonsText);
        outState.putFloat("scale_", this.render_.scale_);
        outState.putFloatArray("modelMtr_", this.render_.modelMtr_);
        outState.putFloatArray("viewMtr_", this.render_.viewMtr_);
        outState.putFloatArray("navView_", this.render_.navView_);
        outState.putFloatArray("projection_", this.render_.projection_);
        outState.putInt("rotateStep_", this.rotateStep_);
        outState.putInt("moveStep_", this.moveStep_);
        outState.putInt("scaleStep_", this.scaleStep_);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override // android.app.Activity
    public Object onRetainNonConfigurationInstance() {
        Log.d("qwe", "save model: " + this.model_.hashCode());
        return this.model_;
    }

    public void createNewModel() {
        clearModel();
        float[] array6 = getDrawModelGabarit();
        this.render_.setDrawGabarit(array6);
        this.mGLSurfaceView_.requestRender();
        File EXTERNAL_DIR = Environment.getExternalStorageDirectory();
        String startPath = EXTERNAL_DIR.getAbsolutePath() + "/subdivformer";
        File sdfDir = new File(startPath);
        if (!sdfDir.exists()) {
            sdfDir.mkdir();
        }
        Date curDate = new Date();
        String dateString = String.valueOf(curDate.getTime());
        String fileName = startPath + "/sdf_" + dateString + ".sdf";
        try {
            saveModel(fileName);
            addFileToGallery(fileName);
            this.fileName_ = fileName;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), String.format("Failed to save file. Exception: %1$s", e.getMessage()), 0).show();
            writeLog("startActivityForResult: REQUEST_SAVE");
            startActivityForResult(new Intent(getBaseContext(), FileDialog.class), 2);
        }
    }

    public void saveCurrentFile() {
        String samplesPath = getFilesDir().getAbsolutePath() + "/samples";
        if (this.fileName_ != null && !this.fileName_.contains(samplesPath)) {
            addFileToGallery(this.fileName_);
            saveModel(this.fileName_);
            Toast toast = Toast.makeText(getApplicationContext(), this.fileName_ + " saved", 0);
            toast.show();
            drawFreeRAM();
        }
    }

    @Override // android.app.Activity
    public synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        writeLog("onActivityResult: " + String.valueOf(requestCode));
        if (resultCode == -1) {
            if (requestCode == 3) {
                writeLog("onActivityResult: GET_COLOR");
                int color = data.getIntExtra(SelectColor.RESULT_COLOR, 0);
                if (this.lastColor1 != color) {
                    this.lastColor3 = this.lastColor2;
                    this.lastColor2 = this.lastColor1;
                    this.lastColor1 = color;
                }
                this.colorR = (16711680 & color) >>> 16;
                this.colorG = (65280 & color) >>> 8;
                this.colorB = color & 255;
                setColorizeMode(this.colorizeState_, this.colorR, this.colorG, this.colorB);
            } else {
                writeLog("onActivityResult: !GET_COLOR");
                String resultStr = data.getStringExtra(Gallery.GALLERY_RESULT);
                if (requestCode == 1 && resultStr.compareToIgnoreCase("OPEN") == 0) {
                    writeLog("onActivityResult: !GET_COLOR REQUEST_LOAD");
                    this.newFilePath_ = data.getStringExtra("RESULT_PATH");
                    this.newPreviewPath_ = data.getStringExtra(Gallery.PREVIEW_PATH);
                    try {
                        loadModel(this.newFilePath_);
                        this.fileName_ = this.newFilePath_;
                        float[] array6 = getDrawModelGabarit();
                        this.render_.setDrawGabarit(array6);
                        this.render_.showAll();
                        this.mGLSurfaceView_.requestRender();
                        if (this.newPreviewPath_ != null) {
                            savePreview(this.newPreviewPath_);
                        }
                        this.newFilePath_ = null;
                        this.newPreviewPath_ = null;
                        drawFreeRAM();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), String.format("Failed to load image. Exception: %1$s", e.getMessage()), 0).show();
                        writeLog("startActivityForResult: REQUEST_LOAD");
                        startActivityForResult(new Intent(getBaseContext(), FileDialog.class), 1);
                        drawFreeRAM();
                    } catch (OutOfMemoryError e2) {
                        Toast.makeText(getApplicationContext(), "Failed to load file. Error: not enough memory", 0).show();
                        writeLog("startActivityForResult: REQUEST_LOAD");
                        startActivityForResult(new Intent(getBaseContext(), FileDialog.class), 1);
                        drawFreeRAM();
                    }
                }
                if (requestCode == 1 && resultStr.compareToIgnoreCase("NEW") == 0) {
                    writeLog("onActivityResult: !GET_COLOR REQUEST_LOAD NEW");
                    createNewModel();
                }
                if (requestCode == 4 && resultStr.compareToIgnoreCase("OPEN") == 0) {
                    writeLog("onActivityResult: !GET_COLOR REQUEST_ADD");
                    String addFilePath = data.getStringExtra("RESULT_PATH");
                    data.getStringExtra(Gallery.PREVIEW_PATH);
                    try {
                        addFile(addFilePath.getBytes());
                        this.mGLSurfaceView_.requestRender();
                        drawFreeRAM();
                    } catch (Exception e3) {
                        Toast.makeText(getApplicationContext(), String.format("Failed to load image. Exception: %1$s", e3.getMessage()), 0).show();
                        writeLog("startActivityForResult: REQUEST_LOAD");
                        startActivityForResult(new Intent(getBaseContext(), FileDialog.class), 1);
                        drawFreeRAM();
                    } catch (OutOfMemoryError e4) {
                        Toast.makeText(getApplicationContext(), "Failed to load file. Error: not enough memory", 0).show();
                        writeLog("startActivityForResult: REQUEST_LOAD");
                        startActivityForResult(new Intent(getBaseContext(), FileDialog.class), 1);
                        drawFreeRAM();
                    }
                }
                if (requestCode == 2) {
                    writeLog("onActivityResult: !GET_COLOR REQUEST_SAVE");
                    String filePath = data.getStringExtra("RESULT_PATH");
                    Boolean send = Boolean.valueOf(data.getBooleanExtra("SEND", false));
                    try {
                        saveModel(filePath);
                        addFileToGallery(filePath);
                        if (send.booleanValue()) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction("android.intent.action.SEND");
                            Uri fileUri = Uri.fromFile(new File(filePath));
                            shareIntent.putExtra("android.intent.extra.STREAM", fileUri);
                            shareIntent.setType("*/*");
                            writeLog("startActivityForResult: Send to...");
                            startActivity(Intent.createChooser(shareIntent, "Send to..."));
                        }
                    } catch (Exception e5) {
                        Toast.makeText(getApplicationContext(), String.format("Failed to save file. Exception: %1$s", e5.getMessage()), 0).show();
                        writeLog("startActivityForResult: REQUEST_SAVE");
                        startActivityForResult(new Intent(getBaseContext(), FileDialog.class), 2);
                    }
                }
                if (requestCode == 3) {
                    writeLog("onActivityResult: !GET_COLOR REQUEST_EXPORT");
                    try {
                        exportModel(data.getStringExtra("RESULT_PATH"), 3);
                    } catch (Exception e6) {
                        Toast.makeText(getApplicationContext(), String.format("Failed to save file. Exception: %1$s", e6.getMessage()), 0).show();
                        writeLog("startActivityForResult: REQUEST_SAVE");
                        startActivityForResult(new Intent(getBaseContext(), FileDialog.class), 2);
                    }
                }
            }
        } else if (resultCode == 0) {
            writeLog("onActivityResult: RESULT_CANCELED");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ascon.subdivformer.SubDivFormer$109  reason: invalid class name */
    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    public static /* synthetic */ class AnonymousClass109 {
        static final /* synthetic */ int[] $SwitchMap$com$ascon$subdivformer$SubDivFormer$state = new int[state.values().length];

        static {
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.moveN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.moveScreen.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.moveX.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.moveY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.moveZ.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.rotateN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.rotateScreen.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.rotateX.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.rotateY.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.rotateZ.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.scaleAll.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.scaleX.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.scaleY.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.scaleZ.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$ascon$subdivformer$SubDivFormer$state[state.navigate.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
        }
    }

    public void buttonsMove(int koeff) {
        switch (AnonymousClass109.$SwitchMap$com$ascon$subdivformer$SubDivFormer$state[this.state_.ordinal()]) {
            case 1:
                moveSelectionN(this.moveStep_ * koeff);
                break;
            case 3:
                moveSelection(this.moveStep_ * koeff, 0.0f, 0.0f);
                break;
            case 4:
                moveSelection(0.0f, (-koeff) * this.moveStep_, 0.0f);
                break;
            case 5:
                moveSelection(0.0f, 0.0f, (-koeff) * this.moveStep_);
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_ERROR /* 6 */:
                rotateSelectionN((koeff * this.rotateStep_) / 57.295776f);
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED /* 8 */:
                rotateSelection((koeff * this.rotateStep_) / 57.295776f, 0.0f, 0.0f);
                break;
            case 9:
                rotateSelection(0.0f, (koeff * this.rotateStep_) / 57.295776f, 0.0f);
                break;
            case 10:
                rotateSelection(0.0f, 0.0f, (koeff * this.rotateStep_) / 57.295776f);
                break;
            case 11:
                scaleSelection((this.scaleStep_ * koeff) / 100.0f, (this.scaleStep_ * koeff) / 100.0f, (this.scaleStep_ * koeff) / 100.0f);
                break;
            case 12:
                scaleSelection((this.scaleStep_ * koeff) / 100.0f, 0.0f, 0.0f);
                break;
            case 13:
                scaleSelection(0.0f, (this.scaleStep_ * koeff) / 100.0f, 0.0f);
                break;
            case 14:
                scaleSelection(0.0f, 0.0f, (this.scaleStep_ * koeff) / 100.0f);
                break;
        }
        float[] array6 = getDrawModelGabarit();
        this.render_.setDrawGabarit(array6);
        drawFreeRAM();
    }

    public void screenMove(float dx, float dy) {
        switch (AnonymousClass109.$SwitchMap$com$ascon$subdivformer$SubDivFormer$state[this.state_.ordinal()]) {
            case 1:
                moveSelectionN(dx + dy);
                break;
            case 3:
                moveSelection(dx + dy, 0.0f, 0.0f);
                break;
            case 4:
                moveSelection(0.0f, (-dx) - dy, 0.0f);
                break;
            case 5:
                moveSelection(0.0f, 0.0f, (-dx) - dy);
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_ERROR /* 6 */:
                rotateSelectionN((dx + dy) / 200.0f);
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED /* 8 */:
                rotateSelection((dx + dy) / 200.0f, 0.0f, 0.0f);
                break;
            case 9:
                rotateSelection(0.0f, (dx + dy) / 200.0f, 0.0f);
                break;
            case 10:
                rotateSelection(0.0f, 0.0f, (dx + dy) / 200.0f);
                break;
            case 11:
                scaleSelection((dx + dy) / 400.0f, (dx + dy) / 400.0f, (dx + dy) / 400.0f);
                break;
            case 12:
                scaleSelection((dx + dy) / 400.0f, 0.0f, 0.0f);
                break;
            case 13:
                scaleSelection(0.0f, (dx + dy) / 400.0f, 0.0f);
                break;
            case 14:
                scaleSelection(0.0f, 0.0f, (dx + dy) / 400.0f);
                break;
        }
        float[] array6 = getDrawModelGabarit();
        this.render_.setDrawGabarit(array6);
        drawFreeRAM();
    }

    public void savePreview(String fileName) {
        if (this.render_ != null) {
            this.render_.fileName_ = fileName;
            this.render_.saveToFile_ = true;
        }
    }

    private void loadLastModel() {
        File dir = getFilesDir();
        File file = new File(dir, "gallery.txt");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("gallery.txt")));
                String str = br.readLine();
                if (str != null) {
                    int delimIndex = str.indexOf(";");
                    String modelName = str.substring(0, delimIndex);
                    loadModel(modelName);
                    this.fileName_ = modelName;
                    float[] array6 = getDrawModelGabarit();
                    this.render_.setDrawGabarit(array6);
                    this.render_.showAll();
                    this.mGLSurfaceView_.requestRender();
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            createNewModel();
        }
        drawFreeRAM();
    }

    public void addFileToGallery(String fileName) {
        File dir = getFilesDir();
        File file = new File(dir, "gallery.txt");
        String previewFileName = null;
        List<String> filesList_ = new ArrayList<>();
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("gallery.txt")));
                while (true) {
                    String str = br.readLine();
                    if (str == null) {
                        break;
                    }
                    int delimIndex = str.indexOf(";");
                    if (delimIndex > 0) {
                        String modelName = str.substring(0, delimIndex);
                        if (modelName.compareTo(fileName) != 0) {
                            filesList_.add(str);
                        } else {
                            previewFileName = str.substring(delimIndex + 1);
                        }
                    }
                }
                br.close();
                filesList_.size();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (previewFileName == null) {
            try {
                File previewFile = File.createTempFile("prw", ".png");
                if (file.exists()) {
                    file.delete();
                }
                previewFileName = previewFile.getAbsolutePath();
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
                return;
            } catch (IOException e4) {
                e4.printStackTrace();
                return;
            }
        }
        this.render_.fileName_ = previewFileName;
        this.render_.saveToFile_ = true;
        this.mGLSurfaceView_.requestRender();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("gallery.txt", 2)));
        bw.write(fileName);
        bw.write(";");
        bw.write(previewFileName);
        if (filesList_ != null) {
            for (int i = 0; i < filesList_.size(); i++) {
                bw.newLine();
                bw.write(filesList_.get(i));
            }
        }
        bw.close();
    }

    public void changeToolState() {
        Button transformPlusButton = (Button) findViewById(R.id.transformPlusButton);
        Button transformMinusButton = (Button) findViewById(R.id.transformMinusButton);
        if (this.toolState_) {
            transformPlusButton.setVisibility(0);
            transformMinusButton.setVisibility(0);
        } else {
            transformPlusButton.setVisibility(4);
            transformMinusButton.setVisibility(4);
        }
        setToolMode(this.toolState_);
    }

    public void changeSelectMode() {
        Drawable buttonDrw;
        Button selectModeButton = (Button) findViewById(R.id.SelectTypeButton);
        if (this.selectMode_ == SelectMode.face) {
            buttonDrw = getResources().getDrawable(R.drawable.select_edge);
            selectModeButton.setText(getResources().getString(R.string.edge));
            this.selectMode_ = SelectMode.edge;
            setSelectMode(1);
        } else if (this.selectMode_ == SelectMode.edge) {
            buttonDrw = getResources().getDrawable(R.drawable.select_vertex);
            selectModeButton.setText(getResources().getString(R.string.vertex));
            this.selectMode_ = SelectMode.vertex;
            setSelectMode(2);
        } else {
            buttonDrw = getResources().getDrawable(R.drawable.select_face);
            selectModeButton.setText(getResources().getString(R.string.face));
            this.selectMode_ = SelectMode.face;
            setSelectMode(0);
        }
        selectModeButton.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, buttonDrw, (Drawable) null, (Drawable) null);
        this.mGLSurfaceView_.requestRender();
        drawFreeRAM();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    public class UpdateInAppTask extends AsyncTask<String, Integer, Integer> {
        UpdateInAppTask() {
        }

        int getResponseCodeFromBundle(Bundle b) {
            SubDivFormer.writeLog("UpdateInAppTask: getResponseCodeFromBundle");
            Log.i("UpdateInAppTask: ", "getResponseCodeFromBundle");
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
            SubDivFormer.writeLog("UpdateInAppTask: doInBackground begin");
            Log.i("UpdateInAppTask: ", "doInBackground");
            ArrayList<String> skuList = new ArrayList<>();
            skuList.add("advanced_export");
            skuList.add("advanced_toolset");
            Bundle querySkus = new Bundle();
            querySkus.putStringArrayList(IabHelper.GET_SKU_DETAILS_ITEM_LIST, skuList);
            try {
                Bundle ownedItems = SubDivFormer.mService.getPurchases(3, SubDivFormer.this.getPackageName(), IabHelper.ITEM_TYPE_INAPP, (String) null);
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
                        Log.i("INAPP_PURCHASE_ITEM_LIST: ", thisResponse);
                    }
                } else {
                    SubDivFormer.writeLog("Billing getPurchases: error");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SubDivFormer.writeLog("UpdateInAppTask: doInBackground end");
            return 0;
        }
    }

    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    class BuyToolsetTask extends AsyncTask<String, Integer, Integer> {
        BuyToolsetTask() {
        }

        int getResponseCodeFromBundle(Bundle b) {
            SubDivFormer.writeLog("BuyToolsetTask: getResponseCodeFromBundle");
            Log.i("BuyToolsetTask: ", "getResponseCodeFromBundle");
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
            SubDivFormer.writeLog("BuyToolsetTask: doInBackground begin");
            Log.i("BuyToolsetTask: ", "doInBackground");
            try {
                Bundle buyIntentBundle = SubDivFormer.mService.getBuyIntent(3, SubDivFormer.this.getPackageName(), "advanced_toolset", IabHelper.ITEM_TYPE_INAPP, BuildConfig.FLAVOR);
                int response = getResponseCodeFromBundle(buyIntentBundle);
                if (response == 0) {
                    Log.i("Subdivformer", "BuyToolsetTask: Consumed");
                    PendingIntent pendingIntent = (PendingIntent) buyIntentBundle.getParcelable(IabHelper.RESPONSE_BUY_INTENT);
                    if (pendingIntent != null) {
                        Log.i("Subdivformer", "BuyToolsetTask: doInBackground startIntentSenderForResult ");
                        SubDivFormer subDivFormer = SubDivFormer.this;
                        IntentSender intentSender = pendingIntent.getIntentSender();
                        Intent intent = new Intent();
                        Integer num = 0;
                        int intValue = num.intValue();
                        Integer num2 = 0;
                        int intValue2 = num2.intValue();
                        Integer num3 = 0;
                        subDivFormer.startIntentSenderForResult(intentSender, 1001, intent, intValue, intValue2, num3.intValue());
                        SubDivFormer.writeLog("BUY_INTENT: PendingIntent");
                    }
                } else {
                    SubDivFormer.writeLog("BUY_INTENT: error " + String.valueOf(response));
                }
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
            SubDivFormer.writeLog("BuyToolsetTask: doInBackground end");
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer result) {
            SubDivFormer.writeLog("BuyToolsetTask: onPostExecute");
            Log.i("BuyToolsetTask: ", "onPostExecute");
            super.onPostExecute((BuyToolsetTask) result);
            UpdateInAppTask updateTask = new UpdateInAppTask();
            updateTask.execute(BuildConfig.FLAVOR);
        }
    }
}
