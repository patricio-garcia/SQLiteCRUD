package cl.untoque.sqlitecrud

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterRecord(): RecyclerView.Adapter<AdapterRecord.HolderRecord>() {

    private var context: Context? = null
    private var recordList: ArrayList<ModelRecord>? = null

    constructor(context: Context, recordList: ArrayList<ModelRecord>?) : this() {
        this.context = context
        this.recordList = recordList
    }

    class HolderRecord(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imgProfile: ImageView = itemView.findViewById(R.id.imgPersonList)
        var tvName: TextView = itemView.findViewById(R.id.tvNameList)
        var tvAge: TextView = itemView.findViewById(R.id.tvAgeList)
        var tvPhone: TextView = itemView.findViewById(R.id.tvPhoneList)
        var moreButton: ImageButton = itemView.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecord {
        return HolderRecord(LayoutInflater.from(context).inflate(R.layout.record_list, parent, false))
    }

    override fun onBindViewHolder(holder: HolderRecord, position: Int) {
        val modelRecord = recordList!!.get(position)
        val id = modelRecord.id
        val image = modelRecord.image
        val name = modelRecord.name
        val age = modelRecord.age
        val phone = modelRecord.phone
        val addedTimeStamp = modelRecord.addedTimeStamp
        val updatedTimeStamp = modelRecord.updatedTimeStamp

        if (image == "null") {
            holder.imgProfile.setImageResource(R.drawable.ic_person)
        }
        else {
            holder.imgProfile.setImageURI(Uri.parse(image))
        }

        holder.tvName.text = name
        holder.tvAge.text = age
        holder.tvPhone.text = phone

        holder.itemView.setOnClickListener {

        }

        holder.moreButton.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return recordList!!.size
    }
}