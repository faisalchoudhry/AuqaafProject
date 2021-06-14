package ubaidgul.appdeveloper.auqaf;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ubaidgul.appdeveloper.auqaf.database.DataBaseSQlite;
import ubaidgul.appdeveloper.auqaf.helper.AuqafProperty;
import ubaidgul.appdeveloper.auqaf.helper.AuqafPropertyAdapter;
import ubaidgul.appdeveloper.auqaf.helper.MyDividerItemDecoration;


public class WaqfPropertiesListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AuqafPropertyAdapter.AuqafPropertyAdapterListener {

    private RecyclerView recyclerView;
    private List<AuqafProperty> contactList;
    private AuqafPropertyAdapter mAdapter;
    private SearchView searchView;
    Context context;
    String queryMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_searchable);
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new AuqafPropertyAdapter(this, contactList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);


//        DatabaseHelper databaseHelper = new DatabaseHelper();
        contactList.clear();
        contactList.addAll(DataBaseSQlite.findAll(context));
        mAdapter.notifyDataSetChanged();
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            queryMain = intent.getStringExtra(SearchManager.QUERY);
            mAdapter.getFilter().filter(queryMain);
        }
    }

    @Override
    public boolean onSearchRequested() {
//        pauseSomeStuff();
        return super.onSearchRequested();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchable_activity, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);
        searchView.setQuery(queryMain,false);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        mAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public void onAuqafPropertySelected(AuqafProperty contact) {
        if(contact.getIsSurveyed()==0){
//            Toast.makeText(context,contact.getWaqfPropertyName(),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context,SurveyForm_Industry.class);
            intent.putExtra("auqaf_property", (Parcelable) contact);
            startActivity(intent);
        } else if (contact.getIsSurveyed()==1) {
            Intent intent = new Intent(context,WaqfAttachedPropertiesListActivity.class);
            intent.putExtra("auqaf_property", (Parcelable) contact);
            startActivity(intent);
        }
//
    }
}
