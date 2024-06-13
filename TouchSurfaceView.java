package com.ascon.subdivformer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.ascon.subdivformer.MeshRender;

/* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
public class TouchSurfaceView extends GLSurfaceView {
    private final float TOUCH_MOVE_FACTOR;
    private final float TOUCH_SCALE_FACTOR;
    private final float TRACKBALL_SCALE_FACTOR;
    private GestureDetector gestureDetector_;
    private float lastMoveX_;
    private float lastMoveY_;
    private float mPreviousX;
    private float mPreviousY;
    private MeshRender mRenderer;
    private int prevCount_;
    private ScaleGestureDetector scaleGestureDetector_;
    public boolean selectMode_;
    public int selectedFaceName_;
    public boolean viewOnly_;

    public native int modellerPickFace(float f, float f2, float f3, float f4, float f5, float f6);

    public native int modellerPickFaceColor(float f, float f2, float f3, float f4, float f5, float f6);

    public TouchSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TOUCH_SCALE_FACTOR = 0.5625f;
        this.TOUCH_MOVE_FACTOR = 0.5f;
        this.TRACKBALL_SCALE_FACTOR = 36.0f;
        this.selectedFaceName_ = 0;
        this.selectMode_ = false;
        this.viewOnly_ = false;
        this.lastMoveX_ = 0.0f;
        this.lastMoveY_ = 0.0f;
        setEGLContextClientVersion(2);
    }

    public TouchSurfaceView(Context context) {
        super(context);
        this.TOUCH_SCALE_FACTOR = 0.5625f;
        this.TOUCH_MOVE_FACTOR = 0.5f;
        this.TRACKBALL_SCALE_FACTOR = 36.0f;
        this.selectedFaceName_ = 0;
        this.selectMode_ = false;
        this.viewOnly_ = false;
        this.lastMoveX_ = 0.0f;
        this.lastMoveY_ = 0.0f;
        this.mRenderer = null;
        this.scaleGestureDetector_ = null;
        this.gestureDetector_ = null;
        setEGLContextClientVersion(2);
    }

    public void initRenderer(MeshRender renderer) {
        setRenderer(renderer);
        this.mRenderer = renderer;
        this.scaleGestureDetector_ = new ScaleGestureDetector(getContext(), new ScaleListener());
        this.gestureDetector_ = new GestureDetector(getContext(), new GestureListener());
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent e) {
        if (this.mRenderer != null) {
            requestRender();
            return true;
        }
        return true;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent e) {
        if (this.scaleGestureDetector_ != null) {
            this.scaleGestureDetector_.onTouchEvent(e);
        }
        if (this.gestureDetector_ != null) {
            this.gestureDetector_.onTouchEvent(e);
            return true;
        }
        return true;
    }

    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector detector) {
            if (TouchSurfaceView.this.mRenderer != null) {
                float scaleFactor = detector.getScaleFactor();
                if (scaleFactor < 0.95d || scaleFactor > 1.05d) {
                    TouchSurfaceView.this.mRenderer.scale(scaleFactor, 0.0f);
                    TouchSurfaceView.this.invalidate();
                    TouchSurfaceView.this.requestRender();
                    TouchSurfaceView.this.lastMoveX_ = 0.0f;
                    TouchSurfaceView.this.lastMoveY_ = 0.0f;
                    return true;
                } else if (TouchSurfaceView.this.lastMoveX_ != 0.0f && TouchSurfaceView.this.lastMoveY_ != 0.0f) {
                    float x = detector.getFocusX() - TouchSurfaceView.this.lastMoveX_;
                    float y = detector.getFocusY() - TouchSurfaceView.this.lastMoveY_;
                    TouchSurfaceView.this.lastMoveX_ = detector.getFocusX();
                    TouchSurfaceView.this.lastMoveY_ = detector.getFocusY();
                    TouchSurfaceView.this.mRenderer.move(x / 3.0f, (-y) / 3.0f);
                    TouchSurfaceView.this.invalidate();
                    TouchSurfaceView.this.requestRender();
                    return true;
                } else {
                    TouchSurfaceView.this.lastMoveX_ = detector.getFocusX();
                    TouchSurfaceView.this.lastMoveY_ = detector.getFocusY();
                    return true;
                }
            }
            return true;
        }
    }

    /* loaded from: /storage/emulated/0/Documents/jadec/sources/com.royalegames.sculpty/dex-files/0.dex */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private GestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent e) {
            if (!TouchSurfaceView.this.viewOnly_ && TouchSurfaceView.this.mRenderer != null) {
                ((SubDivFormer) TouchSurfaceView.this.getContext()).clearSelection();
                TouchSurfaceView.this.requestRender();
            }
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent e) {
            TouchSurfaceView.this.lastMoveX_ = 0.0f;
            TouchSurfaceView.this.lastMoveY_ = 0.0f;
            if (!TouchSurfaceView.this.viewOnly_ && ((SubDivFormer) TouchSurfaceView.this.getContext()).toolState_) {
                ((SubDivFormer) TouchSurfaceView.this.getContext()).updateUndoContainer();
                return true;
            }
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent e) {
            ((SubDivFormer) TouchSurfaceView.this.getContext()).fullScreenOff();
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent e) {
            TouchSurfaceView.this.lastMoveX_ = 0.0f;
            TouchSurfaceView.this.lastMoveY_ = 0.0f;
            float x = e.getX();
            float y = e.getY();
            int k = TouchSurfaceView.this.mRenderer.btnZoom;
            if (TouchSurfaceView.this.mRenderer != null) {
                if (x > 130.0f && x < 230.0f && y > TouchSurfaceView.this.mRenderer.viewport_[3] - 110 && y < TouchSurfaceView.this.mRenderer.viewport_[3] - 10) {
                    ((SubDivFormer) TouchSurfaceView.this.getContext()).openColorDialog();
                } else if (x <= 10.0f || x >= 110.0f || y <= TouchSurfaceView.this.mRenderer.viewport_[3] - 110 || y >= TouchSurfaceView.this.mRenderer.viewport_[3] - 10) {
                    if (TouchSurfaceView.this.mRenderer.showNavButtons_) {
                        if (x > 0.0f && x < k * 64 && y > (TouchSurfaceView.this.mRenderer.viewport_[3] - 120) - (k * 64) && y < TouchSurfaceView.this.mRenderer.viewport_[3] - 120) {
                            TouchSurfaceView.this.mRenderer.viewMode_ = MeshRender.ViewMode.front;
                            TouchSurfaceView.this.mRenderer.showAll();
                            TouchSurfaceView.this.requestRender();
                        } else if (x > k * 65 && x < k * 128 && y > (TouchSurfaceView.this.mRenderer.viewport_[3] - 120) - (k * 64) && y < TouchSurfaceView.this.mRenderer.viewport_[3] - 120) {
                            TouchSurfaceView.this.mRenderer.viewMode_ = MeshRender.ViewMode.back;
                            TouchSurfaceView.this.mRenderer.showAll();
                            TouchSurfaceView.this.requestRender();
                        } else if (x > 0.0f && x < k * 64 && y > (TouchSurfaceView.this.mRenderer.viewport_[3] - 120) - (k * 128) && y < (TouchSurfaceView.this.mRenderer.viewport_[3] - 120) - (k * 65)) {
                            TouchSurfaceView.this.mRenderer.viewMode_ = MeshRender.ViewMode.top;
                            TouchSurfaceView.this.mRenderer.showAll();
                            TouchSurfaceView.this.requestRender();
                        } else if (x > k * 65 && x < k * 128 && y > (TouchSurfaceView.this.mRenderer.viewport_[3] - 120) - (k * 128) && y < (TouchSurfaceView.this.mRenderer.viewport_[3] - 120) - (k * 65)) {
                            TouchSurfaceView.this.mRenderer.viewMode_ = MeshRender.ViewMode.left;
                            TouchSurfaceView.this.mRenderer.showAll();
                            TouchSurfaceView.this.requestRender();
                        } else if (x > k * 129 && x < k * 192 && y > (TouchSurfaceView.this.mRenderer.viewport_[3] - 120) - (k * 64) && y < TouchSurfaceView.this.mRenderer.viewport_[3] - 120) {
                            TouchSurfaceView.this.mRenderer.viewMode_ = MeshRender.ViewMode.iso;
                            TouchSurfaceView.this.mRenderer.showAll();
                            TouchSurfaceView.this.requestRender();
                        }
                    }
                    float[] pickLine = new float[6];
                    TouchSurfaceView.this.mRenderer.pick((int) x, (int) y, pickLine);
                    if (((SubDivFormer) TouchSurfaceView.this.getContext()).pickColorState_) {
                        ((SubDivFormer) TouchSurfaceView.this.getContext()).setDrawColor(TouchSurfaceView.this.modellerPickFaceColor(pickLine[0], pickLine[1], pickLine[2], pickLine[3], pickLine[4], pickLine[5]));
                    } else {
                        TouchSurfaceView.this.modellerPickFace(pickLine[0], pickLine[1], pickLine[2], pickLine[3], pickLine[4], pickLine[5]);
                    }
                    TouchSurfaceView.this.requestRender();
                } else {
                    if (TouchSurfaceView.this.mRenderer.showNavButtons_) {
                        TouchSurfaceView.this.mRenderer.hideNavButtons();
                    } else {
                        TouchSurfaceView.this.mRenderer.showNavButtons();
                    }
                    TouchSurfaceView.this.requestRender();
                }
            }
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
            TouchSurfaceView.this.lastMoveX_ = 0.0f;
            TouchSurfaceView.this.lastMoveY_ = 0.0f;
            int touchCount = e2.getPointerCount();
            if (TouchSurfaceView.this.mRenderer != null && (dx > 1.0f || dx < -1.0f || dy > 1.0f || dy < -1.0f)) {
                if (TouchSurfaceView.this.viewOnly_) {
                    if (TouchSurfaceView.this.mRenderer != null) {
                        if (touchCount == 1) {
                            TouchSurfaceView.this.mRenderer.rotate(dx * 0.5625f, dy * 0.5625f);
                            TouchSurfaceView.this.requestRender();
                        } else {
                            TouchSurfaceView.this.mRenderer.move((-dx) * 0.5f, 0.5f * dy);
                            TouchSurfaceView.this.requestRender();
                        }
                    }
                } else if (!((SubDivFormer) TouchSurfaceView.this.getContext()).toolState_) {
                    if (TouchSurfaceView.this.mRenderer != null) {
                        if (touchCount == 1) {
                            TouchSurfaceView.this.mRenderer.rotate(dx * 0.5625f, dy * 0.5625f);
                            TouchSurfaceView.this.requestRender();
                        } else {
                            TouchSurfaceView.this.mRenderer.move((-dx) * 0.5f, 0.5f * dy);
                            TouchSurfaceView.this.requestRender();
                        }
                    }
                } else {
                    ((SubDivFormer) TouchSurfaceView.this.getContext()).screenMove(-dx, dy);
                    TouchSurfaceView.this.requestRender();
                }
            }
            return true;
        }
    }
}
