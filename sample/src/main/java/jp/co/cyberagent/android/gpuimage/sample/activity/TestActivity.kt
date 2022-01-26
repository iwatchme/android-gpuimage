package jp.co.cyberagent.android.gpuimage.sample.activity

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.Utils
import jp.co.cyberagent.android.gpuimage.sample.R
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils
import kotlinx.android.synthetic.main.activity_test.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.content.pm.ConfigurationInfo

import android.app.ActivityManager
import android.provider.SyncStateContract


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
            setRenderer(PointerRender(this@TestActivity))
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

class PointerRender(val context:Context) : GLSurfaceView.Renderer {
    val point by lazy {
        Point(context)
    }

    val line by lazy {
        Line(context)
    }

    val triangle by lazy {
        Triangle(context)
    }



    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
//        point.bindData()
 //       line.bindData()
         triangle.bindData()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //确定视口大小
        GLES20.glViewport(0,0, width, height)

    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
//        point.draw()
    //    line.draw()
        triangle.draw()
    }

}

class Point(context: Context) {

    private fun buildProgram() : Int{

        val vertexSourceCode = ResourceUtils.readRaw2String(R.raw.simple).trim()

        val fragSourceCode = ResourceUtils.readRaw2String(R.raw.simple_frag).trim()

        val programId = OpenGlUtils.loadProgram(vertexSourceCode, fragSourceCode).apply {
            Log.e("Frank", "${this}")
        }

        return programId
    }

    var pointvertex = floatArrayOf(
            0f, 0f)

    private var mProgramId: Int  = -1

    private var position :Int = 0

    private var color:Int = 0

    val POSITION_COPOMENT_COUNT = 2

    private val vertexArray by lazy {
       ByteBuffer.allocateDirect(
               pointvertex.size * 4
       ).order(ByteOrder.nativeOrder())
               .asFloatBuffer()
               .put(pointvertex).apply {
                   this.position(0)
               }
    }

    init {
         mProgramId = buildProgram()
         GLES20.glUseProgram(mProgramId)
    }


    fun bindData() {
        position = GLES20.glGetAttribLocation(mProgramId, "a_position")
        color = GLES20.glGetUniformLocation(mProgramId, "u_color")


        GLES20.glVertexAttribPointer(
                position, // 属性位置
                POSITION_COPOMENT_COUNT, // 每个属性的数据计数，比如一个点用一个x和一个y表示
                GLES20.GL_FLOAT, //数据类型
                false,
                0,
                vertexArray

        )
        GLES20.glEnableVertexAttribArray(position)
    }

    fun draw() {
        GLES20.glUniform4f(color, 0f, 0f, 1f, 1f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, pointvertex.size / 2)
    }

}

class Line(context: Context) {

    private fun buildProgram() : Int{

        val vertexSourceCode = ResourceUtils.readRaw2String(R.raw.simple).trim()

        val fragSourceCode = ResourceUtils.readRaw2String(R.raw.simple_frag).trim()

        val programId = OpenGlUtils.loadProgram(vertexSourceCode, fragSourceCode).apply {
            Log.e("Frank", "${this}")
        }

        return programId
    }

    var pointvertex = floatArrayOf(
            -1f, 1f, 1f, -1f )

    private var mProgramId: Int  = -1

    private var position :Int = 0

    private var color:Int = 0

    val POSITION_COPOMENT_COUNT = 2

    private val vertexArray by lazy {
        ByteBuffer.allocateDirect(
                pointvertex.size * 4
        ).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(pointvertex).apply {
                    this.position(0)
                }
    }

    init {
        mProgramId = buildProgram()
        GLES20.glUseProgram(mProgramId)
    }


    fun bindData() {
        position = GLES20.glGetAttribLocation(mProgramId, "a_position")
        color = GLES20.glGetUniformLocation(mProgramId, "u_color")


        GLES20.glVertexAttribPointer(
                position, // 属性位置
                POSITION_COPOMENT_COUNT, // 每个属性的数据计数，比如一个点用一个x和一个y表示
                GLES20.GL_FLOAT, //数据类型
                false,
                0,
                vertexArray

        )
        GLES20.glEnableVertexAttribArray(position)
    }

    fun draw() {
        GLES20.glUniform4f(color, 0f, 0f, 1f, 1f)
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, pointvertex.size / 2)
    }

}

class Triangle(context: Context) {

    private fun buildProgram() : Int{

        val vertexSourceCode = ResourceUtils.readRaw2String(R.raw.simple).trim()

        val fragSourceCode = ResourceUtils.readRaw2String(R.raw.simple_frag).trim()

        val programId = OpenGlUtils.loadProgram(vertexSourceCode, fragSourceCode).apply {
            Log.e("Frank", "${this}")
        }

        return programId
    }

    var pointvertex = floatArrayOf(
            -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f )

    private var mProgramId: Int  = -1

    private var position :Int = 0

    private var color:Int = 0

    val POSITION_COPOMENT_COUNT = 2

    private val vertexArray by lazy {
        ByteBuffer.allocateDirect(
                pointvertex.size * 4
        ).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(pointvertex).apply {
                    this.position(0)
                }
    }

    init {
        mProgramId = buildProgram()
        GLES20.glUseProgram(mProgramId)
    }


    fun bindData() {
        position = GLES20.glGetAttribLocation(mProgramId, "a_position")
        color = GLES20.glGetUniformLocation(mProgramId, "u_color")


        GLES20.glVertexAttribPointer(
                position, // 属性位置
                POSITION_COPOMENT_COUNT, // 每个属性的数据计数，比如一个点用一个x和一个y表示
                GLES20.GL_FLOAT, //数据类型
                false,
                0,
                vertexArray

        )
        GLES20.glEnableVertexAttribArray(position)
    }

    fun draw() {
        GLES20.glUniform4f(color, 0f, 0f, 1f, 1f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, pointvertex.size / 2)
    }

}



class Circle(context: Context) {

    private fun buildProgram() : Int{

        val vertexSourceCode = ResourceUtils.readRaw2String(R.raw.simple).trim()

        val fragSourceCode = ResourceUtils.readRaw2String(R.raw.simple_frag).trim()

        val programId = OpenGlUtils.loadProgram(vertexSourceCode, fragSourceCode).apply {
            Log.e("Frank", "${this}")
        }

        return programId
    }

    val VERTEX_DATA_NUM = 360

    var pointvertex = FloatArray(VERTEX_DATA_NUM * 2 + 4)

    private var mProgramId: Int  = -1

    private var position :Int = 0

    private var color:Int = 0

    val POSITION_COPOMENT_COUNT = 2

    val radian = 2 * Math.PI / VERTEX_DATA_NUM

    val radius = 0.8f





    private val vertexArray by lazy {
        ByteBuffer.allocateDirect(
                pointvertex.size * 4
        ).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(pointvertex).apply {
                    this.position(0)
                }
    }

    init {
        mProgramId = buildProgram()
        GLES20.glUseProgram(mProgramId)
    }


    fun bindData() {
        position = GLES20.glGetAttribLocation(mProgramId, "a_position")
        color = GLES20.glGetUniformLocation(mProgramId, "u_color")


        GLES20.glVertexAttribPointer(
                position, // 属性位置
                POSITION_COPOMENT_COUNT, // 每个属性的数据计数，比如一个点用一个x和一个y表示
                GLES20.GL_FLOAT, //数据类型
                false,
                0,
                vertexArray

        )
        GLES20.glEnableVertexAttribArray(position)
    }

    fun draw() {
        GLES20.glUniform4f(color, 0f, 0f, 1f, 1f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, pointvertex.size / 2)
    }

}

