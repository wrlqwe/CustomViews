package com.wrl.viewtest;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLESActivity extends ActionBarActivity {

	private GLSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity.
		mGLView = new MyGLSurfaceView(this);
		setContentView(mGLView);

	}

	class MyGLRenderer implements GLSurfaceView.Renderer {

		public void onSurfaceCreated(GL10 unused, EGLConfig config) {
			// Set the background frame color
			GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		}

		public void onDrawFrame(GL10 unused) {
			// Redraw background color
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		}

		public void onSurfaceChanged(GL10 unused, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
		}
	}

	class MyGLSurfaceView extends GLSurfaceView {

		private final MyGLRenderer mRenderer;

		public MyGLSurfaceView(Context context) {
			super(context);

			// Create an OpenGL ES 2.0 context
			setEGLContextClientVersion(2);

			mRenderer = new MyGLRenderer();

			// Set the Renderer for drawing on the GLSurfaceView
			setRenderer(mRenderer);
			// Render the view only when there is a change in the drawing data
			setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
	}

}
