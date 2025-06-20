package com.submission.tesapp.ui.transactions

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
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

    private lateinit var drugInput: TextInputEditText
    private lateinit var dateText: TextView
    private lateinit var saveButton: Button

    private lateinit var binding: ActivityInputTransactionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityInputTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivMenu.setOnClickListener {
           onBackPressedDispatcher.onBackPressed()
        }
        binding.date.setOnClickListener {
            val dialogFragment = DatePickerFragment()
            dialogFragment.show(supportFragmentManager,"datePicker")
        }
        binding.addDate.setText("Tanggal")


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
        drugInput = findViewById<TextInputEditText>(R.id.add_drug)
        dateText = findViewById<TextView>(R.id.add_date)
        saveButton = findViewById<Button>(R.id.btn_input)

        saveButton.isEnabled = false
        saveButton.alpha = 0.5f

        fun validateInput() {
            val drugText = drugInput.text?.toString()?.trim() ?: ""
            val wordCount = drugText.split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
            val isDrugValid = wordCount >= 2
            val isDateValid = dateText.text.toString().isNotBlank()

            saveButton.isEnabled = isDrugValid && isDateValid
            saveButton.alpha = if (saveButton.isEnabled) 1f else 0.5f
        }

// Tambahkan listener
        drugInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        dateText.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            validateInput()
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
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}