package com.conicskill.app.ui.pdf;

import static com.airbnb.lottie.L.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.chat.MessageListItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.Constants;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;


public class ChatFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @Nullable
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.recyclerViewChat)
    RecyclerView recyclerViewChat;
    @BindView(R.id.button_chat_send)
    ImageButton mSendButton;
    @BindView(R.id.editTextChat)
    EditText editTextChat;
    @BindView(R.id.buttonLiveUsers)
    MaterialButton buttonLiveUsers;
    private PdfViewModel mViewModel;
    private LinearLayoutManager mLayoutManager;
    private ChatAdapter mChatAdapter;
    private String lastSentMessage = "";
    private String lastMessageID = "0";
    private String chatId = null;
    private String chatEnabled = "";
    private Socket socket;

    {
        try {
            socket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean typingStarted = false;

    public static ChatFragment newInstance(String chatId, String chatEnabled) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable("chatId", chatId);
        args.putSerializable("chatEnabled", chatEnabled);
        chatFragment.setArguments(args);
        return chatFragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.chat_fragment;
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #()} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     *
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(PdfViewModel.class);
        Bundle bundle = getArguments();

        if(bundle != null) {
            if(bundle.getSerializable("chatId") != null) {
                chatId = bundle.getString("chatId");
                chatEnabled = bundle.getString("chatEnabled");
            }
        }

        if(chatEnabled != null && chatId != null) {

//            socket = ((BaseApplication) getActivity().getApplication()).getSocket();
            socket.on(Socket.EVENT_CONNECT, args -> socket.emit("add user", mViewModel.tinyDB.getString(Constants.NAME), chatId)).on("new message", args -> getActivity().runOnUiThread(() -> {
                JSONArray jsonArray = (JSONArray) args[0];
                for(int i=0; i< jsonArray.length(); i++) {
                    try {
                        JSONObject data = (JSONObject) jsonArray.get(i);
                        String username;
                        String message = "";
                        String messageAt = "";
                        String userId = "";
                        JSONObject jsonObjectData;
                        username = data.getString("username");
                        jsonObjectData = data.getJSONObject("message");
                        message = jsonObjectData.getString("message");
                        if (jsonObjectData.has("messageAt")) {
                            messageAt = jsonObjectData.getString("messageAt");
                        }
                        if(jsonObjectData.has("userId")) {
                            userId = jsonObjectData.getString("userId");
                        }
                        addMessage(username, message, messageAt, userId);
                    } catch (JSONException e) {
                        return;
                    }
                }
            })).on(Socket.EVENT_DISCONNECT, args -> Log.e(TAG, "call: Disconnect")).on("user joined", args -> getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    buttonLiveUsers.setText(data.getString("numUsers"));
                } catch (Exception e) {

                }
            })).on("login",args -> getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    buttonLiveUsers.setText(data.getString("numUsers"));
                } catch (Exception e) {

                }
            })).on("typing", args -> getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    buttonLiveUsers.setText(data.getString("numUsers"));
                } catch (Exception e) {

                }
            })).on("user left", args -> getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    buttonLiveUsers.setText(data.getString("numUsers"));
                } catch (Exception e) {

                }
            })).on("reconnect", args -> getActivity().runOnUiThread(() -> {
                socket.emit("add user", mViewModel.tinyDB.getString(Constants.NAME), chatId);
            }));
            socket.connect();
            mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
            recyclerViewChat.setLayoutManager(mLayoutManager);

            mChatAdapter = new ChatAdapter();
            recyclerViewChat.setAdapter(mChatAdapter);

            editTextChat.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1) {
                        //Log.i(TAG, “typing started event…”);
                        typingStarted = true;
//                        socket.emit("typing");
                        //send typing started status
                    } else if (s.toString().trim().length() == 0 && typingStarted) {
                        //Log.i(TAG, “typing stopped event…”);
                        typingStarted = false;
                        //send typing stopped status
                    }
                }
            });

            mSendButton.setOnClickListener(v -> {
                if (editTextChat.getText().toString().isEmpty()) {
                    editTextChat.setError("Enter some text");
                } else if (editTextChat.getText().length() > 255) {
                    editTextChat.setError("Enter less than 255 characters");
                } else {
                    try {
                        String username = mViewModel.tinyDB.getString(Constants.NAME);
                        String message = editTextChat.getText().toString();
//                        String timeStamp = new SimpleDateFormat("HH.mm", Locale.ENGLISH).format(new Date());
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(new Date());

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("username", username);
                        jsonObject.put("message", message);
                        jsonObject.put("userId", mViewModel.tinyDB.getString(Constants.CANDIDATE_ID));
                        jsonObject.put("messageAt", timeStamp);

                        if(socket.connected()) {
                            socket.emit("new message", jsonObject, (Ack) args -> {
                                getActivity().runOnUiThread(() -> {

                                });
                            });
//                            addMessage(username, message, timeStamp);
                            editTextChat.setText("");
//                                new Emitter().emit("new message", jsonObject);
                        } else {
                            socket.connect();
                        }
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            editTextChat.setText("Chat Not Enabled");
            editTextChat.setEnabled(false);
            mSendButton.setEnabled(false);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void addMessage(String username, String message, String messageAt, String userId) {
        MessageListItem messageListItem = new MessageListItem();
        messageListItem.setAuthor(username);
        messageListItem.setChatId(chatId);
        messageListItem.setText(message);
        messageListItem.setMessageAt(messageAt);
        messageListItem.setUserId(userId);
        mChatAdapter.appendMessage(messageListItem);
    }

    private class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_MESSAGE_SENT = 1;
        private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

        private ArrayList<MessageListItem> mMessageList;

        ChatAdapter() {
            mMessageList = new ArrayList<>();
            refresh();
        }

        // Retrieves 30 most recent messages.
        void refresh() {
            /*mChannel.getPreviousMessagesByTimestamp(Long.MAX_VALUE, true, 30, true,
                    BaseChannel.MessageTypeFilter.USER, null, new BaseChannel.GetMessagesHandler() {
                        @Override
                        public void onResult(List<BaseMessage> list, SendBirdException e) {
                            if (e != null) {
                                e.printStackTrace();
                                return;
                            }
                            mMessageList = (ArrayList<BaseMessage>) list;

                            notifyDataSetChanged();

                        }
                    });*/

        }

        void loadPreviousMessages() {
//            final long lastTimestamp = mMessageList.get(mMessageList.size() - 1).getCreatedAt();
            /*mChannel.getPreviousMessagesByTimestamp(lastTimestamp, false, 30, true,
                    BaseChannel.MessageTypeFilter.USER, null, new BaseChannel.GetMessagesHandler() {
                        @Override
                        public void onResult(List<BaseMessage> list, SendBirdException e) {
                            if (e != null) {
                                e.printStackTrace();
                                return;
                            }
                            mMessageList.addAll(list);

                            notifyDataSetChanged();
                        }
                    });*/
        }

        // Appends a new message to the beginning of the message list.
        void appendMessage(MessageListItem message) {
            mMessageList.add(0, message);
            notifyItemInserted(0);
            recyclerViewChat.smoothScrollToPosition(0);
        }

        void removeMessage() {
            mMessageList.remove(0);
            notifyDataSetChanged();
        }

        // Sends a new message, and appends the sent message to the beginning of the message list.
        /*void sendMessage(final String message) {
            mChannel.sendUserMessage(message, new BaseChannel.SendUserMessageHandler() {
                @Override
                public void onSent(UserMessage userMessage, SendBirdException e) {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }

                    mMessageList.add(0, userMessage);
                    notifyDataSetChanged();
                }
            });
        }*/

        // Determines the appropriate ViewType according to the sender of the message.
        @Override
        public int getItemViewType(int position) {
            MessageListItem message = mMessageList.get(position);

            if (message.getUserId().equalsIgnoreCase(mViewModel.tinyDB.getString(Constants.CANDIDATE_ID))) {
                // If the current user is the sender of the message
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                // If some other user sent the message
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }

        // Inflates the appropriate layout according to the ViewType.
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            if (viewType == VIEW_TYPE_MESSAGE_SENT) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                return new SentMessageHolder(view);
            } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                return new ReceivedMessageHolder(view);
            }

            return null;
        }

        // Passes the message object to a ViewHolder so that the contents can be bound to UI.
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MessageListItem message = mMessageList.get(position);

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_MESSAGE_SENT:
                    ((SentMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    ((ReceivedMessageHolder) holder).bind(message);
            }
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }

        // Messages sent by me do not display a profile image or nickname.
        private class SentMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText, timeText;

            SentMessageHolder(View itemView) {
                super(itemView);

                messageText = itemView.findViewById(R.id.text_message_body);
                timeText = itemView.findViewById(R.id.text_message_time);
            }

            void bind(MessageListItem message) {
                messageText.setText(message.getText());
                if(message.getMessageAt() != null) {
//                    timeText.setText(message.getMessageAt().substring(10,16));
                    timeText.setText(message.getMessageAt());
                }
//                timeText.setText();

                // Format the stored timestamp into a readable String using method.
//                timeText.setText(Utils.formatTime(message.getCreatedAt()));
            }
        }

        // Messages sent by others display a profile image and nickname.
        private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText, timeText, nameText;
            ImageView profileImage;

            ReceivedMessageHolder(View itemView) {
                super(itemView);

                messageText = itemView.findViewById(R.id.text_message_body);
                timeText = itemView.findViewById(R.id.text_message_time);
                nameText = itemView.findViewById(R.id.text_message_name);
                profileImage = itemView.findViewById(R.id.image_message_profile);
            }

            void bind(MessageListItem message) {
                messageText.setText(message.getText());
                if (message.getAuthor() != null) {
                    nameText.setText(message.getAuthor());
                }
                if(message.getMessageAt() != null) {
                    timeText.setText(message.getMessageAt());
                }
//                Utils.displayRoundImageFromUrl(ChatActivity.this,
//                        message.getSender().getProfileUrl(), profileImage);
//                timeText.setText(Utils.formatTime(message.getCreatedAt()));

            }
        }
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(socket != null && socket.connected()) {
            socket.disconnect();
        }
    }
}
