package com.example.study_habit_planer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.study_habit_planer.databinding.ActivityFixedCostsBinding
import java.util.Locale

class FixedCostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFixedCostsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFixedCostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Fixkosten Rechner"

        binding.buttonCalculate.setOnClickListener {
            calculateRemainingAmount()
        }
    }

    private fun calculateRemainingAmount() {
        val income = binding.editTextIncome.text.toString().toDoubleOrNull() ?: 0.0
        val rent = binding.editTextRent.text.toString().toDoubleOrNull() ?: 0.0
        val electricity = binding.editTextElectricity.text.toString().toDoubleOrNull() ?: 0.0
        val internet = binding.editTextInternet.text.toString().toDoubleOrNull() ?: 0.0
        val insurance = binding.editTextInsurance.text.toString().toDoubleOrNull() ?: 0.0
        val other = binding.editTextOther.text.toString().toDoubleOrNull() ?: 0.0

        val totalExpenses = rent + electricity + internet + insurance + other
        val remaining = income - totalExpenses

        binding.textViewResult.text = String.format(Locale.GERMANY, "Verbleibend: %.2f €", remaining)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}