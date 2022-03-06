package jp.co.cyberagent.android.gpuimage.sample.activity

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.co.cyberagent.android.gpuimage.sample.R
import kotlinx.android.synthetic.main.activity_test.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.app.ActivityManager
import android.view.animation.RotateAnimation
import jp.co.cyberagent.android.gpuimage.sample.test.Circle
import jp.co.cyberagent.android.gpuimage.sample.test.Line
import jp.co.cyberagent.android.gpuimage.sample.test.Point
import jp.co.cyberagent.android.gpuimage.sample.test.Triangle
import jp.co.cyberagent.android.gpuimage.sample.test.texture.RotateTriangleTexture
import jp.co.cyberagent.android.gpuimage.sample.test.texture.TriangleTexture


class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        if (!detectOpenGLES20()) {
            return
        }


        surfaceView.apply {
            setEGLContextClientVersion(2)
            setEGLConfigChooser(8, 8, 8,8, 16, 0)
            setRenderer(CustomRender(this@TestActivity))
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }

    }
    private fun detectOpenGLES20(): Boolean {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val info = am.deviceConfigurationInfo
        return info.reqGlEsVersion >= 0x20000
    }



    override fun onPause() {
        super.onPause()
        surfaceView.onPause()
    }


    override fun onResume() {
        super.onResume()
        surfaceView.onResume()
    }





}

class CustomRender(val context:Context) : GLSurfaceView.Renderer {
    val point by lazy {
        Point(context)
    }

    val line by lazy {
        Line(context)
    }

    val triangle by lazy {
        Triangle(context)
    }

    val circle by lazy {
        Circle(context)
    }


    val triangleTexture by  lazy {
       RotateTriangleTexture(context)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
//        point.bindData()
 //       line.bindData()
//         triangle.bindData()
//             circle.bindData()
        triangleTexture.bindData()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //确定视口大小
        GLES20.glViewport(0,0, width, height)
//        circle.onSurfaceChanged(width, height)
        triangleTexture.onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
//        point.draw()
    //    line.draw()
//        triangle.draw()
//        circle.draw()
        triangleTexture.draw()
    }

}



