package com.example.projectkeep.fragments

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectkeep.R
import com.example.projectkeep.adapter.TransactionAdapter
import com.example.projectkeep.databinding.FragmentDashboardBinding

import com.example.projectkeep.model.Transactions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_dashboard.*

import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(R.layout.fragment_dashboard) {
    //fab buttons
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this.context,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this.context,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this.context,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this.context,
            R.anim.to_bottom_anim
        )
    }
    private var clicked = false

//    private lateinit var recyclerView: RecyclerView
//    private lateinit var transactionAdapter: TransactionAdapter
//    private lateinit var transactionArrayList : ArrayList<Transactions>

   // var db = Firebase.firestore

  private lateinit var binding: FragmentDashboardBinding
    private lateinit var navController: NavController
    lateinit var userDetails: SharedPreferences
//connect firestore and recycler
  private  lateinit var recyclerView: RecyclerView
  private lateinit var transactionList : ArrayList<Transactions>
  private lateinit var transactionAdapter : TransactionAdapter
  private var db = Firebase.firestore

  private lateinit var chipGroup : ChipGroup
    private lateinit var chipAll : Chip
    private lateinit var tenDaysChip: Chip
    private lateinit var thirtyDaysChip: Chip
    private lateinit var sixtyDaysChip: Chip



 // private  lateinit var db : FirebaseFirestore





    val database = FirebaseDatabase.getInstance()

    private lateinit var totalBalance: TextView
    private lateinit var totalIncome: TextView
    private lateinit var totalExpense: TextView

    //var totalBalanceValue = 0
    var totalIncomeValue = 0
    var totalExpenseValue = 0




    private fun getTotalExpense(){
        totalExpense = binding.itemExpenseCardView.tvTotalExpenses

        db.collection("Transaction").whereEqualTo("transactionType","Expense")
            .get()
            .addOnSuccessListener { result ->
                 totalExpenseValue = 0
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    totalExpenseValue += document.data["amount"].toString().toInt()
                    totalExpense.text = totalExpenseValue.toString()
                    // add comma to total expense
                    val formatter = NumberFormat.getInstance(Locale.US)
                    val formatted = formatter.format(totalExpenseValue)
                    totalExpense.text = formatted

                    //add baht sign
                    totalExpense.text = "฿$formatted"


                    getTotalBalance()
                   // totalExpense.text = document.data["totalExpense"].toString()
                }

            }
    }






    private fun getTotalIncome(){
        totalIncome = binding.itemIncomeCardView.tvTotalIncome




        db.collection("Transaction").whereEqualTo("transactionType","Income")
            .get()
           // get the sum of all the income
            .addOnSuccessListener { result ->
                 totalIncomeValue = 0
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    // total income = total income + income

                    totalIncomeValue += document.data["amount"].toString().toInt()


                    totalIncome.text = totalIncomeValue.toString()
                    //add comma to the total income
                    val formatter = NumberFormat.getInstance(Locale.US)
                    val formatted = formatter.format(totalIncomeValue)
                    totalIncome.text = formatted

                    //add baht sign
                    totalIncome.text = "฿" + totalIncome.text
                    getTotalBalance()
                   // totalIncome.text = document.data["totalIncome"].toString()
                }
//                totalIncome.text = totalIncomeValue.toString()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


    }

    private fun getRecyclerView() {

        //val db = FirebaseFirestore.getInstance()
        db.collection("Transaction").orderBy("date",com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {

                if (!it.isEmpty){
                    for (data in it.documents){
                        val transaction = data.toObject(Transactions::class.java)
                        transactionList.add(transaction!!)

                    }
                }
                transactionAdapter = TransactionAdapter(transactionList)
                recyclerView.adapter = transactionAdapter



            }





    }




    private fun getTotalBalance() {

        //total balance = total income - total expense
        totalBalance = binding.totalBalanceView.totalBalance
        val totalBalanceValue = totalIncomeValue - totalExpenseValue
        totalBalance.text = totalBalanceValue.toString()



        // add comma to the total balance
        val formatter = NumberFormat.getInstance(Locale.US)
        val formattedBalance = formatter.format(totalBalanceValue)
        totalBalance.text = formattedBalance

        // add baht sign to the total balance
        totalBalance.text = "฿ $formattedBalance"


        // print out in logcat the total balance
        Log.d(TAG, "Total Balance: $totalBalanceValue")
        Log.d(TAG, "Total Income: $totalIncomeValue")
        Log.d(TAG, "Total Expense: $totalExpenseValue")



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
       
        //binding = FragmentDashboardBinding.inflate(layoutInflater)
    }

























    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)



        recyclerView = binding.rvTransactions
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        transactionList = arrayListOf()
        transactionAdapter = TransactionAdapter(transactionList)
        recyclerView.adapter = transactionAdapter

        chipGroup = binding.chipsTransactionsFilter.filterChipGroup
        chipAll = binding.chipsTransactionsFilter.chipAllTransactions
        tenDaysChip = binding.chipsTransactionsFilter.chipTenDays
        thirtyDaysChip = binding.chipsTransactionsFilter.chipThirtyDays
        sixtyDaysChip = binding.chipsTransactionsFilter.chipSixtyDays




        getTotalIncome()
        getTotalExpense()

        getTotalBalance()
        getRecyclerView()





        val db = Firebase.firestore
        // get transaction data from firestore
        db.collection("Transaction")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }









        binding.addNew.setOnClickListener() {
            onAddButtonClicked()
//Toast.makeText(context, "Add new", Toast.LENGTH_SHORT).show()
        }
    add_income.setOnClickListener()
    {
        val addIncomeFragment = AddIncomeFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, AddIncomeFragment())
        transaction.addToBackStack(null)
        transaction.commit()

        Toast.makeText(context, "Add income", Toast.LENGTH_SHORT).show()



    }
    add_expense.setOnClickListener()
    {
       // once the button is clicked it will go to add expense fragment
        val addExpenseFragment = AddExpense()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, AddExpense())
        transaction.addToBackStack(null)
        transaction.commit()


        Toast.makeText(context, "Add expense", Toast.LENGTH_SHORT).show()


    }


        chipAll.setOnClickListener {
            // Handle all transactions chip click
            Toast.makeText(context, "All transactions", Toast.LENGTH_SHORT).show()
            getRecyclerView()

        }
        tenDaysChip.setOnClickListener {
            // Handle 10 days chip click
            Toast.makeText(context, "10 days", Toast.LENGTH_SHORT).show()
            updateRecyclerView(10)
        }
        thirtyDaysChip.setOnClickListener {
            // Handle 30 days chip click
            Toast.makeText(context, "30 days", Toast.LENGTH_SHORT).show()
            updateRecyclerView(30)

        }
        sixtyDaysChip.setOnClickListener {
            // Handle 60 days chip click
            Toast.makeText(context, "60 days", Toast.LENGTH_SHORT).show()
            updateRecyclerView(60)

        }














}




    private fun onAddButtonClicked() {
    setVisibility(clicked)
    setAnimation(clicked)
    if (!clicked) clicked = true
    else clicked = false

}


    fun addCommasToAmount(amount: Int): String {
        val formatter = NumberFormat.getInstance(Locale.US)
        return formatter.format(amount)
    }


    private fun setAnimation(clicked: Boolean) {
    if (!clicked) {
        add_income.startAnimation(fromBottom)
        add_expense.startAnimation(fromBottom)
        add_new.startAnimation(rotateOpen)
    } else {
        add_income.startAnimation(toBottom)
        add_expense.startAnimation(toBottom)
        add_new.startAnimation(rotateClose)
    }
}

