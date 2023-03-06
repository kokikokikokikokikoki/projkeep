package com.example.projectkeep.fragments

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectkeep.R
import com.example.projectkeep.adapter.TransactionAdapter

import com.example.projectkeep.databinding.FragmentReportBinding
import com.example.projectkeep.model.Transactions
import com.example.projectkeep.utilities.Constants.pieChartColors
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.add_goal.*
import kotlinx.android.synthetic.main.content_add_transaction.*
import kotlinx.android.synthetic.main.content_add_transaction.view.*
import kotlinx.android.synthetic.main.fragment_add_transaction_layout.*
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.transaction_item.*
import java.text.DecimalFormat

import java.util.*


class ReportFragment : Fragment(R.layout.fragment_report), View.OnClickListener {
    //R.layout.fragment_add_transaction_layout
    private lateinit var binding: FragmentReportBinding
    private lateinit var pieChart: PieChart
    private lateinit var totalIncome: TextView
    private lateinit var expenseTab: Button
    private lateinit var incomeTab: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionList: ArrayList<Transactions>
    private lateinit var transactionAdapter: TransactionAdapter

    private lateinit var january: Button
    private lateinit var february: Button
    private lateinit var march: Button
    private lateinit var april: Button
    private lateinit var may: Button
    private lateinit var june: Button
    private lateinit var july: Button
    private lateinit var august: Button
    private lateinit var september: Button
    private lateinit var october: Button
    private lateinit var november: Button
    private lateinit var december: Button

    private lateinit var monthly: TextView

    private var monthInt = 1
    private var month = ""


    // make spinner for years
    private lateinit var spinner: Spinner

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)

    // make array for years
//val years = arrayOf(currentYear, currentYear - 1)
    val years = mutableListOf<String>()


    private var db = Firebase.firestore
    private lateinit var database: DatabaseReference


    private fun updatePieChartYear(selectedYear: String, monthInt: Int) {
        Log.d("updatePieChartYear", "Called with selectedYear=$selectedYear and monthInt=$monthInt")
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, selectedYear.toInt())
        cal.set(Calendar.MONTH, monthInt - 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val startOfMonth = cal.time
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DATE, -1)
        val endOfMonth = cal.time


        val categoryAmounts = mutableMapOf<String, Float>()




        db.collection("Transaction").whereEqualTo("transactionType", "Income")
            .whereGreaterThanOrEqualTo("date", startOfMonth)
            .whereLessThan("date", endOfMonth)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val categoryType = document.get("categoryType").toString()
                    val amount = document.get("amount").toString().toFloat()
                    if (categoryAmounts.containsKey(categoryType)) {
                        categoryAmounts[categoryType] = categoryAmounts[categoryType]!! + amount
                    } else {
                        categoryAmounts[categoryType] = amount
                    }
                }
                // The rest of the code to update the PieChart is the same as before
                val entries = mutableListOf<PieEntry>()
                for ((category, amount) in categoryAmounts) {
                    entries.add(PieEntry(amount, category))
                }
                val dataSet = PieDataSet(entries, "")
                addColorsToDataSet(dataSet)
                val data = PieData(dataSet)


                dataSet.sliceSpace = 1f
                // turn into percent
                val percentFormatter = PercentFormatter(pieChart)

                data.setValueFormatter(percentFormatter)
                data.setValueTextSize(12f)
                data.setValueFormatter(object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return if (value < 5f) "" else "${"%d".format(value.toInt())}%"
                    }
                })
                pieChart.data = data
                pieChart.invalidate()

                pieChart.setUsePercentValues(true)


