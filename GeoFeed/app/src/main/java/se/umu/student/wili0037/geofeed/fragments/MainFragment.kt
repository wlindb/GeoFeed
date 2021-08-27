package se.umu.student.wili0037.geofeed.fragments

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import se.umu.student.wili0037.geofeed.MainViewModel
import se.umu.student.wili0037.geofeed.MainViewModelFactory
import se.umu.student.wili0037.geofeed.R
import se.umu.student.wili0037.geofeed.activities.adapters.RecyclerAdapter
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.repository.Repository

class MainFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val fab = view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        fab.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_mainFragment_to_createPostFragment)
        }

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

        viewModel.responsePosts.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                if (response.body() == null) return@observe
                val postsRecyclerView: RecyclerView = view.findViewById(R.id.rv_recyclerView) as RecyclerView
                postsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = RecyclerAdapter(
                        response.body()!!.posts,
                        onClickCallback = { post -> handleOnPostClicked(post, view) })
                }
            } else {
                Log.d("Response", response.code().toString())
            }
        })
        return view
    }

    private fun handleOnPostClicked(post: Post, view: View) {
        viewModel.setCurrentPost(post)
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_viewPostFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val uuid =
                    Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
                viewModel.getPostsByUUID(uuid)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_mainFragment_to_yourPostsFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}