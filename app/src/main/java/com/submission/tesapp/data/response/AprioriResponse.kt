package com.submission.tesapp.data.response

import com.google.gson.annotations.SerializedName

data class AprioriResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Status? = null
)

data class Status(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AprioriItem(

	@field:SerializedName("jaccard")
	val jaccard: Any? = null,

	@field:SerializedName("consequents")
	val consequents: List<String?>? = null,

	@field:SerializedName("leverage")
	val leverage: Any? = null,

	@field:SerializedName("confidence")
	val confidence: Any? = null,

	@field:SerializedName("certainty")
	val certainty: Any? = null,

	@field:SerializedName("conviction")
	val conviction: Any? = null,

	@field:SerializedName("antecedents")
	val antecedents: List<String?>? = null,

	@field:SerializedName("kulczynski")
	val kulczynski: Any? = null,

	@field:SerializedName("zhangs_metric")
	val zhangsMetric: Any? = null,

	@field:SerializedName("representativity")
	val representativity: Any? = null,

	@field:SerializedName("consequent support")
	val consequentSupport: Any? = null,

	@field:SerializedName("lift")
	val lift: Any? = null,

	@field:SerializedName("antecedent support")
	val antecedentSupport: Any? = null,

	@field:SerializedName("support")
	val support: Any? = null
)

data class Data(

	@field:SerializedName("apriori")
	val apriori: List<AprioriItem?>? = null
)
