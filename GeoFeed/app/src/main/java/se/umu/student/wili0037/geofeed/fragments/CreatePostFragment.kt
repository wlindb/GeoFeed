package se.umu.student.wili0037.geofeed.fragments

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import se.umu.student.wili0037.geofeed.MainViewModel
import se.umu.student.wili0037.geofeed.MainViewModelFactory
import se.umu.student.wili0037.geofeed.R
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.repository.Repository

class CreatePostFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_post, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        viewModel.responseNewPost.observe(viewLifecycleOwner, { response ->
            if(response.isSuccessful) {
                if (response.body() == null) return@observe
                viewModel.clearResponseNewPost()
                viewModel.getPosts()
                Navigation.findNavController(view).navigate(R.id.action_createPostFragment_to_mainFragment)
            } else {
                Toast.makeText(context, "Sorry, something went wrong while creating the post", Toast.LENGTH_SHORT).show()
            }
        })
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_post_action_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submit -> handleOnSubmit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleOnSubmit() {
        val text = view?.findViewById<TextInputEditText>(R.id.tiet_edittext)?.text.toString()
        val uuid = Secure.getString(context?.contentResolver, Secure.ANDROID_ID)
        if (text.isNotEmpty()) {
            viewModel.createNewPost(uuid, text)
            view?.findViewById<TextInputEditText>(R.id.tiet_edittext)?.hideKeyboard()
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}