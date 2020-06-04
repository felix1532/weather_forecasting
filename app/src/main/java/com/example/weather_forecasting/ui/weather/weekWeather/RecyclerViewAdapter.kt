package com.example.weather_forecasting.ui.weather.weekWeather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forecasting.R
import com.example.weather_forecasting.model.modelWeekWeather.General
import kotlin.collections.ArrayList

class RecyclerViewAdapter(private val list: ArrayList<General?>, private val nameDays: MutableMap<Int, String>): RecyclerView.Adapter<RecyclerViewAdapter.Companion.RecyclerViewHolder>() {

    companion object{
        const val TYPE_HEAD : Int = 0
        const val TYPE_LIST : Int = 1

        class RecyclerViewHolder : RecyclerView.ViewHolder
        {
            var ViewType : Int = 0
            lateinit var mDateView: TextView
            lateinit var mImageView: ImageView
            lateinit var mTemperatureView: TextView
            lateinit var mDescriptionView: TextView
            lateinit  var mRelativeLayout: RelativeLayout

            lateinit var mTextViewSection: TextView
            lateinit  var mRelativeLayoutSection: RelativeLayout

            constructor(rv: View, viewType: Int) : super(rv){
                if(viewType == TYPE_LIST){
                    mDateView = rv.findViewById(R.id.time_text_recycler_view)  as TextView
                    mImageView = rv.findViewById(R.id.image_recycler_view) as ImageView
                    mTemperatureView = rv.findViewById(R.id.temperature_recycler_view) as TextView
                    mDescriptionView = rv.findViewById(R.id.description_text_recycler_view) as TextView
                    mRelativeLayout = rv.findViewById(R.id.relativeLayout) as RelativeLayout
                    ViewType = 1
                }
                else if(viewType == TYPE_HEAD){
                    mTextViewSection = rv.findViewById(R.id.text_recycler_view_sections) as TextView
                    mRelativeLayoutSection = rv.findViewById(R.id.relativeLayout_sections) as RelativeLayout
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val rv:View
        val holder: RecyclerViewHolder
        if(viewType == TYPE_HEAD)
        {
            rv = LayoutInflater.from(parent.context).inflate(R.layout.item_week_weather_fragment_sections,parent,false)
            holder =
                RecyclerViewHolder(
                    rv,
                    viewType
                )
            return holder
        }
            rv = LayoutInflater.from(parent.context).inflate(R.layout.item_week_weather_fragment,parent,false)
            holder =
                RecyclerViewHolder(
                    rv,
                    viewType
                )
            return holder

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0)
        {
            return TYPE_HEAD
        }
        if(position>0){
            if(list[position-1]?.timeHoursMinutes == list[position]?.timeHoursMinutes ){
                return TYPE_HEAD
            }
                return TYPE_LIST
        }else
        {
            return TYPE_LIST
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int ) {
        if(holder.ViewType == TYPE_LIST)
        {
            holder.mRelativeLayout?.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,R.anim.fade_scale_animation))
            holder.mImageView?.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context ,R.anim.fade_transition_animation))
            holder.mDateView?.text = list[position]?.timeHoursMinutes
            holder.mImageView?.setImageResource(list[position]?.weather?.get(0)?.id_drawable_icon!!)
            holder.mTemperatureView?.text = list[position]?.main?.temp?.toInt().toString() + " ℃"
            holder.mDescriptionView?.text = list[position]?.weather?.get(0)?.description
        }
        if(holder.ViewType == TYPE_HEAD){
            holder.mRelativeLayoutSection?.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context ,R.anim.fade_transition_animation))
            holder.mTextViewSection?.setText(nameDays.getValue(position))
        }
    }

}