package com.submission.tesapp.ui.transactions

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.tesapp.R
import com.submission.tesapp.adapter.TransactionAdapter
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.databinding.FragmentTransactionsBinding
import com.submission.tesapp.firebase.FirebaseDataManager
import com.submission.tesapp.utils.DateConvert
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionsFragment : Fragment() {
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TransactionAdapter
    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarProcess.visibility = View.VISIBLE
        binding.cv1.visibility = View.INVISIBLE
        binding.cetak.visibility = View.INVISIBLE
        adapter = TransactionAdapter()
        firebaseDataManager = FirebaseDataManager()
        val layoutManager = LinearLayoutManager(requireParentFragment().requireContext())
        binding.rvTx.layoutManager = layoutManager
        setupFirebase()
        binding.refresh.setOnRefreshListener {
            adapter = TransactionAdapter()
            setupFirebase()
            binding.refresh.isRefreshing = false
        }
        binding.rvTx.setOnClickListener {
            val intent: TransactionModel? = requireActivity().intent.getParcelableExtra(ID)
            intent?.id
            Log.e(TAG, intent?.id.toString())
        }

        binding.deleteAll.setOnClickListener {
            showDeleteConfirmationDialog()
        }
        binding.cetak.setOnClickListener {
            previewAndSavePdf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi")
            .setMessage("Apakah kamu yakin akan menghapus semua transaksi?")
            .setPositiveButton("Hapus") { _, _ ->
                binding.progressBarProcess.visibility = View.VISIBLE
                binding.rvTx.visibility = View.INVISIBLE
                deleteTransaction()

            }.setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteTransaction() {
        firebaseDataManager.deleteAllTransactions { deleteSuccess ->
            if (deleteSuccess) {
                binding.rvTx.visibility = View.VISIBLE
                binding.progressBarProcess.visibility = View.GONE
                setupFirebase()
                showToast("Transaction deleted successfully")
            } else {
                binding.rvTx.visibility = View.VISIBLE
                binding.progressBarProcess.visibility = View.GONE
                showToast("Failed to delete transaction")
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupFirebase() {
        firebaseDataManager.getTransactions { list ->
            binding.rvTx.setHasFixedSize(true)
            binding.rvTx.adapter = adapter
            binding.progressBarProcess.visibility = View.GONE
            binding.cv1.visibility = View.VISIBLE
            if(list.isEmpty()) {
                binding.noData.visibility = View.VISIBLE
                binding.cv1.visibility = View.INVISIBLE
                binding.cetak.visibility = View.INVISIBLE
            } else {
                binding.noData.visibility = View.INVISIBLE
                binding.cetak.visibility = View.VISIBLE
                adapter.submitList(list)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun cetakTransaksi(logoBitmap: Bitmap, callback: (ByteArray) -> Unit) {
        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
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

        val pageWidth = pageInfo.pageWidth.toFloat()
        var yPos = 50f
     val logoTargetSize = 80f  // Ukuran ideal agar tidak terlalu besar dan tidak pecah
        val aspectRatio = logoBitmap.width.toFloat() / logoBitmap.height
        val logoWidth = logoTargetSize
        val logoHeight = logoTargetSize / aspectRatio

        val logoLeft = 40f
        val logoTop = yPos

// Gunakan RectF agar scaling lebih tajam
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

        // Judul
        val title = "DAFTAR TRANSAKSI"
        val titleWidth = boldPaint.measureText(title)
        canvas.drawText(title, (pageWidth - titleWidth) / 2, yPos, boldPaint)
        yPos += 30f

        // Tabel
        val noWidth = 60f
        val dateWidth = 100f
        val produkWidth = pageWidth - 80f - noWidth - dateWidth
        val colWidths = listOf(noWidth, dateWidth, produkWidth)
        val padding = 8f
        val headers = listOf("No", "Tanggal", "Produk")

        // Header Kolom
        var x = 40f
        val headerHeight = 25f
        for (i in headers.indices) {
            canvas.drawRect(x, yPos, x + colWidths[i], yPos + headerHeight, borderPaint)
            val text = headers[i]
            val centerX = x + (colWidths[i] - boldSmallPaint.measureText(text)) / 2
            canvas.drawText(text, centerX, yPos + 17f, boldSmallPaint)
            x += colWidths[i]
        }
        yPos += headerHeight

        // ================= ISI DATA =================
        firebaseDataManager.getTransactions { list ->
            val rows = list.mapIndexed { index, doc ->
                listOf(
                    (index + 1).toString(),
                    DateConvert.convertDate(doc.date),
                    doc.item.toString()
                )
            }

            val maxRowHeight = rows.maxOf { row ->
                row.mapIndexed { i, text ->
                    calculateTextHeight(text, paint, colWidths[i] - 2 * padding)
                }.maxOrNull() ?: 25f
            }

            for (rowData in rows) {
                if (yPos + maxRowHeight > 720f) break
                x = 40f
                for (i in rowData.indices) {
                    canvas.drawRect(x, yPos, x + colWidths[i], yPos + maxRowHeight, borderPaint)
                    val isCenter = i == 0 || i == 1
                    drawWrappedText(canvas, rowData[i], x + padding, yPos + paint.textSize, colWidths[i] - 2 * padding, paint, isCenter)
                    x += colWidths[i]
                }
                yPos += maxRowHeight
            }


            val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
            val tanggalSekarang = sdf.format(Date())
           // val marginRight = 40f
            val bottomY = pageInfo.pageHeight - 80f  // Jarak dari bawah halaman
            val centerX = pageWidth / 2f
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
            pdfDocument.finishPage(page)
            val outputStream = ByteArrayOutputStream()
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            callback(outputStream.toByteArray())
            }
        }
    @RequiresApi(Build.VERSION_CODES.O)
    fun drawTransactionTable(canvas: Canvas, paint: Paint, borderPaint: Paint, yStart: Float) {
        val startX = 40f
        var yPos = yStart
        val colWidths = listOf(40f, 200f, 250f)
        val headers = listOf("No", "Tanggal", "Produk")
        val textPadding = 8f

        // Header
        var x = startX
        for (i in headers.indices) {
            canvas.drawRect(x, yPos, x + colWidths[i], yPos + 25f, borderPaint)
            val text = headers[i]
            val textWidth = paint.measureText(text)
            val centerX = x + (colWidths[i] - textWidth) / 2
            canvas.drawText(text, centerX, yPos + 17f, paint)
            x += colWidths[i]
        }
        yPos += 25f

        // Data
        firebaseDataManager.getTransactions { list ->
            var no = 1
            for (doc in list) {
                if (yPos > 800f) break
                val rowData = listOf(
                    no.toString(),
                    DateConvert.convertDate(doc.date),
                    doc.item.toString()
                )

                // Hitung tinggi maksimal dari setiap kolom (untuk wrap text)
                val heights = rowData.mapIndexed { index, text ->
                    calculateTextHeight(text, paint, colWidths[index] - 2 * textPadding)
                }
                val rowHeight = heights.maxOrNull() ?: 25f

                x = startX
                for (i in rowData.indices) {
                    canvas.drawRect(x, yPos, x + colWidths[i], yPos + rowHeight, borderPaint)
                    drawWrappedText(canvas, rowData[i], x + textPadding, yPos + paint.textSize, colWidths[i] - 2 * textPadding, paint)
                    x += colWidths[i]
                }

                yPos += rowHeight
                no++
            }
        }
    }

    // Membungkus teks agar tidak keluar dari kolom
    fun drawWrappedText(canvas: Canvas, text: String, x: Float, y: Float, maxWidth: Float, paint: Paint, centered: Boolean = false) {
        val words = text.split(" ")
        var line = ""
        var yOffset = 0f

        for (word in words) {
            val testLine = if (line.isEmpty()) word else "$line $word"
            if (paint.measureText(testLine) > maxWidth) {
                val drawX = if (centered) x + (maxWidth - paint.measureText(line)) / 2 else x
                canvas.drawText(line, drawX, y + yOffset, paint)
                line = word
                yOffset += paint.textSize + 4f
            } else {
                line = testLine
            }
        }
        if (line.isNotEmpty()) {
            val drawX = if (centered) x + (maxWidth - paint.measureText(line)) / 2 else x
            canvas.drawText(line, drawX, y + yOffset, paint)
        }
    }

    // Menghitung tinggi teks terbungkus
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


 companion object {
     const val ID = "id"
 }
    private lateinit var pdfBytes: ByteArray

    private val createDocumentLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        uri?.let {
            requireContext().contentResolver.openOutputStream(uri)?.use { out ->
                out.write(pdfBytes)
            }
        }
    }

    private fun previewAndSavePdf() {
        val assetManager = context?.assets
        val inputStream = assetManager?.open("logo_apt_mujarab.png")
        val logoBitmap = BitmapFactory.decodeStream(inputStream)

        cetakTransaksi(logoBitmap) { bytes ->
            pdfBytes = bytes

            // Simpan sementara untuk preview
            val tempFile = File(requireContext().cacheDir, "preview.pdf")
            tempFile.writeBytes(bytes)

            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                tempFile
            )

            // Intent preview PDF
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            startActivity(Intent.createChooser(intent, "Preview PDF"))

            // Setelah preview, minta lokasi simpan
            createDocumentLauncher.launch("LaporanTransaksi.pdf")
        }
    }

}