package com.submission.tesapp.ui.transactions

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
        val borderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        val pageWidth = pageInfo.pageWidth.toFloat()
        var yPos = 50f
//// Logo dan posisi
//        val logoSize = 60
//        val logoLeft = 40f
//        val logoTop = yPos
//        val resizedLogo = Bitmap.createScaledBitmap(logoBitmap, logoSize, logoSize, false)
//        canvas.drawBitmap(resizedLogo, logoLeft, logoTop, paint)
//
//// Teks kop
//        val kop1 = "APOTEK MUJARAB"
//        val kop2Lines = listOf(
//            "Jl. Raya Warungasem, Kel. Warungasem, Kec. Warungasem,",
//            "Kab. Batang, Jawa Tengah, 51252"
//        )
//
//// Posisi teks disesuaikan agar rata tengah vertikal terhadap logo
//        val textX = logoLeft + logoSize + 15f
//        val textYStart = logoTop + 15f
//
//        canvas.drawText(kop1, textX, textYStart, boldPaint)
//
//        paint.textSize = 12f
//        for ((i, line) in kop2Lines.withIndex()) {
//            canvas.drawText(line, textX, textYStart + 20f + (i * 15f), paint)
//        }
//
//// Update yPos agar tidak tumpang tindih
//        yPos = logoTop + logoSize + 10f
//        canvas.drawLine(40f, yPos, pageWidth - 40f, yPos, borderPaint)
//        yPos += 25f
//// Garis pemisah
//        canvas.drawLine(40f, yPos, pageWidth - 40f, yPos, borderPaint)
//        yPos += 25f
        val logoSize = 60
        val logoLeft = 40f
        val logoTop = yPos
        val resizedLogo = Bitmap.createScaledBitmap(logoBitmap, logoSize, logoSize, false)
        canvas.drawBitmap(resizedLogo, logoLeft, logoTop, paint)

        val kop1 = "APOTEK MUJARAB"
        val kop2Lines = listOf(
            "Jl. Raya Warungasem, Kel. Warungasem, Kec. Warungasem,",
            "Kab. Batang, Jawa Tengah, 51252"
        )

// Hitung tinggi total dari header text
        val totalTextHeight = 20f + (kop2Lines.size * 15f)
        val textStartY = logoTop + (logoSize - totalTextHeight) / 2f + 15f

// Header Line 1 (bold) - rata tengah
        val kop1Width = boldPaint.measureText(kop1)
        canvas.drawText(kop1, (pageWidth - kop1Width) / 2f, textStartY, boldPaint)

// Header Line 2 - tiap baris rata tengah
        paint.textSize = 12f
        for ((i, line) in kop2Lines.withIndex()) {
            val lineWidth = paint.measureText(line)
            val lineX = (pageWidth - lineWidth) / 2f
            canvas.drawText(line, lineX, textStartY + 20f + (i * 15f), paint)
        }

// Update yPos
        yPos = logoTop + logoSize + 10f
        canvas.drawLine(40f, yPos, pageWidth - 40f, yPos, borderPaint)
        yPos += 25f



        // ----------------------
        // Judul
        // ----------------------
        val title = "DAFTAR TRANSAKSI"
        val titleWidth = boldPaint.measureText(title)
        canvas.drawText(title, (pageWidth - titleWidth) / 2, yPos, boldPaint)
        yPos += 30f

        // ----------------------
        // Tabel
        // ----------------------
        val startX = 40f
        val rowHeight = 25f
        val colWidths = listOf(40f, 200f, 250f) // Total lebar 490f

        // Header Kolom
        val headers = listOf("No", "Tanggal", "Produk")
        var x = startX
        for (i in headers.indices) {
            canvas.drawRect(x, yPos, x + colWidths[i], yPos + rowHeight, borderPaint)
            val text = headers[i]
            val textWidth = paint.measureText(text)
            val centerX = x + (colWidths[i] - textWidth) / 2
            canvas.drawText(text, centerX, yPos + 17f, paint)

            x += colWidths[i]
        }
        yPos += rowHeight

        firebaseDataManager.getTransactions { list ->
            var i = 1
            for (doc in list) {
                if (yPos + rowHeight > 800f) break
                x = startX

                val rowData = listOf(
                    i.toString(),
                    DateConvert.convertDate(doc.date),
                    doc.item.toString(),
                )

                for (i in rowData.indices) {
                    canvas.drawRect(x, yPos, x + colWidths[i], yPos + rowHeight, borderPaint)
                    val text = rowData[i]
                    val textWidth = paint.measureText(text)
                    val centerX = x + (colWidths[i] - textWidth) / 2
                    canvas.drawText(text, centerX, yPos + 17f, paint)
                    x += colWidths[i]
                }

                yPos += rowHeight
                i++
            }

            val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
            val tanggalSekarang = sdf.format(Date())

            val text1 = "Batang, $tanggalSekarang"
            val text2 = "(Pak Tarno)"

            val text1Width = paint.measureText(text1)
            val text2Width = paint.measureText(text2)

            val marginRight = 40f
            val posX1 = pageInfo.pageWidth - text1Width - marginRight
            val posX2 = pageInfo.pageWidth - text2Width - marginRight
            val posY1 = 770f
            val posY2 = posY1 + 60f

            canvas.drawText(text1, posX1, posY1, paint)
            canvas.drawText(text2, posX2, posY2, paint)


            pdfDocument.finishPage(page)


            val outputStream = ByteArrayOutputStream()
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()

            callback(outputStream.toByteArray())

            }
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
        val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.add)
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