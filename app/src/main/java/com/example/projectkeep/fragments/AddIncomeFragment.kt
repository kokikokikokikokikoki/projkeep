package com.example.projectkeep.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.CalendarViewBindingAdapter.setDate
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import com.example.projectkeep.R
import com.example.projectkeep.databinding.AddIncomeBinding
import com.example.projectkeep.databinding.AddTransactionBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_income.*
import java.text.SimpleDateFormat
import java.util.*


class AddIncomeFragment : Fragment(),View.OnClickListener {

    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    private lateinit var binding: AddIncomeBinding
    private lateinit var backBtn : ImageView
    private lateinit var incomeCategory : Button
    private lateinit var stocksCategory : Button
    private lateinit var otherCategory : Button
    private lateinit var savingsCategory : Button

    private lateinit var titleBtn : EditText
    private lateinit var amountBtn : EditText
    private lateinit var saveBtn : Button
    private var category = ""
    private lateinit var datePicker : EditText

    val database = FirebaseFirestore.getInstance()

    val bundle = this.arguments
    val transactionId = bundle?.getString("transactionId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_income, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = AddIncomeBinding.bind(view)

        amountBtn = binding.editMoney
        titleBtn = binding.editTitle
        datePicker = binding.editDate
        backBtn = binding.back
        incomeCategory = binding.income
        stocksCategory = binding.stocks
        otherCategory = binding.others
        savingsCategory = binding.pocket
        saveBtn = binding.addIncome


        saveBtn.setOnClickListener {
            saveTransaction()
            validateFields()
        }

        setListener(binding)
        backBtn.setOnClickListener{
            activity?.onBackPressed()
        }

        setDate()
    }
    private fun setDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker.setText("" + day + "/" + (month + 1) + "/" + year)
        datePicker.isEnabled = false
    }
    private fun setListener(binding: AddIncomeBinding) {
        binding.income.setOnClickListener(this)
        binding.stocks.setOnClickListener(this)
        binding.pocket.setOnClickListener(this)

        binding.others.setOnClickListener(this)


    }


    override fun onClick(v: View?) {
        when (v) {
            binding.income -> {
                setCategory(v,binding.income)
            }
            binding.stocks -> {
                setCategory(v,binding.stocks)
            }
            binding.others -> {
                setCategory(v,binding.others)
            }
            binding.pocket -> {
                setCategory(v,binding.pocket)
            }

        }
    }



    private fun setCategory(v: View, button: MaterialButton) {
        category = button.text.toString()
        //print out category when selected
        Log.d("category", category)
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.mtrl_btn_text_btn_bg_color_selector))
        button.setIconTintResource(R.color.purple_200)
        button.setStrokeColorResource(R.color.purple_200)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        when (v) {
            binding.income -> {
                removeBackground(binding.pocket)
                removeBackground(binding.stocks)

                removeBackground(binding.others)

            }
            binding.pocket -> {
                removeBackground(binding.income)
                removeBackground(binding.stocks)

                removeBackground(binding.others)

            }
            binding.stocks -> {
                removeBackground(binding.income)
                removeBackground(binding.pocket)

                removeBackground(binding.others)

            }
            binding.others -> {
                removeBackground(binding.pocket)
                removeBackground(binding.income)
                removeBackground(binding.stocks)

            }

        }
    }

    private fun removeBackground(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
        button.setIconTintResource(R.color.textSecondary)
        button.setStrokeColorResource(R.color.textSecondary)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.textSecondary))
    }
    private fun saveTransaction() {
        val date = datePicker.text.toString()
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val parsedDate = formatter.parse(date)
        val timestamp = Timestamp(parsedDate)
        val currentDocument = database.collection("Transaction").document()
        val income = hashMapOf(
            "title" to titleBtn.text.toString(),
            "amount" to amountBtn.text.toString().toDouble().toLong(),
            "categoryType" to category,
            "date" to timestamp,
            "transactionType" to "Income",
            "id" to currentDocument.id
        )
        currentDocument.set(income)
        //database.collection("Transaction").add(income)


// go back to home fragment after saving
        activity?.onBackPressed()

    }

    private fun validateFields(): Boolean {
        return when {
            TextUtils.isEmpty(titleBtn.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter a title.",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            TextUtils.isEmpty(amountBtn.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter an amount.",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
    }


}