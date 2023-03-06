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
import com.example.projectkeep.databinding.AddTransactionBinding
import com.example.projectkeep.databinding.EditGoalBinding
import com.example.projectkeep.model.Transactions
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Unit.toString


class EditExpenseFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: AddTransactionBinding
    private lateinit var backBtn: ImageView

    val database = FirebaseFirestore.getInstance()
    private lateinit var titleBtn: EditText
    private lateinit var amountBtn: EditText
    private lateinit var saveBtn: Button
    private var category = ""
    private lateinit var datePicker: EditText
    private lateinit var categoryFood: Button
    private lateinit var categoryTransport: Button
    private lateinit var categoryShopping: Button
    private lateinit var categoryEducation: Button
    private lateinit var categoryMedical: Button
    private lateinit var categoryOther: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = AddTransactionBinding.bind(view)
        backBtn = binding.back
        datePicker = binding.editDate
        titleBtn = binding.editTitle
        amountBtn = binding.editMoney
        saveBtn = binding.addTransaction

        val bundle = this.arguments

        //buttons
        categoryFood = binding.food
        categoryShopping = binding.shopping
        categoryTransport = binding.transport
        categoryEducation = binding.academics
        categoryMedical = binding.health
        categoryOther = binding.others


        retrieveExpenseData()

        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        setListener(binding)


        saveBtn.setOnClickListener {
            updateExpenseData()
        }


    }

    private fun setListener(binding: AddTransactionBinding) {
        binding.food.setOnClickListener(this)
        binding.shopping.setOnClickListener(this)
        binding.transport.setOnClickListener(this)
        binding.health.setOnClickListener(this)
        binding.others.setOnClickListener(this)
        binding.academics.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.food -> {
                setCategory(v, binding.food)
                category = "Food"


            }
            binding.shopping -> {
                setCategory(v, binding.shopping)
                category = "Shopping"
            }
            binding.transport -> {
                setCategory(v, binding.transport)
                category = "Transport"
            }
            binding.health -> {
                setCategory(v, binding.health)
                category = "Medical"

            }
            binding.others -> {
                setCategory(v, binding.others)
                category = "Other"
            }
            binding.academics -> {
                setCategory(v, binding.academics)
                category = "Education"
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
            binding.food -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.shopping -> {
                removeBackground(binding.food)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.transport -> {
                removeBackground(binding.shopping)
                removeBackground(binding.food)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.health -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.food)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.others -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.food)
                removeBackground(binding.academics)
            }
            binding.academics -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.food)
            }
        }
    }

    private fun removeBackground(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
        button.setIconTintResource(R.color.textSecondary)
        button.setStrokeColorResource(R.color.textSecondary)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.textSecondary))
    }

    private fun retrieveExpenseData() {

        val expenseId = arguments?.getString("id")
        val transRef = expenseId?.let { database.collection("Transaction").document(it) }
//log the expense id
        Log.d("ExpenseId", expenseId.toString())
        //disable title field
        titleBtn.isEnabled = false
        titleBtn.isClickable = false

        categoryFood.isClickable = false
        categoryShopping.isClickable = false
        categoryTransport.isClickable = false
        categoryEducation.isClickable = false
        categoryMedical.isClickable = false
        categoryOther.isClickable = false

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
                            "Food" -> {
                                setCategory(binding.food, binding.food)
                            }
                            "Shopping" -> {
                                setCategory(binding.shopping, binding.shopping)
                            }
                            "Transport" -> {
                                setCategory(binding.transport, binding.transport)
                            }
                            "Medical" -> {
                                setCategory(binding.health, binding.health)
                            }
                            "Other" -> {
                                setCategory(binding.others, binding.others)
                            }
                            "Education" -> {
                                setCategory(binding.academics, binding.academics)
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


    private fun updateExpenseData() {
        val expenseId = arguments?.getString("id")
        val transRef = expenseId?.let { database.collection("Transaction").document(it) }

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