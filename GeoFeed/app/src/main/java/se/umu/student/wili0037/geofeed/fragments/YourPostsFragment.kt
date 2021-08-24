package se.umu.student.wili0037.geofeed.fragments
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.student.wili0037.geofeed.MainViewModel
import se.umu.student.wili0037.geofeed.MainViewModelFactory
import se.umu.student.wili0037.geofeed.R
import se.umu.student.wili0037.geofeed.activities.adapters.RecyclerAdapter
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.repository.Repository


class YourPostsFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_your_posts, container, false)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

        viewModel.responseUserPosts.observe(viewLifecycleOwner, { response ->
            if(response.isSuccessful) {
                if (response.body() == null) return@observe
                val rv_recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.rv_recyclerView) as RecyclerView
                rv_recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = RecyclerAdapter(response.body()!!.posts, onClickCallback = {post -> handleOnPostClicked(post, view)})
                }
            } else {
                Log.d("Response", response.code().toString())
            }
        })
        return view
    }

    private fun handleOnPostClicked(post: Post, view: View) {
        viewModel.setCurrentPost(post)
        Navigation.findNavController(view).navigate(R.id.action_yourPostsFragment_to_viewPostFragment)
    }
}