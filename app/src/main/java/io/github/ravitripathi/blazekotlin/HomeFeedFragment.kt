package io.github.ravitripathi.blazekotlin

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList


class HomeFeedFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFeedFragment()
    }

    private lateinit var viewModel: HomeFeedViewModel
    private lateinit var recyclerView: RecyclerView
    internal var databaseReference = FirebaseDatabase.getInstance().getReference()
    private val photoItemList = ArrayList<PhotoModelDict>()
    private val TAG = "HomeFeed"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_feed_fragment, container, false)
        recyclerView = view.findViewById(R.id.photosView)
        recyclerView.layoutManager = GridLayoutManager(activity, 3)
        recyclerView.adapter = HomeFeedAdapter(photoItemList,context)
        setupListeners()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeFeedViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun setupListeners() {
        val UID = FirebaseAuth.getInstance().currentUser?.uid  ?: return
        val urlReference = databaseReference.child("users").child(UID)

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val photoUrl = dataSnapshot.getValue(String::class.java) ?: return
                val photo = PhotoModel(photoUrl)
                val key = dataSnapshot.key ?: return
                Log.d(TAG, "onChildAdded:" + key)
                val model = PhotoModelDict(key, photo)
                photoItemList.add(model)
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")
                val photoUrl = dataSnapshot.getValue(String::class.java) ?: return
                val photo = PhotoModel(photoUrl)
                val key = dataSnapshot.key ?: return

                photoItemList.forEach {
                    if (it.key == key) {
                        it.model = photo
                    }
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
                val key = dataSnapshot.key ?: return
                photoItemList.forEach {
                    if (it.key == key) {
                        photoItemList.remove(it)
                    }
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                //Do nothing
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                //Do nothing
            }
        }
        urlReference.addChildEventListener(childEventListener)

    }

}
