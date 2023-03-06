package com.example.projectkeep.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import com.example.projectkeep.R
import com.example.projectkeep.databinding.AddIncomeBinding
import com.example.projectkeep.databinding.AddTransactionBinding
import com.example.projectkeep.model.Transactions
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_income.*
import java.text.SimpleDateFormat
import java.util.*


class EditIncomeFragment : Fragment(), View.OnClickListener {
private lateinit var binding: AddIncomeBinding
private lateinit var backBtn : ImageView

    val database = FirebaseFirestore.getInstance()

    private lateinit var titleBtn: EditText
    private lateinit var amountBtn: EditText
    private lateinit var saveBtn: Button
    private var category = ""
    private lateinit var datePicker: EditText
    private lateinit var incomeCategory: Button
    private lateinit var stocksCategory: Button
    private lateinit var otherCategory: Button
    private lateinit var giftsCategory : Button


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
        //binding of layout
        binding = AddIncomeBinding.bind(view)
        backBtn = binding.back
        saveBtn = binding.addIncome
        amountBtn = binding.editMoney
        titleBtn = binding.editTitle
        datePicker = binding.editDate
        incomeCategory = binding.income
        stocksCategory = binding.stocks
        otherCategory = binding.others
        giftsCategory = binding.pocket
        val bundle = this.arguments


        retrieveIncomeData()
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        setListener(binding)

        saveBtn.setOnClickListener{
            updateIncomeData()
        }


    }

    private fun setListener(binding: AddIncomeBinding) {
        binding.income.setOnClickListener(this)
        binding.others.setOnClickListener(this)
        binding.stocks.setOnClickListener(this)
        binding.pocket.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.income -> {
                setCategory(v, binding.income)



            }
            binding.stocks -> {
                setCategory(v, binding.stocks)
            }
            binding.pocket -> {
                setCategory(v, binding.pocket)
            }
            binding.others -> {
                setCategory(v, binding.others)
            }

        }
    }
    private fun setCategory(v: View, button: MaterialButton) {
        category = button.text.toString()
        button.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                com.google.android.material.R.color.mtrl_btn_text_btn_bg_color_selector
            )
        )
        button.setIconTintResource(R.color.purple_200)
        button.setStrokeColorResource(R.color.purple_200)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        when (v) {
            binding.income -> {
                removeBackground(binding.stocks)
                removeBackground(binding.pocket)

                removeBackground(binding.others)

            }
            binding.stocks -> {
                removeBackground(binding.income)
                removeBackground(binding.pocket)

                removeBackground(binding.others)

            }
            binding.pocket -> {
                removeBackground(binding.income)
                removeBackground(binding.stocks)

                removeBackground(binding.others)

            }

            binding.others -> {
                removeBackground(binding.income)
                removeBackground(binding.stocks)
                removeBackground(binding.pocket)

            }

        }
    }


    private fun removeBackground(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
        button.setIconTintResource(R.color.textSecondary)
        button.setStrokeColorResource(R.color.textSecondary)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.textSecondary))
    }

    private fun retrieveIncomeData() {

        val incomeId = arguments?.getString("id")
        val transRef = incomeId?.let { database.collection("Transaction").document(it) }
//log the expense id
        Log.d("incomeId", incomeId.toString())
        //disable title field
        titleBtn.isEnabled = false
        titleBtn.isClickable = false

        incomeCategory.isClickable = false
        stocksCategory.isClickable = false
        otherCategory.isClickable = false
        giftsCategory.isClickable = false


        // click date and can choose the date
        datePicker.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            //cannot choose date in the future
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    datePicker.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                },
                year,
                month,
                day
            )
            dpd.show()
            dpd.datePicker.maxDate = System.currentTimeMillis()
        }


        if (transRef != null) {
            transRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val timestamp = document.getTimestamp("date")
                    if (timestamp != null) {
                        val formatter = SimpleDateFormat("dd/MM/yyyy")
                        val date = timestamp.toDate()
                        val dateString = formatter.format(date)
                        datePicker.setText(dateString)

                    }
                    val transaction = document.toObject(Transactions::class.java)

                    if (transaction != null) {
                        titleBtn.setText(transaction.title)
                        amountBtn.setText(transaction.amount.toString())
                        category = transaction.categoryType.toString()
                        when (category) {
                            "Income" -> {
                                setCategory(binding.income, binding.income)
                            }
                            "Stocks" -> {
                                setCategory(binding.stocks, binding.stocks)
                            }
                            "Gifts" -> {
                                setCategory(binding.pocket, binding.pocket)
                            }

                            "Other" -> {
                                setCategory(binding.others, binding.others)
                            }

                        }
                    }


                }
            }
                // log if the document is not found
                .addOnFailureListener { exception ->
                    Log.d("errormsg", "get failed with ", exception)
                }
        }
    }

    private fun updateIncomeData() {
        val incomeId = arguments?.getString("id")
        val transRef = incomeId?.let { database.collection("Transaction").document(it) }

        val date = datePicker.text.toString()
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val parsedDate = formatter.parse(date)
        val timestamp = Timestamp(parsedDate)

        val title = titleBtn.text.toString()
        val amount = amountBtn.text.toString().toDouble()
        val categoryType = category

        transRef?.update(

            "date", timestamp,
            "title", title,
            "amount", amount.toLong(),
            "categoryType", categoryType
        )

            ?.addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully updated!")
                // Navigate back to the expense list or wherever you want to go after the update is done.
                activity?.onBackPressed()
            }
            ?.addOnFailureListener { e ->
                Log.w("TAG", "Error updating document", e)
            }

    }
}