//            pieChart.data = data
//            pieChart.invalidate()
                pieChart.setCenterText("Income")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

    }



    override fun onClick(v: View?) {
        when (v) {
            binding.January -> {
                setMonth(v, binding.January)
                monthInt = 1
                month = "January"
                // total income for month
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)


            }
            binding.February -> {
                setMonth(v, binding.February)
                monthInt = 2
                month = "February"

                // total income for month
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)


            }
            binding.March -> {
                setMonth(v, binding.March)
                monthInt = 3
                month = "March"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)

            }
            binding.April -> {
                setMonth(v, binding.April)
                monthInt = 4
                month = "April"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)
            }
            binding.May -> {
                setMonth(v, binding.May)
                monthInt = 5
                month = "May"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)
            }
            binding.June -> {
                setMonth(v, binding.June)
                monthInt = 6
                month = "June"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)
            }
            binding.July -> {
                setMonth(v, binding.July)
                monthInt = 7
                month = "July"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)

            }
            binding.August -> {
                setMonth(v, binding.August)
                monthInt = 8
                month = "August"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)
            }
            binding.September -> {
                setMonth(v, binding.September)
                monthInt = 9
                month = "September"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)
            }
            binding.October -> {
                setMonth(v, binding.October)
                monthInt = 10
                month = "October"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)
            }
            binding.November -> {
                setMonth(v, binding.November)
                monthInt = 11
                month = "November"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)
            }
            binding.December -> {
                setMonth(v, binding.December)
                monthInt = 12
                month = "December"
                val selectedYear = spinner.selectedItem.toString()
                totalIncomeForMonth(selectedYear, monthInt)
                updatePieChartYear(selectedYear, monthInt)
                updateRecyclerView(selectedYear.toInt(), monthInt)
            }
        }
    }

    private fun totalIncomeForMonth(selectedYear: String, monthInt: Int) {
        val cal = Calendar.getInstance()
        // cal.set(Calendar.YEAR, currentYear)
        cal.set(Calendar.YEAR, selectedYear.toInt())
        cal.set(Calendar.MONTH, monthInt - 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val startOfMonth = cal.time
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DATE, -1)
        val endOfMonth = cal.time




        db.collection("Transaction").whereEqualTo("transactionType", "Income")
            .whereGreaterThanOrEqualTo("date", startOfMonth).whereLessThan("date", endOfMonth)
            .get()
            .addOnSuccessListener { result ->
                var totalIncome = 0.0
                for (document in result) {
                    val amount = document.get("amount").toString().toDouble()
                    totalIncome += amount
                }

                val formatter = DecimalFormat("#,###.00")
                binding.budget.text = "฿" + formatter.format(totalIncome)

                //print out
                Log.d("Total income", totalIncome.toString())
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


    }


    private fun addColorsToDataSet(dataSet: PieDataSet) {
        val colors = mutableListOf<Int>()
        for (c in pieChartColors) {
            colors.add(ContextCompat.getColor(requireContext(), c))
        }
        dataSet.colors = colors
    }

    private fun pieChartConfig() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleColor(Color.WHITE)
        //text size
//        pieChart.setEntryLabelTextSize(24f)
//        pieChart.setEntryLabelColor(Color.CYAN)
        pieChart.setCenterTextSize(16f)
        pieChart.setCenterTextColor(R.color.fourthblue)
        pieChart.setHoleRadius(50f)
        pieChart.setTransparentCircleRadius(55f)
        pieChart.setDrawEntryLabels(false)
        pieChart.setDrawMarkers(false)
        pieChart.setDrawCenterText(true)
        pieChart.setDrawHoleEnabled(true)
        pieChart.setDrawSlicesUnderHole(true)

        pieChart.animateY(1400, Easing.EaseInOutQuad)


    }

    private fun setListener() {
        binding.January.setOnClickListener(this)
        binding.February.setOnClickListener(this)
        binding.March.setOnClickListener(this)
        binding.April.setOnClickListener(this)
        binding.May.setOnClickListener(this)
        binding.June.setOnClickListener(this)
        binding.July.setOnClickListener(this)
        binding.August.setOnClickListener(this)
        binding.September.setOnClickListener(this)
        binding.October.setOnClickListener(this)
        binding.November.setOnClickListener(this)
        binding.December.setOnClickListener(this)

    }


    private fun setMonth(v: View, button: MaterialButton) {
        month = button.text.toString()
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.firstblue))
        button.setStrokeColorResource(R.color.secondblue)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        when (v) {
            binding.January -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.February -> {
                removeBackground(binding.January)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.March -> {
                removeBackground(binding.February)
                removeBackground(binding.January)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.April -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.January)
                removeBackground(binding.May)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.May -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.January)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.June -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.January)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.July -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.January)
                removeBackground(binding.June)
                removeBackground(binding.January)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.August -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.January)
                removeBackground(binding.July)
                removeBackground(binding.January)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.September -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.January)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.January)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.October -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.January)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.January)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.November -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.January)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.January)
                removeBackground(binding.December)
            }
            binding.December -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.January)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.January)
            }
        }
    }

    private fun removeBackground(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
        button.setIconTintResource(R.color.textSecondary)
        button.setStrokeColorResource(R.color.textSecondary)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.textSecondary))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReportBinding.bind(view)
        // bind piechart
        pieChart = binding.pieChart
        // bind total income
        totalIncome = binding.totalIncome

        // bind expense tab
        expenseTab = binding.expensetab
        // bind income tab
        incomeTab = binding.all

        setListener()








        spinner = binding.yearSpinner
        for (year in currentYear downTo currentYear - 2) {
            years.add(year.toString())
        }

        recyclerView = binding.transactionRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        transactionList = arrayListOf()
        transactionAdapter = TransactionAdapter(transactionList)
        recyclerView.adapter = transactionAdapter



        pieChartConfig()
        //updatePieChart()


        getRecyclerView()



        all.setOnClickListener {
            val fragment = ReportFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, fragment)
            transaction.commit()

        }
        expenseTab.setOnClickListener {
            val fragment = ExpenseFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, fragment)
            transaction.commit()
        }

        //click listener for piechart
        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {

                pieChart.data.dataSet.valueTextSize = 20f

                pieChart.data.dataSet.valueTextColor = R.color.firstblue
                pieChart.setCenterTextSize(16f)


            }

            override fun onNothingSelected() {
                pieChart.setCenterTextSize(14f)
                pieChart.data.dataSet.valueTextSize = 12f
                pieChart.data.dataSet.valueTextColor = R.color.black

            }
        })


        // make adapter for spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // set listener for spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedYear = parent?.getItemAtPosition(position).toString()
                updatePieChartYear(selectedYear, monthInt)
                Toast.makeText(requireContext(), selectedYear, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }
        // show total income for the whole year
        showTotalIncome()


    }


    private fun getRecyclerView() {

        //val db = FirebaseFirestore.getInstance()
        db.collection("Transaction").whereEqualTo("transactionType", "Income")
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {

                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val transaction = data.toObject(Transactions::class.java)
                        transactionList.add(transaction!!)

                    }
                }
                transactionAdapter = TransactionAdapter(transactionList)
                recyclerView.adapter = transactionAdapter


            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


    }

    private fun updateRecyclerView(selectedYear: Int, monthInt: Int) {
        val cal = Calendar.getInstance()
        // cal.set(Calendar.YEAR, currentYear)
        cal.set(Calendar.YEAR, selectedYear.toInt())
        cal.set(Calendar.MONTH, monthInt - 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val startOfMonth = cal.time
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DATE, -1)
        val endOfMonth = cal.time
        // whereGreaterThanOrEqualTo("date", startOfMonth).whereLessThan("date", endOfMonth)
        // Filter the transactions based on the selected year and month
        db.collection("Transaction")
            .whereEqualTo("transactionType", "Income")
            .whereGreaterThanOrEqualTo("date", startOfMonth)
            .whereLessThan("date", endOfMonth)
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    // Clear the current data in the transactionList
                    transactionList.clear()

                    for (data in it.documents) {
                        val transaction = data.toObject(Transactions::class.java)
                        transactionList.add(transaction!!)
                    }
                }
                else {
                    transactionList.clear()
                }


                transactionAdapter.notifyDataSetChanged()

            }
    }


    private fun showTotalIncome() {
        db.collection("Transaction").whereEqualTo("transactionType", "Income")
            .get()
            .addOnSuccessListener { result ->
                var total = 0f
                for (document in result) {
                    val amount = document.get("amount").toString().toFloat()
                    total += amount
                }
                // currency in baht


                // show commas
                val formatter = DecimalFormat("#,###.00")
                totalIncome.text = "฿${formatter.format(total)}"


            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }


}















































