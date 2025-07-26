package com.submission.appriori.ui.transactions

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.appriori.R
import com.submission.appriori.adapter.TransactionAdapter
import com.submission.appriori.adapter.TransactionsAdapter
import com.submission.appriori.data.TransactionsPagingSource
import com.submission.appriori.data.model.TransactionModel
import com.submission.appriori.databinding.FragmentTransactionsBinding
import com.submission.appriori.firebase.FirebaseDataManager
import com.submission.appriori.utils.DateConvert
import com.submission.appriori.utils.TextUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionsFragment : Fragment() {
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TransactionAdapter
    private val viewModel by viewModels<TransactionsViewModel>()
    private lateinit var adapter1: TransactionsAdapter
    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarProcess.visibility = View.VISIBLE
        binding.cv1.visibility = View.INVISIBLE
        binding.cetak.visibility = View.INVISIBLE

        adapter1 = TransactionsAdapter(
            onEditClicked = { userData ->
                val intent = Intent(requireContext(), InputTransactionsActivity::class.java)
                intent.putExtra(InputTransactionsActivity.ID, userData)
                startActivity(intent)
                requireActivity().overridePendingTransition(
                    R.anim.slide_in_up,
                    R.anim.slide_out_down
                )
            },
            onDeleteConfirmed = { id ->
                showDeleteConfirmationDialogID(id)
            }
        )
        firebaseDataManager = FirebaseDataManager()
        binding.refresh.setOnRefreshListener {
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
        binding.rvTx.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapter1
        }
        val firestore = FirebaseFirestore.getInstance()
        val pager = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                TransactionsPagingSource(firestore, 10)
            }
        )

        lifecycleScope.launch {
            pager.flow.collectLatest { pagingData ->
                adapter1.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            adapter1.loadStateFlow.collectLatest { loadStates ->
                val isListEmpty =
                    loadStates.refresh is LoadState.NotLoading && adapter1.itemCount == 0

                binding.noData.visibility = if (isListEmpty) View.VISIBLE else View.GONE
                binding.rvTx.visibility = if (isListEmpty) View.GONE else View.VISIBLE

                binding.progressBarProcess.visibility =
                    if (loadStates.refresh is LoadState.Loading) View.VISIBLE else View.GONE
                binding.cv1.visibility =
                    if (loadStates.refresh is LoadState.Loading) View.GONE else View.VISIBLE
                binding.cetak.visibility =
                    if (loadStates.refresh is LoadState.Loading) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showDeleteConfirmationDialogID(id: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi")
            .setMessage("Apakah kamu yakin akan menghapus transaksi ini?")
            .setPositiveButton("Hapus") { _, _ ->
                binding.progressBarProcess.visibility = View.VISIBLE
                binding.rvTx.visibility = View.INVISIBLE

                firebaseDataManager.deleteTransactions(id) { success ->
                    if (success) {
                        showToast("Berhasil dihapus")
                        adapter1.refresh() // ✅ Bisa digunakan karena di dalam Fragment
                    } else {
                        showToast("Gagal menghapus")
                    }

                    binding.progressBarProcess.visibility = View.GONE
                    binding.rvTx.visibility = View.VISIBLE
                }
            }
            .setNegativeButton("Batal", null)
            .show()
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
                showToast("Berhasil menghapus data transaksi!")
            } else {
                binding.rvTx.visibility = View.VISIBLE
                binding.progressBarProcess.visibility = View.GONE
                showToast("Data transaksi gagal dihapus")
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupFirebase() {
        val firestore = FirebaseFirestore.getInstance()
        val pager = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                TransactionsPagingSource(firestore, 10)
            }
        )

        lifecycleScope.launch {
            pager.flow.collectLatest { pagingData ->
                adapter1.submitData(pagingData)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun cetakTransaksi(logoBitmap: Bitmap, callback: (ByteArray) -> Unit) {
        val pdfDocument = PdfDocument()
        val pageWidth = 595f
        val pageHeight = 842f
        val padding = 8f
        val leftMargin = 40f
        val topMarginFirstPage = 50f
        val topMarginNextPage = 30f

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

        val noWidth = 60f
        val dateWidth = 100f
        val produkWidth = pageWidth - 80f - noWidth - dateWidth
        val colWidths = listOf(noWidth, dateWidth, produkWidth)

        val headers = listOf("No", "Tanggal", "Produk")

        var pageIndex = 1
        var yPos = topMarginFirstPage
        var pageInfo =
            PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), pageIndex).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas


        val logoTargetSize = 80f
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
        for ((i, line) in kop2Lines.withIndex()) {
            val lineX = (pageWidth - paint.measureText(line)) / 2f
            val lineY = textStartY + kop2LineHeight + (i * kop2LineHeight)
            canvas.drawText(line, lineX, lineY, paint)
        }

        yPos = maxOf(logoTop + logoHeight, textStartY + totalTextHeight) + 25f
        canvas.drawLine(leftMargin, yPos, pageWidth - leftMargin, yPos, borderPaint)
        yPos += 25f


        val title = "DAFTAR TRANSAKSI"
        val titleWidth = boldPaint.measureText(title)
        canvas.drawText(title, (pageWidth - titleWidth) / 2, yPos, boldPaint)
        yPos += 30f
        var currentY = yPos
        var isFirstPage = true

        fun drawTableHeader() {
            if (!isFirstPage) return
            var x = 40f
            val headerHeight = 25f
            for (i in headers.indices) {
                canvas.drawRect(x, currentY, x + colWidths[i], currentY + headerHeight, borderPaint)
                val text = headers[i]
                val centerX = x + (colWidths[i] - boldSmallPaint.measureText(text)) / 2
                canvas.drawText(text, centerX, currentY + 17f, boldSmallPaint)
                x += colWidths[i]
            }
            currentY += headerHeight
            isFirstPage = false
        }

        drawTableHeader()
        yPos = currentY

        firebaseDataManager.getTransactions { list ->
            val rows = list.mapIndexed { index, doc ->
                listOf(
                    (index + 1).toString(),
                    DateConvert.convertDate(doc.date),
                    doc.item.toString()
                )
            }

            for (rowData in rows) {
                val rowHeight = rowData.mapIndexed { i, text ->
                    calculateTextHeight(text, paint, colWidths[i] - 2 * padding)
                }.maxOrNull()?.coerceAtLeast(25f) ?: 25f

                if (yPos + rowHeight > pageHeight - 120f) {
                    // Akhiri halaman sebelumnya
                    pdfDocument.finishPage(page)
                    pageIndex++
                    pageInfo = PdfDocument.PageInfo.Builder(
                        pageWidth.toInt(),
                        pageHeight.toInt(),
                        pageIndex
                    ).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    yPos = topMarginNextPage
                }

                var x = leftMargin
                for (i in rowData.indices) {
                    canvas.drawRect(x, yPos, x + colWidths[i], yPos + rowHeight, borderPaint)
                    val isCenter = i == 0 || i == 1
                    //drawWrappedText(canvas, rowData[i], x + padding, yPos + paint.textSize, colWidths[i] - 2 * padding, paint, isCenter)
                    drawWrappedText(
                        canvas,
                        rowData[i],
                        x + padding,
                        yPos,
                        colWidths[i] - 2 * padding,
                        paint,
                        isCenter,
                        rowHeight
                    )

                    x += colWidths[i]
                }
                yPos += rowHeight
            }


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

            // ===== Output =====
            val outputStream = ByteArrayOutputStream()
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            callback(outputStream.toByteArray())
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


    companion object {
        const val ID = "id"
    }

    private lateinit var pdfBytes: ByteArray

    private val createDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
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


            createDocumentLauncher.launch("LaporanTransaksi.pdf")
        }
    }

}