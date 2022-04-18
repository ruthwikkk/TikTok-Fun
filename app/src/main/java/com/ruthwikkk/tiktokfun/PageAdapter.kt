package com.ruthwikkk.tiktokfun

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ruthwikkk.tiktokfun.databinding.PageItemBinding
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

class PageAdapter: ListAdapter<PlaybackItem, PageViewHolder>(VideoDataDiffCallback) {

    private var recyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return LayoutInflater.from(parent.context)
            .let { inflater -> PageItemBinding.inflate(inflater, parent, false) }
            .let { binding ->
                PageViewHolder(binding)
            }
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        getItem(position).let(holder::bind)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    suspend fun attachPlayerView(appPlayerView: ExoAppPlayerView, position: Int) {
        awaitViewHolder(position).attach(appPlayerView)
    }

    // Hides the video preview image when the player is ready to be shown.
    suspend fun showPlayerFor(position: Int) {
        awaitViewHolder(position).hidePreviewImage()
    }

    private suspend fun awaitViewHolder(position: Int): PageViewHolder {
        if (currentList.isEmpty()) error("Tried to get ViewHolder at position $position, but the list was empty")

        var viewHolder: PageViewHolder?

        do {
            viewHolder = recyclerView?.findViewHolderForAdapterPosition(position) as? PageViewHolder
        } while (currentCoroutineContext().isActive && viewHolder == null && recyclerView?.awaitNextLayout() == Unit)

        return requireNotNull(viewHolder)
    }
}

private object VideoDataDiffCallback : DiffUtil.ItemCallback<PlaybackItem>() {
    override fun areItemsTheSame(oldItem: PlaybackItem, newItem: PlaybackItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlaybackItem, newItem: PlaybackItem): Boolean {
        return oldItem == newItem
    }
}