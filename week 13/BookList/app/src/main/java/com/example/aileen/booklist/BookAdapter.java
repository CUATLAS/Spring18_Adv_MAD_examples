package com.example.aileen.booklist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class BookAdapter extends RealmBaseAdapter<Book> implements ListAdapter{

    private BookListActivity activity;

    private static class ViewHolder {
        TextView bookName;
        TextView authorName;
        CheckBox hasRead;
    }

    BookAdapter(BookListActivity activity, OrderedRealmCollection<Book> data){
        super(data);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.bookName = (TextView) convertView.findViewById(R.id.bookTextView);
            viewHolder.authorName = (TextView) convertView.findViewById(R.id.authorTextView);
            viewHolder.hasRead = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewHolder.hasRead.setOnClickListener(listener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (adapterData != null) {
            Book book = adapterData.get(position);
            viewHolder.bookName.setText(book.getBook_name());
            viewHolder.authorName.setText(book.getAuthor_name());
            viewHolder.hasRead.setChecked(book.hasRead());
            viewHolder.hasRead.setTag(position);
        }
        return convertView;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            if (adapterData != null) {
                Book book = adapterData.get(position);
                activity.changeBookRead(book.getId());
            }
        }
    };
}