private fun setVisibility(clicked: Boolean) {
    if (!clicked) {
        add_income.visibility = View.VISIBLE
        add_expense.visibility = View.VISIBLE

    } else {
        add_income.visibility = View.INVISIBLE
        add_expense.visibility = View.INVISIBLE

    }
}



private fun updateRecyclerView(days: Int = 0) {
    // Get the current date
    val currentDate = Calendar.getInstance().time
    // Get the date 10 days ago
    val calendar = Calendar.getInstance()
    calendar.time = currentDate
    calendar.add(Calendar.DAY_OF_YEAR, -days)
    val date10DaysAgo = calendar.time

    // Get the transactions from the database
    val db = Firebase.firestore
    db.collection("Transaction")
        .whereGreaterThanOrEqualTo("date", date10DaysAgo)
        .get()
        .addOnSuccessListener { result ->
            // Clear the list
            transactionList.clear()
            // Add the transactions to the list
            for (document in result) {
                val transaction = document.toObject(Transactions::class.java)
                transactionList.add(transaction)
            }
            // Notify the adapter that the data has changed
            transactionAdapter.notifyDataSetChanged()
        }
        .addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents.", exception)
        }
}

}





















//    private fun hideViews() = with(binding) {
//        dashboardData.visibility = View.GONE
//        emptyDataView.visibility = View.VISIBLE
//    }
//
//    private fun showViews() = with(binding) {
//        dashboardData.visibility = View.VISIBLE
//        emptyDataView.visibility = View.GONE
//    }
















