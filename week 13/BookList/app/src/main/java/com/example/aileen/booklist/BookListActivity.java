package com.example.aileen.booklist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.UUID;
import io.realm.Realm;
import io.realm.RealmResults;

public class BookListActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        //get realm instance
        realm = Realm.getDefaultInstance();

        //get all saved Book objects
        RealmResults<Book> books = realm.where(Book.class).findAll();

        final BookAdapter adapter = new BookAdapter(this, books);

        ListView listView = (ListView) findViewById(R.id.book_list);

        //set our RealmBaseAdapter to the listview
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //create a vertical linear layout to hold edit texts
                LinearLayout layout = new LinearLayout(BookListActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final Book book = (Book) adapterView.getAdapter().getItem(i);

                //create edit texts and add to layout
                final EditText bookEditText = new EditText(BookListActivity.this);
                bookEditText.setText(book.getBook_name());
                layout.addView(bookEditText);
                final EditText authorEditText = new EditText(BookListActivity.this);
                authorEditText.setText(book.getAuthor_name());
                layout.addView(authorEditText);

                AlertDialog.Builder dialog = new AlertDialog.Builder(BookListActivity.this);
                dialog.setTitle("Edit Book");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //get updated book and author names
                                final String updatedBookName = bookEditText.getText().toString();
                                final String updatedAuthorName = authorEditText.getText().toString();
                                if(!updatedBookName.isEmpty()) {
                                    changeBook(book.getId(), updatedBookName, updatedAuthorName);
                                }
                            }
                        });
                dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteBook(book.getId());
                            }
                        });
                dialog.create();
                dialog.show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a vertical linear layout to hold edit texts
                LinearLayout layout = new LinearLayout(BookListActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                //create edit texts and add to layout
                final EditText bookEditText = new EditText(BookListActivity.this);
                bookEditText.setHint("Book name");
                layout.addView(bookEditText);
                final EditText authorEditText = new EditText(BookListActivity.this);
                authorEditText.setHint("Author name");
                layout.addView(authorEditText);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(BookListActivity.this);
                dialog.setTitle("Add Book");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get book name entered
                        final String newBookName = bookEditText.getText().toString();
                        final String newAuthorName = authorEditText.getText().toString();

                        if(!newBookName.isEmpty()) {
                            //start realm write transaction
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    //create a realm object
                                    Book newbook = realm.createObject(Book.class, UUID.randomUUID().toString());
                                    newbook.setBook_name(newBookName);
                                    newbook.setAuthor_name(newAuthorName);
                                }
                            });
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //close the Realm instance when the Activity exits
        realm.close();
    }

    public void changeBookRead(final String bookId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book book = realm.where(Book.class).equalTo("id", bookId).findFirst();
                book.setRead(!book.hasRead());
            }
        });
    }

    private void changeBook(final String bookId, final String book_name, final String author_name) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book book = realm.where(Book.class).equalTo("id", bookId).findFirst();
                book.setBook_name(book_name);
                book.setAuthor_name(author_name);
            }
        });
    }

    private void deleteBook(final String bookId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Book.class).equalTo("id", bookId)
                        .findFirst()
                        .deleteFromRealm();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
