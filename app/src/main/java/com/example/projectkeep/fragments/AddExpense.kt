package com.example.projectkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.bind
import com.example.projectkeep.R
import com.example.projectkeep.adapter.TransactionAdapter
import com.example.projectkeep.databinding.ActivityMainBinding.bind
import com.example.projectkeep.databinding.AddIncomeBinding
import com.example.projectkeep.databinding.AddTransactionBinding
import com.example.projectkeep.databinding.FragmentAddExpenseBinding
import com.example.projectkeep.databinding.FragmentAddTransactionLayoutBinding
import com.example.projectkeep.model.Transactions
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_transaction.*
import kotlinx.android.synthetic.main.transaction_item.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AddExpense : Fragment(),View.OnClickListener {

    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    // TODO: Rename and change types of parameters
    private lateinit var binding: AddTransactionBinding
    private lateinit var backBtn : ImageView

    private lateinit var titleBtn : EditText
    private lateinit var amountBtn : EditText
    private lateinit var saveBtn : Button
    private var category = ""
    private lateinit var datePicker : EditText
    private lateinit var categoryFood : Button
    private lateinit var categoryTransport : Button
    private lateinit var categoryShopping : Button
    private lateinit var categoryEducation : Button
    private lateinit var categoryMedical : Button
    private lateinit var categoryOther : Button




    val database = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_transaction, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddTransactionBinding.bind(view)
        //backBtn = go back to previous fragment(home fragment)
        backBtn = binding.back
        datePicker = binding.editDate
        titleBtn = binding.editTitle
        amountBtn = binding.editMoney
        saveBtn = binding.addTransaction


        saveBtn.setOnClickListener {

           if( validateFields()) {

               saveTransaction()
           }
        }

        setListener(binding)

            categoryFood = binding.food
            categoryShopping = binding.shopping
            categoryTransport = binding.transport
            categoryEducation = binding.academics
            categoryMedical = binding.health
            categoryOther = binding.others


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
                setCategory(v,binding.food)
                category = "Food"


            }
            binding.shopping -> {
                setCategory(v,binding.shopping)
            }
            binding.transport -> {
                setCategory(v,binding.transport)
            }
            binding.health -> {
                setCategory(v,binding.health)
            }
            binding.others -> {
                setCategory(v,binding.others)
            }
            binding.academics -> {
                setCategory(v,binding.academics)
            }
        }
    }
private fun setCategory(v: View, button: MaterialButton) {
    category = button.text.toString()
    button.setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.mtrl_btn_text_btn_bg_color_selector))
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

    private fun saveTransaction() {
        val date = datePicker.text.toString()
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val parsedDate = formatter.parse(date)
        val timestamp = Timestamp(parsedDate)

       val expenses = hashMapOf(
    "title" to titleBtn.text.toString(),
                "amount" to amountBtn.text.toString().toDouble().toLong(),
                "categoryType" to category,
                "date" to timestamp,
                "transactionType" to "Expense"
            )
            database.collection("Transaction").add(expenses)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }




// go back to home fragment after saving
        activity?.onBackPressed()












    }
    // if the fields are not filled, show a toast message
    private fun validateFields(): Boolean {
        if (TextUtils.isEmpty(titleBtn.text)) {
            Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(amountBtn.text)) {
            Toast.makeText(context, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }



}