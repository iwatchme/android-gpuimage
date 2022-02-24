package jp.co.cyberagent.android.gpuimage.sample.test

import android.content.Context
import android.opengl.GLES20
import android.util.Log
import com.blankj.utilcode.util.ResourceUtils
import jp.co.cyberagent.android.gpuimage.sample.R
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder

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