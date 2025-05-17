package com.submission.tesapp.ui.transactions

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.submission.tesapp.R
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.databinding.ActivityInputTransactionsBinding
import com.submission.tesapp.databinding.ActivityResultDetailBinding
import com.submission.tesapp.firebase.FirebaseDataManager
import com.submission.tesapp.ui.MainActivity
import com.submission.tesapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class InputTransactionsActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener  {
    private val firebaseDataManager: FirebaseDataManager = FirebaseDataManager()
    private lateinit var binding: ActivityInputTransactionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.date.setOnClickListener {
            val dialogFragment = DatePickerFragment()
            dialogFragment.show(supportFragmentManager,"datePicker")
        }

        val txId: TransactionModel? = intent.getParcelableExtra(ID)
        binding.btnInput.setOnClickListener {
            if (txId?.id != null) {
                editTransactions()
            } else {
                saveTransactions()
            }
        }
        if (txId != null) {
            binding.addDrug.setText(txId?.item)
            val item = binding.addDrug.text.toString()
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val formattedTimestamp = dateFormat.format(txId?.date ?: Date())
            binding.addDate.setText(formattedTimestamp)
            binding.date.setOnClickListener {
                val dialogFragment = DatePickerFragment()
                dialogFragment.show(supportFragmentManager, "datePicker")
            }
        }
    }
    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.addDate.text = dateFormat.format(calendar.time)
        InputTransactionsActivity.dueDateMillis = dateFormat.calendar.timeInMillis
    }

    private fun saveTransactions() {
        val item = binding.addDrug.text.toString()
        val dateText = binding.addDate.text.toString()
        val date = dueDateMillis

        firebaseDataManager.saveTransaction(item, date) {success ->
            if(success) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                binding.addDrug.text?.clear()
                binding.addDate.text = ""
            } else {
            }

        }

    }
    private fun editTransactions() {
        val txId: TransactionModel? = intent.getParcelableExtra(ID)

        if(txId != null) {

            val item = binding.addDrug.text.toString()
            val date = dueDateMillis
            txId.id?.let {
                firebaseDataManager.updateTransactions(it, item, dueDateMillis) { success ->
                    if(success) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        binding.addDrug.text?.clear()
                    } else {

                    }
                }
            }

        }

    }


    companion object {

        var dueDateMillis: Long = System.currentTimeMillis()
        const val ID = "id"
    }

}