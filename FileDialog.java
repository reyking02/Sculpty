package com.ascon.subdivformer;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class FileDialog extends ListActivity {
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
    private File selectedFile;
    private Button sendButton;
    private List<String> item = null;
    private List<String> path = null;
    private String root = "/";
    private String currentPath = this.root;
    private HashMap<String, Integer> lastPositions = new HashMap<>();

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        this.root = Environment.getExternalStorageDirectory().getAbsolutePath();
        this.currentPath = this.root;
        super.onCreate(savedInstanceState);
        setResult(0, getIntent());
        setContentView(R.layout.filedialog);
        this.myPath = (TextView) findViewById(R.id.path);
        this.selectButton = (Button) findViewById(R.id.fdButtonSelect);
        this.selectButton.setEnabled(false);
        this.selectButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.FileDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (FileDialog.this.selectedFile != null) {
                    FileDialog.this.getIntent().putExtra("RESULT_PATH", FileDialog.this.selectedFile.getPath());
                    FileDialog.this.getIntent().putExtra("SEND", false);
                    FileDialog.this.setResult(-1, FileDialog.this.getIntent());
                    FileDialog.this.finish();
                }
            }
        });
        this.sendButton = (Button) findViewById(R.id.fdButtonSend);
        this.sendButton.setEnabled(false);
        this.sendButton.setOnClickListener(new View.OnClickListener() { // from class: com.ascon.subdivformer.FileDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (FileDialog.this.selectedFile != null) {
                    FileDialog.this.getIntent().putExtra("RESULT_PATH", FileDialog.this.selectedFile.getPath());
                    FileDialog.this.getIntent().putExtra("SEND", true);
                    FileDialog.this.setResult(-1, FileDialog.this.getIntent());
                    FileDialog.this.finish();
                }
            }
        });
        String startPath = EXTERNAL_DIR.getAbsolutePath();
        if (startPath != null) {
            getDir(startPath);
        } else {
            getDir(this.root);
        }
    }

    private void getDir(String dirPath) {
        boolean useAutoSelection = dirPath.length() < this.currentPath.length();
        Integer position = this.lastPositions.get(this.parentPath);
        getDirImpl(dirPath);
        if (position != null && useAutoSelection) {
            getListView().setSelection(position.intValue());
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
            unselect();
            if (file.canRead()) {
                this.lastPositions.put(this.currentPath, Integer.valueOf(position));
                getDir(this.path.get(position));
                return;
            }
            new AlertDialog.Builder(this).setIcon(R.drawable.icon).setTitle("[" + file.getName() + "] " + ((Object) getText(R.string.cant_read_folder))).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.ascon.subdivformer.FileDialog.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
            return;
        }
        this.selectedFile = file;
        v.setSelected(true);
        this.selectButton.setEnabled(true);
        this.sendButton.setEnabled(true);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            unselect();
            if (!this.currentPath.equals(this.root)) {
                getDir(this.parentPath);
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void unselect() {
        this.selectButton.setEnabled(false);
    }
}
