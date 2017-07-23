package blue.aodev.animeultimetv.presentation.common

import android.content.Context
import android.support.v17.leanback.widget.ImageCardView
import blue.aodev.animeultimetv.R
import blue.aodev.animeultimetv.domain.model.Episode
import blue.aodev.animeultimetv.extensions.formatEpisodeDuration
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class EpisodeCardPresenter : BaseCardPresenter() {

    override fun initResources(context: Context) {
        super.initResources(context)

        val res = context.resources
        cardImageWidth = res.getDimensionPixelSize(R.dimen.episode_card_width)
        cardImageHeight = res.getDimensionPixelSize(R.dimen.episode_card_height)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val context = viewHolder.view.context
        val episode = item as Episode
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = context.getString(R.string.episode_title, episode.number)
        cardView.contentText = context.formatEpisodeDuration(episode.duration)

        Glide.with(context)
                .load(episode.imageUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(cardView.mainImageView)
    }
}