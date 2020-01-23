package com.quinox.mobile


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.viewModels.LessonDetailVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lesson_detail.*
import java.util.regex.Pattern


@RequiresActivityViewModel(LessonDetailVM.ViewModel::class)
class LessonDetailActivity : BaseActivity<LessonDetailVM.ViewModel>() {
    private val compositeDisposable = CompositeDisposable()
    lateinit var adapterImg: ImageAdapter
    lateinit var imgManager: LinearLayoutManager
    lateinit var imgAlbum: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_detail)
        supportActionBar?.title = "Clase"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        imgAlbum = findViewById(R.id.recyclerViewClassDetail)
        imgManager = LinearLayoutManager(this)
        imgManager.orientation = LinearLayoutManager.HORIZONTAL
        imgManager.reverseLayout = true
        imgAlbum.layoutManager = imgManager
        adapterImg = ImageAdapter(this)
        imgAlbum.adapter = adapterImg
        youtubePlayer.visibility = View.GONE

        /*youtubePlayer.getPlayerUiController().showFullscreenButton(true)
        youtubePlayer.getPlayerUiController().setFullScreenButtonClickListener(View.OnClickListener {
            if(youtubePlayer.isFullScreen())
            {
                youtubePlayer.exitFullScreen()
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                if(supportActionBar != null){
                    supportActionBar!!.show()
                }
            }
            else {
                youtubePlayer.enterFullScreen()
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                if(supportActionBar != null){
                    supportActionBar!!.hide()
                }
            }

        })*/

        compositeDisposable.add(viewModel.output.loadInfo().observeOn(AndroidSchedulers.mainThread()).subscribe {
            txt_titleClassDetail.text = it.title
            webViewClassDetail.loadData(it.descripcion,"text/html","UTF-8")
            adapterImg.setAlbum(it.imagenes)
            val video = it.urlVideo
            if(video != null)
            {
                val videoId = extractYTId(video)
                youtubePlayer.addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
                    override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                        if(videoId != null){
                            youtubePlayer.visibility = View.VISIBLE
                            youTubePlayer.cueVideo(videoId,0f)
                        }
                        else
                        {
                            youtubePlayer.release()

                        }
                    }
                })
            }
        })
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
        youtubePlayer.release()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun extractYTId(ytUrl: String): String? {
        var vId: String? = null
        val pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = pattern.matcher(ytUrl)
        if (matcher.matches()) {
            vId = matcher.group(1)
        }
        return vId
    }
}
