package com.ascon.subdivformer;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class SaveFileDialog extends ListActivity {
    public static File EXTERNAL_DIR = Environment.getExternalStorageDirectory();
    private static final String ITEM_IMAGE = "image";
    private static final String ITEM_KEY = "key";
    public static final String RESULT_PATH = "RESULT_PATH";
    public static final String SEND = "SEND";
    public static final String START_PATH = "START_PATH";
    private ArrayList<HashMap<String, Object>> mList;
    private TextView myPath;
    private String parentPath;
    private Button selectButton;
    private List<String> item = null;
    private List<String> path = null;
    private String root = "/";
    private EditText fNameEdit = null;
    private String currentPath = this.root;
    private HashMap<String, Integer> lastPositions = new HashMap<>();

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        this.root = Environment.getExternalStorageDirectory().getAbsolutePath();
        this.currentPath = this.root;
        super.onCreate(savedInstanceState);
        setResult(0, getIntent());
        setContentView(R.layout.savefiledialog);
        this.myPath = (TextView) findViewById(R.id.path);
        this.fNameEdit = (EditText) findViewById(R.id.fileName);
        this.selectButton = (Button) findViewById(R.id.fdButtonSelect);
        this.selectButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SaveFileDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SaveFileDialog.this.fNameEdit.getText().length() > 0) {
                    String saveFileName = SaveFileDialog.this.currentPath + "/" + ((Object) SaveFileDialog.this.fNameEdit.getText()) + ".sdf";
                    SaveFileDialog.this.getIntent().putExtra("RESULT_PATH", saveFileName);
                    SaveFileDialog.this.setResult(-1, SaveFileDialog.this.getIntent());
                    SaveFileDialog.this.finish();
                }
            }
        });
        Button sendButton = (Button) findViewById(R.id.fdButtonSend);
        sendButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.SaveFileDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SaveFileDialog.this.fNameEdit.getText().length() > 0) {
                    String saveFileName = SaveFileDialog.this.currentPath + "/" + ((Object) SaveFileDialog.this.fNameEdit.getText()) + ".sdf";
                    SaveFileDialog.this.getIntent().putExtra("RESULT_PATH", saveFileName);
                    SaveFileDialog.this.getIntent().putExtra("SEND", true);
                    SaveFileDialog.this.setResult(-1, SaveFileDialog.this.getIntent());
                    SaveFileDialog.this.finish();
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

    private void getDir(String dirPath) {
        if (dirPath != null) {
            boolean useAutoSelection = dirPath.length() < this.currentPath.length();
            Integer position = this.lastPositions.get(this.parentPath);
            getDirImpl(dirPath);
            if (position != null && useAutoSelection) {
                getListView().setSelection(position.intValue());
            }
        }
    }

    private void getDirImpl(String dirPath) {
        this.myPath.setText(((Object) getText(R.string.location)) + ": " + dirPath);
        this.currentPath = dirPath;
        this.item = new ArrayList();
        this.path = new ArrayList();
        this.mList = new ArrayList<>();
        File f = new File(dirPath);
        File[] files = f.listFiles();
        if (!dirPath.equals(this.root)) {
            this.item.add(this.root);
            addItem(this.root, R.drawable.folder);
            this.path.add(this.root);
            this.item.add("../");
            addItem("../", R.drawable.folder);
            this.path.add(f.getParent());
            this.parentPath = f.getParent();
        }
        TreeMap<String, String> dirsMap = new TreeMap<>();
        TreeMap<String, String> dirsPathMap = new TreeMap<>();
        TreeMap<String, String> filesMap = new TreeMap<>();
        TreeMap<String, String> filesPathMap = new TreeMap<>();
        for (File file : files) {
            if (file.isDirectory()) {
                String dirName = file.getName();
                dirsMap.put(dirName, dirName);
                dirsPathMap.put(dirName, file.getPath());
            } else {
                filesMap.put(file.getName(), file.getName());
                filesPathMap.put(file.getName(), file.getPath());
            }
        }
        this.item.addAll(dirsMap.tailMap(BuildConfig.FLAVOR).values());
        this.item.addAll(filesMap.tailMap(BuildConfig.FLAVOR).values());
        this.path.addAll(dirsPathMap.tailMap(BuildConfig.FLAVOR).values());
        this.path.addAll(filesPathMap.tailMap(BuildConfig.FLAVOR).values());
        SimpleAdapter fileList = new SimpleAdapter(this, this.mList, R.layout.file_dialog_row, new String[]{ITEM_KEY, ITEM_IMAGE}, new int[]{R.id.fdrowtext, R.id.fdrowimage});
        for (String dir : dirsMap.tailMap(BuildConfig.FLAVOR).values()) {
            addItem(dir, R.drawable.folder);
        }
        for (String file2 : filesMap.tailMap(BuildConfig.FLAVOR).values()) {
            addItem(file2, R.drawable.file);
        }
        fileList.notifyDataSetChanged();
        setListAdapter(fileList);
    }

    private void addItem(String fileName, int imageId) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(ITEM_KEY, fileName);
        item.put(ITEM_IMAGE, Integer.valueOf(imageId));
        this.mList.add(item);
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(this.path.get(position));
        if (file.isDirectory()) {
            if (file.canRead()) {
                this.lastPositions.put(this.currentPath, Integer.valueOf(position));
                getDir(this.path.get(position));
                return;
            }
            new AlertDialog.Builder(this).setIcon(R.drawable.icon).setTitle("[" + file.getName() + "] " + ((Object) getText(R.string.cant_read_folder))).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.ascon.subdivformer.SaveFileDialog.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }
    }
}
