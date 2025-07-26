package com.submission.appriori.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.submission.appriori.data.model.TransactionModel
import kotlinx.coroutines.tasks.await

class TransactionsPagingSource(private val db: FirebaseFirestore,
                               private val pageSize: Long
) : PagingSource<DocumentSnapshot, TransactionModel>() {
    private var lastDocumentSnapshot: DocumentSnapshot? = null

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, TransactionModel>): DocumentSnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, TransactionModel> {
        return try {
            var query = db.collection("users").document("admin").collection("transactions")
                .orderBy("date", Query.Direction.ASCENDING)
                .limit(pageSize)

            // handle next page
            if (lastDocumentSnapshot != null) {
                query = query.startAfter(lastDocumentSnapshot!!)
            }

            val snapshot = query.get().await()

            // update last visible document for next pagination
            val documents = snapshot.documents
            if (documents.isNotEmpty()) {
                lastDocumentSnapshot = documents.last()
            }

            val transactionList = documents.map { doc ->
                TransactionModel(
                    id = doc.id,
                    item = doc.getString("item") ?: "",
                    date = doc.getTimestamp("date")?.toDate() ?: Timestamp.now().toDate(),

                )
            }
            val nextKey = if (documents.isNotEmpty()) documents.last() else null
            LoadResult.Page(
                data = transactionList,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}