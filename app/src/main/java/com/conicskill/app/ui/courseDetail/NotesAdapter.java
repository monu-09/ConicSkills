package com.conicskill.app.ui.courseDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.conicskill.app.R;
import com.conicskill.app.data.model.notes.PdfListItem;
import com.conicskill.app.ui.courseDetail.interfaces.NotesClickListner;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private ArrayList<PdfListItem>  pdfListItems= new ArrayList<>();
    private Context context;
    private NotesClickListner listner;

    private   String getName(String url) {
        Log.e("TAG", "getName: " + url );
        int slashIndex = url.lastIndexOf('/');
        int dotIndex = url.lastIndexOf('.');
        String filenameWithoutExtension;
        if (dotIndex == -1) {
            filenameWithoutExtension = url.substring(slashIndex + 1);
        } else {
            filenameWithoutExtension = url.substring(slashIndex + 1, dotIndex);
        }
        return filenameWithoutExtension;
    }

    public NotesAdapter(ArrayList<PdfListItem> pdfListItems, Context context, NotesClickListner listner) {
        this.pdfListItems = pdfListItems;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_pdf_list,parent,false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.imageViewFolder.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24);
        holder.textViewName.setText( getName(pdfListItems.get(position).getPdfUrl()));
        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onNotesItemClicked(pdfListItems.get(position));
            }
        });
        holder.buttonDelete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return pdfListItems.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewName)
        AppCompatTextView textViewName;

        @BindView(R.id.buttonView)
        MaterialButton buttonView;

        @BindView(R.id.buttonDelete)
        MaterialButton buttonDelete;

        @BindView(R.id.imageViewFolder)
        AppCompatImageView imageViewFolder;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
