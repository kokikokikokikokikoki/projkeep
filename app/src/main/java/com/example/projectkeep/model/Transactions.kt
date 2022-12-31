package com.example.projectkeep.model

import com.google.firebase.Timestamp

data class Transactions(var categoryType: String? = null,
                        var date: Timestamp? = null,
                        var id: String? = null,
                        var title: String? = null,
                        var transactionType: String? = null,
                        var amount: Long? = null)





