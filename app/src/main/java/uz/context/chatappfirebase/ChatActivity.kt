package uz.context.chatappfirebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.context.chatappfirebase.adapter.MessageAdapter
import uz.context.chatappfirebase.databinding.ActivityChatBinding
import uz.context.chatappfirebase.model.Message
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    private lateinit var messageAdapter: MessageAdapter
    private val messageList: ArrayList<Message> = ArrayList()
    private var senderRoom: String? = null
    private var receiveRoom: String? = null
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        mDbRef = FirebaseDatabase.getInstance().reference
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        supportActionBar?.title = name

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiverUid + senderUid
        receiveRoom = senderUid + receiverUid
        messageAdapter = MessageAdapter(this, messageList)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnap in snapshot.children) {
                        val message = postSnap.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        binding.editMessBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = p0.toString()
                if (text.isNotEmpty()) {
                    binding.btnSend.isVisible = true
                    binding.btnSend.setOnClickListener {
                        val messageObj = Message(text, senderUid)
                        mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                            .setValue(messageObj).addOnSuccessListener {
                                mDbRef.child("chats").child(receiveRoom!!).child("messages").push()
                                    .setValue(messageObj)
                            }
                        binding.editMessBox.setText("")
                    }
                } else {
                    binding.btnSend.isVisible = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }
}