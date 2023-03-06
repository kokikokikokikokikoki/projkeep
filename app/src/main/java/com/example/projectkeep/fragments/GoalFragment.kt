package com.example.projectkeep.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectkeep.R
import com.example.projectkeep.adapter.GoalAdapter
import com.example.projectkeep.databinding.GoalDashboardBinding
import com.example.projectkeep.databinding.GoalFragmentBinding
import com.example.projectkeep.model.GoalModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.layout_goal_card.*


class GoalFragment : Fragment(R.layout.goal_dashboard) {
    //bindings
    private lateinit var binding: GoalDashboardBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var goalAdapter: GoalAdapter
    private lateinit var GoalList: ArrayList<GoalModel>

    // binding the goals
    private lateinit var totalGoal: TextView
    private lateinit var goalsCompleted: TextView
    private lateinit var goalsInProgress: TextView

    private var db = Firebase.firestore
    val database = FirebaseDatabase.getInstance()


    private fun getGoalData() {
        totalGoal = binding.totalGoal.totalGoals
        goalsCompleted = binding.itemIncomeCardView.totalCompleted
        goalsInProgress = binding.itemExpenseCardView.totalInprogress
        var completedGoals = 0
        var inProgressGoals = 0
        var totalGoals = 0


        db.collection("Goals").get().addOnSuccessListener { result ->
            for (document in result) {
                val goal = document.toObject(GoalModel::class.java)

                val progress = (goal.initialAmount!!.toFloat() / goal.goalAmount!!.toFloat()) * 100
                goal.progress = progress.toLong()

                totalGoals++

                if (goal.remainingAmount == 0L) {
                    goal.goalStatus = "Completed"
                    completedGoals++
                } else {
                    goal.goalStatus = "InProgress"
                    inProgressGoals++
                }



                GoalList.add(goal)


            }
            goalAdapter.notifyDataSetChanged()

            totalGoal.text = totalGoals.toString()
            goalsCompleted.text = completedGoals.toString()
            goalsInProgress.text = inProgressGoals.toString()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = GoalDashboardBinding.bind(view)

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        GoalList = arrayListOf()
        goalAdapter = GoalAdapter(GoalList)

        recyclerView.adapter = goalAdapter

        goalAdapter.attachToRecyclerView(recyclerView)






        goalAdapter.setOnItemClickListener(object : GoalAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                if (GoalList[position].goalStatus == "Completed") {
                    Toast.makeText(
                        context,
                        "Goal Completed, and cannot be edited",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val goalCard = EditGoalFragment()
                    val bundle = Bundle()
                    bundle.putString("id", GoalList[position].id)


                    goalCard.arguments = bundle


                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, goalCard)
                    transaction.addToBackStack(null)
                    transaction.commit()

                    Toast.makeText(context, "Goal card", Toast.LENGTH_SHORT).show()
                }


            }

        })

        getGoalData()


        // when click on add button, navigate to add goal fragment
        binding.addGoal.setOnClickListener() {
            val addGoalBtn = AddGoalFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, AddGoalFragment())
            transaction.addToBackStack(null)
            transaction.commit()

            Toast.makeText(context, "Add goal", Toast.LENGTH_SHORT).show()

        }


    }


}