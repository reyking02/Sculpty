package com.ascon.subdivformer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class Mesh {
    public ShortBuffer indexBuffer_;
    FloatBuffer linesBuffer;
    FloatBuffer mVertexBuffer;
    public float[] normals;
    FloatBuffer normalsBuffer;
    public FloatBuffer normalsBuffer_;
    FloatBuffer triangleBuffer;
    public FloatBuffer vertexBuffer_;
    public int triangleCount_ = 0;
    public float[] pickLine_ = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public float[] triangle = {0.0f, 0.0f, 0.0f, 100.0f, 100.0f, 0.0f, 100.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 100.0f, 0.0f, 100.0f, 100.0f, 0.0f, 0.0f, 0.0f, 100.0f, 100.0f, 0.0f, 100.0f, 100.0f, 100.0f, 100.0f, 0.0f, 0.0f, 100.0f, 100.0f, 100.0f, 100.0f, 0.0f, 100.0f, 100.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 100.0f, 0.0f, 100.0f, 0.0f, 0.0f, 0.0f, 100.0f, 0.0f, 100.0f, 100.0f, 0.0f, 100.0f, 0.0f, 100.0f, 0.0f, 0.0f, 100.0f, 100.0f, 0.0f, 100.0f, 0.0f, 100.0f, 100.0f, 0.0f, 100.0f, 100.0f, 100.0f, 0.0f, 100.0f, 100.0f, 100.0f, 0.0f, 0.0f, 0.0f, 100.0f, 0.0f, 0.0f, 100.0f, 0.0f, 100.0f, 0.0f, 0.0f, 0.0f, 100.0f, 0.0f, 100.0f, 0.0f, 0.0f, 100.0f, 0.0f, 100.0f, 0.0f, 100.0f, 100.0f, 100.0f, 100.0f, 100.0f, 0.0f, 0.0f, 100.0f, 0.0f, 0.0f, 100.0f, 100.0f, 100.0f, 100.0f, 100.0f};
    public float[] lines_arr = {0.0f, 0.0f, 0.0f, 0.0f, 1000.0f, 0.0f, 0.0f, 1000.0f, 0.0f, 1000.0f, 1000.0f, 0.0f, 1000.0f, 1000.0f, 0.0f, 1000.0f, 0.0f, 0.0f, 1000.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1000.0f, 0.0f, 1000.0f, 1000.0f, 0.0f, 1000.0f, 1000.0f, 1000.0f, 1000.0f, 1000.0f, 1000.0f, 1000.0f, 1000.0f, 1000.0f, 0.0f, 1000.0f, 1000.0f, 0.0f, 1000.0f, 0.0f, 0.0f, 1000.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1000.0f, 1000.0f, 0.0f, 0.0f, 1000.0f, 0.0f, 1000.0f, 0.0f, 1000.0f, 0.0f, 0.0f, 1000.0f, 1000.0f, 1000.0f, 1000.0f, 0.0f, 1000.0f, 1000.0f, 1000.0f};

    public Mesh() {
        float[] verticesFB = {-5.0f, -5.0f, 6.0f, 5.0f, -5.0f, 6.0f, -5.0f, 5.0f, 6.0f, 5.0f, -5.0f, 6.0f, 5.0f, 5.0f, 6.0f, -5.0f, 5.0f, 6.0f, -5.0f, -5.0f, -6.0f, 5.0f, -5.0f, -6.0f, -5.0f, 5.0f, -6.0f, 5.0f, -5.0f, -6.0f, 5.0f, 5.0f, -6.0f, -5.0f, 5.0f, -6.0f, -5.0f, 5.0f, 6.0f, 5.0f, 5.0f, 6.0f, -5.5f, 5.5f, 5.5f, 5.0f, 5.0f, 6.0f, 5.5f, 5.5f, 5.5f, -5.5f, 5.5f, 5.5f, -5.0f, -5.0f, 6.0f, 5.0f, -5.0f, 6.0f, -5.5f, -5.5f, 5.5f, 5.0f, -5.0f, 6.0f, 5.5f, -5.5f, 5.5f, -5.5f, -5.5f, 5.5f, 5.0f, 5.0f, 6.0f, 5.5f, 5.5f, 5.5f, 5.5f, -5.5f, 5.5f, 5.0f, 5.0f, 6.0f, 5.5f, -5.5f, 5.5f, 5.0f, -5.0f, 6.0f, -5.0f, 5.0f, 6.0f, -5.5f, 5.5f, 5.5f, -5.5f, -5.5f, 5.5f, -5.0f, 5.0f, 6.0f, -5.5f, -5.5f, 5.5f, -5.0f, -5.0f, 6.0f, -5.0f, 5.0f, -6.0f, 5.0f, 5.0f, -6.0f, -5.5f, 5.5f, -5.5f, 5.0f, 5.0f, -6.0f, 5.5f, 5.5f, -5.5f, -5.5f, 5.5f, -5.5f, -5.0f, -5.0f, -6.0f, 5.0f, -5.0f, -6.0f, -5.5f, -5.5f, -5.5f, 5.0f, -5.0f, -6.0f, 5.5f, -5.5f, -5.5f, -5.5f, -5.5f, -5.5f, 5.0f, 5.0f, -6.0f, 5.5f, 5.5f, -5.5f, 5.5f, -5.5f, -5.5f, 5.0f, 5.0f, -6.0f, 5.5f, -5.5f, -5.5f, 5.0f, -5.0f, -6.0f, -5.0f, 5.0f, -6.0f, -5.5f, 5.5f, -5.5f, -5.5f, -5.5f, -5.5f, -5.0f, 5.0f, -6.0f, -5.5f, -5.5f, -5.5f, -5.0f, -5.0f, -6.0f, 6.0f, -5.0f, -5.0f, 6.0f, -5.0f, 5.0f, 6.0f, 5.0f, -5.0f, 6.0f, -5.0f, 5.0f, 6.0f, 5.0f, 5.0f, 6.0f, 5.0f, -5.0f, -6.0f, -5.0f, -5.0f, -6.0f, -5.0f, 5.0f, -6.0f, 5.0f, -5.0f, -6.0f, -5.0f, 5.0f, -6.0f, 5.0f, 5.0f, -6.0f, 5.0f, -5.0f, 6.0f, 5.0f, -5.0f, 6.0f, 5.0f, 5.0f, 5.5f, 5.5f, -5.5f, 6.0f, 5.0f, 5.0f, 5.5f, 5.5f, 5.5f, 5.5f, 5.5f, -5.5f, 6.0f, -5.0f, -5.0f, 6.0f, -5.0f, 5.0f, -5.5f, -5.5f, -5.5f, 6.0f, -5.0f, 5.0f, 5.5f, -5.5f, 5.5f, 5.5f, -5.5f, -5.5f, 6.0f, 5.0f, 5.0f, 5.5f, 5.5f, 5.5f, 5.5f, -5.5f, 5.5f, 6.0f, 5.0f, 5.0f, 5.5f, -5.5f, 5.5f, 6.0f, -5.0f, 5.0f, 6.0f, 5.0f, -5.0f, 5.5f, 5.5f, -5.5f, 5.5f, -5.5f, -5.5f, 6.0f, 5.0f, -5.0f, 5.5f, -5.5f, -5.5f, 6.0f, -5.0f, -5.0f, -6.0f, 5.0f, -5.0f, -6.0f, 5.0f, 5.0f, 5.5f, 5.5f, -5.5f, -6.0f, 5.0f, 5.0f, -5.5f, 5.5f, 5.5f, -5.5f, 5.5f, -5.5f, -6.0f, -5.0f, -5.0f, -6.0f, -5.0f, 5.0f, -5.5f, -5.5f, -5.5f, -6.0f, -5.0f, 5.0f, -5.5f, -5.5f, 5.5f, -5.5f, -5.5f, -5.5f, -6.0f, 5.0f, 5.0f, -5.5f, 5.5f, 5.5f, -5.5f, -5.5f, 5.5f, -6.0f, 5.0f, 5.0f, -5.5f, -5.5f, 5.5f, -6.0f, -5.0f, 5.0f, -6.0f, 5.0f, -5.0f, -5.5f, 5.5f, -5.5f, -5.5f, -5.5f, -5.5f, -6.0f, 5.0f, -5.0f, -5.5f, -5.5f, -5.5f, -6.0f, -5.0f, -5.0f, -5.0f, 6.0f, -5.0f, 5.0f, 6.0f, -5.0f, -5.0f, 6.0f, 5.0f, 5.0f, 6.0f, -5.0f, 5.0f, 6.0f, 5.0f, -5.0f, 6.0f, 5.0f, -5.0f, -6.0f, -5.0f, 5.0f, -6.0f, -5.0f, -5.0f, -6.0f, 5.0f, 5.0f, -6.0f, -5.0f, 5.0f, -6.0f, 5.0f, -5.0f, -6.0f, 5.0f, -5.0f, 6.0f, 5.0f, 5.0f, 6.0f, 5.0f, -5.5f, 5.5f, 5.5f, 5.0f, 6.0f, 5.0f, 5.5f, 5.5f, 5.5f, -5.5f, 5.5f, 5.5f, -5.0f, 6.0f, -5.0f, 5.0f, 6.0f, -5.0f, -5.5f, 5.5f, -5.5f, 5.0f, 6.0f, -5.0f, 5.5f, 5.5f, -5.5f, -5.5f, 5.5f, -5.5f, 5.0f, 6.0f, 5.0f, 5.5f, 5.5f, 5.5f, 5.5f, 5.5f, -5.5f, 5.0f, 6.0f, 5.0f, 5.5f, 5.5f, -5.5f, 5.0f, 6.0f, -5.0f, -5.0f, 6.0f, 5.0f, -5.5f, 5.5f, 5.5f, -5.5f, 5.5f, -5.5f, -5.0f, 6.0f, 5.0f, -5.5f, 5.5f, -5.5f, -5.0f, 6.0f, -5.0f, -5.0f, -6.0f, 5.0f, 5.0f, -6.0f, 5.0f, -5.5f, -5.5f, 5.5f, 5.0f, -6.0f, 5.0f, 5.5f, -5.5f, 5.5f, -5.5f, -5.5f, 5.5f, -5.0f, -6.0f, -5.0f, 5.0f, -6.0f, -5.0f, -5.5f, -5.5f, -5.5f, 5.0f, -6.0f, -5.0f, 5.5f, -5.5f, -5.5f, -5.5f, -5.5f, -5.5f, 5.0f, -6.0f, 5.0f, 5.5f, -5.5f, 5.5f, 5.5f, -5.5f, -5.5f, 5.0f, -6.0f, 5.0f, 5.5f, -5.5f, -5.5f, 5.0f, -6.0f, -5.0f, -5.0f, -6.0f, 5.0f, -5.5f, -5.5f, 5.5f, -5.5f, -5.5f, -5.5f, -5.0f, -6.0f, 5.0f, -5.5f, -5.5f, -5.5f, -5.0f, -6.0f, -5.0f};
        ByteBuffer vbb = ByteBuffer.allocateDirect(verticesFB.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        this.mVertexBuffer = vbb.asFloatBuffer();
        this.mVertexBuffer.put(verticesFB);
        this.mVertexBuffer.position(0);
    }

    public void setTrialngles() {
    }

    public void draw(GL10 gl) {
        gl.glEnable(2977);
        gl.glDisable(2896);
        gl.glEnable(16384);
        gl.glEnableClientState(32884);
        gl.glFrontFace(2305);
        gl.glEnable(2929);
        gl.glEnableClientState(32885);
        gl.glVertexPointer(3, 5126, 0, this.mVertexBuffer);
        gl.glColor4f(0.0f, 0.8f, 0.0f, 1.0f);
        gl.glDrawArrays(4, 0, 60);
        gl.glColor4f(0.8f, 0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(4, 60, 60);
        gl.glColor4f(0.0f, 0.0f, 0.8f, 1.0f);
        gl.glDrawArrays(4, 120, 60);
        gl.glDrawElements(4, this.triangleCount_ * 3, 5123, this.indexBuffer_);
        gl.glDisable(2896);
        gl.glDisableClientState(32885);
        gl.glDisableClientState(32884);
        gl.glDisable(3042);
    }

    public void setPickLine(float[] pickLine) {
        for (int i = 0; i < 6; i++) {
            this.pickLine_[i] = pickLine[i];
        }
    }
}
