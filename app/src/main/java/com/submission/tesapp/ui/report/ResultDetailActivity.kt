package com.submission.tesapp.ui.report

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.tesapp.R
import com.submission.tesapp.adapter.Itemset11Adapter
import com.submission.tesapp.adapter.Itemset21Adapter
import com.submission.tesapp.adapter.Itemset31Adapter
import com.submission.tesapp.adapter.ItemsetAssoc1Adapter
import com.submission.tesapp.adapter.ReportAdapter
import com.submission.tesapp.adapter.ResultFinalAdapter
import com.submission.tesapp.data.model.AssociationRule
import com.submission.tesapp.data.model.Itemset1
import com.submission.tesapp.data.model.Itemset2
import com.submission.tesapp.data.model.Itemset3
import com.submission.tesapp.data.model.ResultModel
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.databinding.ActivityLoginBinding
import com.submission.tesapp.databinding.ActivityResultDetailBinding
import com.submission.tesapp.databinding.FragmentReportBinding
import com.submission.tesapp.firebase.FirebaseDataManager
import com.submission.tesapp.utils.DateConvert
import java.io.ByteArrayOutputStream
import java.io.File
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ResultDetailActivity : AppCompatActivity() {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var pdfBytes: ByteArray
    private lateinit var binding: ActivityResultDetailBinding
    private lateinit var adapter1: Itemset11Adapter
    private lateinit var adapter2: Itemset21Adapter
    private lateinit var adapter3: Itemset31Adapter
    private lateinit var adapter4: ItemsetAssoc1Adapter
    private lateinit var adapter5: ResultFinalAdapter

    private lateinit var firebaseDataManager: FirebaseDataManager
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.ivMenu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val scrollView = findViewById<ScrollView>(R.id.sv)
        val header = findViewById<View>(R.id.rl1)
        val header2 =
            findViewById<View>(R.id.rl2) // ini RelativeLayout kedua yang membungkus ScrollView

        scrollView?.setOnScrollChangeListener { v: View, scrollX: Int, scrollY: Int, _, oldScrollY: Int ->
            if (scrollY != null) {
                if (scrollY > 50 && oldScrollY < scrollY) {
                    // Sembunyikan header (animasikan ke atas)
                    header2?.animate()?.translationY(-header?.height?.toFloat()!!)?.setDuration(100)?.start()
                    header2?.setBackgroundResource(R.drawable.card_flat)
                } else if (scrollY < 50 && oldScrollY > scrollY) {
                    // Munculkan kembali header
                    header2?.animate()?.translationY(0f)?.setDuration(100)?.start()
                    header2?.setBackgroundResource(R.drawable.card)
                }
            }
        }

        binding.sv.visibility = View.INVISIBLE
        binding.fabMenu.visibility = View.INVISIBLE
        binding.progressBarProcess.visibility = View.VISIBLE
        firebaseDataManager = FirebaseDataManager()
        adapter1 = Itemset11Adapter()
        adapter2 = Itemset21Adapter()
        adapter3 = Itemset31Adapter()
        adapter4 = ItemsetAssoc1Adapter()
        adapter5 = ResultFinalAdapter()
        val resultsModel: ResultModel? = intent.getParcelableExtra(RESULTID)
        if (resultsModel != null) {
            loadItemset(resultsModel.resultId)
        }

        binding.fabMenu.setOnClickListener{
            if (resultsModel != null) {
                previewAndSavePdf(resultsModel.resultId)
            }
        }
        with(binding) {
            rvItemset1.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter1
            }
            rvItemset2.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter2
            }
            rvItemset3.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter3
            }
            rvAssoc.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter4
            }
            rvFinal.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter5
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun loadItemset(resultId: String) {
         val baseRef = db.collection("users")
            .document("admin")
            .collection("result")
            .document(resultId)
        val i1 = baseRef.collection("itemset1")
        val i2 = baseRef.collection("itemset2")
        val i3 = baseRef.collection("itemset3")
        val i4 = baseRef.collection("assoc_rules")
        val txList: ArrayList<Itemset1> = arrayListOf()

        baseRef.get().addOnSuccessListener { snapshot ->
            val data = snapshot.data
            with(binding) {
                minSupText.text = (data?.get("min_support") as? Double ?: 0.0).toString()
                minConfText.text = (data?.get("conf") as? Double ?: 0.0).toString()
                val from = data?.get("from") as Timestamp
                val end = data?.get("end") as Timestamp
                tgl.text = DateConvert.convertDate(from.toDate()) + " - " + DateConvert.convertDate(end.toDate())
            }
        }

        i1.get().addOnSuccessListener { snapshot1 ->
            val list = snapshot1.mapNotNull { it.toObject(Itemset1::class.java) }
            val txList: MutableList<Itemset1> = arrayListOf()
            for(doc in snapshot1) {
               val item = doc.getString("item")
               val ket = doc.getString("keterangan")
               val sup = doc.getDouble("support")
               val qty = doc.getDouble("totalQuantity")
                val rsl = Itemset1(
                   item,
                   ket,
                   sup,
                   qty
               )
                txList.add(rsl)
                Log.e(TAG, txList.toString())
            }
            adapter1.submitList(txList)
            Log.e(TAG, adapter1.toString())
            i2.get().addOnSuccessListener { snapshot2 ->
                val list2 = snapshot2.mapNotNull { it.toObject(Itemset2::class.java) }
                adapter2.submitList(list2)
                i3.get().addOnSuccessListener { snapshot3 ->
                    val list3  = snapshot3.mapNotNull { it.toObject(Itemset3::class.java) }
                    adapter3.submitList(list3)
                    i4.get().addOnSuccessListener { snapshot4 ->
                        val list4 = snapshot4.mapNotNull { it.toObject(AssociationRule::class.java) }
                        adapter4.submitList(list4)
                        adapter5.submitList(list4)
                        binding.sv.visibility = View.VISIBLE
                        binding.fabMenu.visibility = View.VISIBLE
                        binding.progressBarProcess.visibility = View.GONE
                    }
                }
            }
        }
    }

    private val createDocumentLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        uri?.let {
            contentResolver.openOutputStream(uri)?.use { out ->
                out.write(pdfBytes)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun cetakTransaksi(logoBitmap: Bitmap, resultId: String, callback: (ByteArray) -> Unit) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val pageWidth = pageInfo.pageWidth.toFloat()
        var yPos = 40f

        val paint = Paint()
        val boldPaint = Paint().apply {
            textSize = 16f
            isFakeBoldText = true
        }

        val boldSmallPaint = Paint().apply {
            isFakeBoldText = true
            textSize = 12f
        }
        val borderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        val logoTargetSize = 80f  // Ukuran ideal agar tidak terlalu besar dan tidak pecah
        val aspectRatio = logoBitmap.width.toFloat() / logoBitmap.height
        val logoWidth = logoTargetSize
        val logoHeight = logoTargetSize / aspectRatio

        val logoLeft = 40f
        val logoTop = yPos

        val destRect = RectF(logoLeft, logoTop, logoLeft + logoWidth, logoTop + logoHeight)
        canvas.drawBitmap(logoBitmap, null, destRect, null)

        val kop1 = "APOTEK MUJARAB"
        val kop2Lines = listOf(
            "Jl. Raya Warungasem, Kel. Warungasem, Kec. Warungasem,",
            "Kab. Batang, Jawa Tengah, 51252"
        )
        val kop1Height = 20f
        val kop2LineHeight = 15f
        val totalTextHeight = kop1Height + (kop2Lines.size * kop2LineHeight)
        val centerOfLogo = logoTop + (logoHeight / 2f)
        val textStartY = centerOfLogo - (totalTextHeight / 2f) + kop1Height

        val kop1Width = boldPaint.measureText(kop1)
        canvas.drawText(kop1, (pageWidth - kop1Width) / 2f, textStartY, boldPaint)
        paint.textSize = 12f
        for ((i, line) in kop2Lines.withIndex()) {
            val lineWidth = paint.measureText(line)
            val lineX = (pageWidth - lineWidth) / 2f
            val lineY = textStartY + kop2LineHeight + (i * kop2LineHeight)
            canvas.drawText(line, lineX, lineY, paint)
        }
        yPos = maxOf(logoTop + logoHeight, textStartY + totalTextHeight) + 10f
        canvas.drawLine(40f, yPos, pageWidth - 40f, yPos, borderPaint)
        yPos += 25f

        // ----------------------
        // Judul
        // ----------------------
        val title1 = "Laporan Data Hasil"
        canvas.drawText(title1, (pageWidth - boldPaint.measureText(title1)) / 2, yPos, boldPaint)
        yPos += 30f
        val headers1 = listOf("Start Tanggal", "End Tanggal", "Nilai Support", "Nilai Confidence")
        val colWidths1 = listOf(140f, 140f, 130f, 150f)
        val tableWidth = colWidths1.sum()
        val startX = (pageWidth - tableWidth) / 2f
        val rowHeight = 25f


        var x = startX
        for (i in headers1.indices) {
            canvas.drawRect(x, yPos, x + colWidths1[i], yPos + rowHeight, borderPaint)
            val center = x + (colWidths1[i] - boldSmallPaint.measureText(headers1[i])) / 2
            canvas.drawText(headers1[i], center, yPos + 17f, boldSmallPaint)
            x += colWidths1[i]
        }
        yPos += rowHeight

        val baseRef = db.collection("users")
            .document("admin")
            .collection("result")
            .document(resultId)
        baseRef.get().addOnSuccessListener { snapshot ->
            val data = snapshot.data
            val from = data?.get("from") as Timestamp
            val end = data?.get("end") as Timestamp
            val data1 = listOf(
                listOf(
                    DateConvert.convertDate(from.toDate()),
                    DateConvert.convertDate(end.toDate()),
                    binding.minSupText.text.toString(),
                    binding.minConfText.text.toString(),
                )
            )

            for (row in data1) {
                x = startX
                for (i in row.indices) {
                    canvas.drawRect(x, yPos, x + colWidths1[i], yPos + rowHeight, borderPaint)
                    val center = x + (colWidths1[i] - paint.measureText(row[i])) / 2
                    canvas.drawText(row[i], center, yPos + 17f, paint)
                    x += colWidths1[i]
                }
                yPos += rowHeight
            }

            // Lanjut ambil collection assoc_rules
            baseRef.collection("assoc_rules").get().addOnSuccessListener { hasil ->

                // Tulis judul tabel kedua
                yPos += 40f
                val title2 = "Laporan Data Hasil Analisa"
                canvas.drawText(
                    title2,
                    (pageWidth - boldPaint.measureText(title2)) / 2,
                    yPos,
                    boldPaint
                )
                yPos += 30f

                val headers2 = listOf("No", "Hasil Analisis", "Confidence")
                val colWidths2 = listOf(40f, 360f, 120f)

                x = startX
                for (i in headers2.indices) {
                    canvas.drawRect(x, yPos, x + colWidths2[i], yPos + rowHeight, borderPaint)
                    val center = x + (colWidths2[i] - paint.measureText(headers2[i])) / 2
                    canvas.drawText(headers2[i], center, yPos + 17f, paint)
                    x += colWidths2[i]
                }
                yPos += rowHeight

                val data2 = hasil.mapIndexed { index, doc ->
                    val antecedents = doc.get("antecedents") as? List<*> ?: emptyList<Any>()
                    val consequents = doc.get("consequents") as? List<*> ?: emptyList<Any>()
                    val confidence = doc.getDouble("confidence") ?: 0.0

                    val hasilAnalisis =
                        "Jika membeli ${antecedents.joinToString()} maka akan membeli ${consequents.joinToString()}"

                    listOf(
                        (index + 1).toString(),
                        hasilAnalisis,
                        "%.2f".format(confidence)
                    )
                }

                for (row in data2) {
                    x = startX
                    for (i in row.indices) {
                        canvas.drawRect(x, yPos, x + colWidths2[i], yPos + rowHeight, borderPaint)
                        val center = x + (colWidths2[i] - paint.measureText(row[i])) / 2
                        canvas.drawText(row[i], center, yPos + 17f, paint)
                        x += colWidths2[i]
                    }
                    yPos += rowHeight
                }

                // Tambahkan tanda tangan
                val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
                val tanggalSekarang = sdf.format(Date())

                val text1 = "Batang, $tanggalSekarang"
                val text2 = "Disetujui oleh,"
                val text3 = "(..............................................)"
                val marginRight = 40f
                val signatureX = pageWidth - 160f  // Posisi horizontal blok tanda tangan
                val signatureY = pageInfo.pageHeight - 120f // Jarak dari bawah halaman

                val drawCenter = { text: String, yOffset: Float ->
                    val textWidth = paint.measureText(text)
                    val x = signatureX + (120f - textWidth) / 2f
                    canvas.drawText(text, x, signatureY + yOffset, paint)
                }

                drawCenter(text1, 0f)
                drawCenter(text2, 20f)
                drawCenter(text3, 70f)
                // Selesai menggambar, simpan halaman & kirim kembali ke callback
                pdfDocument.finishPage(page)
                val outputStream = ByteArrayOutputStream()
                pdfDocument.writeTo(outputStream)
                pdfDocument.close()
                callback(outputStream.toByteArray())
            }
        }
        }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun previewAndSavePdf(resultId: String) {
        val assetManager = applicationContext?.assets
        val inputStream = assetManager?.open("logo_apt_mujarab.png")
        val logoBitmap = BitmapFactory.decodeStream(inputStream)
        cetakTransaksi(logoBitmap, resultId) { bytes ->
            pdfBytes = bytes

            val tempFile = File(cacheDir, "preview.pdf")
            tempFile.writeBytes(bytes)

            val uri = FileProvider.getUriForFile(
                applicationContext,
                "${packageName}.provider",
                tempFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            startActivity(Intent.createChooser(intent, "Preview PDF"))


            createDocumentLauncher.launch("LaporanResult.pdf")
        }
    }

    companion object {
        const val RESULTID = "resultId"
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}