package com.example.projectkeep.fragments

import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.projectkeep.R
import com.example.projectkeep.adapter.TransactionAdapter
import com.example.projectkeep.databinding.FragmentTransactionDetailBinding
import com.example.projectkeep.fragments.TransactionDetailFragment
import com.example.projectkeep.model.Transactions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_transaction_detail.*

class TransactionDetailFragment : Fragment() {
    private lateinit var btnAddTransaction: FloatingActionButton
    private lateinit var binding : FragmentTransactionDetailBinding
    private lateinit var transactionList: ArrayList<Transactions>
    private lateinit var transactionAdapter: TransactionAdapter

    private var db = Firebase.firestore
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTransactionDetailBinding.bind(view)



        edit_transaction.setOnClickListener {
            val fragment = AddExpense()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }







    }



