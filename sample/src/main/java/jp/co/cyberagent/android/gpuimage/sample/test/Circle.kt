package jp.co.cyberagent.android.gpuimage.sample.test

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import com.blankj.utilcode.util.ResourceUtils
import jp.co.cyberagent.android.gpuimage.sample.R
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.cos
import kotlin.math.sin

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

    private var uProjectionMatrixPosition : Int = 0

    private var uViewMatrixPostition : Int = 0

    private var uModelMatrixPosition :Int = 0


    private var mProjectionMatrix = FloatArray(16)

    private var mViewMatrix = FloatArray(16)

    private var mModelMatrix = FloatArray(16)

    private val U_VIEW_MATRIX = "u_ViewMatrix"

    private val U_MODEL_MATRIX = "u_ModelMatrix"

    private val U_PROJECTION_MATRIX = "u_ProjectionMatrix"





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
        initVertexData()
    }

    private fun initVertexData() {
        pointvertex[0] = 0f
        pointvertex[0] = 0f

        (0..VERTEX_DATA_NUM).forEach {
            pointvertex[it * 2 + 2] = (radius * cos(radian * it)).toFloat()
            pointvertex[it * 2 + 2 + 1] = (radius * sin(radian * it)).toFloat()
        }


        pointvertex[ VERTEX_DATA_NUM * 2 + 2 ] = (radius * cos(radian)).toFloat()
        pointvertex[ VERTEX_DATA_NUM * 2 + 2 + 1] = (radius * sin(radian)).toFloat()

    }


    fun bindData() {
        position = GLES20.glGetAttribLocation(mProgramId, "a_position")
        color = GLES20.glGetUniformLocation(mProgramId, "u_color")
        uProjectionMatrixPosition = GLES20.glGetUniformLocation(mProgramId, U_PROJECTION_MATRIX)
        uViewMatrixPostition = GLES20.glGetUniformLocation(mProgramId, U_VIEW_MATRIX)
        uModelMatrixPosition = GLES20.glGetUniformLocation(mProgramId, U_MODEL_MATRIX)


        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.setIdentityM(mViewMatrix, 0)
        Matrix.setIdentityM(mProjectionMatrix, 0)



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


    fun onSurfaceChanged(width:Int, height :Int) {
        val aspectRatio = if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()

        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 0f, 10f)
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 0f, 10f)
        }
    }

    fun draw() {
        GLES20.glUniformMatrix4fv(uModelMatrixPosition, 1, false, mModelMatrix, 0)
        GLES20.glUniformMatrix4fv(uViewMatrixPostition, 1, false, mViewMatrix, 0)
        GLES20.glUniformMatrix4fv(uProjectionMatrixPosition, 1, false, mProjectionMatrix, 0)

        GLES20.glUniform4f(color, 0f, 0f, 1f, 1f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, pointvertex.size / 2)
    }

}