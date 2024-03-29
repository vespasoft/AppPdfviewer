package es.enaire.apptest

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.unisys.android.pdfviewer.platform.activities.PdfViewerActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import java.nio.charset.Charset




class MainActivity : AppCompatActivity() {

    val pdfVisorRequestCode = 1100
    val fileName = "sample.pdf"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fileHandler = FileHandler(applicationContext)

        btnDocument.setOnClickListener {
            val inputStream = baseContext.assets.open(fileName)
            fileHandler.createFile(1, "872019", inputStream )
            val path = fileHandler.getFilePath(1, "872019")
            launch(applicationContext, path)
        }

    }

    private fun launch(context: Context, filePath : String) {
        val intent = Intent(context, PdfViewerActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        intent.putExtra("filepath", filePath)
        context.startActivity(intent)
    }

    private fun getInputStream(context: Context, fileName: String): String {
        var stringObtained: String? = null

        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        stringObtained = String(buffer,  Charset.forName("UTF-8"))

        Log.e("data", stringObtained)
        return stringObtained

    }

}
