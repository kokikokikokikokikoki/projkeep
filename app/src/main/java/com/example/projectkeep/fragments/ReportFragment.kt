package com.example.projectkeep.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.viewpager2.widget.ViewPager2
import com.example.projectkeep.R
import com.example.projectkeep.adapter.ViewPagerAdapter
import com.example.projectkeep.databinding.AddGoalBinding
import com.example.projectkeep.databinding.AddIncomeBinding

import com.example.projectkeep.databinding.FragmentAddTransactionLayoutBinding
import com.example.projectkeep.model.Constants
import com.example.projectkeep.model.TransactionModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_goal.*
import kotlinx.android.synthetic.main.content_add_transaction.*
import kotlinx.android.synthetic.main.content_add_transaction.view.*
import kotlinx.android.synthetic.main.fragment_add_transaction_layout.*
import kotlinx.android.synthetic.main.transaction_item.*
import java.util.*




class ReportFragment : Fragment(R.layout.add_income) {
    //R.layout.fragment_add_transaction_layout
    private lateinit var binding: AddIncomeBinding

    // private lateinit var binding: FragmentAddTransactionLayoutBinding

    private lateinit var database: DatabaseReference
// link fragment_add_income.xml

    private lateinit var category : Button
    private lateinit var categorytwo : Button
    private lateinit var categorythree : Button
    private lateinit var categoryfour : Button

    // link the date picker
    private lateinit var datePicker: EditText
    //link title
    private lateinit var title: EditText
    //link amount
    private lateinit var amount: EditText

    //link the add button
    private lateinit var addBtn: Button

    //link the cancel button
    private lateinit var cancelBtn: ImageView




   private fun setDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker.setText("" + day + "/" + (month + 1) + "/" + year)
    }



    private fun addBtnPressed() {
        addBtn.setOnClickListener {
            Toast.makeText(context, "Add", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelBtnPressed() {
        cancelBtn.setOnClickListener {
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddIncomeBinding.bind(view)
        //binding = FragmentAddTransactionLayoutBinding.bind(view)


        //initialize the views
        category = binding.income
        categorytwo= binding.stocks
        categorythree=binding.pocket
        categoryfour=binding.others

        datePicker = binding.editDate
        title = binding.editTitle
        amount = binding.editMoney
        addBtn = binding.addIncome
        cancelBtn = binding.back

        //set on click listeners

        setDate()
        addBtnPressed()
        cancelBtnPressed()
    }


    }






//    lateinit var title: EditText
//    lateinit var addCategory: AutoCompleteTextView
//    lateinit var addDate: EditText
//    lateinit var amount: EditText
//    lateinit var addBtn: Button
//    lateinit var transactiontype: AutoCompleteTextView
// transactiontypemodel is the model class for transaction type
    //  private lateinit var transactiontypemodel: TransactionTypeModel

    // private lateinit var transactiontype: TransactionTypeModel



































