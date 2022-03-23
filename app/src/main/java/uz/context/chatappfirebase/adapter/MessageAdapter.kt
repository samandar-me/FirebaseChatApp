package uz.context.chatappfirebase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import uz.context.chatappfirebase.R
import uz.context.chatappfirebase.model.Message

class MessageAdapter(
    private val context: Context,
    private val messageList: ArrayList<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        private const val ITEM_RECEIVE = 1
        private const val ITEM_SENT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view = LayoutInflater.from(context).inflate(R.layout.receive_layout, parent, false)
            ReceiveVewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.sent_layout, parent, false)
            SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.textView.text = message.message

        } else {
            val viewHolder = holder as ReceiveVewHolder
            holder.textView.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid!! == currentMessage.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    inner class SentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    inner class ReceiveVewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }
}