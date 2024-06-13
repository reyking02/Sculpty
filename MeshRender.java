package com.ascon.subdivformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class MeshRender implements GLSurfaceView.Renderer {
    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 20;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
    private FloatBuffer colorBuffer;
    private Context context;
    private Shader mShader;
    private Shader mTexShader;
    private float[] modelMatrix;
    public float[] modelMtr_;
    private float[] modelViewMatrix;
    private float[] modelViewProjectionMatrix;
    private Mesh model_;
    public float[] navProjection_;
    public float[] navView_;
    private FloatBuffer normalBuffer;
    private float[] projectionMatrix;
    public float[] projection_;
    private FloatBuffer vertexBuffer;
    private float[] viewMatrix;
    public float[] viewMtr_;
    public int[] viewport_;
    private float xCamera;
    private float xLightPosition;
    private float yCamera;
    private float yLightPosition;
    private float zCamera;
    private float zLightPosition;
    float[] modelGab_ = {-100.0f, -100.0f, -100.0f, 100.0f, 100.0f, 100.0f};
    float[] modelGabCenter_ = {0.0f, 0.0f, 0.0f, 1.0f};
    float modelGabDiag_ = 350.0f;
    private final float[] mTriangleVerticesData = {-1.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 0.0f, 1.0f};
    private boolean perspective_ = false;
    public boolean drawAmbient_ = false;
    public boolean saveToFile_ = false;
    public float zDisp_ = 0.0f;
    float moveX_ = 0.0f;
    float moveY_ = 0.0f;
    public String fileName_ = null;
    public int textureTop = 0;
    public int textureLeft = 0;
    public int textureFront = 0;
    public int textureBack = 0;
    public int textureFree = 0;
    public int textureColor = 0;
    public boolean showNavButtons_ = false;
    public ViewMode viewMode_ = ViewMode.iso;
    public int btnZoom = 2;
    public float ratio_ = 1.0f;
    public float scale_ = 1.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    public enum ViewMode {
        top,
        bottom,
        left,
        right,
        front,
        back,
        iso
    }

    public native int drawAmbientModel(int i);

    public native int drawColorSphereModel(int i);

    public native int drawCurrentModel(int i);

    public native int drawNavSphereModel(int i);

    public MeshRender(Mesh model, Context context_) {
        this.modelMtr_ = null;
        this.viewMtr_ = null;
        this.navView_ = null;
        this.navProjection_ = null;
        this.projection_ = null;
        this.viewport_ = null;
        this.model_ = model;
        this.viewport_ = new int[4];
        this.modelMtr_ = new float[16];
        Matrix.setIdentityM(this.modelMtr_, 0);
        this.viewMtr_ = new float[16];
        Matrix.setIdentityM(this.viewMtr_, 0);
        InitIsoViewMatrix(this.viewMtr_);
        this.navView_ = new float[16];
        Matrix.setIdentityM(this.navView_, 0);
        InitIsoNavMatrix(this.navView_);
        this.projection_ = new float[16];
        Matrix.setIdentityM(this.projection_, 0);
        this.navProjection_ = new float[16];
        Matrix.setIdentityM(this.navProjection_, 0);
        Matrix.orthoM(this.navProjection_, 0, -45.0f, 45.0f, -45.0f, 45.0f, -45.0f, 45.0f);
        this.context = context_;
        this.modelViewMatrix = new float[16];
        this.modelViewProjectionMatrix = new float[16];
    }

    private void InitIsoViewMatrix(float[] viewMtr) {
        Matrix.setIdentityM(viewMtr, 0);
        if (this.viewMode_ == ViewMode.iso) {
            Matrix.rotateM(viewMtr, 0, -45.0f, 1.0f, 0.0f, 0.0f);
            Matrix.rotateM(viewMtr, 0, 45.0f, 0.0f, 0.0f, 1.0f);
        } else if (this.viewMode_ != ViewMode.top) {
            if (this.viewMode_ == ViewMode.front) {
                Matrix.rotateM(viewMtr, 0, -90.0f, 1.0f, 0.0f, 0.0f);
            } else if (this.viewMode_ == ViewMode.back) {
                Matrix.rotateM(viewMtr, 0, 90.0f, 1.0f, 0.0f, 0.0f);
            } else if (this.viewMode_ == ViewMode.left) {
                Matrix.rotateM(viewMtr, 0, -90.0f, 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(viewMtr, 0, 90.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }

    private void InitIsoNavMatrix(float[] viewMtr) {
        Matrix.setIdentityM(viewMtr, 0);
        if (this.viewMode_ == ViewMode.iso) {
            Matrix.rotateM(viewMtr, 0, 45.0f, 1.0f, 0.0f, 0.0f);
            Matrix.rotateM(viewMtr, 0, 45.0f, 0.0f, 0.0f, 1.0f);
        } else if (this.viewMode_ != ViewMode.top) {
            if (this.viewMode_ == ViewMode.front) {
                Matrix.rotateM(viewMtr, 0, 90.0f, 1.0f, 0.0f, 0.0f);
            } else if (this.viewMode_ == ViewMode.back) {
                Matrix.rotateM(viewMtr, 0, -90.0f, 1.0f, 0.0f, 0.0f);
            } else if (this.viewMode_ == ViewMode.left) {
                Matrix.rotateM(viewMtr, 0, 90.0f, 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(viewMtr, 0, 90.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }

    private void initProjectionMatrix() {
        float persDisp = 3.0f * this.modelGabDiag_ * this.scale_;
        if (this.perspective_) {
            Matrix.frustumM(this.projection_, 0, (-this.ratio_) * 150.0f, this.ratio_ * 150.0f, -150.0f, 150.0f, persDisp - ((this.modelGabDiag_ * 0.5f) * this.scale_), (this.modelGabDiag_ * 0.5f * this.scale_) + persDisp);
        } else {
            Matrix.orthoM(this.projection_, 0, (-this.ratio_) * 150.0f, this.ratio_ * 150.0f, -150.0f, 150.0f, 2.0f * this.modelGabDiag_ * this.scale_, 4.0f * this.modelGabDiag_ * this.scale_);
        }
        Matrix.translateM(this.projection_, 0, 0.0f, 0.0f, -persDisp);
        Matrix.translateM(this.projection_, 0, this.moveX_, this.moveY_, 0.0f);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl) {
        OutputStream outStream;
        GLES20.glClear(16640);
        gl.glClear(16640);
        gl.glEnable(3042);
        gl.glBlendFunc(770, 771);
        float[] mvpMatrix = new float[16];
        Matrix.setIdentityM(this.modelMtr_, 0);
        float[] modelViewMtr = new float[16];
        float[] navMatrix = new float[16];
        Matrix.multiplyMM(modelViewMtr, 0, this.viewMtr_, 0, this.modelMtr_, 0);
        Matrix.multiplyMM(mvpMatrix, 0, this.projection_, 0, modelViewMtr, 0);
        Matrix.multiplyMM(navMatrix, 0, this.navView_, 0, this.navProjection_, 0);
        gl.glViewport(this.viewport_[0], this.viewport_[1], this.viewport_[2], this.viewport_[3]);
        this.mShader.linkModelViewProjectionMatrix(mvpMatrix);
        this.mShader.linkModelViewMatrix(navMatrix);
        this.mShader.linkLightSource(0.0f, 0.0f, 0.0f);
        if (this.drawAmbient_) {
            drawAmbientModel(this.mShader.program_Handle);
        } else {
            drawCurrentModel(this.mShader.program_Handle);
        }
        if (this.saveToFile_) {
            ByteBuffer buffer = ByteBuffer.allocate(this.viewport_[2] * this.viewport_[3] * 4);
            gl.glReadPixels(0, 0, this.viewport_[2], this.viewport_[3], 6408, 5121, buffer);
            Bitmap bm = Bitmap.createBitmap(this.viewport_[2], this.viewport_[3], Bitmap.Config.ARGB_8888);
            bm.copyPixelsFromBuffer(buffer);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, this.viewport_[2] / 4, (-this.viewport_[3]) / 4, false);
            File file = new File(this.fileName_);
            try {
                outStream = new FileOutputStream(file);
            } catch (Exception e) {
                e = e;
            }
            try {
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e2) {
                e = e2;
                System.out.println("Fail to write screenshot");
                e.printStackTrace();
                this.saveToFile_ = false;
                gl.glViewport(120, 0, 120, 120);
                this.mShader.linkModelViewProjectionMatrix(navMatrix);
                drawColorSphereModel(this.mShader.program_Handle);
                GLES20.glClear(256);
                gl.glClear(256);
                gl.glViewport(0, 0, 120, 120);
                this.mShader.linkModelViewProjectionMatrix(navMatrix);
                drawNavSphereModel(this.mShader.program_Handle);
                DrawNavButtons(gl);
            }
            this.saveToFile_ = false;
        }
        gl.glViewport(120, 0, 120, 120);
        this.mShader.linkModelViewProjectionMatrix(navMatrix);
        drawColorSphereModel(this.mShader.program_Handle);
        GLES20.glClear(256);
        gl.glClear(256);
        gl.glViewport(0, 0, 120, 120);
        this.mShader.linkModelViewProjectionMatrix(navMatrix);
        drawNavSphereModel(this.mShader.program_Handle);
        DrawNavButtons(gl);
    }

    public void DrawNavButtons(GL10 gl) {
        float[] btnMatrix = new float[16];
        Matrix.setIdentityM(btnMatrix, 0);
        GLES20.glUseProgram(this.mTexShader.program_Handle);
        int maPositionHandle = GLES20.glGetAttribLocation(this.mTexShader.program_Handle, "vPosition");
        int maTextureHandle = GLES20.glGetAttribLocation(this.mTexShader.program_Handle, "aTextureCoord");
        int muMVPMatrixHandle = GLES20.glGetUniformLocation(this.mTexShader.program_Handle, "u_modelViewProjectionMatrix");
        FloatBuffer mTriangleVertices = ByteBuffer.allocateDirect(this.mTriangleVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangleVertices.put(this.mTriangleVerticesData).position(0);
        mTriangleVertices.position(0);
        GLES20.glVertexAttribPointer(maPositionHandle, 3, 5126, false, (int) TRIANGLE_VERTICES_DATA_STRIDE_BYTES, (Buffer) mTriangleVertices);
        mTriangleVertices.position(3);
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glVertexAttribPointer(maTextureHandle, 2, 5126, false, (int) TRIANGLE_VERTICES_DATA_STRIDE_BYTES, (Buffer) mTriangleVertices);
        GLES20.glEnableVertexAttribArray(maTextureHandle);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, btnMatrix, 0);
        if (this.showNavButtons_) {
            gl.glViewport(this.btnZoom * 0, (this.btnZoom * 64) + 120, this.btnZoom * 64, this.btnZoom * 64);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.textureTop);
            GLES20.glDrawArrays(4, 0, 6);
            gl.glViewport(this.btnZoom * 64, (this.btnZoom * 64) + 120, this.btnZoom * 64, this.btnZoom * 64);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.textureLeft);
            GLES20.glDrawArrays(4, 0, 6);
            gl.glViewport(this.btnZoom * 0, 120, this.btnZoom * 64, this.btnZoom * 64);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.textureFront);
            GLES20.glDrawArrays(4, 0, 6);
            gl.glViewport(this.btnZoom * 64, 120, this.btnZoom * 64, this.btnZoom * 64);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.textureBack);
            GLES20.glDrawArrays(4, 0, 6);
            gl.glViewport(this.btnZoom * 128, 120, this.btnZoom * 64, this.btnZoom * 64);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.textureFree);
            GLES20.glDrawArrays(4, 0, 6);
        }
        gl.glViewport(148, 28, 64, 64);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.textureColor);
        GLES20.glDrawArrays(4, 0, 6);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glGetString(7938);
        this.viewport_[0] = 0;
        this.viewport_[1] = 0;
        this.viewport_[2] = width;
        this.viewport_[3] = height;
        this.ratio_ = width / height;
        Matrix.setIdentityM(this.projection_, 0);
        Matrix.orthoM(this.projection_, 0, this.ratio_ * (-100.0f), this.ratio_ * 100.0f, -100.0f, 100.0f, -10000.0f, 10000.0f);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(2929);
        GLES20.glDisable(2884);
        this.mShader = new Shader("uniform mat4 u_modelViewProjectionMatrix;\nuniform mat4 u_modelViewMatrix;\nuniform vec3 u_lightPosition;\nattribute vec3 vPosition;\nattribute vec3 a_normal;\nvarying lowp vec4 colorVarying;\nuniform vec4 fColor;\nvoid main() {\nvec3 eyeNormal = normalize(a_normal);\nvec3 lightPosition = vec3(0.0, 0.0, -1000.0);\nvec4 lp = vec4(lightPosition, 1.0) * u_modelViewProjectionMatrix;\nvec4 light_vector = lp;\nvec3 lightvector = normalize(light_vector.xyz-vPosition);\nvec3 reflectvector = reflect(-lightvector, eyeNormal);\nvec4 diffuseColor = fColor;\nvec4 ambientColor = fColor*0.3;\nfloat nDotVP = max(0.0, dot(eyeNormal, normalize(lp.xyz)));\ncolorVarying = vec4(diffuseColor.xyz * nDotVP + ambientColor.xyz, fColor.w);\ngl_Position = u_modelViewProjectionMatrix * vec4(vPosition,1.0);\n}\n", "varying lowp vec4 colorVarying;\nvoid main() {\ngl_FragColor = colorVarying;\n}\n");
        this.mTexShader = new Shader("uniform mat4 u_modelViewProjectionMatrix;\nattribute vec3 vPosition;\nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = u_modelViewProjectionMatrix * vec4(vPosition,1.0);\n  vTextureCoord = aTextureCoord;\n}\n", "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
        this.textureTop = loadTexture(R.drawable.top);
        this.textureLeft = loadTexture(R.drawable.left);
        this.textureFront = loadTexture(R.drawable.front);
        this.textureBack = loadTexture(R.drawable.back);
        this.textureFree = loadTexture(R.drawable.free);
        this.textureColor = loadTexture(R.drawable.select_color);
    }

    public void pick(int x, int y, float[] pickLine) {
        float[] newcoords = new float[4];
        float winx = x;
        float winy = this.viewport_[3] - y;
        float[] modelViewMtr = new float[16];
        Matrix.multiplyMM(modelViewMtr, 0, this.viewMtr_, 0, this.modelMtr_, 0);
        GLU.gluUnProject(winx, winy, 0.0f, modelViewMtr, 0, this.projection_, 0, this.viewport_, 0, newcoords, 0);
        float[] newcoords2 = convertTo3d(newcoords);
        pickLine[0] = newcoords2[0];
        pickLine[1] = newcoords2[1];
        pickLine[2] = newcoords2[2];
        GLU.gluUnProject(winx, winy, 1.0f, modelViewMtr, 0, this.projection_, 0, this.viewport_, 0, newcoords2, 0);
        float[] newcoords3 = convertTo3d(newcoords2);
        pickLine[3] = newcoords3[0];
        pickLine[4] = newcoords3[1];
        pickLine[5] = newcoords3[2];
        this.model_.setPickLine(pickLine);
    }

    private float[] convertTo3d(float[] vector) {
        float[] result = new float[4];
        for (int index = 0; index < vector.length; index++) {
            result[index] = vector[index] / vector[3];
        }
        return result;
    }

    public void move(float dx, float dy) {
        this.moveX_ += dx;
        this.moveY_ += dy;
        Matrix.translateM(this.projection_, 0, dx, dy, 0.0f);
    }

    public void rotate(float dx, float dy) {
        if (this.viewMode_ == ViewMode.iso) {
            float dy2 = dy * (-1.0f);
            float[] move = {dy2, -dx, 0.0f, 0.0f};
            float[] modelGabCenter = new float[4];
            Matrix.multiplyMV(modelGabCenter, 0, this.viewMtr_, 0, this.modelGabCenter_, 0);
            if (Math.abs(modelGabCenter[2]) > 0.001f) {
                throw new RuntimeException("View Matrix is not valid.");
            }
            Matrix.translateM(this.viewMtr_, 0, this.modelGabCenter_[0], this.modelGabCenter_[1], this.modelGabCenter_[2]);
            float[] viewInvMtr = new float[16];
            if (!Matrix.invertM(viewInvMtr, 0, this.viewMtr_, 0)) {
                throw new IllegalArgumentException("ModelView is not invertible.");
            }
            float[] moveView = new float[4];
            Matrix.multiplyMV(moveView, 0, viewInvMtr, 0, move, 0);
            if (!Matrix.invertM(viewInvMtr, 0, this.navView_, 0)) {
                throw new IllegalArgumentException("ModelView is not invertible.");
            }
            float[] moveNavView = new float[4];
            Matrix.multiplyMV(moveNavView, 0, viewInvMtr, 0, move, 0);
            float angle = (float) Math.hypot(dx, dy2);
            Matrix.rotateM(this.viewMtr_, 0, angle, moveView[0], moveView[1], moveView[2]);
            Matrix.rotateM(this.navView_, 0, -angle, moveNavView[0], moveNavView[1], moveNavView[2]);
            Matrix.translateM(this.viewMtr_, 0, -this.modelGabCenter_[0], -this.modelGabCenter_[1], -this.modelGabCenter_[2]);
            float[] modelGabCenter2 = new float[4];
            Matrix.multiplyMV(modelGabCenter2, 0, this.viewMtr_, 0, this.modelGabCenter_, 0);
            if (Math.abs(modelGabCenter2[2]) > 0.001f) {
                throw new RuntimeException("View Matrix is not valid.");
            }
        }
    }

    public void scale(float factor, float summand) {
        float tmpScale = this.scale_;
        this.scale_ *= factor;
        this.scale_ += summand;
        if (this.scale_ < 0.001f) {
            this.scale_ = 0.001f;
        }
        if (this.scale_ > 1000.0f) {
            this.scale_ = 1000.0f;
        }
        float tmpScale2 = this.scale_ / tmpScale;
        Matrix.scaleM(this.viewMtr_, 0, tmpScale2, tmpScale2, tmpScale2);
        normalizeViewMatrix();
    }

    public void setDrawGabarit(float[] gab3d) {
        this.modelGab_ = gab3d;
        this.modelGabCenter_[0] = (this.modelGab_[0] + this.modelGab_[3]) * 0.5f;
        this.modelGabCenter_[1] = (this.modelGab_[1] + this.modelGab_[4]) * 0.5f;
        this.modelGabCenter_[2] = (this.modelGab_[2] + this.modelGab_[5]) * 0.5f;
        float dx = this.modelGab_[3] - this.modelGab_[0];
        float dy = this.modelGab_[4] - this.modelGab_[1];
        float dz = this.modelGab_[5] - this.modelGab_[2];
        this.modelGabDiag_ = (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
        normalizeViewMatrix();
    }

    public void normalizeViewMatrix() {
        float[] gabCenter = new float[4];
        Matrix.multiplyMV(gabCenter, 0, this.viewMtr_, 0, this.modelGabCenter_, 0);
        this.viewMtr_[14] = this.viewMtr_[14] - gabCenter[2];
        float[] modelGabCenter = new float[4];
        Matrix.multiplyMV(modelGabCenter, 0, this.viewMtr_, 0, this.modelGabCenter_, 0);
        if (Math.abs(modelGabCenter[2]) > 0.001f) {
            throw new RuntimeException("View Matrix is not valid.");
        }
    }

    public void showAll() {
        InitIsoViewMatrix(this.viewMtr_);
        InitIsoNavMatrix(this.navView_);
        this.scale_ = (float) (150.0d / this.modelGabDiag_);
        Matrix.scaleM(this.viewMtr_, 0, this.scale_, this.scale_, this.scale_);
        normalizeViewMatrix();
        float[] moveVect = new float[4];
        Matrix.multiplyMV(moveVect, 0, this.viewMtr_, 0, this.modelGabCenter_, 0);
        Matrix.setIdentityM(this.projection_, 0);
        Matrix.orthoM(this.projection_, 0, (-100.0f) * this.ratio_, 100.0f * this.ratio_, -100.0f, 100.0f, -10000.0f, 10000.0f);
        Matrix.translateM(this.projection_, 0, -moveVect[0], -moveVect[1], 0.0f);
    }

    public int loadTexture(int resId) {
        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        if (textureHandle[0] != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), resId, options);
            GLES20.glBindTexture(3553, textureHandle[0]);
            GLES20.glTexParameteri(3553, 10241, 9728);
            GLES20.glTexParameteri(3553, 10240, 9728);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
            bitmap.recycle();
        }
        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
        return textureHandle[0];
    }

    public void showNavButtons() {
        this.showNavButtons_ = true;
    }

    public void hideNavButtons() {
        this.showNavButtons_ = false;
    }
}
