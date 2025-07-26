package com.submission.appriori.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultModel(
    var resultId: String = "",
    var created_at: com.google.firebase.Timestamp? = null,
    var from: com.google.firebase.Timestamp?=null,
    var end: com.google.firebase.Timestamp?=null,
    var min_support: Double?=null,
    var conf: Double?=null,
    val itemset1: List<Itemset1> = emptyList(),
    val itemset2: List<Itemset2> = emptyList(),
    val itemset3: List<Itemset3> = emptyList(),
    val association_rules: List<AssociationRule> = emptyList()
) : Parcelable

@Parcelize
data class Itemset1(
    val item: String ?= "",
    val keterangan: String ?= "",
    val support: Double ?= 0.0,
    val totalQuantity: Double ?= 0.0
) : Parcelable

@Parcelize
data class Itemset2(
    val itemsets1: String = "",
    val itemsets2: String = "",
    val keterangan: String = "",
    val support: Double = 0.0,
    val count: Double = 0.0
) : Parcelable

@Parcelize
data class Itemset3(
    val itemsets1: String = "",
    val itemsets2: String = "",
    val itemsets3: String = "",
    val keterangan: String = "",
    val support: Double = 0.0,
    val count: Double = 0.0
) : Parcelable

@Parcelize
data class AssociationRule(
    val antecedents: List<String> = emptyList(),
    val consequents: List<String> = emptyList(),
    val confidence: Double = 0.0,
    val lift: Double = 0.0,
    val support: Double = 0.0
) : Parcelable