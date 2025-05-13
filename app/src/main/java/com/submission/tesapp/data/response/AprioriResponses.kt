package com.submission.tesapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AprioriResponses(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Status? = null
) : Parcelable

@Parcelize
data class AssociationRulesItem(

	@field:SerializedName("consequents")
	val consequents: List<String?>? = null,

	@field:SerializedName("confidence")
	val confidence: Double? = null,

	@field:SerializedName("lift")
	val lift: Double? = null,

	@field:SerializedName("support")
	val support: Double? = null,

	@field:SerializedName("antecedents")
	val antecedents: List<String?>? = null
) : Parcelable

@Parcelize
data class Itemset2Item(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("itemsets")
	val itemsets: List<String?>? = null,

	@field:SerializedName("itemsets2")
	val itemsets2: String? = null,

	@field:SerializedName("support")
	val support: Double? = null,

	@field:SerializedName("itemsets1")
	val itemsets1: String? = null
) : Parcelable

@Parcelize
data class Status(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class Itemset3LolosItem(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("itemsets3")
	val itemsets3: String? = null,

	@field:SerializedName("itemsets2")
	val itemsets2: String? = null,

	@field:SerializedName("support")
	val support: Double? = null,

	@field:SerializedName("itemsets1")
	val itemsets1: String? = null
) : Parcelable

@Parcelize
data class Itemset2LolosItem(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("itemsets")
	val itemsets: List<String?>? = null,

	@field:SerializedName("itemsets2")
	val itemsets2: String? = null,

	@field:SerializedName("support")
	val support: Double? = null,

	@field:SerializedName("itemsets1")
	val itemsets1: String? = null
) : Parcelable

@Parcelize
data class Itemset1LolosItem(

	@field:SerializedName("item")
	val item: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("total_quantity")
	val totalQuantity: Double? = null,

	@field:SerializedName("support")
	val support: Double? = null
) : Parcelable

@Parcelize
data class Itemset3Item(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("itemsets3")
	val itemsets3: String? = null,

	@field:SerializedName("itemsets2")
	val itemsets2: String? = null,

	@field:SerializedName("support")
	val support: Double? = null,

	@field:SerializedName("itemsets1")
	val itemsets1: String? = null
) : Parcelable

@Parcelize
data class Itemset1Item(

	@field:SerializedName("item")
	val item: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("total_quantity")
	val totalQuantity: Double? = null,

	@field:SerializedName("support")
	val support: Double? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("itemset3_lolos")
	val itemset3Lolos: List<Itemset3LolosItem?>? = null,

	@field:SerializedName("itemset1_lolos")
	val itemset1Lolos: List<Itemset1LolosItem?>? = null,

	@field:SerializedName("association_rules")
	val associationRules: List<AssociationRulesItem?>? = null,

	@field:SerializedName("itemset3")
	val itemset3: List<Itemset3Item?>? = null,

	@field:SerializedName("itemset2_lolos")
	val itemset2Lolos: List<Itemset2LolosItem?>? = null,

	@field:SerializedName("itemset2")
	val itemset2: List<Itemset2Item?>? = null,

	@field:SerializedName("itemset1")
	val itemset1: List<Itemset1Item?>? = null
) : Parcelable
