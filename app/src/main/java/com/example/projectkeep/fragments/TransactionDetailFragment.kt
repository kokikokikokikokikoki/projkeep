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
import com.example.projectkeep.fragments.TransactionDetailFragment

class TransactionDetailFragment : Fragment() {
    private lateinit var btnAddTransaction: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
           val view = inflater.inflate(R.layout.fragment_add_transaction_layout, container, false)
        return view
            }




    }



