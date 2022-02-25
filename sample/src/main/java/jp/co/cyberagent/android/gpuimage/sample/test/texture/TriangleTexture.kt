package jp.co.cyberagent.android.gpuimage.sample.test.texture

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS
import android.opengl.Matrix
import android.util.Log
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ResourceUtils
import jp.co.cyberagent.android.gpuimage.sample.R
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils.NO_TEXTURE
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

class TriangleTexture(context: Context) {

    private fun buildProgram() : Int{

        val vertexSourceCode = ResourceUtils.readRaw2String(R.raw.texture).trim()

        val fragSourceCode = ResourceUtils.readRaw2String(R.raw.texture_frag).trim()

        val programId = OpenGlUtils.loadProgram(vertexSourceCode, fragSourceCode).apply {
            Log.e("Frank", "${this}")
        }

        return programId
    }

    private var pointvertex = floatArrayOf(
            -1f, 1f, -1f, -1f, 1f, -1f )

    private var textureArrayData = floatArrayOf(
            0.5f, 0f,
            0f, 1.0f,
            1.0f, 1.0f
    )

    private var mProgramId: Int  = -1

    private var aPosition :Int = 0


    val POSITION_COMPONENT_COUNT = 2


    private var uProjectionMatrixPosition : Int = 0

    private var uViewMatrixPosition : Int = 0

    private var uModelMatrixPosition :Int = 0

    private var aTextureCoordinateAttr: Int = 0

    private var uTextureUnitAttr: Int = 0


    private var mProjectionMatrix = FloatArray(16)

    private var mViewMatrix = FloatArray(16)

    private var mModelMatrix = FloatArray(16)

    private val U_VIEW_MATRIX = "u_ViewMatrix"

    private val U_MODEL_MATRIX = "u_ModelMatrix"

    private val U_PROJECTION_MATRIX = "u_ProjectionMatrix"

    private val A_TEXTURE_COORDINATE = "a_TextureCoordinates"

    private val U_TEXTURE_UNIT: String = "u_TextureUnit"

    private val A_POSITION = "a_position"

    private var mTextureId = -1

    lateinit var bitmap:Bitmap


    private val vertexArray by lazy {
        ByteBuffer.allocateDirect(
                pointvertex.size * 4
        ).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(pointvertex).apply {
                    this.position(0)
                }
    }

    private val textureArray by lazy {
        ByteBuffer.allocateDirect(
                textureArrayData.size * 4
        ).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureArrayData).apply {
                    this.position(0)
                }
    }

    init {
        mProgramId = buildProgram()
        GLES20.glUseProgram(mProgramId)
    }


    fun bindData() {
        aPosition = GLES20.glGetAttribLocation(mProgramId, A_POSITION)
        uProjectionMatrixPosition = GLES20.glGetUniformLocation(mProgramId, U_PROJECTION_MATRIX)
        uViewMatrixPosition = GLES20.glGetUniformLocation(mProgramId, U_VIEW_MATRIX)
        uModelMatrixPosition = GLES20.glGetUniformLocation(mProgramId, U_MODEL_MATRIX)
        aTextureCoordinateAttr = GLES20.glGetAttribLocation(mProgramId, A_TEXTURE_COORDINATE)
        uTextureUnitAttr = GLES20.glGetUniformLocation(mProgramId, U_TEXTURE_UNIT)


        bitmap = ImageUtils.getBitmap(R.drawable.ic_blend_mode_darken)

        mTextureId = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE, false)

        GLES20.glUniform1i(uTextureUnitAttr, 0)


        val intBuffer: IntBuffer = IntBuffer.allocate(1)

        GLES20.glGetIntegerv(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, intBuffer)

       Log.e("Frank","max combined texture image units " + intBuffer[0])



        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.setIdentityM(mViewMatrix, 0)
        Matrix.setIdentityM(mProjectionMatrix, 0)

    }

    fun draw() {
        GLES20.glUniformMatrix4fv(uModelMatrixPosition, 1, false, mModelMatrix, 0)
        GLES20.glUniformMatrix4fv(uViewMatrixPosition, 1, false, mViewMatrix, 0)
        GLES20.glUniformMatrix4fv(uProjectionMatrixPosition, 1, false, mProjectionMatrix, 0)

        vertexArray.setVertexAttribPointer(0, aPosition, POSITION_COMPONENT_COUNT, 0 )
        textureArray.setVertexAttribPointer(0, aTextureCoordinateAttr, POSITION_COMPONENT_COUNT, 0)

        Log.e("Frank", "draw: ${mTextureId} ${aPosition} ${aTextureCoordinateAttr}")

        OpenGlUtils.loadTexture(bitmap, mTextureId, false)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)
    }

}


fun FloatBuffer.setVertexAttribPointer(dataOffset:Int,attributeLocation:Int, componentCount: Int, stride:Int) {
    position(dataOffset)
    GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, false, stride, this)
    GLES20.glEnableVertexAttribArray(attributeLocation)
    position(0)
}