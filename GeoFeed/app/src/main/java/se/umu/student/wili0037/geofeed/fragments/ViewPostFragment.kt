package se.umu.student.wili0037.geofeed.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import se.umu.student.wili0037.geofeed.repository.Repository
import kotlin.random.Random

class ViewPostFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var commentInputEditText: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
            rv_recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CommentListRecyclerAdapter(post.comments)
            }
        })

        // https://material.io/components/text-fields/android#using-text-fields
        val newCommentLayout = view.findViewById<TextInputLayout>(R.id.til_textinputlayout)
        newCommentLayout.setEndIconOnClickListener { handleOnCommentSubmit() }

        return view
    }

    private fun handleOnCommentSubmit() {
        Toast.makeText(context, "${commentInputEditText.text}", Toast.LENGTH_SHORT).show()
        commentInputEditText.setText("")
        commentInputEditText.hideKeyboard()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun getMockComments(): List<Comment> {
        val str = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. "

        return List(40) { i -> Comment(i.toString(), str.substring(Random.nextInt(10, str.length-1)), "Timstamp $i") }
    }
}