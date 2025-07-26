package com.submission.appriori.ui.report

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.submission.appriori.R
import com.submission.appriori.adapter.Itemset11Adapter
import com.submission.appriori.adapter.Itemset21Adapter
import com.submission.appriori.adapter.Itemset31Adapter
import com.submission.appriori.adapter.ItemsetAssoc1Adapter
import com.submission.appriori.adapter.ResultFinalAdapter
import com.submission.appriori.data.model.AssociationRule
import com.submission.appriori.data.model.Itemset1
import com.submission.appriori.data.model.Itemset2
import com.submission.appriori.data.model.Itemset3
import com.submission.appriori.data.model.ResultModel
import com.submission.appriori.databinding.ActivityResultDetailBinding
import com.submission.appriori.firebase.FirebaseDataManager
import com.submission.appriori.utils.DateConvert
import com.submission.appriori.utils.TextUtils
import java.io.ByteArrayOutputStream
import java.io.File
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
            findViewById<View>(R.id.rl2)

        scrollView?.setOnScrollChangeListener { v: View, scrollX: Int, scrollY: Int, _, oldScrollY: Int ->
            if (scrollY != null) {
                if (scrollY > 50 && oldScrollY < scrollY) {

                    header2?.animate()?.translationY(-header?.height?.toFloat()!!)?.setDuration(100)?.start()
                    header2?.setBackgroundResource(R.drawable.card_flat)
                } else if (scrollY < 50 && oldScrollY > scrollY) {

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
        val i1 = baseRef.collection("itemset1").orderBy("support", Query.Direction.DESCENDING)
        val i2 = baseRef.collection("itemset2").orderBy("support", Query.Direction.DESCENDING)
        val i3 = baseRef.collection("itemset3").orderBy("support", Query.Direction.DESCENDING)
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
        val pageWidth = 595f
        val pageHeight = 842f
        val padding = 8f
        val leftMargin = 40f
        val topMarginFirstPage = 50f
        val topMarginNextPage = 30f
        val margin = 40f
        val rowHeightMin = 25f

        val paint = Paint().apply { textSize = 12f }
        val boldPaint = Paint().apply {
            isFakeBoldText = true
            textSize = 16f
        }
        val boldSmallPaint = Paint().apply {
            isFakeBoldText = true
            textSize = 12f
        }
        val borderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        val logoTargetSize = 80f
        val aspectRatio = logoBitmap.width.toFloat() / logoBitmap.height
        val logoWidth = logoTargetSize
        val logoHeight = logoTargetSize / aspectRatio

        val headers2 = listOf("No", "Hasil Analisis", "Confidence")
        val colWidths2 = listOf(40f, 360f, 120f)
        val tableWidth = colWidths2.sum()
        val startX = (pageWidth - tableWidth) / 2f

        val pdfDocument = PdfDocument()
        var pageIndex = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), pageIndex).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        var yPos = margin

        fun newPage() {
            pdfDocument.finishPage(page)
            pageIndex++
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), pageIndex).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas
            yPos = margin
        }

        fun drawWrappedText(text: String, x: Float, y: Float, maxWidth: Float, paint: Paint): Pair<List<String>, Float> {
            val lines = TextUtils.splitTextToLines(text, paint, maxWidth)
            val lineHeight = paint.textSize + 6f
            var curY = y
            for (line in lines) {
                canvas.drawText(line, x, curY, paint)
                curY += lineHeight
            }
            return Pair(lines, curY - y)
        }

        // Draw logo
        val destRect = RectF(margin, yPos, margin + logoWidth, yPos + logoHeight)
        canvas.drawBitmap(logoBitmap, null, destRect, null)

        // Draw header text
        val kop1 = "APOTEK MUJARAB"
        val kop2Lines = listOf(
            "Jl. Raya Warungasem, Kel. Warungasem, Kec. Warungasem,",
            "Kab. Batang, Jawa Tengah, 51252"
        )
        val kop1Height = 20f
        val kop2LineHeight = 15f
        val totalTextHeight = kop1Height + (kop2Lines.size * kop2LineHeight)
        val centerOfLogo = yPos + (logoHeight / 2f)
        val textStartY = centerOfLogo - (totalTextHeight / 2f) + kop1Height
        canvas.drawText(kop1, (pageWidth - boldPaint.measureText(kop1)) / 2f, textStartY, boldPaint)
        for ((i, line) in kop2Lines.withIndex()) {
            val lineY = textStartY + kop2LineHeight + (i * kop2LineHeight)
            canvas.drawText(line, (pageWidth - paint.measureText(line)) / 2f, lineY, paint)
        }

        yPos = maxOf(destRect.bottom, textStartY + totalTextHeight) + 10f
        canvas.drawLine(margin, yPos, pageWidth - margin, yPos, borderPaint)
        yPos += 25f

        val title1 = "Laporan Data Hasil"
        canvas.drawText(title1, (pageWidth - boldPaint.measureText(title1)) / 2, yPos, boldPaint)
        yPos += 30f

        val headers1 = listOf("Tanggal Mulai", "Tanggal Akhir", "Nilai Support", "Nilai Confidence")
        val colWidths1 = listOf(130f, 130f, 130f, 130f)
        var x = startX
        for (i in headers1.indices) {
            canvas.drawRect(x, yPos, x + colWidths1[i], yPos + rowHeightMin, borderPaint)
            val center = x + (colWidths1[i] - boldSmallPaint.measureText(headers1[i])) / 2
            canvas.drawText(headers1[i], center, yPos + 17f, boldSmallPaint)
            x += colWidths1[i]
        }
        yPos += rowHeightMin

        val baseRef = db.collection("users").document("admin").collection("result").document(resultId)
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
                    canvas.drawRect(x, yPos, x + colWidths1[i], yPos + rowHeightMin, borderPaint)
                    val center = x + (colWidths1[i] - paint.measureText(row[i])) / 2
                    canvas.drawText(row[i], center, yPos + 17f, paint)
                    x += colWidths1[i]
                }
                yPos += rowHeightMin
            }
            // ===== Judul =====
            yPos += 40f
            val title = "DAFTAR HASIL ANALISIS"
            val titleWidth = boldPaint.measureText(title)
            canvas.drawText(title, (pageWidth - titleWidth) / 2, yPos, boldPaint)
            yPos += 30f
            var currentY = yPos  // posisi awal setelah judul
            var isFirstPage = true  // hanya halaman pertama yang gambar header
            // ===== Tabel Header Function =====
            fun drawTableHeader() {
                if (!isFirstPage) return  // hanya gambar di halaman pertama
                var x = 40f
                val headerHeight = 25f
                for (i in headers2.indices) {
                    canvas.drawRect(x, currentY, x + colWidths2[i], currentY + headerHeight, borderPaint)
                    val text = headers2[i]
                    val centerX = x + (colWidths2[i] - boldSmallPaint.measureText(text)) / 2
                    canvas.drawText(text, centerX, currentY + 17f, boldSmallPaint)
                    x += colWidths2[i]
                }
                currentY += headerHeight
                isFirstPage = false  // hanya untuk halaman pertama
            }

            drawTableHeader()
            yPos = currentY
            baseRef.collection("assoc_rules").get().addOnSuccessListener { hasil ->

                val data2 = hasil.mapIndexed { index, doc ->
                    val antecedents = doc.get("antecedents") as? List<*> ?: emptyList<Any>()
                    val consequents = doc.get("consequents") as? List<*> ?: emptyList<Any>()
                    val confidence = doc.getDouble("confidence") ?: 0.0
                    val hasilAnalisis = "Jika membeli ${antecedents.joinToString()} maka akan membeli ${consequents.joinToString()}"
                    listOf((index + 1).toString(), hasilAnalisis, "%.2f".format(confidence))
                }

                for (row in data2) {
                    val rowHeight = row.mapIndexed { i, text ->
                        calculateTextHeight(text, paint, colWidths2[i] - 2 * padding)
                    }.maxOrNull()?.coerceAtLeast(25f) ?: 25f

                    if (yPos + rowHeight > pageHeight - 120f) {
                        // Akhiri halaman sebelumnya
                        pdfDocument.finishPage(page)
                        pageIndex++
                        pageInfo = PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), pageIndex).create()
                        page = pdfDocument.startPage(pageInfo)
                        canvas = page.canvas
                        yPos = topMarginNextPage
                    }

                    var x = leftMargin
                    for (i in row.indices) {
                        canvas.drawRect(x, yPos, x + colWidths2[i], yPos + rowHeight, borderPaint)
                        val isCenter = i == 0 || i == 1 || i == 2
                        //drawWrappedText(canvas, rowData[i], x + padding, yPos + paint.textSize, colWidths[i] - 2 * padding, paint, isCenter)
                        drawWrappedText(
                            canvas,
                            row[i],
                            x + padding,
                            yPos,
                            colWidths2[i] - 2 * padding,
                            paint,
                            isCenter,
                            rowHeight
                        )

                        x += colWidths2[i]
                    }
                    yPos += rowHeight
                }

                val signatureTopMargin = 50f
                if (yPos + 100f > pageHeight - 60f) {
                    newPage()
                }

                yPos += signatureTopMargin
                val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
                val tanggalSekarang = sdf.format(Date())
                val text1 = "Batang, $tanggalSekarang"
                val text2 = "Disetujui oleh,"
                val text3 = "(..............................................)"
                val signatureX = pageWidth - 160f
                val signatureY = pageHeight - 120f

                val drawCenter = { text: String, yOffset: Float ->
                    val textWidth = paint.measureText(text)
                    val x = signatureX + (120f - textWidth) / 2f
                    canvas.drawText(text, x, signatureY + yOffset, paint)
                }
                drawCenter(text1, 0f)
                drawCenter(text2, 20f)
                drawCenter(text3, 70f)

                pdfDocument.finishPage(page)
                val outputStream = ByteArrayOutputStream()
                pdfDocument.writeTo(outputStream)
                pdfDocument.close()
                callback(outputStream.toByteArray())
            }
        }
    }
    fun drawWrappedText(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        width: Float,
        paint: Paint,
        isCenter: Boolean = false,
        rowHeight: Float = 25f
    ) {
        val lines = TextUtils.splitTextToLines(text, paint, width)
        val lineHeight = paint.textSize + 2f
        val totalTextHeight = lines.size * lineHeight

        var startY = y + paint.textSize
        if (isCenter) {
            startY = y + (rowHeight - totalTextHeight) / 2f + paint.textSize
        }

        for (line in lines) {
            val textX = if (isCenter) {
                x + (width - paint.measureText(line)) / 2f
            } else {
                x
            }
            canvas.drawText(line, textX, startY, paint)
            startY += lineHeight
        }
    }
    fun calculateTextHeight(text: String, paint: Paint, maxWidth: Float): Float {
        val words = text.split(" ")
        var line = ""
        var lines = 0
        for (word in words) {
            val testLine = if (line.isEmpty()) word else "$line $word"
            if (paint.measureText(testLine) > maxWidth) {
                lines++
                line = word
            } else {
                line = testLine
            }
        }
        if (line.isNotEmpty()) lines++
        return lines * (paint.textSize + 4f)
    }


    @Suppress("DEPRECATION")
    fun drawWrappedText(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        width: Float,
        paint: TextPaint
    ): Float {
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder
                .obtain(text, 0, text.length, paint, width.toInt())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)
                .build()
        } else {
            StaticLayout(text, paint, width.toInt(), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        }

        canvas.save()
        canvas.translate(x, y)
        staticLayout.draw(canvas)
        canvas.restore()

        return staticLayout.height.toFloat()
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