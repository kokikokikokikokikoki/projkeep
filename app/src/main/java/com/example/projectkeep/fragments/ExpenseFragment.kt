package com.example.projectkeep.fragments

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.projectkeep.R
import com.example.projectkeep.databinding.FragmentExpenseBinding
import com.example.projectkeep.databinding.FragmentReportBinding
import com.example.projectkeep.utilities.Constants
import com.example.projectkeep.utilities.ConstantsExpense
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_expense.*
import kotlinx.android.synthetic.main.fragment_report.*
import java.text.DecimalFormat
import java.util.*


class ExpenseFragment : Fragment(R.layout.fragment_expense) {
private lateinit var binding : FragmentExpenseBinding
private lateinit var  pieChart : PieChart
private lateinit var tabIncome : Button
private lateinit var tabExpense : Button
private lateinit var totalExpense : TextView






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    // make spinner for years
    private lateinit var spinner: Spinner

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)

    // make array for years
//val years = arrayOf(currentYear, currentYear - 1)
    val years = mutableListOf<String>()


//private val years = arrayOf("2022", "2023", "2024", "2025", "2026")

    private var db = Firebase.firestore
    private lateinit var database: DatabaseReference






    private fun updatePieChart() {
        val categoryAmounts = mutableMapOf<String, Float>()

        db.collection("Transaction").whereEqualTo("transactionType", "Expense")
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
                pieChart.data = data
                pieChart.invalidate()

                pieChart.setUsePercentValues(true)



//            pieChart.data = data
//            pieChart.invalidate()
                pieChart.setCenterText("Expense")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
    // add color to pie chart slices, depending on category
    private fun addColorsToDataSet(dataSet: PieDataSet) {
        val colors = mutableListOf<Int>()
        for (c in ConstantsExpense.pieChartColorsExpense) {
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

        // show all legend
        val legend = pieChart.legend
        legend.maxSizePercent = 1f
        legend.isWordWrapEnabled = true



        pieChart.animateY(1400, Easing.EaseInOutQuad)





    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExpenseBinding.bind(view)
        // bind piechart
        pieChart = binding.pieChartExpense
        // bind total income
        totalExpense = binding.totalExpense

        // bind expense tab
        tabExpense = binding.expenseTab
        // bind income tab
        tabIncome = binding.incomeTab

        spinner = binding.yearSpinnerExpense
        for (year in currentYear downTo currentYear - 2) {
            years.add(year.toString())
        }
        pieChartConfig()
        updatePieChart()


        incomeTab.setOnClickListener {
            val fragment = ReportFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, fragment)
            transaction.commit()

        }
        expenseTab.setOnClickListener{
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
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedYear = parent?.getItemAtPosition(position).toString()
                Toast.makeText(requireContext(), selectedYear, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }
        // show total income for the whole year
        showTotalIncome()







    }

    private fun showTotalIncome() {
        db.collection("Transaction").whereEqualTo("transactionType", "Expense")
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
                totalExpense.text = "฿${formatter.format(total)}"


            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }





}