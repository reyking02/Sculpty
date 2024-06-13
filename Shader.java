package com.ascon.subdivformer;

import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.FloatBuffer;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class Shader {
    public int program_Handle;

    public Shader(String vertexShaderCode, String fragmentShaderCode) {
        createProgram(vertexShaderCode, fragmentShaderCode);
    }

    private void createProgram(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader_Handle = GLES20.glCreateShader(35633);
        GLES20.glShaderSource(vertexShader_Handle, vertexShaderCode);
        GLES20.glCompileShader(vertexShader_Handle);
        GLES20.glGetError();
        int fragmentShader_Handle = GLES20.glCreateShader(35632);
        GLES20.glShaderSource(fragmentShader_Handle, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader_Handle);
        GLES20.glGetError();
        this.program_Handle = GLES20.glCreateProgram();
        GLES20.glAttachShader(this.program_Handle, vertexShader_Handle);
        GLES20.glGetError();
        GLES20.glAttachShader(this.program_Handle, fragmentShader_Handle);
        GLES20.glGetError();
        GLES20.glLinkProgram(this.program_Handle);
        GLES20.glGetError();
    }

    public void linkVertexBuffer(FloatBuffer vertexBuffer) {
        GLES20.glUseProgram(this.program_Handle);
        int a_vertex_Handle = GLES20.glGetAttribLocation(this.program_Handle, "a_vertex");
        GLES20.glEnableVertexAttribArray(a_vertex_Handle);
        GLES20.glVertexAttribPointer(a_vertex_Handle, 3, 5126, false, 0, (Buffer) vertexBuffer);
    }

    public void linkNormalBuffer(FloatBuffer normalBuffer) {
        GLES20.glUseProgram(this.program_Handle);
        int a_normal_Handle = GLES20.glGetAttribLocation(this.program_Handle, "a_normal");
        GLES20.glEnableVertexAttribArray(a_normal_Handle);
        GLES20.glVertexAttribPointer(a_normal_Handle, 3, 5126, false, 0, (Buffer) normalBuffer);
    }

    public void linkColorBuffer(FloatBuffer colorBuffer) {
        GLES20.glUseProgram(this.program_Handle);
        int a_color_Handle = GLES20.glGetAttribLocation(this.program_Handle, "a_color");
        GLES20.glEnableVertexAttribArray(a_color_Handle);
        GLES20.glVertexAttribPointer(a_color_Handle, 4, 5126, false, 0, (Buffer) colorBuffer);
    }

    public void linkModelViewProjectionMatrix(float[] modelViewProjectionMatrix) {
        GLES20.glUseProgram(this.program_Handle);
        int u_modelViewProjectionMatrix_Handle = GLES20.glGetUniformLocation(this.program_Handle, "u_modelViewProjectionMatrix");
        GLES20.glUniformMatrix4fv(u_modelViewProjectionMatrix_Handle, 1, false, modelViewProjectionMatrix, 0);
    }

    public void linkModelViewMatrix(float[] modelViewMatrix) {
        GLES20.glUseProgram(this.program_Handle);
        int u_modelViewMatrix_Handle = GLES20.glGetUniformLocation(this.program_Handle, "u_modelViewMatrix");
        GLES20.glUniformMatrix4fv(u_modelViewMatrix_Handle, 1, false, modelViewMatrix, 0);
    }

    public void linkCamera(float xCamera, float yCamera, float zCamera) {
        GLES20.glUseProgram(this.program_Handle);
        int u_camera_Handle = GLES20.glGetUniformLocation(this.program_Handle, "u_camera");
        GLES20.glUniform3f(u_camera_Handle, xCamera, yCamera, zCamera);
    }

    public void linkLightSource(float xLightPosition, float yLightPosition, float zLightPosition) {
        GLES20.glUseProgram(this.program_Handle);
        int u_lightPosition_Handle = GLES20.glGetUniformLocation(this.program_Handle, "u_lightPosition");
        GLES20.glUniform3f(u_lightPosition_Handle, xLightPosition, yLightPosition, zLightPosition);
    }

    public void useProgram() {
        GLES20.glUseProgram(this.program_Handle);
    }
}
