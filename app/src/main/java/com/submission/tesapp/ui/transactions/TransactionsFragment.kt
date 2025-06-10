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
            } else {
                binding.noData.visibility = View.INVISIBLE
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

        // ----------------------
        // ==== HEADER DENGAN LOGO & KOP TENGAH ====

// Logo di kiri
        val resizedLogo = Bitmap.createScaledBitmap(logoBitmap, 60, 60, false)
        canvas.drawBitmap(resizedLogo, 40f, yPos, paint)

// KOP RATA TENGAH (dengan penyesuaian posisi vertikal terhadap logo)
        val kop1 = "APOTEK MUJARAB"
        val kop2 = "Jl. Raya Warungasem, Kel. Warungasem, Kec. Warungasem, Kab. Batang, Jawa Tengah, 51252"

// Ukuran dan posisi teks
        boldPaint.textSize = 16f
        paint.textSize = 12f

        val kop1Width = boldPaint.measureText(kop1)
        val kop2Width = paint.measureText(kop2)

        val textCenterX = (pageWidth - kop1Width.coerceAtLeast(kop2Width)) / 2f
        val logoTop = yPos
        val textTop = yPos + 10f // geser agar sejajar tengah logo

// Tulis teks tengah
        canvas.drawText(kop1, textCenterX, textTop + 15f, boldPaint)
        canvas.drawText(kop2, textCenterX, textTop + 33f, paint)

// Update posisi Y
        yPos += 60f

// Garis pemisah
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