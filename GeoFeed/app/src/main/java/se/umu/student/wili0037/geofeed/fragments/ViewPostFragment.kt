package se.umu.student.wili0037.geofeed.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import se.umu.student.wili0037.geofeed.MainViewModel
import se.umu.student.wili0037.geofeed.MainViewModelFactory
import se.umu.student.wili0037.geofeed.R
import se.umu.student.wili0037.geofeed.activities.adapters.CommentListRecyclerAdapter
import se.umu.student.wili0037.geofeed.activities.adapters.RecyclerAdapter
import se.umu.student.wili0037.geofeed.model.Comment
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.repository.Repository
import kotlin.random.Random

class ViewPostFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var commentInputEditText: TextInputEditText
    private lateinit var currentPost: Post


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_post, container, false)
        commentInputEditText = view.findViewById(R.id.edit_comment)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

        val postTopic = view.findViewById<TextView>(R.id.tv_post)
        val nrComments = view.findViewById<TextView>(R.id.tv_nrComments)
        val rv_recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.rv_comments) as RecyclerView
        viewModel.currentPost.observe(viewLifecycleOwner, { post ->
            postTopic.text = post.body
            nrComments.text = post.comments.size.toString()
            currentPost = post
            rv_recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CommentListRecyclerAdapter(post.comments)
            }
            viewModel.getPosts()
        })

        // https://material.io/components/text-fields/android#using-text-fields
        val newCommentLayout = view.findViewById<TextInputLayout>(R.id.til_textinputlayout)
        newCommentLayout.setEndIconOnClickListener { handleOnCommentSubmit() }

        return view
    }

    private fun handleOnCommentSubmit() {
        if(commentInputEditText.text.isNullOrBlank() || currentPost._id == null) return
        val uuid = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
        val commentText : String = commentInputEditText.text.toString()
        viewModel.postComment(currentPost._id.toString(), uuid, commentText)
        commentInputEditText.setText("")
        commentInputEditText.hideKeyboard()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}